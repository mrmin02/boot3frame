package com.custom.boot3Cms.application.common.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.custom.boot3Cms.application.common.system.jwt.vo.JwtResultVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    
     public static String DEFAULT_TARGET_PARAMETER = "spring-security-redirect-login-failure";
     private String targetUrlParameter = DEFAULT_TARGET_PARAMETER;
     public String getTargetUrlParameter() {
          return targetUrlParameter;
     }

     public void setTargetUrlParameter(String targetUrlParameter) {
          this.targetUrlParameter = targetUrlParameter;
     }

     @Override
     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
          HttpSession session = request.getSession();
          String rMsg = "";
          switch (exception.getMessage()){
               case "err01" : rMsg = "아이디가 존재하지 않습니다.";
                    break;
               case "err02" : rMsg = "비밀번호가 올바르지 않습니다.";
                    break;
               case "err03" : rMsg = "탈퇴 된 회원 입니다.";
                    break;
               case "err04" : rMsg = "승인 대기중 입니다.";
                    break;
               case "err05" : rMsg = "반려 된 회원 입니다.";
                    break;
               case "err06" : rMsg = "조직코드 조회 에러 입니다.";
                    break;
               default : rMsg = exception.getMessage();
                    break;
          }

          JwtResultVO resultVO = new JwtResultVO();
          resultVO.setResultCode(404);
          resultVO.setResultMessage("로그인에 실패하였습니다.");

          /**
           * 로그인 실패 상세 정보
           * FIXME 로그인 실패 원인 상세정보 return
           */
          resultVO.putResult("result",false);
          resultVO.putResult("rMsg",rMsg);

          ObjectMapper mapper = new ObjectMapper();

          //Convert object to JSON string
          String jsonInString = mapper.writeValueAsString(resultVO);

          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          response.setContentType(MediaType.APPLICATION_JSON.toString());
          response.setCharacterEncoding("UTF-8");
          response.getWriter().println(jsonInString);
     }

}
