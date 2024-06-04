package com.custom.boot3Cms.config.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
//import io.swagger.v3.oas.integration.SwaggerUiConfigParametersBuilder;
import org.springdoc.core.customizers.OpenApiCustomizer;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPIV3Parser().readLocation("classpath:/swagger/api-docs.yaml", null, null).getOpenAPI();
//    }

    @Bean
    public OpenAPI customOpenAPI() throws Exception {

        String swagger_mode = env.getProperty("Globals.swagger.mode");
        String swagger_yaml_location = env.getProperty("Globals.swagger.yaml.location");

        // FIXME Yaml 파일을 import 해도, 프로젝트 내부에 swagger 관련 어노테이션 존재하면, 두 내용이 섞임..
        // FIXME swagger/index.html 에서 FIXME resource 경로로 접근해서 yaml 파일 import
        if(swagger_mode.equals("yaml")) {
            if(new ClassPathResource(swagger_yaml_location).exists()){
                try (InputStream inputStream = new ClassPathResource(swagger_yaml_location).getInputStream()) {
                    System.out.println("###### Swagger try import yaml #######");
                    return Yaml.mapper().readValue(inputStream, OpenAPI.class);
//                    YAMLFactory factory = new YAMLFactory();
//                    factory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
//                    factory.enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
//                    factory.enable(YAMLGenerator.Feature.SPLIT_LINES);
//                    factory.enable(YAMLGenerator.Feature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS);
//                    ObjectMapper mapper = new ObjectMapper(factory);
//                    return mapper.readValue(inputStream, OpenAPI.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("###### Swagger default auto generate #######");
        /**
         * swagger auto generate
         */
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




        // parser 는 java 11 까지 지원
//        try{
//            OpenAPI yamlOpenAPI = new OpenAPIV3Parser().read("src/main/resources/swagger/api-docs.yaml");
//            openAPI.getPaths().putAll(yamlOpenAPI.getPaths());
//            openAPI.getComponents().getSchemas().putAll(yamlOpenAPI.getComponents().getSchemas());
//        }catch (Exception e){
//            e.printStackTrace();
//            System.err.println("#### Swagger OpenAPI read Error !! ####");
//        }
    }

}
