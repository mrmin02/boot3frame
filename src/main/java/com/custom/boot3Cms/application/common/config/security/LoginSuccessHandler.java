package com.custom.boot3Cms.application.common.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.custom.boot3Cms.application.common.config.security.jwt.EgovJwtTokenUtil;
import com.custom.boot3Cms.application.common.system.jwt.vo.JwtResultVO;
import com.custom.boot3Cms.application.common.system.login.service.LoginService;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Resource(name = "loginService")
    LoginService loginService;

    @Autowired
    EgovJwtTokenUtil egovJwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        LoginVO vo = (LoginVO) auth.getDetails();

        /**
         * 토큰 생성
         */
        vo.setAccess_token(egovJwtTokenUtil.generateAccessToken(vo));
        vo.setRefresh_token(egovJwtTokenUtil.generateRefreshToken(vo));

        try {
            loginService.updUserLoginDateIp(vo, request);
            loginService.setAdminLog(vo, request);

            // 회원 토큰 저장
            loginService.setUserToken(vo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JwtResultVO resultVO = new JwtResultVO();
        resultVO.setResultCode(200);
        resultVO.setResultMessage("로그인 되었습니다.");


        /**
         * 로그인 성공 상세 정보
         */
        resultVO.putResult("result",true);
        resultVO.putResult("access_token",vo.getAccess_token());
        resultVO.putResult("refresh_token",vo.getRefresh_token());

        ObjectMapper mapper = new ObjectMapper();

        //Convert object to JSON string
        String jsonInString = mapper.writeValueAsString(resultVO);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(jsonInString);
    }

    /**
     * 로그인 하기 전의 요청했던 URL을 알아낸다.
     *
     * @param request
     * @param response
     * @return
     */
    private String getReturnUrl(HttpServletRequest request, HttpServletResponse response) {
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {
            return "/mng/main";
        }
        return savedRequest.getRedirectUrl();
    }

}
