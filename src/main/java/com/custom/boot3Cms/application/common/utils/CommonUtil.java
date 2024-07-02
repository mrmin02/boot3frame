package com.custom.boot3Cms.application.common.utils;

import com.nhncorp.lucy.security.xss.XssFilter;
import egovframework.cmmn.utils.EgovFileUploadUtil;
import egovframework.cmmn.utils.EgovFormBasedFileVo;
import com.custom.boot3Cms.application.common.config.vo.DefaultVO;
import com.custom.boot3Cms.application.common.system.file.vo.FileMngVO;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.tika.Tika;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * 공통 유틸함수 클래스
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2017-09-14 / 최재민	  / 최초 생성
 * </pre>
 * @since 2017-09-14
 */
public class CommonUtil {

    @Value("${Globals.file.DefaultPath}")
    private static String FILE_PATH;

    /**
     * 파일 MIME TYPE을 검증한다. 단 DefaultVO를 상속받아야 한다.
     * @param obj
     * @param mime_type
     * @return
     * @throws Exception
     */
    public static boolean fn_checkFileMime(Object obj, String mime_type) throws Exception{
        DefaultVO vo = new DefaultVO();
        BeanUtils.copyProperties(vo, obj);
        boolean fileResult = false;
        if(vo.getFile() != null){
            for (MultipartFile file : vo.getFile()) {
                if (StringUtil.isNotEmpty(file.getOriginalFilename())) {
                    String mime = new Tika().detect(file.getInputStream());
                    if(mime.startsWith(mime_type)){
                        fileResult = true;
                    }
                }
            }
        }
        return fileResult;
    }

