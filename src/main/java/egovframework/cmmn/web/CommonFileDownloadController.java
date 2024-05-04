package egovframework.cmmn.web;

import egovframework.cmmn.utils.FileDownLoad;
import com.custom.boot3Cms.application.common.system.file.service.FileMngService;
import com.custom.boot3Cms.application.common.system.file.vo.FileMngVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * 파일 다운로드를 위한 컨트롤러 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.3.25  이삼섭          최초 생성
 *
 * Copyright (C) 2009 by MOPAS  All right reserved.
 * </pre>
 */
@Controller
public class CommonFileDownloadController {

    //파일  서비스
    @Resource(name="fileMngService")
    FileMngService fileMngService;
    

    /**
     * 첨부파일로 등록된 파일에 대하여 다운로드를 제공한다.
     * 
     * @param commandMap
     * @param response
     * @throws Exception
     */
    @RequestMapping("/cmmn/fileDown.do")
    public void cvplFileDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //String orignlFileNm = request.getParameter("filename");
        //String filePath = request.getParameter("filepath");
        //String systemFileNm = request.getParameter("filerealname");
        String fileFullPath = request.getParameter("fileFullPath");


//	String was =  GlobalsProperties.getProperty("Globals.WAS");
//	if(!was.equals("JEUS")){
//		orignlFileNm = new String(orignlFileNm.getBytes("EUC-KR"), "ISO-8859-1");
//	}


        String fileSeq = request.getParameter("fileSeq");
        FileMngVO filevo = fileMngService.getFileDetail(fileSeq);

        String orignlFileNm = "";
        String filePath = "";
        String systemFileNm = "";

        if(filevo != null){
            orignlFileNm = filevo.getFile_nm();
            filePath = filevo.getFile_path();
            systemFileNm = filevo.getFile_sys_nm();
        }


        FileDownLoad.fileDownLoad(filePath, systemFileNm, orignlFileNm, fileFullPath, request, response);
    }
    
}
