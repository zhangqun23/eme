package cn.xidian.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import cn.xidian.entity.AverClazzCoursePoint;
import cn.xidian.entity.AverTeachingTargetEvaluate;
import cn.xidian.entity.ClazzCoursePoint;
import cn.xidian.entity.CourseTeachingMode;
import cn.xidian.entity.TeachingTarget;
import cn.xidian.entity.TeachingTargetEvaluate;
import cn.xidian.service.AdminCourseTeachingModeService;
import cn.xidian.service.ClazzCoursePointService;
import cn.xidian.service.ExportWordService;
import cn.xidian.service.TeachingTargetEvaluateService;
import cn.xidian.service.TeachingTargetService;
import cn.xidian.utils.FileHelper;
import cn.xidian.utils.WordHelper;
import cn.xidian.web.bean.WordCourseContent;
import cn.xidian.web.bean.WordCourseContent0;
import cn.xidian.web.bean.WordCourseEvaluate;
import cn.xidian.web.bean.WordCourseTarget;
import cn.xidian.web.service.CourseTargetDetailService;
import cn.xidian.web.service.WordCourseTargetService;

@Component
public class ExportWordServiceImpl implements ExportWordService {
	private List<CourseTeachingMode> ctm;

	private AdminCourseTeachingModeService adminCourseTeachingModeService;// 引用AdminCourseTeachingModeService

	@Resource(name = "adminCourseTeachingModeServiceImpl")
	public void setAdminCourseTeachingModeService(AdminCourseTeachingModeService adminCourseTeachingModeService) {
		this.adminCourseTeachingModeService = adminCourseTeachingModeService;
	}
	TeachingTargetService teachingTargetService;

	@Resource(name = "teachingTargetServiceImpl")
	public void setTeachingTargetService(TeachingTargetService teachingTargetService) {
		this.teachingTargetService = teachingTargetService;
	}
	
	ClazzCoursePointService clazzCoursePointService;

	@Resource(name = "clazzCoursePointServiceImpl")
	public void setClazzCoursePointService(ClazzCoursePointService clazzCoursePointService) {
		this.clazzCoursePointService = clazzCoursePointService;
	}

	TeachingTargetEvaluateService teachingTargetEvaluateService;

	@Resource(name = "teachingTargetEvaluateServiceImpl")
	public void setTeachingTargetEvaluateService(TeachingTargetEvaluateService teachingTargetEvaluateService) {
		this.teachingTargetEvaluateService = teachingTargetEvaluateService;
	}
	
