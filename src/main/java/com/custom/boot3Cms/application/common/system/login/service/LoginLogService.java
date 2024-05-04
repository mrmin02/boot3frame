package com.custom.boot3Cms.application.common.system.login.service;

import com.custom.boot3Cms.application.common.system.login.mapper.LoginLogMapper;
import com.custom.boot3Cms.application.common.system.login.vo.LoginLogVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 로그인 로그 서비스
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2022-09-07 / coding	 / 최초 생성
 *
 * </pre>
 * @since 2018-03-06
 */
@Service("loginLogService")
public class LoginLogService {

	@Resource(name = "loginLogMapper")
	LoginLogMapper loginLogMapper;

	@Autowired
	Environment env;

	/**
	 * 회원 최근 실패 로그인 이력 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public LoginLogVO getLastFailedLoginLog(LoginLogVO vo) throws Exception {
		/*
		 * 최근 로그인 실패 내역을 조회하며, 최근 로그인 내역이 성공인 경우, 새로운 레코드 생성
		 */

		LoginLogVO lastLoginLog = loginLogMapper.getLastLoginLog(vo);

		if (lastLoginLog != null && "N".equals(lastLoginLog.getSuc_yn())) {
			return lastLoginLog;
		}

		lastLoginLog = new LoginLogVO(vo.getUser_id(), vo.getLogin_ip());
		lastLoginLog.setErr_cnt("0");

		return lastLoginLog;
	}

	/**
	 * 성공 로그 남기기
	 * @param vo
	 * @return 영향받은 행 수
	 */
	public int setSuccessLog(LoginLogVO vo) throws Exception {
		boolean isNew = vo.getUser_login_log_seq() == null || "0".equals(vo.getUser_login_log_seq());

		vo.setSuc_yn("Y");

		if (isNew) {
			vo.setErr_cnt("0");

			int rowCount = loginLogMapper.setUserLoginLog(vo);

			// 로그인 잠금 기한 클리어
			loginLogMapper.updClearLockUntil(vo);

			return rowCount;
		} else {
			return loginLogMapper.updUserLoginLog(vo);
		}
	}

	/**
	 * 실패 로그 남기기
	 * @param vo
	 * @return 영향받은 행 수
	 */
	public int setErrorLog(LoginLogVO vo) throws Exception {
		boolean isNew = vo.getUser_login_log_seq() == null || "0".equals(vo.getUser_login_log_seq());

		vo.setLast_try_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		vo.setSuc_yn("N");

		if (isNew) {
			// 신규인 경우 에러 카운트 1개

			vo.setErr_cnt("1");

			Date loginBannedUntil = vo.computeLoginBannedUntil();

			if (loginBannedUntil != null) {
				vo.setLock_until(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(loginBannedUntil));
			}

			return loginLogMapper.setUserLoginLog(vo);
		} else {
			// 수정인 경우 에러카운트를 1 증가.

			int errCnt = 0;

			if (CommonUtil.isNumber(vo.getErr_cnt())) {
				errCnt = Integer.parseInt(vo.getErr_cnt());
			}

			// 실제 카운트 증가는 DB 상에서 하지만, 아래의 getLoginBannedUntil에서 차단 여부를 정하기 위해 에러카운트를 +1 해줌
			vo.setErr_cnt(String.valueOf(errCnt + 1));

			Date loginBannedUntil = vo.computeLoginBannedUntil();

			if (loginBannedUntil != null) {
				vo.setLock_until(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(loginBannedUntil));
			}

			return loginLogMapper.updUserLoginLog(vo);
		}
	}

	/**
	 * 로그인 차단되기까지 로그인 실패 카운트
	 * @return
	 */
	public int getLoginBanErrCnt() {
		String strLoginBanErrcnt = env.getProperty("login.ban.errcnt");

		try {
			return Integer.parseInt(strLoginBanErrcnt);
		} catch (Exception ex) {
			return -1;
		}
	}

	/**
	 * 로그인 실패 횟수가 getLoginBanErrCnt를 넘어설 경우 로그인 차단 시간 (기본 1분)
	 * @return
	 */
	public int getLoginBanDuration() {
		String strLoginBanDuration = env.getProperty("login.ban.duration");

		try {
			return Integer.parseInt(strLoginBanDuration);
		} catch (Exception ex) {
			return 60;
		}
	}
}
