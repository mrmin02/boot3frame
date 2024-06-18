package com.custom.boot3Cms.application.mng.popup.vo;


import com.custom.boot3Cms.application.common.config.vo.DefaultVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 팝업 관리 VO
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
@EqualsAndHashCode(callSuper = false)
@Data
@Getter @Setter
public class PopupMngVO extends DefaultVO {

    private String popup_seq;       // seq
    private String popup_title;     // 제목
    private String popup_type;      // 타입
    private String popup_order;     // 순서
    private String content;         // 내용
    private String link_type;       // 링크 타입
    private String link_url;        // 링크 url
    private String remark;          // 설명
    private String del_yn;          // 삭제여부
    private String use_yn;          // 사용여부
    private String inpt_seq;        // 등록자 seq
    private String inpt_date;       // 등록일
    private String upd_seq;         // 수정자 seq
    private String upd_date;        // 수정일
    private String popup_start_date; // 배너 시작일자
    private String popup_end_date; // 배너 종료일자
    private String inpt_user_name;  // 등록자
    private String upd_user_name;   // 수정자
    private String flag;

	private String TABLE_SEQ;  		// TB_FILES 의 테이블 SEQ
	private String FILE_SYS_NM;		// TB_FILES 의 파일 명
	private String FILE_NM;			// TB_FILES 의 실제 파일 명
	private String FILE_PATH;		// TB_FILES 의 파일 path


    public PopupMngVO(){}

    public PopupMngVO(String popup_seq){
        this.popup_seq = popup_seq;
    }

}
