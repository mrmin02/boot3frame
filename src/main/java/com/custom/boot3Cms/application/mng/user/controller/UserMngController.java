package com.custom.boot3Cms.application.mng.user.controller;

import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.code.service.CodeService;
import com.custom.boot3Cms.application.mng.code.vo.CodeVO;
import com.custom.boot3Cms.application.mng.user.service.UserMngService;
import com.custom.boot3Cms.application.mng.user.vo.UserMngVO;
import com.custom.boot3Cms.application.mng.user.vo.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * 회원관리 컨트롤러
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2018-03-13 / 최재민  / 최초 생성
 * 	2020-08-28 / 최민석  /  로그인 되어 있을 때, , login 요청이 오면, 사용자의 권한에 따라 리다이렉트
 * </pre>
 * @since 2018-03-13
 */
@Controller
public class UserMngController {

    @Resource(name = "userMngService")
    UserMngService userMngService;

    @Resource(name = "codeService")
    CodeService codeService;

    /**
     * 회원 목록
     *
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/mng/user/list")
    public String userList(@ModelAttribute("userVO") UserVO userVO,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           ModelMap model,
                           HttpSession session,
                           RedirectAttributes redirectAttributes,
                           Principal principal) throws Exception {
        model.addAttribute("list", userMngService.getUserList(userVO));
        return "mng/user/list";
    }

    /**
     * 관리자 회원 등록/수정 FORM
     *
     * @param userVO
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/mng/admin/form")
    public String adminForm(@ModelAttribute("userVO") UserVO userVO,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           ModelMap model,
                           HttpSession session,
                           RedirectAttributes redirectAttributes,
                           Principal principal) throws Exception {
        if(principal == null) {
            return "redirect:/mng/user/list";
        }
        LoginVO login = CommonUtil.fn_getUserAuth(principal);
        if (StringUtil.isNotEmpty(login.getUser_seq())) {
            userVO.setUser_seq(login.getUser_seq());
            Map<String, Object> map = userMngService.getUserAdminDetail(userVO);
            if ((boolean) map.get("result")) {
                UserVO vo = (UserVO) map.get("value");
                if (vo != null) {
                    model.addAttribute("userVO", vo);
                    return "mng/user/adminForm";
                }
            } else {
                redirectAttributes.addFlashAttribute("rHeader", map.get("rHeader"));
                redirectAttributes.addFlashAttribute("rMsg", map.get("rMsg"));
            }
        }
        return "redirect:/mng/user/list";

    }


    /**
     * 회원 상세보기
     *
     * @param userVO
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/mng/user/detail/{user_seq}")
    public String bannerDetail(@ModelAttribute("userVO") UserVO userVO,
                               @PathVariable("user_seq") String user_seq,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               ModelMap model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes,
                               Principal principal) throws Exception {
        userVO.setUser_seq(user_seq);
        Map<String, Object> map = userMngService.getUserDetail(userVO);
        if ((boolean) map.get("result")) {
            model.addAttribute("userVO", map.get("value"));
        } else {
            redirectAttributes.addFlashAttribute("rHeader", map.get("rHeader"));
            redirectAttributes.addFlashAttribute("rMsg", map.get("rMsg"));
            return "redirect:/mng/user/list";
        }
        return "mng/user/detail";
    }

    /**
     * 회원 등록/수정 FORM
     *
     * @param userVO
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/mng/user/form")
    public String userForm(@ModelAttribute("userVO") UserVO userVO,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           ModelMap model,
                           HttpSession session,
                           RedirectAttributes redirectAttributes,
                           Principal principal) throws Exception {
        if (StringUtil.isNotEmpty(userVO.getUser_seq())) {
            Map<String, Object> map = userMngService.getUserDetail(userVO);
            if ((boolean) map.get("result")) {
                UserVO vo = (UserVO) map.get("value");
                if (vo != null) {
                    model.addAttribute("userVO", vo);
                }
            } else {
                redirectAttributes.addFlashAttribute("rHeader", map.get("rHeader"));
                redirectAttributes.addFlashAttribute("rMsg", map.get("rMsg"));
                return "redirect:/mng/user/list";
            }
        }
        return "mng/user/form";
    }

    /**
     * 회원 등록/수정/탈퇴|탈퇴복구
     *
     * @param userVO
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/mng/user/proc")
    public String userProc(@ModelAttribute("userVO") UserVO userVO,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           ModelMap model,
                           HttpSession session,
                           RedirectAttributes redirectAttributes,
                           Principal principal) throws Exception {
        if (principal != null) {
            LoginVO vo = CommonUtil.fn_getUserAuth(principal);
            userVO.setInpt_seq(vo.getUser_seq());
            userVO.setUpd_seq(vo.getUser_seq());
        }
        Map<String, Object> map = userMngService.userProc(userVO);
        String rtnUrl = "";
        if ((boolean) map.get("result")) {
            if ("d".equals(userVO.getFlag())) {
                if (userVO.getOut_yn().equals("Y")) {
                    rtnUrl = "redirect:/mng/user/list";
                } else {
                    rtnUrl = "redirect:/mng/user/detail/" + userVO.getUser_seq();
                }
            } else {
                rtnUrl = "redirect:/mng/user/detail/" + userVO.getUser_seq();
            }
            redirectAttributes.addFlashAttribute("rHeader", map.get("rHeader"));
            redirectAttributes.addFlashAttribute("rMsg", map.get("rMsg"));
        } else {
            if ("d".equals(userVO.getFlag())) {
                redirectAttributes.addFlashAttribute("rHeader", map.get("rHeader"));
                redirectAttributes.addFlashAttribute("rMsg", map.get("rMsg"));
                rtnUrl = "redirect:/mng/user/detail/" + userVO.getUser_seq();
            } else if ("c".equals(userVO.getFlag()) || "u".equals(userVO.getFlag())) {
                model.addAttribute("rHeader", map.get("rHeader"));
                model.addAttribute("rMsg", map.get("rMsg"));
                model.addAttribute("userVO", userVO);
                rtnUrl = "mng/user/form";
            } else {
                redirectAttributes.addFlashAttribute("rHeader", map.get("rHeader"));
                redirectAttributes.addFlashAttribute("rMsg", map.get("rMsg"));
                rtnUrl = "redirect:/mng/user/list";
            }
        }
        return rtnUrl;
    }


    /**
     * 회원 ID 중복확인
     *
     * @param userVO
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/mng/user/check")
    public boolean getUserCheck(@ModelAttribute("userVO") UserVO userVO,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                RedirectAttributes redirectAttributes) throws Exception {
        Map<String, Object> map = userMngService.userProc(userVO);
        boolean result = (boolean) map.get("result");
        return result;
    }

}
