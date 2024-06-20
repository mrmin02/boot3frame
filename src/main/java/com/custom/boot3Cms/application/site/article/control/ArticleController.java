
package com.custom.boot3Cms.application.site.article.control;


import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.system.file.service.FileMngService;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.bbs.service.BbsMngService;
import com.custom.boot3Cms.application.mng.bbs.vo.BbsMngVO;
import com.custom.boot3Cms.application.mng.code.service.CodeMngService;
import com.custom.boot3Cms.application.site.article.service.ArticleService;
import com.custom.boot3Cms.application.site.article.vo.ArticleVO;
import com.nhncorp.lucy.security.xss.XssFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 게시글 컨트롤러
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
@Tag(name = "게시글 컨트롤러")
public class ArticleController {

    // TODO XSS FILTER
    XssFilter xssFilter = XssFilter.getInstance("lucy-xss.xml", true);

    //게시글 관리 서비스
    @Resource(name = "articleService")
    ArticleService articleService;

    //게시판 관리 서비스
    @Resource(name = "bbsMngService")
    private BbsMngService bbsMngService;

    //게시판 관리 서비스
    @Resource(name = "codeService")
    private CodeMngService codeService;

    //첨부파일 관리 서비스
    @Resource(name = "fileMngService")
    FileMngService fileMngService;


    /**
     * 게시글 목록
     * @param bbs_cd
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글 리스트")
    @GetMapping("/api/article/{bbs_cd}/list")
    public ResultVO articleList(@PathVariable("bbs_cd") String bbs_cd,
                                @ModelAttribute("articleVO") ArticleVO articleVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal
    ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        String rMsg = "";
        int code = 200;

        try{
            articleVO.setBbs_cd(bbs_cd);
            BbsMngVO bbsMngVO = new BbsMngVO();
            bbsMngVO.setBbs_cd(bbs_cd);
            Map<String, Object> map = bbsMngService.getBbsDetail(bbsMngVO);
            bbsMngVO = (BbsMngVO) map.get("value");

            List<ArticleVO> list = null;
            Map<String, String> auth = new HashMap<>();
            auth = articleService.getAuth(articleVO, principal);

            //검색키워드 xss필터 추가 2023-06-20
            String searchKeyword = request.getParameter("searchKeyword");
            if (searchKeyword != null) {
                searchKeyword = xssFilter.doFilter(searchKeyword);
                articleVO.setSearchKeyword(CommonUtil.fn_searchXSS(searchKeyword));
            }

            if (auth.get("readAuth").equals("N")) {
                code = 403;
                rMsg = "해당 게시판 이용 권한이 없습니다.";
            } else {

                /**
                 * QNA 게시판
                 */
                if (bbsMngVO.getBbs_type().equals("BBS_008")) {
                    list = articleService.getArticleQnaList(articleVO);
                } else {
                    list = articleService.getArticleList(articleVO);
                    if (bbsMngVO.getNotice_use_yn().equals("Y")) {
                        resultVO.putResult("notice",articleService.getArticlenNoticeList(articleVO));
                    }
                }

                resultVO.putResult("list",list);
                resultVO.putResult("bbs",bbsMngVO);
                resultVO.putResult("auth",auth);

