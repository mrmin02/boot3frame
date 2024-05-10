package com.custom.boot3Cms.config.spring;

import com.custom.boot3Cms.application.common.config.security.jwt.JwtAuthenticationFilter;
import com.custom.boot3Cms.application.common.config.security.jwt.JwtErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/**
 * SecurityConfig (context-security)
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일       / 수정자    / 수정내용
 * 	------------------------------------------
 * 	2018-04-13 / 최재민	  / 최초 생성
 * 	2024-04-15 / 최민석   /  JWT 인증방식으로 변경
 * </pre>
 * @since 2018-04-13
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"com.custom.boot3Cms.application.common.config.security", "com.custom.boot3Cms.application.common.system.login.**"})
public class SecurityConfig {

//    private final AuthenticationConfiguration configuration;
//
//    @Autowired
//    private LoginSuccessHandler loginSuccessHandler;
////
//    @Autowired
//    private LoginFailureHandler loginFailureHandler;
////
//    @Autowired
//    private LogoutSuccessHandler logoutSuccessHandler;

    /**
     * 토큰 검증 필터
     */
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 토큰 검증 시, 오류 발생할 경우
     */
    @Autowired
    private JwtErrorHandler jwtErrorHandler;

//    public SecurityConfig(AuthenticationConfiguration configuration) {
//        this.configuration = configuration;
//    }

    /**
     * 2024-04-15 cms
     * 로그인 로직 처리 필터
     * jwt 방식에서는 provider 를 일반적으로 사용하지 않아,
     * 기본 로그인 인증을 진행 하는 UsernamePasswordAuthenticationFilter 를 상속한 필터를 이용
     * @return
     * @throws Exception
     */
//    @Bean
//    public SecurityAuthenticationFilter securityAuthenticationFilter() throws Exception {
//
//        SecurityAuthenticationFilter filter = new SecurityAuthenticationFilter();
//        filter.setFilterProcessesUrl("/login/proc");
//        filter.setAuthenticationManager(configuration.getAuthenticationManager());
//        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
//        filter.setAuthenticationFailureHandler(loginFailureHandler);
//
//        return filter;
//    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**", "/images/**", "/css/**", "/cmmn/**", "/js/**", "/file/ckeditorFileupload");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .requestMatchers("/mng/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
                /**
                 * 세션 방식 대신 토큰 방식
                 */
            .sessionManagement((sessionManagement)->{
                sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            });
        http.csrf(AbstractHttpConfigurer::disable)
            /**
             * jwt 핵심 필터
             * 1. JwtAuthenticationFilter : 토큰 검증
             * 2. SecurityAuthenticationFilter  :  로그인 로직
             * ( UsernamePasswordAuthenticationFilter.class 를 상속 )
             * 일반적으로 토큰 검증만 filter 에서 하고, controller 에서 로그인 로직 처리..
             * [ UsernamePasswordAuthenticationFilter.class 는 동작하지 않지만 명시 )
             *
             * FIXME Swagger 및 관리를 위해 Security Filter Chain 로그인 방식에서 REST API 방식으로 변경
             */
            .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
//            .addFilterBefore(jwtAuthenticationFilter, SecurityAuthenticationFilter.class)
//            .addFilterBefore(securityAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)
            /**
             *  로그인 실패 및 jwt 토큰 오류를 jwtErrorHandler 에서 처리
             *  jwt 에서는 success fail handler 처리를 일반적으로 하지 않고,
             *  실패했을 경우에 Exception 발생으로 처리..
             *
             *  authenticationEntryPoint  : 401
             *  accessDeniedHandler : 403
             */
            .exceptionHandling((exceptionHandling)->{
                exceptionHandling
                        .authenticationEntryPoint(jwtErrorHandler)
                        .accessDeniedHandler(jwtErrorHandler);
            });
//        http.logout((logout)->logout
//                .logoutSuccessHandler(logoutSuccessHandler)
//        );
        return http.build();
    }
}
