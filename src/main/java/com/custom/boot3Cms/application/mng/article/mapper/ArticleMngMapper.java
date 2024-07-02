
package com.custom.boot3Cms.application.mng.article.mapper;

import com.custom.boot3Cms.application.mng.article.vo.ArticleMngVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 게시글 관리 Mapper
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-07-02 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-07-02 */
@Mapper
public interface ArticleMngMapper {

    /**
     * 게시글 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<ArticleMngVO> getArticleList(ArticleMngVO vo) throws Exception;

    /**
     * 게시글 목록 CNT
     * @param vo
     * @return
     * @throws Exception
     */
    int getArticleListCNT(ArticleMngVO vo) throws Exception;

    /**
     * 게시글 상세보기 -->
     * @param vo
     * @return
     * @throws Exception
     */
    ArticleMngVO getArticle(ArticleMngVO vo) throws Exception;

    /**
     * 게시글 코멘트 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<ArticleMngVO> getCommentList(ArticleMngVO vo) throws Exception;

    /**
     * 게시글 순번 조회
     * @param vo
     * @return
     * @throws Exception
     */
    String getArticleOrder(ArticleMngVO vo) throws Exception;

    /**
     * 댓글 순번 조회
     * @param vo
     * @return
     * @throws Exception
     */
    String getCommentOrder(ArticleMngVO vo) throws Exception;

    /**
     * 게시글 컨텐츠 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setArticle(ArticleMngVO vo) throws Exception;

    /**
     * 게시글 컨텐츠 등록 (답글이 아닐 경우 prt_grp에 본인 seq를 넣어준다.)
     * @param vo
     * @return
     * @throws Exception
     */
    int setArticlePrtGrp(ArticleMngVO vo) throws Exception;

    /**
     * 게시글 댓글 등록 (답글이 아닐 경우 prt_grp에 본인 seq를 넣어준다.)
     * @param vo
     * @return
     * @throws Exception
     */
    int setCommentPrtGrp(ArticleMngVO vo) throws Exception;

    /**
     * 댓글 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setComment(ArticleMngVO vo) throws Exception;

    /**
     * 조회수 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updArticleReadCNT(ArticleMngVO vo) throws Exception;

    /**
     * 게시글 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updArticle(ArticleMngVO vo) throws Exception;

    /**
     * 게시글 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delArticle(ArticleMngVO vo) throws Exception;

    /**
     * 댓글 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delComment(ArticleMngVO vo) throws Exception;

    /**
     * 게시글 물리 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delArticleReal(ArticleMngVO vo) throws Exception;

    /**
     * 댓글 물리 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delCommentReal(ArticleMngVO vo) throws Exception;

}

