package com.custom.boot3Cms.application.mng.user.service;

import com.custom.boot3Cms.application.common.system.login.mapper.LoginLogMapper;
import com.custom.boot3Cms.application.common.system.login.vo.LoginLogVO;
import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.user.mapper.UserMngMapper;
import com.custom.boot3Cms.application.mng.user.vo.UserVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 회원관리 서비스
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2018-03-13 / 최재민  / 최초 생성
 * 	2023-07-21 / 김기식  / 산그림에 쓰일 구조로 변경
 * </pre>
 * @since 2018-03-13
 */
@Service("userMngService")
@Transactional
public class UserMngService {

    @Resource(name = "userMngMapper")
    UserMngMapper userMngMapper;

    @Resource(name = "loginLogMapper")
    private LoginLogMapper loginLogMapper;

    @Value("${Globals.file.DefaultPath}")
    private String DEFAULT_FILE_PATH;

    @Value("${Globals.file.DefaultPath}")
    private String PROFILE_PATH;


    /**
     * 회원 목록
     * @param vo
     * @return
     * @throws Exception
     */
    public List<UserVO> getUserList(UserVO vo) throws Exception{
        return userMngMapper.getUserList(vo);
    }

    /**
     *  회원 CNT
     * @param vo
     * @return
     * @throws Exception
     */
    public int getUserListCNT(UserVO vo) throws Exception{
        return userMngMapper.getUserListCNT(vo);
    }

    /**
     * 회원 상세정보
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getUserDetail(UserVO vo) throws Exception{
        return CommonUtil.fn_getDetail(userMngMapper.getUserDetail(vo));
    }

    /**
     * 회원 상세정보 of ID
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getUserDetailOfId(UserVO vo) throws Exception{
        return CommonUtil.fn_getDetail(userMngMapper.getUserDetailOfId(vo));
    }


    /**
     * 관리자 회원 상세정보
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getUserAdminDetail(UserVO vo) throws Exception{
        return CommonUtil.fn_getDetail(userMngMapper.getUserDetail(vo));
    }


    /**
     * 회원 상세정보
     * @param vo
     * @return
     * @throws Exception
     */
    public UserVO getUserDetailOne(UserVO vo) throws Exception{
        return userMngMapper.getUserDetail(vo);
    }

    /**
     * 관리자 권한 조회
     * @param user_role
     * @return
     * @throws Exception
     */
    public List<UserVO> getAdminUser(String user_role) throws Exception{
        return userMngMapper.getAdminUser(user_role);
    }

