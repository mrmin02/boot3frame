package com.custom.boot3Cms.application.mng.user.vo;

import com.custom.boot3Cms.application.common.config.vo.DefaultVO;
import com.custom.boot3Cms.application.common.utils.EncryptUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@Schema(name = "사용자 DTO")
public class UserVO extends DefaultVO {

    // FIXME 사용안하는 변수 정리 필요

    /* TODO tb_user_list set */

    @Schema(description = "사용자 시퀀스")
    String user_seq;
    @Schema(description = "사용자 아이디")
    String user_id;
    @Schema(description = "사용자 비밀번호")
    String user_pwd;
    @Schema(description = "마지막 로그인 날짜", hidden = true)
    String last_login_date;
    @Schema(description ="마지막 로그인 IP ", hidden = true)
    String last_login_ip;
    @Schema(description ="등록자 시퀀스 ", hidden = true)
    String inpt_seq;
    @Schema(description ="등록일", hidden = true)
    String inpt_date;
    @Schema(description ="수정자 시퀀스", hidden = true)
    String upd_seq;
    @Schema(description ="수정일", hidden = true)
    String upd_date;
    @Schema(description ="탈퇴여부", hidden = true)
    String out_yn;
    @Schema(description ="사용자 권한", hidden = true)
    String user_auth;
    @Schema(description ="사용자 가입 상태", hidden = true)
    String user_status;

    /* TODO tb_user_detail set */

    @Schema(description ="이름", hidden = true)
    String user_name;
    @Schema(description ="별명", hidden = true)
    String nickname;
    @Schema(description ="성별", hidden = true)
    String gender;
    @Schema(description ="생년월일", hidden = true)
    String birth;
    @Schema(description ="우편번호", hidden = true)
    String zipcode;
    @Schema(description ="주소", hidden = true)
    String addr;
    @Schema(description ="상세주소", hidden = true)
    String addr_detail;
    @Schema(description ="휴대폰 번호", hidden = true)
    String phone_number; // 휴대폰 번호
    @Schema(description ="전화번호", hidden = true)
    String tel;
    @Schema(description ="이메일", hidden = true)
    String user_email;
    @Schema(description ="관리자 메모", hidden = true)
    String admin_memo;
    @Schema(description ="전체 로그인 카운트", hidden = true)
    String total_login_cnt;
    @Schema(description ="", hidden = true)
    String dup_info;
    @Schema(description ="", hidden = true)
    String order_by_join;

    /* TODO tb_code에 값을 연계하여 사용하는 부분에 대하여 code_nm을 가져오기 위해 생성된 변수 정의 */
    @Schema(description ="", hidden = true)
    String user_status_str;
    @Schema(description ="", hidden = true)
    String user_auth_str;

    @Schema(description ="비밀번호 검증 값", hidden = true)
    String user_pwd_req; // 비밀번호 검증값

    @Schema(description ="", hidden = true)
    String prev_date;
    @Schema(description ="", hidden = true)
    String next_date;

    @Schema(description ="", hidden = true)
    String flag;

    @Schema(description ="마지막 수정일", hidden = true)
    String last_update_date;

    public UserVO() {
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = EncryptUtil.fn_encryptSHA256(user_pwd);
    }

    @Builder(builderMethodName = "UserVO_list", buildMethodName = "UserVO_list")
    public UserVO(String USER_SEQ, String USER_ID, String USER_PWD, String LAST_LOGIN_DATE, String LAST_LOGIN_IP,
                  String INPT_SEQ, String INPT_DATE, String UPD_SEQ, String UPD_DATE, String OUT_YN, String USER_AUTH,
                  String USER_STATUS) {
        this.user_seq = USER_SEQ;
        this.user_id = USER_ID;
        /** TODO SHA-256 적용 */
        this.user_pwd = USER_PWD;
        this.last_login_date = LAST_LOGIN_DATE;
        this.last_login_ip = LAST_LOGIN_IP;
        this.inpt_seq = INPT_SEQ;
        this.inpt_date = INPT_DATE;
        this.upd_seq = UPD_SEQ;
        this.upd_date = UPD_DATE;
        this.out_yn = OUT_YN;
        this.user_auth = USER_AUTH;
        this.user_status = USER_STATUS;
    }

    @Builder(builderMethodName = "UserVO_detail", buildMethodName = "UserVO_detail")
    public UserVO(String USER_SEQ, String USER_NAME, String NICKNAME, String GENDER, String BIRTH,
                  String ZIPCODE, String ADDR, String ADDR_DETAIL, String PHONE_NUMBER, String TEL,
                  String USER_EMAIL, String ADMIN_MEMO, String TOTAL_LOGIN_CNT, String DUP_INFO, String FLAG) {
        this.user_seq = USER_SEQ;
        this.user_name = USER_NAME;
        this.nickname = NICKNAME;
        this.gender = GENDER;
        this.birth = BIRTH;
        this.zipcode = ZIPCODE;
        this.addr = ADDR;
        this.addr_detail = ADDR_DETAIL;
        this.phone_number = PHONE_NUMBER;
        this.tel = TEL;
        this.user_email = USER_EMAIL;
        this.admin_memo = ADMIN_MEMO;
        this.total_login_cnt = TOTAL_LOGIN_CNT;
        this.dup_info = DUP_INFO;
        this.flag = FLAG;
    }
}
