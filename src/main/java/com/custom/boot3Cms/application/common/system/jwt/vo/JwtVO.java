package com.custom.boot3Cms.application.common.system.jwt.vo;

import lombok.Data;

@Data
public class JwtVO {

    private String access_token;
    private String refresh_token;

}
