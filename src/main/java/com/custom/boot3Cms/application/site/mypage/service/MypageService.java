package com.custom.boot3Cms.application.site.mypage.service;

import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.site.mypage.mapper.MypageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 회원가입 서비스
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2020-12-29 / 김기식  / 최초 생성
 * </pre>
 * @since 2020-12-29
 */
@Service("mypageService")
@Transactional
public class MypageService {

    @Autowired
    MypageMapper mypageMapper;

    @Value("${Globals.file.DefaultPath}")
    private String DEFAULT_FILE_PATH;


    /**
     * 회원 상세정보
     * @param vo
     * @return
     * @throws Exception
     */
    public LoginVO getUserDetail(LoginVO vo) throws Exception{
        return mypageMapper.getUserDetail(vo);
    }

    /**
     * 회원정보 수정
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String ,Object> updateUserInMypage(LoginVO vo) throws Exception{
        Map<String, Object> rtnMap = new HashMap<>();
        boolean result = false;

        // 회원정보 상세 수정
        mypageMapper.updInfo(vo);
        result = mypageMapper.updDetail(vo) > 0;
        // 비밀번호 수정
        if(vo.getUser_pwd() != null && !vo.getUser_pwd().equals("")){
            // setter 에 user_pwd 암호화 로직 포함
            result = mypageMapper.updPwd(vo) > 0;
        }

        rtnMap.put("result", result);
        return rtnMap;
    }

    /**
     * 비밀번호 검증 및 회원 탈퇴 처리
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String ,Object> deleteUserInMypage(LoginVO vo) throws Exception{
        Map<String, Object> rtnMap = new HashMap<>();
        String rMsg = "";
        String rHeader = "알림!";
        boolean result = false;

        if(mypageMapper.checkMyInfoPwd(vo) > 0){
            if(mypageMapper.accountDelProc(vo) > 0){
                result = true;
                rMsg = "정상적으로 탈퇴되었습니다.";
            }else{
                result = false;
                rMsg = "탈퇴 처리에 실패하였습니다. 잠시 후 다시 시도하거나, 현상이 반복 될 경우 개발사에 문의 바랍니다.";
            }
        }else{
            result = false;
            rMsg = "비밀번호가 일치하지 않습니다";
        }

        rtnMap.put("result", result);
        rtnMap.put("header", rHeader);
        rtnMap.put("rMsg", rMsg);

        return rtnMap;
    }
}
