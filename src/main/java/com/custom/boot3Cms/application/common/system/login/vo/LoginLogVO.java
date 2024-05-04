package com.custom.boot3Cms.application.common.system.login.vo;

import com.custom.boot3Cms.application.common.system.login.service.LoginLogService;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 로그인 이력 구조체
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
 * @since 2018-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class LoginLogVO {
	private String user_login_log_seq;

	private String user_id;

	private String suc_yn;

	private String err_cnt;

	private String login_ip;

	private String last_try_date;

	private String inpt_date;

	private String lock_until;

	@Resource(name = "loginLogService")
	LoginLogService loginLogService;

	public LoginLogVO() {
	}

	public LoginLogVO(String user_id, String login_ip) {
		this.user_id = user_id;
		this.login_ip = login_ip;
	}

	/**
	 * 로그인 금지 여부
	 * @return
	 */
	public boolean isLoginBanned() throws Exception {
		// 오류 카운트에 의한 로그인 차단
		Date loginBanUntil = computeLoginBannedUntil();

		// 오류 카운트에 의한 로그인 차단 여부
		boolean isLoginBanned = loginBanUntil != null;

		return isLoginBanned;
	}

	/**
	 * 로그인 금지 종료 일시
	 * @return 로그인 금지시 종료일시. 로그인 가능시 null
	 * @throws Exception
	 */
	public Date computeLoginBannedUntil() throws Exception {
		if (loginLogService.getLoginBanErrCnt() <= 0) {
	 		// 로그인 차되기까지 로그인 실패 카운트가 0 이하인 경우 로그인 항상 허용
			return null;
		}

		int errCnt = 0;

		if ("Y".equals(getSuc_yn())) {
			// 최근 로그인 이력이 로그인 성공인 경우 로그인 허용 (에러 횟수가 있어도 관계없음)

			return null;
		}

		if (CommonUtil.isNumber(getErr_cnt())) {
			try {
				errCnt = Integer.parseInt(getErr_cnt());
			} catch (Exception ignored) {
				// 무시
			}
		}

		if (errCnt < loginLogService.getLoginBanErrCnt()) {
			// 오류 횟수가 초과하지 않은 경우 로그인 허용
			return null;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date lastTryDate = simpleDateFormat.parse(getLast_try_date());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastTryDate);
		calendar.add(Calendar.SECOND, loginLogService.getLoginBanDuration());
		Date nextTryDate = calendar.getTime();

		Date now = new Date();

		if (nextTryDate.before(now)) {
			// 재시도 시간 이후인 경우 로그인 허용
			return null;
		}

		// 로그인 허용 않음
		return nextTryDate;
	}
}
