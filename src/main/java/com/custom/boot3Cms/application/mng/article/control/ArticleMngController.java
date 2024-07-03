
package com.custom.boot3Cms.application.mng.article.control;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.article.service.ArticleMngService;
import com.custom.boot3Cms.application.mng.article.vo.ArticleMngVO;
import com.custom.boot3Cms.application.mng.bbs.service.BbsMngService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * 게시글 관리 컨트롤러
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-07-03 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-07-03 */
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
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글의 게시판 설정 정보")
    @GetMapping("/api/mng/article/{bbs_cd}/setting")
    public ResultVO articleForm( @ModelAttribute ArticleMngVO articleMngVO,
            @PathVariable("bbs_cd") String bbs_cd
            , HttpServletRequest request
            , HttpServletResponse response
    ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            articleMngVO.setBbs_cd(bbs_cd);
            Map<String, Object> map = articleMngService.getArticle(articleMngVO);
            ArticleMngVO vo = (ArticleMngVO) map.get("value");

            if(StringUtil.isNotEmpty(articleMngVO.getArticle_seq())) {
                if ((boolean) map.get("result")) {
                    if (vo != null) {
                        // 답글이 아닐 경우
                        if ( articleMngVO.getFlag() == null ||
                                !articleMngVO.getFlag().equals("r")) {
                            resultVO.putResult("data", vo);
                        } else {
                            // 답글일 경우
                            articleMngVO.setPrt_seq(vo.getArticle_seq());
                            articleMngVO.setPrt_grp(vo.getPrt_grp());
                            articleMngVO.setBbs_order(vo.getBbs_order());
                            articleMngVO.setArticle_seq("");
                            articleMngVO.setUser_id("");
                            articleMngVO.setUser_pwd("");
                            resultVO.putResult("articleMngVO", articleMngVO);
                        }
                        resultVO.putResult("commentList", articleMngService.getCommentList(articleMngVO));
                    }
                }
            }else{
                vo.setInpt_date("");
            }
            if(vo != null && "Y".equals(vo.getCategory_use_yn())){
                resultVO.putResult("categoryList", bbsMngService.getBbsCategory(vo));
            }
            if(vo != null && "Y".equals(vo.getAttach_file_use_yn())){
                JSONArray jsonArray = new JSONArray();
                bbsMngService.getBbsFileInfo(vo).forEach(v-> {
                    JSONObject data = new JSONObject();
                    data.put("file_size", v.getFile_size());
                    data.put("file_type", v.getFile_type());
                    jsonArray.add(data);
                });
                resultVO.putResult("attachFileJSON", jsonArray);
            }
            result = true;
            rMsg = String.valueOf(map.get("rMsg"));
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "게시글 설정 정보 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 게시글 등록
     * @param articleMngVO
     * @param request
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글 등록")
    @PostMapping(value = "/api/mng/article/{bbs_cd}/proc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultVO articleProc(@ModelAttribute ArticleMngVO articleMngVO,
                          @PathVariable("bbs_cd") String bbs_cd,
                          HttpServletRequest request,
                          Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            articleMngVO.setBbs_cd(bbs_cd);
            articleMngVO.setInpt_ip(StringUtil.getIP(request));
            if(StringUtil.isEmpty(articleMngVO.getFlag())) {
                articleMngVO.setFlag("c");
            }
            // FIXME flag 값은 c : 생성, r : 답글
            Map<String,Object> map = articleMngService.setArticle(articleMngVO, principal);
            if((boolean)map.get("result")){
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "게시글 등록 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 게시글 수정
     * @param articleMngVO
     * @param request
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글 수정")
    @PutMapping(value = "/api/mng/article/{bbs_cd}/proc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultVO articleUpdateProc(@ModelAttribute ArticleMngVO articleMngVO,
                                @PathVariable("bbs_cd") String bbs_cd,
                                HttpServletRequest request,
                                Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            articleMngVO.setBbs_cd(bbs_cd);
            articleMngVO.setInpt_ip(StringUtil.getIP(request));
            articleMngVO.setFlag("u");
            articleMngVO.setUpd_ip(StringUtil.getIP(request));
            Map<String,Object> map = articleMngService.updArticle(articleMngVO, principal);
            if((boolean)map.get("result")){
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "게시글 수정 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 게시글 삭제
     * @param articleMngVO
     * @param request
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글 삭제")
    @DeleteMapping(value = "/api/mng/article/{bbs_cd}/proc")
    public ResultVO articleDeleteProc(@RequestBody ArticleMngVO articleMngVO,
                                      @PathVariable("bbs_cd") String bbs_cd,
                                      HttpServletRequest request,
                                      Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            articleMngVO.setBbs_cd(bbs_cd);
            articleMngVO.setInpt_ip(StringUtil.getIP(request));
            articleMngVO.setFlag("d");
            articleMngVO.setUpd_ip(StringUtil.getIP(request));
            Map<String,Object> map = articleMngService.updArticle(articleMngVO, principal);
            if((boolean)map.get("result")){
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "게시글 삭제 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 댓글 PROC
     *
     * @param articleMngVO
     * @return
     * @throws Exception
     */
    @Operation(summary = "댓글 등록")
    @PostMapping("/api/mng/article/{bbs_cd}/{article_seq}/comment")
    public ResultVO commentInsert(@RequestBody ArticleMngVO articleMngVO
            , @PathVariable("bbs_cd") String bbs_cd
            , @PathVariable("article_seq") String article_seq
            , Principal principal
    ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null) {
                LoginVO loginVO = CommonUtil.fn_getUserAuth(principal);

                articleMngVO.setInpt_seq(loginVO.getUser_seq());
                articleMngVO.setInpt_user_name(loginVO.getUser_name());
            }
            // FIXME flag 는 댓글 등록, 대댓글 등록만, 만약 flag 이상하면 댓글 등록으로만
            if( articleMngVO.getFlag() == null || 
                    !(articleMngVO.getFlag().equals("c") || articleMngVO.getFlag().equals("r"))){
                articleMngVO.setFlag("c"); // 댓글등록으로 셋팅
            }
            articleMngVO.setBbs_cd(bbs_cd);
            articleMngVO.setArticle_seq(article_seq);
            Map<String, Object> map = articleMngService.commentProc(articleMngVO);

            if((boolean)map.get("result")){
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }

        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "댓글 등록 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 댓글 삭제
     *
     * @param articleMngVO
     * @return
     * @throws Exception
     */
    @Operation(summary = "댓글 복원 및 삭제")
    @PutMapping("/api/mng/article/{bbs_cd}/{article_seq}/comment")
    public ResultVO commentTmpDelete(@RequestBody ArticleMngVO articleMngVO
            , @PathVariable("bbs_cd") String bbs_cd
            , @PathVariable("article_seq") String article_seq
            , Principal principal
    ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null) {
                LoginVO loginVO = CommonUtil.fn_getUserAuth(principal);
                articleMngVO.setUpd_seq(loginVO.getUser_seq());
                articleMngVO.setUpd_user_name(loginVO.getUser_name());
            }

            if(StringUtil.isEmpty(articleMngVO.getFlag())){
                articleMngVO.setFlag("d");
            }
            articleMngVO.setBbs_cd(bbs_cd);
            articleMngVO.setArticle_seq(article_seq);
            Map<String, Object> map = articleMngService.commentProc(articleMngVO);

            if((boolean)map.get("result")){
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }

        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "댓글 삭제 및 복원 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    //TODO 댓글은 생성, 삭제(복원) 만 되서, 수정하는 API는 필요하면 만들기
}

