package cn.xidian.web.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.LinkedHashSet;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.struts2.interceptor.RequestAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.xidian.entity.AverClazzCoursePoint;
import cn.xidian.entity.AverTeachingTargetEvaluate;
import cn.xidian.entity.Clazz;
import cn.xidian.entity.Course;
import cn.xidian.entity.ClazzCoursePoint;
import cn.xidian.entity.CoursePoint;
import cn.xidian.entity.GradeCoursePoint;
import cn.xidian.entity.IsEvaluate;
import cn.xidian.entity.PageBean;
import cn.xidian.entity.Student;
import cn.xidian.entity.StudentCourse;
import cn.xidian.entity.Survey;
import cn.xidian.entity.SurveyQuestion;
import cn.xidian.entity.SurveyReplyer;
import cn.xidian.entity.SurveySelector;
import cn.xidian.entity.Teacher;
import cn.xidian.entity.TeacherExperiment;
import cn.xidian.entity.TeachingTarget;
import cn.xidian.entity.TeachingTargetEvaluate;
import cn.xidian.entity.TextAnswer;
import cn.xidian.entity.User;
import cn.xidian.exception.ClazzCoursePointNotExistException;
import cn.xidian.exception.ClazzNotExistException;
import cn.xidian.exception.CourseNotExistException;
import cn.xidian.exception.CursRulesNotExistException;
import cn.xidian.exception.StudentCourseNotExistsException;
import cn.xidian.exception.TTargetNotExistException;
import cn.xidian.exception.TchingTargetNotExistException;
import cn.xidian.service.ClazzService;
import cn.xidian.service.ClazzCoursePointService;
import cn.xidian.service.CoursePointService;
import cn.xidian.service.CourseService;
import cn.xidian.service.GradeCoursePointService;
import cn.xidian.service.IsEvaluateService;
import cn.xidian.service.StudentCourseService;
import cn.xidian.service.SurveyService;
import cn.xidian.service.TeacherCourseService;
import cn.xidian.service.TeacherExperimentService;
import cn.xidian.service.TeacherService;
import cn.xidian.service.TeachingTargetEvaluateService;
import cn.xidian.service.TeachingTargetService;
import cn.xidian.web.bean.AdminStuLimits;
import cn.xidian.web.bean.B1;
import cn.xidian.web.bean.B2;
import cn.xidian.web.bean.CourseTargetEvaluate;
import cn.xidian.web.bean.StuCursLimits;
import cn.xidian.web.service.CourseTargetDetailService;
import cn.xidian.web.service.UploadActionService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component(value = "TeacherAction")
@Scope("prototype")
public class TeacherAction extends ActionSupport implements RequestAware {
	Teacher teacher = new Teacher();
	private TeacherExperiment experiment;
	private List<TeacherExperiment> experiments = new LinkedList<TeacherExperiment>();
	private Integer expId;
	private List<Clazz> allClazz;
	private List<Course> tchrCourses;
	private StuCursLimits stuCursLimit;
	private List<StudentCourse> stuPerformances = new LinkedList<StudentCourse>();
	private String password;
	private List<Course> teachCourses;// 讲授的课程
	private List<Course> chargeCourses;// 负责的课程
	private String index;
	private Course course;
	private AdminStuLimits limits;
	private String schNum;
	private Clazz clazz = new Clazz();
	// 查询达成度
	private String cursName;
	private String calculateType;
	private String gradeName;
	List<TeachingTarget> targets;
	List<String> points = new LinkedList<String>();
	List<List<String>> b1 = new LinkedList<List<String>>();
	List<List<String>> b2 = new LinkedList<List<String>>();
	// 获得一个班级一门课程的达成度b1、b2的详细列表
	private String clazzName;
	private List<B1> claCursB1s = new LinkedList<B1>();
	private List<B2> claCursB2s = new LinkedList<B2>();
	// 课程负责人进行课程评定
	List<GradeCoursePoint> gradeCoursePoints = new LinkedList<GradeCoursePoint>();
	private List<CourseTargetEvaluate> cte = new LinkedList<CourseTargetEvaluate>();
	private List<IsEvaluate> isEvaluate = new LinkedList<IsEvaluate>();
	private String grade;
	private Integer cursId;
	// 问卷添加
	private PageBean<Survey> suPageBean;
	private Survey survey;
	private Integer surveyId;
	private Teacher tchr;
	private Student student;
	private List<SurveyQuestion> surveyQuestions;
	private List<SurveySelector> surveySelectors;
	private List<TextAnswer> textAnswers;
	private Integer role;
	private String message;
	// 换页添加
	private Integer page;
	/* 上传头像 */
	private String uploadUrl;
	private File file;
	/* 上传头像 */
	private Map<String, Object> request;
	Map<String, Object> session = ActionContext.getContext().getSession();

