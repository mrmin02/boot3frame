package com.custom.boot3Cms.application.common.config.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * @Class Name : DefaultVO.java
 * @Description : DefaultVO Class
 * @Modification Information
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2009.03.16           최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2009. 03.16
 * @version 1.0
 * @see
 *
 */
public class DefaultVO implements Serializable {
	public String getInpt_user_name() {
		return inpt_user_name;
	}

	public void setInpt_user_name(String inpt_user_name) {
		this.inpt_user_name = inpt_user_name;
	}

	public String getUpd_user_name() {
		return upd_user_name;
	}

	public void setUpd_user_name(String upd_user_name) {
		this.upd_user_name = upd_user_name;
	}

	/**
	 *  serialVersion UID
	 */
	private static final long serialVersionUID = -858838578081269359L;

	/** 검색조건 */
	private String searchCondition;

	/** 검색Keyword */
	private String searchKeyword;

	/** 검색사용여부 */
	private String searchUseYn = "";

	/** 검색정렬 */
	private String searchOrderBy;

	/** 현재페이지 */
	private int pageIndex = 1;

	/** 페이지갯수 */
	private int pageUnit = 10;

	/** 페이지사이즈 */
	private int pageSize = 10;

	/** firstIndex */
	private int firstIndex = 1;

	/** lastIndex */
	private int lastIndex = 1;

	/** recordCountPerPage */
	private int recordCountPerPage = 10;

	/** 확장목록 */
	private String extensionPage;

	private int totalCount = 1;

	private String notIn="";

	private String page = "";

	/** 날짜 검색 타입 **/
	private String searchDateType = "";

	/** 날짜 검색 시작일 **/
	private String searchDateStart = "";

	/** 날짜 검색 종료일 **/
	private String searchDateEnd = "";

	/** 날짜 검색 년 **/
	private int searchDateYear;

	/** 날짜 검색 월 **/
	private String searchDateMonth = "";

	/** 날짜 검색 월 **/
	private String searchDateDay = "";

	private String searchDateYearMonth = "";

	private String searchDate = "";

	private String searchParam = "";

	private int rowNum = 1;

	private int rNum = 1;

	private String formMode;

	private String replyMode;

	private String validCheck;

	private String flag;

	private String mode;

	private String file_nm;

	private String file_sys_nm;

	private String file_path;

	private MultipartFile[] file;

	private MultipartFile[] pic_book_file;

	private MultipartFile[] thumbnail;

	private boolean super_user;

	private String user_type;

	private String[] del_file_seq;

	private String del_file_count;

	private String inpt_user_name;

	private String upd_user_name;

	private String[] del_img_seq;

	public String[] getDel_img_seq() {
		return del_img_seq;
	}

	public void setDel_img_seq(String[] del_img_seq) {
		this.del_img_seq = del_img_seq;
	}

	public List<String> getRole_list() {
		return role_list;
	}

	public void setRole_list(List<String> role_list) {
		this.role_list = role_list;
	}

	private List<String> role_list;

	public String getDel_file_count() { return del_file_count; }

	public void setDel_file_count(String del_file_count) { this.del_file_count = del_file_count; }

	public String[] getDel_file_seq() { return del_file_seq; }

	public void setDel_file_seq(String[] del_file_seq) { this.del_file_seq = del_file_seq; }

	public String getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}

	public String getSearchDateYearMonth() {
		return searchDateYearMonth;
	}

	public void setSearchDateYearMonth(String searchDateYearMonth) {
		this.searchDateYearMonth = searchDateYearMonth;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public boolean isSuper_user() {
		return super_user;
	}

	public void setSuper_user(boolean super_user) {
		this.super_user = super_user;
	}

	public String getFile_nm() {
		return file_nm;
	}

	public void setFile_nm(String file_nm) {
		this.file_nm = file_nm;
	}

	public String getFile_sys_nm() {
		return file_sys_nm;
	}

	public void setFile_sys_nm(String file_sys_nm) {
		this.file_sys_nm = file_sys_nm;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public MultipartFile[] getFile() {
		return file;
	}

	public void setFile(MultipartFile[] file) {
		this.file = file;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public int getrNum() {
		return rNum;
	}

	public void setrNum(int rNum) {
		this.rNum = rNum;
	}

	public String getSearchParam() {
		return searchParam;
	}

	public void setSearchParam(String searchParam) {
		this.searchParam = searchParam;
	}

	public String getExtensionPage() {
		return extensionPage;
	}

	public void setExtensionPage(String extensionPage) {
		this.extensionPage = extensionPage;
	}

	public String getValidCheck() {
		return validCheck;
	}

	public void setValidCheck(String validCheck) {
		this.validCheck = validCheck;
	}

	public String getReplyMode() {
		return replyMode;
	}

	public void setReplyMode(String replyMode) {
		this.replyMode = replyMode;
	}

	public String getSearchDateMonth() {
		return searchDateMonth;
	}


	public void setSearchDateMonth(String searchDateMonth) {
		this.searchDateMonth = searchDateMonth;
	}


	public String getSearchDateDay() {
		return searchDateDay;
	}


	public void setSearchDateDay(String searchDateDay) {
		this.searchDateDay = searchDateDay;
	}

	public String getFormMode() {
		return formMode;
	}

	public void setFormMode(String formMode) {
		this.formMode = formMode;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public String getSearchDateStart() {
		return searchDateStart;
	}

	public void setSearchDateStart(String searchDateStart) {
		this.searchDateStart = searchDateStart;
	}

	public String getSearchDateEnd() {
		return searchDateEnd;
	}

	public void setSearchDateEnd(String searchDateEnd) {
		this.searchDateEnd = searchDateEnd;
	}

	public String getSearchDateType() {
		return searchDateType;
	}

	public void setSearchDateType(String searchDateType) {
		this.searchDateType = searchDateType;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getNotIn() {
		return notIn;
	}

	public void setNotIn(String notIn) {
		this.notIn = notIn;
	}

	public int getFirstIndex() {
		firstIndex = ((this.getPageIndex()-1)*this.getRecordCountPerPage()) ;
		return firstIndex;
	}

	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}

	public int getLastIndex() {
		lastIndex = ((this.getPageIndex()-1) * this.getRecordCountPerPage() + this.getRecordCountPerPage());
		return lastIndex;
	}

	public int getSearchDateYear() {
		return searchDateYear;
	}

	public void setSearchDateYear(int searchDateYear) {
		this.searchDateYear = searchDateYear;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public int getRecordCountPerPage() {
		return recordCountPerPage;
	}

	public void setRecordCountPerPage(int recordCountPerPage) {
		this.recordCountPerPage = recordCountPerPage;
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public String getSearchUseYn() {
		return searchUseYn;
	}

	public void setSearchUseYn(String searchUseYn) {
		this.searchUseYn = searchUseYn;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageUnit() {
		return pageUnit;
	}

	public void setPageUnit(int pageUnit) {
		this.pageUnit = pageUnit;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSearchOrderBy() {
		return searchOrderBy;
	}

	public void setSearchOrderBy(String searchOrderBy) {
		this.searchOrderBy = searchOrderBy;
	}

	public MultipartFile[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(MultipartFile[] thumbnail) {
		this.thumbnail = thumbnail;
	}

	public MultipartFile[] getPic_book_file() {
		return pic_book_file;
	}

	public void setPic_book_file(MultipartFile[] pic_book_file) {
		this.pic_book_file = pic_book_file;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
