package com.custom.boot3Cms.application.mng.banner.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.custom.boot3Cms.application.mng.banner.vo.BannerMngVO;
import org.springframework.boot.Banner;

import java.util.List;

/**
 * 배너 관리 Mapper
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
@Mapper
public interface BannerMngMapper {

    /**
     * 배너 List CNT
     * @param vo
     * @return
     * @throws Exception
     */
    int getBannerListCNT(BannerMngVO vo) throws Exception;

    /**
     * 배너 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<BannerMngVO> getBannerList(BannerMngVO vo) throws Exception;

    /**
     * 배너 상세보기
     * @param vo
     * @return
     * @throws Exception
     */
    BannerMngVO getBannerDetail(BannerMngVO vo) throws Exception;

    /**
     * 배너 등록
     * @param vo
     * @return
     * @throws Exception
     */
    int setBanner(BannerMngVO vo) throws Exception;

    /**
     * 배너 수정
     * @param vo
     * @return
     * @throws Exception
     */
    int updBanner(BannerMngVO vo) throws Exception;

    /**
     * 배너 삭제 (UPDATE)
     * @param vo
     * @return
     * @throws Exception
     */
    int delBanner(BannerMngVO vo) throws Exception;

    /**
     * 배너 완전 삭제 (DELETE)
     * @param vo
     * @return
     * @throws Exception
     */
    int deleteBanner(BannerMngVO vo) throws Exception;


    /**
     * 메인 배너 목록
     * @param vo
     * @return
     * @throws Exception
     */
    List<BannerMngVO> getMainBannerList(BannerMngVO vo) throws Exception;

    /**
     * 메인 배너 갯수
     * @param vo
     * @return
     * @throws Exception
     */
    int getMainBannerCnt(BannerMngVO vo) throws Exception;

}
