package com.custom.boot3Cms.site.main;

import com.custom.boot3Cms.application.common.config.security.SecurityAuthenticationFilter;
import com.custom.boot3Cms.application.common.config.security.jwt.JwtAuthenticationFilter;
import com.custom.boot3Cms.application.site.main.control.MainController;
import com.custom.boot3Cms.application.site.main.service.MainService;
import com.custom.boot3Cms.config.spring.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MainController.class,
    excludeFilters = {
        // WebMvcTest 의 경우, MVC 에만 중점을 두기 때문에, @Service 등의 어노테이션에 대한 Bean 등록이 이루어지지 않아 Security 관련 Filter 를 제외합니다.
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityAuthenticationFilter.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class),
    }
)
public class MainControllerTests {

    MultiValueMap<String,String> params; // url 요청에 추가적인 parameter

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MainService mainService;

    /**
     * Given value
     * 테스트를 위한 Given
     * @throws Exception
     */
    @BeforeEach
    public void beforeInit() throws Exception {
        this.params = new LinkedMultiValueMap<>();
        this.params.add("test1","1");
    }

    /**
     * Given value 를 위한
     * @throws Exception
     */
    @BeforeEach
    public void beforeConetnt() throws Exception {

    }

    @Test
    @WithMockUser // security 설정을 exclude 하여, 401 오류 발생 > MockUser 주입
    public void mainTest() throws Exception {
        //given

        // when / then
        mockMvc.perform(get("/main")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .params(params)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

    }

}
