package com.custom.boot3Cms.application.common.system.login.service;

import com.custom.boot3Cms.application.common.system.login.mapper.LoginMapper;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 로그인 서비스
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2018-03-06 / 최재민	 / 최초 생성
 *
 * </pre>
 * @since 2018-03-06
 */
@Service("loginService")
public class LoginService {

    @Autowired
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

//    /**
//     * 휴면 회원 아이디 비밀번호 검증
//     * @param vo
//     * @return
//     */
//    public LoginVO getUserRest(LoginVO vo) {
//        return loginMapper.getUserRest(vo);
//    }
    
    /**
     * 회원 상세정보
     * @param vo
     * @return
     * @throws Exception
     */
    public LoginVO getUserDetail(LoginVO vo) throws Exception{
        vo = loginMapper.getUserDetail(vo);
        return vo;
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
     *  회원 로그인 횟수 업
     * @param vo
     * @return
     * @throws Exception
     */
    public boolean updUserLoginTotalCntUp(LoginVO vo) throws Exception{
        return loginMapper.updUserLoginTotalCntUp(vo)>0;
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
     * 회원 토큰 저장
     * @param vo
     * @return
     * @throws Exception
     */
    public int setUserToken(LoginVO vo) throws Exception {
        return loginMapper.setUserToken(vo);
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
