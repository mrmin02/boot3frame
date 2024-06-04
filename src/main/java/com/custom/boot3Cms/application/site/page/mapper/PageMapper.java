package com.custom.boot3Cms.application.site.page.mapper;

import com.custom.boot3Cms.application.site.page.vo.PageVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * PageMapper ( html )
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
@Mapper
public interface PageMapper {


    /**
     * HTML 정보 상세보기
     * @param vo
     * @return
     * @throws Exception
     */
    PageVO getPageDetail(PageVO vo) throws Exception;
}
