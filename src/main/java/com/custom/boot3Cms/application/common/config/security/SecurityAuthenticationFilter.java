//package com.custom.boot3Cms.application.common.config.security;
//
//
//import com.custom.boot3Cms.application.common.system.login.service.LoginService;
//import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
//import com.custom.boot3Cms.application.common.utils.StringUtil;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.stereotype.Component;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class SecurityAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    @Resource(name = "loginService")
//    LoginService loginService;
//
//    /**
//     * 로그인 인증처리 FUNCTION
//     * ERR01 = 아이디 없음
//     * ERR02 = 비밀번호 틀림
//     * ERR03 = 탈퇴회원
//     * ERR04 = 가입대기
//     * ERR05 = 가입반려
//     * @param request
//     * @param response
//     * @return
//     * @throws AuthenticationException
//     */
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String user_id = StringUtil.isNullToString(request.getParameter("user_id"));
//        String user_pwd = StringUtil.isNullToString(request.getParameter("user_pwd"));
//        LoginVO vo = new LoginVO(user_id, user_pwd);
//
//        UsernamePasswordAuthenticationToken result = null;
//        List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
//        try {
//            LoginVO loginInfo = loginService.getUser(vo);
//            if (loginInfo == null) {
//                throw new BadCredentialsException("err01");
//            } else if ("Y".equals(loginInfo.getPwd_check())) {
//                if ("Y".equals(loginInfo.getOut_yn())) {
//                    throw new BadCredentialsException("err03");
//                } else {
//                    switch (loginInfo.getUser_status()){
//                        case "UST_001" :
//                            throw new BadCredentialsException("err04");
//                        case "UST_003" :
//                            throw new BadCredentialsException("err05");
//                    }
//                    LoginVO loginVO = loginService.getUserDetail(loginInfo);
//                    roles.add(new SimpleGrantedAuthority(loginVO.getUser_auth()));
//                    result = new UsernamePasswordAuthenticationToken(user_id, user_pwd, roles);
//                    result.setDetails(loginVO);
//                }
//            } else {
//                throw new BadCredentialsException("err02");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new BadCredentialsException(e.getMessage());
//        }
//        return result;
//    }
//}
