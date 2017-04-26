package cn.xidian.service;


import java.util.List;
import cn.xidian.entity.PageBean;
import cn.xidian.entity.StuEvaluateResult;
import cn.xidian.entity.Student;
import cn.xidian.entity.StudentCourse;
import cn.xidian.entity.TeachingTargetEvaluate;
import cn.xidian.web.bean.B1;
import cn.xidian.web.bean.B2;

public interface StudentService {

	boolean loginValidate(String stuSchNum, String stuPwd);

	Student selectInfBySchNum(String stuSchNum);

	boolean updateInfBySchNum(Student student);
	
	boolean updateGoal(Student student);
	
	boolean modifyPassword(String schNum,String pwd);

	PageBean<StudentCourse> selectStuAllGradesById(Integer id,Integer page);
	
	List<StuEvaluateResult> selectStuEvaluateResults(Integer stuId,String schoolYear);

	List<TeachingTargetEvaluate> caculateBySCourse(StudentCourse sCourse);

	List<B2> getB2(StudentCourse sCourse, List<B1> claCursB1s);

	StudentCourse getSCourse(int stuCursId);

	
}