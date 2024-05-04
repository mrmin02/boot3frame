package com.custom.boot3Cms.application.common.system.login.controller;

import com.custom.boot3Cms.application.common.system.login.service.LoginService;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
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

    @Resource(name = "loginService")
    LoginService loginService;

    /**
     * 2020-08-28 최민석    로그인 되어 있을 때, , login 요청이 오면, 사용자의 권한에 따라 리다이렉트
     *
     * @param loginVO
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login")
    public String loginForm(@ModelAttribute("loginVO") LoginVO loginVO,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            ModelMap model,
                            HttpSession session,
                            RedirectAttributes redirectAttributes,
                            Principal principal) throws Exception {
        if (principal != null) {
            redirectAttributes.addFlashAttribute("rHeader", "알림!");
            redirectAttributes.addFlashAttribute("rMsg", "이미 로그인 되어 있습니다.");
            LoginVO login = CommonUtil.fn_getUserAuth(principal);
            if (login.getUser_auth().equals("ROLE_ADMIN")) {
                return "redirect:/mng/main";
            } else {
                return "redirect:/main";
            }

        }
        return "siteEmpty/login";
    }

}
