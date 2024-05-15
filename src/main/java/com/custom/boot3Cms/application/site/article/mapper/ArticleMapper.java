
package com.custom.boot3Cms.application.site.article.mapper;

import com.custom.boot3Cms.application.site.article.vo.ArticleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 * 게시글 관리 매퍼
 *
 * @author SEKOREA
 * @version 1.1
 * @see <pre>
 *  Modification Information
 *
 * 	수정일       / 수정자    / 수정내용
 * 	------------------------------------------
 * 	2017-11-10 / 김기식	  / 최초 생성
 * 	2018-03-23 / 전성진	  / 프레임워크 구조 변경
 * </pre>
 * @since 2017-11-10
 */
@Mapper
public interface ArticleMapper {

    /**
     * 메인 게시글 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<ArticleVO> getMainArticleList(ArticleVO vo) throws Exception;

    /**
     * 게시글 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<ArticleVO> getArticleList(ArticleVO vo) throws Exception;

    /**
     * 게시글 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<ArticleVO> getArticleQnaList(ArticleVO vo) throws Exception;

    /**
     * 게시글 공지사항 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<ArticleVO> getArticlenNoticeList(ArticleVO vo) throws Exception;

    /**
     * 게시글 상세보기 -->
     * @param vo
     * @return
     * @throws Exception
     */
    ArticleVO getArticle(ArticleVO vo) throws Exception;

    /**
     * 게시글 답변 상세보기 -->
     * @param vo
     * @return
     * @throws Exception
     */
    ArticleVO getReply(ArticleVO vo) throws Exception;

    /**
     * 게시글 CNT
     * @param vo
     * @return
     * @throws Exception
     */
    int getArticleCnt(ArticleVO vo) throws Exception;

    /**
     * 게시글 CNT
     * @param vo
     * @return
     * @throws Exception
     */
    int getArticleQnaCnt(ArticleVO vo) throws Exception;

    /**
     * 게시글 코멘트 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<ArticleVO> getCommentList(ArticleVO vo) throws Exception;

    /**
     * 게시글 순번 조회
     * @param vo
     * @return
     * @throws Exception
     */
    String getArticleOrder(ArticleVO vo) throws Exception;

    /**
     * 댓글 순번 조회
     * @param vo
     * @return
     * @throws Exception
     */
    String getCommentOrder(ArticleVO vo) throws Exception;

    /**
     * 게시글 컨텐츠 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setArticle(ArticleVO vo) throws Exception;

    /**
     * 게시글 컨텐츠 등록 (답글이 아닐 경우 prt_grp에 본인 seq를 넣어준다.)
     * @param vo
     * @return
     * @throws Exception
     */
    int setArticlePrtGrp(ArticleVO vo) throws Exception;

    /**
     * 댓글 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setComment(ArticleVO vo) throws Exception;

    /**
     * 게시글 댓글 등록 (답글이 아닐 경우 prt_grp에 본인 seq를 넣어준다.)
     * @param vo
     * @return
     * @throws Exception
     */
    int setCommentPrtGrp(ArticleVO vo) throws Exception;

    /**
     * 댓글 수정/삭제 권한검사를 위한 댓글 정보 GET
     * @param vo
     * @return
     * @throws Exception
     */
    ArticleVO getCommentInfo(ArticleVO vo) throws Exception;

    /**
     * 댓글 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updComment(ArticleVO vo) throws Exception;

    /**
     * 조회수 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updArticleReadCNT(ArticleVO vo) throws Exception;

    /**
     * 요약문 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updArticleSummary(ArticleVO vo) throws Exception;

    /**
     * 게시글 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updArticle(ArticleVO vo) throws Exception;

    /**
     * 게시글 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delArticle(ArticleVO vo) throws Exception;

    /**
     * 댓글 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delComment(ArticleVO vo) throws Exception;

    /**
     * 게시글 물리 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delArticleReal(ArticleVO vo) throws Exception;

    /**
     * 댓글 물리 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delCommentReal(ArticleVO vo) throws Exception;

    /**
     * 게시글 권한 조회
     * @param vo
     * @return
     * @throws Exception
     */
    List<ArticleVO> getAuth(ArticleVO vo) throws Exception;


    /**
     * 게시판 권한 확인
     * @param vo
     * @return
     * @throws Exception
     */
    Map<String, Object> getArticleBbsAuth(ArticleVO vo) throws Exception;

    /**
     * 게시판 카테고리 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<ArticleVO> getCategory(ArticleVO vo) throws Exception;

    /**
     * Q&A 비밀번호 체크
     * @param vo
     * @return
     * @throws Exception
     */
    int checkPwd(ArticleVO vo) throws Exception;

    /**
     * 게시판 글 CNT
     * @param vo
     * @return
     * @throws Exception
     */
    int getArticleListCNT(ArticleVO vo) throws Exception;

    /**
     * 대댓글 COUNT
     * @param vo
     * @return
     * @throws Exception
     */
    int commentCommentCnt(ArticleVO vo) throws Exception;
}

