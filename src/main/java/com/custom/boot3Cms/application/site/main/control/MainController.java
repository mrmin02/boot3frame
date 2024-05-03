package com.custom.boot3Cms.application.site.main.control;

import com.custom.boot3Cms.application.site.main.service.MainService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    MainService mainService;

    @ApiOperation(value = "", notes = "메인 테스트")
    @GetMapping(value = {"/","/main"})
    public String mainTest() throws Exception {

        // System.out.println(mainService.test()+1);


        return "test123";
    }
}
