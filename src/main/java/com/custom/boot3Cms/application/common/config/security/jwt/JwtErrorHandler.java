package com.custom.boot3Cms.application.common.config.security.jwt;

import com.custom.boot3Cms.application.common.system.jwt.vo.JwtResultVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * fileName       : JwtAuthenticationEntryPoint
 * author         : crlee
 * date           : 2023/06/11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/06/11        crlee       최초 생성
 */

/**
 * 2024-04-15 cms
 * Spring Security Error Control
 * 스프링 시큐리티  JWT 필터에서 에러 발생 시..
 */
@Component
public class JwtErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {


    /**
     * AuthenticationEntryPoint : 401 error
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        JwtResultVO resultVO = new JwtResultVO();
        resultVO.setResultCode(404);
        resultVO.setResultMessage("페이지를 찾을 수 없습니다. 401");
        resultVO.putResult("result",false);
        ObjectMapper mapper = new ObjectMapper();

        //Convert object to JSON string
        String jsonInString = mapper.writeValueAsString(resultVO);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(jsonInString);
    }

    /**
     * AccessDeniedHandler : 403 error
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        JwtResultVO resultVO = new JwtResultVO();
        resultVO.setResultCode(404);
        resultVO.setResultMessage("페이지를 찾을 수 없습니다. 403");
        resultVO.putResult("result",false);
        ObjectMapper mapper = new ObjectMapper();

        //Convert object to JSON string
        String jsonInString = mapper.writeValueAsString(resultVO);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(jsonInString);
    }
}