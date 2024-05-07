package com.custom.boot3Cms.config.spring;


import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


/**
 * Initializer
 */
public class ApplicationInitializer  { //implements WebApplicationInitializer

//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
//        rootContext.register(MybatisConfig.class);
//        rootContext.register(SecurityConfig.class);
//        rootContext.register(SwaggerConfig.class);
//        servletContext.addListener(new ContextLoaderListener(rootContext));
//    }
}
