package com.custom.boot3Cms.application.mng.user.mapper;

import com.custom.boot3Cms.application.common.config.vo.DefaultVO;
import com.custom.boot3Cms.application.mng.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

/**
 * 회원관리 매퍼
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2018-03-13 / 최재민  / 최초 생성
 * 	2020-08-28 / 최민석  / 유저 정렬 순서 컬럼 추가 및 마지막 순번 구하기 추가
 * </pre>
 * @since 2018-03-13
 */
@Mapper
public interface UserMngMapper {

    /**
     * 회원 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<UserVO> getUserList(UserVO vo) throws Exception;


    /**
     * 회원 CNT
     * @param vo
     * @return
     * @throws Exception
     */
    int getUserListCNT(UserVO vo) throws Exception;

    /**
     * 회원 상세보기
     * @param vo
     * @return
     * @throws Exception
     */
    UserVO getUserDetail(UserVO vo) throws Exception;

    /**
     * 회원 상세보기 of ID
     * @param vo
     * @return
     * @throws Exception
     */
    UserVO getUserDetailOfId(UserVO vo) throws Exception;

    /**
     * 회원 아이디 중복검사
     * @param user_id
     * @return
     * @throws Exception
     */
    int checkUserId(String user_id) throws Exception;

    /**
     * 회원 이메일 중복검사
     * @param email
     * @return
     * @throws Exception
     */
    int checkUserEmail(String email) throws Exception;

    /**
     * 관리자 회원 조회
     * @return
     * @throws Exception
     */
    List<UserVO> getAdminUser(String user_role) throws Exception;

    /**
     * 회원 등록 (목록)
     * @param vo
     * @return
     * @throws Exception
     */
    int setUserList(UserVO vo) throws Exception;

    /**
     * 회원 상세정보 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setUserDetail(UserVO vo) throws Exception;

    /**
     * 회원 정보 수정 (목록)
     * @param vo
     * @return
     * @throws Exception
     */
    int updUserList(UserVO vo) throws Exception;

    /**
     * 회원 상세정보 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updUserDetail(UserVO vo) throws Exception;

    /**
     * 회원 탈퇴/복구 처리
     * @param vo
     * @return
     * @throws Exception
     */
    int updUserOut(UserVO vo) throws Exception;

    /**
     * 회원 삭제
     * @param user_seq
     * @return
     * @throws Exception
     */
    int delUserList(String user_seq) throws Exception;

	/**
	 * 회원 정렬순서
	 * @return
	 * @throws Exception
	 */
	UserVO getLastUserOrder() throws Exception;

    /**
     * 관리자 목록 (ROLE)
     * @param user_role
     * @return
     * @throws Exception
     */
    List<UserVO> getUserListAsROLE(List<String> user_role) throws Exception;

    /**
     * 마이페이지 : 회원 정보 수정 (목록)
     * @param vo
     * @return
     * @throws Exception
     */
    int updMypageList(UserVO vo) throws Exception;

    /**
     * 마이페이지 : 회원 상세정보 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updMypageDetail(UserVO vo) throws Exception;


    /**
     * 중복가입 확인
     * @param vo
     * @return
     * @throws Exception
     */
    int checkDupInfo(UserVO vo) throws Exception;

    /**
     * user select box 회원 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<UserVO> getUserListOfSelectBox(UserVO vo) throws Exception;

    /**
     * 회원 상세보기 (아이디로 찾기)
     * @param vo
     * @return
     * @throws Exception
     */
    UserVO getUserDetailOfUserId(UserVO vo) throws Exception;


    /**
     * 마지막 업로드 날짜 저장
     * @param vo
     * @return
     * @throws Exception
     */
    int setLastUploadDate(DefaultVO vo) throws Exception;
}

