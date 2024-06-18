package com.custom.boot3Cms.application.mng.html.vo;


import com.custom.boot3Cms.application.common.config.vo.DefaultVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Html 관리 VO
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-06-18 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-06-18 */
@Data
@EqualsAndHashCode(callSuper = false)
@Getter @Setter
public class HtmlMngVO extends DefaultVO {

    private String html_seq;        // seq
    private String html_title;      // 제목
    private String html_content;    // 내용
    private String inpt_seq;        // 등록자 seq
    private String inpt_date;       // 등록일
    private String upd_seq;         // 수정자 seq
    private String upd_date;        // 수정일
    private String inpt_user_name;  // 등록자
    private String upd_user_name;   // 수정자
    private String del_yn;          // 삭제 여부
    private String flag;

    public HtmlMngVO(){}

    public HtmlMngVO(String html_seq){
        this.html_seq = html_seq;
    }

}
