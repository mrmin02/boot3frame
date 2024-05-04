package egovframework.cmmn.utils;

import com.custom.boot3Cms.application.common.utils.ImageUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author 공통컴포넌트 개발팀 한성곤
 * @version 1.0
 * @Class Name  : EgovFileUploadUtil.java
 * @Description : Spring 기반 File Upload 유틸리티
 * @Modification Information
 * <p>
 * 수정일         수정자                   수정내용
 * -------          --------        ---------------------------
 * 2009.08.26       한성곤                  최초 생성
 * @see
 * @since 2009.08.26
 */
public class EgovFileUploadUtil extends EgovFormBasedFileUtil {
    /**
     * 파일을 Upload 처리한다.
     *
     * @param request
     * @param where
     * @param maxFileSize
     * @return
     * @throws Exception
     */

    public static List<EgovFormBasedFileVo> uploadFiles(HttpServletRequest request, String where, String whereSubPath, long maxFileSize) {
        List<EgovFormBasedFileVo> list = new ArrayList<EgovFormBasedFileVo>();
        MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest) request;
        Iterator fileIter = mptRequest.getFileNames();

        EgovFormBasedFileVo vo = new EgovFormBasedFileVo();
        while (fileIter.hasNext()) {
            try {
                MultipartFile mFile = mptRequest.getFile((String) fileIter.next());

                vo = new EgovFormBasedFileVo();
                String tmp = mFile.getOriginalFilename();

                if (tmp.lastIndexOf("\\") >= 0) {
                    tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
                }
                vo.setFileName(tmp);
                vo.setContentType(mFile.getContentType());
                vo.setServerSubPath(whereSubPath + "/" + getTodayString());
                vo.setPhysicalName(getPhysicalFileName());
                vo.setSize(mFile.getSize());
                vo.setFileParamName(mFile.getName());
                vo.setFileDefaultPath(where);
                if (tmp.lastIndexOf(".") >= 0) {
                    vo.setPhysicalName(vo.getPhysicalName() + tmp.substring(tmp.lastIndexOf(".")));
                }

                if (mFile.getSize() > 0 && mFile.getSize() <= maxFileSize) {
                    saveFile(mFile.getInputStream(), new File(EgovWebUtil.filePathBlackList(where + SEPERATOR + vo.getServerSubPath() + SEPERATOR + vo.getPhysicalName())));
                    list.add(vo);
                }
            } catch (IOException e) {
                vo.setSize(-1);
                list.add(vo);
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * 파일 업로드 처리, 용량체크를 하지 않고, 이미지일 경우 이미지 크기를 리사이징한다. (크롭인지 아닌지 정할 수 있는 걸로 개선)
     *
     * @param mFile
     * @param where
     * @param whereSubPath
     * @return
     */
    public static EgovFormBasedFileVo uploadFile_noValid_resizeType(MultipartFile mFile, String where, String whereSubPath, boolean imageYn, int width, int height, String resizeType, String max_gijun) {
        EgovFormBasedFileVo retFile = null;
        try {

            retFile = new EgovFormBasedFileVo();
            String tmp = mFile.getOriginalFilename();

            if (tmp.lastIndexOf("\\") >= 0) {
                tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
            }
            retFile.setFileName(tmp);
            retFile.setContentType(mFile.getContentType());
            retFile.setServerSubPath(whereSubPath);
            retFile.setPhysicalName(getPhysicalFileName());
            retFile.setSize(mFile.getSize());
            retFile.setFileParamName(mFile.getName());
            retFile.setFileDefaultPath(where);
            if (tmp.lastIndexOf(".") >= 0) {
                retFile.setPhysicalName(retFile.getPhysicalName() + tmp.substring(tmp.lastIndexOf(".")));
            }
            saveFile(mFile.getInputStream(), new File(EgovWebUtil.filePathBlackList(where + SEPERATOR + retFile.getServerSubPath() + SEPERATOR + retFile.getPhysicalName())));
            int maxHeight = height;
            int maxWidth = width;
            if (imageYn){ //true
                //이 부분을 최적화 시켜야 함...
                if(resizeType.equals("crop")) {
                    ImageUtil.fn_resizeImg(width, height, where+SEPERATOR+retFile.getServerSubPath()+SEPERATOR, retFile.getPhysicalName());
                } else {
                    if(max_gijun.equals("width")) {
                        ImageUtil.fn_resizeImageRatioOfWidthGijun(maxWidth,where+SEPERATOR+retFile.getServerSubPath()+SEPERATOR, retFile.getPhysicalName());
                    } else {
                        ImageUtil.fn_resizeImageRatio(maxHeight,where+SEPERATOR+retFile.getServerSubPath()+SEPERATOR, retFile.getPhysicalName());
                    }

                }
            }
            retFile.setFileFlag(true);
        } catch (IOException e) {
            e.printStackTrace();
            retFile.setFileFlag(false);
        }
        return retFile;
    }

    /**
     * 파일 업로드 처리, 용량체크를 하지 않고, 이미지일 경우 이미지 크기를 리사이징한다.
     *
     * @param mFile
     * @param where
     * @param whereSubPath
     * @return
     */
    public static EgovFormBasedFileVo uploadFile_noValid(MultipartFile mFile, String where, String whereSubPath, boolean imageYn, int width, int height, String table_nm) {
        EgovFormBasedFileVo retFile = null;
        try {

            retFile = new EgovFormBasedFileVo();
            String tmp = mFile.getOriginalFilename();

            if (tmp.lastIndexOf("\\") >= 0) {
                tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
            }
            retFile.setFileName(tmp);
            retFile.setContentType(mFile.getContentType());
            retFile.setServerSubPath(whereSubPath);
            retFile.setPhysicalName(getPhysicalFileName());
            retFile.setSize(mFile.getSize());
            retFile.setFileParamName(mFile.getName());
            retFile.setFileDefaultPath(where);
            if (tmp.lastIndexOf(".") >= 0) {
                retFile.setPhysicalName(retFile.getPhysicalName() + tmp.substring(tmp.lastIndexOf(".")));
            }
            saveFile(mFile.getInputStream(), new File(EgovWebUtil.filePathBlackList(where + SEPERATOR + retFile.getServerSubPath() + SEPERATOR + retFile.getPhysicalName())));

            String table_name = table_nm;
            int maxHeight = 0;
            if(table_name.equals("TB_EVENT") || table_name.equals("TB_SNS_LINK") ) {
                maxHeight = 440;
                ImageUtil.fn_resizeImageRatio(maxHeight,where+SEPERATOR+retFile.getServerSubPath()+SEPERATOR, retFile.getPhysicalName());
            }

            if (imageYn){ //true
                ImageUtil.fn_resizeImg(width, height, where+SEPERATOR+retFile.getServerSubPath()+SEPERATOR, retFile.getPhysicalName());
            }
            retFile.setFileFlag(true);
        } catch (IOException e) {
            e.printStackTrace();
            retFile.setFileFlag(false);
        }
        return retFile;
    }

    /**
     * 파일을 Upload 처리한다.
     *
     * @param mFile
     * @param where
     * @param whereSubPath
     * @return
     * @throws Exception
     */

    public static EgovFormBasedFileVo uploadFile(MultipartFile mFile, String where, String whereSubPath) {
        EgovFormBasedFileVo retFile = null;
        try {
            retFile = new EgovFormBasedFileVo();
            String tmp = mFile.getOriginalFilename();
            if (tmp.lastIndexOf("\\") >= 0) {
                tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
            }
            retFile.setFileName(tmp);
            retFile.setContentType(mFile.getContentType());
            retFile.setServerSubPath(whereSubPath);
            retFile.setPhysicalName(getPhysicalFileName());
            retFile.setSize(mFile.getSize());
            retFile.setFileParamName(mFile.getName());
            retFile.setFileDefaultPath(where);
            if (tmp.lastIndexOf(".") >= 0) {
                retFile.setPhysicalName(retFile.getPhysicalName() + tmp.substring(tmp.lastIndexOf(".")));
            }
            saveFile(mFile.getInputStream(), new File(EgovWebUtil.filePathBlackList(where + SEPERATOR + retFile.getServerSubPath() + SEPERATOR + retFile.getPhysicalName())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retFile;
    }


    /**
     * 파일을 Upload 처리한다.
     *
     * @param where
     * @param maxFileSize
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static EgovFormBasedFileVo uploadFile(MultipartFile mFile, String where, String whereSubPath, long maxFileSize, String file_name) {

        EgovFormBasedFileVo retFile = null;

        try {

            retFile = new EgovFormBasedFileVo();
            String tmp = mFile.getOriginalFilename();

            if (tmp.lastIndexOf("\\") >= 0) {
                tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
            }
            retFile.setFileName(tmp);
            retFile.setContentType(mFile.getContentType());
            retFile.setServerSubPath(whereSubPath);
            retFile.setPhysicalName(getPhysicalFileName());
            retFile.setSize(mFile.getSize());
            retFile.setFileParamName(mFile.getName());
            if (tmp.lastIndexOf(".") >= 0) {
                retFile.setPhysicalName(retFile.getPhysicalName() + tmp.substring(tmp.lastIndexOf(".")));
            }

            if (mFile.getSize() > 0 && mFile.getSize() <= maxFileSize) {
                saveFile(mFile.getInputStream(), new File(EgovWebUtil.filePathBlackList(where + SEPERATOR + retFile.getServerSubPath() + SEPERATOR + file_name)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retFile;
    }


    /**
     * 파일또는 디렉토리 삭제
     *
     * @param fullPath
     * @return
     */
    public static boolean delFile(String fullPath) {
        File f = new File(fullPath);
        try{
            if (f.delete()) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 전자정부 프레임워크 파일 삭제
     *
     * @param list
     * @return
     */
    public static boolean delFiles(List<EgovFormBasedFileVo> list) {

        boolean rtnBool = true;
        try {
            for (EgovFormBasedFileVo item : list) {
                delFile(EgovWebUtil.filePathBlackList(item.getFileDefaultPath() + SEPERATOR + item.getServerSubPath() + SEPERATOR + item.getPhysicalName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            rtnBool = false;
        }
        return rtnBool;
    }

    /**
     * 파일 업로드 처리, 용량체크를 하지 않고, 이미지일 경우 이미지 크기를 리사이징한다. (크롭인지 아닌지 정할 수 있는 걸로 개선)
     * 화질 문제로 인해 png 로 저장
     *
     * @param mFile
     * @param where
     * @param whereSubPath
     * @return
     */
    public static EgovFormBasedFileVo uploadFile_noValid_resizeType_for_thumbnail(MultipartFile mFile, String where, String whereSubPath, boolean imageYn, int width, int height, String resizeType, String max_gijun) {
        EgovFormBasedFileVo retFile = null;
        try {

            retFile = new EgovFormBasedFileVo();
            String tmp = mFile.getOriginalFilename();

            if (tmp.lastIndexOf("\\") >= 0) {
                tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
            }
            retFile.setFileName(tmp);
            retFile.setContentType(mFile.getContentType());
            retFile.setServerSubPath(whereSubPath);
            retFile.setPhysicalName(getPhysicalFileName());
            retFile.setSize(mFile.getSize());
            retFile.setFileParamName(mFile.getName());
            retFile.setFileDefaultPath(where);
            if (tmp.lastIndexOf(".") >= 0) {
                retFile.setPhysicalName(retFile.getPhysicalName() + ".png"); // png로 저장
            }
            saveFile(mFile.getInputStream(), new File(EgovWebUtil.filePathBlackList(where + SEPERATOR + retFile.getServerSubPath() + SEPERATOR + retFile.getPhysicalName())));
            int maxHeight = height;
            int maxWidth = width;
            if (imageYn){ //true
                //이 부분을 최적화 시켜야 함...
                if(resizeType.equals("crop")) {
                    ImageUtil.fn_resizeImg(width, height, where+SEPERATOR+retFile.getServerSubPath()+SEPERATOR, retFile.getPhysicalName());
                } else {
                    if(max_gijun.equals("width")) {
                        ImageUtil.fn_resizeImageRatioOfWidthGijun(maxWidth,where+SEPERATOR+retFile.getServerSubPath()+SEPERATOR, retFile.getPhysicalName());
                    } else {
                        ImageUtil.fn_resizeImageRatio(maxHeight,where+SEPERATOR+retFile.getServerSubPath()+SEPERATOR, retFile.getPhysicalName());
                    }

                }
            }
            retFile.setFileFlag(true);
        } catch (IOException e) {
            e.printStackTrace();
            retFile.setFileFlag(false);
        }
        return retFile;
    }
}
