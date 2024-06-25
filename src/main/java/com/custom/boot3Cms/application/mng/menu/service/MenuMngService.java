package com.custom.boot3Cms.application.mng.menu.service;

import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.menu.mapper.MenuMngMapper;
import com.custom.boot3Cms.application.mng.menu.vo.MenuMngVO;
import com.custom.boot3Cms.application.mng.user.vo.UserVO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 메뉴 관리 Service
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-06-25 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-06-25 */
@Service("menuMngService")
public class MenuMngService {

    @Autowired
    MenuMngMapper menuMngMapper;

    /**
     * 메뉴 정보 목록
     * @param vo
     * @return
     * @throws Exception
     */
    public List<MenuMngVO> getMenuInfoList(MenuMngVO vo) throws Exception {
        List<MenuMngVO> rtnList = new ArrayList<>();
        menuMngMapper.getMenuInfoList(vo).forEach(v->{
            try {
                v.setUserVOList(menuMngMapper.getMenuAdminList(v));
            } catch (Exception e) {
                e.printStackTrace();
            }
            rtnList.add(v);
        });
        return rtnList;
    }

    /**
     * 메뉴 정보 상세보기
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getMenuInfoDetail(MenuMngVO vo) throws Exception{
        return CommonUtil.fn_getDetail(menuMngMapper.getMenuInfoDetail(vo));
    }

    /**
     * 메뉴 정보 관리자 목록 조회
     * @param vo
     * @return
     * @throws Exception
     */
    public List<UserVO> getMenuAdminList(MenuMngVO vo) throws Exception{
        return menuMngMapper.getMenuAdminList(vo);
    }

    /**
     * 메뉴 정보 등록
     * @param vo
     * @return
     * @throws Exception
     */
    @Transactional
    public Map<String, Object> setMenuInfo(MenuMngVO vo) throws Exception{
        Map<String, Object> map = new HashMap<>();
        String rMsg = "";
        String rHeader = "";
        boolean result = false;
        if(StringUtil.isEmpty(vo.getMenu_info_seq())){
            result = menuMngMapper.setMenuInfo(vo) > 0;
            // 메뉴 정보 등록에 성공 할 경우
            if(result){
                // 메뉴 정보 관리자 등록
                boolean adminResult = false;
                for(String user_seq : vo.getUser_seq().split(",")){
                    vo.setUser_seq(user_seq);
                    if(menuMngMapper.setMenuInfoAdmin(vo) > 0){
                        adminResult = true;
                    }else{
                        adminResult = false;
                        break;
                    }
                }
                // 메뉴 정보 관리자 등록에 성공할 경우
                if(adminResult){
                    rHeader = "알림!";
                    rMsg = "메뉴 정보 등록이 완료되었습니다.";
                }else{
                    // 메뉴 정보 물리삭제
                    result = menuMngMapper.delMenuInfoReal(vo) > 0;
                    if(result){
                        rHeader = "에러!";
                        rMsg = "메뉴 정보 등록에 실패하였습니다.<br/>메뉴 정보 관리자 정보를 등록하지 않았거나, 메뉴 정보 관리자 정보 등록에 실패하였습니다.";
                    }else{
                        rHeader = "에러!";
                        rMsg = "메뉴 정보 등록에 실패하였습니다.<br/>(메뉴 정보 더미 데이터 삭제에 실패하였습니다.)";
                        // 이 이후의 처리는 에러로 간주함.
                    }
                    result = false;
                }
            }else{
                rHeader = "에러!";
                rMsg = "메뉴 정보 등록에 실패하였습니다.<br/>메뉴 정보 등록 중 오류가 발생했습니다.";
            }
        }else{
            rHeader = "에러!";
            rMsg = "잘못된 접근입니다.";
        }
        if(!result){
            vo.setMenu_info_seq(null);
            vo.setFlag("c");
        }
        map.put("rHeader", rHeader);
        map.put("rMsg", rMsg);
        map.put("result", result);
        return map;
    }

