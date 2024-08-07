package com.custom.boot3Cms.application.common.system.login.mapper;

import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 로그인 매퍼
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2018-03-06 / 최재민	 / 최초 생성
 * 	2020-08-28 / 최민석	 / sso 로그인을 위한 계정검색 추가
 * </pre>
 * @since 2018-03-06
 */
@Mapper
public interface LoginMapper {

	/**
	 * 회원 아이디 비밀번호 검증
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	LoginVO getUser(LoginVO vo) throws Exception;

//	/**
//	 * 휴면 회원 아이디 비밀번호 검증
//	 * @param vo
//	 * @return
//	 */
//	LoginVO getUserRest(LoginVO vo);

	/**
	 * 회원 상세정보
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	LoginVO getUserDetail(LoginVO vo) throws Exception;

	/**
	 * 회원 로그인 일자 및 IP 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updUserLoginDateIp(LoginVO vo) throws Exception;

	/**
	 * 회원 로그인 횟수 업
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updUserLoginTotalCntUp(LoginVO vo) throws Exception;


//	LoginVO getUserSso(LoginVO vo) throws  Exception;

	/**
	 * 관리자페이지 접속 로그 저장
	 * @param vo
	 * @throws Exception
	 */
	void setAdminLog(LoginVO vo) throws Exception;

	/**
	 * 회원 토큰정보 저장
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int setUserToken(LoginVO vo) throws Exception;

	/**
	 * Access Token 블랙 리스트 등록
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int setBlackAccessToken(LoginVO vo) throws Exception;

	/**
	 * refresh Token 블랙 리스트 등록
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int setBlackRefreshToken(LoginVO vo) throws Exception;

	/**
	 * 토큰 블랙리스트 검증
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int checkBlackToken(LoginVO vo) throws Exception;

}
