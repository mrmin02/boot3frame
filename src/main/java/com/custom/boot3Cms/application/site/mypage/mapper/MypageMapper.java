package com.custom.boot3Cms.application.site.mypage.mapper;

import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import org.apache.ibatis.annotations.Mapper;


/**
 * HTML 정보 매퍼
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2018-08-07 / 에스이코리아  / 최초 생성
 * </pre>
 * @since 2018-08-07
 */
@Mapper
public interface MypageMapper {


    /**
     * 회원 상세보기
     * @param vo
     * @return
     * @throws Exception
     */
    LoginVO getUserDetail(LoginVO vo) throws Exception;


    /**
     * 회원 정보수정 - 비밀번호 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updPwd(LoginVO vo) throws Exception;

    /**
     * 비밀번호 확인 - 회원 탈퇴
     * @param vo
     * @return
     * @throws Exception
     */
    int checkMyInfoPwd(LoginVO vo) throws Exception;


    /**
     * 회원 탈퇴
     * @param vo
     * @return
     * @throws Exception
     */
    int accountDelProc(LoginVO vo) throws Exception;

    /**
     * 회원 정보 수정 ( list )
     * @param vo
     * @return
     * @throws Exception
     */
    int updInfo(LoginVO vo) throws Exception;

    /**
     * 회원 정보수정 ( detail )
     * @param vo
     * @return
     * @throws Exception
     */
    int updDetail(LoginVO vo) throws Exception;

}
