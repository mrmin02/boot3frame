package com.custom.boot3Cms;

import com.custom.boot3Cms.config.spring.ApplicationInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
// Security 기본 login 화면 제거
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Boot3CmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(Boot3CmsApplication.class, args);
		System.out.println("----- Application Started -----");
	}

}