    /**
     * 메뉴 정보 수정
     * @param vo
     * @return
     * @throws Exception
     */
    @Transactional
    public Map<String, Object> updMenuInfo(MenuMngVO vo) throws Exception{
        Map<String, Object> map = new HashMap<>();
        String rMsg = "";
        String rHeader = "";
        boolean result = false;
        if(StringUtil.isNotEmpty(vo.getMenu_info_seq()) && "u".equals(vo.getFlag())){
            // 수정
            // 메뉴 정보 임시 저장
            MenuMngVO tempMenuMngVO = menuMngMapper.getMenuInfoDetail(vo);
            // 메뉴 정보 수정
            result = menuMngMapper.updMenuInfo(vo) > 0;
            // 메뉴 정보 수정에 성공 할 경우
            if(result){
                // 메뉴 정보 관리자 정보 임시저장
                List<UserVO> tempAdminList = menuMngMapper.getMenuAdminList(vo);
                // 메뉴 정보 관리자 정보 물리삭제
                result = menuMngMapper.delMenuInfoAdminReal(vo) > 0;
                // 메뉴 정보 관리자 정보 물리삭제에 성공할 경우
                if(result){
                    // 메뉴 정보 관리자 등록
                    boolean adminResult = false;
                    for(String user_seq : vo.getUser_seq().split(",")){
                        vo.setUser_seq(user_seq);
                        if(menuMngMapper.setMenuInfoAdmin(vo) > 0){
                            adminResult = true;
                        }else{
                            adminResult = false;
                            break;
                        }
                    }
                    if(adminResult){
                        rHeader = "알림!";
                        rMsg = "메뉴 정보 수정이 완료되었습니다.";
                    }else{
                        // 관리자 정보 복원
                        boolean rollbackAdminResult = false;
                        for(UserVO userVO : tempAdminList){
                            vo.setUser_seq(userVO.getUser_seq());
                            if(menuMngMapper.setMenuInfoAdmin(vo) > 0){
                                rollbackAdminResult = true;
                            }else{
                                rollbackAdminResult = false;
                                break;
                            }
                        }
                        // 메뉴 정보 복원
                        result = menuMngMapper.updMenuInfo(tempMenuMngVO) > 0;
                        if(result){
                            rHeader = "에러!";
                            rMsg = "메뉴 정보 수정에 실패하였습니다.<br/>메뉴 정보 관리자 정보를 등록하지 않았거나, 등록 중 오류가 발생했습니다.";
                        }else{
                            rHeader = "에러!";
                            rMsg = "메뉴 정보 수정에 실패하였습니다.<br/>"+(rollbackAdminResult ? "메뉴 정보" : "메뉴 정보 관리자 정보")+" 복원 중 오류가 발생했습니다.";
                        }
                    }
                }else{
                    // 메뉴 정보 복원
                    result = menuMngMapper.updMenuInfo(tempMenuMngVO) > 0;
                    if(result){
                        rHeader = "에러!";
                        rMsg = "메뉴 정보 수정에 실패하였습니다.<br/>메뉴 정보 관리자 정보 삭제 중 오류가 발생했습니다.";
                    }else{
                        rHeader = "에러!";
                        rMsg = "메뉴 정보 수정에 실패하였습니다.<br/>메뉴 정보 복원 중 오류가 발생했습니다.";
                    }
                }
            }else{
                rHeader = "에러!";
                rMsg = "메뉴 정보 수정에 실패하였습니다.<br/>메뉴 정보 수정 중 오류가 발생했습니다.";
            }
        }else if(StringUtil.isNotEmpty(vo.getMenu_info_seq()) && "d".equals(vo.getFlag())){
            // 삭제
            result = menuMngMapper.delMenuInfo(vo) > 0;
            rHeader = result ? "알림!" : "에러!";
            rMsg = result ? "메뉴 정보 삭제에 성공하였습니다." : "메뉴 정보 삭제에 실패하였습니다.";
        }else{
            rHeader = "에러!";
            rMsg = "잘못된 접근입니다.";
        }
        map.put("rHeader", rHeader);
        map.put("rMsg", rMsg);
        map.put("result", result);
        return map;
    }

    /**
     * 메뉴 목록
     * @param vo
     * @return
     * @throws Exception
     */
    public List<MenuMngVO> getMenu(MenuMngVO vo) throws Exception{
        return menuMngMapper.getMenu(vo);
    }

