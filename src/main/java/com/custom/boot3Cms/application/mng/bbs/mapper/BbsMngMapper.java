
package com.custom.boot3Cms.application.mng.bbs.mapper;

import com.custom.boot3Cms.application.mng.bbs.vo.BbsMngVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 게시판 관리 매퍼
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
@Mapper
public interface BbsMngMapper {

    /**
     * 게시판 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<BbsMngVO> getBbsList(BbsMngVO vo) throws Exception;

    /**
     * 게시판 목록 (메뉴)
     * @param type
     * @return
     * @throws Exception
     */
    List<BbsMngVO> getBbsTitleList(String type) throws Exception;

    /**
     * 게시판 코드 중복검사
     * @param vo
     * @return
     * @throws Exception
     */
    int checkBbsCode(BbsMngVO vo) throws Exception;

    /**
     * 게시판 정보 상세보기
     * @param vo
     * @return
     * @throws Exception
     */
    BbsMngVO getBbsDetail(BbsMngVO vo) throws Exception;

    /**
     * 게시판 카테고리 정보
     * @param vo
     * @return
     * @throws Exception
     */
    List<BbsMngVO> getBbsCategory(BbsMngVO vo) throws Exception;

    /**
     * 게시판 파일 정보
     * @param vo
     * @return
     * @throws Exception
     */
    List<BbsMngVO> getBbsFileInfo(BbsMngVO vo) throws Exception;

    /**
     * 게시판 권한 정보
     * @param vo
     * @return
     * @throws Exception
     */
    List<BbsMngVO> getBbsAuth(BbsMngVO vo) throws Exception;

    /**
     * 게시판 기본정보 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setBbsInfo(BbsMngVO vo) throws Exception;

    /**
     * 게시판 설정정보 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setBbsDetail(BbsMngVO vo) throws Exception;

    /**
     * 게시판 카테고리 정보 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setBbsCategory(BbsMngVO vo) throws Exception;

    /**
     * 게시판 파일 설정정보 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setBbsFileInfo(BbsMngVO vo) throws Exception;

    /**
     * 게시판 권한정보 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setBbsAuth(BbsMngVO vo) throws Exception;

    /**
     * 게시판 삭제 (논리삭제)
     * @param vo
     * @return
     * @throws Exception
     */
    int delBbs(BbsMngVO vo) throws Exception;

    /**
     * 게시판 기본정보 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updBbsInfo(BbsMngVO vo) throws Exception;

    /**
     * 게시판 상세정보 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updBbsDetail(BbsMngVO vo) throws Exception;

    /**
     * 게시판 카테고리 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updBbsCategory(BbsMngVO vo) throws Exception;

    /**
     * 게시판 파일 정보 삭제 (삭제 후 등록으로 수정)
     * @param vo
     * @return
     * @throws Exception
     */
    int delBbsFileInfo(BbsMngVO vo) throws Exception;

    /**
     * 게시판 권한 정보 삭제 (삭제 후 등록으로 수정)
     * @param vo
     * @return
     * @throws Exception
     */
    int delBbsAuth(BbsMngVO vo) throws Exception;

    /**
     * 게시판 카테고리 정보 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delBbsCategory(BbsMngVO vo) throws Exception;


    /**
     * 게시판 HTML 내용 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delBbsHtml(BbsMngVO vo) throws Exception;

    /**
     * 게시판 상세정보 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delBbsDetail(BbsMngVO vo) throws Exception;

    /**
     * 게시판 기본정보 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delBbsInfo(BbsMngVO vo) throws Exception;

    /**
     * BBS_INFO_SEQ로 게시판 존재여부 확인
     * @param vo
     * @return
     * @throws Exception
     */
    String getBbsCnt(BbsMngVO vo) throws Exception;


    /**
     * 게시판 HTHML 내용 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setBbsHtml(BbsMngVO vo) throws Exception;


}

