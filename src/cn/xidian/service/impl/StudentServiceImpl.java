package cn.xidian.service.impl;

import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.xidian.dao.CompositionRulesDao;
import cn.xidian.dao.ContributeTargetDao;
import cn.xidian.dao.StudentDao;
import cn.xidian.dao.TeachingTargetDao;
import cn.xidian.entity.AverClazzCoursePoint;
import cn.xidian.entity.ClazzCoursePoint;
import cn.xidian.entity.CompositionRules;
import cn.xidian.entity.ContributeTarget;
import cn.xidian.entity.PageBean;
import cn.xidian.entity.StuEvaluateResult;
import cn.xidian.entity.Student;
import cn.xidian.entity.StudentCourse;
import cn.xidian.entity.TeachingTarget;
import cn.xidian.entity.TeachingTargetEvaluate;
import cn.xidian.exception.CursRulesNotExistException;
import cn.xidian.exception.TchingTargetNotExistException;
import cn.xidian.service.StudentService;
import cn.xidian.utils.PageUtils;
import cn.xidian.utils.ServiceUtils;
import cn.xidian.web.bean.B1;
import cn.xidian.web.bean.B2;

@Component
public class StudentServiceImpl implements StudentService {

	private StudentDao studentDao;

	@Resource(name = "studentDaoImpl")
	public void setStudentDao(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	private CompositionRulesDao compositionRulesDao;

	@Resource(name = "compositionRulesDaoImpl")
	public void setCompositionRulesDao(CompositionRulesDao compositionRulesDao) {
		this.compositionRulesDao = compositionRulesDao;
	}
	private TeachingTargetDao teachingTargetDao;

	@Resource(name = "teachingTargetDaoImpl")
	public void setTeachingTargetDao(TeachingTargetDao teachingTargetDao) {
		this.teachingTargetDao = teachingTargetDao;
	}
	
	private ContributeTargetDao contributeTargetDao;
	
	@Resource(name = "contributeTargetDaoImpl")
	public void setContributeTargetDao(ContributeTargetDao contributeTargetDao) {
		this.contributeTargetDao = contributeTargetDao;
	}
	
	@Override
	public boolean loginValidate(String stuSchNum, String stuPwd) {
		try {
			return studentDao.findBySchNumAndPwd(stuSchNum,
					ServiceUtils.md5(stuPwd));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Student selectInfBySchNum(String stuSchNum) {
		return studentDao.findBySchNum(stuSchNum);
	}

	@Override
	public boolean updateInfBySchNum(Student student) {
		return studentDao.updateByStudent(student);
	}

	@Override
	public boolean updateGoal(Student student) {
		// TODO Auto-generated method stub
		return studentDao.updateGoal(student);
	}

	@Override
	public boolean modifyPassword(String schNum, String pwd){
		String passwordString = "";
		try {
			passwordString = ServiceUtils.md5(pwd);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return studentDao.modifyPassword(schNum,passwordString);
	}

	@Override
	public PageBean<StudentCourse> selectStuAllGradesById(Integer id,Integer page) {
		// TODO Auto-generated method stub
		List<StudentCourse> studentCourses=studentDao.selectStuAllGradesById(id);
		PageBean<StudentCourse> pageBean=PageUtils.page(page, studentCourses.size(),15);
		List<StudentCourse> studentCourses2=studentDao.findStuCoursesByStuId(id,pageBean.getBegin(),pageBean.getLimit());
		pageBean.setList(studentCourses2);
		return pageBean;
	}


	@Override
	public List<StuEvaluateResult> selectStuEvaluateResults(Integer stuId, String schoolYear) {
		// TODO Auto-generated method stub
		return studentDao.selectStuEvaluateResults(stuId,schoolYear);
	}

	@Override
	public StudentCourse getSCourse(int stuCursId) {
		StudentCourse sCourse = studentDao.getEvaPer(stuCursId);
		return sCourse;
	}

	@Override
	public List<TeachingTargetEvaluate> caculateBySCourse(StudentCourse sCourse) {
		CompositionRules rules = compositionRulesDao.selectByCourse(sCourse.getCourse().getCursNum());
		
		Double midEvaPer = rules.getMidTermPer() / 100;// 期中成绩百分比
		Double finEvaPer = rules.getFinalExamPer() / 100;// 期末成绩百分比
		Double classEvaPer = rules.getClazzPer() / 100;// 课堂表现百分比
		Double workEvaPer = rules.getHomeworkResultPer() / 100;// 平时作业百分比
		Double expEvaPer = rules.getExpResultPer() / 100;// 实验成绩百分比
		
		Double classMidValue = sCourse.getMidEvaValue() * midEvaPer;// 期中成绩
		Double classFinValue = sCourse.getFinEvaValue() * finEvaPer;// 期末成绩
		Double classClazzValue = sCourse.getClassEvaValue() * classEvaPer;// 课堂成绩
		Double classWorkValue = sCourse.getWorkEvaValue() * workEvaPer;// 作业成绩
		Double classExpValue = sCourse.getExpEvaValue() * expEvaPer;// 实验成绩
		
		List<TeachingTarget> tts = teachingTargetDao.selectByCursId(sCourse.getCourse().getCursId());
		if (tts.size() == 0) {
			throw new TchingTargetNotExistException("未找到课程目标，请检查课程信息！");
		}
		Double classMidTargetValue = 0.0;// 班级期中成绩目标值
		Double classFinTargetValue = 0.0;// 班级期末成绩目标值
		Double classClazzTargetValue = 0.0;// 班级课堂成绩目标值
		Double classWorkTargetValue = 0.0;// 班级作业成绩目标值
		Double classExpTargetValue = 0.0;// 班级实验成绩目标值

		for (int i = 0; i < tts.size(); i++) {
			classMidTargetValue += tts.get(i).getTchtargetMidTargetValue();
			classFinTargetValue += tts.get(i).getTchtargetFinTargetValue();
			classClazzTargetValue += tts.get(i).getTchtargetClassTargetValue();
			classWorkTargetValue += tts.get(i)
					.getTchtargetHomeworkTargetValue();
			classExpTargetValue += tts.get(i).getTchtargetExpTargetValue();
		}

		List<Double> b1s = new LinkedList<Double>();
		List<TeachingTargetEvaluate> stuEvaluate = new LinkedList<TeachingTargetEvaluate>();
		for (int i = 0; i < tts.size(); i++) {
			TeachingTargetEvaluate ttEvaluate = new TeachingTargetEvaluate();
			Double classMidEvaValue = 0.0;// 班级期中成绩评价值
			Double classFinEvaValue = 0.0;// 班级期末成绩评价值
			Double classClazzEvaValue = 0.0;// 班级课堂成绩评价值
			Double classWorkEvaValue = 0.0;// 班级作业成绩评价值
			Double classExpEvaValue = 0.0;// 班级实验成绩评价值
			Double a1 = 0.0;
			Double b1 = 0.0;
			// 先分别计算每一行的值，再把每一行加到列表中

			if (classMidTargetValue == 0.0) {
				classMidEvaValue = 0.0;
			} else {
				classMidEvaValue = classMidValue
						* tts.get(i).getTchtargetMidTargetValue()
						/ classMidTargetValue;
			}
			if (classFinTargetValue == 0.0) {
				classFinEvaValue = 0.0;
			} else {
				classFinEvaValue = classFinValue
						* tts.get(i).getTchtargetFinTargetValue()
						/ classFinTargetValue;
			}
			if (classClazzTargetValue == 0.0) {
				classClazzEvaValue = 0.0;
			} else {
				classClazzEvaValue = classClazzValue
						* tts.get(i).getTchtargetClassTargetValue()
						/ classClazzTargetValue;
			}
			if (classWorkTargetValue == 0.0) {
				classWorkEvaValue = 0.0;
			} else {
				classWorkEvaValue = classWorkValue
						* tts.get(i).getTchtargetHomeworkTargetValue()
						/ classWorkTargetValue;
			}
			if (classExpTargetValue == 0.0) {
				classExpEvaValue = 0.0;
			} else {
				classExpEvaValue = classExpValue
						* tts.get(i).getTchtargetExpTargetValue()
						/ classExpTargetValue;
			}

			a1 = classMidEvaValue + classFinEvaValue + classClazzEvaValue
					+ classWorkEvaValue + classExpEvaValue;
			double denominator = (tts.get(i).getTchtargetMidTargetValue()
					+ tts.get(i).getTchtargetFinTargetValue()
					+ tts.get(i).getTchtargetClassTargetValue()
					+ tts.get(i).getTchtargetHomeworkTargetValue() + tts.get(i)
					.getTchtargetExpTargetValue());
			if (denominator == 0) {
				b1 = 0.0;
			} else {
				b1 = a1 / denominator;
			}
			ttEvaluate.setTchtargetMidEvaValue(classMidEvaValue);
			ttEvaluate.setTchtargetFinEvaValue(classFinEvaValue);
			ttEvaluate.setTchtargetClassEvaValue(classClazzEvaValue);
			ttEvaluate.setTchtargetWorkEvaValue(classWorkEvaValue);
			ttEvaluate.setTchtargetExpEvaValue(classExpEvaValue);
			ttEvaluate.setA1(a1);
			ttEvaluate.setB1(b1);
			b1s.add(b1);// 把b1存到一个list，一会计算a2、b2拿出来用
			ttEvaluate.setTeachingTarget(tts.get(i));
			stuEvaluate.add(ttEvaluate);
		}
		return stuEvaluate;
	}

	@Override
	public List<B2> getB2(StudentCourse sCourse, List<B1> claCursB1s) {
		List<B2> claCursB2s = new LinkedList<B2>();
		List<TeachingTarget> tts = teachingTargetDao.selectByCursId(sCourse.getCourse().getCursId());
		List<ContributeTarget> cts = contributeTargetDao.selectByTarget(tts
				.get(0).getTchTargetId());
		int n = cts.size();
		if (n == 0) {
			throw new CursRulesNotExistException("评分规则不完整，请检查课程信息！");
		}
		Double[] a2 = new Double[n];
		Double[] b2 = new Double[n];
		Double[] targetTarValue = new Double[n];
		for (int x = 0; x < n; x++) {
			a2[x] = 0.0;
			b2[x] = 0.0;
			targetTarValue[x] = 0.0;
		}
		List<ContributeTarget> ct = new LinkedList<ContributeTarget>();
		for (int j = 0; j < tts.size(); j++) {
			ct = contributeTargetDao
					.selectByTarget(tts.get(j).getTchTargetId());
			for (int x = 0; x < ct.size(); x++) {
				Double ai = ct.get(x).getConTarValue() * Float.parseFloat(claCursB1s.get(j).getB1());
				targetTarValue[x] += ct.get(x).getConTarValue();
				a2[x] += ai;
			}
		}
		DecimalFormat df = new DecimalFormat("#0.000");// 用于格式化Double类型数据
		for (int x = 0; x < n; x++) {
			if (targetTarValue[x] == 0) {
				b2[x] = 0.0;
			} else {
				b2[x] = a2[x] / targetTarValue[x];
			}
			B2 bValue2 = new B2();
			bValue2.setPoint(cts.get(x).getIndicatorPoint().getIndPointNum()+
					cts.get(x).getIndicatorPoint().getIndPointContent());
			bValue2.setTargetTarValue(df.format(targetTarValue[x]));
			bValue2.setA2(df.format(a2[x]));
			bValue2.setB2(df.format(b2[x]));
			claCursB2s.add(bValue2);
		}
		return claCursB2s;
	}

}
