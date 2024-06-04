package com.custom.boot3Cms.application.site.menu.vo;

import lombok.Data;

import java.util.List;

/**
 * 메뉴 구조체
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-06-04 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-06-04 */
@Data
public class MenuVO {

    private String menu_seq;
    private String menu_title;
    private String menu_type;
    private String menu_link;
    private String menu_link_type;
    private String prt_seq;
    private String use_yn;
    private String ord;
    private String remark;
    private String inpt_seq;
    private String inpt_date;
    private String upd_seq;
    private String upd_date;
    private String user_auth;
    private String access_code;
    private String menu_json;
    private String menu_order;
    private String menu_depth;

    private String menu_link_seq;
    private String menu_link_title;

    private List<MenuVO> sub_menu_list;


    public MenuVO(){}

    public MenuVO(String menu_seq){
        this.prt_seq = menu_seq;
    }

}
