package com.custom.boot3Cms.application.common.system.login.service;


import com.custom.boot3Cms.application.common.system.login.mapper.LoginMapper;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 로그인 서비스
 * 2024-05-03
 */
@Service("loginService")
public class LoginService {

    @Resource(name = "loginMapper")
    LoginMapper loginMapper;

    /**
     * 회원 아이디 비밀번호 검증
     * @param vo
     * @return
     * @throws Exception
     */
    public LoginVO getUser(LoginVO vo) throws Exception{
        return loginMapper.getUser(vo);
    }

    /**
     * 회원 상세정보
     * @param vo
     * @return
     * @throws Exception
     */
    public LoginVO getUserDetail(LoginVO vo) throws Exception{
        return loginMapper.getUserDetail(vo);
    }

    /**
     * 회원 토큰 저장
     * @param vo
     * @return
     * @throws Exception
     */
    public int setUserToken(LoginVO vo) throws Exception {
        return loginMapper.setUserToken(vo);
    }

    /**
     * 회원 로그인 일자 및 IP 수정
     * @param vo
     * @return
     * @throws Exception
     */
    public boolean updUserLoginDateIp(LoginVO vo, HttpServletRequest request) throws Exception{
        vo.setLast_login_ip(StringUtil.getIP(request));
        return loginMapper.updUserLoginDateIp(vo)>0;
    }

    /**
     * 관리자페이지 접속 로그 저장
     * @param vo
     * @param request
     * @throws Exception
     */
    public void setAdminLog(LoginVO vo, HttpServletRequest request) throws Exception {
        String ip = StringUtil.getIP(request);
        if(!ip.equals("0:0:0:0:0:0:0:1")) {
            vo.setLast_login_ip(ip);
            loginMapper.setAdminLog(vo);
        }
    }


    /**
     * Access Token 블랙 리스트 등록
     * @param vo
     * @param request
     * @return
     * @throws Exception
     */
    public int setBlackAccessToken(LoginVO vo, HttpServletRequest request) throws Exception {
        vo.setLast_login_ip(StringUtil.getIP(request));
        return loginMapper.setBlackAccessToken(vo);
    }

    /**
     * refresh Token 블랙 리스트 등록
     * @param vo
     * @param request
     * @return
     * @throws Exception
     */
    public int setBlackRefreshToken(LoginVO vo, HttpServletRequest request) throws Exception {
        vo.setLast_login_ip(StringUtil.getIP(request));
        return loginMapper.setBlackRefreshToken(vo);
    }

    /**
     * 토큰 블랙리스트 검증
     * @param vo
     * @return
     * @throws Exception
     */
    public int checkBlackToken(LoginVO vo) throws Exception {
        return loginMapper.checkBlackToken(vo);
    }
}
