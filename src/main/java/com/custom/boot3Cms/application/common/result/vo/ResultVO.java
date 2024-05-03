package com.custom.boot3Cms.application.common.result.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * ResultVO RestAPI response 객체
 */
@Schema(description = "REST API 응답 결과 객체")
@Data
public class ResultVO {

    @Schema(description = "응답 메시지")
    private String resultMsg = "";

    @Schema(description = "응답 코드", example = "200, 202, 300 ..")
    private int resultCode = 0;

    @Schema(description = "응답 결과 데이터")
    private Map<String,Object> result = new HashMap<String,Object>();

    public void putResult(String key, Object value) {
        this.result.put(key, value);
    }

    public ResultVO() {}

    public ResultVO(String resultCode) {}
    public ResultVO(int resultCode, String resultMsg) {}
}
