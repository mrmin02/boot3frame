package com.custom.boot3Cms.application.mng.article.service;

import com.custom.boot3Cms.application.common.system.file.service.FileMngService;
import com.custom.boot3Cms.application.common.system.file.vo.FileMngVO;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.article.mapper.ArticleMngMapper;
import com.custom.boot3Cms.application.mng.article.vo.ArticleMngVO;
import com.custom.boot3Cms.application.mng.bbs.mapper.BbsMngMapper;
import com.custom.boot3Cms.application.mng.bbs.vo.BbsMngVO;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 게시글 관리 Service
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-07-02 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-07-02 */
@Transactional
@Service("articleMngService")
public class ArticleMngService {

    @Autowired
    ArticleMngMapper articleMngMapper;

    @Resource(name="bbsMngMapper")
    BbsMngMapper bbsMngMapper;

    @Resource(name="fileMngService")
    FileMngService fileMngService;

    @Value("${Globals.file.DefaultPath}")
    private String FILE_PATH;
    @Value("${Globals.file.ArticleFilePath}")
    private String ARTICLE_PATH;

    /**
     * 게시글 목록
     * @param vo
     * @return
     * @throws Exception
     */
    public List<ArticleMngVO> getArticleList(ArticleMngVO vo) throws Exception{
        return articleMngMapper.getArticleList(vo);
    }

    /**
     * 게시글 목록 CNT
     * @param vo
     * @return
     * @throws Exception
     */
    public int getArticleListCNT(ArticleMngVO vo) throws Exception{
        return articleMngMapper.getArticleListCNT(vo);
    }

    /**
     * 게시글 상세보기
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getArticle(ArticleMngVO vo) throws Exception{
        BbsMngVO bbsMngVO = bbsMngMapper.getBbsDetail(vo);
        // COPY bbsMngVO to vo
        CommonUtil.fn_copyClass(vo, bbsMngVO);
        if(StringUtil.isNotEmpty(vo.getArticle_seq())){
            articleMngMapper.updArticleReadCNT(vo);
            CommonUtil.fn_copyClassByFilterBeanName(vo = articleMngMapper.getArticle(vo), bbsMngVO, "inpt_user_name", "upd_user_name", "inpt_seq", "upd_seq", "user_id","inpt_date");

            Map<String, Object> rtnMap = new HashMap<>();
            rtnMap = CommonUtil.fn_getDetail(vo);

            return rtnMap;
        }else{
            return CommonUtil.fn_getDetail(vo);
        }
    }

    /**
     * 게시글 댓글 목록
     * @param vo
     * @return
     * @throws Exception
     */
    public List<ArticleMngVO> getCommentList(ArticleMngVO vo) throws Exception{
        return articleMngMapper.getCommentList(vo);
    }

