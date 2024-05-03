package com.custom.boot3Cms.application.site.main.service;

import com.custom.boot3Cms.application.site.main.mapper.MainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    @Autowired
    MainMapper mainMapper;

    public int test() throws Exception {
        return mainMapper.test();
    }

}
