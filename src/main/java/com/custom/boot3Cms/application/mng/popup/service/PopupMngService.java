package com.custom.boot3Cms.application.mng.popup.service;

import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.common.system.file.service.FileMngService;
import com.custom.boot3Cms.application.common.system.file.vo.FileMngVO;
import com.custom.boot3Cms.application.mng.popup.mapper.PopupMngMapper;
import com.custom.boot3Cms.application.mng.popup.vo.PopupMngVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 팝업 관리 서비스
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
@Service("popupMngService")
@Transactional
public class PopupMngService {

    @Resource
    PopupMngMapper popupMngMapper;

    @Resource(name = "fileMngService")
    private FileMngService fileMngService;

    //팝업 path로 수정
    @Value("${Globals.file.DefaultPath}")
    private String FILE_PATH;

    @Value("${Globals.file.PopupFilePath}")
    private String POPUP_PATH;

    public int getPopupListCNT(PopupMngVO vo) throws Exception {
        return popupMngMapper.getPopupListCNT(vo);
    }

    /**
     * 팝업 목록
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public List<PopupMngVO> getPopupList(PopupMngVO vo) throws Exception {
        return popupMngMapper.getPopupList(vo);
    }

    /**
     * 팝업 상세보기
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getPopupDetail(PopupMngVO vo) throws Exception {
        return CommonUtil.fn_getDetail(popupMngMapper.getPopupDetail(vo));
    }

    /**
     * 팝업 등록, 수정, 삭제 Proc
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> popupProc(PopupMngVO vo) throws Exception {
        Map<String, Object> rtnMap = new HashMap<>();
        String rtnHeader = "알림!";
        String rtnMsg = "";
        boolean result = false;
        vo.setUse_yn(StringUtil.isNotEmpty(vo.getUse_yn()) ? "Y" : "N");
        // [팝업 삭제]
        if ("d".equals(vo.getFlag())) {
            result = popupMngMapper.delPopup(vo) > 0;
            rtnMsg = result ? "팝업 삭제가 완료되었습니다." : "팝업 삭제 중 오류가 발생했습니다.<br/>잠시 후 다시 시도하거나, 현상이 반복 될 경우 개발사에 문의 바랍니다.";

        // [팝업 등록] = (글 등록 + 첨부파일 등록)
        }else if ("c".equals(vo.getFlag())) {
            result = popupMngMapper.setPopup(vo) > 0;
            if (result) { // POPUP 테이블에 글입력 성공했다면, 아래 로직에서 파일 업로드 수행
                // 첨부파일에 파일이 들어있는지 확인하는 로직
                boolean isFileAdd = false;
                if(vo.getFile() != null){
                    for(MultipartFile file : vo.getFile()){
                        if(StringUtil.isNotEmpty(file.getOriginalFilename())){
                            isFileAdd = true;
                            break;
                        }
                    }

                    // 첨부파일이 있다면
                    if (isFileAdd) {
                        List<FileMngVO> fileMngVoList = CommonUtil.fn_getFileInfo(vo, "TB_POPUP", vo.getPopup_seq(), FILE_PATH, POPUP_PATH, vo.getInpt_seq(), "image"); // 파일 업로드
                        if (fileMngVoList.size() > 0) { // 파일업로드에 성공했다면
                            boolean fileUplaodResult = false;
                            for (FileMngVO list : fileMngVoList) {
                                fileUplaodResult = list.isFileResult();
                                if(fileUplaodResult) {
                                    result = fileMngService.setFile(list);
                                    if(!result) break;
                                } else
                                    break;;
                            }

                            if(!fileUplaodResult){
                                result = popupMngMapper.delPopup(vo) > 0;
                                if(result){
                                    rtnHeader = "에러!";
                                    rtnMsg = "팝업 등록에 실패하였습니다.<br/>(첨부파일 업로드중 에러가 발생했습니다. 개발사에 문의 바랍니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                }else{
                                    rtnHeader = "에러!";
                                    rtnMsg = "팝업 등록에 실패하였습니다.<br/>(더미 데이터 삭제에 실패하였습니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                }
                                result = false;
                            }else {

                                if (!result) { // 파일 정보를 TB_FILES 테이블에 등록 실패시
                                    result = popupMngMapper.delPopup(vo) > 0;
                                    if (result) {
                                        result = fileMngService.delFiles(new FileMngVO("TB_POPUP", vo.getPopup_seq(), ""));
                                        if (result) {
                                            rtnHeader = "에러!";
                                            rtnMsg = "팝업 등록에 실패하였습니다.<br/>(첨부파일 업로드에 실패하였습니다.)";
                                        } else {
                                            rtnHeader = "에러!";
                                            rtnMsg = "팝업 등록에 실패하였습니다.<br/>(더미 파일데이터 삭제에 실패하였습니다.)";
                                        }
                                        result = false;
                                    } else { // 기존 팝업글 삭제 실패시
                                        rtnHeader = "에러!";
                                        rtnMsg = "팝업 등록에 실패하였습니다.<br/>(더미 데이터 삭제에 실패하였습니다.)";
                                    }
                                    vo.setPopup_seq("");
                                    vo.setFlag("c");
                                } else {
                                    rtnHeader = "알림!";
                                    rtnMsg = "팝업 등록이 완료되었습니다.";
                                }
                            }
                        } else {
                            result = popupMngMapper.deletePopup(vo) > 0; // 파일 업로드에 실패했지만 게시글은 등록되어있는 상태이므로 게시글 삭제
                            vo.setPopup_seq("");
                            vo.setFlag("c");
                            if(result){
                                rtnHeader = "에러!";
                                rtnMsg = "팝업 등록에 실패하였습니다.<br/>(첨부파일이 존재하지 않습니다.)";
                            }else{
                                rtnHeader = "에러!";
                                rtnMsg = "팝업 등록에 실패하였습니다.<br/>(더미 데이터 삭제에 실패하였습니다.)";
                                // 이 이후의 처리는 에러로 간주함.
                            }
                            result = false;
                        }
                    }
                }

                if(result){
                    rtnHeader = "알림!";
                    rtnMsg = "팝업 등록이 완료되었습니다.";
                }
            } else { // 팝업글 등록 실패시
                rtnHeader = "에러!";
                rtnMsg = "팝업 등록에 실패하였습니다.";
            }
        // [팝업 수정] = (글 수정 + 첨부파일 수정)
        } else if ("u".equals(vo.getFlag())) {
            PopupMngVO tempVO = popupMngMapper.getPopupDetail(vo); // 복원에 이용될 글정보를 미리 가져온다
            List<FileMngVO> tempFileList = fileMngService.getFileList(new FileMngVO("TB_POPUP", vo.getPopup_seq(), "")); // 복원될 파일정보 미리 저장

            // 팝업글 수정 성공시
            if (popupMngMapper.updPopup(vo) > 0) {
                // 삭제된 첨부파일이 있다면
                if(StringUtil.isNotEmptyArray(vo.getDel_file_seq())) {

                    for(String del_file_seq : vo.getDel_file_seq()) {
                        result = fileMngService.delFile(new FileMngVO("TB_POPUP", vo.getPopup_seq(), del_file_seq));
                        if(!result) break;
                    }

                    // 파일 DB에서 삭제 성공
                    if(result){
                        rtnHeader = "알림!";
                        rtnMsg = "팝업 수정이 완료되었습니다.";
                    // 파일 DB에서 삭제 실패
                    } else {
                        result = popupMngMapper.updPopup(tempVO) > 0; // 기존 팝업글 복구 시도
                        // 기존 팝업글 복구 성공시
                        if(result){
                            result = fileMngService.delFiles(new FileMngVO("TB_POPUP", vo.getPopup_seq(), "")); // 기존 파일 삭제 시도
                            // 기존 파일 삭제 성공시
                            if(result){
                                for(FileMngVO fileMngVO : tempFileList){
                                    result = fileMngService.setFile(fileMngVO); // 기존 파일 복구 시도
                                    if(!result) break;
                                }
                                // 기존 파일 백업 성공
                                if(result){
                                    rtnHeader = "에러!";
                                    rtnMsg = "팝업 수정에 실패하였습니다.<br/>(이전 파일정보 삭제에 실패하였습니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                // 기존 파일 복원 실패
                                }else{
                                    rtnHeader = "에러!";
                                    rtnMsg = "팝업 수정에 실패하였습니다.<br/>(이전 파일정보 복원에 실패하였습니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                }
                            // 기존 파일 삭제 실패
                            }else {
                                rtnHeader = "에러!";
                                rtnMsg = "팝업 수정에 실패하였습니다.<br/>(업로드 파일정보 삭제에 실패하였습니다.)";
                                // 이 이후의 처리는 에러로 간주함.
                            }
                        }else{
                            rtnHeader = "에러!";
                            rtnMsg = "팝업 수정에 실패하였습니다.<br/>(이전 데이터 복원에 실패하였습니다.)";
                        }
                        result = false;
                    }
                } else { // 삭제할 파일이 없으면 true, 파일을 정상적으로 삭제했을때도 true
                    rtnHeader = "알림!";
                    rtnMsg = "팝업 수정이 완료되었습니다.";
                    result = true;
                }



                // 첨부파일의 변경을 확인하고 플래그 설정
                boolean isFileAdd = false;
                if(vo.getFile() != null) {
                    for (MultipartFile file : vo.getFile()) {
                        if (StringUtil.isNotEmpty(file.getOriginalFilename())) {
                            isFileAdd = true;
                            break;
                        }
                    }
                }

                // DB에서 파일 삭제가 성공했고, 추가된 첨부파일이 있다면
                if (result && isFileAdd) {

                    // 파일 업로드
                    List<FileMngVO> fileMngVOList = CommonUtil.fn_getFileInfo(vo, "TB_POPUP", vo.getPopup_seq(), FILE_PATH, POPUP_PATH, vo.getInpt_seq(), "image");

                    // 파일업로드에 성공했다면
                    if(fileMngVOList.size() > 0) {
                        boolean fileUplaodResult = false;
                        for (FileMngVO fileMngVO : fileMngVOList) {
                            fileUplaodResult = fileMngVO.isFileResult();
                            if(fileUplaodResult){
                                result = fileMngService.setFile(fileMngVO); // 파일 DB에 등록 시도
                                if(!result){
                                    break;
                                }
                            }else{
                                break;
                            }
                        }
                        if(!fileUplaodResult){
                            result = popupMngMapper.delPopup(vo) > 0;
                            if(result){
                                rtnHeader = "에러!";
                                rtnMsg = "팝업 수정에 실패하였습니다.<br/>(첨부파일 업로드중 에러가 발생했습니다. 개발사에 문의 바랍니다.)";
                                // 이 이후의 처리는 에러로 간주함.
                            }else{
                                rtnHeader = "에러!";
                                rtnMsg = "팝업 수정에 실패하였습니다.<br/>(더미 데이터 삭제에 실패하였습니다.)";
                                // 이 이후의 처리는 에러로 간주함.
                            }
                            result = false;
                        }else {

                            // 파일 정보 DB에 넣다가 실패할 경우
                            if (!result) {
                                // 이전글 정보 복원
                                result = popupMngMapper.updPopup(tempVO) > 0; // 이전 글 정보 복원 시도
                                // 이전글 정보 복원 성공시
                                if (result) {
                                    result = fileMngService.delFiles(new FileMngVO("TB_BANNER", vo.getPopup_seq(), "")); // 파일 정보 DB에서 삭제 시도
                                    // 파일정보 삭제 성공 시
                                    if (result) {
                                        for (FileMngVO fileMngVO : tempFileList) {
                                            result = fileMngService.setFile(fileMngVO); // 기존 파일 정보 복원 시도
                                            if (!result) break;
                                        }
                                        // 기존 파일 복원 성공
                                        if (result) {
                                            rtnHeader = "에러!";
                                            rtnMsg = "팝업 수정에 실패하였습니다.<br/>(첨부파일 업로드에 실패하였습니다.)";
                                            // 이 이후의 처리는 에러로 간주함.
                                            // 기존 파일 복원 실패시
                                        } else {
                                            rtnHeader = "에러!";
                                            rtnMsg = "팝업 수정에 실패하였습니다.<br/>(이전 파일정보 복원에 실패하였습니다.)";
                                            // 이 이후의 처리는 에러로 간주함.
                                        }
                                        // 파일정보 삭제 실패시
                                    } else {
                                        rtnHeader = "에러!";
                                        rtnMsg = "팝업 수정에 실패하였습니다.<br/>(이전 파일정보 복원에 실패하였습니다.)";
                                        // 이 이후의 처리는 에러로 간주함.
                                    }
                                    // 이전글 정보 복원 실패시
                                } else {
                                    rtnHeader = "에러!";
                                    rtnMsg = "팝업 수정에 실패하였습니다.<br/>(이전 데이터 복원에 실패하였습니다.)";
                                    // 이 이후의 처리는 에러로 간주함.
                                }
                                // 파일정보 DB에 잘넣음
                            } else {
                                rtnHeader = "알림!";
                                rtnMsg = "팝업 수정이 완료되었습니다.";
                            }
                        }

                    // 파일 물리적 업로드 실패
                    } else {
                        rtnHeader = "에러!";
                        rtnMsg = "팝업 수정에 실패하였습니다.<br/>(파일이 올바르지 않습니다. 파일을 확인해주십시오.)";
                    }
                }

                // 파일첨부 혹은 파일삭제 수행하지않고 팝업글만 수정된 경우
                if(StringUtil.isNotEmptyArray(vo.getDel_file_seq()) && !isFileAdd) {
                    rtnHeader = "알림!";
                    rtnMsg = "팝업 수정에 성공하였습니다.";
                }

            } else { // 팝업글 수정 실패시
                rtnHeader = "에러!";
                rtnMsg = "팝업 수정에 실패하였습니다.";
            }
        // 잘못된 접근 (vo.getFlag()가 null이거나 c,u,d 중 아무것도 아닐때)
        } else {
            rtnHeader = "알림!";
            rtnMsg = "잘못 된 접근입니다.";
        }

        rtnMap.put("rHeader", rtnHeader);
        rtnMap.put("result", result);
        rtnMap.put("rMsg", rtnMsg);
        return rtnMap;
    }

	/**
	 * 메인화면 팝업 출력
	 * @return
	 * @throws Exception
	 */
	public List<PopupMngVO> showPopup() throws Exception{
		return popupMngMapper.showPopup();
	}
}
