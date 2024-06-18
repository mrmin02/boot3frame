package com.custom.boot3Cms.application.mng.html.service;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.mng.html.mapper.HtmlMngMapper;
import com.custom.boot3Cms.application.mng.html.vo.HtmlMngVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTML 관리 Service
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
@Transactional
@Service("htmlMngService")
public class HtmlMngService {

    @Resource(name = "htmlMngMapper")
    HtmlMngMapper htmlMngMapper;

    public int getHtmlListCNT(HtmlMngVO vo) throws Exception {
        return htmlMngMapper.getHtmlListCNT(vo);
    }

    /**
     * Html 조회 (List)
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public List<HtmlMngVO> getHtmlList(HtmlMngVO vo) throws Exception {
        return htmlMngMapper.getHtmlList(vo);
    }

    /**
     * Html 상세보기 (One)
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getHtmlDetail(HtmlMngVO vo) throws Exception {
        return CommonUtil.fn_getDetail(htmlMngMapper.getHtmlDetail(vo));
    }


    /**
     * Html 등록, 수정, 삭제 Proc
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> htmlProc(HtmlMngVO vo) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String rMsg = "";
        String rHeader = "";
        boolean result = false;

        // 플래그 d일 경우 HTML Delete
        if("d".equals(vo.getFlag())) {
            result = htmlMngMapper.delHtml(vo) > 0;
            rHeader = result ? "알림!" : "에러!";
            rMsg = result ? "HTML 삭제가 완료되었습니다." : "HTML 삭제 중 오류가 발생했습니다.<br/>잠시 후 다시 시도하거나, 현상이 반복 될 경우 개발사에 문의 바랍니다.";

        // 플래그 c일 경우 Html Set
        }else if ("c".equals(vo.getFlag())) {
            result = htmlMngMapper.setHtml(vo) > 0;
            rHeader = result ? "알림!" : "에러!";
            rMsg = result ? "HTML 등록이 완료되었습니다." : "HTML 등록 중 오류가 발생했습니다.<br/>잠시 후 다시 시도하거나, 현상이 반복 될 경우 개발사에 문의 바랍니다.";

        // 플래그 u일 경우 Html Update
        }else if("u".equals(vo.getFlag())) {
            result = htmlMngMapper.updHtml(vo) > 0;
            rHeader = result ? "알림!" : "에러!";
            rMsg = result ? "HTML 수정이 완료되었습니다." : "HTML 수정 중 오류가 발생했습니다.<br/>잠시 후 다시 시도하거나, 현상이 반복 될 경우 개발사에 문의 바랍니다.";

        // 플래그 없는 경우
        }else {
            rHeader = "알림!";
            rMsg = "잘못 된 접근입니다.";
        }

        map.put("rHeader", rHeader);
        map.put("rMsg", rMsg);
        map.put("result", result);
        return map;
    }

    /**
     * HTML_SEQ로 HTML 존재여부 확인
     * @param vo
     * @return
     * @throws Exception
     */
    public String getHtmlCnt(HtmlMngVO vo) throws Exception {
        return htmlMngMapper.getHtmlCnt(vo);
    }

}
