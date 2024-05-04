package com.custom.boot3Cms.application.common.system.file.mapper;

import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;

import com.custom.boot3Cms.application.common.system.file.vo.FileMngVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 파일관리 매퍼
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
@Mapper
public interface FileMngMapper {

    /**
     * 파일 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<FileMngVO> getFileList(FileMngVO vo) throws Exception;

    /**
     * 파일 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setFile(FileMngVO vo) throws Exception;

    /**
     * 파일 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delFile(FileMngVO vo) throws Exception;

    /**
     * 파일 삭제 (TABLE/SEQ)
     * @param vo
     * @return
     * @throws Exception
     */
    int delFiles(FileMngVO vo) throws Exception;

    /**
     * 파일 단건 정보 가져오기
     * @param file_seq
     * @return
     * @throws Exception
     */
    FileMngVO getFileDetail(String file_seq) throws Exception;



}
