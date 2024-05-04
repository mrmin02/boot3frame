package com.custom.boot3Cms.application.common.system.login.mapper;

import com.custom.boot3Cms.application.common.system.login.vo.LoginLogVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 로그인 로그 매퍼
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2022-09-07 / coding	 / 최초 생성
 * </pre>
 * @since 2022-09-07
 */
@Mapper
public interface LoginLogMapper {
	/**
	 * 회원 로그인 이력 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	LoginLogVO getLastLoginLog(LoginLogVO vo) throws Exception;

	/**
	 * 회원 로그인 일자 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int setUserLoginLog(LoginLogVO vo) throws Exception;

	/**
	 * 회원 로그인 일자 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updUserLoginLog(LoginLogVO vo) throws Exception;

	/**
	 * 회원 로그인 오류 업데이트
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updUserLoginLogError(LoginLogVO vo) throws Exception;

	/**
	 * 잠금 기한 초기화
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	int updClearLockUntil(LoginLogVO vo) throws Exception;
}
