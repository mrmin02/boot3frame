package com.custom.boot3Cms.application.common.config.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;


/**
 * 공통 DTO
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-05-07 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-05-07 */
@Schema(description = "공통 DTO")
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

	@Schema(description = "검색 조건")
	private String searchCondition;

	@Schema(description = "검색어")
	private String searchKeyword;

	@Schema(description = "검색 사용 여부", example = "Y or N")
	private String searchUseYn = "";

	@Schema(description = "검색 정렬 기준")
	private String searchOrderBy;

	@Schema(description = "현재 페이지")
	private int pageIndex = 1;

	@Schema(description = "페이지 개수")
	private int pageUnit = 10;

	@Schema(description = "페이지 사이즈", hidden = true)
	private int pageSize = 10;

	@Schema(description = "첫번째 페이지", hidden = true)
	private int firstIndex = 1;

	@Schema(description = "마지막 페이지", hidden = true)
	private int lastIndex = 1;

	@Schema(description = "페이지 당 노출 개수", hidden = true)
	private int recordCountPerPage = 10;

	@Schema(description = "확장목록", hidden = true)
	private String extensionPage;

	@Schema(description = "전체 개수", hidden = true)
	private int totalCount = 1;

	@Schema(description = "검색결과 순번", hidden = true)
	private int rNum = 1;

//	private String formMode;
//
//	private String replyMode;
//
//	private String validCheck;

//	private String flag;

//	private String mode;

	@Schema(description = "파일이름", hidden = true)
	private String file_nm;

	@Schema(description = "파일 물리 이름", hidden = true)
	private String file_sys_nm;

	@Schema(description = "파일 경로", hidden = true)
	private String file_path;

	@Schema(description = "파일 배열", hidden = true)
	private MultipartFile[] file;

	@Schema(description = "회원 타입", hidden = true)
	private String user_type;

	@Schema(description = "삭제할 파일 시퀀스 배열", hidden = true)
	private String[] del_file_seq;

	@Schema(description = "삭제할 파일 개수", hidden = true)
	private String del_file_count;

	@Schema(description = "작성자 이름", hidden = true)
	private String inpt_user_name;

	@Schema(description = "수정자 이름", hidden = true)
	private String upd_user_name;

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

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
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

	public int getrNum() {
		return rNum;
	}

	public void setrNum(int rNum) {
		this.rNum = rNum;
	}

	public String getExtensionPage() {
		return extensionPage;
	}

	public void setExtensionPage(String extensionPage) {
		this.extensionPage = extensionPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
