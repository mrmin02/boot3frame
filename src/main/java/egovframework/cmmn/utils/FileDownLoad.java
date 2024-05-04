package egovframework.cmmn.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class FileDownLoad {

    @Value("${Globals.file.DefaultPath}")
    public static String FILE_PATH;

    public static void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String browser = getBrowser(request);

        String dispositionPrefix = "attachment; filename=";
        String encodedFilename = null;

        if (browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Firefox")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Opera")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString().replace(",", "");
        } else {
            //throw new RuntimeException("Not supported browser");
            throw new IOException("Not supported browser");
        }

        response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);

        if ("Opera".equals(browser)) {
            response.setContentType("application/octet-stream;charset=UTF-8");
        }
    }

    public static String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");

        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        } else if (header.indexOf("Firefox") > -1) {
            return "Firefox";
        }
        return "MSIE";
    }

    public static void fileDownLoad(
            String filePath,
            String systemFileNm,
            String orignlFileNm,
            String fileFullPath,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {

        String fileStorePath = FILE_PATH;
        String fullPath = fileStorePath + filePath + "/" + systemFileNm;

        if (fileFullPath != null && fileFullPath != "") {
            fullPath = fileFullPath;
        }

        ////System.out.println(fullPath+ " ////////");

        File uFile = new File(fullPath);

        if (!uFile.isFile()) {
            return;
        }

        int fSize = (int) uFile.length();

        if (fSize > 0) {
            String mimetype = "application/x-msdownload";

            //response.setBufferSize(fSize);	// OutOfMemeory 발생
            response.setContentType(mimetype);
            //response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fvo.getOrignlFileNm(), "utf-8") + "\"");
            setDisposition(orignlFileNm, request, response);
            response.setContentLength(fSize);

				/*
				 * FileCopyUtils.copy(in, response.getOutputStream());
				 * in.close(); 
				 * response.getOutputStream().flush();
				 * response.getOutputStream().close();
				 */
            BufferedInputStream in = null;
            BufferedOutputStream out = null;

            try {
                in = new BufferedInputStream(new FileInputStream(uFile));
                out = new BufferedOutputStream(response.getOutputStream());

                FileCopyUtils.copy(in, out);
                out.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
                //ex.printStackTrace();
                // 다음 Exception 무시 처리
                // Connection reset by peer: socket write error
                //log.debug("IGNORED: " + ex.getMessage());
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception ignore) {
                        ignore.printStackTrace();
                        // no-op
                        // log.debug("IGNORED: " + ignore.getMessage());
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception ignore) {
                        ignore.printStackTrace();
                        // no-op
                        //log.debug("IGNORED: " + ignore.getMessage());
                    }
                }
            }

        } else {
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding("utf-8");
            PrintWriter printwriter = response.getWriter();
            printwriter.println("<html>");
            printwriter.println("<br><br><br><h2>Could not get file name:<br>" + orignlFileNm + "</h2>");
            printwriter.println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>");
            printwriter.println("<br><br><br>&copy; webAccess");
            printwriter.println("</html>");
            printwriter.flush();
            printwriter.close();
        }
    }
}
