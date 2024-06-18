package com.custom.boot3Cms.application.mng.popup.mapper;

import com.custom.boot3Cms.application.mng.popup.vo.PopupMngVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 관리자 팝업 Mapper
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-06-18 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-06-18 */
@Mapper
public interface PopupMngMapper {

    /**
     * 팝업 list cnt
     * @param popupMngVO
     * @return
     * @throws Exception
     */
    int getPopupListCNT(PopupMngVO popupMngVO) throws Exception;

    /**
     * 팝업 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<PopupMngVO> getPopupList(PopupMngVO vo) throws Exception;

    /**
     * 팝업 상세보기
     * @param popup_seq
     * @return
     * @throws Exception
     */
    PopupMngVO getPopupDetail(PopupMngVO popup_seq) throws Exception;

    /**
     * 팝업 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setPopup(PopupMngVO vo) throws Exception;

    /**
     * 팝업 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updPopup(PopupMngVO vo) throws Exception;

    /**
     * 임시 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delPopup(PopupMngVO vo) throws Exception;

    /**
     * 완전 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int deletePopup(PopupMngVO vo) throws Exception;

	/**
	 * 메인화면 팝업 출력
	 * @return
	 * @throws Exception
	 */
	List<PopupMngVO> showPopup() throws Exception;
}
