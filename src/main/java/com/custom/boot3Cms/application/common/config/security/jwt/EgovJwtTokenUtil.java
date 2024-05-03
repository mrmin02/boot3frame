package com.custom.boot3Cms.application.common.config.security.jwt;

import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author : 정완배
 * @since : 2023. 8. 9.
 * @version : 1.0
 *
 * @package : egovframework.com.jwt
 * @filename : EgovJwtTokenUtil.java
 * @modificationInformation
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일            수정자             수정내용
 *  ----------   ----------   ----------------------
 *  2023. 8. 9.    정완배              주석추가
 * </pre>
 *
 *
 */
//security 관련 제외한 jwt util 클래스
@Slf4j
@Component
public class EgovJwtTokenUtil {

    //public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60; //하루
    public final String SECRET_KEY;

    /**
     * JWT Header
     */
    public final String ACCESS_HEADER_STRING;// = getProperties.getProperty("jwt.header.access");
    public final String REFRESH_HEADER_STRING;// = getProperties.getProperty("jwt.header.refresh");

    /**
     * JWT valid time
     */
    private final int JWT_ACCESS_VALIDITY;// = getProperties.getIntProperty("jwt.validity.access");
    private final int JWT_REFRESH_VALIDITY;// = getProperties.getIntProperty("jwt.validity.refresh");

    /**
     * final 변수 값 초기화
     * @param SECRET_KEY
     * @param ACCESS_HEADER_STRING
     * @param REFRESH_HEADER_STRING
     * @param JWT_ACCESS_VALIDITY
     * @param JWT_REFRESH_VALIDITY
     */
    public EgovJwtTokenUtil(
            @Value("${Globals.jwt.secret}") String SECRET_KEY,
            @Value("${Globals.jwt.header.access}") String ACCESS_HEADER_STRING,
            @Value("${Globals.jwt.header.refresh}") String REFRESH_HEADER_STRING,
            @Value("${Globals.jwt.validity.access}") int JWT_ACCESS_VALIDITY,
            @Value("${Globals.jwt.validity.refresh}") int JWT_REFRESH_VALIDITY){
        this.SECRET_KEY = SECRET_KEY;
        this.ACCESS_HEADER_STRING = ACCESS_HEADER_STRING;
        this.REFRESH_HEADER_STRING = REFRESH_HEADER_STRING;
        this.JWT_ACCESS_VALIDITY = JWT_ACCESS_VALIDITY;
        this.JWT_REFRESH_VALIDITY = JWT_REFRESH_VALIDITY;
    }

//    public static final long JWT_REFRESH_TOKEN_VALIDITY = (long) ((1 * 60 * 60) / 60) * 120;

    /**
     * Access Token 유효시간
     * @return
     */
    private int getJwtAccessValidity(){
        if(JWT_ACCESS_VALIDITY != 0){
            return JWT_ACCESS_VALIDITY;
        }
        return 30;
    }

    /**
     * Refresh Token 유효시간
     * @return
     */
    private int getJwtRefreshValidity(){
        if(JWT_REFRESH_VALIDITY != 0){
            return JWT_REFRESH_VALIDITY;
        }
        return 120;
    }

	
	//retrieve username from jwt token
    public String getUserIdFromToken(String token) {
        Claims claims = getClaimFromToken(token);
        return claims.get("user_id").toString();
    }
    public String getUserSeFromToken(String token) {
        Claims claims = getClaimFromToken(token);
        return claims.get("userSe").toString();
    }
    public String getInfoFromToken(String type, String token) {
        Claims claims = getClaimFromToken(token);
        return claims.get(type).toString();
    }
    public Claims getClaimFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims;
    }
	
    //for retrieveing any information from token we will need the secret key
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    /**
     * Access Token 생성
     * @param loginVO
     * @return
     */
    //generate token for user
    public String generateAccessToken(LoginVO loginVO) {
        return doGenerateAccessToken(loginVO, ACCESS_HEADER_STRING);
    }

	//while creating the token -
	//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
	//2. Sign the JWT using the HS512 algorithm and secret key.
	//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	//   compaction of the JWT to a URL-safe string
    private String doGenerateAccessToken(LoginVO loginVO, String subject) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", loginVO.getUser_id() );
        claims.put("user_name", loginVO.getUser_name() );
        claims.put("user_role", loginVO.getUser_auth() );
        claims.put("type", subject);

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + ((long)((1 * 60 * 60) / 60) * getJwtAccessValidity()) * 1000))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    /**
     * refresh 토큰 생성
     * @param loginVO
     * @return
     */
    public String generateRefreshToken(LoginVO loginVO) {
        return doGenerateRefreshToken(loginVO, REFRESH_HEADER_STRING);
    }

    /**
     * refresh 토큰 생성
     * @param loginVO
     * @return
     */
    private String doGenerateRefreshToken(LoginVO loginVO, String subject) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", loginVO.getUser_id() );
        claims.put("user_name", loginVO.getUser_name() );
        claims.put("type", subject);
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ((long)((1 * 60 * 60) / 60) * getJwtRefreshValidity()) * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

}