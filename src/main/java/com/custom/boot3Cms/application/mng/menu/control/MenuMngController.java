package com.custom.boot3Cms.application.mng.menu.control;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.mng.menu.service.MenuMngService;
import com.custom.boot3Cms.application.mng.menu.vo.MenuMngVO;
import com.custom.boot3Cms.application.mng.user.service.UserMngService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;

/**
 * 메뉴관리 Controller
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-06-25 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-06-25 */
@Tag(name = "메뉴 관리 컨트롤러")
@RestController
public class MenuMngController {

    @Resource(name = "menuMngService")
    MenuMngService menuMngService;

    @Resource(name = "userMngService")
    UserMngService userMngService;

    /**
     * 메뉴 정보 목록
     * @param menuMngVO
     * @param model
     * @param request
     * @param response
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "메뉴 정보 목록")
    @GetMapping("/api/mng/menu/list")
    public ResultVO menuInfoList(@ModelAttribute("menuMngVO") MenuMngVO menuMngVO
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
            , Principal principal) throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            // FIXME 체크 필요
            resultVO.putResult("total_cnt", menuMngService.getMenuInfoListCNT(menuMngVO));
            resultVO.putResult("list", menuMngService.getMenuInfoList(menuMngVO));
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "메뉴 목록 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 메뉴 정보 상세보기
     * @param menuMngVO
     * @param model
     * @param request
     * @param response
     * @param redirectAttributes
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "메뉴 정보 상세")
    @GetMapping("/api/mng/menu/detail/{menu_info_seq}")
    public ResultVO menuInfoDetail(@ModelAttribute("menuMngVO") MenuMngVO menuMngVO
            , @PathVariable("menu_info_seq") String menu_info_seq
            , Model model
            , HttpServletRequest request
            , HttpServletResponse response
            , RedirectAttributes redirectAttributes
            , Principal principal) throws Exception {
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            Map<String, Object> map = menuMngService.getMenuInfoDetail(new MenuMngVO(menu_info_seq, ""));
            if ((boolean) map.get("result")) {
                menuMngVO = (MenuMngVO) map.get("value");
                resultVO.putResult("list", menuMngService.getMenuAdminList(menuMngVO));
                result = true;
//                model.addAttribute("menuMngVO", menuMngVO);
            } else {
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "메뉴 상세정보 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 메뉴 정보 등록
     * @param menuMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "메뉴 정보 등록")
    @PostMapping(value = "/api/mng/menu/proc")
    public ResultVO menuInfoProc(@RequestBody MenuMngVO menuMngVO,
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
                menuMngVO.setInpt_seq(vo.getUser_seq());
                menuMngVO.setUpd_seq(vo.getUser_seq());
            }
            Map<String,Object> map = menuMngService.setMenuInfo(menuMngVO);
            if((boolean)map.get("result")){
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "메뉴 등록 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 메뉴 정보 수정
     * @param menuMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "메뉴 정보 수정")
    @PutMapping(value = "/api/mng/menu/proc")
    public ResultVO menuInfoUpdate(@RequestBody MenuMngVO menuMngVO,
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
                menuMngVO.setInpt_seq(vo.getUser_seq());
                menuMngVO.setUpd_seq(vo.getUser_seq());
            }
            menuMngVO.setFlag("u");
            Map<String,Object> map = menuMngService.updMenuInfo(menuMngVO);
            if((boolean)map.get("result")){
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "메뉴 정보 수정 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 메뉴 정보 수정
     * @param menuMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "메뉴 정보 삭제")
    @PutMapping(value = "/api/mng/menu/proc")
    public ResultVO menuInfoDelete(@RequestBody MenuMngVO menuMngVO,
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
                menuMngVO.setInpt_seq(vo.getUser_seq());
                menuMngVO.setUpd_seq(vo.getUser_seq());
            }
            menuMngVO.setFlag("d");
            Map<String,Object> map = menuMngService.updMenuInfo(menuMngVO);
            if((boolean)map.get("result")){
                result = true;
            }else{
                rMsg = String.valueOf(map.get("rMsg"));
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "메뉴 정보 삭제 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 메뉴 데이터 리스트
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "메뉴 데이터 리스트")
    @GetMapping("/api/mng/menu/data/list")
    public ResultVO menuList(@RequestParam("menu_info_seq") String menu_info_seq
            , HttpServletRequest request
            , HttpServletResponse response ) throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            MenuMngVO menuMngVO = new MenuMngVO();
            menuMngVO.setMenu_info_seq(menu_info_seq);

            resultVO.putResult("list",menuMngService.getMenu(menuMngVO));
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "메뉴 데이터 리스트 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 메뉴 타입별 링크 목록
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Operation(summary = "메뉴 타입별 링크 목록")
    @GetMapping("/api/mng/menu/data/{menu_type}/{menu_info_seq}/list")
    public ResultVO menuLinkList(@PathVariable("menu_type") String menu_type
            , @PathVariable("menu_info_seq") String menu_info_seq
            , HttpServletRequest request
            , HttpServletResponse response ) throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            MenuMngVO menuMngVO = new MenuMngVO();
            menuMngVO.setMenu_type(menu_type);
            menuMngVO.setMenu_info_seq(menu_info_seq);

            resultVO.putResult("list",menuMngService.getLinkList(menuMngVO));
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "메뉴 데이터 리스트 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 메뉴 등록
     * @param menuMngVO
     * @param request
     * @param response
     * @param principal
     * @return
     * @throws Exception
     */
    @Operation(summary = "메뉴 데이터 등록")
    @PostMapping("/api/mng/config/menu/data/proc")
    public ResultVO setMenu(@ModelAttribute("menuMngVO") MenuMngVO menuMngVO
            , HttpServletRequest request
            , HttpServletResponse response
            , Principal principal) throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";

        try{
            LoginVO vo = null;
            if(principal != null){
                vo = CommonUtil.fn_getUserAuth(principal);
            }
            menuMngService.setMenu(menuMngVO, vo.getUser_seq());
            result = true;
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "메뉴 데이터 등록 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }
}
