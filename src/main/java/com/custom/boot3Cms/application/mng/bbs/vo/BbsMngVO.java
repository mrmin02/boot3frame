package com.custom.boot3Cms.application.mng.bbs.vo;

import com.custom.boot3Cms.application.common.config.vo.DefaultVO;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시판 관리 구조체
 *
 * @author SEKOREA
 * @version 1.1
 * @see <pre>
 *  Modification Information
 *
 * 	수정일       / 수정자    / 수정내용
 * 	------------------------------------------
 * 	2017-11-10 / 김기식	  / 최초 생성
 * 	2018-03-18 / 에스이코리아	  / 프레임워크 구조 변경
 * </pre>
 * @since 2017-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BbsMngVO extends DefaultVO {

    /**
     * TB_BBS_AUTH
     */
    @Getter @Setter
    private String user_auth; // 권한 (공통코드)
    @Getter @Setter
    private String read_yn; // 읽기 가능 여부
    @Getter @Setter
    private String write_yn; // 쓰기 가능 여부
    @Getter @Setter
    private String update_yn; // 수정 가능 여부
    @Getter @Setter
    private String delete_yn; // 삭제 가능 여부

    /**
     * TB_BBS_CATEGORY
     */
    @Getter @Setter
    private String category_nm; // 카테고리 명
    @Getter @Setter
    private String category_del_yn; // 카테고리 삭제여부
    @Getter @Setter
    private String category_order; // 카테고리 순서
    @Getter @Setter
    private String category_cd; // 카테고리 코드

    /**
     * TB_BBS_DETAIL
     */
    @Getter @Setter
    private String file_cnt; // 파일갯수
    @Getter @Setter
    private String bbs_type; // PC 게시판 유형
    @Getter @Setter
    private String mobile_bbs_type; // 모바일 게시판 유형
    @Getter @Setter
    private String bbs_dft_list_cnt; // 게시판 목록수
    @Getter @Setter
    private String bbs_page_cnt = "10"; // 게시판 페이지수
    @Setter
    private String attach_file_use_yn; // 첨부파일 사용여부
    @Setter
    private String comment_use_yn; // 댓글 사용 여부
    @Setter
    private String reply_use_yn; // 답글 사용 여부
    @Setter
    private String notice_use_yn; // 공지사항 사용 여부
    @Setter
    private String secret_use_yn; // 비밀글 사용 여부
    @Setter
    private String category_use_yn; // 카테고리 사용 여부
	@Setter
	private String pwd_use_yn; // 비밀번호 사용 여부
    @Setter
    private String date_range_use_yn; // 기간설정 사용 여부

    /**
     * TB_BBS_FILE_INFO
     */
    @Getter @Setter
    private String file_size; // 첨부파일 크기
    @Getter @Setter
    private String file_type; // 첨부파일 형식

    /**
     * TB_BBS_INFO
     */
    @Getter @Setter
    private String bbs_info_seq; // 게시판 정보 순번
    @Getter @Setter
    private String bbs_cd; // 게시판 코드
    @Getter @Setter
    private String bbs_nm; // 게시판 이름
    @Getter @Setter
    private String bbs_remark; // 게시판 설명
    @Setter
    private String bbs_use_yn; // 게시판 사용 여부
    @Getter @Setter
    private String bbs_del_yn; // 게시판 삭제여부
    @Getter @Setter
    private String inpt_seq; // 등록자
    @Getter @Setter
    private String inpt_date; // 등록일자
    @Getter @Setter
    private String upd_seq; // 수정자
    @Getter @Setter
    private String upd_date; // 수정일자
    @Getter @Setter
    private String date_remark; // 날짜 설명

    @Getter @Setter
    private String bbs_cd_check_yn; // 코드 중복검사 여부
    @Getter @Setter
    private String site_type; //사이트 타입


    @Getter @Setter
    private String data_range_use_yn;

    private String flag;

    public BbsMngVO(){}

    public BbsMngVO(String bbs_info_seq){
        this.bbs_info_seq = bbs_info_seq;
    }

    public BbsMngVO(String site_type, String bbs_type) { this.site_type = site_type; this.bbs_type = bbs_type;}

    public String getAttach_file_use_yn() {
        return StringUtil.isEmpty(attach_file_use_yn) ? "N" : attach_file_use_yn;
    }

    public String getComment_use_yn() {
        return StringUtil.isEmpty(comment_use_yn) ? "N" : comment_use_yn;
    }

    public String getReply_use_yn() {
        return StringUtil.isEmpty(reply_use_yn) ? "N" : reply_use_yn;
    }

    public String getNotice_use_yn() {
        return StringUtil.isEmpty(notice_use_yn) ? "N" : notice_use_yn;
    }

    public String getSecret_use_yn() {
        return StringUtil.isEmpty(secret_use_yn) ? "N" : secret_use_yn;
    }

    public String getCategory_use_yn() {
        return StringUtil.isEmpty(category_use_yn) ? "N" : category_use_yn;
    }

    public String getBbs_use_yn() {
        return StringUtil.isEmpty(bbs_use_yn) ? "N" : bbs_use_yn;
    }


}
