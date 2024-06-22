package com.custom.boot3Cms.application.mng.code.service;

import com.custom.boot3Cms.application.mng.code.mapper.CodeMngMapper;
import com.custom.boot3Cms.application.mng.code.vo.CodeMngVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 관리자 코드관리 서비스
 *
 * @author SEKOREA
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2017-09-06 / 최재민	  / 최초 생성
 * </pre>
 * @since 2017-09-06
 */
@Service("codeMngService")
@Transactional
public class CodeMngService {

    @Autowired
    CodeMngMapper codeMapper;

    /**
     * 코드 목록
     * @param vo
     * @return
     * @throws Exception
     */
    public List<CodeMngVO> getCodeList(CodeMngVO vo) throws Exception{
        List<CodeMngVO> codeVoList = codeMapper.getCodeList(vo);
        Map<String, Boolean> state = new HashMap<>();
        state.put("opened", true);
        codeVoList.add(0, new CodeMngVO("0", "공통코드 그룹", "#", state, "root"));
        return codeVoList;
    }

    /**
     * 코드 상세보기
     * @param vo
     * @return
     * @throws Exception
     */
    public CodeMngVO getCodeDetail(CodeMngVO vo) throws Exception{
        return codeMapper.getCodeDetail(vo);
    }

    /**
     * 코드 목록 (커스텀 태그)
     * @param code
     * @return
     * @throws Exception
     */
    public List<CodeMngVO> getCodeToTag(String code) throws Exception{
        return codeMapper.getCodeToTag(code);
    }

    /**
     * 자식코드 개수 확인
     * @param code
     * @return
     * @throws Exception
     */
    public int getChildCodeSize(String code) throws Exception{
        return codeMapper.getChildCodeSize(code);
    }

    /**
     * 코드 NAME 출력 (커스텀 태그)
     * @param code
     * @return
     * @throws Exception
     */
    public String getCodeToText(String code) throws Exception{
        return codeMapper.getCodeToText(code);
    }

    /**
     * 코드 중복 체크
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getCodeCheck(CodeMngVO vo) throws Exception{
        Map<String, Object> rtnMap = new HashMap<>();
        int result = codeMapper.getCodeCheck(vo);
        boolean rtnVal = result>0?false:true;
        String rtnMsg = result>0?"중복된 코드명이 존재합니다.":"사용가능한 코드입니다.";
        rtnMap.put("result", rtnVal);
        rtnMap.put("rMsg", rtnMsg);
        return rtnMap;
    }

    /**
     * 코드 등록/수정/삭제
     * @param vo
     * @return
     * @throws Exception
     */
    public Map<String, Object> codeProc(CodeMngVO vo) throws Exception{
        int result = 0;
        Map<String, Object> rtnMap = new HashMap<>();
        String rtnMsg = "";
        if("c".equals(vo.getFlag())){
            result = codeMapper.setCode(vo);
            rtnMsg = result>0?"코드 등록에 성공하였습니다.":"코드 등록에 실패하였습니다.";
        }else if("u".equals(vo.getFlag())){
            result = codeMapper.updCode(vo);
            rtnMsg = result>0?"코드 수정에 성공하였습니다.":"코드 수정에 실패하였습니다.";
        }else if("d".equals(vo.getFlag())){
            result = codeMapper.delCode(vo);
            rtnMsg = result>0?"코드 삭제에 성공하였습니다.":"코드 삭제에 실패하였습니다.";
        }else if("dp".equals(vo.getFlag())){
            codeMapper.delChildrenCode(vo);
            result = codeMapper.delCode(vo);
            rtnMsg = result>0?"코드 삭제에 성공하였습니다.":"코드 삭제에 실패하였습니다.";
        }else{
            rtnMap.put("result", false);
            rtnMap.put("msg", "잘못된 접근입니다.");
            return rtnMap;
        }
        boolean rtnVal = result>0?true:false;
        String rtnHeader = rtnVal ? "알림!" : "에러!";
        rtnMap.put("result", rtnVal);
        rtnMap.put("rHeader", rtnHeader);
        rtnMap.put("rMsg", rtnMsg);
        return rtnMap;
    }

    /**
     * 코드 목록 by Searching
     * @param vo
     * @return
     * @throws Exception
     */
    public List<CodeMngVO> getCodeListByType(CodeMngVO vo) throws Exception{
        return codeMapper.getCodeListByType(vo);
    }

    /**
     * 산그림 홈페이지에 맞게 권한관리 구현을 위한 코드 목록
     * @param userAuthConfVO
     * @return
     * @throws Exception
     */
    public List<CodeMngVO> getCodeToAuthConf(CodeMngVO userAuthConfVO) throws Exception{
        return codeMapper.getCodeToAuthConf(userAuthConfVO);
    }

}
