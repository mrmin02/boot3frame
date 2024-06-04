package com.custom.boot3Cms.application.site.page.service;

import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.site.page.mapper.PageMapper;
import com.custom.boot3Cms.application.site.page.vo.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.Map;

/**
 * PageService ( html )
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
@Service("pageService")
@Transactional
public class PageService {

    @Resource(name = "pageMapper")
    PageMapper pageMapper;

    /**
     * HTML 정보 상세보기
     * @param vo
     * @return
     * @throws Exception
     */
    public PageVO getPageDetail(PageVO vo) throws Exception{
        return pageMapper.getPageDetail(vo);
    }


}
