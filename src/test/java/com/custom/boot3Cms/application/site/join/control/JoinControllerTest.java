//package com.custom.boot3Cms.application.site.join.control;
//
//import com.custom.boot3Cms.application.common.config.security.jwt.JwtAuthenticationFilter;
//import com.custom.boot3Cms.application.mng.user.service.UserMngService;
//import com.custom.boot3Cms.application.site.join.service.JoinService;
//import com.custom.boot3Cms.application.site.join.vo.JoinVO;
//import com.custom.boot3Cms.config.spring.SecurityConfig;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@WebMvcTest(controllers = JoinController.class,
//        excludeFilters = {
//                // WebMvcTest 의 경우, MVC 에만 중점을 두기 때문에, @Service 등의 어노테이션에 대한 Bean 등록이 이루어지지 않아 Security 관련 Filter 를 제외합니다.
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class),
////                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityAuthenticationFilter.class),
//                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class),
//        }
//)
//class JoinControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean(name="userMngService")
//    UserMngService userMngService;
//
//    @MockBean(name="joinService")
//    JoinService joinService;
//
//    ObjectMapper mapper = new ObjectMapper();
//    String postContent = "";
//
//
//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext) throws Exception {
//        this.mockMvc = MockMvcBuilders
//                .webAppContextSetup(webApplicationContext)
////                .defaultResponseCharacterEncoding("UTF-8")
//                //.defaultRequest(post("/**").with(csrf()))
//                .build();
//
////        when(joinService.checkUserId())
//    }
//
//    @Test
//    @WithMockUser
//    void getUserCheck() throws Exception {
////        JoinVO givenVO = new JoinVO();
////        givenVO.setUser_id("test");
//
//        Map<String,Object> givenMap = new HashMap<>();
//        givenMap.put("result",true);
//        givenMap.put("rMsg","사용가능한 아이디입니다.");
//
//        given(joinService.checkUserId("test")).willReturn(givenMap);
//
//
//
//        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","test");
////        map.put("flag","chk");
//
//        // when / then
//        mockMvc.perform(post("/join/user/check")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(map))
//                        .characterEncoding("UTF-8")
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser
//    void joinProc() throws Exception {
//        JoinVO givenVO = new JoinVO();
//        givenVO.setUser_id("test");
//        givenVO.setUser_id_check("Y");
//        givenVO.setUser_email("test@test.com");
//        givenVO.setUser_name("test_name");
//        givenVO.setUser_pwd("1234");
//
//        given(joinService.userProc(givenVO)).willReturn(1);
//
//
//        Map<String,Object> map = new HashMap<>();
//        map.put("user_id","test");
//        map.put("user_id_check","Y");
//        map.put("email","test@test.com");
//        map.put("user_name","test_name");
//        map.put("user_pwd","1234");
//
//        mockMvc.perform(post("/join/proc")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(map))
//                .characterEncoding("UTF-8")
//        ).andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print());
//    }
//}