                code = 200;
                result = true;
            }

        }catch (Exception e){
            e.printStackTrace();
            code = 404;
            result = false;
            rMsg = "게시글 리스트 검색 도중 오류가 발생하였습니다.";
        }

        resultVO.setResultCode(code);
        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        return resultVO;
    }

    /**
     * 카테고리 리스트
     * @param bbs_cd
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "카테고리 리스트 API", description = "게시판의 카테고리 리스트를 반환합니다.")
    @GetMapping("/api/article/{bbs_cd}/category")
    public ResultVO getCategoryList(@PathVariable("bbs_cd") String bbs_cd, Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        String rMsg = "";
        int code = 200;

        try {
            Map<String, String> auth = new HashMap<>();
            ArticleVO articleVO = new ArticleVO();
            articleVO.setBbs_cd(bbs_cd);
            auth = articleService.getAuth(articleVO, principal);

            if(auth.get("readAuth").equals("N")) {
                code = 403;
                rMsg = "접근 권한이 없습니다.";
            }else{
                BbsMngVO bbsMngVO = new BbsMngVO();
                bbsMngVO.setBbs_cd(bbs_cd);
                Map<String, Object> map = bbsMngService.getBbsDetail(bbsMngVO);
                bbsMngVO = (BbsMngVO) map.get("value");

                resultVO.putResult("list",bbsMngService.getBbsCategory(bbsMngVO));
                result = true;
            }
        }catch (Exception e){
            e.printStackTrace();
            code = 404;
            rMsg = "카테고리 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultCode(code);
        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        return resultVO;
    }

    /**
     * 게시글 상세보기
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글 상세보기")
    @GetMapping("/api/article/{bbs_cd}/detail/{article_seq}")
    public ResultVO articleDetail( @ModelAttribute("articleVO") ArticleVO articleVO
            , @PathVariable("bbs_cd") String bbs_cd
            , @PathVariable("article_seq") String article_seq
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal
            , HttpSession session
    ) throws Exception {

        ResultVO resultVO = new ResultVO();
        boolean result = false;
        String rMsg = "";
        int code = 200;

        try{
            articleVO.setBbs_cd(bbs_cd);
            articleVO.setArticle_seq(article_seq);

            BbsMngVO bbsMngVO = new BbsMngVO();
            bbsMngVO.setBbs_cd(bbs_cd);
            Map<String, Object> bbsmap = bbsMngService.getBbsDetail(bbsMngVO);
            bbsMngVO = (BbsMngVO) bbsmap.get("value");

            Map<String, Object> map = null;
            map = articleService.getArticle(articleVO, principal);
            if ((boolean) map.get("result")) {

                ArticleVO vo = (ArticleVO) map.get("value");

                Map<String, String> authMap = articleService.getAuth(articleVO, principal);

                if (articleVO.getArticle_seq() != null && principal != null) { // 글 조회시 글 등록자와 현재 로그인자의 seq가 같다면 조회권한 줌
                    LoginVO loginVO = CommonUtil.fn_getUserAuth(principal);
                    if (loginVO.getUser_seq().equals(vo.getInpt_seq())) {
                        authMap.put("readAuth", "Y");
                    }
                }

                /**
                 * 조회 권한 체크
                 */
                if (authMap.get("readAuth").equals("N")) {
                    code = 403;
                    rMsg = "권한이 없습니다.";
                }else{

                    boolean board_status = true;

                    /**
                     * Q&A 게시판
                     */
                    if (bbsMngVO.getBbs_type().equals("BBS_008")) {

                        // TODO 비밀번호 체크 로직 추가

                        if (principal != null) {
                            if (vo.getSecret_yn().equals("Y") && !authMap.get("user_seq").equals(vo.getInpt_seq()) && !authMap.get("user_auth").equals("ROLE_ADMIN")) {
                                code = 403;
                                rMsg = "비밀글 조회권한이 없습니다.";
                                board_status = false;
                            }
                        } else if (vo.getSecret_yn().equals("Y") && authMap.get("user_auth").equals("ROLE_ANON")) {
                            if (articleVO.getPwd_check_yn() == null || !articleVO.getPwd_check_yn().equals("Y")) {
                                code = 403;
                                rMsg = "비밀글 조회권한이 없습니다.";
                                board_status = false;
                            }
                        }
                        if(board_status){
                            //답변 가져오기
                            resultVO.putResult("reply", articleService.getReply(articleVO));
                        }
                    }

                    /**
                     * Q&A 게시판이 아닐경우에는 항상 True
                     */
                    if(board_status){
                        if(bbsMngVO.getComment_use_yn().equals("Y")){
                            // 댓글 가져오기
                            resultVO.putResult("comment",articleService.getCommentList(articleVO));
                        }
                        resultVO.putResult("article",map.get("value"));
                        resultVO.putResult("bbs", bbsMngVO);
                        resultVO.putResult("auth", articleService.getAuth(articleVO));
                        code = 200;
                        result = true;
                    }
                }
            } else {
                code = 404;
                rMsg = "게시글 정보가 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            code = 404;
            rMsg = "게시글 조회에 실패하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.setResultCode(code);
        resultVO.putResult("result",result);
        return resultVO;
    }

    /**
     * 게시글 및 답변 등록
     *
     * FIXME 현재 프레임워크 기능 및 구조 특성상 댓글 및 답변을 개별 API 로 가져올 시,
     * 트래픽 증가 및 권한체크 등등의 비용 증가로 별도 API 로 제공하지 않고, 게시글 상세정보에 추가
     *
     * @param articleVO
     * @param bbs_cd
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글 및 답변 등록")
    @PostMapping(value={"/api/article/{bbs_cd}/proc"}
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultVO setArticle(@ModelAttribute ArticleVO articleVO,
                                @PathVariable("bbs_cd") String bbs_cd,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                Principal principal ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        String rMsg = "";
        int code = 200;

        try{
            articleVO.setBbs_cd(bbs_cd);
            Map<String, String> authMap = articleService.getAuth(articleVO, principal);
            if(authMap.get("writeAuth").equals("Y")){
                // TODO XSS FILTER
                if (articleVO.getContent() != null) {
                    articleVO.setContent(xssFilter.doFilter(articleVO.getContent().replaceAll("\r\n", "<br>")));
                }
                if (articleVO.getSubject() != null) {
                    articleVO.setSubject(xssFilter.doFilter(articleVO.getSubject().replaceAll("\r\n", "<br>")));
                }

                articleVO.setInpt_ip(StringUtil.getIP(request));
                Map<String, Object> map = articleService.setArticle(articleVO, principal);

                if ((boolean) map.get("result")) {
                    code = 200;
                    result = true;
                }else{
                    code = 404;
                    rMsg = String.valueOf(map.get("rMsg").toString());
                }
            } else {
              code = 403;
              rMsg = "권한이 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            code = 404;
            rMsg = "게시글 등록 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.setResultCode(code);
        resultVO.putResult("result",result);
        return resultVO;
    }

    /**
     * 게시글 및 답변 수정
     * @param articleVO
     * @param bbs_cd
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글 및 답변 수정")
    @PatchMapping(value={"/api/article/{bbs_cd}/proc"}
            , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultVO updateArticle(@ModelAttribute ArticleVO articleVO,
                               @PathVariable("bbs_cd") String bbs_cd,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Principal principal ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        String rMsg = "";
        int code = 200;

        try{
            articleVO.setBbs_cd(bbs_cd);
            Map<String, String> authMap = articleService.getAuth(articleVO, principal);
            if(authMap.get("updateAuth").equals("Y")){
                // TODO XSS FILTER
                if (articleVO.getContent() != null) {
                    articleVO.setContent(xssFilter.doFilter(articleVO.getContent().replaceAll("\r\n", "<br>")));
                }
                if (articleVO.getSubject() != null) {
                    articleVO.setSubject(xssFilter.doFilter(articleVO.getSubject().replaceAll("\r\n", "<br>")));
                }

                articleVO.setInpt_ip(StringUtil.getIP(request));
                Map<String, Object> map = articleService.updateArticle(articleVO, principal);

                if ((boolean) map.get("result")) {
                    code = 200;
                    result = true;
                }else{
                    code = 404;
                    rMsg = String.valueOf(map.get("rMsg").toString());
                }
            } else {
                code = 403;
                rMsg = "권한이 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            code = 404;
            rMsg = "게시글 수정 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.setResultCode(code);
        resultVO.putResult("result",result);
        return resultVO;
    }

    /**
     * 게시글 및 답변 삭제
     * @param articleVO
     * @param bbs_cd
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글 및 답변 삭제")
    @DeleteMapping("/api/article/{bbs_cd}/proc")
    public ResultVO deleteArticle(@RequestBody ArticleVO articleVO,
                                  @PathVariable("bbs_cd") String bbs_cd,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  Principal principal ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        String rMsg = "";
        int code = 200;

        try{
            articleVO.setBbs_cd(bbs_cd);
            Map<String, String> authMap = articleService.getAuth(articleVO, principal);
            if(authMap.get("deleteAuth").equals("Y")){
                // TODO XSS FILTER
                if (articleVO.getContent() != null) {
                    articleVO.setContent(xssFilter.doFilter(articleVO.getContent().replaceAll("\r\n", "<br>")));
                }
                if (articleVO.getSubject() != null) {
                    articleVO.setSubject(xssFilter.doFilter(articleVO.getSubject().replaceAll("\r\n", "<br>")));
                }

                articleVO.setInpt_ip(StringUtil.getIP(request));
                Map<String, Object> map = articleService.deleteArticle(articleVO, principal);

                if ((boolean) map.get("result")) {
                    code = 200;
                    result = true;
                }else{
                    code = 404;
                    rMsg = String.valueOf(map.get("rMsg").toString());
                }
            } else {
                code = 403;
                rMsg = "권한이 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            code = 404;
            rMsg = "게시글 삭제 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.setResultCode(code);
        resultVO.putResult("result",result);
        return resultVO;
    }

    /**
     * 댓글 등록
     * @param articleVO
     * @param bbs_cd
     * @param article_seq
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글 댓글 등록")
    @PostMapping("/article/{bbs_cd}/{article_seq}/comment")
    public ResultVO insertComment(@ModelAttribute("articleVO") ArticleVO articleVO
            , @PathVariable("bbs_cd") String bbs_cd
            , @PathVariable("article_seq") String article_seq
            , Principal principal ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        String rMsg = "";
        int code = 200;

        if (principal != null) {
            LoginVO loginVO = CommonUtil.fn_getUserAuth(principal);
            articleVO.setInpt_user_name(loginVO.getUser_name());
            articleVO.setInpt_seq(loginVO.getUser_seq());

        }
        articleVO.setContent(xssFilter.doFilter(articleVO.getContent()));
        articleVO.setBbs_cd(bbs_cd);
        articleVO.setArticle_seq(article_seq);

        Map<String, Object> map = articleService.setComment(articleVO);

        rMsg = String.valueOf(map.get("rMsg").toString());
        if((boolean) map.get("result")){
            code = 200;
            result = true;
        }else{
            code = 404;
        }

        resultVO.setResultMsg(rMsg);
        resultVO.setResultCode(code);
        resultVO.putResult("result",result);
        return resultVO;
    }

    /**
     * 댓글 등록
     * @param articleVO
     * @param bbs_cd
     * @param article_seq
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글 댓글 수정")
    @PatchMapping("/api/article/{bbs_cd}/{article_seq}/comment")
    public ResultVO updateComment(@ModelAttribute("articleVO") ArticleVO articleVO
            , @PathVariable("bbs_cd") String bbs_cd
            , @PathVariable("article_seq") String article_seq
            , Principal principal ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        String rMsg = "";
        int code = 200;

        if (principal != null) {
            LoginVO loginVO = CommonUtil.fn_getUserAuth(principal);
            articleVO.setInpt_user_name(loginVO.getUser_name());
            articleVO.setInpt_seq(loginVO.getUser_seq());
        }
        articleVO.setContent(xssFilter.doFilter(articleVO.getContent()));
        articleVO.setBbs_cd(bbs_cd);
        articleVO.setArticle_seq(article_seq);

        Map<String, Object> map = articleService.updateComment(articleVO, principal);

        rMsg = String.valueOf(map.get("rMsg").toString());
        if((boolean) map.get("result")){
            code = 200;
            result = true;
        }else{
            code = 404;
        }

        resultVO.setResultMsg(rMsg);
        resultVO.setResultCode(code);
        resultVO.putResult("result",result);
        return resultVO;
    }

    /**
     * 댓글 삭제
     * @param articleVO
     * @param bbs_cd
     * @param article_seq
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "게시글 댓글 삭제")
    @DeleteMapping("/api/article/{bbs_cd}/{article_seq}/comment")
    public ResultVO deleteComment(@ModelAttribute("articleVO") ArticleVO articleVO
            , @PathVariable("bbs_cd") String bbs_cd
            , @PathVariable("article_seq") String article_seq
            , Principal principal ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        String rMsg = "";
        int code = 200;

        if (principal != null) {
            LoginVO loginVO = CommonUtil.fn_getUserAuth(principal);
            articleVO.setInpt_user_name(loginVO.getUser_name());
            articleVO.setInpt_seq(loginVO.getUser_seq());
        }
        articleVO.setContent(xssFilter.doFilter(articleVO.getContent()));
        articleVO.setBbs_cd(bbs_cd);
        articleVO.setArticle_seq(article_seq);

        Map<String, Object> map = articleService.updateComment(articleVO, principal);

        rMsg = String.valueOf(map.get("rMsg").toString());
        if((boolean) map.get("result")){
            code = 200;
            result = true;
        }else{
            code = 404;
        }

        resultVO.setResultMsg(rMsg);
        resultVO.setResultCode(code);
        resultVO.putResult("result",result);
        return resultVO;
    }

    /**
     * Q&A 비밀번호 체크
     *
     * @param articleVO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "비밀번호 체크")
    @PostMapping(value = "/api/article/{bbs_cd}/checkpwd")
    public ResultVO getAjaxUserCheck(@ModelAttribute("articleVO") ArticleVO articleVO,
                                    @PathVariable("bbs_cd") String bbs_cd,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    RedirectAttributes redirectAttributes) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        String rMsg = "";
        int code = 200;

        Map<String, Object> map = articleService.checkPwd(articleVO);
        result = (boolean) map.get("result");
        if(!result){
//            rMsg = map.get("rMsg").toString();
            rMsg = "비밀번호가 올바르지 않습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.setResultCode(code);
        resultVO.putResult("result",result);
        return resultVO;
    }

}

