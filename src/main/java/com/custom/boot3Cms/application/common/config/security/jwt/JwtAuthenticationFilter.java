package com.custom.boot3Cms.application.common.config.security.jwt;

import com.custom.boot3Cms.application.common.system.login.service.LoginService;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * JWT Filter
 * 토큰 검증 필터
 * fileName       : JwtAuthenticationFilter
 * author         : crlee
 * date           : 2023/06/11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/06/11        crlee       최초 생성
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private EgovJwtTokenUtil jwtTokenUtil;

    @Resource(name = "loginService")
    LoginService loginService;

    public final String ACCESS_TOKEN_HEADER;

    private JwtAuthenticationFilter(@Value("${Globals.jwt.header.access}") String ACCESS_TOKEN_HEADER){
        this.ACCESS_TOKEN_HEADER = ACCESS_TOKEN_HEADER;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        boolean verificationFlag = true;

        // step 1. request header에서 토큰을 가져온다.
        String jwtToken = StringUtil.isNullToString(req.getHeader(ACCESS_TOKEN_HEADER),"");

        // step 2. 토큰에 내용이 있는지 확인해서 id값을 가져옴
        // Exception 핸들링 추가처리 (토큰 유효성, 토큰 변조 여부, 토큰 만료여부)
        // 내부적으로 parse하는 과정에서 해당 여부들이 검증됨
        String id = null;

        try {

            /**
             * 토큰 검증
             */
            id = jwtTokenUtil.getUserIdFromToken(jwtToken);
            if (id == null) {
                logger.debug("jwtToken not validate");
                verificationFlag =  false;
            }

            /**
             * 블랙 리스트에 등록된 토큰인지 검사
             */
            LoginVO tmpVO = new LoginVO();
            tmpVO.setAccess_token(jwtToken);
            if(loginService.checkBlackToken(tmpVO) > 0){
                verificationFlag = false;
            }
            logger.debug("===>>> id = " + id);
        } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | SignatureException e) {
            /**
             * 검증 및 만료.. 등 의 오류 발생
             */
            logger.debug("Unable to verify JWT Token: " + e.getMessage());
            verificationFlag = false;
        } catch (Exception e){
            /**
             * 블랙리스트 조회 중 오류 발생
             */
            e.printStackTrace();
            verificationFlag = false;
        }

        // TODO black list 검증

        LoginVO loginVO = new LoginVO();
        if( verificationFlag ){
            /**
             * 인가
             */
            logger.debug("jwtToken validated");
            loginVO.setUser_id(id);
            loginVO.setUser_name( jwtTokenUtil.getInfoFromToken("user_name",jwtToken) );

            /**
             * TODO 유저 권한 체크
             */
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginVO, null,
//                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
                    Arrays.asList(new SimpleGrantedAuthority(jwtTokenUtil.getInfoFromToken("user_role",jwtToken)))
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(req, res);

    }
}
