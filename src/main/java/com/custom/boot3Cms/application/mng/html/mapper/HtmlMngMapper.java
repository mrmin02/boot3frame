package com.custom.boot3Cms.application.mng.html.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.custom.boot3Cms.application.mng.html.vo.HtmlMngVO;

import java.util.List;

/**
 * Html 관리 Mapper
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
public interface HtmlMngMapper {

    /**
     * html 조회 (list) cnt
     * @param htmlMngVO
     * @return
     * @throws Exception
     */
    int getHtmlListCNT(HtmlMngVO htmlMngVO) throws Exception;

    /**
     * Html 조회 (List)
     * @param vo
     * @return
     * @throws Exception
     */
    List<HtmlMngVO> getHtmlList(HtmlMngVO vo) throws Exception;

    /**
     * Html 상세보기 (One)
     * @param vo
     * @return
     * @throws Exception
     */
    HtmlMngVO getHtmlDetail(HtmlMngVO vo) throws Exception;

    /**
     * Html 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setHtml(HtmlMngVO vo) throws Exception;

    /**
     * Html 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updHtml(HtmlMngVO vo) throws Exception;

    /**
     * Html 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delHtml(HtmlMngVO vo) throws Exception;

    /**
     * HTML_SEQ로 HTML 존재여부 확인
     * @param vo
     * @return
     * @throws Exception
     */
    String getHtmlCnt(HtmlMngVO vo) throws Exception;




}
