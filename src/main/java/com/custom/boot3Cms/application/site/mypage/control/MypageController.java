package com.custom.boot3Cms.application.site.mypage.control;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.site.mypage.service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;

/**
 * 회원가입 컨트롤러
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2020-12-29 / 에스이코리아  / 최초 생성
 * </pre>
 * @since 2020-12-29
 */
@RestController
@Tag(name="마이페이지 컨트롤러")
public class MypageController {

    @Resource(name = "mypageService")
    MypageService mypageService;


    /**
     * 마이페이지
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
    @Operation(summary = "사용자 정보 GET",description = "jwt 로 사용자 정보를 가져옵니다.")
    @GetMapping(value = "/api/mypage/info")
    public ResultVO userInfoInMypage(
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
        
        try {
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                resultVO.putResult("data",mypageService.getUserDetail(vo));
                result = true;
            }else{
                code = 403;
                rMsg = "회원정보가 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            code = 403;
            rMsg = "알 수 없는 오류가 발생하였습니다.";
        }
        resultVO.setResultCode(code);
        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        return resultVO;
    }


    /**
     * 개인정보 수정 프로세스 처리단
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
    @Operation(summary = "마이페이지 회원 정보 수정")
    @PutMapping(value = "/api/mypage/proc")
    public ResultVO updateUserInfo(
            @RequestBody LoginVO loginVO,
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

        try {
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                loginVO.setUser_seq(vo.getUser_seq());
                loginVO.setUpd_seq(vo.getUser_seq());
                Map<String, Object> rtnMap = mypageService.updateUserInMypage(loginVO);

                if((boolean)rtnMap.get("result")){
                    result = true;
                    rMsg = "회원정보가 수정되었습니다.";
                }else{
                    code = 404;
                    rMsg = "회원정보 수정에 실패하였습니다.";    
                }
            }else{
                code = 403;
                rMsg = "회원정보가 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            code = 403;
            rMsg = "알 수 없는 오류가 발생하였습니다.";
        }
        
        resultVO.setResultMsg(rMsg);
        resultVO.setResultCode(code);
        resultVO.putResult("result",result);
        return resultVO;
    }

    /**
     * 비밀번호 확인 및 탈퇴 처리
     *
     * @param loginVO
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @Operation(summary = "회원 탈퇴", description = "비밀번호 체크를 통한 회원탈퇴 처리")
    @DeleteMapping(value = "/api/mypage/proc")
    public ResultVO deleteUserInfo(@RequestBody LoginVO loginVO,
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

        try {
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                loginVO.setUser_seq(vo.getUser_seq());
                loginVO.setUpd_seq(vo.getUser_seq());
                Map<String, Object> rtnMap = mypageService.deleteUserInMypage(loginVO);

                if((boolean)rtnMap.get("result")){
                    result = true;
                }else{
                    code = 404;
                }
                rMsg = rtnMap.get("rMsg").toString();
            }else{
                code = 403;
                rMsg = "회원정보가 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            code = 403;
            rMsg = "알 수 없는 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.setResultCode(code);
        resultVO.putResult("result",result);
        return resultVO;
    }

}
