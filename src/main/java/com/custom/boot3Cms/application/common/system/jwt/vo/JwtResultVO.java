package com.custom.boot3Cms.application.common.system.jwt.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author : 정완배
 * @since : 2023. 8. 9.
 * @version : 1.0
 *
 * @package : egovframework.com.cmm.service
 * @filename : ResultVO.java
 * @modificationInformation
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일            수정자             수정내용
 *  ----------   ----------   ----------------------
 *  2023. 8. 9.    정완배             주석추가
 * </pre>
 *
 *
 */
@Getter
@Setter
public class JwtResultVO {

    /**
     * 응답 코드
     */
    private int resultCode = 0;

    /**
     * 응답 메시지
     */
    private String resultMessage = "OK";
    private Map<String, Object> result = new HashMap<String, Object>();

    public void putResult(String key, Object value) {
        result.put(key, value);
    }

    public Object getResult(String key) {
        return this.result.get(key);
    }
}
