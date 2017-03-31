package cn.xidian.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import cn.xidian.dao.StudentItemDao;
import cn.xidian.dao.TeacherStudentDao;
import cn.xidian.entity.Clazz;
import cn.xidian.web.bean.EvaluateResult;
import cn.xidian.entity.ItemEvaluateType;
import cn.xidian.entity.PageBean;
import cn.xidian.entity.StuEvaluateResult;
import cn.xidian.entity.Student;
import cn.xidian.entity.StudentCourse;
import cn.xidian.service.TeacherStudentService;
import cn.xidian.utils.PageUtils;
import cn.xidian.web.bean.AdminStuLimits;

@Component
public class TeacherStudentServiceImpl implements TeacherStudentService {

	private TeacherStudentDao teacherStudentDao;

	@Resource(name = "teacherStudentDaoImpl")
	public void teacherStudentDao(TeacherStudentDao teacherStudentDao) {
		this.teacherStudentDao = teacherStudentDao;
	}

	private StudentItemDao studentItemDao;

	@Resource(name = "studentItemDaoImpl")
	public void setStudentItemDao(StudentItemDao studentItemDao) {
		this.studentItemDao = studentItemDao;
	}

	@Override
	public List<Clazz> findChargeCla(Integer id) {
		// TODO Auto-generated method stub
		return teacherStudentDao.selectChargeCla(id);
	}

	@Override
	public List<Student> selectChargeStus(Integer id) {
		// TODO Auto-generated method stub
		return teacherStudentDao.selectChargeStus(id);
	}

	@Override
	public Set<Student> selectStuLimits(AdminStuLimits limits, List<Clazz> clazzs) {
		// TODO Auto-generated method stub
		return teacherStudentDao.selectStuLimits(limits, clazzs);
	}

	@Override
	public Clazz selectClazzById(Integer id) {
		// TODO Auto-generated method stub
		return teacherStudentDao.selectClazzById(id);
	}

	@Override
	public List<StudentCourse> selectStuGrades(Integer stuId, String schoolYear) {
		// TODO Auto-generated method stub
		return teacherStudentDao.selectStuGrades(stuId, schoolYear);
	}

	@Override
	public PageBean<EvaluateResult> findByPageCid(Integer claId, String schoolYear, Integer page) {
		// TODO Auto-generated method stub
		PageBean<EvaluateResult> pageBean = new PageBean<EvaluateResult>();
		int totalCount = 0;
		int typeNum = 0;
		List<ItemEvaluateType> itemEvaluateTypes = studentItemDao.selectItemEvaTypes();
		typeNum = itemEvaluateTypes.size();// 有多少种类型
		totalCount = teacherStudentDao.selectSummaryStuEvas(claId, schoolYear).size();// 计算应该有多少个stuEvaluateresult
		pageBean = PageUtils.page(page, totalCount / typeNum, 20);
		List<EvaluateResult> evaluateResults = new LinkedList<EvaluateResult>();
		for (int i = 0; i < typeNum; i++) {
			List<StuEvaluateResult> stuEvaluateResults = teacherStudentDao.findStuEvaByPageCid(itemEvaluateTypes.get(i).getItemEvaTypeId(), claId, schoolYear, pageBean.getBegin(), pageBean.getLimit());
			switch (i) {
			case 0:
				for (int j = 0; j < stuEvaluateResults.size(); j++) {
					EvaluateResult e = new EvaluateResult();
					e.setStudent(stuEvaluateResults.get(j).getStudent());
					e.setClazz(stuEvaluateResults.get(j).getClazz());
					e.setM1(stuEvaluateResults.get(j).getmScore());
					e.setSchoolYear(stuEvaluateResults.get(j).getSchoolYear());
					evaluateResults.add(e);
				}
				break;
			case 1:
				for (int k = 0; k < stuEvaluateResults.size(); k++) {
					evaluateResults.get(k).setM2(stuEvaluateResults.get(k).getmScore());
				}
				break;
			case 2:
				for (int k = 0; k < stuEvaluateResults.size(); k++) {
					evaluateResults.get(k).setM3(stuEvaluateResults.get(k).getmScore());
				}
				break;
			case 3:
				for (int k = 0; k < stuEvaluateResults.size(); k++) {
					evaluateResults.get(k).setM4(stuEvaluateResults.get(k).getmScore());
				}
				break;
			case 4:
				for (int k = 0; k < stuEvaluateResults.size(); k++) {
					evaluateResults.get(k).setM5(stuEvaluateResults.get(k).getmScore());
				}
				break;
			}

		}
		pageBean.setList(evaluateResults);
		return pageBean;

	}

	@Override
	public PageBean<StudentCourse> selectStuGradesByPage(Integer stuId, String schoolYear, Integer page) {
		// TODO Auto-generated method stub
		PageBean<StudentCourse> pageBean = new PageBean<StudentCourse>();
		List<StudentCourse> studentCourses = teacherStudentDao.selectStuGrades(stuId, schoolYear);
		pageBean = PageUtils.page(page, studentCourses.size(), 15);
		List<StudentCourse> list = teacherStudentDao.findStuGradesByPage(stuId, schoolYear, pageBean.getBegin(),
				pageBean.getLimit());
		pageBean.setList(list);
		return pageBean;
	}

	@Override
	public boolean addStuEvaScore(StuEvaluateResult stuEvaluateResult) {
		// TODO Auto-generated method stub
		return teacherStudentDao.addStuEvaScore(stuEvaluateResult);
	}

	@Override
	public List<StuEvaluateResult> selectSummaryStuEvas(Integer claId, String schoolYear) {
		// TODO Auto-generated method stub

		return teacherStudentDao.selectSummaryStuEvas(claId, schoolYear);
	}

	@Override
	public boolean deleteStuEvas(Integer claId, String schoolYear) {
		// TODO Auto-generated method stub
		return teacherStudentDao.deleteStuEvas(claId, schoolYear);
	}

	@Override
	public List<StuEvaluateResult> selectMaxEva(Integer i, String schoolYear) {
		// TODO Auto-generated method stub
		return teacherStudentDao.selectMaxEva(schoolYear, i);
	}

	@Override
	public List<StuEvaluateResult> findStuEvaByPageCid(Integer itemEvaTypeId, Integer claId, String schoolYear) {
		// TODO Auto-generated method stub
		return teacherStudentDao.findStuEvas(itemEvaTypeId,claId,schoolYear);
	}

}
