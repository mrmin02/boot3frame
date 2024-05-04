package com.custom.boot3Cms.application.mng.code.controller;

import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.code.service.CodeService;
import com.custom.boot3Cms.application.mng.code.vo.CodeVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * 관리자 코드관리 컨트롤러
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일       / 수정자 / 수정내용
 * 	------------------------------------------
 * 	2017-09-06 / 최재민 / 최초 생성
 * </pre>
 * @since 2017-09-06
 */


@Controller
public class CodeController {

    @Resource(name = "codeService")
    CodeService codeService;

    /**
     * 코드 등록 FORM
     *
     * @param codeVO
     * @param model
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @throws Exception
     **/
    @RequestMapping("/mng/system/code/list")
    public String codeForm(@ModelAttribute("codeVO") CodeVO codeVO
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
    ) throws Exception {
        return "mng/system/code/list";
    }

    /**
     * 코드 리스트
     *
     * @param codeVO
     * @param model
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping("/mng/system/code/getList")
    @ResponseBody
    public List<CodeVO> codeList(@ModelAttribute("codeVO") CodeVO codeVO
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
    ) throws Exception {
        return codeService.getCodeList(codeVO);
    }

    /**
     * 코드 중복 체크
     *
     * @param codeVO
     * @param model
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping("/mng/system/code/check")
    @ResponseBody
    public Map<String, Object> codeCheck(@ModelAttribute("codeVO") CodeVO codeVO
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
    ) throws Exception {
        return codeService.getCodeCheck(codeVO);
    }

    /**
     * 코드 등록/수정/삭제
     *
     * @param codeVO
     * @param model
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping("/mng/system/code/proc")
    public String codeProc(@ModelAttribute("codeVO") CodeVO codeVO
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
            , Principal principal
    ) throws Exception {
        if(principal != null){
            LoginVO vo = CommonUtil.fn_getUserAuth(principal);
            codeVO.setInpt_seq(vo.getUser_seq());
            codeVO.setUpd_seq(vo.getUser_seq());
        }
        Map<String, Object> rtnMap = codeService.codeProc(codeVO);
        if ((boolean) rtnMap.get("result")) {
            redirectAttributes.addFlashAttribute("rHeader", rtnMap.get("rHeader"));
            redirectAttributes.addFlashAttribute("rMsg", rtnMap.get("rMsg"));
            return "redirect:/mng/system/code/list";
        } else {
            model.addAttribute("codeVO", codeVO);
            model.addAttribute("rHeader", rtnMap.get("rHeader"));
            model.addAttribute("rMsg", rtnMap.get("rMsg"));
            return "mng/system/code/list";
        }
    }

    /**
     * 코드 NAME 출력 AJAX
     *
     * @param codeVO
     * @param model
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping("/ajax/system/code/text")
    @ResponseBody
    public String codeToNameAjax(@ModelAttribute("codeVO") CodeVO codeVO
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
    ) throws Exception {
        return codeService.getCodeToText(codeVO.getCode());
    }

    /**
     * 코드 목록 출력 AJAX (param = PRT_SEQ)
     *
     * @param codeVO
     * @param model
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping("/ajax/system/code/prt/list")
    @ResponseBody
    public List<CodeVO> codeListAjax(@ModelAttribute("codeVO") CodeVO codeVO
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
    ) throws Exception {
        return codeService.getCodeToTag(codeVO.getCode());
    }

    /**
     * 코드 상세정보 출력
     *
     * @param codeVO
     * @param model
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequestMapping("/ajax/system/code/detail")
    @ResponseBody
    public CodeVO codeDetailAjax(@ModelAttribute("codeVO") CodeVO codeVO
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
    ) throws Exception {
        return codeService.getCodeDetail(codeVO);
    }

}
