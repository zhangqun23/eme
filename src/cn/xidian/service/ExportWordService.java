package cn.xidian.service;

import java.util.List;

import cn.xidian.entity.CourseTeachingMode;
import cn.xidian.web.bean.WordCourseContent;

public interface ExportWordService {

	List<WordCourseContent> selectCourseContent(Integer cId);

}
