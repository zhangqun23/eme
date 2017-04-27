package cn.xidian.web.action;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;

import cn.xidian.service.ExportWordService;
import cn.xidian.web.bean.WordCourseContent;

@SuppressWarnings("serial")
@Component(value = "WordAction")
@Scope("prototype")
public class WordAction extends ActionSupport implements RequestAware, SessionAware {

	private List<WordCourseContent> ctm;
	private String ctm0;

	private Map<String, Object> request;
	private Map<String, Object> session;
	// 文件下载（导出word）
	private InputStream fileInput;
	private String fileName;
	private String saveFileName;
	// 课程教学目标达成度评价表（导出word）
	private String cursName;
	private String clazzName;

	private ExportWordService exportWordService;

	@Resource(name = "exportWordServiceImpl")
	public void setExportWordService(ExportWordService exportWordService) {
		this.exportWordService = exportWordService;
	}

	public String selectCourseContent() throws UnsupportedEncodingException {
		Integer cId = (Integer) session.get("cursId");
		String path = ServletActionContext.getServletContext().getRealPath(constants.ReportFormConstants.SAVE_PATH);// 上传服务器的路径
		
		Map<String, String> map = new HashMap<String, String>();
		// cursName = session.get("cursName").toString();
		cursName = new String(cursName.getBytes("ISO8859-1"), "utf-8");
		clazzName = new String(clazzName.getBytes("ISO8859-1"), "utf-8");
		String modelPath = null ;
		if(clazzName.length()>6){
			modelPath = ServletActionContext.getServletContext()
					.getRealPath(constants.ReportFormConstants.EXPORTWORD110_PATH);// 模板路径
		}else{
			modelPath = ServletActionContext.getServletContext()
					.getRealPath(constants.ReportFormConstants.EXPORTWORD111_PATH);// 模板路径
		}
		
		map = exportWordService.selectCourseContent(39, path, modelPath, cursName, clazzName);
		String fileName0 = map.get("fileName");
		// fileName= new String(fileName0.getBytes("ISO8859-1"), "utf-8");
		fileName = new String(fileName0.getBytes("GB2312"), "ISO8859-1");// 避免文件名中文不显示
		String path0 = map.get("path0");
		try {
			fileInput = new FileInputStream(path0);
			request.put("Message", "下载成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "word";
		// if (cId!=null) {
		// ctm0= exportWordService.selectCourseContent(39,path,modelPath);
		// return "word";
		// }else {
		// return "words";
		// }
	}


	public InputStream getFileInput() {
		return fileInput;
	}

	public void setFileInput(InputStream fileInput) {
		this.fileInput = fileInput;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSaveFileName() {
		return saveFileName;
	}

	public void setSaveFileName(String saveFileName) {
		this.saveFileName = saveFileName;
	}

	public Map<String, Object> getRequest() {
		return request;
	}

	@Override
	public void setRequest(Map<String, Object> request) {
		this.request = request;

	}

	public Map<String, Object> getSession() {
		return session;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getCursName() {
		return cursName;
	}

	public void setCursName(String cursName) {
		this.cursName = cursName;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}
}
