package com.custom.boot3Cms.application.mng.html.control;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.mng.bbs.service.BbsMngService;
import com.custom.boot3Cms.application.mng.html.service.HtmlMngService;
import com.custom.boot3Cms.application.mng.html.vo.HtmlMngVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * HTML 페이지 관리 Controller
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
@Tag(name = "Html 관리 컨트롤러")
@RestController
public class HtmlMngController {

    @Resource(name = "htmlMngService")
    HtmlMngService htmlMngService;


    /**
     * Html 조회 (List)
     *
     * @param htmlMngVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     **/
    @Operation(summary = "Html list 조회")
    @GetMapping("/api/mng/html/list")
    public ResultVO htmlList(@ModelAttribute("HtmlMngVO") HtmlMngVO htmlMngVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal
    ) throws Exception {

        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null) {
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                htmlMngVO.setInpt_seq(vo.getUser_seq());
                htmlMngVO.setUpd_seq(vo.getUser_seq());
                htmlMngVO.setPageIndex(htmlMngVO.getPageIndex());
                resultVO.putResult("total_cnt",htmlMngService.getHtmlListCNT(htmlMngVO));
                resultVO.putResult("list",htmlMngService.getHtmlList(htmlMngVO));
                result = true;
            }else{
                rMsg = "로그인 정보가 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "Html 리스트 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }
    
    /**
     * Html 상세보기 (One)
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "Html 상세보기")
    @GetMapping("/api/mng/html/detail/{html_seq}")
    public ResultVO htmlDetail( @PathVariable("html_seq") String html_seq
            , HttpServletRequest request
            , HttpServletResponse response )throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            Map<String, Object> map = htmlMngService.getHtmlDetail(new HtmlMngVO(html_seq));
            if((boolean)map.get("result")){
                result = true;
                resultVO.putResult("data",map.get("value"));
            }else{
                rMsg = map.get("rMsg").toString();
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "Html 정보 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }


    /**
     * Html 등록/수정 PROC
     *
     * @param htmlMngVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "Html 등록")
    @PostMapping("/api/mng/html/proc")
    public ResultVO htmlProc(@RequestBody HtmlMngVO htmlMngVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal )throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                htmlMngVO.setInpt_seq(vo.getUser_seq());
                htmlMngVO.setUpd_seq(vo.getUser_seq());
            }
            htmlMngVO.setFlag("c");
            Map<String, Object> map = htmlMngService.htmlProc(htmlMngVO);
            if((boolean)map.get("result")){
                result = true;
                resultVO.putResult("data",map.get("value"));
            }
            rMsg = map.get("rMsg").toString();

        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "Html 등록 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * Html 수정
     * @param htmlMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "Html 수정")
    @PutMapping("/api/mng/html/proc")
    public ResultVO htmlUpdateProc(@RequestBody HtmlMngVO htmlMngVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal )throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                htmlMngVO.setInpt_seq(vo.getUser_seq());
                htmlMngVO.setUpd_seq(vo.getUser_seq());
            }
            htmlMngVO.setFlag("u");
            Map<String, Object> map = htmlMngService.htmlProc(htmlMngVO);
            if((boolean)map.get("result")){
                result = true;
                resultVO.putResult("data",map.get("value"));
            }
            rMsg = map.get("rMsg").toString();

        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "Html 수정 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * Html 삭제
     * @param htmlMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "Html 삭제")
    @DeleteMapping("/api/mng/html/proc")
    public ResultVO htmlDeleteProc(@RequestBody HtmlMngVO htmlMngVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal )throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                htmlMngVO.setInpt_seq(vo.getUser_seq());
                htmlMngVO.setUpd_seq(vo.getUser_seq());
            }
            htmlMngVO.setFlag("d");
            Map<String, Object> map = htmlMngService.htmlProc(htmlMngVO);
            if((boolean)map.get("result")){
                result = true;
                resultVO.putResult("data",map.get("value"));
            }
            rMsg = map.get("rMsg").toString();

        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "Html 수정 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * HTML_SEQ로 HTML 존재여부 확인
     * @param html_seq
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "Html 존재여부 확인", description = "메뉴 관리에서 html_seq 로 html 존재여부 확인")
    @GetMapping("/api/mng/html/check/{html_seq}")
    public ResultVO menuLinkList( @PathVariable("html_seq") String html_seq
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal) throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            HtmlMngVO htmlMngVO = new HtmlMngVO();
            htmlMngVO.setHtml_seq(html_seq);

            resultVO.putResult("data",htmlMngService.getHtmlCnt(htmlMngVO));
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "Html 존재여부 확인 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

}
