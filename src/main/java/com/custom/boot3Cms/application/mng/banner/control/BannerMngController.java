package com.custom.boot3Cms.application.mng.banner.control;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.banner.service.BannerMngService;
import com.custom.boot3Cms.application.mng.banner.vo.BannerMngVO;
import com.custom.boot3Cms.application.common.system.file.service.FileMngService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
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
    @Operation(summary = "배너 목록")
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
            rMsg = "배너 목록 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 배너 상세보기
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "배너 상세정보")
    @GetMapping(value = "/api/mng/banner/detail/{banner_seq}")
    public ResultVO bannerDetail(@PathVariable("banner_seq") String banner_seq,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            Map<String, Object> map = bannerMngService.getBannerDetail(new BannerMngVO(banner_seq));
            if((boolean)map.get("result")){
                resultVO.putResult("data",map.get("value"));
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "배너 상세정보 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }


    /**
     * 배너 등록/수정/삭제
     * @param bannerMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "배너 등록")
    @PostMapping(value = "/api/mng/banner/proc" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultVO bannerInsertProc(@ModelAttribute("bannerMngVO") BannerMngVO bannerMngVO,
                                  HttpServletRequest request,
                                  HttpServletResponse response,
                                  Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                bannerMngVO.setInpt_seq(vo.getUser_seq());
                bannerMngVO.setUpd_seq(vo.getUser_seq());
            }
            bannerMngVO.setFlag("c");
            Map<String, Object> map = bannerMngService.bannerProc(bannerMngVO);
            if((boolean)map.get("result")){
                resultVO.putResult("data",map.get("value"));
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "배너 등록 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }


    /**
     * 배너 등록/수정/삭제
     * @param bannerMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "배너 수정")
    @PutMapping(value = "/api/mng/banner/proc" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultVO bannerUpdateProc(@ModelAttribute("bannerMngVO") BannerMngVO bannerMngVO,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                bannerMngVO.setInpt_seq(vo.getUser_seq());
                bannerMngVO.setUpd_seq(vo.getUser_seq());
            }
            bannerMngVO.setFlag("u");
            Map<String, Object> map = bannerMngService.bannerProc(bannerMngVO);
            if((boolean)map.get("result")){
                resultVO.putResult("data",map.get("value"));
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "배너 수정 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 배너 등록/수정/삭제
     * @param bannerMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "배너 삭제")
    @DeleteMapping(value = "/api/mng/banner/proc")
    public ResultVO bannerDeleteProc(@RequestBody BannerMngVO bannerMngVO,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(principal != null){
                LoginVO vo = CommonUtil.fn_getUserAuth(principal);
                bannerMngVO.setInpt_seq(vo.getUser_seq());
                bannerMngVO.setUpd_seq(vo.getUser_seq());
            }
            bannerMngVO.setFlag("d");
            Map<String, Object> map = bannerMngService.bannerProc(bannerMngVO);
            if((boolean)map.get("result")){
                resultVO.putResult("data",map.get("value"));
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "배너 삭제 중 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

}
