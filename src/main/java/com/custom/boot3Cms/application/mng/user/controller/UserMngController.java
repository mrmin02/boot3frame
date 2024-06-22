package com.custom.boot3Cms.application.mng.user.controller;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.mng.code.service.CodeMngService;
import com.custom.boot3Cms.application.mng.user.service.UserMngService;
import com.custom.boot3Cms.application.mng.user.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * 관리자 회원 Controller
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
@Tag(name = "관리자 회원 컨트롤러")
public class UserMngController {

    @Resource(name = "userMngService")
    UserMngService userMngService;

    @Resource(name = "codeMngService")
    CodeMngService codeService;

    /**
     * 회원 목록
     *
     * @param request
     * @param response
     * @param session
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "회원 목록" , description = "회원 목록 조회")
    @GetMapping(value = "/api/mng/user/list")
    public ResultVO userList(@ModelAttribute("UserVO") UserVO userVO,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             HttpSession session,
                             Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            int total_cnt = userMngService.getUserListCNT(userVO);
            List<UserVO> list = userMngService.getUserList(userVO);

            resultVO.putResult("total_cnt", total_cnt);
            resultVO.putResult("data", list);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "회원목록 검색 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 회원 상세보기
     *
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "회원 상세보기", description = "회원 상세보기")
    @GetMapping(value = "/api/mng/user/detail/{user_seq}")
    public ResultVO bannerDetail(@PathVariable("user_seq") String user_seq,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               ModelMap model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes,
                               Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            UserVO userVO = new UserVO();
            userVO.setUser_seq(user_seq);
            Map<String, Object> map = userMngService.getUserDetail(userVO);
            if ((boolean) map.get("result")) {
                result = true;
                resultVO.putResult("data",map.get("value"));
            } else {
                code = 404;
                rMsg = "해당하는 회원정보가 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "회원정보 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }


    /**
     * 회원 등록
     * @param userVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "회원 등록", description = "회원 등록")
    @PostMapping(value = "/api/mng/user/proc")
    public ResultVO userSetProc(@RequestBody UserVO userVO,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if (principal != null) {
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                userVO.setInpt_seq(vo.getUser_seq());
                userVO.setUpd_seq(vo.getUser_seq());
            }
            userVO.setFlag("c");
            Map<String, Object> map = userMngService.userProc(userVO);

            if((boolean) map.get("result")){
                result = true;
            }else{
                code = 404;
                rMsg = String.valueOf(map.get("rMsg"));
            }

        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "회원 등록 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 회원 수정
     * @param userVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "회원 수정", description = "회원 수정")
    @PutMapping(value = "/api/mng/user/proc")
    public ResultVO userUpdateProc(@RequestBody UserVO userVO,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if (principal != null) {
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                userVO.setInpt_seq(vo.getUser_seq());
                userVO.setUpd_seq(vo.getUser_seq());
            }
            userVO.setFlag("u");
            Map<String, Object> map = userMngService.userProc(userVO);

            if((boolean) map.get("result")){
                result = true;
            }else{
                code = 404;
                rMsg = String.valueOf(map.get("rMsg").toString());
            }

        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "회원 수정 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 회원 탈퇴 및 복구
     * @param userVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "회원 탈퇴 및 복구", description = "out_yn 값으로 회원 탈퇴 및 복구")
    @DeleteMapping(value = "/api/mng/user/proc")
    public ResultVO userDeleteProc(@RequestBody UserVO userVO,
                                   HttpServletRequest request,
                                   HttpServletResponse response,
                                   Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if (principal != null) {
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                userVO.setInpt_seq(vo.getUser_seq());
                userVO.setUpd_seq(vo.getUser_seq());
            }
            userVO.setFlag("d");
            Map<String, Object> map = userMngService.userProc(userVO);

            if((boolean) map.get("result")){
                result = true;
            }else{
                code = 404;
                rMsg = String.valueOf(map.get("rMsg").toString());
            }

        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "회원 탈퇴 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 회원 ID 중복확인
     *
     * @param userVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "회원 ID 중복 체크", description = "회원 아이디 중복체크")
    @PostMapping(value = "/api/mng/user/check")
    public ResultVO getUserCheck(@RequestBody UserVO userVO,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            userVO.setFlag("chk");
            Map<String, Object> map = userMngService.userProc(userVO);
            result = (boolean) map.get("result");
            if(result){
                rMsg = "사용가능한 아이디 입니다.";
            }else{
                rMsg = "이미 사용중인 아이디 입니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "아이디 중복체크 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

}