	User tUser = (User) session.get("tUser");

	private TeacherService teacherService;

	@Resource(name = "teacherServiceImpl")
	public void setTeacherService(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	private TeacherCourseService teacherCourseService;

	@Resource(name = "teacherCourseServiceImpl")
	public void setTeacherCourseService(TeacherCourseService teacherCourseService) {
		this.teacherCourseService = teacherCourseService;
	}

	private SurveyService surveyService;

	@Resource(name = "surveyServiceImpl")
	public void set(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	private StudentCourseService studentCourseService;

	@Resource(name = "studentCourseServiceImpl")
	public void setStudentCourseService(StudentCourseService studentCourseService) {
		this.studentCourseService = studentCourseService;
	}

	private ClazzService clazzService;

	@Resource(name = "clazzServiceImpl")
	public void setClazzService(ClazzService clazzService) {
		this.clazzService = clazzService;
	}

	public String selectBasicByNum() {
		schNum = tUser.getSchNum();
		teacher = teacherService.selectInfBySchNum(schNum);
		return "teacher";
	}

	/* 修改界面获得教师信息 */
	public String selectInfByNum() {
		schNum = tUser.getSchNum();
		teacher = teacherService.selectInfBySchNum(schNum);
		experiments = teacherExperimentService.selectByTchrNum(schNum);
		return "teacher";
	}

	public String modifyBasicInf() {
		schNum = tUser.getSchNum();
		teacher.setTchrSchNum(schNum);
		boolean aa = teacherService.updateInfBySchNum(teacher);
		if (file != null)
			uploadFile();
		if (aa) {
			request.put("Message", "修改成功！");
			return "teacherModify";
		} else {
			request.put("Message", "对不起，修改失败！");
			return "teacherModify";
		}
	}

	/* 上传头像 */
	public boolean uploadFile() {
		UploadActionService uas = new UploadActionService();
		try {
			String fileName = teacher.getTchrSchNum() + ".jpg";
			uas.upload(file, uploadUrl, fileName); // 自定义方法调用
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/* 上传头像完 */
	/* 修改密码 */
	public String modifyPassword() {
		schNum = tUser.getSchNum();
		boolean isSuccess = teacherService.modifyPassword(schNum, password);
		if (isSuccess) {
			request.put("Message", "修改密码成功！");
			return "teacherModify";
		} else {
			request.put("Message", "对不起，修改密码失败！");
			return "teacherModify";
		}
	}

	private TeacherExperimentService teacherExperimentService;

	@Resource(name = "teacherExperimentServiceImpl")
	public void setTeacherExperimentService(TeacherExperimentService teacherExperimentService) {
		this.teacherExperimentService = teacherExperimentService;
	}

	/* 添加学历履历信息 */
	public String addExpByTchrNum() {
		schNum = tUser.getSchNum();
		teacherExperimentService.addByTchrNum(experiment, schNum);
		request.put("Message", "添加成功！");
		return "teacherModify";
	}

	/* 删除学历履历信息 */
	public String deleteExpById() {
		teacherExperimentService.deleteById(expId);
		request.put("Message", "删除成功！");
		return "teacherModify";
	}

	/* 查找老师教授和负责的课程 */
	public String selectTchrCourse() {
		schNum = tUser.getSchNum();
		teachCourses = teacherService.selectTchCoursesByTchrNum(schNum);// 教授的课程
		chargeCourses = teacherService.selectChargeCoursesByTchrNum(schNum);// 负责的课程
		return "teacher";
	}

	/* 调出本学期所有课程 */
	public String selectAllCurs() {
		tchrCourses = courseService.selectByCurTerm();
		return "teacher";
	}

	/* 查询学生成绩 */
	public String selectStuPer() {
		schNum = tUser.getSchNum();
		stuCursLimit.setTchrSchNum(schNum);
		Set<StudentCourse> stuCurs = studentCourseService.selectStuCursLimits(stuCursLimit);
		Iterator<StudentCourse> it = stuCurs.iterator();
		while (it.hasNext()) {
			stuPerformances.add(it.next());
		}
		if (stuPerformances.size() == 0) {
			request.put("Message", "没有找到相关记录。");
		}
		course = courseService.findByName(stuCursLimit.getCursName());
		return "tchrManagement4";
	}

	CourseService courseService;

	@Resource(name = "courseServiceImpl")
	public void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}

	TeachingTargetService teachingTargetService;

	@Resource(name = "teachingTargetServiceImpl")
	public void setTeachingTargetService(TeachingTargetService teachingTargetService) {
		this.teachingTargetService = teachingTargetService;
	}

	TeachingTargetEvaluateService teachingTargetEvaluateService;

	@Resource(name = "teachingTargetEvaluateServiceImpl")
	public void setTeachingTargetEvaluateService(TeachingTargetEvaluateService teachingTargetEvaluateService) {
		this.teachingTargetEvaluateService = teachingTargetEvaluateService;
	}

	ClazzCoursePointService clazzCoursePointService;

	@Resource(name = "clazzCoursePointServiceImpl")
	public void setClazzCoursePointService(ClazzCoursePointService clazzCoursePointService) {
		this.clazzCoursePointService = clazzCoursePointService;
	}

	CoursePointService coursePointService;

	@Resource(name = "coursePointServiceImpl")
	public void setCoursePointService(CoursePointService coursePointService) {
		this.coursePointService = coursePointService;
	}

	/* 任课老师计算某个班级某门课程的达成度 */
	public String calculateClazzCursTarget() {
		try {
			String tchrSchNum = ((User) session.get("tUser")).getSchNum();
			teacherService.caculateClazzTarget(calculateType, gradeName, cursName, clazzName, tchrSchNum);
			CourseTargetDetailService courseTargetDetailService = new CourseTargetDetailService();
			List<TeachingTarget> targets = teachingTargetService.selectByCursName(cursName);
			if (calculateType.equals("0")) {
				//0是班级评估
				List<TeachingTargetEvaluate> targetValues = teachingTargetEvaluateService.selectByCursAndClazz(cursName,
						clazzName);
				claCursB1s = courseTargetDetailService.getB1(targets, targetValues);

				List<ClazzCoursePoint> ccPoints = clazzCoursePointService.selectByCursAndClazz(cursName, clazzName);
				claCursB2s = courseTargetDetailService.getB2(ccPoints);
				course = courseService.findByName(cursName);
			} else {
				//1是年级评估
				List<AverTeachingTargetEvaluate> atargetValues = teachingTargetEvaluateService
						.selectByGradeAndClazz(cursName, gradeName);
				claCursB1s = courseTargetDetailService.getAB1(targets, atargetValues);

				List<AverClazzCoursePoint> accPoints = clazzCoursePointService.selectByCursAndGrade(cursName,
						gradeName);
				claCursB2s = courseTargetDetailService.getAB2(accPoints);
				course = courseService.findByName(cursName);
			}
		} catch (CourseNotExistException e) {
			request.put("Message", e.getMessage());
		} catch (ClazzNotExistException e) {
			request.put("Message", e.getMessage());
		} catch (TchingTargetNotExistException e) {
			request.put("Message", e.getMessage());
		} catch (CursRulesNotExistException e) {
			request.put("Message", e.getMessage());
		} catch (StudentCourseNotExistsException e) {
			request.put("Message", e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "targetDetail";
	}

	/* 查询达成度 */
	public String findB1B2() {
		try {
			DecimalFormat df = new DecimalFormat("0.000"); // 格式化，保留三位小数
			session.put("cursName", cursName);
			// 获取课程的教学目标
			targets = teachingTargetService.selectByCursName(cursName);
			course = targets.get(0).getCourse();
			List<TeachingTargetEvaluate> ttValue = courseService.selectByCursNameAndGrade(cursName, grade);
			List<AverTeachingTargetEvaluate> attValue = courseService.selectByCursNameAndGradeName(cursName, grade);
			Set<String> clazz = new LinkedHashSet<String>();
			List<String> clazzs = new LinkedList<String>();

			// 获取班级的集合，set可自动去重
			for (int i = 0; i < ttValue.size(); i++) {
				clazz.add(ttValue.get(i).getClazz().getClaName());
			}
			clazzs.addAll(clazz);
			// 每个班级的B1就是一条数据，这个搞法效率忒低，待改进
			for (int i = 0; i < clazzs.size(); i++) {
				List<String> B1 = new LinkedList<String>();
				B1.add(clazzs.get(i));
				for (int j = 0; j < targets.size(); j++) {
					for (int k = 0; k < ttValue.size(); k++) {
						if (ttValue.get(k).getClazz().getClaName().equals(clazzs.get(i)) && ttValue.get(k)
								.getTeachingTarget().getTchTargetId() == targets.get(j).getTchTargetId()) {
							B1.add(df.format(ttValue.get(k).getB1()));
						}
					}
				}
				b1.add(B1);// 将B1数据加入b1
			}
			if (attValue.size() != 0) {
				List<String> B1 = new LinkedList<String>();
				B1.add(grade + "级");
				for (int i = 0; i < attValue.size(); i++) {
					B1.add(df.format(attValue.get(i).getB1()));
				}
				b1.add(B1);
			}
			// 以上获得第一张表的数据
			// 以下是第二张表
			List<ClazzCoursePoint> cursPoints = clazzCoursePointService.selectBycursNameAndTerm(cursName);
			List<AverClazzCoursePoint> acursPoints = clazzCoursePointService.selectByCursAndGrade(cursName, grade);
			List<CoursePoint> point = coursePointService.selectByCursId(course.getCursId());
			// 获得该门课程对应指标点编号的集合
			for (int i = 0; i < point.size(); i++) {
				points.add(point.get(i).getIndPoint().getIndPointNum());
			}

			for (int i = 0; i < clazzs.size(); i++) {
				List<String> B2 = new LinkedList<String>();
				B2.add(clazzs.get(i));
				for (int j = 0; j < points.size(); j++) {
					for (int k = 0; k < cursPoints.size(); k++) {
						if (cursPoints.get(k).getClazz().getClaName().equals(clazzs.get(i))
								&& cursPoints.get(k).getIndPoint().getIndPointNum().equals(points.get(j))) {
							B2.add(df.format(cursPoints.get(k).getB2()));
						}
					}
				}
				b2.add(B2);// 将B1数据加入b1
			}
			if (acursPoints.size() != 0) {
				List<String> B2 = new LinkedList<String>();
				B2.add(grade + "级");
				for (int k = 0; k < acursPoints.size(); k++) {
					B2.add(df.format(acursPoints.get(k).getB2()));
				}

				b2.add(B2);
			}
		} catch (CourseNotExistException e) {
			request.put("Message", e.getMessage());
		} catch (TTargetNotExistException e) {
			request.put("Message", e.getMessage());
		}
		allClazz = clazzService.findAllCla();
		User t = (User) session.get("tUser");
		tchrCourses = teacherCourseService.selectByNumAndTerm(t.getSchNum());
		if (b1.size() == 0 || b2.size() == 0) {
			request.put("Message", "对不起，没有找到相关信息！");
		}

		return "teacher";
	}

	// 获得一个班级一门课程的达成度b1、b2的详细列表
	public String getClaCursTargetDetail() {

		CourseTargetDetailService courseTargetDetailService = new CourseTargetDetailService();
		if (clazzName.length() > 6) {
			// 获取b1
			cursName = session.get("cursName").toString();

			List<TeachingTarget> targets = teachingTargetService.selectByCursName(cursName);
			List<TeachingTargetEvaluate> targetValues = teachingTargetEvaluateService.selectByCursAndClazz(cursName,
					clazzName);
			claCursB1s = courseTargetDetailService.getB1(targets, targetValues);

			List<ClazzCoursePoint> ccPoints = clazzCoursePointService.selectByCursAndClazz(cursName, clazzName);
			claCursB2s = courseTargetDetailService.getB2(ccPoints);
			course = courseService.findByName(cursName);
			if (claCursB1s.size() == 0 || claCursB2s.size() == 0) {
				request.put("Message", "对不起，没有找到相关信息！");
				return "tchrManagement5";
			}
		} else {
			clazzName = clazzName.substring(0, 4);
			cursName = session.get("cursName").toString();

			List<TeachingTarget> targets = teachingTargetService.selectByCursName(cursName);
			List<AverTeachingTargetEvaluate> atargetValues = teachingTargetEvaluateService
					.selectByGradeAndClazz(cursName, clazzName);
			claCursB1s = courseTargetDetailService.getAB1(targets, atargetValues);
			List<AverClazzCoursePoint> accPoints = clazzCoursePointService.selectByCursAndGrade(cursName, clazzName);
			claCursB2s = courseTargetDetailService.getAB2(accPoints);
			course = courseService.findByName(cursName);
			if (claCursB1s.size() == 0 || claCursB2s.size() == 0) {
				request.put("Message", "对不起，没有找到相关信息！");
				return "tchrManagement5";
			}
		}

		return "teacher";
	}

	private IsEvaluateService isEvaluateService;

	@Resource(name = "isEvaluateServiceImpl")
	public void setIsEvaluateService(IsEvaluateService isEvaluateService) {
		this.isEvaluateService = isEvaluateService;
	}

	private GradeCoursePointService gradeCoursePointService;

	@Resource(name = "gradeCoursePointServiceImpl")
	public void setGradeCoursePointService(GradeCoursePointService gradeCoursePointService) {
		this.gradeCoursePointService = gradeCoursePointService;
	}

	// 课程负责人进行课程达成评价
	public String selectTargetEvaByCursId() {

		gradeCoursePoints = gradeCoursePointService.selectByCursIdAndGrade(cursId, grade);

		return "teacher";
	}

	public String selectClazzCursTargetValueByGrade() {

		isEvaluate = isEvaluateService.selectByCursIdAndGrade(cursId, grade);
		for (int i = 0; i < isEvaluate.size(); i++) {
			IsEvaluate ie = isEvaluate.get(i);
			CourseTargetEvaluate c = new CourseTargetEvaluate();
			c.setCursId(ie.getCourse().getCursId());
			c.setCourse(ie.getCourse().getCursName());
			c.setClazz(ie.getClazz().getClaName());
			c.setTeacher(ie.getTeacher().getTchrSchNum() + ie.getTeacher().getTchrName());
			c.setDate(ie.getEvaDate().toString());
			c.setState("评价完成");
			cte.add(c);
		}

		if (cte.size() == 0) {
			request.put("Message", "对不起，没有找到相关数据");
		}

		gradeCoursePoints = gradeCoursePointService.selectByCursIdAndGrade(cursId, grade);

		return "teacher";
	}

	public String caculateCursTargetValue() {// 计算课程的达成度
		try {
			courseService.caculateCursTargetValueByCursIdAndGrade(cursId, grade);
			request.put("Message", "课程达成评价成功");
		} catch (ClazzCoursePointNotExistException e) {
			request.put("Message", e.getMessage());
		}
		isEvaluate = isEvaluateService.selectByCursIdAndGrade(cursId, grade);
		for (int i = 0; i < isEvaluate.size(); i++) {
			IsEvaluate ie = isEvaluate.get(i);
			CourseTargetEvaluate c = new CourseTargetEvaluate();
			c.setCursId(ie.getCourse().getCursId());
			c.setCourse(ie.getCourse().getCursName());
			c.setClazz(ie.getClazz().getClaName());
			c.setTeacher(ie.getTeacher().getTchrSchNum() + ie.getTeacher().getTchrName());
			c.setDate(ie.getEvaDate().toString());
			c.setState("评价完成");
			cte.add(c);
		}
		return "tchrCursEvaluate";
	}

	public String selectTeacherSurveys() {
		if (page == null) {
			page = 1;
		}
		if (tUser != null) {
			String userRole = tUser.getIdentity().toString();
			if (userRole == "TEACHER") {
				role = 2;
			}
			suPageBean = surveyService.selectTchrSurveys(role, page);
		}
		if (message != null) {
			try {
				message = new String(message.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.put("Message", message);
		}
		return "teacher";
	}

	// 查找某个问卷的全部信息
	public String selectSurveyById() {
		survey = surveyService.selectSurveyById(surveyId);
		surveyQuestions = surveyService.selectQuestionBysurveyId(surveyId);
		return "teacher";
	}

	// 提交老师填写的问卷结果
	public String addTchrSurveyDone() {
		Teacher teacher = teacherService.selectInfBySchNum(tUser.getSchNum());
		System.out.println("用户角色" + tUser.getIdentity().toString());
		List<SurveyReplyer> replayers = surveyService.selectSurveyReplayer(surveyId, teacher.getTchrId(),
				tUser.getIdentity().toString());
		if (replayers.size() == 0) {// 存储问卷者信息
			// 存储问卷者信息
			if (tUser != null) {

				survey = surveyService.selectSurveyById(surveyId);
				SurveyReplyer surveyReplyer = new SurveyReplyer();
				Date replyTime = new Date();// 做問卷的时间
				surveyReplyer.setTeacher(teacher);
				surveyReplyer.setSurvey(survey);
				surveyReplyer.setReplyTime(replyTime);
				/* surveyService.addSurveyReplyer(surveyReplyer); */
				// 存储问卷选择结果
				boolean isSuccess = surveyService.addSurveyDone(surveyReplyer, surveySelectors, textAnswers, survey);
				if (isSuccess) {
					request.put("Message", "提交成功！！");
					message = "提交成功！！";
				} else {
					request.put("Message", "提交失败！");
					message = "提交成功！！";
				}

			}

		} else {
			request.put("Message", "提交失败，不能重复填写问卷！");
			message = "提交失败，不能重复填写问卷！";
		}
		return "surveyDone";
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public TeacherExperiment getExperiment() {
		return experiment;
	}

	public void setExperiment(TeacherExperiment experiment) {
		this.experiment = experiment;
	}

	public List<TeacherExperiment> getExperiments() {
		return experiments;
	}

	public void setExperiments(List<TeacherExperiment> experiments) {
		this.experiments = experiments;
	}

	public List<Course> getTchrCourses() {
		return tchrCourses;
	}

	public void setTchrCourses(List<Course> tchrCourses) {
		this.tchrCourses = tchrCourses;
	}

	public List<Clazz> getAllClazz() {
		return allClazz;
	}

	public void setAllClazz(List<Clazz> allClazz) {
		this.allClazz = allClazz;
	}

	public Integer getExpId() {
		return expId;
	}

	public void setExpId(Integer expId) {
		this.expId = expId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Course> getTeachCourses() {
		return teachCourses;
	}

	public void setTeachCourses(List<Course> teachCourses) {
		this.teachCourses = teachCourses;
	}

	public List<Course> getChargeCourses() {
		return chargeCourses;
	}

	public void setChargeCourses(List<Course> chargeCourses) {
		this.chargeCourses = chargeCourses;
	}

	public Map<String, Object> getRequest() {
		return request;
	}

	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

	public StuCursLimits getStuCursLimit() {
		return stuCursLimit;
	}

	public void setStuCursLimit(StuCursLimits stuCursLimit) {
		this.stuCursLimit = stuCursLimit;
	}

	public String getCursName() {
		return cursName;
	}

	public void setCursName(String cursName) {
		this.cursName = cursName;
	}

	public List<StudentCourse> getStuPerformances() {
		return stuPerformances;
	}

	public void setStuPerformances(List<StudentCourse> stuPerformances) {
		this.stuPerformances = stuPerformances;
	}

	public List<List<String>> getB1() {
		return b1;
	}

	public void setB1(List<List<String>> b1) {
		this.b1 = b1;
	}

	public List<List<String>> getB2() {
		return b2;
	}

	public void setB2(List<List<String>> b2) {
		this.b2 = b2;
	}

	public List<B1> getClaCursB1s() {
		return claCursB1s;
	}

	public void setClaCursB1s(List<B1> claCursB1s) {
		this.claCursB1s = claCursB1s;
	}

	public List<B2> getClaCursB2s() {
		return claCursB2s;
	}

	public void setClaCursB2s(List<B2> claCursB2s) {
		this.claCursB2s = claCursB2s;
	}

	public List<TeachingTarget> getTargets() {
		return targets;
	}

	public void setTargets(List<TeachingTarget> targets) {
		this.targets = targets;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	public List<String> getPoints() {
		return points;
	}

	public void setPoints(List<String> points) {
		this.points = points;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public List<CourseTargetEvaluate> getCte() {
		return cte;
	}

	public void setCte(List<CourseTargetEvaluate> cte) {
		this.cte = cte;
	}

	public List<IsEvaluate> getIsEvaluate() {
		return isEvaluate;
	}

	public void setIsEvaluate(List<IsEvaluate> isEvaluate) {
		this.isEvaluate = isEvaluate;
	}

	public Integer getCursId() {
		return cursId;
	}

	public void setCursId(Integer cursId) {
		this.cursId = cursId;
	}

	public List<GradeCoursePoint> getGradeCoursePoints() {
		return gradeCoursePoints;
	}

	public void setGradeCoursePoints(List<GradeCoursePoint> gradeCoursePoints) {
		this.gradeCoursePoints = gradeCoursePoints;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String excute() {
		allClazz = clazzService.findAllCla();
		User t = (User) session.get("tUser");
		tchrCourses = teacherCourseService.selectByNumAndTerm(t.getSchNum());
		return "teacher";
	}

	public AdminStuLimits getLimits() {
		return limits;
	}

	public void setLimits(AdminStuLimits limits) {
		this.limits = limits;
	}

	public String getSchNum() {
		return schNum;
	}

	public void setSchNum(String schNum) {
		this.schNum = schNum;
	}

	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	public PageBean<Survey> getSuPageBean() {
		return suPageBean;
	}

	public void setSuPageBean(PageBean<Survey> suPageBean) {
		this.suPageBean = suPageBean;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public Integer getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Integer surveyId) {
		this.surveyId = surveyId;
	}

	public Teacher getTchr() {
		return tchr;
	}

	public void setTchr(Teacher tchr) {
		this.tchr = tchr;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public List<SurveyQuestion> getSurveyQuestions() {
		return surveyQuestions;
	}

	public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
		this.surveyQuestions = surveyQuestions;
	}

	public List<SurveySelector> getSurveySelectors() {
		return surveySelectors;
	}

	public void setSurveySelectors(List<SurveySelector> surveySelectors) {
		this.surveySelectors = surveySelectors;
	}

	public List<TextAnswer> getTextAnswers() {
		return textAnswers;
	}

	public void setTextAnswers(List<TextAnswer> textAnswers) {
		this.textAnswers = textAnswers;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCalculateType() {
		return calculateType;
	}

	public void setCalculateType(String calculateType) {
		this.calculateType = calculateType;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

}