    /**
     * 메뉴 타입별 링크목록 GET
     * @param vo
     * @return
     * @throws Exception
     */
    public List<MenuMngVO> getLinkList(MenuMngVO vo) throws Exception{
        List<MenuMngVO> rtnList = new ArrayList<>();
        if("MNT_002".equals(vo.getMenu_type())){
            rtnList = menuMngMapper.getSiteBbsList(vo);
        }else if("MNT_003".equals(vo.getMenu_type())){
            rtnList = menuMngMapper.getSiteHtmlList(vo);
        }
        // FIXME 메뉴 타입 추가 시 else if 문 추가..
        return rtnList;
    }

    /**
     * 메뉴 등록
     * @param vo
     * @param inpt_seq
     * @throws Exception
     */
    @Transactional
    public void setMenu(MenuMngVO vo, String inpt_seq) throws Exception{
        // 메뉴 삭제
        vo.setMenu_json(vo.getMenu_json().replaceAll("&amp;quot;", "\"").replaceAll("&quot;", "\""));
        menuMngMapper.delMenu(vo);
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(vo.getMenu_json());
        // 메뉴 등록 (재귀)
        menuStream(jsonArray, vo, inpt_seq);
        menuMngMapper.updMenuJson(vo);
    }

    public void menuStream(JSONArray jsonArray, MenuMngVO menuMngVO, String inpt_seq) throws Exception{
        JSONParser parser = new JSONParser();
        for(Object o : jsonArray){
            JSONObject objMap = (JSONObject) parser.parse(o.toString());
            menuMngVO.setMenu_seq(""+objMap.get("id"));
            menuMngVO.setMenu_type(""+objMap.get("menu_type"));
            menuMngVO.setMenu_link_type(""+objMap.get("menu_link_type"));
            if((""+objMap.get("menu_type")).equals("MNT_002")){
                menuMngVO.setMenu_link("/article/"+objMap.get("menu_link"));
            }else if((""+objMap.get("menu_type")).equals("MNT_005")){
                menuMngVO.setMenu_link("/picture-book/"+objMap.get("menu_link"));
            }else if((""+objMap.get("menu_type")).equals("MNT_003")){
                menuMngVO.setMenu_link("/page/"+objMap.get("menu_link"));
            }else if((""+objMap.get("menu_type")).equals("MNT_007")){ // 수상 정보
                menuMngVO.setMenu_link("/award/info/"+objMap.get("menu_link"));
            }else if((""+objMap.get("menu_type")).equals("MNT_008")){ // 추천 정보
                menuMngVO.setMenu_link("/recommend/info/"+objMap.get("menu_link"));
            }else if((""+objMap.get("menu_type")).equals("MNT_009")){ // 수상 정보 그림책 목록
                menuMngVO.setMenu_link("/book/award/"+objMap.get("menu_link")+"/list");
            }else if((""+objMap.get("menu_type")).equals("MNT_010")){ // 추천 정보 그림책 목록
                menuMngVO.setMenu_link("/book/recommend/"+objMap.get("menu_link")+"/list");
            }else if((""+objMap.get("menu_type")).equals("MNT_011")){ // 연령별 정보 그림책 목록
                menuMngVO.setMenu_link("/book/age/"+objMap.get("menu_link")+"/list");
            }else{
                menuMngVO.setMenu_link(""+objMap.get("menu_link"));
            }
            menuMngVO.setMenu_title(""+objMap.get("menu_title"));
            menuMngVO.setShow_yn(""+objMap.get("show_yn"));
            menuMngVO.setHighlight_yn((objMap.get("highlight_yn").equals("Y") ? "Y" : "N"));
            menuMngVO.setUse_yn(""+objMap.get("use_yn"));
            menuMngVO.setRemark(""+objMap.get("remark"));
            menuMngVO.setPrt_seq(""+objMap.get("parent"));
            menuMngVO.setMenu_order(""+objMap.get("order"));
            menuMngVO.setMenu_depth(""+objMap.get("depth"));
            menuMngVO.setInpt_seq(inpt_seq);
            menuMngMapper.setMenu(menuMngVO);
            if(objMap.get("children") != null){
                JSONArray childArray = (JSONArray) parser.parse(objMap.get("children").toString());
                menuStream(childArray, menuMngVO, inpt_seq);
            }
        }
    }
}
