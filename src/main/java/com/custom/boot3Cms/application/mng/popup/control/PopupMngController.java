package com.custom.boot3Cms.application.mng.popup.control;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.system.file.service.FileMngService;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.mng.popup.service.PopupMngService;
import com.custom.boot3Cms.application.mng.popup.vo.PopupMngVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;

/**
 * 팝업 관리 Controller
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
@Tag(name="팝업 관리 컨트롤러")
@RestController
public class PopupMngController {

    @Resource(name = "popupMngService")
    PopupMngService popupMngService;

    @Resource(name = "fileMngService")
    FileMngService fileMngService;

    /**
     * 팝업 리스트
     *
     * @param popupMngVO
     * @param model
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @throws Exception
     **/
    @Operation(summary = "팝업 리스트")
    @GetMapping("/api/mng/popup/list")
    public ResultVO popupList(@ModelAttribute("popupMngVO") PopupMngVO popupMngVO
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
            , Principal principal
    ) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null) {
                resultVO.putResult("total_cnt",popupMngService.getPopupListCNT(popupMngVO));
                resultVO.putResult("list",popupMngService.getPopupList(popupMngVO));
                result = true;
            }else{
                rMsg = "로그인 정보가 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "팝업 리스트 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 팝업관리 상세보기
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "팝업 상세정보")
    @GetMapping("/api/mng/popup/detail/{popup_seq}")
    public ResultVO popupDetail( @PathVariable("popup_seq") String popup_seq
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
    )throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";
        // TODO 팝업 첨부파일은 첨부파일을 가져오는 전용 API 를 구현하여 요청하기
        try{
            Map<String, Object> map = popupMngService.getPopupDetail(new PopupMngVO(popup_seq));
            if((boolean)map.get("result")) {
                resultVO.putResult("data",map.get("value"));
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg").toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "팝업 조회 중 오류가 발생하였습니다.";
        }


        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }


    /**
     * 팝업 등록/수정 PROC
     * @param popupMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "팝업 등록")
    @PostMapping(value={"/api/mng/popup/proc"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultVO popupInsertProc(@ModelAttribute("PopupMngVO") PopupMngVO popupMngVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal
    )throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                popupMngVO.setInpt_seq(vo.getUser_seq());
                popupMngVO.setUpd_seq(vo.getUser_seq());

                popupMngVO.setFlag("c");
                Map<String, Object> map = popupMngService.popupProc(popupMngVO);
                if((boolean)map.get("result")) {
                    result = true;
                }else{
                    rMsg = String.valueOf(map.get("rMsg").toString());
                }
            }else{
                rMsg = "로그인 정보가 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "팝업 등록 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 팝업 수정
     * @param popupMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "팝업 수정")
    @PutMapping(value = {"/api/mng/popup/proc"} , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultVO popupUpdateProc(@ModelAttribute("PopupMngVO") PopupMngVO popupMngVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal
    )throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                popupMngVO.setInpt_seq(vo.getUser_seq());
                popupMngVO.setUpd_seq(vo.getUser_seq());

                popupMngVO.setFlag("u");
                Map<String, Object> map = popupMngService.popupProc(popupMngVO);
                if((boolean)map.get("result")) {
                    result = true;
                }else{
                    rMsg = String.valueOf(map.get("rMsg").toString());
                }
            }else{
                rMsg = "로그인 정보가 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "팝업 수정 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 팝업 삭제
     * @param popupMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "팝업 삭제")
    @DeleteMapping("/api/mng/popup/proc")
    public ResultVO popupDeleteProc(@RequestBody PopupMngVO popupMngVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal ) throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                popupMngVO.setInpt_seq(vo.getUser_seq());
                popupMngVO.setUpd_seq(vo.getUser_seq());

                popupMngVO.setFlag("d");
                Map<String, Object> map = popupMngService.popupProc(popupMngVO);
                if((boolean)map.get("result")) {
                    result = true;
                }else{
                    rMsg = String.valueOf(map.get("rMsg").toString());
                }
            }else{
                rMsg = "로그인 정보가 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "팝업 삭제 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }
}
