
package com.custom.boot3Cms.application.mng.article.control;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.article.service.ArticleMngService;
import com.custom.boot3Cms.application.mng.article.vo.ArticleMngVO;
import com.custom.boot3Cms.application.mng.bbs.service.BbsMngService;
import com.custom.boot3Cms.application.mng.bbs.vo.BbsMngVO;
import com.custom.boot3Cms.application.mng.code.service.CodeMngService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "게시글 관리 컨트롤러")
@RestController
public class ArticleMngController {

    //게시글 관리 서비스
    @Resource(name="articleMngService")
    ArticleMngService articleMngService;

    //게시판 관리 서비스
    @Resource(name = "bbsMngService")
    private BbsMngService bbsMngService;

    /**
     * 게시글 목록 PAGE
     *
     * @param articleMngVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글 목록")
    @GetMapping("/api/mng/article/{bbs_cd}/list")
    public ResultVO articleList(@ModelAttribute ArticleMngVO articleMngVO
            , @PathVariable("bbs_cd") String bbs_cd
            , HttpServletRequest request
            , HttpServletResponse response
    ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            articleMngVO.setBbs_cd(bbs_cd);
            Map<String, Object> map = bbsMngService.getBbsDetail(articleMngVO);
            if((boolean)map.get("result")){
//                BbsMngVO bbsMngVO = (BbsMngVO) map.get("value");
//                CommonUtil.fn_copyClass(articleMngVO, bbsMngVO);
                resultVO.putResult("total_cnt", articleMngService.getArticleListCNT(articleMngVO));
                resultVO.putResult("list", articleMngService.getArticleList(articleMngVO));
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "게시글 목록 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

//    /**
//     * 게시글 목록 DATA
//     *
//     * @param articleMngVO
//     * @param request
//     * @param response
//     * @return
//     * @throws Exception
//     */
//    @ResponseBody
//    @RequestMapping("/mng/article/list/{bbs_cd}/data")
//    public Map<String, Object> articleDataList(@ModelAttribute("articleMngVO") ArticleMngVO articleMngVO
//            , @PathVariable("bbs_cd") String bbs_cd
//            , Model model
//            , HttpServletRequest request
//            , HttpServletResponse response
//            , RedirectAttributes redirectAttributes
//    ) throws Exception {
//        articleMngVO.setRecordCountPerPage(Integer.parseInt(request.getParameter("length")));
//        articleMngVO.setPageIndex(Integer.parseInt(request.getParameter("start")) / Integer.parseInt(request.getParameter("length")) + 1);
//        articleMngVO.setBbs_cd(bbs_cd);
//        Map<String, Object> rtnMap = new HashMap<>();
//        if (StringUtil.isNotEmpty(request.getParameter("search[value]"))) {
//            articleMngVO.setSearchKeyword(request.getParameter("search[value]"));
//        }
//        int totalCount = articleMngService.getArticleListCNT(articleMngVO);
//        articleMngVO.setTotalCount(totalCount);
//        rtnMap.put("draw", request.getParameter("draw"));
//        rtnMap.put("recordsTotal", totalCount);
//        rtnMap.put("recordsFiltered", totalCount);
//        rtnMap.put("pageIndex", articleMngVO.getPageIndex());
//        rtnMap.put("showLength", Integer.parseInt(request.getParameter("length")));
//        rtnMap.put("data", articleMngService.getArticleList(articleMngVO));
//        return rtnMap;
//    }

