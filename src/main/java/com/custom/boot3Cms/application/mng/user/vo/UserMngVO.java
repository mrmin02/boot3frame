package com.custom.boot3Cms.application.mng.user.vo;


import com.custom.boot3Cms.application.common.config.vo.DefaultVO;
import com.custom.boot3Cms.application.common.utils.EncryptUtil;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 회원관리 구조체
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2018-03-04 / 전성진  / 최초 생성
 * </pre>
 * @since 2018-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserMngVO extends DefaultVO {

    @Getter @Setter
    private String user_seq; // 회원 순번
    @Getter @Setter
    private String user_id; // 회원 아이디
    @Getter @Setter
    private String email; // 회원 메일
    @Getter
    private String user_pwd; // 회원 비밀번호
    @Getter @Setter
    private String last_login_date; // 마지막 로그인 일자
    @Getter @Setter
    private String last_login_ip; // 마지막 로그인 IP
    @Getter @Setter
    private String inpt_seq; // 등록자
    @Getter @Setter
    private String inpt_date; // 등록일자
    @Getter @Setter
    private String upd_seq; // 수정자
    @Getter @Setter
    private String upd_date; // 수정일자
    @Getter @Setter
    private String out_yn; // 회원 탈퇴여부
    @Getter @Setter
    private String user_auth; // 회원 권한
    @Getter @Setter
    private String user_status; // 회원상태 (대기, 승인, 반려) (공통코드)
    @Getter @Setter
    private String user_name; // 회원 이름
    @Getter @Setter
    private String user_pwd_req; // 비밀번호 검증값

    @Getter @Setter
    private String inpt_user_name;
    @Getter @Setter
    private String upd_user_name;

    @Getter @Setter
    private String phone_number;

    @Getter @Setter
    private String lock_until;

    public UserMngVO(){}

    public UserMngVO(String user_seq){
        this.user_seq = user_seq;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = EncryptUtil.fn_encryptSHA256(user_pwd);
    }

}
