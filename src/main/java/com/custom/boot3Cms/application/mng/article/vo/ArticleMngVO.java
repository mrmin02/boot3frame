package com.custom.boot3Cms.application.mng.article.vo;

import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.bbs.vo.BbsMngVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 관리 구조체
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
public class ArticleMngVO extends BbsMngVO {

    /**
     * TB_ARTICLES
     */
    @Getter @Setter
    private String article_seq; // 순번
    @Getter @Setter
    private String subject; // 제목
    @Setter
    private String secret_yn; // 비밀글 여부
	@Setter
	private String pwd_yn; // 비밀번호 여부
    @Setter
    private String notice_yn; // 공지사항 여부
    @Getter @Setter
    private String content; // 내용
    @Getter @Setter
    private String prt_grp; // 부모 그룹
    @Getter @Setter
    private String prt_seq; // 부모 순번
    @Getter @Setter
    private String bbs_order; // 게시글 순번
    @Getter @Setter
    private String read_cnt; // 조회수
    @Getter @Setter
    private String inpt_ip; // 등록자 IP
    @Getter @Setter
    private String upd_ip; // 수정자 IP
    @Getter @Setter
    private String user_id; // 비회원 아이디
    @Getter @Setter
    private String user_pwd; // 비회원 비밀번호
    @Getter @Setter
    private String del_yn; // 삭제여부

    @Getter @Setter
    private String link_url; // 링크 url
    @Getter @Setter
    private String link_type; // 링크 타입
    @Getter @Setter
    private String hide_yn; // 숨김 여부
    @Getter @Setter
    private String article_start_date; // 참여시작일자
	@Getter @Setter
	private String article_end_date; // 참여종료일자
	@Getter @Setter
	private String pwd; // 패스워드
    @Getter @Setter
    private String inpt_user_name;
    @Getter @Setter
    private String inpt_user_dept; // 담당부서
    @Getter @Setter
    private String inpt_user_type; // 등록자 타입
    @Getter @Setter
    private String upd_user_type; // 수정자 타입
    @Getter @Setter
    private String manager; // 공모 주최
    @Getter @Setter
    private String place; // 전시 장소
    @Getter @Setter
    private String nickname; // 닉네임

    /**
     * 링크형 게시판
     */
    @Getter @Setter
    private String doc_source; // 자료출처
    @Getter @Setter
    private String editor; // 기자
    @Getter @Setter
    private String categorize; // 분류

    /**
     * 발행지 게시판
     */
    @Getter @Setter
    private String publish_year; // 발행년도
    @Getter @Setter
    private String publish_month; // 발행월

    /**
     * 정보공개 게시판
     */
    @Getter @Setter
    private String public_range; // 공개의 범위
    @Getter @Setter
    private String public_cycle; // 공표주기
    @Getter @Setter
    private String public_time; // 공표시기
    @Getter @Setter
    private String public_type; // 공표형태

    /**
     * 영상자료 게시판
     */
    @Getter @Setter
    private String youtube_url; // 영상 url



    /**
     * TB_ARTICLE_COMMENT
     */
    private String comment_seq; // 순번
    private String repl_order; // 댓글 순번

    public ArticleMngVO(){}

    public ArticleMngVO(String article_seq){
        this.article_seq = article_seq;
    }

    public String getSecret_yn() {
        return StringUtil.isNotEmpty(secret_yn) ? secret_yn : "N";
    }

    public String getNotice_yn() {
        return StringUtil.isNotEmpty(notice_yn) ? notice_yn : "N";
    }

    public String getPwd_yn() {
        return StringUtil.isNotEmpty(pwd_yn) ? pwd_yn : "N";
    }
}
