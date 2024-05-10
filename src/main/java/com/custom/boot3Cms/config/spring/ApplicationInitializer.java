package com.custom.boot3Cms.config.spring;


import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


/**
 * ApplicationInitializer ( WebApplicationInitializer )
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-05-10 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-05-10 */
public class ApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
//        rootContext.register(MybatisConfig.class);
//        rootContext.register(SecurityConfig.class);
        rootContext.register(SwaggerConfig.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));
    }
}
