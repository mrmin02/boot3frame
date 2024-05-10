package com.custom.boot3Cms.application.common.system.jwt.control;

import com.custom.boot3Cms.application.common.system.jwt.service.JwtService;
import io.swagger.annotations.ApiOperation;
import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.system.jwt.vo.JwtVO;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * JWT 컨트롤러
 * 2024-04-18 cms
 */
@RestController
public class JwtController {

    @Resource(name = "jwtService")
    JwtService jwtService;


    /**
     * Refresh Token 을 이용해 Access Token 생성
     * @param request
     * @param jwtVO
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "Access Token 생성", notes = "사용자의 Refresh Token을 통해 Access Token을 생성합니다.")
    @PostMapping(value ="/api/jwt/token/refresh")
    public ResultVO apiJwtRefresh(
            HttpServletRequest request, @RequestBody JwtVO jwtVO) throws Exception {

        ResultVO rtnVO = new ResultVO();

        if(StringUtil.isNotEmpty(jwtVO.getRefresh_token())){
            // TODO 토큰 REFRESH 로직 추가
        }else{
            rtnVO.setResultMsg("토큰정보가 누락되었습니다.");
            rtnVO.putResult("result",false);
        }
        return rtnVO;
    }
}
