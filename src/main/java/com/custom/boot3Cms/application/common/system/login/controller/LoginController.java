package com.custom.boot3Cms.application.common.system.login.controller;

import com.custom.boot3Cms.application.common.system.login.service.LoginService;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;


@Controller
public class LoginController {

    @Resource(name="loginService")
    LoginService loginService;


    @RequestMapping(value = "/login")
    public String loginForm(@ModelAttribute("loginVO") LoginVO loginVO,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            ModelMap model,
                            HttpSession session,
                            RedirectAttributes redirectAttributes,
                            Principal principal) throws Exception {
        if(principal != null){
            redirectAttributes.addFlashAttribute("rHeader", "알림!");
            redirectAttributes.addFlashAttribute("rMsg", "이미 로그인 되어 있습니다.");
            return "redirect:/mng/main";
        }
        return "/mng/login";
    }

}