	//课程教学目标达成度评价表（表一）
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, String> selectCourseContent(Integer cId,String path,String modelPath,String cursName,String clazzName){
		WordHelper wh = new WordHelper();
		//List<WordCourseContent> listSource=selectCourseContent0(cId);
		List<WordCourseTarget> listSourse1=selectCourseContent1(cursName,clazzName);//表一
		List<WordCourseEvaluate> listSourse2=selectCourseContent0(cursName,clazzName);//表二
		
		Map<String, Object> listMap = new HashMap<String, Object>();// 多个实体list放到Map中，在WordHelper中解析
		Map<String, Object> contentMap = new HashMap<String, Object>();// 获取文本数据
	
		
		Map<String, String> map=new HashMap<String, String>();
		if (listSourse1 != null || listSourse2 != null) {
			String fileName = "课程教学达成度评价表.docx";
			String path0 = FileHelper.transPath(fileName, path);// 解析后的上传路径
			
			//listMap.put("0", listSource);//用于导出课程内容实施进度
			listMap.put("0", listSourse1);
			listMap.put("1", listSourse2);
			contentMap.put("${photo0}", clazzName);
			contentMap.put("${photo1}", cursName);

			try {
				OutputStream out = new FileOutputStream(path0);// 保存路径
				wh.export2007Word(modelPath, listMap, contentMap, 2, out, -1);
				//wh.export2007Word(path0, listMap, null, 2, out, -1);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			map.put("fileName", fileName);
			map.put("path0", path0);	
		}
		return map;
	}
	public List<WordCourseContent> selectCourseContent0(Integer cId) {
		List<WordCourseContent> listGoal = new ArrayList<WordCourseContent>();
		ctm = adminCourseTeachingModeService.selectTchingModeByCursId(cId);
		Iterator<CourseTeachingMode> it = ctm.iterator();
		int i = 0;
		String string;
		Integer integer;// 学期
		Map<String, String> map = new HashMap<String, String>();
		WordCourseContent0 wordCourseContent0 = new WordCourseContent0();
		WordCourseContent wordCourseContent = null;
		while (it.hasNext()) {			
			
			CourseTeachingMode courseTeachingMode = it.next();

			if (i++ == 0) {
				wordCourseContent = new WordCourseContent();
				wordCourseContent.setContent(courseTeachingMode.getCursContent());
				wordCourseContent0.setContent(courseTeachingMode.getCursContent());
				map.put("period", courseTeachingMode.getPeriod());		
			} else {
				string = wordCourseContent.getContent() + "," + courseTeachingMode.getCursContent();
				wordCourseContent = new WordCourseContent();
				wordCourseContent.setContent(string);
				wordCourseContent0.setContent(courseTeachingMode.getCursContent());
				Integer inn = Integer.valueOf(map.get("period")) + Integer.valueOf(courseTeachingMode.getPeriod());
				map.put("period", inn.toString());
				String stt=map.get("period");
				i++;
			}
			String stt=map.get("period");
			System.out.println("就是爱你"+map.get("period"));
			if (Integer.valueOf(map.get("period")) < 2) {
				i++;
				continue;
			} else {
				wordCourseContent.setContent(wordCourseContent.getContent());
				listGoal.add(wordCourseContent);
				integer=Integer.valueOf(map.get("period"))-2;
				map.put("period", integer.toString());
				if(map.get("period").equals("0")){
					i=0;
					continue;
				}
				else if (Integer.valueOf(map.get("period"))>0 && Integer.valueOf(map.get("period"))< 2) {
					wordCourseContent.setContent(wordCourseContent0.getContent());
					i++;
				} while(Integer.valueOf(map.get("period"))>=2){
					wordCourseContent = new WordCourseContent();
					wordCourseContent.setContent(wordCourseContent0.getContent());
					listGoal.add(wordCourseContent);
					integer=Integer.valueOf(map.get("period"))-2;
					map.put("period", integer.toString());
					if (integer==0) {
						i=0;
						continue;
					}
					i++;
				}
			}
		}
		Iterator<WordCourseContent> itt=listGoal.iterator();
		int j=1;
		while(itt.hasNext()){
			wordCourseContent=itt.next();
			wordCourseContent.setId(j);
			System.out.println(wordCourseContent.getId());
			j++;
			
		}
		return listGoal;
	}

	public List<WordCourseTarget> selectCourseContent1(String cursName,String clazzName){
		WordCourseTargetService wordCourseTargetService=new WordCourseTargetService();
		if(clazzName.length()>6){
			List<TeachingTarget> targets = teachingTargetService.selectByCursName(cursName);
			List<TeachingTargetEvaluate> targetValues = teachingTargetEvaluateService.selectByCursAndClazz(cursName,
					clazzName);
			List<WordCourseTarget> listSourse1 = wordCourseTargetService.getB1(targets, targetValues);
			return listSourse1;
		}else{
			clazzName = clazzName.substring(0, 4);			
			List<TeachingTarget> targets = teachingTargetService.selectByCursName(cursName);
			List<AverTeachingTargetEvaluate> atargetValues = teachingTargetEvaluateService.selectByGradeAndClazz(cursName,
					clazzName);
			List<WordCourseTarget> listSourse1 =wordCourseTargetService.getAB1(targets, atargetValues);
			return listSourse1;	
		}
		
	}
	
	public List<WordCourseEvaluate> selectCourseContent0(String cursName,String clazzName){
		WordCourseTargetService wordCourseTargetService=new WordCourseTargetService();	
		if(clazzName.length()>6){
			List<ClazzCoursePoint> ccPoints = clazzCoursePointService.selectByCursAndClazz(cursName, clazzName);
			List<WordCourseEvaluate> listSourse=wordCourseTargetService.getB2(ccPoints);
			return listSourse;
		}else {
			clazzName = clazzName.substring(0, 4);			
			List<AverClazzCoursePoint> accPoints = clazzCoursePointService.selectByCursAndGrade(cursName, clazzName);
			List<WordCourseEvaluate> listSourse=wordCourseTargetService.getAB2(accPoints);
			//course = courseService.findByName(cursName);
			return listSourse;
			
		}
		
	}
	//课程对毕业要求达成度评价表(表二)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, String> selectCourseContent0(Integer cId, String path, String modelPath, String cursName,
			String clazzName) {
		WordHelper wh = new WordHelper();
		List<WordCourseEvaluate> listSourse=selectCourseContent0(cursName,clazzName);
		
		Map<String, Object> listMap = new HashMap<String, Object>();// 多个实体list放到Map中，在WordHelper中解析
		Map<String, Object> contentMap = new HashMap<String, Object>();// 获取文本数据
	
		
		Map<String, String> map=new HashMap<String, String>();
		if (listSourse != null) {
			String fileName = "课程对毕业要求达成度评价表.docx";
			String path0 = FileHelper.transPath(fileName, path);// 解析后的上传路径
			
			//listMap.put("0", listSource);//用于导出课程内容实施进度
			listMap.put("0", listSourse);
			listMap.put("1", listSourse);
			contentMap.put("${photo0}", clazzName);
			contentMap.put("${photo1}", cursName);

			try {
				OutputStream out = new FileOutputStream(path0);// 保存路径
				wh.export2007Word(modelPath, listMap, contentMap, 2, out, -1);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			map.put("fileName", fileName);
			map.put("path0", path0);	
		}
		return map;
	}

}
