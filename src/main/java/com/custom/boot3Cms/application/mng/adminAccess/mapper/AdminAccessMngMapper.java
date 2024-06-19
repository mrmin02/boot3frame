
package com.custom.boot3Cms.application.mng.adminAccess.mapper;

import com.custom.boot3Cms.application.mng.adminAccess.vo.AdminAccessMngVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 관리자 페이지 접근 가능 IP 관리 Mapper
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
@Mapper
public interface AdminAccessMngMapper {

    /**
     * 관리자 페이지 접근 가능 IP List CNT
     * @param adminAccessMngVO
     * @return
     * @throws Exception
     */
    int getAdminAccessListCNT(AdminAccessMngVO adminAccessMngVO) throws Exception;

    /**
     * 관리자 페이지 접근 가능 IP 리스트
     * @return
     * @throws Exception
     */
    List<AdminAccessMngVO> getAdminAccessList(AdminAccessMngVO adminAccessMngVO) throws Exception;

    /**
     * 관리자 페이지 접근 가능 IP 상세보기
     * @return
     * @param vo
     * @throws Exception
     */
    AdminAccessMngVO getAdminAccessDetail(AdminAccessMngVO vo) throws Exception;

    /**
     * 관리자 페이지 접근 가능 IP 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setAdminAccess(AdminAccessMngVO vo) throws Exception;

    /**
     * 관리자 페이지 접근 가능 IP 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updAdminAccess(AdminAccessMngVO vo) throws Exception;

    /**
     * 관리자 페이지 접근 가능 IP 삭제
     * @param vo
     * @return
     * @throws Exception
     */
    int delAdminAccess(AdminAccessMngVO vo) throws Exception;

    /**
     * 접근 가능한 IP 리스트
     * @return
     * @throws Exception
     */
    List<AdminAccessMngVO> getAdminAccessList2() throws Exception;

}

