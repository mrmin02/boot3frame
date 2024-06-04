package com.custom.boot3Cms.application.site.menu.service;

import com.custom.boot3Cms.application.site.menu.mapper.MenuMapper;
import com.custom.boot3Cms.application.site.menu.vo.MenuVO;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Menu API
 */
@Service("menuService")
public class MenuService {

    @Resource(name = "menuMapper")
    MenuMapper menuMapper;

    public Map<String,Object> getFirstMenu() throws Exception{
        Map<String,Object> rtnMap = new HashMap<>();
        boolean result = false;
        String rMsg = "";

        try{
            List<MenuVO> menuList = menuMapper.getFirstMenu();
            if(menuList != null && !menuList.isEmpty()){
                rtnMap.put("topMenu",menuList);
                result = true;
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
        }

        rtnMap.put("result",result);
        rtnMap.put("rMsg",rMsg);
        return rtnMap;
    }

    /**
     * 메뉴 조회
     * @return
     * @throws Exception
     */
    public Map<String,Object> getMenuList() throws Exception{

        Map<String,Object> rtnMap = new HashMap<>();
        boolean result = true;
        String rMsg = "";

        try{
            List<MenuVO> topMenuList = menuMapper.getFirstMenu();
            rtnMap.put("topMenu", topMenuList);

            /**
             * React 메뉴를 위한 메뉴 가공
             * {
             *      { submenus.. { ... 3dept : {}} }, { submenus.. { ... 3dept : {}} }, { submenus.. { ... 3dept : {}} }
             * }
             */
            List<MenuVO> menuList = menuMapper.getMenu();
            JSONArray jsonArr = new JSONArray();

            for( int i = 0; i < topMenuList.size(); i++){
                List<MenuVO> tmpList = menuList;
                List<MenuVO> removeList = new ArrayList<>();
                JSONArray menuGroup = new JSONArray();
                boolean nextMenu = false;

                int index = 0;
                int menuGroupIdx = 0;

                List<MenuVO> tmpSubMenu = new ArrayList<>(); // 3depth 메뉴 저장
                int prt_index = -1; // 3depth 메뉴가 있는 2depth 메뉴의 index
                boolean haveSubMenu = false;
                for ( MenuVO item : tmpList ){
                    if(item.getMenu_depth().equals("0")){
                        if(nextMenu){
                            break;
                        }else {
                            // menuGroup.add(item);
                            nextMenu = true;
                        }
                    }else{
                        // 3depth 메뉴일 경우, 부모 2depth 의 idx 값을 저장하고, tmpSubMenu array 에 같은 형제의 3depth 메뉴를 저장
                        if(item.getMenu_depth().equals("2")){
                            tmpSubMenu.add(item);
                            if(!haveSubMenu){
                                prt_index = menuGroupIdx -1;     // 2depth 부모의 index 값 세팅
                            }
                            haveSubMenu = true;
                        }else{
                                // 바로 이전의 메뉴가 3depth 의 마지막 메뉴라면 아래의 로직으로 이전 2depth 메뉴 수정
                                if(haveSubMenu){
                                    MenuVO tmpVO = (MenuVO) menuGroup.get(prt_index);
                                    tmpVO.setSub_menu_list(tmpSubMenu);
                                    menuGroup.set(prt_index,tmpVO);
                                    haveSubMenu = false;
                                    tmpSubMenu = new ArrayList<>();;
                                }

                                // 2dept이면서 분류 항목이면 3dpeth 의 링크정보를 가져와서 세팅
                                if(item.getMenu_depth().equals("1") && item.getMenu_link().equals("#")){
                                    // 1depth 중에서 분류 항목인 경우 (url = # ), 2depth 의 항목의 link 를 할당.
                                    MenuVO tmpMenu = tmpList.get(index);
                                    if(tmpMenu.getMenu_depth().equals("2")){
                                        item.setMenu_link(tmpMenu.getMenu_link());
                                        item.setMenu_link_type(tmpMenu.getMenu_link_type());
                                        item.setMenu_type(tmpMenu.getMenu_type());
                                    }
                                }
                                if(item.getMenu_type().equals("MNT_002")){ // 게시판일 경우,
                                    item.setMenu_link("/article/"+item.getMenu_link()+"/list");
                                }
                                menuGroup.add(item);
                                menuGroupIdx++; // menuGroupIdx 의 index 값
                            }
                        }
//                    }
                    removeList.add(item); // array 에 추가한 항목들은 삭제할 List 목록에 임시 저장
                    index++; // tmpList 의 index 값

                    if( index == tmpList.size()){
                        // 현재 메뉴가 마지막 메뉴라면.. 3depth 처리를 다음메뉴 loop 에서 처리하므로 로직 추가
                        if(!tmpSubMenu.isEmpty()){
                            MenuVO tmpVO = (MenuVO) menuGroup.get(prt_index);
                            tmpVO.setSub_menu_list(tmpSubMenu);
                            menuGroup.set(prt_index,tmpVO);
                        }
                    }
                }
                tmpList.removeAll(removeList); // 이미 array 에 추가한 항목들 삭제 ( loop 시행횟수 줄이기 위함 )
                jsonArr.add(menuGroup); //json array 에 추가
            }
            rtnMap.put("menu",jsonArr);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
        }

        rtnMap.put("result",result);
        rtnMap.put("rMsg",rMsg);
        return rtnMap;
    }

    /**
     * 메뉴 계층형 조회(UP tree)
     * @param menuVO
     * @return
     * @throws Exception
     */
    public Map<String,Object> getMenuUptree(MenuVO menuVO) throws Exception{
        Map<String,Object> rtnMap = new HashMap<>();
        boolean result = true;
        String rMsg = "";
        try{
            List<MenuVO> list = menuMapper.getMenuUptree(menuVO);
            if (list.isEmpty()) {
                result = false;
                rMsg = "Empty up tree menu";
            } else {
                rtnMap.put("list", list);
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
        }

        rtnMap.put("result",result);
        rtnMap.put("rMsg",rMsg);
        return rtnMap;
    }

    /**
     * 메뉴 계층형 조회(Down tree)
     * @param menuVO
     * @return
     * @throws Exception
     */
    public Map<String,Object> getMenuDowntree(MenuVO menuVO) throws Exception{
        Map<String,Object> rtnMap = new HashMap<>();
        boolean result = true;
        String rMsg = "";

        try{
            List<MenuVO> list = menuMapper.getMenuDowntree(menuVO);
            if(list.isEmpty()){
                result = false;
                rMsg = "Empty down tree menu";
            }else{
                rtnMap.put("list",list);
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
        }

        rtnMap.put("result",result);
        rtnMap.put("rMsg",rMsg);
        return rtnMap;
    }


}
