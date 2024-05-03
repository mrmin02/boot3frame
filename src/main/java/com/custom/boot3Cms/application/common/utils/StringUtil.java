package com.custom.boot3Cms.application.common.utils;


import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    final static Locale currentLocale = Locale.KOREA;
    final static NumberFormat numberFormatter = NumberFormat.getNumberInstance(currentLocale);

    /**
     * String 값이 비었거나 NULL인지 검증한다.
     *
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
        return !isNotEmpty(o);
    }

    /**
     * String 값이 있거나, NULL이 아닌지 검증한다.
     * 배열의 경우 ""값이거나, NULL이 아닌지 검증한다.
     *
     * @param o
     * @return
     */
    public static boolean isNotEmpty(Object o) {
        if ((o instanceof String)) {
            return o != null && ((String) (o)).length() > 0;
        }else if(o instanceof String[]) {
            if(((String[]) o).length > 0) {
                return true;
            }else {
                int cnt = 0;
                for(String i : (String[]) o) {
                    cnt += i.length();
                }
                return cnt != 0;
            }
        }else {
            return o != null;
        }
    }

    /**
     * 입력받은 문자열을 대문자로 모두 치환하여 반환한다.
     *
     * @param str
     * @return
     */
    public static String toUpper(String str) {
        return str.toUpperCase();
    }

    /**
     * 입력받은 문자열을 소문자로 모두 치환하여 반환한다.
     *
     * @param str
     * @return
     */
    public static String toLower(String str) {
        return str.toLowerCase();
    }

    /**
     * request를 인자로 받아 클라이언트의 IP를 반환한다.
     *
     * @param request
     * @return
     */
    public static String getIP(HttpServletRequest request) {
        String clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (null == clientIp || clientIp.length() == 0
                || clientIp.toLowerCase().equals("unknown")) {
            clientIp = request.getHeader("REMOTE_ADDR");
        }
        if (null == clientIp || clientIp.length() == 0
                || clientIp.toLowerCase().equals("unknown")) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    /**
     * request를 인자로 받아 queryString을 반환한다.
     *
     * @param request
     * @param removeStr
     * @return
     */
    public static String getSearchParam(HttpServletRequest request, String removeStr) {
        String searchParam = request.getParameter("searchParam");
        if (removeStr == null)
            removeStr = "";

        try {
            if (searchParam == null) {
                searchParam = "";

                Enumeration enu = request.getParameterNames();
                while (enu.hasMoreElements()) {
                    String str = (String) enu.nextElement();
                    if (removeStr.indexOf(str) == -1 && !"".equals(request.getParameter(str))
                            && !"%".equals(request.getParameter(str))
                            ) {
                        if (searchParam != "") {
                            searchParam += "&";
                        }

                        searchParam += str + "=" + URLEncoder.encode(request.getParameter(str), "UTF-8");
                    }
                }

                searchParam = StringUtil.modHtml(searchParam);
            } else {
                searchParam = StringUtil.modHtml(searchParam);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return searchParam;
    }

    /**
     * HTML CODE REPLACE
     *
     * @param tmpRp
     * @return
     * @throws Exception
     */
    public static String modHtml(String tmpRp) throws Exception {
        tmpRp = tmpRp == null ? "" : tmpRp;
        tmpRp = tmpRp.replaceAll("&#33;", "!");
        tmpRp = tmpRp.replaceAll("& #33;", "!");
        tmpRp = tmpRp.replaceAll("&#34;", "\"");
        tmpRp = tmpRp.replaceAll("& #34;", "\"");
        tmpRp = tmpRp.replaceAll("&quot;", "\"");
        tmpRp = tmpRp.replaceAll("&#35;", "#");
        tmpRp = tmpRp.replaceAll("& #35;", "#");
        tmpRp = tmpRp.replaceAll("&#36;", "$");
        tmpRp = tmpRp.replaceAll("& #36;", "$");
        tmpRp = tmpRp.replaceAll("&#37;", "%");
        tmpRp = tmpRp.replaceAll("& #37;", "%");
        tmpRp = tmpRp.replaceAll("&#38;", "&");
        tmpRp = tmpRp.replaceAll("& #38;", "&");
        tmpRp = tmpRp.replaceAll("&amp;", "&");
        tmpRp = tmpRp.replaceAll("&#39;", "'");
        tmpRp = tmpRp.replaceAll("& #39;", "'");
        tmpRp = tmpRp.replaceAll("&#40;", "(");
        tmpRp = tmpRp.replaceAll("& #40;", "(");
        tmpRp = tmpRp.replaceAll("&#41;", ")");
        tmpRp = tmpRp.replaceAll("& #41;", ")");
        tmpRp = tmpRp.replaceAll("&#42;", "*");
        tmpRp = tmpRp.replaceAll("& #42;", "*");
        tmpRp = tmpRp.replaceAll("&#43;", "+");
        tmpRp = tmpRp.replaceAll("& #43;", "+");
        tmpRp = tmpRp.replaceAll("&#44;", ",");
        tmpRp = tmpRp.replaceAll("& #44;", ",");
        tmpRp = tmpRp.replaceAll("&#45;", "-");
        tmpRp = tmpRp.replaceAll("& #45;", "-");
        tmpRp = tmpRp.replaceAll("&#46;", ".");
        tmpRp = tmpRp.replaceAll("& #46;", ".");
        tmpRp = tmpRp.replaceAll("&#47;", "/");
        tmpRp = tmpRp.replaceAll("& #47;", "/");
        tmpRp = tmpRp.replaceAll("&#58;", ":");
        tmpRp = tmpRp.replaceAll("& #58;", ":");
        tmpRp = tmpRp.replaceAll("&#59;", ";");
        tmpRp = tmpRp.replaceAll("& #59;", ";");
        tmpRp = tmpRp.replaceAll("&#60;", "<");
        tmpRp = tmpRp.replaceAll("& #60;", "<");
        tmpRp = tmpRp.replaceAll("&lt;", "<");
        tmpRp = tmpRp.replaceAll("&#61;", "=");
        tmpRp = tmpRp.replaceAll("& #61;", "=");
        tmpRp = tmpRp.replaceAll("&#62;", ">");
        tmpRp = tmpRp.replaceAll("& #62;", ">");
        tmpRp = tmpRp.replaceAll("&gt;", ">");
        tmpRp = tmpRp.replaceAll("&#63;", "?");
        tmpRp = tmpRp.replaceAll("& #63;", "?");
        tmpRp = tmpRp.replaceAll("&#64;", "@");
        tmpRp = tmpRp.replaceAll("& #64;", "@");
        tmpRp = tmpRp.replaceAll("&#91;", "[");
        tmpRp = tmpRp.replaceAll("& #91;", "[");
        tmpRp = tmpRp.replaceAll("&#92;", "\\");
        tmpRp = tmpRp.replaceAll("& #92;", "\\");
        tmpRp = tmpRp.replaceAll("&#93;", "]");
        tmpRp = tmpRp.replaceAll("& #93;", "]");
        tmpRp = tmpRp.replaceAll("&#94;", "^");
        tmpRp = tmpRp.replaceAll("& #94;", "^");
        tmpRp = tmpRp.replaceAll("&#95;", "_");
        tmpRp = tmpRp.replaceAll("& #95;", "_");
        tmpRp = tmpRp.replaceAll("&#96;", "`");
        tmpRp = tmpRp.replaceAll("& #96;", "`");
        tmpRp = tmpRp.replaceAll("&#123;", "{");
        tmpRp = tmpRp.replaceAll("& #123;", "{");
        tmpRp = tmpRp.replaceAll("&#124;", "|");
        tmpRp = tmpRp.replaceAll("& #124;", "|");
        tmpRp = tmpRp.replaceAll("&#125;", "}");
        tmpRp = tmpRp.replaceAll("& #125;", "}");
        tmpRp = tmpRp.replaceAll("&#126;", "~");
        tmpRp = tmpRp.replaceAll("& #126;", "~");
        tmpRp = tmpRp.replaceAll("&apos;", "'");
        tmpRp = tmpRp.replaceAll("& apos;", "'");
        return tmpRp;
    }

    /**
     * 원본 문자열의 포함된 특정 문자열을 새로운 문자열로 변환하는 메서드
     *
     * @param source  원본 문자열
     * @param subject 원본 문자열에 포함된 특정 문자열
     * @param object  변환할 문자열
     * @return sb.toString() 새로운 문자열로 변환된 문자열
     */
    public static String replace(String source, String subject, String object) {
        StringBuffer rtnStr = new StringBuffer();
        String preStr = "";
        String nextStr = source;
        String srcStr = source;

        while (srcStr.indexOf(subject) >= 0) {
            preStr = srcStr.substring(0, srcStr.indexOf(subject));
            nextStr =
                    srcStr.substring(srcStr.indexOf(subject) + subject.length(),
                            srcStr.length());
            srcStr = nextStr;
            rtnStr.append(preStr).append(object);
        }
        rtnStr.append(nextStr);
        return rtnStr.toString();
    }

    /**
     * 객체가 null인지 확인하고 null인 경우 "" 로 바꾸는 메서드
     *
     * @param object 원본 객체
     * @return resultVal 문자열
     */
    public static String isNullToString(Object object) {
        String string = "";
        //if (object == null || object.equals("null") || object.toString().trim().equals("")  ) {
        if (object == null || object.equals("null") || "".equals(object.toString().trim())) {
            string = "";
        } else {
            string = object.toString().trim();
        }
        return string;
    }

    /**
     * 객체가 null인지 확인하고 null인 경우 다른문자 로 바꾸는 메서드
     *
     * @param object 원본 객체
     * @param object 바꿀문자열
     * @return resultVal 문자열
     */
    public static String isNullToString(Object object, String def) {
        String string = "";
        //if (object == null || object.equals("null") || object.toString().trim().equals("")  ) {
        if (object == null || object.equals("null") || "".equals(object.toString().trim())) {
            string = def;
        } else {
            string = object.toString().trim();
        }
        return string;
    }


    /**
     * 문자열 길이를 인자로 받아 랜덤 문자열을 return한다.
     * 문자열 길이를 0으로 입력 할 경우 문자열 전체를 return
     *
     * @param length
     * @return
     */
    public static String getRandomStr(int length) {
        Random rnd = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 20; i++) {
            if (rnd.nextBoolean()) {
                buf.append((char) ((int) (rnd.nextInt(26)) + 97));
            } else {
                buf.append((rnd.nextInt(10)));
            }
        }
        if (length > 0) {
            return buf.toString().substring(0, length);
        } else {
            return buf.toString();
        }
    }

    /**
     * 랜덤 숫자 6개 출력
     *
     * @return
     */
    public static String getRandomNum() {
        int count = 0;
        String randomStr = "";
        ArrayList<Integer> numberList = new ArrayList<Integer>();
        while (count < 6) {
            numberList.add(new Random().nextInt(10));
            count++;
        }
        Collections.shuffle(numberList);
        for (int number : numberList) {
            randomStr += number;
        }
        return randomStr;
    }

    /**
     * 랜덤 숫자 n개 출력
     *
     * @return
     */
    public static String getRanNumSetCount(int n) {
        String ranStr = "";
        int ds = 0;
        for (int i = 1; i <= n; i++) {
            Random r = new Random();
            ds = r.nextInt(9);
            ranStr = ranStr + Integer.toString(ds);
        }
        return ranStr;
    }

    /**
     * XSS방어 (CUSTOM TAG)
     *
     * @param str
     * @return
     */
    public static String xss(String str) {
        str = str.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        str = str.replaceAll("script", "");

        return str;
    }

    /**
     * 기업회원 등급 숫자형으로 RETURN
     *
     * @param str
     * @return
     */
    public static int getRating(char str) {
        return Character.getNumericValue(str);
    }


    /**
     * 내용 속에 img 태그를 추출해서 RETURN
     *
     * @param content
     * @return
     */
    public static String getImgTagString(String content) {
        Pattern pattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
        String imgTag = null;
        Matcher match = pattern.matcher(content);
        if (match.find()) {
            imgTag = match.group(0);
        } else {
            imgTag = "noTag";
        }
        return imgTag;
    }

    /**
     * 문자열을 byte 길이만큼 잘라내고 return
     *
     * @param str
     * @param sPoint
     * @param length
     * @return
     * @throws Exception
     */
    public static String getMaxByteString(String str, int sPoint, int length) throws Exception {
        String EncodingLang = "euc-kr";
        byte[] bytes = str.getBytes("euc-kr");
        byte[] value = new byte[length];
        if (bytes.length < sPoint + length) {
            throw new Exception("Length of bytes is less. length : " + bytes.length + " sPoint : " + sPoint + " length : " + length);
        }
        for (int i = 0; i < length; i++) {
            value[i] = bytes[sPoint + i];
        }
        return new String(value, EncodingLang).trim();
    }

    /**
     * 문자열의 모든 HTML태그를 제거하여 RETURN
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String tagReplace(String str) throws Exception {
        return str.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "").replaceAll("\r|\n|&nbsp;", "");
    }

    /**
     * object를 받아 String으로 반환
     * @param obj
     * @return
     * @throws Exception
     */
    public static String toString(Object obj) throws Exception{
        return String.valueOf(obj);
    }

    /**
     * REPLACE ALL
     * @param param
     * @param regex
     * @param replacement
     * @return
     * @throws Exception
     */
    public static String replaceAll(String param, String regex, String replacement) throws Exception{
        String rtnStr = param.replaceAll(regex, replacement);
        return rtnStr;
    }

    /**
     * STRING ARRAY NULL 체크
     * @param arr
     * @return
     */
    public static boolean isNotEmptyArray(String[] arr){
        if(arr == null){
            return false;
        }else{
            return StringUtil.isNotEmpty(StringUtils.join(arr, "").replace(" ", "").trim());
        }
    }

    /**
     * STRING ARRAY NULL 체크
     * @param arr
     * @return
     */
    public static boolean isEmptyArray(String[] arr){
        if(arr == null){
            return true;
        }else{
            return StringUtil.isEmpty(StringUtils.join(arr, "").replace(" ", "").trim());
        }
    }

    /**
     * CONCAT
     * @param str1
     * @param str2
     * @return
     */
    public static String concat(String str1, String str2){
        return str1 + str2;
    }

    /**
     * urlEncode
     * @param str
     * @param enc
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String urlEncode(String str, String enc) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, enc);
    }

    /**
     * urlDecode
     * @param str
     * @param enc
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String urlDecode(String str, String enc) throws UnsupportedEncodingException {
        return URLDecoder.decode(str, enc);
    }

    /**
     * 날짜와 형식을 받아 요일을 반환
     * @param date
     * @param dateType
     * @return
     * @throws Exception
     */
    public static String fn_getDayOfWeek(String date, String dateType) throws Exception {
        String day = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
        Date nDate = dateFormat.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        switch(cal.get(Calendar.DAY_OF_WEEK)){
            case 1: day = "일"; break;
            case 2: day = "월"; break;
            case 3: day = "화"; break;
            case 4: day = "수"; break;
            case 5: day = "목"; break;
            case 6: day = "금"; break;
            case 7: day = "토"; break;
        }

        return day;
    }


}
