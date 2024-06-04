package com.custom.boot3Cms.application.site.page.vo;


import com.custom.boot3Cms.application.common.config.vo.DefaultVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


/**
 * HTML 정보 VO
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2018-08-07 / 에스이코리아  / 최초 생성
 * </pre>
 * @since 2018-08-07
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Getter
@Setter
public class PageVO extends DefaultVO {

    private String html_seq; // 컨텐츠 순번
    private String html_title; // 컨텐츠 제목
    private String html_content; // 컨텐츠 내용
    private String inpt_seq; // 등록자
    private String inpt_date; // 등록일
    private String upd_seq; // 수정자
    private String upd_date; // 수정일자
    private String del_yn; // 삭제여부

}
