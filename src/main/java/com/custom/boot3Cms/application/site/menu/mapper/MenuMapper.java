package com.custom.boot3Cms.application.site.menu.mapper;

import com.custom.boot3Cms.application.site.menu.vo.MenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {

    /**
     * 대메뉴 조회
     * @return
     * @throws Exception
     */
    List<MenuVO> getFirstMenu() throws Exception;

    /**
     * 메뉴 조회
     * 서비스제외, 모든뎁스까지
     * @return
     * @throws Exception
     */
    List<MenuVO> getMenu() throws Exception;

    /**
     * 메뉴 계층형 조회(UP tree)
     * @return
     * @throws Exception
     */
    List<MenuVO> getMenuUptree(MenuVO menuVO) throws Exception;

    /**
     * 메뉴 계층형 조회(Down tree)
     * @return
     * @throws Exception
     */
    List<MenuVO> getMenuDowntree(MenuVO menuVO) throws Exception;


}
