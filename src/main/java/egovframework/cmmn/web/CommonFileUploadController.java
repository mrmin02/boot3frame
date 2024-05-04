package egovframework.cmmn.web;

import egovframework.cmmn.utils.EgovFormBasedFileUtil;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
public class CommonFileUploadController {

	@Autowired
	Environment env;

	/** 첨부파일 위치 지정 */
	@Value("${file.DefaultPath}")
	private String defaultPath;

	@Value("${file.DefaultSubPath}")
	private String defaultSubPath;

	/** 첨부 최대 파일 크기 지정 */
	private long maxFileSizeMB = 20;   //업로드 최대 사이즈 설정 (20M)
	private long maxFileSize = 1024 * 1024 * maxFileSizeMB;

	@Autowired
	private ServletContext servletContext;

	/**
	 * 공통 Upload 화면으로 이동한다.
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/cmmn/fileUpload", method=RequestMethod.GET)
	public String fileUpload(
			Model model
			, HttpServletRequest request
	)  {

		String callBackFnc = request.getParameter("callBackFnc");
		String fileType = request.getParameter("fileType");
		String fileDir = request.getParameter("fileDir");

		model.addAttribute("fileDir", fileDir);
		model.addAttribute("fileType", fileType);
		model.addAttribute("callBackFnc", callBackFnc);

		return "/cmmn/fileUpload";
	}

	@RequestMapping(value = "/file/deleteFile")
	public @ResponseBody boolean deleteFile(HttpServletRequest request)  {
		return systemFileDelete(request.getParameter("filePath"), request.getParameter("sysFileName"));
	}

	public boolean systemFileDelete(String filePath, String sysFileName) {
		String s = defaultPath+File.separator+filePath+File.separator+sysFileName;
		String thumb = defaultPath+File.separator+filePath+File.separator+"THUMB_"+sysFileName;

		File f = new File(s);

		File thumbImage = new File(thumb);

		thumbImage.delete();

		return f.delete();
	}

	/**
	 * 이미지 view를 제공한다.
	 *
	 * @param request
	 * @param response
	 * @throws Exception,
	 */
	@RequestMapping({"/cmmn/fileView"})
	public void fileView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean isMobile = false;
		if (request.getHeader("referer") != null) {
			isMobile = request.getHeader("referer").indexOf("/m/") >= 0;
		}
		String subPath = request.getParameter("path");
		String physical = request.getParameter("physical");
		String mimeType = request.getParameter("contentType");

		EgovFormBasedFileUtil.viewFile(response, defaultPath, subPath, physical, mimeType);
	}
	@RequestMapping({"/common/fullPathFileView"})
	public void fullPathImg(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String subPath = request.getParameter("path");

		EgovFormBasedFileUtil.fullPathViewFile(response, defaultPath, subPath);
	}

	public static String toJsonString(Object bean){
		Map map = null;
		try {
			map = BeanUtils.describe(bean);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.putAll(map);

		String jsonObjStr = jsonObject.toString();
		return jsonObjStr;
	}

	@RequestMapping(value = "/file/ckeditorFileupload")
	public void ckeditorFileUpload(	HttpServletRequest request,
									   HttpServletResponse response,
									   MultipartHttpServletRequest req) throws IOException {
		Iterator fileIter = req.getFileNames();

		InputStream in = null;
		OutputStream out = null;

		HashMap<String, String> map = new HashMap<String, String>();
		String ckeditorPath = env.getProperty("Globals.file.CkeditorPath");
		String procPath = request.getParameter("path");
		String fileDir = env.getProperty("Globals.file.DefaultPath") + ckeditorPath + "/" + procPath + "/";
		while (fileIter.hasNext()) {
			try {
				MultipartFile mFile = req.getFile((String) fileIter.next());

				String originalFileName = mFile.getOriginalFilename();

				if (originalFileName.lastIndexOf("\\") >= 0) {
					originalFileName = originalFileName.substring(originalFileName.lastIndexOf("\\") + 1);
				}

				String targetFileName = new StringBuffer().append(System.currentTimeMillis() )
						.append(".")
						.append( originalFileName.substring(originalFileName.lastIndexOf(".") + 1,originalFileName.length())).toString();

				// 폴더 생성
				File targetPathDir = new File(fileDir);
				if (!targetPathDir.isDirectory()){
					targetPathDir.mkdirs();
				}

				String savedFilePath = fileDir+targetFileName;

				try {
					in = mFile.getInputStream();
					out = new FileOutputStream(savedFilePath);

					int readBytes = 0;
					byte[] buff = new byte[8192];

					while ((readBytes = in.read(buff, 0, 8192)) != -1) {
						out.write(buff, 0, readBytes);
					}
				} finally {
					if (in != null)
						in.close();
					if (out != null)
						out.close();
				}


				map.put("orgFileName", originalFileName); // 실제 파일명
				map.put("sysFileName", targetFileName);   // 저장된 파일명
				map.put("defaultFilePath", fileDir);	  // 기본경로
				map.put("subPath", defaultSubPath);	  // 풀경로
				map.put("fileSize", String.valueOf(mFile.getSize()));
				map.put("fileType", mFile.getContentType());

				String funcNum = request.getParameter("CKEditorFuncNum");
				//String fileUrl = savedFilePath;

				// TODO 테스트서버 URL
				String tmpFileUrl = "/cmmn/fileView?path="+ckeditorPath+"/"+procPath+"&physical="+targetFileName+"&contentType=image";
				// TODO 실서버 URL
				/*String tmpFileUrl = "/data"+ckeditorPath+"/"+procPath+"/"+targetFileName;*/

				response.setContentType("text/html; charset=utf-8");
				PrintWriter print;
				try {
					print = response.getWriter();
					print.println("<script>window.parent.CKEDITOR.tools.callFunction(" + funcNum + ", '" + tmpFileUrl + "', '파일 업로드가 완료되었습니다.');</script>");
				} catch (IOException e) {
					print = response.getWriter();
					print.println("<script>window.parent.CKEDITOR.tools.callFunction('', '', '파일 업로드에 실패하였습니다.');</script>");
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 리뉴얼 이전 Editor 업로드 이미지를 위한 컨트롤러
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value={"/Uploads/SmartEditor/**"})
	public void editorOldFileView(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String reqPath = Arrays.stream(new URL(request.getRequestURL().toString()).getPath().split("/"))
				.map(it -> {
					try {
						return URLDecoder.decode(it, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				})
				.collect(Collectors.joining("/"));

		String path = "/old_files/"+ reqPath.replace("/Uploads/", "").replaceAll("\\.[\\.]+", "");
		String dir = Paths.get(path).getParent().toString();
		String fileNm = Paths.get(path).getFileName().toString();
		String mimeType = URLConnection.guessContentTypeFromName(fileNm);

		EgovFormBasedFileUtil.viewFile(response, defaultPath, dir, fileNm, mimeType);
	}

}