    /**
     * 게시글 상세보기
     *
     * @param articleMngVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글 상세보기")
    @GetMapping("/api/mng/article/{bbs_cd}/detail/{article_seq}")
    public ResultVO articleDetail(@ModelAttribute("articleMngVO") ArticleMngVO articleMngVO
            , @PathVariable("bbs_cd") String bbs_cd
            , @PathVariable("article_seq") String article_seq
            , HttpServletRequest request
            , HttpServletResponse response
    ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            articleMngVO.setBbs_cd(bbs_cd);
            articleMngVO.setArticle_seq(article_seq);
            Map<String, Object> map = articleMngService.getArticle(articleMngVO);

            if((boolean)map.get("result")){
                resultVO.putResult("commentList", articleMngService.getCommentList(articleMngVO));
                resultVO.putResult("data",  map.get("value"));
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "게시글 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 게시글의 게시판 설정 정보
     *
     * @param articleMngVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글의 게시판 설정 정보")
    @GetMapping("/mng/article/{bbs_cd}/setting")
    public String articleForm(@ModelAttribute("articleMngVO") ArticleMngVO articleMngVO
            , @PathVariable("bbs_cd") String bbs_cd
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
    ) throws Exception {
        String rtnUrl = "";
        articleMngVO.setBbs_cd(bbs_cd);
        Map<String, Object> map = articleMngService.getArticle(articleMngVO);
        ArticleMngVO vo = (ArticleMngVO) map.get("value");

        if(StringUtil.isNotEmpty(articleMngVO.getArticle_seq())){
            if((boolean)map.get("result")){
                if(vo != null){
                    // 답글이 아닐 경우
                    if(!articleMngVO.getFlag().equals("r")){
                        model.addAttribute("articleMngVO", vo);
                        model.addAttribute("proglink_list", map.get("proglink_list"));
                    }else{
                        // 답글일 경우
                        articleMngVO.setPrt_seq(vo.getArticle_seq());
                        articleMngVO.setPrt_grp(vo.getPrt_grp());
                        articleMngVO.setBbs_order(vo.getBbs_order());
                        articleMngVO.setArticle_seq("");
                        articleMngVO.setUser_id("");
                        articleMngVO.setUser_pwd("");
                        model.addAttribute("articleMngVO", articleMngVO);
                    }
                    model.addAttribute("commentList", articleMngService.getCommentList(articleMngVO));
                }
            }else{
                redirectAttributes.addFlashAttribute("rHeader",map.get("rHeader"));
                redirectAttributes.addFlashAttribute("rMsg",map.get("rMsg"));
                return "redirect:/mng/article/"+bbs_cd+"/list";
            }
        }else{
            vo.setInpt_date("");
        }
        if(vo != null && "Y".equals(vo.getCategory_use_yn())){
            model.addAttribute("categoryList", bbsMngService.getBbsCategory(vo));
        }
        if(vo != null && "Y".equals(vo.getAttach_file_use_yn())){
            JSONArray jsonArray = new JSONArray();
            bbsMngService.getBbsFileInfo(vo).forEach(v-> {
                JSONObject data = new JSONObject();
                data.put("file_size", v.getFile_size());
                data.put("file_type", v.getFile_type());
                jsonArray.add(data);
            });
            model.addAttribute("attachFileJSON", jsonArray);
        }

        return rtnUrl;
    }

    /**
     * 게시글 등록/수정/삭제
     * @param articleMngVO
     * @param request
     * @param response
     * @param model
     * @param session
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/mng/article/{bbs_cd}/proc")
    public String articleProc(@ModelAttribute("articleMngVO") ArticleMngVO articleMngVO,
                          @PathVariable("bbs_cd") String bbs_cd,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          ModelMap model,
                          HttpSession session,
                          RedirectAttributes redirectAttributes,
                          Principal principal) throws Exception {
        articleMngVO.setBbs_cd(bbs_cd);
        // IP SET
        articleMngVO.setInpt_ip(StringUtil.getIP(request));
        Map<String, Object> map = null;
        switch (articleMngVO.getFlag()){
            case "c": map = articleMngService.setArticle(articleMngVO, principal);
                break;
            case "u": articleMngVO.setUpd_ip(StringUtil.getIP(request));
                map = articleMngService.updArticle(articleMngVO, principal);
                break;
            case "d": map = articleMngService.updArticle(articleMngVO, principal);
                break;
            case "r": map = articleMngService.setArticle(articleMngVO, principal);
                break;
            case "h": map = articleMngService.updArticle(articleMngVO, principal);
                break;
            default:
                redirectAttributes.addFlashAttribute("rHeader", "에러!");
                redirectAttributes.addFlashAttribute("rMsg", "잘못 된 접근입니다.");
                return "redirect:/mng/article/"+bbs_cd+"/list";
        }
        String rtnUrl = "";
        if((boolean)map.get("result")){
            if("d".equals(articleMngVO.getFlag())){
                rtnUrl = "redirect:/mng/article/"+bbs_cd+"/list";
            }else{
                rtnUrl = "redirect:/mng/article/"+bbs_cd+"/detail/"+articleMngVO.getArticle_seq();
            }
            redirectAttributes.addFlashAttribute("rHeader", map.get("rHeader"));
            redirectAttributes.addFlashAttribute("rMsg", map.get("rMsg"));
        }else{
            if("d".equals(articleMngVO.getFlag())){
                redirectAttributes.addFlashAttribute("rHeader", map.get("rHeader"));
                redirectAttributes.addFlashAttribute("rMsg", map.get("rMsg"));
                rtnUrl = "redirect:/mng/article/"+bbs_cd+"/detail/"+articleMngVO.getArticle_seq();
            }else if("c".equals(articleMngVO.getFlag()) || "u".equals(articleMngVO.getFlag())){
                model.addAttribute("rHeader", map.get("rHeader"));
                model.addAttribute("rMsg", map.get("rMsg"));
                model.addAttribute("articleMngVO", articleMngVO);
                if(articleMngVO != null && "Y".equals(articleMngVO.getCategory_use_yn())){
                    model.addAttribute("categoryList", bbsMngService.getBbsCategory(articleMngVO));
                }
                if(articleMngVO != null && "Y".equals(articleMngVO.getAttach_file_use_yn())){
                    JSONArray jsonArray = new JSONArray();
                    bbsMngService.getBbsFileInfo(articleMngVO).forEach(v-> {
                        JSONObject data = new JSONObject();
                        data.put("file_size", v.getFile_size());
                        data.put("file_type", v.getFile_type());
                        jsonArray.add(data);
                    });
                }

                if(articleMngVO.getBbs_type().equals("BBS_004")){
                    rtnUrl = "mng/article/link/form";
                } else if(articleMngVO.getBbs_type().equals("BBS_005")){ // 발행지 게시판
                    rtnUrl =  "mng/article/publish/form";
                } else if(articleMngVO.getBbs_type().equals("BBS_001")){ // 웹진형 게시판{
                    rtnUrl =  "mng/article/webzine/form";
                } else if(articleMngVO.getBbs_type().equals("BBS_006")){ // 공개정보형 게시판{
                    rtnUrl =  "mng/article/public/form";
                } else{
                    rtnUrl =  "mng/article/form";
                }
            }else{
                redirectAttributes.addFlashAttribute("rHeader", map.get("rHeader"));
                redirectAttributes.addFlashAttribute("rMsg", map.get("rMsg"));
                rtnUrl = "redirect:/mng/article/"+bbs_cd+"/list";
            }
        }
        return rtnUrl;
    }

    /**
     * 댓글 PROC
     *
     * @param articleMngVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/mng/article/{bbs_cd}/{article_seq}/comment")
    public Map<String, Object> commentProc(@ModelAttribute("articleMngVO") ArticleMngVO articleMngVO
            , @PathVariable("bbs_cd") String bbs_cd
            , @PathVariable("article_seq") String article_seq
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
            , Principal principal
    ) throws Exception {
        if(principal != null) {
            LoginVO loginVO = CommonUtil.fn_getUserAuth(principal);

            articleMngVO.setInpt_seq(loginVO.getUser_seq());
            articleMngVO.setInpt_user_name(loginVO.getUser_name());

            if(articleMngVO.getFlag().equals("u")){
                articleMngVO.setUpd_seq(loginVO.getUser_seq());
                articleMngVO.setUpd_user_name(loginVO.getUser_name());
            }

        }
        articleMngVO.setBbs_cd(bbs_cd);
        articleMngVO.setArticle_seq(article_seq);
        return articleMngService.commentProc(articleMngVO);
    }



}