    /**
     * 파일 확장자를 검증한다.
     * 단 DefaultVO를 상속받아야 한다.
     * 확장자 배열내에 존재하는 확장자와 file 배열의 확장자가 일치 할 경우 true return..
     * @param obj : 원본 오브젝트
     * @param extArr : 검사하고자 하는 확장자 배열
     * @return
     * @throws Exception
     */
    public static boolean fn_checkFileExt(Object obj, Object[] extArr) throws Exception{
        DefaultVO vo = new DefaultVO();
        BeanUtils.copyProperties(vo, obj);
        boolean fileResult = true;
        if(vo.getFile() != null){
            for(MultipartFile file : vo.getFile()){
                if (StringUtil.isNotEmpty(file.getOriginalFilename())) {
                    String fileName = file.getOriginalFilename();
                    String ext = fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());
                    for(Object extStr : extArr){
                        if(ext.equalsIgnoreCase((String) extStr)){
                            fileResult  = true;
                            break;
                        }else{
                            fileResult = false;
                        }
                    }
                    if(!fileResult){
                        break;
                    }
                }
            }
        }else{
            fileResult = true;
        }
        return fileResult;
    }

    /**
     *
     * 파일정보 List를 반환한다.
     * obj : 대상객체 VO, table_nm : 테이블 명, table_seq : 테이블의 시퀀스, table_file_order : 파일순서, fileDirPath : 파일경로, subDirPath : 파일서브경로, inpt_seq : 등록자, mime_type : 허용 할 mime_type (사용안할시 "" OR null)
     * 단, 해당 VO에서 DefaultVO를 상속받아야 한다.
     * @param obj
     * @param table_nm
     * @param table_seq
     * @param fileDirPath
     * @param subDirPath
     * @param inpt_seq
     * @param mime_type
     * @return
     * @throws Exception
     */
    public static List<FileMngVO> fn_getFileInfo(Object obj, String table_nm, String table_seq, String fileDirPath, String subDirPath, String inpt_seq, String mime_type) throws Exception{
        List<FileMngVO> rtnList = new ArrayList<>();
        DefaultVO vo = new DefaultVO();
        BeanUtils.copyProperties(vo, obj);
        boolean fileResult = false;
        if(StringUtil.isNotEmpty(mime_type)){
            fileResult = fn_checkFileMime(obj, mime_type);
        }else{
            fileResult = true;
        }
        if(fileResult){
            if(vo.getFile() != null){
                for (MultipartFile file : vo.getFile()) {
                    if(StringUtil.isNotEmpty(file.getOriginalFilename())){
                        String mime = new Tika().detect(file.getInputStream());
                        EgovFormBasedFileVo egovFileVO = EgovFileUploadUtil.uploadFile_noValid(file, fileDirPath, subDirPath+table_seq+"/", false, 0, 0, table_nm);
                        if(egovFileVO.isFileFlag()){
                            rtnList.add(new FileMngVO(table_nm, table_seq, mime, egovFileVO.getPhysicalName(), egovFileVO.getFileName(), egovFileVO.getServerSubPath(), inpt_seq, egovFileVO.isFileFlag()));
                        }
                    }
                }
            }
        }
        return rtnList;
    }

    /**
     *
     * 파일정보 List를 반환한다.
     * obj : 대상객체 VO, table_nm : 테이블 명, table_seq : 테이블의 시퀀스, table_file_order : 파일순서, fileDirPath : 파일경로, subDirPath : 파일서브경로, inpt_seq : 등록자, mime_type : 허용 할 mime_type (사용안할시 "" OR null)
     * 단, 해당 VO에서 DefaultVO를 상속받아야 한다.
     * TODO mime_type 이 image인 경우에만 width, height를 설정할 수 있다.
     * @param obj
     * @param table_nm
     * @param table_seq
     * @param fileDirPath
     * @param subDirPath
     * @param inpt_seq
     * @param mime_type
     * @return
     * @throws Exception
     */
    public static List<FileMngVO> fn_getFileUploadResizingAndInfo(Object obj, String table_nm, String table_seq, String fileDirPath, String subDirPath, String inpt_seq,
                                                                  String mime_type, boolean imageYn, int width, int height, String resizeType, String max_gijun) throws Exception{
        List<FileMngVO> rtnList = new ArrayList<>();
        DefaultVO vo = new DefaultVO();
        BeanUtils.copyProperties(vo, obj);
        boolean fileResult = false;
        if(StringUtil.isNotEmpty(mime_type)){
            fileResult = fn_checkFileMime(obj, mime_type);
        }else{
            fileResult = true;
        }
        if(fileResult){
            if(vo.getFile() != null){
                for (MultipartFile file : vo.getFile()) {
                    if(StringUtil.isNotEmpty(file.getOriginalFilename())){
                        String mime = new Tika().detect(file.getInputStream());
                        EgovFormBasedFileVo egovFileVO = EgovFileUploadUtil.uploadFile_noValid_resizeType(file, fileDirPath, subDirPath+table_seq+"/", imageYn, width, height, resizeType, max_gijun);
                        if(egovFileVO.isFileFlag()){
                            rtnList.add(new FileMngVO(table_nm, table_seq, mime, egovFileVO.getPhysicalName(), egovFileVO.getFileName(), egovFileVO.getServerSubPath(), inpt_seq, egovFileVO.isFileFlag()));
                        }
                    }
                }
            }
        }
        return rtnList;
    }

    /**
     * 파일 삭제 정보를 반환한다.
     * true / 삭제완료, false / 삭제실패
     * @param filePath
     * @param fileName
     * @return
     * @throws Exception
     */
    public static Boolean fn_delFile(String filePath, String fileName) throws Exception{
        return EgovFileUploadUtil.delFile(filePath+fileName);
    }

    /**
     * 맵(Map)을 키(key)를 기준으로 정렬하는 메소드
     * @param map 정렬하고자하는 맵
     * @param isASC 오름차순 true / 내림차순 false
     * @return 정렬된 맵
     */
    public static Map<Object, Object> fn_sortByKey(final Map map, boolean isASC) {
        if(isASC) {
            return new TreeMap<Object, Object>(map);
        } else {
            TreeMap<Object, Object> tree = new TreeMap<Object, Object>(Collections.reverseOrder());
            tree.putAll(map);
            return tree;
        }
    }

    /**
     * 사용자 로그인 정보 확인
     * @return
     */
    public static LoginVO fn_getUserAuth(Principal principal) {
        if(principal != null){
            return (LoginVO)((Authentication) principal).getPrincipal();
        }else{
            LoginVO loginVO = new LoginVO();
            loginVO.setUser_auth("ROLE_ANON");
            loginVO.setUser_name("비회원");
            return loginVO;
        }
    }

    /**
     * 현재 로그인된 사용자의 ROLE을 확인하여 true / false로 반환한다.
     *
     * @param role
     * @return
     */
    public static boolean fn_getUserRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().filter(o -> o.getAuthority().equals(role)).findAny().isPresent();
    }

    /**
     * 상세보기시 OBJECT를 인자로 받아 메세지와 결과를 담은 MAP으로 리턴한다.
     * @param obj
     * @return
     */
    public static Map<String, Object> fn_getDetail(Object obj) {
        Map<String, Object> rtnMap = new HashMap<>();
        boolean result = false;
        if(obj != null){
            result = true;
            rtnMap.put("value", obj);
        }else{
            rtnMap.put("rHeader", "알림!");
            rtnMap.put("rMsg", "올바르지 않은 접근입니다.");
        }
        rtnMap.put("result", result);
        return rtnMap;
    }

    /**
     * chosen-select 사용 시, int 값에 "" 값이 들어옴 > null 처리
     * private String 변수에 한하여 빈 문자열 "" 을 null 처리
     * 2024-01-29 cms
     * @param obj
     * @return
     */
    public static void fn_getEmptyStringToNull(Object obj) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field : fields){
            if(Modifier.isPrivate(field.getModifiers()) && field.getType().isInstance(new String())){
                String value = BeanUtils.getProperty(obj, field.getName());
                if(value != null && value.isEmpty()){
                    value = null;
                }
                BeanUtils.setProperty(obj, field.getName(), value);
            }
        }
    }


    /**
     * 파일 전체 물리삭제 처리
     * @param fileList
     * @return
     * @throws Exception
     */
    public static boolean fn_filesSysDelete(List<FileMngVO> fileList) throws Exception{
        boolean result = false;
        for(FileMngVO fileMngVO : fileList){
            result = fn_delFile(FILE_PATH+fileMngVO.getFile_path(), fileMngVO.getFile_sys_nm());
            if(!result){
                return result;
            }
        }
        return result;
    }

    /**
     * 파일 단건 물리삭제 처리
     * @param fileMngVO
     * @return
     * @throws Exception
     */
    public static boolean fn_fileSysDelete(FileMngVO fileMngVO) throws Exception{
        boolean result = false;
        result = fn_delFile(FILE_PATH+fileMngVO.getFile_path(), fileMngVO.getFile_sys_nm());
        return result;
    }

    /**
     * 글 댓글 순번변환 (4자리)
     * @param bbs_order
     * @param type
     * @return
     */
    public static String fn_updBbsOrder(String bbs_order, String type){
        String str = bbs_order;
        if("R".equals(type)){
            List<String> orderArr = new ArrayList<>();
            for(int i = 0; i < bbs_order.length(); i+=4){
                orderArr.add(bbs_order.substring(i, i+4));
            }
            String rtnStr = "";
            for(int i = 0; i < orderArr.size(); i++){
                String split = orderArr.get(i);
                if(i == orderArr.size()-1){
                    char str_ch = orderArr.get(i).toCharArray()[orderArr.get(i).length()-1];
                    if(str_ch != 'Z'){
                        str_ch = (char) (str_ch+1);
                        rtnStr += split.substring(0, 3)+str_ch;
                    }else{
                        str_ch = orderArr.get(i).toCharArray()[orderArr.get(i).length()-2];
                        if(str_ch != 'Z'){
                            str_ch = (char) (str_ch+1);
                            rtnStr += split.substring(0, 2)+str_ch+"A";
                        }else{
                            str_ch = orderArr.get(i).toCharArray()[orderArr.get(i).length()-3];
                            if(str_ch != 'Z'){
                                str_ch = (char) (str_ch+1);
                                rtnStr += split.substring(0, 1)+str_ch+"AA";
                            }else{
                                str_ch = orderArr.get(i).toCharArray()[orderArr.get(i).length()-4];
                                if(str_ch != 'Z'){
                                    str_ch = (char) (str_ch+1);
                                    rtnStr += split.substring(0, 0)+str_ch+"AAA";
                                }
                            }
                        }
                    }
                }else{
                    rtnStr += orderArr.get(i);
                }
            }
            str = rtnStr;
        }else if("A".equals(type)){
            str += "AAAA";
        }
        return str;
    }

    /**
     * 클래스 복사 (NULL 요소 제외)
     * @param dest
     * @param source
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void fn_copyClass(Object dest, Object source) throws IllegalAccessException, InvocationTargetException {
        new BeanUtilsBean() {
            @Override
            public void copyProperty(Object dest, String name, Object value)
                    throws IllegalAccessException, InvocationTargetException {
                if(value != null) {
                    super.copyProperty(dest, name, value);
                }
            }
        }.copyProperties(dest, source);
    }

    /**
     * 클래스 복사 (NULL 요소 제외) (지정한 bean 요소에 대해 copy를 하지 않음)
     * @param dest
     * @param source
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void fn_copyClassByFilterBeanName(Object dest, Object source, String... bean_name) throws IllegalAccessException, InvocationTargetException {
        if(bean_name != null){
            new BeanUtilsBean() {
                @Override
                public void copyProperty(Object dest, String name, Object value)
                        throws IllegalAccessException, InvocationTargetException {
                    List<String> list = Arrays.asList(bean_name);
                    if(value != null && !list.contains(name)) {
                        super.copyProperty(dest, name, value);
                    }
                }
            }.copyProperties(dest, source);
        }
    }

    /**
     * 클래스 복사 (NULL 요소 제외)
     * @param dest
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void fn_cleanXSS(Object dest) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        XssFilter xssFilter = XssFilter.getInstance("lucy-xss.xml", false);
        Field[] fields = dest.getClass().getDeclaredFields();
        for(Field field : fields){
            if(Modifier.isPrivate(field.getModifiers()) && field.getType().isInstance(new String())){
                BeanUtils.setProperty(dest, field.getName(), xssFilter.doFilter(BeanUtils.getProperty(dest, field.getName())));
            }
        }
    }

    /**
     * 입력받은 값이 숫자인지 아닌지 판단
     * @param str
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static boolean isNumber(String str){
        try{
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e){
            return false;
        }

    }

    /**
     * 클라이언트 ip를 가져온다.
     * @param req
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static String getClientIp(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = req.getRemoteAddr();
        } else {
            ip = "";
        }
        return ip;
    }

    /**
     * 검색 관련 변수 XSS 추가 처리
     * @param parameter
     * @return
     */
    public static String fn_searchXSS(String parameter) {
       /* return parameter.replaceAll("\"", "&quot;").replaceAll("\'", "&#39;").replaceAll("script", "")
                .replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\\(", "&#40;")
                .replaceAll("\\)", "&#41;").replaceAll("[\\\\\\\"\\\\\\'][\\\\s]*javascript:(.*)[\\\\\\\"\\\\\\']", "\"\"")
                .replaceAll("onmouse", "").replaceAll("onMouse", "").replaceAll("onerror", "").replaceAll("onError", "").replaceAll("href", "");
                */
        return parameter.replaceAll("\"", "&#34;").replaceAll("\'", "&#39;").replaceAll("script", "")
                .replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\\(", "&#40;")
                .replaceAll("\\)", "&#41;").replaceAll("[\\\\\\\"\\\\\\'][\\\\s]*javascript:(.*)[\\\\\\\"\\\\\\']", "\"\"")
                .replaceAll("onerror", "").replaceAll("onError", "").replaceAll("href", "");
    }

    /**
     * 썸네일 전용(png 로 생성)
     * 파일정보 List를 반환한다.
     * obj : 대상객체 VO, table_nm : 테이블 명, table_seq : 테이블의 시퀀스, table_file_order : 파일순서, fileDirPath : 파일경로, subDirPath : 파일서브경로, inpt_seq : 등록자, mime_type : 허용 할 mime_type (사용안할시 "" OR null)
     * 단, 해당 VO에서 DefaultVO를 상속받아야 한다.
     * TODO mime_type 이 image인 경우에만 width, height를 설정할 수 있다.
     * @param obj
     * @param table_nm
     * @param table_seq
     * @param fileDirPath
     * @param subDirPath
     * @param inpt_seq
     * @param mime_type
     * @return
     * @throws Exception
     */
    public static List<FileMngVO> fn_getFileUploadResizingAndInfoForThumbnail(Object obj, String table_nm, String table_seq, String fileDirPath, String subDirPath, String inpt_seq,
                                                                              String mime_type, boolean imageYn, int width, int height, String resizeType, String max_gijun) throws Exception{
        List<FileMngVO> rtnList = new ArrayList<>();
        DefaultVO vo = new DefaultVO();
        BeanUtils.copyProperties(vo, obj);
        boolean fileResult = false;
        if(StringUtil.isNotEmpty(mime_type)){
            fileResult = fn_checkFileMime(obj, mime_type);
        }else{
            fileResult = true;
        }
        if(fileResult){
            if(vo.getFile() != null){
                for (MultipartFile file : vo.getFile()) {
                    if(StringUtil.isNotEmpty(file.getOriginalFilename())){
                        String mime = new Tika().detect(file.getInputStream());
                        EgovFormBasedFileVo egovFileVO = EgovFileUploadUtil.uploadFile_noValid_resizeType_for_thumbnail(file, fileDirPath, subDirPath+table_seq+"/", imageYn, width, height, resizeType, max_gijun);
                        if(egovFileVO.isFileFlag()){
                            rtnList.add(new FileMngVO(table_nm, table_seq, mime, egovFileVO.getPhysicalName(), egovFileVO.getFileName(), egovFileVO.getServerSubPath(), inpt_seq, egovFileVO.isFileFlag()));
                        }
                    }
                }
            }
        }
        return rtnList;
    }


    /**
     *
     * 썸네일 파일정보 List를 반환한다.
     * obj : 대상객체 VO, table_nm : 테이블 명, table_seq : 테이블의 시퀀스, table_file_order : 파일순서, fileDirPath : 파일경로, subDirPath : 파일서브경로, inpt_seq : 등록자, mime_type : 허용 할 mime_type (사용안할시 "" OR null)
     * 단, 해당 VO에서 DefaultVO를 상속받아야 한다.
     * @param obj
     * @param table_nm
     * @param table_seq
     * @param fileDirPath
     * @param subDirPath
     * @param inpt_seq
     * @param mime_type
     * @return
     * @throws Exception
     */
    public static List<FileMngVO> fn_getThumbnailnfo(Object obj, String table_nm, String table_seq, String fileDirPath, String subDirPath, String inpt_seq, String mime_type) throws Exception{
        List<FileMngVO> rtnList = new ArrayList<>();
        DefaultVO vo = new DefaultVO();
        BeanUtils.copyProperties(vo, obj);
        boolean fileResult = false;
        if(StringUtil.isNotEmpty(mime_type)){
            fileResult = fn_checkFileMime(obj, mime_type);
        }else{
            fileResult = true;
        }
        if(fileResult){
            if(vo.getThumbnail() != null){
                for (MultipartFile file : vo.getThumbnail()) {
                    if(StringUtil.isNotEmpty(file.getOriginalFilename())){
                        String mime = new Tika().detect(file.getInputStream());
                        EgovFormBasedFileVo egovFileVO = EgovFileUploadUtil.uploadFile_noValid(file, fileDirPath, subDirPath+table_seq+"/", false, 0, 0, table_nm);
                        if(egovFileVO.isFileFlag()){
                            rtnList.add(new FileMngVO(table_nm, table_seq, mime, egovFileVO.getPhysicalName(), egovFileVO.getFileName(), egovFileVO.getServerSubPath(), inpt_seq, egovFileVO.isFileFlag()));
                        }
                    }
                }
            }
        }
        return rtnList;
    }

    /** 프로젝트, 핸드메이드 전용
     *
     * @param obj
     * @param table_nm
     * @param table_seq
     * @param fileDirPath
     * @param subDirPath
     * @param inpt_seq
     * @param mime_type
     * @return
     * @throws Exception
     */
    public static List<FileMngVO> fn_getFileUploadForProAndHandResizingAndInfo(Object obj, String table_nm, String table_seq, String fileDirPath, String subDirPath, String inpt_seq,
                                                                               String mime_type, boolean imageYn, int width, int height, String resizeType, String max_gijun) throws Exception{
        List<FileMngVO> rtnList = new ArrayList<>();
        DefaultVO vo = new DefaultVO();
        BeanUtils.copyProperties(vo, obj);
        boolean fileResult = false;
        if(StringUtil.isNotEmpty(mime_type)){
            fileResult = fn_checkFileMime(obj, mime_type);
        }else{
            fileResult = true;
        }
        if(fileResult){
            if(vo.getFile() != null){
                for (MultipartFile file : vo.getFile()) {
                    if(StringUtil.isNotEmpty(file.getOriginalFilename())){
                        String mime = new Tika().detect(file.getInputStream());
                        EgovFormBasedFileVo egovFileVO = EgovFileUploadUtil.uploadFile_noValid_resizeType(file, fileDirPath, subDirPath+table_seq+"/", imageYn, width, height, resizeType, max_gijun);
                        if(egovFileVO.isFileFlag()){
                            rtnList.add(new FileMngVO(table_nm, table_seq, mime, egovFileVO.getPhysicalName(), egovFileVO.getFileName(), egovFileVO.getServerSubPath(), inpt_seq, egovFileVO.isFileFlag()));
                        }
                    }
                }
            }
        }
        return rtnList;
    }
}