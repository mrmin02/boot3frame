package egovframework.cmmn.utils;


import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
//import org.apache.log4j.Logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * @author 공통컴포넌트 개발팀 한성곤
 * @version 1.0
 * @Class Name  : EgovFormBasedFileUtil.java
 * @Description : Form-based File Upload 유틸리티
 * @Modification Information
 * <p>
 * 수정일         수정자                   수정내용
 * -------          --------        ---------------------------
 * 2009.08.26       한성곤                  최초 생성
 * @see
 * @since 2009.08.26
 */
public class EgovFormBasedFileUtil {
    /**
     * Buffer size
     */
    public static final int BUFFER_SIZE = 8192;

    public static final String SEPERATOR = File.separator;

    public static String FILE_PATH;

    @Value("${Globals.file.DefaultPath}")
    public void setFilePath(String filePath){
        this.FILE_PATH = filePath;
    }

    /**
     * 오늘 날짜 문자열 취득.
     * ex) 20090101
     *
     * @return
     */
    public static String getTodayString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        return format.format(new Date());
    }

    /**
     * 물리적 파일명 생성.
     *
     * @return
     */
    public static String getPhysicalFileName() {
        return EgovFormBasedUUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    /**
     * 파일명 변환.
     *
     * @param filename String
     * @return
     * @throws Exception
     */
    protected static String convert(String filename) throws Exception {
        //return java.net.URLEncoder.encode(filename, "utf-8");
        return filename;
    }

    /**
     * Stream으로부터 파일을 저장함.
     *
     * @param is   InputStream
     * @param file File
     * @throws IOException
     */
    public static long saveFile(InputStream is, File file) throws IOException {
        // 디렉토리 생성
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        OutputStream os = null;
        long size = 0L;

        try {
            os = new FileOutputStream(file);

            int bytesRead = 0;
            byte[] buffer = new byte[BUFFER_SIZE];

            while ((bytesRead = is.read(buffer, 0, BUFFER_SIZE)) != -1) {
                size += bytesRead;
                os.write(buffer, 0, bytesRead);
            }
        } finally {
            if (os != null) {
                os.close();
            }
        }

        return size;
    }

    /**
     * 파일을 Download 처리한다.
     *
     * @param response
     * @param where
     * @param serverSubPath
     * @param physicalName
     * @param original
     * @throws Exception
     */
    public static void downloadFile(HttpServletResponse response, String where, String serverSubPath, String physicalName, String original) throws Exception {
        String downFileName = where + SEPERATOR + serverSubPath + SEPERATOR + physicalName;

        File file = new File(EgovWebUtil.filePathBlackList(downFileName));

        if (!file.exists()) {
            throw new FileNotFoundException(downFileName);
        }

        if (!file.isFile()) {
            throw new FileNotFoundException(downFileName);
        }

        byte[] b = new byte[BUFFER_SIZE];

        original = original.replaceAll("\r", "").replaceAll("\n", "");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + convert(original) + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        BufferedInputStream fin = null;
        BufferedOutputStream outs = null;

        try {
            fin = new BufferedInputStream(new FileInputStream(file));
            outs = new BufferedOutputStream(response.getOutputStream());

            int read = 0;

            while ((read = fin.read(b)) != -1) {
                outs.write(b, 0, read);
            }
        } finally {
            if (outs != null) {
                outs.close();
            }

            if (fin != null) {
                fin.close();
            }
        }
    }

    /**
     * 이미지에 대한 미리보기 기능을 제공한다.
     * <p>
     * mimeType의 경우는 JSP 상에서 다음과 같이 얻을 수 있다.
     * getServletConfig().getServletContext().getMimeType(name);
     *
     * @param response
     * @param where
     * @param serverSubPath
     * @param physicalName
     * @throws Exception
     */
    public static void viewFile(HttpServletResponse response, String where, String serverSubPath, String physicalName, String mimeTypeParam) throws Exception {
        String mimeType = mimeTypeParam;
        String downFileName = FILE_PATH + SEPERATOR + serverSubPath + SEPERATOR + physicalName;

        File file = new File(EgovWebUtil.filePathBlackList(downFileName));

        /* 파일이 없을때 Default Noimage */
        if (!file.exists()) {
            //downFileName = EgovFormBasedFileUtil.class.getResource("").getPath().substring(1, GetProperties.class.getResource("").getPath().lastIndexOf("WEB-INF")) + "/images/sub/bg_noimg.gif";
            //file = new File(GetProperties.filePathBlackList(downFileName));
        }

        if (!file.isFile()) {
            //throw new FileNotFoundException(downFileName);
        }

        byte[] b = new byte[BUFFER_SIZE];

        if (mimeType == null) {
            mimeType = "application/octet-stream;";
        }

        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", "filename=image;");

        BufferedInputStream fin = null;
        BufferedOutputStream outs = null;

        try {
            fin = new BufferedInputStream(new FileInputStream(file));
            outs = new BufferedOutputStream(response.getOutputStream());

            int read = 0;

            while ((read = fin.read(b)) != -1) {
                outs.write(b, 0, read);
            }
// 2011.10.10 보안점검 후속조치
        } catch (Exception e){

        } finally {
            if (outs != null) {
                try {
                    outs.close();
                } catch (Exception ignore) {
                    // //System.out.println("IGNORE: " + ignore);
                }
            }
            if (fin != null) {
                try {
                    fin.close();
                } catch (Exception ignore) {
                    // //System.out.println("IGNORE: " + ignore);
                }
            }
        }
    }

    public static void fullPathViewFile(HttpServletResponse response, String where, String fullPath) throws Exception {
        String downFileName = where + SEPERATOR + fullPath;


        File file = new File(EgovWebUtil.filePathBlackList(downFileName));

        if (!file.isFile()) {
            throw new FileNotFoundException(downFileName);
        }

        byte[] b = new byte[BUFFER_SIZE];

        response.setContentType("application/octet-stream;");
        response.setHeader("Content-Disposition", "filename=image;");

        BufferedInputStream fin = null;
        BufferedOutputStream outs = null;

        try {
            fin = new BufferedInputStream(new FileInputStream(file));
            outs = new BufferedOutputStream(response.getOutputStream());

            int read = 0;

            while ((read = fin.read(b)) != -1) {
                outs.write(b, 0, read);
            }
            // 2011.10.10 보안점검 후속조치
        } finally {
            if (outs != null) {
                try {
                    outs.close();
                } catch (Exception ignore) {
                    // //System.out.println("IGNORE: " + ignore);
                }
            }
            if (fin != null) {
                try {
                    fin.close();
                } catch (Exception ignore) {
                    // //System.out.println("IGNORE: " + ignore);
                }
            }
        }
    }
}
