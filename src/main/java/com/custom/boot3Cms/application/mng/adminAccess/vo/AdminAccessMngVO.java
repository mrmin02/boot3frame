package com.custom.boot3Cms.application.mng.adminAccess.vo;


import com.custom.boot3Cms.application.common.config.vo.DefaultVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 관리자 페이지 접근 가능 IP 관리 VO
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-06-19 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-06-19 */
@Data
@EqualsAndHashCode(callSuper = false)
@Getter @Setter
public class AdminAccessMngVO extends DefaultVO {

    private String admin_access_seq;
    private String admin_access_nm;
    private String admin_access_ip;
    private String use_yn;
    private String del_yn;
    private String inpt_user_name;
    private String inpt_seq;
    private String inpt_date;
    private String upd_seq;
    private String upd_date;
    private String flag;

}
