package com.custom.boot3Cms.application.mng.user.vo;

import com.custom.boot3Cms.application.common.config.vo.DefaultVO;
import com.custom.boot3Cms.application.common.utils.EncryptUtil;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class UserVO extends DefaultVO {

    // FIXME 사용안하는 변수 정리 필요

    /* TODO tb_user_list set */
    String user_seq;
    String user_id;
    String user_pwd;
    String last_login_date;
    String last_login_ip;
    String inpt_seq;
    String inpt_date;
    String upd_seq;
    String upd_date;
    String out_yn;
    String user_auth;
    String user_status;

    /* TODO tb_user_detail set */
    String user_name;
    String author_name;
    String nickname;
    String gender;
    String birth;
    String solar;
    String zipcode;
    String addr;
    String addr_detail;
    String phone_number; // 휴대폰 번호
    String tel;
    String email;
    String author_type;
    String yearbook_yn;
    String eng_user_name;
    String profile_image;
    String profile_image_sys_name;
    String profile_path;
    String admin_memo;
    String mailing;
    String total_login_cnt;
    String site_type;
    String id_confirm;
    String sns_join_check;
    String sns_id;
    String youtube;
    String dup_info;
    String order_by_join;

    /* TODO tb_code에 값을 연계하여 사용하는 부분에 대하여 code_nm을 가져오기 위해 생성된 변수 정의 */
    String user_status_str;
    String user_auth_str;

    String user_pwd_req; // 비밀번호 검증값

    String admin_include_yn; //관리자포함여부
    String only_author_yn; //작가만 여부

    String one_week;

    String gallery_seq;
    String author_seq;

    String title;

    String prev_date;
    String next_date;

    String flag;
    String url;
    String author_id;

    String last_update_date;
    String contentType;

    String expired_date;
    String dday;
    String dday_status;
    String folder_type;
    String recent_inpt;

    public UserVO() {
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = EncryptUtil.fn_encryptSHA256(user_pwd);
    }

    @Builder(builderMethodName = "UserVO_list", buildMethodName = "UserVO_list")
    public UserVO(String USER_SEQ, String USER_ID, String USER_PWD, String LAST_LOGIN_DATE, String LAST_LOGIN_IP,
                  String INPT_SEQ, String INPT_DATE, String UPD_SEQ, String UPD_DATE, String OUT_YN, String USER_AUTH,
                  String USER_STATUS, String ADMIN_INCLUDE_YN, String ONLY_AUTHOR_YN) {
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
        this.admin_include_yn = ADMIN_INCLUDE_YN;
        this.only_author_yn = ONLY_AUTHOR_YN;
    }

    @Builder(builderMethodName = "UserVO_detail", buildMethodName = "UserVO_detail")
    public UserVO(String USER_SEQ, String USER_NAME, String AUTHOR_NAME, String NICKNAME, String GENDER, String BIRTH,
                  String SOLAR, String ZIPCODE, String ADDR, String ADDR_DETAIL, String PHONE_NUMBER, String TEL,
                  String EMAIL, String AUTHOR_TYPE, String YEARBOOK_YN, String ENG_USER_NAME, String PROFILE_IMAGE,
                  String PROFILE_IMAGE_SYS_NAME, String PROFILE_PATH, String ADMIN_MEMO, String TOTAL_LOGIN_CNT, String SITE_TYPE, String ID_CONFIRM,
                  String SNS_JOIN_CHECK, String SNS_ID, String YOUTUBE, String DUP_INFO, String FLAG) {
        this.user_seq = USER_SEQ;
        this.user_name = USER_NAME;
        this.author_name = AUTHOR_NAME;
        this.nickname = NICKNAME;
        this.gender = GENDER;
        this.birth = BIRTH;
        this.solar = SOLAR;
        this.zipcode = ZIPCODE;
        this.addr = ADDR;
        this.addr_detail = ADDR_DETAIL;
        this.phone_number = PHONE_NUMBER;
        this.tel = TEL;
        this.email = EMAIL;
        this.author_type = AUTHOR_TYPE;
        this.yearbook_yn = YEARBOOK_YN;
        this.eng_user_name = ENG_USER_NAME;
        this.profile_image = PROFILE_IMAGE;
        this.profile_image_sys_name = PROFILE_IMAGE_SYS_NAME;
        this.profile_path = PROFILE_PATH;
        this.admin_memo = ADMIN_MEMO;
        this.total_login_cnt = TOTAL_LOGIN_CNT;
        this.site_type = SITE_TYPE;
        this.id_confirm = ID_CONFIRM;
        this.sns_join_check = SNS_JOIN_CHECK;
        this.sns_id = SNS_ID;
        this.youtube = YOUTUBE;
        this.dup_info = DUP_INFO;
        this.flag = FLAG;
    }
}
