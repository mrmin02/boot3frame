package com.custom.boot3Cms.application.mng.bbs.control;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.bbs.service.BbsMngService;
import com.custom.boot3Cms.application.mng.bbs.vo.BbsMngVO;
import com.custom.boot3Cms.application.mng.code.service.CodeMngService;
import com.custom.boot3Cms.application.mng.code.vo.CodeMngVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

/**
 * 게시판 관리 컨트롤러
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-05-12 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-05-12 */
@Tag(name = "게시판 관리 컨트롤러")
@RestController
public class BbsMngController {

    //게시판 관리 서비스
    @Resource(name = "bbsMngService")
    private BbsMngService bbsMngService;

    //코드 관리 서비스
    @Resource(name = "codeMngService")
    private CodeMngService codeService;

    /**
     * 게시판 목록
     *
     * @param bbsMngVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시판 리스트")
    @GetMapping("/api/mng/bbs/list")
    public ResultVO bbsList(@ModelAttribute BbsMngVO bbsMngVO
            , HttpServletRequest request
            , HttpServletResponse response
    ) throws Exception {

        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            resultVO.putResult("total_cnt", bbsMngService.getBbsListCNT(bbsMngVO));
            resultVO.putResult("list", bbsMngService.getBbsList(bbsMngVO));
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "게시판 목록 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 게시판 코드 중복검사
     *
     * @param bbsMngVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시판 코드 중복검사")
    @PostMapping("/api/mng/bbs/code/check")
    public ResultVO checkBbsCode(@RequestBody BbsMngVO bbsMngVO
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
    ) throws Exception {

        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            resultVO.putResult("data", bbsMngService.checkBbsCode(bbsMngVO));
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "게시판 코드 중복검사 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 게시판 정보
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시판 상세정보")
    @GetMapping("/api/mng/bbs/info")
    public ResultVO bbsForm(@RequestParam String bbs_info_seq
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
    ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(StringUtil.isNotEmpty(bbs_info_seq)){
                Map<String, Object> map = bbsMngService.getBbsDetail(new BbsMngVO(bbs_info_seq));
                if((boolean)map.get("result")){
                    BbsMngVO vo = (BbsMngVO) map.get("value");
                    if(vo != null){
                        resultVO.putResult("data", vo);
                        resultVO.putResult("authList", bbsMngService.getBbsAuth(vo));
                        if("Y".equals(vo.getCategory_use_yn())){
                            resultVO.putResult("categoryList", bbsMngService.getBbsCategory(vo));
                        }
                        if("Y".equals(vo.getAttach_file_use_yn())){
                            resultVO.putResult("attachFileList", bbsMngService.getBbsFileInfo(vo));
                        }
                    }
                }else{
                    code = 404;
                    rMsg = String.valueOf(map.get("rMsg"));
                }
            }
            CodeMngVO userAuthConfVO = new CodeMngVO();
            userAuthConfVO.setCode("user_auth");
            resultVO.putResult("user_auth", codeService.getCodeToAuthConf(userAuthConfVO));
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "게시판 상세정보 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 게시판 등록
     * @param bbsMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시판 등록")
    @PostMapping(value = "/api/mng/bbs/proc")
    public ResultVO createBbs(@RequestBody BbsMngVO bbsMngVO,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                bbsMngVO.setInpt_seq(vo.getUser_seq());
                bbsMngVO.setUpd_seq(vo.getUser_seq());
            }
            Map<String, Object>  map = bbsMngService.setBbs(bbsMngVO);
            if((boolean)map.get("result")){
                resultVO.putResult("data",bbsMngVO);
                result = true;
            }else{
                code = 404;
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "게시판 등록 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 게시판 수정
     * @param bbsMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시판 수정")
    @PutMapping(value = "/api/mng/bbs/proc")
    public ResultVO updateBbs(@RequestBody BbsMngVO bbsMngVO,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                bbsMngVO.setInpt_seq(vo.getUser_seq());
                bbsMngVO.setUpd_seq(vo.getUser_seq());
            }
            bbsMngVO.setFlag("u");
            Map<String, Object> map = bbsMngService.updBbs(bbsMngVO);
            if((boolean)map.get("result")){
                result = true;
            }else{
                code = 404;
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "게시판 수정 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 게시판 삭제
     * @param bbsMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시판 삭제")
    @DeleteMapping(value = "/api/mng/bbs/proc")
    public ResultVO deleteBbs(@RequestBody BbsMngVO bbsMngVO,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                bbsMngVO.setInpt_seq(vo.getUser_seq());
                bbsMngVO.setUpd_seq(vo.getUser_seq());
            }
            bbsMngVO.setFlag("d");
            Map<String, Object> map = bbsMngService.delBbs(bbsMngVO);
            if((boolean)map.get("result")){
                result = true;
            }else{
                code = 404;
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "게시판 삭제 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * BBS_INFO_SEQ로 게시판 존재여부 확인
     * @param bbs_seq
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시판 존재여부 확인")
    @GetMapping("/api/mng/{bbs_seq}/bbs/cnt")
    public ResultVO menuLinkList(@PathVariable("bbs_seq") String bbs_seq
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal) throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            BbsMngVO vo = new BbsMngVO();
            vo.setBbs_info_seq(bbs_seq);
            resultVO.putResult("data",bbsMngService.getBbsCnt(vo));
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "게시판 존재여부 확인 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }
}

