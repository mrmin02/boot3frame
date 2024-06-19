package com.custom.boot3Cms.application.mng.adminAccess.control;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.adminAccess.service.AdminAccessMngService;
import com.custom.boot3Cms.application.mng.adminAccess.vo.AdminAccessMngVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

/**
 * 관리자 페이지 접근 관리 Controller
 * TODO 관리자 Interceptor 구성 시, IP 조회!!
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-06-19 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-06-19 */
@Tag(name = "관리자 페이지 접근 IP 관리 컨트롤러")
@RestController
public class AdminAccessMngController {

    @Resource(name = "adminAccessMngService")
    AdminAccessMngService adminAccessMngService;


    /**
     * 관리자 페이지 접근 가능 IP 리스트
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "관리자 페이지 접근 가능 IP 리스트")
    @GetMapping("/api/mng/adminAccess/list")
    public ResultVO list(@ModelAttribute("AdminAccessMngVO") AdminAccessMngVO adminAccessMngVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal) throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            resultVO.putResult("total_cnt", adminAccessMngService.getAdminAccessListCNT(adminAccessMngVO));
            resultVO.putResult("list", adminAccessMngService.getAdminAccessList(adminAccessMngVO));
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "IP 목록 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 관리자 페이지 접근 가능 IP 상세정보
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "관리자 페이지 접근 가능 IP 상세정보")
    @GetMapping("/api/mng/adminAccess/detail/{admin_access_seq}")
    public ResultVO detail(@PathParam("admin_access_seq") String admin_access_seq
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal) throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            if(StringUtil.isNotEmpty(admin_access_seq)) {
                AdminAccessMngVO adminAccessMngVO = new AdminAccessMngVO();
                adminAccessMngVO.setAdmin_access_seq(admin_access_seq);

                Map<String, Object> map = adminAccessMngService.getAdminAccessDetail(adminAccessMngVO);
                if((boolean)map.get("result")){
                    resultVO.putResult("data",map.get("value"));
                    result = true;
                }else{
                    rMsg = String.valueOf(map.get("rMsg"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "IP 정보 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 관리자 페이지 접근 가능 IP 등록 proc
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "관리자 페이지 접근 가능 IP 등록")
    @PostMapping("/api/mng/adminAccess/proc")
    public ResultVO insertProc(@RequestBody AdminAccessMngVO adminAccessMngVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            adminAccessMngVO.setFlag("C");
            Map<String, Object> map = adminAccessMngService.adminAccessProc(adminAccessMngVO, principal);
            if((boolean)map.get("result")){
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "IP 정보 등록 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }


    /**
     * 관리자 페이지 접근 가능 IP 수정 proc
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "관리자 페이지 접근 가능 IP 수정")
    @PutMapping("/api/mng/adminAccess/proc")
    public ResultVO updateProc(@RequestBody AdminAccessMngVO adminAccessMngVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            adminAccessMngVO.setFlag("U");
            Map<String, Object> map = adminAccessMngService.adminAccessProc(adminAccessMngVO, principal);
            if((boolean)map.get("result")){
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "IP 정보 수정 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 관리자 페이지 접근 가능 IP 삭제 proc
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "관리자 페이지 접근 가능 IP 삭제")
    @DeleteMapping("/api/mng/adminAccess/proc")
    public ResultVO deleteProc(@RequestBody AdminAccessMngVO adminAccessMngVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            adminAccessMngVO.setFlag("D");
            Map<String, Object> map = adminAccessMngService.adminAccessProc(adminAccessMngVO, principal);
            if((boolean)map.get("result")){
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "IP 정보 삭제 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }
}
