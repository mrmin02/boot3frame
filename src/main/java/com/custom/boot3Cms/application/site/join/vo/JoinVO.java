package com.custom.boot3Cms.application.site.join.vo;

import com.custom.boot3Cms.application.common.utils.EncryptUtil;
import com.custom.boot3Cms.application.mng.user.vo.UserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Data
@Getter
@Setter
public class JoinVO extends UserVO {

    /**
     * 로그인
     */
    @Schema(description = "회원 이름")
    private String username;
    @Schema(description = "회원 비밀번호")
    private String password;

    /**
     * 회원 구조체
     */
    @Schema(description = "아이디")
    private String user_id;
    @Schema(description = "이메일")
    private String user_email;
    @Schema(description = "아이디 중복체크 값")
    private String user_id_check;
    @Schema(description = "비밀번호 체크 값")
    private String pwd_check;
    @Schema(description = "회원 권한")
    private String user_auth;
    @Schema(description = "회원 상태", example = "대기, 승인, 반려 > 공통 코드 관리")
    private String user_status;
    @Schema(description = "탈퇴 여부")
    private String out_yn;

    @Schema(description = "회원 시퀀스")
    private String user_seq;
    @Schema(description = "회원 비밀번호")
    private String user_pwd;
    @Schema(description = "마지막 로그인 일자")
    private String last_login_date; // 마지막 로그인 일자
    @Schema(description = "마지막 로그인 IP")
    private String last_login_ip; // 마지막 로그인 IP
    @Schema(description = "등록자")
    private String inpt_seq; // 등록자
    @Schema(description = "등록일자")
    private String inpt_date; // 등록일자
    @Schema(description = "수정자")
    private String upd_seq; // 수정자
    @Schema(description = "수정일자")
    private String upd_date; // 수정일자
    @Schema(description = "회원 이름")
    private String user_name; // 회원 이름
    @Schema(description = "전화번호")
    private String phone_number;
    @Schema(description = "")
    private String dup_info;

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = EncryptUtil.fn_encryptSHA256(user_pwd);
    }
}
