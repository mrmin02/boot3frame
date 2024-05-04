package egovframework.cmmn.utils;

import java.io.Serializable;

/**
 * @author 공통컴포넌트 개발팀 한성곤
 * @version 1.0
 * @Class Name  : EgovFormBasedFileVo.java
 * @Description : Form-based File Upload VO
 * @Modification Information
 * <p>
 * 수정일         수정자                   수정내용
 * -------          --------        ---------------------------
 * 2009.08.26       한성곤                  최초 생성
 * @see Copyright (C) 2008 by MOPAS  All right reserved.
 * @since 2009.08.26
 */
@SuppressWarnings("serial")
public class EgovFormBasedFileVo implements Serializable {
    /**
     * 파일명
     */
    private String fileName = "";
    /**
     * ContextType
     */
    private String contentType = "";
    /**
     * 하위 디렉토리 지정
     */
    private String serverSubPath = "";
    /**
     * 물리적 파일명
     */
    private String physicalName = "";
    /**
     * 파일 사이즈
     */
    private long size = 0L;

    private boolean fileFlag; // 파일 결과값 리턴

    /**
     * 파라메타명
     **/
    private String fileParamName = "";

    /**
     * 파일 기본경로
     */
    private String fileDefaultPath = "";

    public String getFileDefaultPath() {
        return fileDefaultPath;
    }

    public void setFileDefaultPath(String fileDefaultPath) {
        this.fileDefaultPath = fileDefaultPath;
    }

    public String getFileParamName() {
        return fileParamName;
    }

    public void setFileParamName(String fileParamName) {
        this.fileParamName = fileParamName;
    }

    /**
     * fileName attribute를 리턴한다.
     *
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * fileName attribute 값을 설정한다.
     *
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * contentType attribute를 리턴한다.
     *
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * contentType attribute 값을 설정한다.
     *
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * serverSubPath attribute를 리턴한다.
     *
     * @return the serverSubPath
     */
    public String getServerSubPath() {
        return serverSubPath;
    }

    /**
     * serverSubPath attribute 값을 설정한다.
     *
     * @param serverSubPath the serverSubPath to set
     */
    public void setServerSubPath(String serverSubPath) {
        this.serverSubPath = serverSubPath;
    }

    /**
     * physicalName attribute를 리턴한다.
     *
     * @return the physicalName
     */
    public String getPhysicalName() {
        return physicalName;
    }

    /**
     * physicalName attribute 값을 설정한다.
     *
     * @param physicalName the physicalName to set
     */
    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }

    /**
     * size attribute를 리턴한다.
     *
     * @return the size
     */
    public long getSize() {
        return size;
    }

    /**
     * size attribute 값을 설정한다.
     *
     * @param size the size to set
     */
    public void setSize(long size) {
        this.size = size;
    }

    public boolean isFileFlag() {
        return fileFlag;
    }

    public void setFileFlag(boolean fileFlag) {
        this.fileFlag = fileFlag;
    }

}
