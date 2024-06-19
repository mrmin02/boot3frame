package com.custom.boot3Cms.application.mng.adminAccess.service;

import com.custom.boot3Cms.application.common.system.login.vo.LoginVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.mng.adminAccess.mapper.AdminAccessMngMapper;
import com.custom.boot3Cms.application.mng.adminAccess.vo.AdminAccessMngVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.security.Principal;
import java.util.*;

/**
 * 관리자 페이지 접근 관리 Service
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
@Service("adminAccessMngService")
@Transactional
public class AdminAccessMngService {

    @Resource
    AdminAccessMngMapper adminAccessMngMapper;

    /**
     * 관리자 페이지 접근 가능 IP List CNT
     * @param vo
     * @return
     * @throws Exception
     */
    public int getAdminAccessListCNT(AdminAccessMngVO vo) throws Exception {
        return adminAccessMngMapper.getAdminAccessListCNT(vo);
    }

    /**
     * 관리자 페이지 접근 가능 IP 리스트
     * @return
     * @throws Exception
     */
    public List<AdminAccessMngVO> getAdminAccessList(AdminAccessMngVO vo) throws Exception {
        return adminAccessMngMapper.getAdminAccessList(vo);
    }

    /**
     * 관리자 페이지 접근 가능 IP 상세보기
     * @return
     * @param vo
     * @throws Exception
     */
    public Map<String, Object> getAdminAccessDetail(AdminAccessMngVO vo) throws Exception {
        return CommonUtil.fn_getDetail(adminAccessMngMapper.getAdminAccessDetail(vo));
    }

    /**
     * 관리자 페이지 접근 가능 IP Proc
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> adminAccessProc(AdminAccessMngVO vo, Principal principal) throws Exception {
        if(principal != null){
            LoginVO loginVO = CommonUtil.fn_getUserAuth(principal);
            vo.setInpt_seq(loginVO.getUser_seq());
            vo.setUpd_seq(loginVO.getUser_seq());
        }
        Map<String, Object> result = new HashMap<>();
        vo.setUse_yn(vo.getUse_yn() == null ? "N" : "Y");

        switch (vo.getFlag()) {
            case "C": result = setAdminAccess(vo); break;
            case "U": result = updAdminAccess(vo); break;
            case "D": result = delAdminAccess(vo); break;
            default:
                result.put("rHeader", "에러!");
                result.put("rMsg", "잘못된 접근입니다.");
                result.put("result", false);
        }

        return result;
    }

    /**
     * 관리자 페이지 접근 가능 IP 등록
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> setAdminAccess(AdminAccessMngVO vo) throws Exception {
        Map<String, Object> rtnMap = new HashMap<>();
        boolean result = adminAccessMngMapper.setAdminAccess(vo) > 0;
        String rHeader = result ? "알림!" : "에러!";
        String rMsg = result ? "등록이 완료되었습니다." : "오류가 발생했습니다.";

        rtnMap.put("result", result);
        rtnMap.put("rHeader", rHeader);
        rtnMap.put("rMsg", rMsg);

        return rtnMap;
    }

    /**
     * 관리자 페이지 접근 가능 IP 수정
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> updAdminAccess(AdminAccessMngVO vo) throws Exception{
        Map<String, Object> rtnMap = new HashMap<>();
        boolean result = adminAccessMngMapper.updAdminAccess(vo) > 0;
        String rHeader = result ? "알림!" : "에러!";
        String rMsg = result ? "수정이 완료되었습니다." : "오류가 발생했습니다.";

        rtnMap.put("result", result);
        rtnMap.put("rHeader", rHeader);
        rtnMap.put("rMsg", rMsg);

        return rtnMap;
    }

    /**
     * 관리자 페이지 접근 가능 IP 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> delAdminAccess(AdminAccessMngVO vo) throws Exception {
        Map<String, Object> rtnMap = new HashMap<>();
        boolean result = adminAccessMngMapper.delAdminAccess(vo) > 0;
        String rHeader = result ? "알림!" : "에러!";
        String rMsg = result ? "삭제가 완료되었습니다." : "오류가 발생했습니다.";

        rtnMap.put("result", result);
        rtnMap.put("rHeader", rHeader);
        rtnMap.put("rMsg", rMsg);

        return rtnMap;
    }

    /**
     * 관리자 페이지 접근 가능 여부 판별
     * @return
     * @throws Exception
     */
    public boolean getAdminAccessList2(String ip) throws Exception {
        boolean rtnBoolean = false;
        List<AdminAccessMngVO> rtnList = adminAccessMngMapper.getAdminAccessList2();
        List<String> allowedList = new ArrayList<>(Arrays.asList("0:0:0:0:0:0:0:1"));

        // 위의 IP외에는 DB에서 검색해서 true false 리턴
        if(allowedList.contains(ip)) {
            rtnBoolean = true;
        } else if (rtnList.size() == 0) {
            // 호용된 IP가 없는 경우 모두 허용
            rtnBoolean = true;
        } else {
            for(AdminAccessMngVO i : rtnList) {
                if(i.getAdmin_access_ip().equals(ip)) {
                    rtnBoolean = true;
                    break;
                }
            }
        }

        return rtnBoolean;
    }
}
