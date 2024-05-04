package com.custom.boot3Cms.application.site.join.service;

import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.user.mapper.UserMngMapper;
import com.custom.boot3Cms.application.mng.user.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

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
@Service("joinService")
@Transactional
public class JoinService {

    @Resource(name = "userMngMapper")
    UserMngMapper userMngMapper;

    /**
     * 회원 등록/수정/탈퇴-탈퇴복구 PROC
     * @param vo
     * @return
     * @throws Exception
     */
    public int userProc(UserVO vo) throws Exception{
        boolean result = false;
        vo.setFlag("chk");
        if (StringUtil.isNotEmpty(vo.getUser_id())) {
            // 아이디 중복검사
            result = userMngMapper.checkUserId(vo.getUser_id()) == 0;
            if(result){
            } else {
                return 5;
            }
        }
        if (StringUtil.isNotEmpty(vo.getEmail())) {
            // 이메일 중복검사
            result = userMngMapper.checkUserEmail(vo.getEmail()) == 0;
            if(result){
            } else {
                return 6;
            }
        }
        // 회원생성
        vo.setInpt_seq("1");
        vo.setUser_auth("ROLE_USER");
        result = userMngMapper.setUserList(vo) > 0;
        if(result){
            result = userMngMapper.setUserDetail(vo) > 0;
            if(result){
                return 1;  // 가입 성공
            }else{
                result = userMngMapper.delUserList(vo.getUser_seq()) > 0;
                if(result){
                    return 2;  // 가입 실패
                }else{
                    return 3;  // 가입 중 오류 발생
                }
            }
        }else{
            return 4;  // 가입 실패
        }
    }
}
