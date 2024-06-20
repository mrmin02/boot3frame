package com.custom.boot3Cms.application.mng.code.vo;


import com.custom.boot3Cms.application.common.config.vo.DefaultVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 관리자 코드관리 VO
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2017-09-06 / 최재민	  / 최초 생성
 * </pre>
 * @since 2017-09-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Getter @Setter
public class CodeMngVO extends DefaultVO {

    private String code_seq;
    private String code;
    private String code_nm;
    private String prt_seq;
    private String use_yn;
    private String remark;
    private String inpt_seq;
    private String upd_seq;
    private String inpt_date;
    private String ord;

    private String flag;
    private String code_check_yn;
    private String id;
    private String parent;
    private String text;
    private Map<String, Boolean> state;

    /* 산그림 권한관리용도 */
    private String code_detail;
    private String code_detail_nm;

    public CodeMngVO(){}

    public CodeMngVO(String id, String text, String parent, Map<String, Boolean> state, String code){
        this.id = id;
        this.text = text;
        this.parent = parent;
        this.state = state;
        this.code = code;
    }

}
