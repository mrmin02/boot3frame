package com.custom.boot3Cms.application.common.system.file.vo;


import com.custom.boot3Cms.application.common.config.vo.DefaultVO;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 파일관리 VO
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2018-03-04 / 최재민  / 최초 생성
 * </pre>
 * @since 2018-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Getter @Setter
public class FileMngVO extends DefaultVO {

    private String file_seq; // 파일 순번
    private String table_nm; // 테이블 명
    private String table_seq; // 해당 테이블 순번
    private String file_type; // 파일 확장자
    private String inpt_seq; // 등록자 순번
    private String inpt_date; // 등록일
    private String file_flag;
    private String thumbnail_yn; // 썸네일 유무

    private String auth_type;

    private boolean fileResult;

    public  FileMngVO() {}
    public FileMngVO(String table_seq, String table_nm) {
        this.table_seq = table_seq;
        this.table_nm = table_nm;
    }

    /**
     * 목록 / 삭제에 생성될 파일 생성자
     * @param table_nm
     * @param table_seq
     * @param file_seq
     */
    public FileMngVO(String table_nm, String table_seq, String file_seq){
        this.table_nm = table_nm;
        this.table_seq = table_seq;
        if(StringUtil.isNotEmpty(file_seq)){
            this.file_seq = file_seq;
        }
    }

    /**
     * 목록 / 삭제에 생성될 파일 생성자 (썸네일 구분)
     * @param table_nm
     * @param table_seq
     * @param file_seq
     * @param thumbnail_yn
     */
    public FileMngVO(String table_nm, String table_seq, String file_seq, String thumbnail_yn){
        this.table_nm = table_nm;
        this.table_seq = table_seq;
        if(StringUtil.isNotEmpty(file_seq)){
            this.file_seq = file_seq;
        }
        this.thumbnail_yn = thumbnail_yn;
    }

    /**
     * 등록시 사용될 파일 생성자
     * @param table_nm
     * @param table_seq
     * @param file_sys_nm
     * @param file_nm
     * @param file_path
     * @param inpt_seq
     */
    public FileMngVO(String table_nm, String table_seq, String file_type, String file_sys_nm, String file_nm, String file_path, String inpt_seq, boolean fileResult) {
        this.table_nm = table_nm;
        this.table_seq = table_seq;
        this.file_type = file_type;
        super.setFile_sys_nm(file_sys_nm);
        super.setFile_nm(file_nm);
        super.setFile_path(file_path);
        this.inpt_seq = inpt_seq;
        this.fileResult = fileResult;
    }

    public FileMngVO(String table_nm, String table_seq, String file_type, String file_sys_nm, String file_nm, String file_path, String inpt_seq, boolean fileResult, String thumbnail_yn) {
        this.table_nm = table_nm;
        this.table_seq = table_seq;
        this.file_type = file_type;
        super.setFile_sys_nm(file_sys_nm);
        super.setFile_nm(file_nm);
        super.setFile_path(file_path);
        this.inpt_seq = inpt_seq;
        this.fileResult = fileResult;
        this.thumbnail_yn = thumbnail_yn;
    }

}
