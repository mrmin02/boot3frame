package com.custom.boot3Cms.application.mng.code.controller;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.mng.code.service.CodeMngService;
import com.custom.boot3Cms.application.mng.code.vo.CodeMngVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Map;

/**
 * 코드 관리 Controller
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-06-20 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-06-20 */
@Tag(name = "코드 관리 컨트롤러")
@RestController
public class CodeMngController {

    @Resource(name = "codeMngService")
    CodeMngService codeService;

    /**
     * 코드 리스트
     *
     * @param codeVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     **/
    @Operation(summary = "코드 리스트")
    @GetMapping("/api/mng/code/list")
    public ResultVO codeList(@ModelAttribute("CodeVO") CodeMngVO codeVO
            , HttpServletRequest request
            , HttpServletResponse response ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            resultVO.putResult("list",codeService.getCodeList(codeVO));
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "코드 리스트 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }


    /**
     * 코드 중복 체크
     *
     * @param codeVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "코드 중복체크")
    @PostMapping("/api/mng/code/check")
    public ResultVO codeCheck(@RequestBody CodeMngVO codeVO
            , HttpServletRequest request
            , HttpServletResponse response ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            Map<String,Object> map = codeService.getCodeCheck(codeVO);
            if((boolean) map.get("result")){
                result = true;
            }
            rMsg = String.valueOf(map.get("rMsg"));
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "코드 중복체크 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }


    /**
     * 코드 등록
     *
     * @param codeVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "코드 등록")
    @PostMapping("/api/mng/code/proc")
    public ResultVO codeInsert(@RequestBody CodeMngVO codeVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                codeVO.setInpt_seq(vo.getUser_seq());
                codeVO.setUpd_seq(vo.getUser_seq());
            }
            codeVO.setFlag("c");
            Map<String,Object> map = codeService.codeProc(codeVO);
            if((boolean) map.get("result")){
                result = true;
            }
            rMsg = String.valueOf(map.get("rMsg"));
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "코드 등록 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }


    /**
     * 코드 수정
     *
     * @param codeVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "코드 수정")
    @PutMapping("/api/mng/code/proc")
    public ResultVO codeUpdate(@RequestBody CodeMngVO codeVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                codeVO.setInpt_seq(vo.getUser_seq());
                codeVO.setUpd_seq(vo.getUser_seq());
            }
            codeVO.setFlag("u");
            Map<String,Object> map = codeService.codeProc(codeVO);
            if((boolean) map.get("result")){
                result = true;
            }
            rMsg = String.valueOf(map.get("rMsg"));
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "코드 수정 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 코드 수정
     *
     * @param codeVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "코드 삭제")
    @DeleteMapping("/api/mng/code/proc")
    public ResultVO codeDelete(@RequestBody CodeMngVO codeVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                codeVO.setInpt_seq(vo.getUser_seq());
                codeVO.setUpd_seq(vo.getUser_seq());
            }
            if(codeVO.getFlag() == null || !codeVO.getFlag().equals("dp")) {
                codeVO.setFlag("d");
            }

            Map<String,Object> map = codeService.codeProc(codeVO);
            if((boolean) map.get("result")){
                result = true;
            }
            rMsg = String.valueOf(map.get("rMsg"));
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "코드 삭제 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 코드 NAME 출력 AJAX
     *
     * @param codeVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "코드 이름 조회", description = "코드로 코드 이름 조회")
    @GetMapping("/api/code/text")
    public ResultVO codeToNameAjax(@ModelAttribute("CodeVO") CodeMngVO codeVO
            , HttpServletRequest request
            , HttpServletResponse response ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            resultVO.putResult("data",String.valueOf(codeService.getCodeToText(codeVO.getCode())));
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "코드 이름 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }


    /**
     * 코드 상세정보 출력
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "코드 상세정보")
    @GetMapping("/api/mng/code/detail/{code}")
    public ResultVO codeDetailAjax(@RequestParam("code") String code_str
            , HttpServletRequest request
            , HttpServletResponse response ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            CodeMngVO codeVO = new CodeMngVO();
            codeVO.setCode(code_str);

            resultVO.putResult("data",codeService.getCodeDetail(codeVO));
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "코드 정보 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

}
