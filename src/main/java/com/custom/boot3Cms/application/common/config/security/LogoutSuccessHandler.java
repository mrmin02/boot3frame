//package com.custom.boot3Cms.application.common.config.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.custom.boot3Cms.application.common.system.jwt.vo.JwtResultVO;
//import com.custom.boot3Cms.application.common.system.login.service.LoginService;
//import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
//import com.custom.boot3Cms.application.common.utils.StringUtil;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import jakarta.annotation.Resource;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
//
//    @Resource(name = "loginService")
//    LoginService loginService;
//
//    public final String ACCESS_TOKEN_HEADER;
//    public final String REFRESH_TOKEN_HEADER;
//
//    private LogoutSuccessHandler(
//            @Value("${Globals.jwt.header.access}") String ACCESS_TOKEN_HEADER,
//            @Value("${Globals.jwt.header.refresh}") String REFRESH_TOKEN_HEADER){
//        this.ACCESS_TOKEN_HEADER = ACCESS_TOKEN_HEADER;
//        this.REFRESH_TOKEN_HEADER = REFRESH_TOKEN_HEADER;
//    }
//
//    @Override
//    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
//                                Authentication authentication) throws IOException, ServletException {
//        new SecurityContextLogoutHandler().logout(request, response, authentication);
//
//        /**
//         *  1. 헤더에서 토큰 가져오기
//         *  2. 토큰 black list 등록
//         */
//        LoginVO vo = new LoginVO();
//        vo.setAccess_token(StringUtil.isNullToString(request.getHeader(ACCESS_TOKEN_HEADER),""));
//        vo.setRefresh_token(StringUtil.isNullToString(request.getHeader(REFRESH_TOKEN_HEADER),""));
//
//        try{
//            if(StringUtil.isNotEmpty(vo.getAccess_token())){
//                loginService.setBlackAccessToken(vo,request);
//            }
//            if(StringUtil.isNotEmpty(vo.getRefresh_token())){
//                loginService.setBlackRefreshToken(vo,request);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//
//        JwtResultVO resultVO = new JwtResultVO();
//        resultVO.setResultCode(200);
//        resultVO.setResultMessage("로그아웃 되었습니다.");
//
//        /**
//         * 로그인 실패 상세 정보
//         * FIXME 로그인 실패 원인 상세정보 return
//         */
//        resultVO.putResult("result",true);
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        //Convert object to JSON string
//        String jsonInString = mapper.writeValueAsString(resultVO);
//
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType(MediaType.APPLICATION_JSON.toString());
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().println(jsonInString);
//    }
//}