    /**
     * 게시글 등록
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> setArticle(ArticleMngVO vo, Principal principal) throws Exception{
        Map<String, Object> map = new HashMap<>();
        String rMsg = "";
        String rHeader = "";
        boolean result = false;
        // 게시판 설정정보 GET
        BbsMngVO bbsMngVO = bbsMngMapper.getBbsDetail(vo);
        // 동일한 변수가 존재하므로 초기화 방지..
        CommonUtil.fn_copyClassByFilterBeanName(vo, bbsMngVO,"inpt_date");
        if(principal != null){
            LoginVO loginVO = CommonUtil.fn_getUserAuth(principal);
            vo.setInpt_seq(loginVO.getUser_seq());
            vo.setInpt_user_name(loginVO.getUser_name());

            if(vo.getFlag().equals("u")){
                vo.setUpd_seq(loginVO.getUser_seq());
                vo.setUpd_user_name(loginVO.getUser_name());
            }
        }
        // 설정정보가 존재 할 경우
        if(StringUtil.isNotEmpty(vo.getBbs_info_seq())){
            if("r".equals(vo.getFlag())){
                // 답글 순번 PROC
                String bbs_order = articleMngMapper.getArticleOrder(vo);
                if (StringUtil.isNotEmpty(bbs_order)) {
                    vo.setBbs_order(CommonUtil.fn_updBbsOrder(bbs_order, "R"));
                } else {
                    vo.setBbs_order(CommonUtil.fn_updBbsOrder(vo.getBbs_order(), "A"));
                }
            }
            if(bbsMngVO.getBbs_type().equals("BBS_004")){
                vo.setContent(vo.getSubject());
            }
            result = articleMngMapper.setArticle(vo) > 0;
            if (result) {
                if(!vo.getFlag().equals("r")){ //답글이 아닐 경우 prt_grp에 본인 seq를 넣어준다.
                    result = articleMngMapper.setArticlePrtGrp(vo) > 0;
                }
            }
            // 게시판 등록에 성공 할 경우
            if (result) {
                //웹진형,갤러리형 게시판 썸네일 이미지 등록
                if ((vo.getBbs_type().equals("BBS_001")) || (vo.getBbs_type().equals("BBS_002")) || (vo.getBbs_type().equals("BBS_005"))) {
                    List<FileMngVO> thumbnailList = CommonUtil.fn_getThumbnailnfo(vo, "TB_ARTICLES", vo.getArticle_seq(), FILE_PATH, ARTICLE_PATH + vo.getBbs_cd() + "/", vo.getInpt_seq(), "");

                    boolean imgFileUplaodResult = false;
                    if (thumbnailList.size() > 0) {
                        for (FileMngVO fileMngVO : thumbnailList) {
                            imgFileUplaodResult = fileMngVO.isFileResult();
                            if (imgFileUplaodResult) {
                                fileMngVO.setThumbnail_yn("Y");
                                result = fileMngService.setFile(fileMngVO);
                                if (!result) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }

                    } else {
                        imgFileUplaodResult = true;
                    }

                    if (imgFileUplaodResult) {
                        // 파일 업로드 및 데이터베이스 등록에 완전히 성공했을 경우
                        rHeader = "알림!";
                        rMsg = "게시글 등록이 완료되었습니다.";
                    } else {
                        // 게시글 물리삭제
                        result = articleMngMapper.delArticleReal(vo) > 0;
                        vo.setArticle_seq("");
                        if (result) {
                            rHeader = "에러!";
                            rMsg = "게시글 등록에 실패하였습니다.<br/>(썸네일 이미지 업로드중 에러가 발생했습니다. 개발사에 문의 바랍니다.)";
                            // 이 이후의 처리는 에러로 간주함.
                        } else {
                            rHeader = "에러!";
                            rMsg = "게시글 등록에 실패하였습니다.<br/>(더미 데이터 삭제에 실패하였습니다.)";
                            // 이 이후의 처리는 에러로 간주함.
                        }
                        result = false;
                    }
                }


                // 게시판 첨부파일 사용이 활성화 되어 있을 경우
                if ("Y".equals(vo.getAttach_file_use_yn())) {
                    // 파일 확장자 검증
                    ArticleMngVO fileParam = vo;
                    List<String> fileExtList = new ArrayList() {{
                        bbsMngMapper.getBbsFileInfo(fileParam).forEach(v -> add(v.getFile_type()));
                    }};
                    result = CommonUtil.fn_checkFileExt(vo, fileExtList.toArray());
                    // 파일 확장자 검증에 성공했을 경우
                    if (result) {
                        List<FileMngVO> fileList = CommonUtil.fn_getFileInfo(vo, "TB_ARTICLES", vo.getArticle_seq(), FILE_PATH, ARTICLE_PATH + vo.getBbs_cd() + "/", vo.getInpt_seq(), "");

                        if (fileList.size() == 0) {
                            rHeader = "알림!";
                            rMsg = "게시글 등록이 완료되었습니다.";
                        } else {
                            boolean fileUplaodResult = false;
                            if (fileList.size() > 0) {
                                for (FileMngVO fileMngVO : fileList) {
                                    fileUplaodResult = fileMngVO.isFileResult();
                                    if (fileUplaodResult) {
                                        fileMngVO.setThumbnail_yn("N");
                                        result = fileMngService.setFile(fileMngVO);
                                        if (!result) {
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }

                            } else {
                                fileUplaodResult = true;
                            }

                            if (fileUplaodResult) {
                                // 파일 업로드 및 데이터베이스 등록에 완전히 성공했을 경우
                                rHeader = "알림!";
                                rMsg = "게시글 등록이 완료되었습니다.";
                            } else {
                                // 게시글 물리삭제
                                result = articleMngMapper.delArticleReal(vo) > 0;
                                vo.setArticle_seq("");
                                if (result) {
                                    rHeader = "에러!";
                                    rMsg = "게시글 등록에 실패하였습니다.<br/>(썸네일 이미지 업로드중 에러가 발생했습니다. 개발사에 문의 바랍니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                } else {
                                    rHeader = "에러!";
                                    rMsg = "게시글 등록에 실패하였습니다.<br/>(더미 데이터 삭제에 실패하였습니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                }
                                result = false;
                            }

                        }

                    } else {
                        // 게시글 물리삭제
                        result = articleMngMapper.delArticleReal(vo) > 0;
                        vo.setArticle_seq("");
                        if (result) {
                            rHeader = "알림!";
                            rMsg = "게시글 등록에 실패하였습니다.<br/>(첨부파일 중 허용되지 않는 확장자가 포함되어 있습니다.)";
                        } else {
                            rHeader = "에러!";
                            rMsg = "게시글 등록에 실패하였습니다.<br/>(더미 데이터 삭제에 실패하였습니다.)";
                            // 이 이후의 처리는 에러로 간주함.
                        }
                        result = false;
                    }
                } else {
                    rHeader = "알림!";
                    rMsg = "게시글 등록이 완료되었습니다.";
                }
            }else{
                rHeader = "에러!";
                rMsg = "게시글 등록에 실패하였습니다.<br/>게시글 정보 등록 중 오류가 발생했습니다.";
            }
        }else{
            rHeader = "에러!";
            rMsg = "잘못된 접근입니다.";
        }
        map.put("rHeader", rHeader);
        map.put("rMsg", rMsg);
        map.put("result", result);
        return map;
    }


    /**
     * 게시글 수정
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> updArticle(ArticleMngVO vo, Principal principal) throws Exception{
        Map<String, Object> map = new HashMap<>();
        String rMsg = "";
        String rHeader = "";
        boolean result = false;
        // 게시판 설정정보 GET
        BbsMngVO bbsMngVO = bbsMngMapper.getBbsDetail(vo);
        // 동일한 변수가 존재하므로 초기화 방지..
        CommonUtil.fn_copyClassByFilterBeanName(vo, bbsMngVO,"inpt_date");
        if(principal != null){
            vo.setUpd_user_type("U");
            LoginVO loginVO = CommonUtil.fn_getUserAuth(principal);
            vo.setInpt_seq(loginVO.getUser_seq());
            vo.setUpd_seq(loginVO.getUser_seq());
        }else {
            vo.setUpd_user_type("S");
        }
        // 설정정보가 존재 할 경우
        if(StringUtil.isNotEmpty(vo.getBbs_info_seq()) && StringUtil.isNotEmpty(vo.getArticle_seq()) && "u".equals(vo.getFlag())){
            ArticleMngVO tempArticleVO = articleMngMapper.getArticle(vo);
            if(bbsMngVO.getBbs_type().equals("BBS_004")){
                vo.setContent(vo.getSubject());
            }
            result = articleMngMapper.updArticle(vo) > 0;
            // 게시판 수정에 성공 할 경우
            if(result) {

                //웹진형 게시판 썸네일 이미지 등록
                if ((vo.getBbs_type().equals("BBS_001")) || (vo.getBbs_type().equals("BBS_002"))) {
                    List<FileMngVO> thumbnailList = CommonUtil.fn_getThumbnailnfo(vo, "TB_ARTICLES", vo.getArticle_seq(), FILE_PATH, ARTICLE_PATH + vo.getBbs_cd() + "/", vo.getInpt_seq(), "");

                    boolean imgFileUplaodResult = false;
                    if (thumbnailList.size() > 0) {
                        for (FileMngVO fileMngVO : thumbnailList) {
                            imgFileUplaodResult = fileMngVO.isFileResult();
                            if (imgFileUplaodResult) {
                                fileMngVO.setThumbnail_yn("Y");
                                result = fileMngService.setFile(fileMngVO);
                                if (!result) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }

                    } else {
                        imgFileUplaodResult = true;
                    }

                    if (imgFileUplaodResult) {
                        //삭제된 이미지 데이터 삭제
                        if (StringUtil.isNotEmptyArray(vo.getDel_img_seq()) && result) {
                            for (String del_img_seq : vo.getDel_img_seq()) {
                                result = fileMngService.delFileData(new FileMngVO("TB_ARTICLES", vo.getArticle_seq(), del_img_seq));
                                if (!result) {
                                    break;
                                }
                            }
                            if(!result){
                                rHeader = "에러!";
                                rMsg = "게시글 수정에 실패하였습니다.<br/>(이전 파일정보 삭제에 실패하였습니다.)";
                            }

                        }
                        if (result) {
                            rHeader = "알림!";
                            rMsg = "게시글 수정이 완료되었습니다.";
                        }
                    } else {
                        // 게시글 물리삭제
                        result = articleMngMapper.updArticle(tempArticleVO) > 0;
                        vo.setArticle_seq("");
                        if (result) {
                            rHeader = "에러!";
                            rMsg = "게시글 수정에 실패하였습니다.<br/>(이미지 업로드중 에러가 발생했습니다. 개발사에 문의 바랍니다.)";
                            // 이 이후의 처리는 에러로 간주함.
                        } else {
                            rHeader = "에러!";
                            rMsg = "게시글 수정에 실패하였습니다.<br/>(이전 데이터 복원에 실패하였습니다.)";
                            // 이 이후의 처리는 에러로 간주함.
                        }
                        result = false;
                    }
                }

                // 게시판 첨부파일 사용이 활성화 되어 있을 경우
                if ("Y".equals(vo.getAttach_file_use_yn())) {
                    // 파일 확장자 검증
                    ArticleMngVO fileParam = vo;
                    List<String> fileExtList = new ArrayList() {{
                        bbsMngMapper.getBbsFileInfo(fileParam).forEach(v -> add(v.getFile_type()));
                    }};
                    result = CommonUtil.fn_checkFileExt(vo, fileExtList.toArray());
                    // 파일 확장자 검증에 성공했을 경우
                    if (result) {
                        List<FileMngVO> tempFileList = fileMngService.getFileList(new FileMngVO("TB_ARTICLES", vo.getArticle_seq(), ""));
                        List<FileMngVO> fileList = CommonUtil.fn_getFileInfo(vo, "TB_ARTICLES", vo.getArticle_seq(), FILE_PATH, ARTICLE_PATH + vo.getBbs_cd() + "/", vo.getInpt_seq(), "");


                        if (fileList.size() == 0) {
                            rHeader = "알림!";
                            rMsg = "게시글 수정이 완료되었습니다.";
                        } else {
                            boolean fileUplaodResult = false;
                            if (fileList.size() > 0) {
                                for (FileMngVO fileMngVO : fileList) {
                                    fileUplaodResult = fileMngVO.isFileResult();
                                    if (fileUplaodResult) {
                                        fileMngVO.setThumbnail_yn("N");
                                        result = fileMngService.setFile(fileMngVO);
                                        if (!result) {
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            } else {
                                fileUplaodResult = true;
                            }

                            if (fileUplaodResult) {
                                // 파일 업로드 및 데이터베이스 등록에 완전히 성공했을 경우
                                rHeader = "알림!";
                                rMsg = "게시글 수정이 완료되었습니다.";
                            } else {
                                // 게시글 수정
                                result = articleMngMapper.updArticle(tempArticleVO) > 0;
                                if (result) {
                                    rHeader = "에러!";
                                    rMsg = "게시글 수정에 실패하였습니다.<br/>(첨부파일 업로드중 에러가 발생했습니다. 개발사에 문의 바랍니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                } else {
                                    rHeader = "에러!";
                                    rMsg = "게시글 수정에 실패하였습니다.<br/>(이전 데이터 복원에 실패하였습니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                }
                                result = false;
                            }
                        }


                        if (StringUtil.isNotEmptyArray(vo.getDel_file_seq()) && result) {
                            for (String del_file_seq : vo.getDel_file_seq()) {
                                result = fileMngService.delFile(new FileMngVO("TB_ARTICLES", vo.getArticle_seq(), del_file_seq));
                                if (!result) {
                                    break;
                                }
                            }
                            if (!result) {
                                result = articleMngMapper.updArticle(tempArticleVO) > 0;
                                if (result) {
                                    result = fileMngService.delFiles(new FileMngVO("TB_ARTICLES", vo.getArticle_seq(), ""));
                                    if (result) {
                                        for (FileMngVO fileMngVO : tempFileList) {
                                            result = fileMngService.setFile(fileMngVO);
                                            if (!result) {
                                                break;
                                            }
                                        }
                                        if (result) {
                                            rHeader = "에러!";
                                            rMsg = "게시글 수정에 실패하였습니다.<br/>(이전 파일정보 삭제에 실패하였습니다.)";
                                            // 이 이후의 처리는 에러로 간주함.
                                        } else {
                                            rHeader = "에러!";
                                            rMsg = "게시글 수정에 실패하였습니다.<br/>(이전 파일정보 복원에 실패하였습니다.)";
                                            // 이 이후의 처리는 에러로 간주함.
                                        }
                                    } else {
                                        rHeader = "에러!";
                                        rMsg = "게시글 수정에 실패하였습니다.<br/>(업로드 파일정보 삭제에 실패하였습니다.)";
                                        // 이 이후의 처리는 에러로 간주함.
                                    }
                                } else {
                                    rHeader = "에러!";
                                    rMsg = "게시글 수정에 실패하였습니다.<br/>(이전 데이터 복원에 실패하였습니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                }
                                result = false;
                            }
                        }
                        if (result) {
                            rHeader = "알림!";
                            rMsg = "게시글 수정이 완료되었습니다.";
                        }
                    } else {
                        // 게시글 수정
                        result = articleMngMapper.updArticle(vo) > 0;
                        if (result) {
                            rHeader = "알림!";
                            rMsg = "게시글 수정에 실패하였습니다.<br/>(첨부파일 중 허용되지 않는 확장자가 포함되어 있습니다.)";
                        } else {
                            rHeader = "에러!";
                            rMsg = "게시글 수정에 실패하였습니다.<br/>(원본 데이터 복원에 실패하였습니다.)";
                            // 이 이후의 처리는 에러로 간주함.
                        }
                        result = false;
                    }
                } else {
                    rHeader = "알림!";
                    rMsg = "게시글 수정이 완료되었습니다.";
                }
            }else{
                rHeader = "에러!";
                rMsg = "게시글 수정에 실패하였습니다.<br/>게시글 정보 수정 중 오류가 발생했습니다.";
            }
        }else if(StringUtil.isNotEmpty(vo.getBbs_info_seq()) && StringUtil.isNotEmpty(vo.getArticle_seq()) && "d".equals(vo.getFlag())) {
                articleMngMapper.delCommentReal(vo); //게시글 댓글 삭제
                result = articleMngMapper.delArticle(vo) > 0;
                /*result = articleMngMapper.delArticleReal(vo) > 0;*/ //게시글 물리 삭제

