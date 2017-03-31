package cn.xidian.web.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;

import cn.xidian.entity.CourseTeachingMode;
import cn.xidian.service.AdminCourseTeachingModeService;
import cn.xidian.service.ExportWordService;
import cn.xidian.web.bean.WordCourseContent;

@SuppressWarnings("serial")
@Component
public class WordAction extends ActionSupport implements RequestAware,SessionAware {

	private List<WordCourseContent> ctm;
	
	
	
	private Map<String, Object> request;
	private Map<String, Object> session;
	public Map<String, Object> getRequest() {
		return request;
	}
	@Override
	public void setRequest(Map<String, Object> request) {
		this.request=request;
		
	}

	public Map<String, Object> getSession() {
		return session;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session=session;		
	}	
	private ExportWordService exportWordService;
	@Resource(name = "exportWordServiceImpl")
	public void setExportWordService(
			ExportWordService exportWordService) {
		this.exportWordService = exportWordService;
	}
	public String selectCourseContent(){
		Integer cId = (Integer) session.get("cursId");
		if (cId!=null) {
			ctm = exportWordService.selectCourseContent(cId);
			return "teacher";
		}else {
			return "teachers";
		}
	}

	

}
