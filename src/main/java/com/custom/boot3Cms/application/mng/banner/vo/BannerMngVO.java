package com.custom.boot3Cms.application.mng.banner.vo;


import com.custom.boot3Cms.application.common.config.vo.DefaultVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 배너 관리 VO
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
@Data
@EqualsAndHashCode(callSuper = false)
@Getter @Setter
public class BannerMngVO extends DefaultVO {

    private String banner_seq; // 배너 순번
    private String banner_title; // 배너 제목
    private String banner_alt; // 배너 설명
    private String link_type; // 배너 링크타입 (B:새창 / N:현재창 / P:사용안함)
    private String link_url; // 배너 URL
    private String banner_order; // 배너 순서
    private String remark; // 설명
    private String del_yn; // 삭제여부
    private String use_yn; // 사용여부
    private String inpt_seq; // 등록자
    private String inpt_date; // 등록일
    private String upd_seq; // 수정자
    private String upd_date; // 수정일
    private String banner_start_date; // 배너 시작일자
    private String banner_end_date; // 배너 종료일자
    private String inpt_user_name; // 등록자
    private String upd_user_name; // 수정자
    private String banner_type; // 배너 유형 (T:상단배너,N:알림판)
    private String banner_title_color; // 배너 제목 색상
    private String remark_color; // 내용 색상
    private String video_yn; // 동영상 여부
    private String video_url; // 동영상 주소
    private String flag;

    public BannerMngVO(){}


    public BannerMngVO(String banner_seq){
        this.banner_seq = banner_seq;
    }
}
