package com.custom.boot3Cms.application.mng.banner.service;

import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.banner.mapper.BannerMngMapper;
import com.custom.boot3Cms.application.mng.banner.vo.BannerMngVO;
import com.custom.boot3Cms.application.common.system.file.service.FileMngService;
import com.custom.boot3Cms.application.common.system.file.vo.FileMngVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 배너 관리 Service
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
@Service("bannerMngService")
@Transactional
public class BannerMngService {

    @Resource
    BannerMngMapper bannerMngMapper;

    @Resource(name = "fileMngService")
    FileMngService fileMngService;

    //배너 path로 수정
    @Value("${Globals.file.DefaultPath}")
    private String FILE_PATH;

    @Value("${Globals.file.BannerFilePath}")
    private String BANNER_PATH;


    /**
     * 배너 리스트 CNT
     * @param vo
     * @return
     * @throws Exception
     */
    public int getBannerListCNT(BannerMngVO vo) throws Exception {
        return bannerMngMapper.getBannerListCNT(vo);
    }

    /**
     * 배너 목록
     * @param vo
     * @return
     * @throws Exception
     */
    public List<BannerMngVO> getBannerList(BannerMngVO vo) throws Exception{
        return bannerMngMapper.getBannerList(vo);
    }

    /**
     * 배너 상세보기
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getBannerDetail(BannerMngVO vo) throws Exception{
        return CommonUtil.fn_getDetail(bannerMngMapper.getBannerDetail(vo));
    }

    /**
     * 배너 등록/수정/삭제 PROC
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> bannerProc(BannerMngVO vo) throws Exception{
        Map<String, Object> map = new HashMap<>();
        String rMsg = "";
        String rHeader = "";
        boolean result = false;
        vo.setUse_yn(StringUtil.isNotEmpty(vo.getUse_yn())?"Y":"N");
        if("d".equals(vo.getFlag())) {
            result = bannerMngMapper.delBanner(vo) > 0;
            rHeader = result ? "알림!" : "에러!";
            rMsg = result ? "배너 삭제가 완료되었습니다." : "배너 삭제 중 오류가 발생했습니다.<br/>잠시 후 다시 시도하거나, 현상이 반복 될 경우 개발사에 문의 바랍니다.";
            // 이 이후의 처리는 에러로 간주함.
        }else if("c".equals(vo.getFlag())){
            result = bannerMngMapper.setBanner(vo) > 0;
            if(result){
                List<FileMngVO> fileList = CommonUtil.fn_getFileUploadResizingAndInfo(
                        vo, "TB_BANNER", vo.getBanner_seq(),
                        FILE_PATH, BANNER_PATH, vo.getInpt_seq(),
                        "image", true, 900, 1200, "no_crop", "width");
                if(fileList.size() > 0){
                    boolean fileUplaodResult = false;
                    for(FileMngVO fileMngVO : fileList){
                        fileUplaodResult = fileMngVO.isFileResult();
                        if(fileUplaodResult){
                            fileMngVO.setThumbnail_yn("N");
                            result = fileMngService.setFile(fileMngVO);
                            if(!result){
                                break;
                            }
                        }else{
                            break;
                        }
                    }
                    if(!fileUplaodResult){
                        result = bannerMngMapper.delBanner(vo) > 0;
                        if(result){
                            rHeader = "에러!";
                            rMsg = "배너 등록에 실패하였습니다.<br/>(첨부파일 업로드중 에러가 발생했습니다. 개발사에 문의 바랍니다.)";
                            // 이 이후의 처리는 에러로 간주함.
                        }else{
                            rHeader = "에러!";
                            rMsg = "배너 등록에 실패하였습니다.<br/>(더미 데이터 삭제에 실패하였습니다.)";
                            // 이 이후의 처리는 에러로 간주함.
                        }
                        result = false;
                    }else{
                        if(!result){
                            result = bannerMngMapper.delBanner(vo) > 0;
                            if(result){
                                result = fileMngService.delFiles(new FileMngVO("TB_BANNER", vo.getBanner_seq(), ""));
                                if(result){
                                    rHeader = "에러!";
                                    rMsg = "배너 등록에 실패하였습니다.<br/>(첨부파일 업로드에 실패하였습니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                }else{
                                    rHeader = "에러!";
                                    rMsg = "배너 등록에 실패하였습니다.<br/>(더미 파일데이터 삭제에 실패하였습니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                }
                                result = false;
                            }else{
                                rHeader = "에러!";
                                rMsg = "배너 등록에 실패하였습니다.<br/>(더미 데이터 삭제에 실패하였습니다.)";
                                // 이 이후의 처리는 에러로 간주함.
                            }
                            vo.setBanner_seq("");
                            vo.setFlag("c");
                        }else{
                            rHeader = "알림!";
                            rMsg = "배너 등록이 완료되었습니다.";
                        }
                    }
                }else{
                    result = bannerMngMapper.delBanner(vo) > 0;
                    vo.setBanner_seq("");
                    vo.setFlag("c");
                    if(result){
                        rHeader = "에러!";
                        rMsg = "배너 등록에 실패하였습니다.<br/>(첨부파일이 존재하지 않습니다.)";
                    }else{
                        rHeader = "에러!";
                        rMsg = "배너 등록에 실패하였습니다.<br/>(더미 데이터 삭제에 실패하였습니다.)";
                        // 이 이후의 처리는 에러로 간주함.
                    }
                    result = false;
                }
            }else{
                rHeader = "에러!";
                rMsg = "배너 등록에 실패하였습니다.";
            }
        }else if("u".equals(vo.getFlag())){
            BannerMngVO tempVO = bannerMngMapper.getBannerDetail(vo);
            result = bannerMngMapper.updBanner(vo) > 0;
            if(result){
                List<FileMngVO> tempFileList = fileMngService.getFileList(new FileMngVO("TB_BANNER", vo.getBanner_seq(), ""));
                List<FileMngVO> fileList = CommonUtil.fn_getFileInfo(vo, "TB_BANNER", vo.getBanner_seq(), FILE_PATH, BANNER_PATH, vo.getInpt_seq(), "image");
                if(fileList.size() > 0){
                    boolean fileUplaodResult = false;
                    for(FileMngVO fileMngVO : fileList){
                        fileUplaodResult = fileMngVO.isFileResult();
                        if(fileUplaodResult){
                            fileMngVO.setThumbnail_yn("N");
                            result = fileMngService.setFile(fileMngVO);
                            if(!result){
                                break;
                            }
                        }else{
                            break;
                        }
                    }
                    if(!fileUplaodResult){
                        result = bannerMngMapper.delBanner(vo) > 0;
                        if(result){
                            rHeader = "에러!";
                            rMsg = "배너 등록에 실패하였습니다.<br/>(첨부파일 업로드중 에러가 발생했습니다. 개발사에 문의 바랍니다.)";
                            // 이 이후의 처리는 에러로 간주함.
                        }else{
                            rHeader = "에러!";
                            rMsg = "배너 등록에 실패하였습니다.<br/>(더미 데이터 삭제에 실패하였습니다.)";
                            // 이 이후의 처리는 에러로 간주함.
                        }
                        result = false;
                    }else{
                        if(!result){
                            result = bannerMngMapper.updBanner(tempVO) > 0;
                            if(result){
                                result = fileMngService.delFiles(new FileMngVO("TB_BANNER", vo.getBanner_seq(), ""));
                                if(result){
                                    for(FileMngVO fileMngVO : tempFileList){
                                        result = fileMngService.setFile(fileMngVO);
                                        if(!result){
                                            break;
                                        }
                                    }
                                    if(result){
                                        rHeader = "에러!";
                                        rMsg = "배너 수정에 실패하였습니다.<br/>(첨부파일 업로드에 실패하였습니다.)";
                                        // 이 이후의 처리는 에러로 간주함.
                                    }else{
                                        rHeader = "에러!";
                                        rMsg = "배너 수정에 실패하였습니다.<br/>(이전 파일정보 복원에 실패하였습니다.)";
                                        // 이 이후의 처리는 에러로 간주함.
                                    }
                                }else{
                                    rHeader = "에러!";
                                    rMsg = "배너 수정에 실패하였습니다.<br/>(이전 파일정보 복원에 실패하였습니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                }
                            }else{
                                rHeader = "에러!";
                                rMsg = "배너 수정에 실패하였습니다.<br/>(이전 데이터 복원에 실패하였습니다.)";
                                // 이 이후의 처리는 에러로 간주함.
                            }
                            result = false;
                        }else{
                            rHeader = "알림!";
                            rMsg = "배너 수정이 완료되었습니다.";
                        }
                    }
                }else{
                    rHeader = "알림!";
                    rMsg = "배너 수정이 완료되었습니다.";
                }

                if(StringUtil.isNotEmptyArray(vo.getDel_file_seq()) && result){
                    for(String del_file_seq : vo.getDel_file_seq()){
                        result = fileMngService.delFile(new FileMngVO("TB_BANNER", vo.getBanner_seq(), del_file_seq));
                        if(!result){
                            break;
                        }
                    }
                    if(result){
                        rHeader = "알림!";
                        rMsg = "배너 수정이 완료되었습니다.";
                    }else{
                        result = bannerMngMapper.updBanner(tempVO) > 0;
                        if(result){
                            result = fileMngService.delFiles(new FileMngVO("TB_BANNER", vo.getBanner_seq(), ""));
                            if(result){
                                for(FileMngVO fileMngVO : tempFileList){
                                    result = fileMngService.setFile(fileMngVO);
                                    if(!result){
                                        break;
                                    }
                                }
                                if(result){
                                    rHeader = "에러!";
                                    rMsg = "배너 수정에 실패하였습니다.<br/>(이전 파일정보 삭제에 실패하였습니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                }else{
                                    rHeader = "에러!";
                                    rMsg = "배너 수정에 실패하였습니다.<br/>(이전 파일정보 복원에 실패하였습니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                }
                            }else{
                                rHeader = "에러!";
                                rMsg = "배너 수정에 실패하였습니다.<br/>(업로드 파일정보 삭제에 실패하였습니다.)";
                                // 이 이후의 처리는 에러로 간주함.
                            }
                        }else{
                            rHeader = "에러!";
                            rMsg = "배너 수정에 실패하였습니다.<br/>(이전 데이터 복원에 실패하였습니다.)";
                            // 이 이후의 처리는 에러로 간주함.
                        }
                        result = false;
                    }
                }
            }else{
                rHeader = "에러!";
                rMsg = "배너 수정에 실패하였습니다.";
            }
        }else{
            rHeader = "알림!";
            rMsg = "잘못 된 접근입니다.";
        }
        map.put("rHeader", rHeader);
        map.put("rMsg", rMsg);
        map.put("result", result);
        return map;
    }


    /**
     * 메인 배너 목록
     * @param vo
     * @return
     * @throws Exception
     */
    public List<BannerMngVO> getMainBannerList(BannerMngVO vo) throws Exception{
        return bannerMngMapper.getMainBannerList(vo);
    }


    public int getMainBannerCnt(BannerMngVO vo) throws Exception{
        return bannerMngMapper.getMainBannerCnt(vo);
    }

}