    /**
     * 회원 등록/수정/탈퇴-탈퇴복구 PROC
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> userProc(UserVO vo) throws Exception{
        Map<String, Object> rtnMap = new HashMap<>();
        String rHeader = "알림!";
        String rMsg = "";
        boolean result = false;

        if("d".equals(vo.getFlag())){
            // 회원탈퇴
            String rMsgStr = "Y".equals(vo.getOut_yn()) ? "탈퇴" : "탈퇴복구";
            result = userMngMapper.updUserOut(vo) > 0;
            rHeader = result ? "알림!" : "에러!";
            rMsg = result ? "회원 "+rMsgStr+"가 완료되었습니다." : "회원 "+rMsgStr+"에 실패하였습니다.<br/>잠시 후 다시 시도하거나, 현상이 반복 될 경우 개발사에 문의 바랍니다.";
        }else if("c".equals(vo.getFlag())){
			// 회원생성
            result = userMngMapper.setUserList(vo) > 0;
            if(result){

                result = userMngMapper.setUserDetail(vo) > 0;
                if(result){
                    rMsg = "회원정보 등록이 완료되었습니다.";
                }else{
                    result = userMngMapper.delUserList(vo.getUser_seq()) > 0;
                    rHeader = "에러!";
                    if(result){
                        rMsg = "회원정보 등록에 실패하였습니다.<br/>회원 상세정보 등록에 실패하였습니다.";
                    }else{
                        rMsg = "회원정보 등록에 실패하였습니다.<br/>기 등록된 회원목록 삭제 중 에러가 발생했습니다.";
                    }
                    result = false;
                }
            }else{
                rHeader = "에러!";
                rMsg = "회원정보 등록에 실패하였습니다.<br/>잠시 후 다시 시도하거나, 현상이 반복 될 경우 개발사에 문의 바랍니다.";
            }
        }else if("u".equals(vo.getFlag())){
            // 회원수정
            UserVO tempVO = userMngMapper.getUserDetail(vo);
            result = userMngMapper.updUserList(vo) > 0;
            if(result){
                result = userMngMapper.updUserDetail(vo) > 0;
                if(result){
                    rMsg = "회원정보 수정이 완료되었습니다.";
                }else{
                    rHeader = "에러!";
                    result = userMngMapper.updUserList(tempVO) > 0;
                    if(result){
                        result = userMngMapper.updUserDetail(tempVO) > 0;
                        if(result){
                            rMsg = "회원정보 수정에 실패하였습니다.<br/>잠시 후 다시 시도하거나, 현상이 반복 될 경우 개발사에 문의 바랍니다.";
                        }else{
                            rMsg = "회원정보 수정에 실패하였습니다.<br/>회원 상세정보 복원에 실패하였습니다. 개발사에 문의 바랍니다.";
                        }
                        result= false;
                    }else{
                        rMsg = "회원정보 수정에 실패하였습니다.<br/>회원 목록 복원에 실패하였습니다. 개발사에 문의 바랍니다.";
                    }
                }
            }else{
                rHeader = "에러!";
                result = userMngMapper.updUserList(tempVO) > 0;
                if(result){
                    rMsg = "회원정보 수정에 실패하였습니다.<br/>잠시 후 다시 시도하거나, 현상이 반복 될 경우 개발사에 문의 바랍니다.";
                }else{
                    rMsg = "회원정보 수정에 실패하였습니다.<br/>회원 목록 복원에 실패하였습니다. 개발사에 문의 바랍니다.";
                }
                result = false;
            }
        }else if("chk".equals(vo.getFlag())) {
            if (StringUtil.isNotEmpty(vo.getUser_id())) {
                // 아이디 중복검사
                result = userMngMapper.checkUserId(vo.getUser_id()) == 0;
            }
        }else if("l".equals(vo.getFlag())) {
            // 로그인 차단 해제
            String rMsgStr = "로그인 차단 해제";

            UserVO tempVO = userMngMapper.getUserDetail(vo);

            LoginLogVO loginLogVO = new LoginLogVO();
            loginLogVO.setUser_id(tempVO.getUser_id());

            loginLogMapper.updClearLockUntil(loginLogVO);

            result = true; // 잠금 기한이 지난 경우 updClearLockUntil의 결과가 1건도 없을 수도 있으며, 그 경우도 성공이기 때문에 성공으로 강제 처리

            rHeader = result ? "알림!" : "에러!";
            rMsg = result ? "회원 "+rMsgStr+"가 완료되었습니다." : "회원 "+rMsgStr+"에 실패하였습니다.<br/>잠시 후 다시 시도하거나, 현상이 반복 될 경우 개발사에 문의 바랍니다.";
        }else{
            // 정의되지 않은 예외
            rHeader = "알림!";
            rMsg = "잘못 된 접근입니다.";
        }

        rtnMap.put("rHeader", rHeader);
        rtnMap.put("rMsg", rMsg);
        rtnMap.put("result", result);
        return rtnMap;
    }

    /**
     * 권한별 관리자 조회
     * @param user_role
     * @return
     * @throws Exception
     */
    public List<UserVO> getUserListAsROLE(String... user_role) throws Exception{
        return userMngMapper.getUserListAsROLE(Arrays.asList(user_role));
    }

    /**
     * user select box 회원 목록
     * @param vo
     * @return
     * @throws Exception
     */
    public List<UserVO> getUserListOfSelectBox(UserVO vo) throws Exception{
        return userMngMapper.getUserListOfSelectBox(vo);
    }

}
