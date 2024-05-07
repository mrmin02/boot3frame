package com.custom.boot3Cms;

import com.custom.boot3Cms.config.spring.ApplicationInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
public class Boot3CmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(Boot3CmsApplication.class, args);
	}

}
