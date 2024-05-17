package com.custom.boot3Cms.application.site.article.service;

import com.custom.boot3Cms.application.common.system.file.mapper.FileMngMapper;
import com.custom.boot3Cms.application.common.system.file.service.FileMngService;
import com.custom.boot3Cms.application.common.system.file.vo.FileMngVO;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.bbs.mapper.BbsMngMapper;
import com.custom.boot3Cms.application.mng.bbs.vo.BbsMngVO;
import com.custom.boot3Cms.application.site.article.mapper.ArticleMapper;
import com.custom.boot3Cms.application.site.article.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 게시글 관리 서비스
 *
 * @author SEKOREA
 * @version 1.1
 * @see <pre>
 *  Modification Information
 *
 * 	수정일       / 수정자    / 수정내용
 * 	------------------------------------------
 * 	2017-11-10 / 김기식	  / 최초 생성
 * 	2018-03-23 / 전성진	  / 프레임워크 구조 변경
 * </pre>
 * @since 2017-11-10
 */
@Service("articleService")
public class ArticleService {

    @Resource(name="articleMapper")
    ArticleMapper articleMapper;

    @Resource(name="bbsMngMapper")
    BbsMngMapper bbsMngMapper;

    @Resource(name="fileMngMapper")
    FileMngMapper fileMngMapper;

    @Resource(name="fileMngService")
    FileMngService fileMngService;

    @Value("${Globals.file.DefaultPath}")
    private String FILE_PATH;
    @Value("${Globals.file.ArticleFilePath}")
    private String ARTICLE_PATH;

    /**
     * 메인 게시글 목록
     * @param vo
     * @return
     * @throws Exception
     */
    public List<ArticleVO> getMainArticleList(ArticleVO vo) throws Exception{
        List<ArticleVO> rtnList = new ArrayList<>();
        BbsMngVO bbsMngVO = bbsMngMapper.getBbsDetail(vo);
        // NULL 체크를 하여 없는 게시판을 호출하더라도 에러로 나타나지 않도록 조치
        if(bbsMngVO != null){
            // COPY bbsMngVO to vo
            bbsMngVO.setPageIndex(vo.getPageIndex());
            CommonUtil.fn_copyClass(vo, bbsMngVO);
            rtnList = articleMapper.getMainArticleList(vo);
        }
        return rtnList;
    }

    /**
     * 게시글 목록
     * @param vo
     * @return
     * @throws Exception
     */
    public List<ArticleVO> getArticleList(ArticleVO vo) throws Exception{
        BbsMngVO bbsMngVO = bbsMngMapper.getBbsDetail(vo);
        // COPY bbsMngVO to vo
        bbsMngVO.setPageIndex(vo.getPageIndex());
        CommonUtil.fn_copyClass(vo, bbsMngVO);
        List<ArticleVO> rtnList = articleMapper.getArticleList(vo);

        vo.setTotalCount(articleMapper.getArticleCnt(vo));
        return rtnList;
    }


    /**
     * 게시글 목록 getArticleQnaList
     * @param vo
     * @return
     * @throws Exception
     */
    public List<ArticleVO> getArticleQnaList(ArticleVO vo) throws Exception{
        BbsMngVO bbsMngVO = bbsMngMapper.getBbsDetail(vo);
        // COPY bbsMngVO to vo
        bbsMngVO.setPageIndex(vo.getPageIndex());
        CommonUtil.fn_copyClass(vo, bbsMngVO);
        List<ArticleVO> rtnList = articleMapper.getArticleQnaList(vo);

        vo.setTotalCount(articleMapper.getArticleQnaCnt(vo));
        return rtnList;
    }

    /**
     * 게시글 공지사항 목록
     * @param vo
     * @return
     * @throws Exception
     */
    public List<ArticleVO> getArticlenNoticeList(ArticleVO vo) throws Exception{
        return articleMapper.getArticlenNoticeList(vo);
    }

