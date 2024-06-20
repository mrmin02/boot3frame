package com.custom.boot3Cms.application.mng.code.mapper;

import com.custom.boot3Cms.application.mng.code.vo.CodeMngVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 관리자 코드관리 매퍼
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
@Mapper
public interface CodeMngMapper {

    /**
     * 코드 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<CodeMngVO> getCodeList(CodeMngVO vo) throws Exception;

    /**
     * 코드 상세보기
     * @param vo
     * @return
     * @throws Exception
     */
    CodeMngVO getCodeDetail(CodeMngVO vo) throws Exception;

    /**
     * 코드 목록 (커스텀 태그)
     * @param code
     * @return
     * @throws Exception
     */
    List<CodeMngVO> getCodeToTag(String code) throws Exception;

    /**
     * 자식코드 개수 확인
     * @param code
     * @return
     * @throws Exception
     */
    int getChildCodeSize(String code) throws Exception;

    /**
     * 코드 NAME 출력 (커스텀 태그)
     * @param code
     * @return
     * @throws Exception
     */
    String getCodeToText(String code) throws Exception;

    /**
     * 코드 중복 체크
     * @param vo
     * @return
     * @throws Exception
     */
    int getCodeCheck(CodeMngVO vo) throws Exception;

    /**
     * 코드 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setCode(CodeMngVO vo) throws Exception;

    /**
     * 코드 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updCode(CodeMngVO vo) throws Exception;

    /**
     * 코드 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delCode(CodeMngVO vo) throws Exception;

    /**
     * 자식 코드 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delChildrenCode(CodeMngVO vo) throws Exception;

    /**
     * 코드 목록 by Searching
     * @param vo
     * @return
     * @throws Exception
     */
    List<CodeMngVO> getCodeListByType(CodeMngVO vo) throws Exception;

    /**
     * 산그림 홈페이지에 맞게 권한관리 구현을 위한 코드 목록
     * @param userAuthConfVO
     * @return
     * @throws Exception
     */
    List<CodeMngVO> getCodeToAuthConf(CodeMngVO userAuthConfVO) throws Exception;


}
