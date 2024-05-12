package com.custom.boot3Cms.config.spring;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * Swagger Config
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-05-07 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-05-07 */
@Configuration
public class SwaggerConfig {

    @Autowired
    Environment env;

    // javax > jakarta  되면서 spring boot 3 에서는 swagger 버전을 3으로..
//    @Bean
//    public Docket api(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.custom.boot3Cms"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo(){
//        return new ApiInfoBuilder()
//                .title("Swagger Test API Hub")
//                .description("백엔드 Swagger API 명세입니다.")
//                .version("1.0.0")
//                .build();
//    }

    @Bean
    public OpenAPI customOpenAPI() {

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(env.getProperty("Globals.jwt.header.access"));

        return new OpenAPI()
                .info(new Info()
                        .title("REST API 명세")
                        .description("REST API 명세 with Swagger")
                        .version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes(env.getProperty("Globals.jwt.header.access"),
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .in(SecurityScheme.In.HEADER)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .security(Arrays.asList(securityRequirement));
    }
}
