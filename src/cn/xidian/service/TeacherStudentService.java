package cn.xidian.service;

import java.util.List;
import java.util.Set;

import cn.xidian.entity.Clazz;
import cn.xidian.web.bean.EvaluateResult;
import cn.xidian.entity.PageBean;
import cn.xidian.entity.StuEvaluateResult;
import cn.xidian.entity.Student;
import cn.xidian.entity.StudentCourse;
import cn.xidian.entity.SurveyQuestion;
import cn.xidian.web.bean.AdminStuLimits;

public interface TeacherStudentService {

	List<Clazz> findChargeCla(Integer id);

	List<Student> selectChargeStus(Integer id);

	Set<Student> selectStuLimits(AdminStuLimits limits, List<Clazz> clazzs);

	Clazz selectClazzById(Integer id);

	List<StudentCourse> selectStuGrades(Integer stuId, String schoolYear);

	List<StuEvaluateResult> selectMaxEva(Integer i, String schoolYear);

	// 留下这个地方用EvaluateResult到时若type改变就要修改
	PageBean<EvaluateResult> findByPageCid(Integer claId, String schoolYear, Integer page);

	PageBean<StudentCourse> selectStuGradesByPage(Integer stuId, String schoolYear, Integer page);

	boolean addStuEvaScore(StuEvaluateResult stuEvaluateResult);

	List<StuEvaluateResult> selectSummaryStuEvas(Integer claId, String schoolYear);

	List<StuEvaluateResult> findStuEvaByPageCid(Integer itemEvaTypeId, Integer claId, String schoolYear);

	boolean deleteStuEvas(Integer claId, String schoolYear);
}
