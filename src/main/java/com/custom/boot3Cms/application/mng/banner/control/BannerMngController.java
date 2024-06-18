package com.custom.boot3Cms.application.mng.banner.control;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.banner.service.BannerMngService;
import com.custom.boot3Cms.application.mng.banner.vo.BannerMngVO;
import com.custom.boot3Cms.application.common.system.file.service.FileMngService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

/**
 * 배너 관리 Controller
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-06-18 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-06-18 */
@Tag(name = "배너 관리 컨트롤러")
@RestController
public class BannerMngController {

    @Resource(name = "bannerMngService")
    BannerMngService bannerMngService;

    @Resource(name = "fileMngService")
    FileMngService fileMngService;

    /**
     * 배너 목록
     * @param bannerMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/api/mng/banner/list")
    public ResultVO bannerList(@ModelAttribute("BannerMngVO") BannerMngVO bannerMngVO,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            resultVO.putResult("total_cnt", bannerMngService.getBannerListCNT(bannerMngVO));
            resultVO.putResult("list", bannerMngService.getBannerList(bannerMngVO));
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "회원 수정 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 배너 상세보기
     * @param bannerMngVO
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/mng/banner/detail/{banner_seq}")
    public String bannerDetail(@ModelAttribute("bannerMngVO") BannerMngVO bannerMngVO,
                                @PathVariable("banner_seq") String banner_seq,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                ModelMap model,
                                HttpSession session,
                                RedirectAttributes redirectAttributes,
                                Principal principal) throws Exception {
        Map<String, Object> map = bannerMngService.getBannerDetail(new BannerMngVO(banner_seq));
        if((boolean)map.get("result")){
            model.addAttribute("bannerMngVO", map.get("value"));
        }else{
            redirectAttributes.addFlashAttribute("rHeader",map.get("rHeader"));
            redirectAttributes.addFlashAttribute("rMsg",map.get("rMsg"));
            return "redirect:/mng/banner/list";
        }
        return "mng/banner/detail";
    }

    /**
     * 배너 등록/수정 FORM
     * @param bannerMngVO
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/mng/banner/form")
    public String bannerForm(@ModelAttribute("bannerMngVO") BannerMngVO bannerMngVO,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               ModelMap model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes,
                               Principal principal) throws Exception {
        if(StringUtil.isNotEmpty(bannerMngVO.getBanner_seq())){
            Map<String, Object> map = bannerMngService.getBannerDetail(new BannerMngVO(bannerMngVO.getBanner_seq()));
            if((boolean)map.get("result")){
                BannerMngVO vo = (BannerMngVO) map.get("value");
                if(vo != null){
                    model.addAttribute("bannerMngVO", vo);
                }
            }else{
                redirectAttributes.addFlashAttribute("rHeader",map.get("rHeader"));
                redirectAttributes.addFlashAttribute("rMsg",map.get("rMsg"));
                return "redirect:/mng/banner/list";
            }
        }
        return "mng/banner/form";
    }

    /**
     * 배너 등록/수정/삭제
     * @param bannerMngVO
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/mng/banner/proc")
    public String bannerProc(@ModelAttribute("bannerMngVO") BannerMngVO bannerMngVO,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  ModelMap model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes,
                                  Principal principal) throws Exception {
        if(principal != null){
            LoginVO vo = CommonUtil.fn_getUserAuth(principal);
            bannerMngVO.setInpt_seq(vo.getUser_seq());
            bannerMngVO.setUpd_seq(vo.getUser_seq());
        }
        Map<String, Object> map = bannerMngService.bannerProc(bannerMngVO);
        String rtnUrl = "";
        if((boolean)map.get("result")){
            if("d".equals(bannerMngVO.getFlag())){
                rtnUrl = "redirect:/mng/banner/list";
            }else{
                rtnUrl = "redirect:/mng/banner/detail/"+bannerMngVO.getBanner_seq();
            }
            redirectAttributes.addFlashAttribute("rHeader", map.get("rHeader"));
            redirectAttributes.addFlashAttribute("rMsg", map.get("rMsg"));
        }else{
            if("d".equals(bannerMngVO.getFlag())){
                redirectAttributes.addFlashAttribute("rHeader", map.get("rHeader"));
                redirectAttributes.addFlashAttribute("rMsg", map.get("rMsg"));
                rtnUrl = "redirect:/mng/banner/detail/"+bannerMngVO.getBanner_seq();
            }else if("c".equals(bannerMngVO.getFlag()) || "u".equals(bannerMngVO.getFlag())){
                model.addAttribute("rHeader", map.get("rHeader"));
                model.addAttribute("rMsg", map.get("rMsg"));
                model.addAttribute("bannerMngVO", bannerMngVO);
                rtnUrl = "mng/banner/form";
            }else{
                redirectAttributes.addFlashAttribute("rHeader", map.get("rHeader"));
                redirectAttributes.addFlashAttribute("rMsg", map.get("rMsg"));
                rtnUrl = "redirect:/mng/banner/list";
            }
        }
        return rtnUrl;
    }



}
