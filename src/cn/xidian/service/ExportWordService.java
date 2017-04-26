package cn.xidian.service;

import java.util.List;
import java.util.Map;

import cn.xidian.entity.CourseTeachingMode;
import cn.xidian.web.bean.WordCourseContent;

public interface ExportWordService {

	Map<String, String> selectCourseContent(Integer cId,String path,String modelPath,String cursName,String clazzName);

	Map<String, String> selectCourseContent0(Integer cId,String path,String modelPath,String cursName,String clazzName);

}
