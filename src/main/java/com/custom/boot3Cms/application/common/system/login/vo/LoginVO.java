package com.custom.boot3Cms.application.common.system.login.vo;

import com.custom.boot3Cms.application.common.utils.EncryptUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 로그인 구조체
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2018-03-06 / 최재민	 / 최초 생성
 * </pre>
 * @since 2018-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class LoginVO implements UserDetails {

    /**
     * 로그인
     */
    private String username;
    private String password;

    /**
     * 회원 구조체
     */
    private String user_id; // 회원 아이디
    private String user_pwd; // 회원 비밀번호
    private String pwd_check; // 아이디 비밀번호 체크값
    private String user_auth; // 회원 권한
    private String user_status; // 회원상태 (대기, 승인, 반려) (공통코드)
    private String out_yn; // 회원 탈퇴여부


    private String user_seq; // 회원 시퀀스
    private String last_login_date; // 마지막 로그인 일자
    private String last_login_ip; // 마지막 로그인 IP
    private String inpt_seq; // 등록자
    private String inpt_date; // 등록일자
    private String upd_seq; // 수정자
    private String upd_date; // 수정일자
    private String user_name; // 회원 이름
    private String author_name;
    private String nickname;

    private String profile_image;
    private String profile_image_sys_name;
    private String profile_path;

    private String phone_number;
    private String tel;
    private String email;

    private String user_pwd_req; // 비밀번호 검증값

    private String gender;
    private String birth;
    private String solar;
    private String zipcode;
    private String addr;
    private String addr_detail;
    private String author_type;
    private String yearbook_yn;
    private String eng_user_name;
    private String inter_number;
    private String homepage;
    private String blog;
    private String twitter;
    private String facebook;
    private String grafolio;
    private String notefolio;
    private String introduce;
    private String eng_introduce;
    private String admin_memo;
    private String regular_first_date;
    private String regular_last_date;
    private String recom_date;
    private String picturebook_link;
    private String mailing;
    private String career_year;
    private String author_gallery_read_cnt;
    private String pinterest;
    private String instagram;
    private String total_login_cnt;
    private String site_type;
    private String id_confirm;
    private String sns_join_check;
    private String sns_id;
    private String youtube;
    private String dup_info;
    private String flag;


    /**
     * JWT
     */
    private String token_seq;
    private String access_token;
    private String refresh_token;

    public LoginVO() {

    }

    public LoginVO(String username, String password) {
        this.user_id = username;
        this.user_pwd = EncryptUtil.fn_encryptSHA256(password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
