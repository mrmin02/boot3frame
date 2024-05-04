package com.custom.boot3Cms.application.common.system.file.service;

import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.system.file.mapper.FileMngMapper;
import com.custom.boot3Cms.application.common.system.file.vo.FileMngVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 파일관리 서비스
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
@Transactional
@Service("fileMngService")
public class FileMngService {

    @Autowired
    FileMngMapper fileMngMapper;

    /**
     * 파일 목록
     * @param vo
     * @return
     * @throws Exception
     */
    public List<FileMngVO> getFileList(FileMngVO vo) throws Exception{
        return fileMngMapper.getFileList(vo);
    }

    /**
     * 파일 단건 정보 가져오기
     * @param file_seq
     * @return
     * @throws Exception
     */
    public FileMngVO getFileDetail(String file_seq) throws Exception{
        return fileMngMapper.getFileDetail(file_seq);
    }



    /**
     * 파일 등록
     * @param vo
     * @return
     * @throws Exception
     */
    public boolean setFile(FileMngVO vo) throws Exception{
        return fileMngMapper.setFile(vo) > 0;
    }

    /**
     * 파일 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    public boolean delFile(FileMngVO vo) throws Exception{
        boolean result;
        FileMngVO fileInfo = getFileDetail(vo.getFile_seq());
        vo.setFile_sys_nm(fileInfo.getFile_sys_nm());
        vo.setFile_path(fileInfo.getFile_path());
        result = fileMngMapper.delFile(vo) > 0;
        if(result){
            result = CommonUtil.fn_fileSysDelete(vo);
            result = true;
        }
        return result;
    }

    /**
     * 파일 삭제 (TABLE/SEQ)
     * @param vo
     * @return
     * @throws Exception
     */
    public boolean delFiles(FileMngVO vo) throws Exception{
        boolean result;
        List<FileMngVO> fileList = fileMngMapper.getFileList(vo);
        result = CommonUtil.fn_filesSysDelete(fileList);
        if(result){
            result = fileMngMapper.delFiles(vo) > 0;
        }
        return result;
    }

    /**
     * 파일 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    public boolean delFileData(FileMngVO vo) throws Exception{
        boolean result;
        result = fileMngMapper.delFile(vo) > 0;
        return result;
    }

}
