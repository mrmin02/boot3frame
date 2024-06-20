package com.custom.boot3Cms.application.mng.bbs.control;

import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.bbs.service.BbsMngService;
import com.custom.boot3Cms.application.mng.bbs.vo.BbsMngVO;
import com.custom.boot3Cms.application.mng.code.service.CodeMngService;
import com.custom.boot3Cms.application.mng.code.vo.CodeMngVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.ui.Model;
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
 * 게시판 관리 컨트롤러
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-05-12 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-05-12 */
@RestController
public class BbsMngController {

    //게시판 관리 서비스
    @Resource(name = "bbsMngService")
    private BbsMngService bbsMngService;

    //코드 관리 서비스
    @Resource(name = "codeService")
    private CodeMngService codeService;

    /**
     * 게시판 목록
     *
     * @param bbsMngVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시판 리스트")
    @GetMapping("/mng/bbs/list")
    public String bbsList(@RequestBody BbsMngVO bbsMngVO
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
    ) throws Exception {
        model.addAttribute("list", bbsMngService.getBbsList(bbsMngVO));
        return "mng/bbs/list";
    }

    /**
     * 게시판 코드 중복검사
     *
     * @param bbsMngVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시판 코드 중복검사")
    @PostMapping("/mng/bbs/code/check")
    public boolean checkBbsCode(@RequestBody BbsMngVO bbsMngVO
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
    ) throws Exception {
        return bbsMngService.checkBbsCode(bbsMngVO);
    }

    /**
     * 게시판 정보
     *
     * @param bbsMngVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시판 상세정보")
    @GetMapping("/mng/bbs/info")
    public String bbsForm(@RequestBody BbsMngVO bbsMngVO
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
    ) throws Exception {
        if(StringUtil.isNotEmpty(bbsMngVO.getBbs_info_seq())){
            Map<String, Object> map = bbsMngService.getBbsDetail(new BbsMngVO(bbsMngVO.getBbs_info_seq()));
            if((boolean)map.get("result")){
                BbsMngVO vo = (BbsMngVO) map.get("value");
                if(vo != null){
                    model.addAttribute("bbsMngVO", vo);
                    model.addAttribute("authList", bbsMngService.getBbsAuth(vo));
                    if("Y".equals(vo.getCategory_use_yn())){
                        model.addAttribute("categoryList", bbsMngService.getBbsCategory(vo));
                    }
                    if("Y".equals(vo.getAttach_file_use_yn())){
                        model.addAttribute("attachFileList", bbsMngService.getBbsFileInfo(vo));
                    }
                }
            }else{
                redirectAttributes.addFlashAttribute("rHeader",map.get("rHeader"));
                redirectAttributes.addFlashAttribute("rMsg",map.get("rMsg"));
                return "redirect:/mng/bbs/list";
            }
        }
        CodeMngVO userAuthConfVO = new CodeMngVO();
        userAuthConfVO.setCode("user_auth");
        model.addAttribute("user_auth", codeService.getCodeToAuthConf(userAuthConfVO));
        return "mng/bbs/form";
    }

    /**
     * 게시판 등록
     * @param bbsMngVO
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시판 등록")
    @PostMapping(value = "/mng/bbs/proc")
    public String createBbs(@RequestBody BbsMngVO bbsMngVO,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          ModelMap model,
                          HttpSession session,
                          RedirectAttributes redirectAttributes,
                          Principal principal) throws Exception {
        if(principal != null){
            LoginVO vo = CommonUtil.fn_getUserAuth(principal);
            bbsMngVO.setInpt_seq(vo.getUser_seq());
            bbsMngVO.setUpd_seq(vo.getUser_seq());
        }
        Map<String, Object>  map = bbsMngService.setBbs(bbsMngVO);
        
        String rtnUrl = "";
        if((boolean)map.get("result")){
            
        }else{
            
        }
        
        return "";
    }

    /**
     * 게시판 수정
     * @param bbsMngVO
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시판 수정")
    @PutMapping(value = "/mng/bbs/proc")
    public String updateBbs(@RequestBody BbsMngVO bbsMngVO,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            ModelMap model,
                            HttpSession session,
                            RedirectAttributes redirectAttributes,
                            Principal principal) throws Exception {
        if(principal != null){
            LoginVO vo = CommonUtil.fn_getUserAuth(principal);
            bbsMngVO.setInpt_seq(vo.getUser_seq());
            bbsMngVO.setUpd_seq(vo.getUser_seq());
        }
        Map<String, Object>  map = bbsMngService.updateBbs(bbsMngVO);

        String rtnUrl = "";
        if((boolean)map.get("result")){

        }else{

        }

        return "";
    }

    /**
     * 게시판 삭제
     * @param bbsMngVO
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시판 삭제")
    @DeleteMapping(value = "/mng/bbs/proc")
    public String deleteBbs(@RequestBody BbsMngVO bbsMngVO,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            ModelMap model,
                            HttpSession session,
                            RedirectAttributes redirectAttributes,
                            Principal principal) throws Exception {
        if(principal != null){
            LoginVO vo = CommonUtil.fn_getUserAuth(principal);
            bbsMngVO.setInpt_seq(vo.getUser_seq());
            bbsMngVO.setUpd_seq(vo.getUser_seq());
        }
        Map<String, Object>  map = bbsMngService.deleteBbs(bbsMngVO);

        String rtnUrl = "";
        if((boolean)map.get("result")){

        }else{

        }

        return "";
    }

    /**
     * BBS_INFO_SEQ로 게시판 존재여부 확인
     * @param bbs_seq
     * @param model
     * @param request
     * @param response
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시판 존재여부 확인")
    @GetMapping("/mng/{bbs_seq}/config/bbs/cnt")
    public String menuLinkList(@PathVariable("bbs_seq") String bbs_seq
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
            , Principal principal) throws Exception{
        BbsMngVO vo = new BbsMngVO();
        vo.setBbs_info_seq(bbs_seq);
        return bbsMngService.getBbsCnt(vo);
    }

}