                rHeader = result ? "알림!" : "에러!";
                rMsg = result ? "게시글 삭제에 성공하였습니다." : "게시글 삭제에 실패하였습니다.";

        }else{
            rHeader = "에러!";
            rMsg = "잘못된 접근입니다.";
        }
        map.put("rHeader", rHeader);
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
    public Map<String, Object> commentProc(ArticleMngVO vo) throws Exception{
        Map<String, Object> map = new HashMap<>();
        String rMsg = "";
        String rHeader = "";
        boolean result = false;
        ArticleMngVO articleMngVO = articleMngMapper.getArticle(vo);
        if(articleMngVO != null && StringUtil.isNotEmpty(vo.getArticle_seq()) && ("r".equals(vo.getFlag()) || "c".equals(vo.getFlag()))){
            if("r".equals(vo.getFlag())){
                // 답글 순번 PROC
                String repl_order = articleMngMapper.getCommentOrder(vo);
                if (StringUtil.isNotEmpty(repl_order)) {
                    vo.setRepl_order(CommonUtil.fn_updBbsOrder(repl_order, "R"));
                } else {
                    vo.setRepl_order(CommonUtil.fn_updBbsOrder(vo.getRepl_order(), "A"));
                }
                vo.setComment_seq("");
                // 초기화
            }
            result = articleMngMapper.setComment(vo) > 0;
            if (result) {
                if(!vo.getFlag().equals("r")){ //답댓글이 아닐 경우 prt_grp에 본인 seq를 넣어준다.
                    result = articleMngMapper.setCommentPrtGrp(vo) > 0;
                }
            }
            if(result){
                rHeader = "알림!";
                rMsg = "댓글 등록이 완료되었습니다.";
            }else{
                rHeader = "에러!";
                rMsg = "댓글 등록에 실패하였습니다.";
            }
        }else if(articleMngVO != null && StringUtil.isNotEmpty(vo.getArticle_seq()) && ("d".equals(vo.getFlag()) || "u".equals(vo.getFlag()))){
            vo.setDel_yn("d".equals(vo.getFlag()) ? "Y" : "N");
            result = articleMngMapper.delComment(vo) > 0;
            rHeader = result ? "알림!" : "에러!";
            rMsg = result ? "댓글 "+("d".equals(vo.getFlag()) ? "삭제가" : "복원이")+" 완료되었습니다." : "댓글 "+("d".equals(vo.getFlag()) ? "삭제에" : "복원에")+" 실패하였습니다.";
        }else{
            rHeader = "에러!";
            rMsg = "잘못된 접근입니다.";
        }
        map.put("rHeader", rHeader);
        map.put("rMsg", rMsg);
        map.put("result", result);
        return map;
    }

}

