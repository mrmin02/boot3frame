package com.custom.boot3Cms.application.common.system.login.controller;

import com.custom.boot3Cms.application.common.config.security.jwt.EgovJwtTokenUtil;
import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.system.login.service.LoginService;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 로그인 컨트롤러
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-05-10 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-05-10 */
@RestController
public class LoginController {

    @Resource(name = "loginService")
    LoginService loginService;

    @Autowired
    EgovJwtTokenUtil egovJwtTokenUtil;

    @Value("${Globals.jwt.header.access}") String ACCESS_TOKEN_HEADER;
    @Value("${Globals.jwt.header.refresh}") String REFRESH_TOKEN_HEADER;


    /**
     * 로그인 프로세스
     * SecurityAuthenticationFilter > LoginSuccessHandler , LoginFailureHandler 를
     * MVC 로 구현 ( swagger api 테스트 및 관리 을 위함 )
     * @param loginVO
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary = "로그인 API", description = "아이디, 비밀번호로 로그인",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_UTF8_VALUE,
                    schema = @Schema(
                            allOf = {LoginVO.class},
                            requiredProperties = {
                                    "user_id","user_pwd"
                            }
                    )
            )
    ))
    @PostMapping(value = "/api/login")
    public ResultVO login(@RequestBody LoginVO loginVO, HttpServletRequest request) throws Exception {
        ResultVO resultVO = new ResultVO();
        int resultCode = 200;
        boolean result = false;
        String rMsg = "";

        try{
            Map<String,Object> checkMap =  loginService.checkIdAndPwd(loginVO);

            /**
             * 로그인 성공
             */
            if((boolean) checkMap.get("result")){
                /**
                 * 회원정보 체크 성공
                 */
                LoginVO userVO = (LoginVO) checkMap.get("vo");

                /**
                 * 토큰 생성
                 */
                userVO.setAccess_token(egovJwtTokenUtil.generateAccessToken(userVO));
                userVO.setRefresh_token(egovJwtTokenUtil.generateRefreshToken(userVO));

                try {
                    loginService.updUserLoginDateIp(userVO, request);
                    loginService.setAdminLog(userVO, request);

                    // 회원 토큰 저장
                    loginService.setUserToken(userVO);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /**
                 * 로그인 성공 상세 정보
                 */
                resultVO.putResult("result",true);
                resultVO.putResult("access_token", userVO.getAccess_token());
                resultVO.putResult("refresh_token", userVO.getRefresh_token());

                result = true;
                rMsg = "로그인 성공";
            }else{
                /**
                 * 로그인 실패
                 */
                switch (checkMap.get("errMsg").toString()){
                    case "err01" -> rMsg = "아이디가 존재하지 않습니다.";
                    case "err02" -> rMsg = "비밀번호가 올바르지 않습니다.";
                    case "err03" -> rMsg = "탈퇴 된 회원 입니다.";
                    case "err04" -> rMsg = "승인 대기중 입니다.";
                    case "err05" -> rMsg = "반려 된 회원 입니다.";
                    case "err06" -> rMsg = "조직코드 조회 에러 입니다.";
                    default -> rMsg = "알 수 없는 에러가 발생하였습니다.";
                }
                resultCode = 401;
            }
        }catch (Exception e){
            e.printStackTrace();
            resultCode = 401;
            rMsg = "로그인 도중 오류가 발생하였습니다.";
            result = false;
        }

        resultVO.setResultCode(resultCode);
        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);

        return resultVO;
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃을 위한 REST API")
    @PostMapping(value = "/api/logout")
    public ResultVO logout(HttpServletRequest request) throws Exception {
        ResultVO resultVO = new ResultVO();

        /**
         *  1. 헤더에서 토큰 가져오기
         *  2. 토큰 black list 등록
         */
        LoginVO vo = new LoginVO();
        vo.setAccess_token(StringUtil.isNullToString(request.getHeader(ACCESS_TOKEN_HEADER),""));
        vo.setRefresh_token(StringUtil.isNullToString(request.getHeader(REFRESH_TOKEN_HEADER),""));

        try{
            if(StringUtil.isNotEmpty(vo.getAccess_token())){
                loginService.setBlackAccessToken(vo,request);
            }
            if(StringUtil.isNotEmpty(vo.getRefresh_token())){
                loginService.setBlackRefreshToken(vo,request);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        resultVO.setResultCode(200);
        resultVO.setResultMsg("로그아웃 되었습니다.");

        return resultVO;
    }
}
