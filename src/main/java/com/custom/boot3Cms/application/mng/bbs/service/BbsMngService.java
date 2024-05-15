package com.custom.boot3Cms.application.mng.bbs.service;

import com.custom.boot3Cms.application.common.utils.CommonUtil;
import com.custom.boot3Cms.application.common.utils.StringUtil;
import com.custom.boot3Cms.application.mng.bbs.mapper.BbsMngMapper;
import com.custom.boot3Cms.application.mng.bbs.vo.BbsMngVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 게시판 관리 서비스
 * boot3Cms
 *
 * @author cms
 * @version 1.0
 * @see <pre>
 *  Modification Information
 *
 * 	수정일     / 수정자   / 수정내용
 * 	------------------------------------------
 * 	2024-05-12 / cms  / 최초 생성
 *
 * </pre>
 * @since 2024-05-12 */
@Transactional
@Service("bbsMngService")
public class BbsMngService{

	// 게시판 Mapper
	@Resource(name = "bbsMngMapper")
	private BbsMngMapper bbsMngMapper;

	/**
	 * 게시판 목록
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<BbsMngVO> getBbsList(BbsMngVO vo) throws Exception{
		return bbsMngMapper.getBbsList(vo);
	}

	/**
	 * 게시판 목록 (메뉴)
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<BbsMngVO> getBbsTitleList(String type) throws Exception{
		return bbsMngMapper.getBbsTitleList(type);
	}

	/**
	 * 게시판 코드 중복검사
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public boolean checkBbsCode(BbsMngVO vo) throws Exception{
		return bbsMngMapper.checkBbsCode(vo) == 0;
	}

	/**
	 * 게시판 정보 상세보기
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getBbsDetail(BbsMngVO vo) throws Exception{
		return CommonUtil.fn_getDetail(bbsMngMapper.getBbsDetail(vo));
	}

	/**
	 * 게시판 카테고리 정보
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<BbsMngVO> getBbsCategory(BbsMngVO vo) throws Exception{
		return bbsMngMapper.getBbsCategory(vo);
	}

	/**
	 * 게시판 파일 정보
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<BbsMngVO> getBbsFileInfo(BbsMngVO vo) throws Exception{
		return bbsMngMapper.getBbsFileInfo(vo);
	}

	/**
	 * 게시판 권한 정보
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<BbsMngVO> getBbsAuth(BbsMngVO vo) throws Exception{
		return bbsMngMapper.getBbsAuth(vo);
	}

	/**
	 * 게시판 등록
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> setBbs(BbsMngVO vo) throws Exception{
		Map<String, Object> map = new HashMap<>();
		String rMsg = "";
		boolean result = false;

		try{
			result = bbsMngMapper.setBbsInfo(vo) > 0;
			if(result){
				if(vo.getFile_cnt() == null || vo.getFile_cnt().equals("")){
					vo.setFile_cnt("0");
				}
				result = bbsMngMapper.setBbsDetail(vo) > 0;
				if(result){
					String[] authArr = vo.getUser_auth().split(",");
					String[] readArr = vo.getRead_yn().split(",");
					String[] writeArr = vo.getWrite_yn().split(",");
					String[] updateArr = vo.getUpdate_yn().split(",");
					String[] deleteArr = vo.getDelete_yn().split(",");
					for (int i = 0; i < authArr.length; i++) {
						vo.setUser_auth(authArr[i]);
						vo.setRead_yn(readArr[i]);
						vo.setWrite_yn(writeArr[i]);
						vo.setUpdate_yn(updateArr[i]);
						vo.setDelete_yn(deleteArr[i]);
						result = bbsMngMapper.setBbsAuth(vo) > 0;
						if(!result){
							break;
						}
					}
					if(result){
						if("Y".equals(vo.getAttach_file_use_yn()) && StringUtil.isNotEmpty(vo.getFile_type())){
							if(vo.getFile_type().contains(",")){
								String[] fileArr = vo.getFile_type().split(",");
								String[] sizeArr = vo.getFile_size().split(",");
								for (int i = 0; i < fileArr.length; i++) {
									vo.setFile_type(fileArr[i]);
									vo.setFile_size(sizeArr[i]);
									result = bbsMngMapper.setBbsFileInfo(vo) > 0;
									if(!result){
										break;
									}
								}
								if(!result){
									delBbsTemp("all", vo);
									rMsg = "첨부파일 정보 등록에 실패하였습니다.";
								}
							}else{
								result = bbsMngMapper.setBbsFileInfo(vo) > 0;
								if(!result){
									delBbsTemp("all", vo);
									rMsg = "첨부파일 정보 등록에 실패하였습니다.";
								}
							}
						}
						if("Y".equals(vo.getCategory_use_yn()) && StringUtil.isNotEmpty(vo.getCategory_nm())){
							if(vo.getCategory_nm().contains(",")){
								String[] categoryArr = vo.getCategory_nm().split(",");
								String[] orderArr = vo.getCategory_order().split(",");
								String[] cdArr = vo.getCategory_cd().split(",");
								boolean sqlFlag = true;
								for (int i = 0; i < categoryArr.length; i++) {
									vo.setCategory_nm(categoryArr[i]);
									vo.setCategory_order(orderArr[i]);
									vo.setCategory_cd(cdArr[i]);
									sqlFlag = bbsMngMapper.setBbsCategory(vo) > 0;
									if(!sqlFlag){
										break;
									}
								}
								result = sqlFlag;
								if(!result){
									delBbsTemp("all", vo);
									rMsg = "게시판 등록에 실패하였습니다.<br/>카테고리 정보 등록에 실패하였습니다.";
								}
							}else{
								result = bbsMngMapper.setBbsCategory(vo) > 0;
								if(!result){
									delBbsTemp("all", vo);
									rMsg = "게시판 등록에 실패하였습니다.<br/>카테고리 정보 등록에 실패하였습니다.";
								}
							}
						}

						if(result){
							rMsg = "게시판 등록이 완료되었습니다.";
						}
					}else{
						delBbsTemp("info", vo);
						rMsg = "게시판 등록에 실패하였습니다.<br/>게시판 권한정보 등록에 실패하였습니다.";
					}
				}else{
					delBbsTemp("info", vo);
					rMsg = "게시판 등록에 실패하였습니다.<br/>게시판 상세정보 등록에 실패하였습니다.";
				}
			}else{
				rMsg = "게시판 기본정보 등록 중 오류가 발생했습니다.";
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		map.put("rMsg", rMsg);
		map.put("result", result);
		return map;
	}

	/**
	 * 게시판 수정
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateBbs(BbsMngVO vo) throws Exception{
		Map<String, Object> map = new HashMap<>();
		String rMsg = "";
		boolean result = false;

		try{
			BbsMngVO tempBbsVO = bbsMngMapper.getBbsDetail(vo);
			List<BbsMngVO> tempFileList = bbsMngMapper.getBbsFileInfo(vo);
			List<BbsMngVO> tempAuthList = bbsMngMapper.getBbsAuth(vo);
			List<BbsMngVO> tempCategoryList = bbsMngMapper.getBbsCategory(vo);

			result = bbsMngMapper.updBbsInfo(vo) > 0;
			if(result){
				result = bbsMngMapper.updBbsDetail(vo) > 0;
				if(result){
					result = delBbsTemp("auth", vo);
					if(result){
						String[] authArr = vo.getUser_auth().split(",");
						String[] readArr = vo.getRead_yn().split(",");
						String[] writeArr = vo.getWrite_yn().split(",");
						String[] updateArr = vo.getUpdate_yn().split(",");
						String[] deleteArr = vo.getDelete_yn().split(",");
						for (int i = 0; i < authArr.length; i++) {
							vo.setUser_auth(authArr[i]);
							vo.setRead_yn(readArr[i]);
							vo.setWrite_yn(writeArr[i]);
							vo.setUpdate_yn(updateArr[i]);
							vo.setDelete_yn(deleteArr[i]);
							result = bbsMngMapper.setBbsAuth(vo) > 0;
							if(!result){
								break;
							}
						}
						if(result){
							if("Y".equals(vo.getAttach_file_use_yn()) && StringUtil.isNotEmpty(vo.getFile_type())){
								delBbsTemp("file", vo);
								if(vo.getFile_type().contains(",")){
									String[] fileArr = vo.getFile_type().split(",");
									String[] sizeArr = vo.getFile_size().split(",");
									for (int i = 0; i < fileArr.length; i++) {
										vo.setFile_type(fileArr[i]);
										vo.setFile_size(sizeArr[i]);
										result = bbsMngMapper.setBbsFileInfo(vo) > 0;
										if(!result){
											break;
										}
									}
									if(!result){
										delBbsTemp("file", vo);
										tempFileList.forEach(v->{
											try {
												bbsMngMapper.setBbsFileInfo(v);
											} catch (Exception e) {
												e.printStackTrace();
											}
										});
										delBbsTemp("auth", vo);
										tempAuthList.forEach(v->{
											try {
												bbsMngMapper.setBbsAuth(v);
											} catch (Exception e) {
												e.printStackTrace();
											}
										});
										result = updBbsTemp("detail", tempBbsVO);
										if(result){
											result = updBbsTemp("info", tempBbsVO);
											if(result){
												rMsg = "게시판 등록에 실패하였습니다.<br/>첨부파일정보 수정 중 오류가 발생했습니다.";
											}else{
												rMsg = "게시판 복원에 실패하였습니다.<br/>게시판 기본정보 복원 중 오류가 발생했습니다.";
											}
										}else{
											rMsg = "게시판 복원에 실패하였습니다.<br/>게시판 상세정보 복원 중 오류가 발생했습니다.";
										}
										result = false;
									}
								}else{
									result = bbsMngMapper.setBbsFileInfo(vo) > 0;
									if(!result){
										delBbsTemp("file", vo);
										if(tempFileList != null){
											tempFileList.forEach(v->{
												try {
													bbsMngMapper.setBbsFileInfo(v);
												} catch (Exception e) {
													e.printStackTrace();
												}
											});
										}
										delBbsTemp("category", vo);
										if(tempCategoryList != null){
											tempCategoryList.forEach(v->{
												try {
													bbsMngMapper.setBbsCategory(v);
												} catch (Exception e) {
													e.printStackTrace();
												}
											});
										}
										delBbsTemp("auth", vo);
										tempAuthList.forEach(v->{
											try {
												bbsMngMapper.setBbsAuth(v);
											} catch (Exception e) {
												e.printStackTrace();
											}
										});
										result = updBbsTemp("detail", tempBbsVO);
										if(result){
											result = updBbsTemp("info", tempBbsVO);
											if(result){
												rMsg = "게시판 수정에 실패하였습니다.<br/>첨부파일 정보 수정 중 오류가 발생했습니다.";
											}else{
												rMsg = "게시판 복원에 실패하였습니다.<br/>게시판 기본정보 복원 중 오류가 발생했습니다.";
											}
										}else{
											rMsg = "게시판 복원에 실패하였습니다.<br/>게시판 상세정보 복원 중 오류가 발생했습니다.";
										}
										result = false;
									}
								}
							}else{
								delBbsTemp("file", vo);
							}
							if("Y".equals(vo.getCategory_use_yn()) && StringUtil.isNotEmpty(vo.getCategory_nm())){
								delBbsTemp("category", vo);
								if(vo.getCategory_nm().contains(",")){
									String[] categoryArr = vo.getCategory_nm().split(",");
									String[] orderArr = vo.getCategory_order().split(",");
									String[] cdArr = vo.getCategory_cd().split(",");
									boolean sqlFlag = true;
									for (int i = 0; i < categoryArr.length; i++) {
										vo.setCategory_nm(categoryArr[i]);
										vo.setCategory_order(orderArr[i]);
										vo.setCategory_cd(cdArr[i]);
										sqlFlag = bbsMngMapper.setBbsCategory(vo) > 0;
										if(!sqlFlag){
											break;
										}
									}
									result = sqlFlag;
									if(!result){
										delBbsTemp("category", vo);
										if(tempCategoryList != null){
											tempCategoryList.forEach(v->{
												try {
													bbsMngMapper.setBbsCategory(v);
												} catch (Exception e) {
													e.printStackTrace();
												}
											});
										}
										delBbsTemp("file", vo);
										if(tempFileList != null){
											tempFileList.forEach(v->{
												try {
													bbsMngMapper.setBbsFileInfo(v);
												} catch (Exception e) {
													e.printStackTrace();
												}
											});
										}
										delBbsTemp("auth", vo);
										tempAuthList.forEach(v->{
											try {
												bbsMngMapper.setBbsAuth(v);
											} catch (Exception e) {
												e.printStackTrace();
											}
										});
										result = updBbsTemp("detail", tempBbsVO);
										if(result){
											result = updBbsTemp("info", tempBbsVO);
											if(result){
												rMsg = "게시판 수정에 실패하였습니다.<br/>카테고리 정보 수정 중 오류가 발생했습니다.";
											}else{
												rMsg = "게시판 복원에 실패하였습니다.<br/>게시판 기본정보 복원 중 오류가 발생했습니다.";
											}
										}else{
											rMsg = "게시판 복원에 실패하였습니다.<br/>게시판 상세정보 복원 중 오류가 발생했습니다.";
										}
										result = false;
									}
								}else{
									result = bbsMngMapper.setBbsCategory(vo) > 0;
									if(!result){
										delBbsTemp("all", vo);
										rMsg = "게시판 등록에 실패하였습니다.<br/>카테고리 정보 등록에 실패하였습니다.";
									}
								}
							}else{
								delBbsTemp("category", vo);
							}

							if(result){
								rMsg = "게시판 수정이 완료되었습니다.";
							}
						}else{
							delBbsTemp("auth", vo);
							tempAuthList.forEach(v->{
								try {
									bbsMngMapper.setBbsAuth(v);
								} catch (Exception e) {
									e.printStackTrace();
								}
							});
							result = updBbsTemp("detail", tempBbsVO);
							if(result){
								result = updBbsTemp("info", tempBbsVO);
								if(result){
									rMsg = "게시판 수정에 실패하였습니다.<br/>게시판 권한정보 수정 중 오류가 발생했습니다.";
								}else{
									rMsg = "게시판 복원에 실패하였습니다.<br/>게시판 기본정보 복원 중 오류가 발생했습니다.";
								}
							}else{
								rMsg = "게시판 복원에 실패하였습니다.<br/>게시판 상세정보 복원 중 오류가 발생했습니다.";
							}
							result = false;
						}
					}else{
						result = updBbsTemp("detail", tempBbsVO);
						if(result){
							result = updBbsTemp("info", tempBbsVO);
							if(result){
								rMsg = "게시판 수정에 실패하였습니다.<br/>게시판 권한정보 삭제 중 오류가 발생했습니다.";
							}else{
								rMsg = "게시판 복원에 실패하였습니다.<br/>게시판 기본정보 복원 중 오류가 발생했습니다.";
							}
						}else{
							rMsg = "게시판 복원에 실패하였습니다.<br/>게시판 상세정보 복원 중 오류가 발생했습니다.";
						}
						result = false;
					}
				}else{
					result = updBbsTemp("info", tempBbsVO);
					if(result){
						rMsg = "게시판 상세정보 수정 중 오류가 발생했습니다.";
					}else{
						rMsg = "게시판 기본정보 복원 중 오류가 발생했습니다.";
					}
					result = false;
				}
			}else{
				rMsg = "게시판 기본정보 수정 중 오류가 발생했습니다.";
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		map.put("rMsg", rMsg);
		map.put("result", result);
		return map;
	}

	/**
	 * 게시판 삭제 ( 논리 삭제 )
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> deleteBbs(BbsMngVO vo) throws Exception{
		Map<String, Object> map = new HashMap<>();
		String rMsg = "";
		boolean result = false;

		result = bbsMngMapper.delBbs(vo) > 0;

		rMsg = result ? "게시판 삭제에 성공하였습니다." : "게시판 삭제에 실패하였습니다.";

		map.put("rMsg", rMsg);
		map.put("result", result);
		return map;
	}

	/**
	 * 상태값과 VO를 인자로 받아 삭제처리 실시
	 * @param flag
	 * @param vo
	 */
	private boolean delBbsTemp(String flag, BbsMngVO vo) throws Exception{
		boolean result = false;
		switch (flag){
			case "all" :
				bbsMngMapper.delBbsFileInfo(vo);
				bbsMngMapper.delBbsCategory(vo);
				bbsMngMapper.delBbsAuth(vo);
				bbsMngMapper.delBbsDetail(vo);
				bbsMngMapper.delBbsHtml(vo);
				result = bbsMngMapper.delBbsInfo(vo) > 0;
				break;
			case "file" :
				result = bbsMngMapper.delBbsFileInfo(vo) > 0;
				break;
			case "auth" :
				result = bbsMngMapper.delBbsAuth(vo) > 0;
				break;
			case "category" :
				result = bbsMngMapper.delBbsCategory(vo) > 0;
				break;
			case "detail" :
				result = bbsMngMapper.delBbsDetail(vo) > 0;
				break;
			case "info" :
				result = bbsMngMapper.delBbsInfo(vo) > 0;
				break;
			case "html" :
				result = bbsMngMapper.delBbsHtml(vo) > 0;
				break;
		}
		return result;
	}

	/**
	 * 상태값과 VO를 인자로 받아 수정처리 실시
	 * @param flag
	 * @param vo
	 */
	private boolean updBbsTemp(String flag, BbsMngVO vo) throws Exception{
		boolean result = false;
		switch (flag){
			case "detail" :
				result = bbsMngMapper.delBbsDetail(vo) > 0;
				break;
			case "info" :
				result = bbsMngMapper.delBbsInfo(vo) > 0;
				break;
		}
		return result;
	}

	/**
	 * BBS_INFO_SEQ로 게시판 존재여부 확인
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String getBbsCnt(BbsMngVO vo) throws Exception {
		return bbsMngMapper.getBbsCnt(vo);
	}


}