    /**
     * 게시글 상세보기
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getArticle(ArticleVO vo, Principal principal) throws Exception{
        Map rtnMap = new HashMap();
        BbsMngVO bbsMngVO = bbsMngMapper.getBbsDetail(vo);
        // COPY bbsMngVO to vo
        if(StringUtil.isNotEmpty(bbsMngVO.getBbs_cd())){
            int pageIndex = vo.getPageIndex();
            CommonUtil.fn_copyClassByFilterBeanName(vo, bbsMngVO, "inpt_user_name", "upd_user_name", "inpt_seq", "upd_seq", "user_id");

            /**
             * 비밀번호 검증을 위함
             */
            ArticleVO tmpVO = new ArticleVO();
            tmpVO.setArticle_seq(vo.getArticle_seq());
            tmpVO.setNotEncUser_pwd(vo.getUser_pwd());

            String searchCondition = vo.getSearchCondition();
            String searchKeyword = vo.getSearchKeyword();
            if(StringUtil.isNotEmpty(vo.getArticle_seq())){
                vo = articleMapper.getArticle(vo);
                if(vo != null){
                    /**
                     * 비밀글일 때, 관리자가 아니면 비밀번호 체크
                     */
                    if(vo.getSecret_yn().equals("Y")){
                        LoginVO loginVO = CommonUtil.fn_getUserAuth(principal);
                        boolean result = false;
                        // 권한체크
                        /**
                         *  작성자 검증 및 관리자 권한 검증
                         */
                        /**
                         * 비회원 글은 inpt_seq 가 null
                         */
                        if(vo.getInpt_seq() != null){ // 회원 작성글
                            if(loginVO.getUser_auth().equals("ROLE_ADMIN") || vo.getInpt_seq().equals(loginVO.getUser_seq())){
                                result = true;
                            }
                        }else{ // 비회원 작성글
                            if(loginVO.getUser_auth().equals("ROLE_ADMIN")){
                                result = true;
                            }else if(articleMapper.checkPwd(tmpVO) > 0){
                                // 관리자가 아닐때는 패스워드 체크
                                result = true;
                            }
                        }

                        if(!result){
                            return CommonUtil.fn_getDetail(null);
                        }
                    }
                    articleMapper.updArticleReadCNT(vo);

                    if(vo.getContent() != null){
                        vo.setContent(vo.getContent().replaceAll("<br>","\r\n"));
                    }
                    vo.setBbs_cd(bbsMngVO.getBbs_cd());
                    vo.setBbs_type(bbsMngVO.getBbs_type());
                    vo.setSearchCondition(searchCondition);
                    vo.setSearchKeyword(searchKeyword);
                    vo.setPageIndex(pageIndex);

                    rtnMap = CommonUtil.fn_getDetail(vo);

                    // 첨부파일 유무 확인 후 SELECT
                    if ("Y".equals(vo.getAttach_file_use_yn())) {
                        rtnMap.put("fileList", fileMngMapper.getFileList(new FileMngVO("TB_ARTICLES", vo.getArticle_seq(), "", null)));
                    }
                }else{
                    rtnMap = CommonUtil.fn_getDetail(null);
                }
                return rtnMap;
            }else{
                return CommonUtil.fn_getDetail(vo);
            }
        }else{
            return CommonUtil.fn_getDetail(null);
        }
    }

    /**
     * 게시글 답변 상세보기
     * @param vo
     * @return
     * @throws Exception
     */
    public ArticleVO getReply(ArticleVO vo) throws Exception{
        ArticleVO replyVo = new ArticleVO();
        if(StringUtil.isNotEmpty(vo.getArticle_seq())){
            replyVo = articleMapper.getReply(vo);
        }
        return replyVo;
    }

    /**
     * 게시글 댓글 목록
     * @param vo
     * @return
     * @throws Exception
     */
    public List<ArticleVO> getCommentList(ArticleVO vo) throws Exception{
        List<ArticleVO> list = articleMapper.getCommentList(vo);

        /**
         * 이름 * 처리
         */
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getUser_auth() == null) {

                String inpt_user_name = list.get(i).getInpt_user_name();
                if(list.get(i).getInpt_user_name()!=null) {
                    int name_size = list.get(i).getInpt_user_name().length();

                    String user_name = inpt_user_name.substring(0, 1);
                    if (name_size == 2) {
                        user_name += "*";
                    } else {
                        for (int j = 0; j < name_size - 2; j++) {
                            user_name += "*";
                        }
                        user_name += inpt_user_name.substring(name_size - 1, name_size);
                    }

                    list.get(i).setInpt_user_name(user_name);
                }
            }
        }
        return list;
    }

    /**
     * 게시글 등록
     * @param vo
     * @return
     * @throws Exception
     */
    @Transactional
    public Map<String, Object> setArticle(ArticleVO vo, Principal principal) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String rMsg = "";
        String rHeader = "";
        boolean result = false;
        // 게시판 설정정보 GET
        BbsMngVO bbsMngVO = bbsMngMapper.getBbsDetail(vo);
        // 동일한 변수가 존재하므로 초기화 방지..
        CommonUtil.fn_copyClass(vo, bbsMngVO);

        boolean check_user = true;

        try{
            LoginVO loginVO = CommonUtil.fn_getUserAuth(principal);
            if(principal != null){
                vo.setInpt_seq(loginVO.getUser_seq());
                vo.setInpt_user_name(loginVO.getUser_name());
            }else{
                /**
                 * 비회원 작성
                 */
                if(StringUtil.isNotEmpty(vo.getUser_id()) && StringUtil.isNotEmpty(vo.getUser_pwd())){
                    vo.setUser_id("anon");
                    vo.setInpt_user_name(vo.getUser_id());
                    vo.setInpt_seq(null);
                }else{
                    rMsg = "비회원 글 작성 시, 이름과 비밀번호가 필요합니다.";
                    check_user = false;
                }
            }

            /**
             * 공지사항 지정은 관리자만 가능
             */
            if(StringUtil.isNotEmpty(vo.getNotice_yn())){
                if(!loginVO.getUser_auth().equals("ROLE_ADMIN")){
                    vo.setNotice_yn("N");
                }
            }

            /**
             * 이용자 상태 확인
             * 비회원인데, 필수값 없는 경우를 위한 조건절
             */
            if(check_user){
                if(StringUtil.isEmpty(vo.getSecret_yn())){
                    vo.setSecret_yn("N");
                }
                if(StringUtil.isEmpty(vo.getNotice_yn())){
                    vo.setNotice_yn("N");
                }

                // 설정정보가 존재 할 경우
                if(StringUtil.isNotEmpty(vo.getBbs_info_seq())){

                    /**
                     * Q&A 의 답글도 게시글로 취급하기 때문에 답글인지 yn 값을 확인하여 prt 값 셋팅
                     */
                    if("Y".equals(vo.getReply_yn())){
                        // 답글 순번 PROC
                        String bbs_order = articleMapper.getArticleOrder(vo);
                        if (StringUtil.isNotEmpty(bbs_order)) {
                            vo.setBbs_order(CommonUtil.fn_updBbsOrder(bbs_order, "R"));
                        } else {
                            vo.setBbs_order(CommonUtil.fn_updBbsOrder(vo.getBbs_order(), "A"));
                        }
                    }
                    result = articleMapper.setArticle(vo) > 0;

                    if (result) {
                        if(!"Y".equals(vo.getReply_yn())){ //답글이 아닐 경우 prt_grp에 본인 seq를 넣어준다.
                            result = articleMapper.setArticlePrtGrp(vo) > 0;
                        }
                    }
                    // 게시판 등록에 성공 할 경우
                    if(result){
                        // 게시판 첨부파일 사용이 활성화 되어 있을 경우
                        if("Y".equals(vo.getAttach_file_use_yn())){
                            // 파일 확장자 검증
                            ArticleVO fileParam = vo;
                            List<String> fileExtList = new ArrayList(){{
                                bbsMngMapper.getBbsFileInfo(fileParam).forEach(v-> add(v.getFile_type()));
                            }};
                            result = CommonUtil.fn_checkFileExt(vo, fileExtList.toArray());
                            // 파일 확장자 검증에 성공했을 경우
                            if(result){
                                List<FileMngVO> fileList = CommonUtil.fn_getFileInfo(vo, "TB_ARTICLES", vo.getArticle_seq(), FILE_PATH, ARTICLE_PATH+vo.getBbs_cd()+"/", vo.getInpt_seq(), "");
                                // 파일이 존재 할 경우
                                if(fileList.size() > 0){
                                    boolean fileUplaodResult = false;
                                    for(FileMngVO fileMngVO : fileList){
                                        fileUplaodResult = fileMngVO.isFileResult();
                                        if(fileUplaodResult){
                                            result = fileMngService.setFile(fileMngVO);
                                            if(!result){
                                                break;
                                            }
                                        }else{
                                            break;
                                        }
                                    }
                                    if(fileUplaodResult){
                                        // 파일 업로드 및 데이터베이스 등록에 완전히 성공했을 경우
                                        if("Y".equals(vo.getReply_yn())){
                                            rMsg = "답변 등록이 완료되었습니다.";
                                        }else{
                                            rMsg = "게시글 등록이 완료되었습니다.";
                                        }

                                    }else{
                                        // 게시글 물리삭제
                                        result = articleMapper.delArticleReal(vo) > 0;
                                        vo.setArticle_seq("");
                                        if(result){
                                            rMsg = "게시글 등록에 실패하였습니다. (첨부파일 업로드중 에러가 발생했습니다. 개발사에 문의 바랍니다.)";
                                            // 이 이후의 처리는 에러로 간주함.
                                        }else{
                                            rMsg = "게시글 등록에 실패하였습니다. (더미 데이터 삭제에 실패하였습니다.)";
                                            // 이 이후의 처리는 에러로 간주함.
                                        }
                                        result = false;
                                    }
                                }else{
                                    if("Y".equals(vo.getReply_yn())){
                                        rMsg = "답변 등록이 완료되었습니다.";
                                    }else{
                                        rMsg = "게시글 등록이 완료되었습니다.";
                                    }
                                }
                            }else{
                                // 게시글 물리삭제
                                result = articleMapper.delArticleReal(vo) > 0;
                                vo.setArticle_seq("");
                                if(result){
                                    rMsg = "게시글 등록에 실패하였습니다. (첨부파일 중 허용되지 않는 확장자가 포함되어 있습니다.)";
                                }else{
                                    rMsg = "게시글 등록에 실패하였습니다. (더미 데이터 삭제에 실패하였습니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                }
                                result = false;
                            }
                        }else{
                            if("Y".equals(vo.getReply_yn())){
                                rMsg = "답변 등록이 완료되었습니다.";
                            }else{
                                rMsg = "게시글 등록이 완료되었습니다.";
                            }
                        }
                    }else{
                        rMsg = "게시글 등록에 실패하였습니다. 게시글 정보 등록 중 오류가 발생했습니다.";
                    }
                }else{
                    rMsg = "올바르지 않은 접근입니다.";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            rMsg = "등록에 실패하였습니다.";
        }

        map.put("rMsg", rMsg);
        map.put("result", result);
        return map;
    }

    /**
     * 게시판 권한
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getArticleBbsAuth(ArticleVO vo) throws Exception{
        return articleMapper.getArticleBbsAuth(vo);
    }

    /**
     * 게시글 및 답글 수정
     * @param vo
     * @param principal
     * @return
     * @throws Exception
     */
    @Transactional
    public Map<String, Object> updateArticle(ArticleVO vo, Principal principal) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String rMsg = "";
        boolean result = false;

        try{

            // 게시판 설정정보 GET
            BbsMngVO bbsMngVO = bbsMngMapper.getBbsDetail(vo);
            // 동일한 변수가 존재하므로 초기화 방지..
            CommonUtil.fn_copyClass(vo, bbsMngVO);
            LoginVO loginVO = new LoginVO();
            if(principal != null){
                loginVO = CommonUtil.fn_getUserAuth(principal);
                vo.setInpt_seq(loginVO.getUser_seq());
                vo.setUpd_seq(loginVO.getUser_seq());
                vo.setUpd_user_name(loginVO.getUser_name());
                vo.setUser_auth(loginVO.getUser_auth());
            }else{
                loginVO.setUser_auth("ROLE_ANON");
                vo.setUser_auth("ROLE_ANON");
                vo.setUpd_user_name("anon");
            }
            if(StringUtil.isEmpty(vo.getSecret_yn())){
                vo.setSecret_yn("N");
            }

            /**
             * 공지사항 지정은 관리자만 가능
             */
            if(StringUtil.isNotEmpty(vo.getNotice_yn())){
                if(!loginVO.getUser_auth().equals("ROLE_ADMIN")){
                    vo.setNotice_yn("N");
                }
            }

            // 설정정보가 존재 할 경우
            if(StringUtil.isNotEmpty(vo.getBbs_info_seq()) && StringUtil.isNotEmpty(vo.getArticle_seq())){
                ArticleVO tempArticleVO = articleMapper.getArticle(vo);
                Map<String, Object> authMap = articleMapper.getArticleBbsAuth(vo);

                if(tempArticleVO != null){
                    if(authMap.get("update_yn").equals("Y")) { // FIXME 게시판에 대한 권한검사는 controller 에서 이미 체크함.
                        /**
                         *  작성자 검증 및 관리자 권한 검증
                         */
                        if(loginVO.getUser_auth().equals("ROLE_ANON") && (bbsMngVO.getBbs_type().equals("BBS_008") || bbsMngVO.getBbs_type().equals("BBS_009"))){
                            result = true;
                        }else{
                            /**
                             * 비회원 글은 inpt_seq 가 null
                             */
                            if(tempArticleVO.getInpt_seq() != null){
                                if(loginVO.getUser_auth().equals("ROLE_ADMIN") || vo.getInpt_seq().equals(tempArticleVO.getInpt_seq())){
                                    result = true;
                                }
                            }else{
                                if(loginVO.getUser_auth().equals("ROLE_ADMIN")){
                                    result = true;
                                }else if(articleMapper.checkPwd(vo) > 0 ){
                                    // 관리자가 아닌 비회원 글을 수정하려할 때
                                    result = true;
                                    if(loginVO.getUser_seq() != null){
                                        vo.setUpd_seq(loginVO.getUser_seq());
                                    }
                                }
                            }
                        }
                    }else{
                        rMsg = "게시글 수정 권한이 없습니다.";
                    }
                }else{
                    rMsg = "존재하지 않는 게시글입니다.";
                }

                if(result){
                    result = articleMapper.updArticle(vo) > 0;
                    // 게시판 수정에 성공 할 경우
                    if(result){
                        // 게시판 첨부파일 사용이 활성화 되어 있을 경우
                        if("Y".equals(vo.getAttach_file_use_yn())){
                            // 파일 확장자 검증
                            ArticleVO fileParam = vo;
                            List<String> fileExtList = new ArrayList(){{
                                bbsMngMapper.getBbsFileInfo(fileParam).forEach(v-> add(v.getFile_type()));
                            }};
                            result = CommonUtil.fn_checkFileExt(vo, fileExtList.toArray());
                            // 파일 확장자 검증에 성공했을 경우
                            if(result){
                                List<FileMngVO> tempFileList = fileMngService.getFileList(new FileMngVO("TB_ARTICLES", vo.getArticle_seq(), ""));
                                List<FileMngVO> fileList = CommonUtil.fn_getFileInfo(vo, "TB_ARTICLES", vo.getArticle_seq(), FILE_PATH, ARTICLE_PATH+vo.getBbs_cd()+"/", vo.getInpt_seq(), "");
                                // 파일이 존재 할 경우
                                if(fileList.size() > 0){
                                    boolean fileUplaodResult = false;
                                    for(FileMngVO fileMngVO : fileList){
                                        fileUplaodResult = fileMngVO.isFileResult();
                                        if(fileUplaodResult){
                                            result = fileMngService.setFile(fileMngVO);
                                            if(!result){
                                                break;
                                            }
                                        }else{
                                            break;
                                        }
                                    }
                                    if(fileUplaodResult){
                                        // 파일 업로드 및 데이터베이스 등록에 완전히 성공했을 경우
                                        if(StringUtil.isNotEmpty(vo.getBbs_order())){
                                            rMsg = "답변 수정이 완료되었습니다.";
                                        }else{
                                            rMsg = "게시글 수정이 완료되었습니다.";
                                        }
                                    }else{
                                        // 게시글 수정
                                        result = articleMapper.updArticle(tempArticleVO) > 0;
                                        if(result){
                                            rMsg = "게시글 수정에 실패하였습니다.(첨부파일 업로드중 에러가 발생했습니다. 개발사에 문의 바랍니다.)";
                                            // 이 이후의 처리는 에러로 간주함.
                                        }else{
                                            rMsg = "게시글 수정에 실패하였습니다.(이전 데이터 복원에 실패하였습니다.)";
                                            // 이 이후의 처리는 에러로 간주함.
                                        }
                                        result = false;
                                    }
                                }
                                if(StringUtil.isNotEmptyArray(vo.getDel_file_seq()) && result){
                                    for(String del_file_seq : vo.getDel_file_seq()){
                                        result = fileMngService.delFile(new FileMngVO("TB_ARTICLES", vo.getArticle_seq(), del_file_seq));
                                        if(!result){
                                            break;
                                        }
                                    }if(!result){
                                        result = articleMapper.updArticle(tempArticleVO) > 0;
                                        if(result){
                                            result = fileMngService.delFiles(new FileMngVO("TB_ARTICLES", vo.getArticle_seq(), ""));
                                            if(result){
                                                for(FileMngVO fileMngVO : tempFileList){
                                                    result = fileMngService.setFile(fileMngVO);
                                                    if(!result){
                                                        break;
                                                    }
                                                }
                                                if(result){
                                                    rMsg = "게시글 수정에 실패하였습니다.(이전 파일정보 삭제에 실패하였습니다.)";
                                                    // 이 이후의 처리는 에러로 간주함.
                                                }else{
                                                    rMsg = " 게시글 수정에 실패하였습니다.(이전 파일정보 복원에 실패하였습니다.)";
                                                    // 이 이후의 처리는 에러로 간주함.
                                                }
                                            }else{
                                                rMsg = "게시글 수정에 실패하였습니다.(업로드 파일정보 삭제에 실패하였습니다.)";
                                                // 이 이후의 처리는 에러로 간주함.
                                            }
                                        }else{
                                            rMsg = "게시글 수정에 실패하였습니다.(이전 데이터 복원에 실패하였습니다.)";
                                            // 이 이후의 처리는 에러로 간주함.
                                        }
                                        result = false;
                                    }
                                }
                                if(result){
                                    if(StringUtil.isNotEmpty(vo.getBbs_order())){
                                        rMsg = "답변 수정이 완료되었습니다.";
                                    }else{
                                        rMsg = "게시글 수정이 완료되었습니다.";
                                    }
                                }
                            }else{
                                // 게시글 수정
                                result = articleMapper.updArticle(vo) > 0;
                                if(result){
                                    rMsg = "게시글 수정에 실패하였습니다.(첨부파일 중 허용되지 않는 확장자가 포함되어 있습니다.)";
                                }else{
                                    rMsg = "게시글 수정에 실패하였습니다.(원본 데이터 복원에 실패하였습니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                }
                                result = false;
                            }
                        }else{
                            if(StringUtil.isNotEmpty(vo.getBbs_order())){
                                rMsg = "답변 수정이 완료되었습니다.";
                            }else{
                                rMsg = "게시글 수정이 완료되었습니다.";
                            }
                        }
                    }else{
                        rMsg = "게시글 수정에 실패하였습니다.게시글 정보 수정 중 오류가 발생했습니다.";
                    }

                }else{
                    rMsg = rMsg.equals("") ?  "권한이 없습니다." : rMsg;
                }
            }else{
                rMsg = "필수 정보가 누락되었습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            rMsg = "수정에 실패하였습니다.";
        }

        map.put("rMsg", rMsg);
        map.put("result", result);
        return map;
    }

    /**
     * 게시글 및 답글 삭제
     * @param vo
     * @param principal
     * @return
     * @throws Exception
     */
    public Map<String, Object> deleteArticle(ArticleVO vo, Principal principal) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String rMsg = "";
        boolean result = false;

        try{
            // 게시판 설정정보 GET
            BbsMngVO bbsMngVO = bbsMngMapper.getBbsDetail(vo);

            // 동일한 변수가 존재하므로 초기화 방지..
            CommonUtil.fn_copyClass(vo, bbsMngVO);
            LoginVO loginVO = new LoginVO();
            if(principal != null){
                loginVO = CommonUtil.fn_getUserAuth(principal);
                vo.setInpt_seq(loginVO.getUser_seq());
                vo.setUpd_seq(loginVO.getUser_seq());
                vo.setUpd_user_name(loginVO.getUser_name());
                vo.setUser_auth(loginVO.getUser_auth());
            }else{
                loginVO.setUser_auth("ROLE_ANON");
                vo.setUser_auth("ROLE_ANON");
                vo.setUpd_user_name("anon");
            }
            if(StringUtil.isEmpty(vo.getSecret_yn())){
                vo.setSecret_yn("N");
            }

            ArticleVO tempArticleVO = articleMapper.getArticle(vo);
            Map<String, Object> authMap = articleMapper.getArticleBbsAuth(vo);

            if(tempArticleVO != null){
                /**
                 * 작성자 검증 및 관리자 권한 검증
                 */
                if(authMap.get("update_yn").equals("Y")) {
                    if (loginVO.getUser_auth().equals("ROLE_ANON") && (bbsMngVO.getBbs_type().equals("BBS_008") || bbsMngVO.getBbs_type().equals("BBS_009"))) {
                        result = true;
                    } else {
                        /**
                         * 비회원 글은 inpt_seq 가 null
                         */
                        if (tempArticleVO.getInpt_seq() != null) {
                            if (loginVO.getUser_auth().equals("ROLE_ADMIN") || vo.getInpt_seq().equals(tempArticleVO.getInpt_seq())) {
                                result = true;
                            }
                        } else {
                            if (loginVO.getUser_auth().equals("ROLE_ADMIN")) {
                                result = true;
                            } else if (articleMapper.checkPwd(vo) > 0) {
                                // 관리자가 아닌 비회원 글을 수정하려할 때
                                result = true;
                                if (loginVO.getUser_seq() != null) {
                                    vo.setUpd_seq(loginVO.getUser_seq());
                                }
                            }
                        }
                    }
                }else{
                    rMsg = "게시글 삭제 권한이 없습니다.";
                }
            }else{
                rMsg = "존재하지 않는 게시글입니다.";
            }

            if(result){

                result = articleMapper.delArticle(vo) > 0;
                if(StringUtil.isNotEmpty(vo.getBbs_order())){
                    rMsg = result ? "답변 삭제에 성공하였습니다." : "답변 삭제에 실패하였습니다.";
                }else{
                    rMsg = result ? "게시글 삭제에 성공하였습니다." : "게시글 삭제에 실패하였습니다.";
                }
            }else{
                rMsg = rMsg.equals("") ?  "권한이 없습니다." : rMsg;
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            rMsg = "삭제에 실패하였습니다.";
        }

        map.put("rMsg", rMsg);
        map.put("result", result);
        return map;
    }

    /**
     * 댓글 등록
     * @param vo
     * @return
     * @throws Exception
     */
    @Transactional
    public Map<String ,Object> setComment(ArticleVO vo) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String rMsg = "";
        boolean result = false;

        // TODO 댓글을 작성할 수 있는 게시판인지 확인하는 절차 추가
        try{
            ArticleVO articleVO = articleMapper.getArticle(vo);
            if(articleVO != null && StringUtil.isNotEmpty(articleVO.getArticle_seq())){
                /**
                 * 대댓글인 경우,
                 */
                if(StringUtil.isNotEmpty(vo.getPrt_seq())){
                    // 답글의 다음 순번 get
                    String repl_order = articleMapper.getCommentOrder(vo);
                    vo.setRepl_order(repl_order);
                }

                result = articleMapper.setComment(vo) > 0;
                if(result){
                    rMsg = "댓글이 등록되었습니다.";
                    result = true;
                }
            }else{
                rMsg = "게시글 정보가 없습니다.";
            }

        }catch (Exception e){
            e.printStackTrace();
            rMsg = "댓글등록에 실패하였습니다.";
            result = false;
        }

        map.put("rMsg", rMsg);
        map.put("result", result);
        return map;
    }

    /**
     * 댓글 수정
     * @param vo
     * @param principal
     * @return
     * @throws Exception
     */
    public Map<String,Object> updateComment(ArticleVO vo, Principal principal) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String rMsg = "";
        boolean result = false;

        try{
            if(principal != null){
                LoginVO loginVO = CommonUtil.fn_getUserAuth(principal);
                /**
                 * 댓글 정보 확인
                 */
                ArticleVO commentInfo = articleMapper.getCommentInfo(vo);
                if(commentInfo != null){
                    /**
                     * 댓글 수정 권한 검사 ( 작성자 및 관리자만 )
                     */
                    if(commentInfo.getInpt_seq().equals(loginVO.getUser_seq()) || loginVO.getUser_auth().equals("ROLE_ADMIN")){
                        result = articleMapper.updComment(vo) > 0;
                        rMsg = "댓글을 수정하였습니다.";
                    }else{
                        rMsg = "수정 권한이 없습니다.";
                    }
                }else{
                    rMsg = "댓글 정보가 없습니다.";
                }
            }else{
                rMsg = "로그인이 필요한 서비스입니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            rMsg = "댓글 수정에 실패하였습니다.";
        }

        map.put("rMsg", rMsg);
        map.put("result", result);
        return map;
    }

    /**
     * 댓글 삭제
     * @param vo
     * @param principal
     * @return
     * @throws Exception
     */
    public Map<String,Object> deleteComment(ArticleVO vo, Principal principal) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String rMsg = "";
        boolean result = false;

        try{
            if(principal != null){
                LoginVO loginVO = CommonUtil.fn_getUserAuth(principal);
                /**
                 * 댓글 정보 확인
                 */
                ArticleVO commentInfo = articleMapper.getCommentInfo(vo);
                if(commentInfo != null){
                    /**
                     * 댓글 수정 권한 검사 ( 작성자 및 관리자만 )
                     */
                    if(commentInfo.getInpt_seq().equals(loginVO.getUser_seq()) || loginVO.getUser_auth().equals("ROLE_ADMIN")){
                        if(articleMapper.commentCommentCnt(vo) == 0){
                            vo.setDel_yn("Y");
                            result = articleMapper.delComment(vo) > 0;
                            rMsg = "댓글을 삭제하였습니다.";
                        }else{
                            rMsg = "대댓글이 있는 댓글은 삭제할 수 없습니다.";
                        }
                    }else{
                        rMsg = "삭제 권한이 없습니다.";
                    }
                }else{
                    rMsg = "댓글 정보가 없습니다.";
                }
            }else{
                rMsg = "로그인이 필요한 서비스입니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            rMsg = "댓글 삭제에 실패하였습니다.";
        }

        map.put("rMsg", rMsg);
        map.put("result", result);
        return map;
    }


    /**
     * 게시글 권한 조회
     * @param vo
     * @return
     * @throws Exception
     */
    public List<ArticleVO> getAuth(ArticleVO vo) throws Exception{
        return articleMapper.getAuth(vo);
    }

    /**
     * 게시글 권한 조회
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, String> getAuth(ArticleVO vo, Principal principal) throws Exception{
        List<ArticleVO> authList = articleMapper.getAuth(vo);
        Map<String, String> map = new HashMap<>();

        LoginVO loginVO = new LoginVO();
        String writeAuth="", readAuth="", updateAuth="", deleteAuth = "";

        if (principal != null) { // 사용자 로그인 되어있을때
            loginVO = CommonUtil.fn_getUserAuth(principal);
            for(int i=0; i<authList.size(); i++) {
                if (authList.get(i).getUser_auth().equals(loginVO.getUser_auth())) {
                    writeAuth = authList.get(i).getWrite_yn();
                    readAuth = authList.get(i).getRead_yn();
                    updateAuth = authList.get(i).getUpdate_yn();
                    deleteAuth = authList.get(i).getDelete_yn();
                }
            }
            map.put("user_auth",loginVO.getUser_auth());
            map.put("user_seq", loginVO.getUser_seq());

        }else { // 사용자 로그인 안되어있을때
            for(int i=0; i<authList.size(); i++) {
                if (authList.get(i).getUser_auth().equals("ROLE_ANON")) {
                    writeAuth = authList.get(i).getWrite_yn();
                    readAuth = authList.get(i).getRead_yn();
                    updateAuth = authList.get(i).getUpdate_yn();
                    deleteAuth = authList.get(i).getDelete_yn();
                }
            }
            map.put("user_auth","ROLE_ANON");
        }

        map.put("writeAuth", writeAuth);
        map.put("readAuth", readAuth);
        map.put("updateAuth", updateAuth);
        map.put("deleteAuth", deleteAuth);
        return map;
    }

    /**
     * Q&A 비밀번호 체크
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> checkPwd(ArticleVO vo) throws Exception{
        Map<String, Object> rtnMap = new HashMap<>();
        String rHeader = "알림!";
        String rMsg = "";
        boolean result = false;

        result = articleMapper.checkPwd(vo) > 0;

        rtnMap.put("rHeader", rHeader);
        rtnMap.put("rMsg", rMsg);
        rtnMap.put("result", result);
        return rtnMap;
    }
}

