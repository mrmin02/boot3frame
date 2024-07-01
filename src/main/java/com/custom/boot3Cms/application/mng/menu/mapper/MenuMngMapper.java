package com.custom.boot3Cms.application.mng.menu.mapper;

import com.custom.boot3Cms.application.mng.menu.vo.MenuMngVO;
import com.custom.boot3Cms.application.mng.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 메뉴 관리 Mapper
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
@Mapper
public interface MenuMngMapper {

    /**
     * 메뉴 정보 목록 CNT
     * @param menuMngVO
     * @return
     * @throws Exception
     */
    int getMenuInfoListCNT(MenuMngVO menuMngVO) throws Exception;

    /**
     * 메뉴 정보 목록
     *
     * @param vo
     * @return
     * @throws Exception
     */
    List<MenuMngVO> getMenuInfoList(MenuMngVO vo) throws Exception;

    /**
     * 메뉴 관리자 목록
     *
     * @param vo
     * @return
     * @throws Exception
     */
    List<UserVO> getMenuAdminList(MenuMngVO vo) throws Exception;

    /**
     * 메뉴 정보 상세보기
     *
     * @param vo
     * @return
     * @throws Exception
     */
    MenuMngVO getMenuInfoDetail(MenuMngVO vo) throws Exception;

    /**
     * 메뉴 정보 등록
     *
     * @param vo
     * @return
     * @throws Exception
     */
    int setMenuInfo(MenuMngVO vo) throws Exception;

    /**
     * 메뉴 정보 관리자 등록
     *
     * @param vo
     * @return
     * @throws Exception
     */
    int setMenuInfoAdmin(MenuMngVO vo) throws Exception;

    /**
     * 메뉴 정보 수정
     *
     * @param vo
     * @return
     * @throws Exception
     */
    int updMenuInfo(MenuMngVO vo) throws Exception;

    /**
     * 메뉴 정보 삭제 (논리)
     *
     * @param vo
     * @return
     * @throws Exception
     */
    int delMenuInfo(MenuMngVO vo) throws Exception;

    /**
     * 메뉴 정보 삭제 (물리)
     *
     * @param vo
     * @return
     * @throws Exception
     */
    int delMenuInfoReal(MenuMngVO vo) throws Exception;

    /**
     * 메뉴 관리자 정보 삭제 (물리)
     *
     * @param vo
     * @return
     * @throws Exception
     */
    int delMenuInfoAdminReal(MenuMngVO vo) throws Exception;

    /**
     * 메뉴 Json
     *
     * @param vo
     * @return
     * @throws Exception
     */
    MenuMngVO getMenuJson(MenuMngVO vo) throws Exception;

    /**
     * 메뉴 json 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setMenuJson(MenuMngVO vo) throws Exception;

    /**
     * 메뉴 등록
     *
     * @param vo
     * @return
     * @throws Exception
     */
    int setMenu(MenuMngVO vo) throws Exception;

    /**
     * 메뉴 삭제
     *
     * @param vo
     * @return
     * @throws Exception
     */
    int delMenu(MenuMngVO vo) throws Exception;

    /**
     * 사이트 게시판 목록
     *
     * @param vo
     * @return
     * @throws Exception
     */
    List<MenuMngVO> getSiteBbsList(MenuMngVO vo) throws Exception;

    /**
     * 사이트 HTML 목록
     *
     * @param vo
     * @return
     * @throws Exception
     */
    List<MenuMngVO> getSiteHtmlList(MenuMngVO vo) throws Exception;


    /**
     * 최상위 JSON을 제외한 자식 JSON NULL 처리 (속도문제)
     *
     * @param vo
     * @return
     * @throws Exception
     */
    int updMenuJson(MenuMngVO vo) throws Exception;

    /**
     * 기관관리 수정시 메뉴반영
     *
     * @param vo
     * @return
     * @throws Exception
     */
    int updMenuUseYN(MenuMngVO vo) throws Exception;

}
