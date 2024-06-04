package com.custom.boot3Cms.application.site.menu.control;

import com.custom.boot3Cms.application.common.result.vo.ResultVO;
import com.custom.boot3Cms.application.site.menu.service.MenuService;
import com.custom.boot3Cms.application.site.menu.vo.MenuVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * MenuController 메뉴 컨트롤러
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-06-04 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-06-04 */
@RestController
@Tag(name="메뉴 컨트롤러")
public class MenuController {

    @Resource(name = "menuService")
    MenuService menuService;

    /**
     * 대메뉴 조회
     * @return
     * @throws Exception
     */
    @Operation(summary = "대메뉴 조회", description = "대메뉴 조회")
    @GetMapping("/api/menu/first")
    public ResultVO getfirstMenu() throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";
        try{
            Map<String,Object> map = menuService.getFirstMenu();
            if((boolean) map.get("result")){
                result = true;
                resultVO.putResult("data", map.get("topMenu"));
            }else{
                code = 404;
                rMsg = "메뉴정보가 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            code = 404;
            rMsg = "메뉴정보 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 메뉴 목록 조회
     * (대메뉴 + 서브메뉴 )
     * @return
     * @throws Exception
     */
    @Operation(summary = "메뉴 조회", description = "대메뉴 + 서브메뉴")
    @GetMapping("/api/menu/list")
    public ResultVO getMenuList() throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";
        try{
            Map<String,Object> map = menuService.getMenuList();
            if((boolean) map.get("result")){
                result = true;
                resultVO.putResult("data",map.get("topMenu"));
                resultVO.putResult("menu",map.get("menu"));
            }else{
                code = 404;
                rMsg = "메뉴정보가 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            rMsg = "메뉴정보 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 메뉴 계층형 조회(UP tree)
     * @param menuVO
     * @return
     * @throws Exception
     */
    @Operation(summary = "계층형 메뉴 조회 up", description = "계층형 메뉴 조회 ( up tree )")
    @PostMapping("/api/menu/upTree")
    public ResultVO menuUpTree(@RequestBody MenuVO menuVO) throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";
        try{
            Map<String,Object> map = menuService.getMenuUptree(menuVO);
            if((boolean) map.get("result")){
                result = true;
                resultVO.putResult("data",map.get("list"));
            }else{
                code = 404;
                rMsg = "메뉴정보가 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            rMsg = "메뉴정보 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }

    /**
     * 메뉴 계층형 조회(Down tree)
     * @param menuVO
     * @return
     * @throws Exception
     */
    @Operation(summary = "계층형 메뉴 조회 down", description = "계층형 메뉴 조회 down")
    @PostMapping("/api/menu/downTree")
    public ResultVO menuDownTree(@RequestBody MenuVO menuVO) throws Exception{
        ResultVO resultVO = new ResultVO();
        boolean result = false;
        int code = 200;
        String rMsg = "";
        try{
            Map<String,Object> map = menuService.getMenuDowntree(menuVO);
            if((boolean) map.get("result")){
                result = true;
                resultVO.putResult("data",map.get("list"));
            }else{
                code = 404;
                rMsg = "메뉴정보가 없습니다.";
            }
        }catch (Exception e){
            e.printStackTrace();
            result = false;
            rMsg = "메뉴정보 조회 중 오류가 발생하였습니다.";
        }

        resultVO.setResultMsg(rMsg);
        resultVO.putResult("result",result);
        resultVO.setResultCode(code);
        return resultVO;
    }
}
