package cn.xidian.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.stereotype.Component;


import cn.xidian.entity.CourseTeachingMode;
import cn.xidian.service.AdminCourseTeachingModeService;
import cn.xidian.service.ExportWordService;
import cn.xidian.web.bean.WordCourseContent;
import cn.xidian.web.bean.WordCourseContent0;

@Component
public class ExportWordServiceImpl implements ExportWordService {
	private List<CourseTeachingMode> ctm;
	
	private AdminCourseTeachingModeService adminCourseTeachingModeService;//引用AdminCourseTeachingModeService
	@Resource(name = "adminCourseTeachingModeServiceImpl")
	public void setAdminCourseTeachingModeService(
			AdminCourseTeachingModeService adminCourseTeachingModeService) {
		this.adminCourseTeachingModeService = adminCourseTeachingModeService;
	}
	@SuppressWarnings("null")
	@Override
	public List<WordCourseContent> selectCourseContent(Integer cId) {
		List<WordCourseContent> listGoal = new ArrayList<WordCourseContent>();
		ctm = adminCourseTeachingModeService.selectTchingModeByCursId(cId);
		Iterator<CourseTeachingMode> it=ctm.iterator();
		int i=0;
		String string;
		Integer integer;//学期
		Integer period = null;//学期差
		Integer sub;//差值
		Map<String, String> map=new HashMap<String, String>();
		while(it.hasNext()){
			WordCourseContent wordCourseContent0 = null;
			WordCourseContent wordCourseContent = null;
			CourseTeachingMode courseTeachingMode=it.next();
			
			if (i++==0) {
				Instantiation(courseTeachingMode.getCursContent(), courseTeachingMode.getPeriod());
				Instantiation0(courseTeachingMode.getCursContent(), courseTeachingMode.getPeriod());
				map.put("period", courseTeachingMode.getPeriod());
			}else{
				string=wordCourseContent.getContent()+","+courseTeachingMode.getCursContent();
				Instantiation(string, courseTeachingMode.getPeriod());
				Instantiation0(courseTeachingMode.getCursContent(), courseTeachingMode.getPeriod());
				Integer inn=Integer.valueOf(map.get("period"))+Integer.valueOf(courseTeachingMode.getPeriod());
				map.put("period", inn.toString());
				i++;
			}
			if (Integer.valueOf(map.get("period"))<2) {
				i++;
				continue;
			}else {
				Instantiation(wordCourseContent.getContent(), map.get("period"));
				listGoal.add(wordCourseContent);
				map.put("period",Integer.valueOf(map.get("period")).toString());
				if (Integer.valueOf(map.get("period"))-2<2) {
					Instantiation(wordCourseContent0.getContent(), wordCourseContent0.getPeriod());
					Instantiation0(courseTeachingMode.getCursContent(), courseTeachingMode.getPeriod());
					Integer inn=Integer.valueOf(map.get("period"))+Integer.valueOf(courseTeachingMode.getPeriod());
					map.put("period", inn.toString());
					i++;
				}else{
					Instantiation(wordCourseContent0.getContent(), wordCourseContent0.getPeriod());
					listGoal.add(wordCourseContent0);
					map.put("period",Integer.valueOf(map.get("period")).toString());
					i++;
				}
			}			
		}
		return listGoal;
	}
	//实例化0
	private void Instantiation0(String content,String period){
		WordCourseContent0 wordCourseContent0=new WordCourseContent0();		
		wordCourseContent0.setContent(content);
		wordCourseContent0.setPeriod(period);
	}
	//实例化
		private void Instantiation(String content,String period){
			WordCourseContent wordCourseContent=new WordCourseContent();		
			wordCourseContent.setContent(content);
			wordCourseContent.setPeriod(period);
		}

}
