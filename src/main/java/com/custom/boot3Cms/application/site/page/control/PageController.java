package com.custom.boot3Cms.application.site.page.control;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.site.page.service.PageService;
import com.custom.boot3Cms.application.site.page.vo.PageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * PageController ( html )
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-06-04 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-06-04 */
@RestController
@Tag(name="Html 페이지 컨트롤러")
public class PageController {

    @Resource(name = "pageService")
    PageService pageService;

    /**
     * HTML 정보 상세보기
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "페이지 정보 GET",description = "Html 페이지 정보를 가져옵니다.")
    @GetMapping(value = "/api/page/{html_seq}")
    public ResultVO pageDetail(@PathVariable("html_seq") String html_seq,
                                   HttpServletRequest request,
                                   HttpServletResponse response,
                                   ModelMap model,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes,
                                   Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO(200);
        boolean result = false;
        int code = 200;
        String rMsg = "";
        try{
            if(CommonUtil.isNumber(html_seq) == false) {
                code = 404;
                rMsg = "페이지를 찾을 수 없습니다.";
            } else {
                PageVO pageVO = new PageVO();
                pageVO.setHtml_seq(html_seq);
                PageVO map = pageService.getPageDetail(pageVO);
                if(map != null){
                    resultVO.putResult("data",map);
                    code = 200;
                    result = true;
                }else{
                    rMsg = "페이지를 찾을 수 없습니다.";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            rMsg = "페이지를 찾을 수 없습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }
}
