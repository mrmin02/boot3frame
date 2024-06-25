package com.custom.boot3Cms.application.mng.menu.vo;


import com.custom.boot3Cms.application.common.config.vo.DefaultVO;
import com.custom.boot3Cms.application.mng.user.vo.UserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 메뉴 관리 VO
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-06-25 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-06-25 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class MenuMngVO extends DefaultVO {

    private String access_code;
    private String inpt_date;
    private String inpt_seq;
    private String menu_depth;
    private String menu_info_seq;
    private String menu_json;
    private String menu_link;
    private String menu_link_seq;
    private String menu_link_title;
    private String menu_link_type;
    private String menu_name;
    private String menu_order;
    private String menu_seq;
    private String menu_title;
    private String menu_type;
    private String ord;
    private String prt_seq;
    private String remark;
    private String upd_date;
    private String upd_seq;
    private String show_yn;
    private String highlight_yn;
    private String use_yn;
    private String user_auth;
    private String user_seq;
    private String update_menu;
    private String id;
    private String parent;
    private String depth;
    private String flag;
    private List<MenuMngVO> children = new ArrayList<>();

    private List<UserVO> userVOList; // 관리자 정보 목록

    public MenuMngVO() {
    }

    public MenuMngVO(String menu_seq) {
        this.prt_seq = menu_seq;
    }

    public MenuMngVO(String menu_info_seq, String menu_seq){
        this.menu_info_seq = menu_info_seq;
        this.menu_seq = menu_seq;
    }

}
