package com.custom.boot3Cms.application.site.join.control;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.mng.user.service.UserMngService;
import com.custom.boot3Cms.application.site.join.service.JoinService;
import com.custom.boot3Cms.application.site.join.vo.JoinVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;

/**
 *
 * 회원가입 Controller
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-05-04 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-05-04 */
@RestController
public class JoinController {

    @Resource(name = "userMngService")
    UserMngService userMngService;

    @Resource(name = "joinService")
    JoinService joinService;

    /**
     * 회원 ID 중복확인
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @Operation(summary = "회원 ID 중복 확인", description = "user_id 를 이용하여 ID 중복 확인")
    @PostMapping(value = "/join/user/check")
    public ResultVO getUserCheck(@RequestBody JoinVO joinVO,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 RedirectAttributes redirectAttributes) throws Exception {
        ResultVO resultVO = new ResultVO(200);

        Map<String, Object> map = joinService.checkUserId(joinVO.getUser_id());
        boolean result = (boolean) map.get("result");

        resultVO.setResultMsg(map.get("rMsg").toString());
        resultVO.putResult("result",result);

        return resultVO;
    }

    /**
     * 회원 가입 프로세스
     * @param joinVO
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "회원가입 프로세스", notes = "회원가입 프로세스 입니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user_id_check", value = "중복체크 여부 yn", required = true),
            @ApiImplicitParam(name="user_id", value = "가입할 아이디", required = true),
            @ApiImplicitParam(name="email", value = "가입할 email", required = true),
            @ApiImplicitParam(name="user_name", value = "사용자 이름", required = true),
            @ApiImplicitParam(name="user_pwd", value = "사용자 비밀번호", required = true)
    })
    @PostMapping(value = "/join/proc")
    public ResultVO joinProc(
            @RequestBody JoinVO joinVO,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           ModelMap model,
                           HttpSession session,
                           RedirectAttributes redirectAttributes,
                           Principal principal) throws Exception {
        // TODO 로그인 되어있는지 체크하는 기능 추가 ( feat.JWT )
        ResultVO resultVO = new ResultVO(200);

        boolean result = false;
        String rtnMsg = "";

        if(joinVO.getUser_id_check().equals("N")){
            resultVO.setResultMsg("올바르지 않은 시도입니다.");
        }else{
            int userResult = joinService.userProc(joinVO);

            switch (userResult){
                case 1 -> result = true;
                case 2,3,4 -> rtnMsg = "회원가입 중 오류가 발생하였습니다.";
                case 5 -> rtnMsg = "이미 가입된 ID 입니다.";
                case 6 -> rtnMsg = "이미 가입된 이메일 입니다.";
                default -> rtnMsg = "알 수 없는 오류가 발생하였습니다.";
            }
        }

        resultVO.setResultMsg(rtnMsg);
        resultVO.putResult("result", result);
        return resultVO;
    }
}
