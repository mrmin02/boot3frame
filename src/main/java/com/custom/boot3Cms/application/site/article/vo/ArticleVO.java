package com.custom.boot3Cms.application.site.article.vo;

import com.custom.boot3Cms.application.common.system.file.vo.FileMngVO;
import com.custom.boot3Cms.application.common.utils.EncryptUtil;
import com.custom.boot3Cms.application.mng.bbs.vo.BbsMngVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 게시글 VO
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-05-12 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-05-12 */
@Data
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class ArticleVO extends BbsMngVO {

    /**
     * TB_ARTICLES
     */
    private String article_seq; // 순번
    private String article_num; // 글 번호
    private String subject; // 제목
    private String secret_yn; // 비밀글 여부
    private String notice_yn; // 공지사항 여부
    private String content; // 내용 (or 현황/문제점)
//    private String prt_grp; // 부모 그룹
    private String prt_seq; // 부모 순번
    private String bbs_order; // 게시글 순번
    private String read_cnt; // 조회수
    private String inpt_ip; // 등록자 IP
    private String upd_ip; // 수정자 IP
    private String user_id; // 비회원 아이디
    private String user_pwd; // 비회원 비밀번호
    private String del_yn; // 삭제여부
    private List<ArticleVO> articleAuth; // 게시판 권한정보
    private String inpt_user_name;
    private String inpt_user_type; // 등록자 타입
    private String upd_user_type; // 수정자 타입
    private List<FileMngVO> fileList;
    private String user_no;
    private String inpt_user_dept; // 등록자 부서코드
    private String nickname; // 닉네임
    private String manager; // 공모 주최
    private String place; // 장소
    private String author_name; // 작가명
    private String user_name; // 작가명

    private String reply_yn;    // 게시글 등록 시, 답글인지?

    /**
     * 링크형 게시판
     */
    private String link_url; //링크
    private String doc_source; // 자료출처

    /**
     * 웹진형 게시판
     */
    private String file_seq; // 계간지 첨부파일

    /**
     * 영상자료 게시판
     */
    private String youtube_url;// 유튜브 url


    private String content_word;

    private String inpt_date; // 등록날

    private String article_start_date; // 시작일자
    private String article_end_date; // 종료일자

    private String reply_cnt; // 답글수

    private String pwd_check_yn;  //비밀번호 확인 여부
    // 추가
    private String content_article; // 이미지를 제외한 게시글

    /**
     * TB_ARTICLE_COMMENT
     */
    private String comment_seq; // 순번
    private String repl_order; // 댓글 순번
    private String comment_cnt; // 댓글 수
    private String profile_path;
    private String profile_image_sys_name;
    private String profile_image;

    private int start_num;
    private int end_num;

    /**
     * 통합검색
     */
    private List<ArticleVO> searchList;
    private int search_cnt;



    private String public_time; // 발표 일자



    public ArticleVO(){}

    public ArticleVO(String bbs_cd){
        super.setBbs_cd(bbs_cd);
    }

    public ArticleVO(String bbs_cd, int start_num, int end_num){
        this.start_num = start_num;
        this.end_num = end_num;
        super.setBbs_cd(bbs_cd);
    }

    public ArticleVO(String bbs_cd, String article_seq){
        this.article_seq = article_seq;
        super.setBbs_cd(bbs_cd);
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = EncryptUtil.fn_encryptSHA256(user_pwd);
    }

    public List<ArticleVO> getArticleAuth() { return articleAuth; }

    public void setArticleAuth(List<ArticleVO> articleAuth) { this.articleAuth = articleAuth; }

    public List<FileMngVO> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileMngVO> fileList) {
        this.fileList = fileList;
    }



}
