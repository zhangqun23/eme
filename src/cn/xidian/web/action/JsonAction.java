package cn.xidian.web.action;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.RequestAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import cn.xidian.entity.Clazz;
import cn.xidian.entity.GradeClazzSurvey;
import cn.xidian.entity.ItemEvaluatePoint;
import cn.xidian.entity.ItemEvaluateScore;
import cn.xidian.entity.ItemEvaluateType;
import cn.xidian.entity.PageBean;
import cn.xidian.entity.StuEvaluateResult;
import cn.xidian.entity.Student;
import cn.xidian.entity.StudentCourse;
import cn.xidian.entity.StudentItem;
import cn.xidian.entity.Survey;
import cn.xidian.entity.SurveyReplyer;
import cn.xidian.entity.SurveySelector;
import cn.xidian.entity.SurveySelectorDouble;
import cn.xidian.entity.SurveySelectorRelate;
import cn.xidian.entity.Teacher;
import cn.xidian.entity.TextAnswer;
import cn.xidian.entity.User;
import cn.xidian.service.StudentItemService;
import cn.xidian.service.StudentService;
import cn.xidian.service.SurveyService;
import cn.xidian.service.TeacherService;
import cn.xidian.service.TeacherStudentService;
import cn.xidian.web.bean.EvaluateResult;
import cn.xidian.web.bean.TableData;

@SuppressWarnings("serial")
@Component(value = "JsonAction")
@Scope("prototype")
public class JsonAction extends ActionSupport implements RequestAware {
	private List<ItemEvaluateScore> itemEvaluateScores;
	private List<ItemEvaluatePoint> itemEvaluatePoints;
	private ItemEvaluateScore itemEvaluateScore;
	private Integer pointId;
	private Integer itemTypeId;
	private Integer gradeId;

	// 汇总添加
	private List<Student> stus;
	private Integer clazz;
	private List<StudentItem> items;
	private StudentItemService studentItemService;
	private Clazz cla;
	private String schoolYear;
	private EvaluateResult evaluateResult;
	private Integer size;
	private List<StudentCourse> studentCourses;
	private Student s;
	private Date startTime;
	private Date endTime;
	private String start;
	private String end;
	private Date date1;
	private Date date2;
	private Date date3;
	private Double average;
	private Integer page;
	private PageBean<EvaluateResult> pageBean;
	private PageBean<StudentCourse> pbStuCours;
	private PageBean<StudentItem> siPageBean;
	private String stuNum;
	private List<ItemEvaluateType> itemEvaluateTypes;
	private StuEvaluateResult stuEvaluateResult;
	private List<StuEvaluateResult> stuEvaluateResults;
	private Double[] MaxScoreArr;

	// 问卷添加
	private Teacher teacher;
	private PageBean<Survey> suPageBean;
	private List<SurveySelector> surveySelectors;
	private Integer surveyId;
	private Integer questionId;
	private String[] sels;
	private Integer role;
	private PageBean<TextAnswer> taPageBean;
	private PageBean<GradeClazzSurvey> gcsPageBean;
	private Integer amount;
	private PageBean<SurveyReplyer> srPageBean;// 查看问卷参与者的列表
	private String strEndTime;
	private Survey survey;
	private List<SurveySelectorDouble> ssd;
	private List<TableData> tds;
	private List<String> yaxis;
	Map<String, Object> session = ActionContext.getContext().getSession();
	User tUser = (User) session.get("tUser");

	@Resource(name = "studentItemServiceImpl")
	public void setStudentItemService(StudentItemService studentItemService) {
		this.studentItemService = studentItemService;
	}

	private StudentService studentService;

	@Resource(name = "studentServiceImpl")
	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}

	private SurveyService surveyService;

	@Resource(name = "surveyServiceImpl")
	public void set(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	TeacherService teacherService;

	@Resource(name = "teacherServiceImpl")
	public void setTeacherService(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	private TeacherStudentService teacherStudentService;

	@Resource(name = "teacherStudentServiceImpl")
	public void setTeacherStudentService(TeacherStudentService teacherStudentService) {
		this.teacherStudentService = teacherStudentService;
	}

	// 根据小指标点查找该指标点对应的得分项(学生)
	public String selectItemEvaScores() {
		itemEvaluateScores = studentItemService.selectItemEvaScoresByPointId(pointId);
		return "list";
	}

	// 根据项目的类型查找对应的小指标点（学生）
	public String selectItemEvaPoint() {
		itemEvaluatePoints = studentItemService.selectItemEvaPoints(itemTypeId);
		return "list";
	}

	// 根据得分项的ID获取该项的具体分数（老师）
	public String selectItemEvaScore() {
		itemEvaluateScore = studentItemService.selectItemEvaScore(gradeId);
		return "list";
	}

	// 老师评估学生的项目得分
	public String evaluateStuSummaryByClazz() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		start = simpleDateFormat.format(startTime);
		end = simpleDateFormat.format(endTime);
		try {
			date1 = simpleDateFormat.parse(start);
			date2 = simpleDateFormat.parse(end);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date2);
			calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			date3 = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stus = teacherStudentService.selectChargeStus(clazz);
		cla = teacherStudentService.selectClazzById(clazz);
		itemEvaluateTypes = studentItemService.selectItemEvaTypes();
		size = teacherStudentService.selectSummaryStuEvas(clazz, schoolYear).size();
		if (size != 0) {
			teacherStudentService.deleteStuEvas(clazz, schoolYear);
		}
		for (Student element : stus) {
			String sch = element.getStuSchNum();
			DecimalFormat df = new DecimalFormat("######0.00");
			for (int i = 1; i <= itemEvaluateTypes.size(); i++) {
				Double M = 0.0;

				items = studentItemService.selectItemByLimitTimes(i, sch, date1, date3);
				StuEvaluateResult ser = new StuEvaluateResult();
				for (StudentItem st : items) {
					M += Double.parseDouble(st.getItemScore());

				}
				ser.setClazz(cla);
				ser.setmScore(Double.parseDouble(df.format(M)));
				ser.setStudent(element);
				ser.setSchoolYear(schoolYear);
				ser.setItemEvaluateType(itemEvaluateTypes.get(i - 1));
				teacherStudentService.addStuEvaScore(ser);
			}
		}
		return "list";
	}

	// 查看评估结果（老师和学生都要）
	public String selectSummaryEva() {
		pageBean = teacherStudentService.findByPageCid(clazz, schoolYear, page);// 根据一级分类查询带分页的商品
		// 将PageBean存入到值栈中
		ActionContext.getContext().getValueStack().set("pageBean", pageBean);
		return "list";
	}

	// 计算学生的课程加权平均分
	public Double countGrade(Student stu, String schoolYear) {
		Double allCredit = 0.0;
		Double allCreditAndScore = 0.0;
		studentCourses = teacherStudentService.selectStuGrades(stu.getStuId(), schoolYear);
		for (StudentCourse st : studentCourses) {
			st.getCourse().getCursCredit();
			st.getFinEvaValue();
			allCredit += st.getCourse().getCursCredit();
			allCreditAndScore += st.getCourse().getCursCredit() * st.getFinEvaValue();
		}

		if (allCredit == 0.0) {
			average = allCreditAndScore / 1.00;
		} else {
			average = allCreditAndScore / allCredit;
		}
		return average;
	}

	// 根据学年查询学生的成绩（学生）
	public String selectStuCourseGrades() {
		String schNum = tUser.getSchNum();
		s = studentService.selectInfBySchNum(schNum);
		if (schoolYear.equals("-")) {
			pbStuCours = (studentService.selectStuAllGradesById(s.getStuId(), page));
		} else {
			pbStuCours = teacherStudentService.selectStuGradesByPage(s.getStuId(), schoolYear, page);
		}
		return "list";
	}

	// 学生查询评估结果显示雷达图
	public String selectEvaluateResult() {
		String schNum = tUser.getSchNum();
		s = studentService.selectInfBySchNum(schNum);
		stuEvaluateResults = studentService.selectStuEvaluateResults(s.getStuId(), schoolYear);
		itemEvaluateTypes = studentItemService.selectItemEvaTypes();
		Double[] arr = new Double[itemEvaluateTypes.size()];
		for (int i = 1; i <= itemEvaluateTypes.size(); i++) {
			List<StuEvaluateResult> sEvaluateResults = teacherStudentService.selectMaxEva(i, schoolYear);
			if (sEvaluateResults.size() != 0) {
				arr[i - 1] = sEvaluateResults.get(0).getmScore();
			}
		}
		MaxScoreArr = arr;
		return "list";
	}

	// 查找学生项目列表
	public String selectItem() {
		String schNum;
		if (stuNum == null) {
			schNum = tUser.getSchNum();
		} else {
			schNum = stuNum;
		}
		if (page == null) {
			page = 1;
		}
		siPageBean = studentItemService.selectByStuNum(schNum, page);
		return "list";
	}

	// 翻页查找问卷列表（老师）
	public String selectSurveys() {
		String tchrSchNum = tUser.getSchNum();
		teacher = teacherService.selectInfBySchNum(tchrSchNum);
		suPageBean = surveyService.selectAllSurveys(teacher, page);
		return "list";
	}

	// 发布问卷（老师）
	public String publishSurvey() {
		surveyService.publishSurvey(surveyId);
		return "list";
	}

	// 查找问卷结果
	public String selectSurveyResult() {
		surveySelectors = surveyService.selectSurveySelectors(surveyId, questionId);
		return "list";
	}

	// 删除问卷
	public String deleteSurvey() {
		surveyService.deleteSurvey(surveyId);
		return "list";
	}

	// 查询属于老师或学生的问卷
	public String selectStuSurveys() {
		if (tUser != null) {
			String userRole = tUser.getIdentity().toString();
			Student student = studentService.selectInfBySchNum(tUser.getSchNum());
			if (userRole == "STUDENT") {
				role = 1;
			}
			if (userRole == "TEACHER") {
				role = 2;
			}
			gcsPageBean = surveyService.selectStuSurveys(role, page, student.getClazz().getClaId(),
					student.getClazz().getClaGrade());
		}
		return "list";
	}

	// 查询属于老师或学生的问卷
	public String selectTchrSurveys() {
		if (tUser != null) {
			String userRole = tUser.getIdentity().toString();
			if (userRole == "STUDENT") {
				role = 1;
			}
			if (userRole == "TEACHER") {
				role = 2;
			}
			suPageBean = surveyService.selectTchrSurveys(role, page);
		}
		return "list";
	}

	// 结束问卷调查
	public String overSurvey() {
		surveyService.overSurvey(surveyId);
		return "list";
	}

	// 查看问卷的问本题的信息
	public String selectSurveyTextResult() {
		taPageBean = surveyService.selectSurveyTextResult(page, surveyId, questionId);
		return "list";
	}

	// 查看是否做过问卷
	public String selectSurveyReplayer() {
		String schNum = tUser.getSchNum();
		String userRole = tUser.getIdentity().toString();
		Integer userId = null;
		if (userRole == "STUDENT") {
			s = studentService.selectInfBySchNum(schNum);
			userId = s.getStuId();
		}
		if (userRole == "TEACHER") {
			teacher = teacherService.selectInfBySchNum(schNum);
			userId = teacher.getTchrId();
		}
		List<SurveyReplyer> replyers = surveyService.selectSurveyReplayer(surveyId, userId, userRole);
		amount = replyers.size();
		return "list";
	}

	public String selectSurveyReplyersById() {
		if (page == null) {
			page = 1;
		}
		srPageBean = surveyService.selectSurveyReplyerById(surveyId, page);
		return "list";
	}

	public String updateSurveyDate() {

		surveyService.updateSurveyDate(surveyId, strEndTime);
		return "list";
	}

	// 获取问卷表格的数据统计表
	public String selectSurveyTableResult() {
		surveySelectors = surveyService.selectSurveySelectors(surveyId, questionId);
		yaxis = new ArrayList<String>();
		tds = new ArrayList<TableData>();
		for (int i = 0; i < surveySelectors.size(); i++) {
			List<SurveySelectorRelate> ssr = surveyService
					.selectSurveySelectorRelates(surveySelectors.get(i).getSelectorId());
			TableData td = new TableData();
			td.setName(surveySelectors.get(i).getContent());
			Integer[] selectorArr = new Integer[ssr.size()];
			for (int j = 0; j < ssr.size(); j++) {
				selectorArr[j] = ssr.get(j).getSumNum();
				String ss = ssr.get(j).getSurveySelectorDouble().getContent();
				if (i == 0) {
					yaxis.add(ssr.get(j).getSurveySelectorDouble().getContent());
				}
			}
			td.setData(selectorArr);
			tds.add(td);
		}
		System.out.println(yaxis);
		return "list";
	}

	public String selectSurveyById() {
		survey = surveyService.selectSurveyById(surveyId);
		return "list";
	}

	public String selectStudentInfo() {
		String schNum = tUser.getSchNum();
		s = studentService.selectInfBySchNum(schNum);
		return "list";
	}

	public List<ItemEvaluateScore> getItemEvaluateScores() {
		return itemEvaluateScores;
	}

	public void setItemEvaluateScores(List<ItemEvaluateScore> itemEvaluateScores) {
		this.itemEvaluateScores = itemEvaluateScores;
	}

	public Integer getPointId() {
		return pointId;
	}

	public void setPointId(Integer pointId) {
		this.pointId = pointId;
	}

	@Override
	public void setRequest(Map<String, Object> arg0) {
		// TODO Auto-generated method stub

	}

	public Integer getItemTypeId() {
		return itemTypeId;
	}

	public void setItemTypeId(Integer itemTypeId) {
		this.itemTypeId = itemTypeId;
	}

	public List<ItemEvaluatePoint> getItemEvaluatePoints() {
		return itemEvaluatePoints;
	}

	public void setItemEvaluatePoints(List<ItemEvaluatePoint> itemEvaluatePoints) {
		this.itemEvaluatePoints = itemEvaluatePoints;
	}

	public ItemEvaluateScore getItemEvaluateScore() {
		return itemEvaluateScore;
	}

	public void setItemEvaluateScore(ItemEvaluateScore itemEvaluateScore) {
		this.itemEvaluateScore = itemEvaluateScore;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public List<Student> getStus() {
		return stus;
	}

	public void setStus(List<Student> stus) {
		this.stus = stus;
	}

	public Integer getClazz() {
		return clazz;
	}

	public void setClazz(Integer clazz) {
		this.clazz = clazz;
	}

	public List<StudentItem> getItems() {
		return items;
	}

	public void setItems(List<StudentItem> items) {
		this.items = items;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Clazz getCla() {
		return cla;
	}

	public void setCla(Clazz cla) {
		this.cla = cla;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Student getS() {
		return s;
	}

	public void setS(Student s) {
		this.s = s;
	}

	public List<StudentCourse> getStudentCourses() {
		return studentCourses;
	}

	public void setStudentCourses(List<StudentCourse> studentCourses) {
		this.studentCourses = studentCourses;
	}

	public EvaluateResult getEvaluateResult() {
		return evaluateResult;
	}

	public void setEvaluateResult(EvaluateResult evaluateResult) {
		this.evaluateResult = evaluateResult;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public PageBean<EvaluateResult> getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean<EvaluateResult> pageBean) {
		this.pageBean = pageBean;
	}

	public PageBean<StudentCourse> getPbStuCours() {
		return pbStuCours;
	}

	public void setPbStuCours(PageBean<StudentCourse> pbStuCours) {
		this.pbStuCours = pbStuCours;
	}

	public PageBean<StudentItem> getSiPageBean() {
		return siPageBean;
	}

	public void setSiPageBean(PageBean<StudentItem> siPageBean) {
		this.siPageBean = siPageBean;
	}

	public String getStuNum() {
		return stuNum;
	}

	public void setStuNum(String stuNum) {
		this.stuNum = stuNum;
	}

	public PageBean<Survey> getSuPageBean() {
		return suPageBean;
	}

	public void setSuPageBean(PageBean<Survey> suPageBean) {
		this.suPageBean = suPageBean;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public List<SurveySelector> getSurveySelectors() {
		return surveySelectors;
	}

	public void setSurveySelectors(List<SurveySelector> surveySelectors) {
		this.surveySelectors = surveySelectors;
	}

	public Integer getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Integer surveyId) {
		this.surveyId = surveyId;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String[] getSels() {
		return sels;
	}

	public void setSels(String[] sels) {
		this.sels = sels;
	}

	public PageBean<TextAnswer> getTaPageBean() {
		return taPageBean;
	}

	public void setTaPageBean(PageBean<TextAnswer> taPageBean) {
		this.taPageBean = taPageBean;
	}

	public List<ItemEvaluateType> getItemEvaluateTypes() {
		return itemEvaluateTypes;
	}

	public void setItemEvaluateTypes(List<ItemEvaluateType> itemEvaluateTypes) {
		this.itemEvaluateTypes = itemEvaluateTypes;
	}

	public StuEvaluateResult getStuEvaluateResult() {
		return stuEvaluateResult;
	}

	public void setStuEvaluateResult(StuEvaluateResult stuEvaluateResult) {
		this.stuEvaluateResult = stuEvaluateResult;
	}

	public List<StuEvaluateResult> getStuEvaluateResults() {
		return stuEvaluateResults;
	}

	public void setStuEvaluateResults(List<StuEvaluateResult> stuEvaluateResults) {
		this.stuEvaluateResults = stuEvaluateResults;
	}

	public Double[] getMaxScoreArr() {
		return MaxScoreArr;
	}

	public void setMaxScoreArr(Double[] maxScoreArr) {
		MaxScoreArr = maxScoreArr;
	}

	public PageBean<GradeClazzSurvey> getGcsPageBean() {
		return gcsPageBean;
	}

	public void setGcsPageBean(PageBean<GradeClazzSurvey> gcsPageBean) {
		this.gcsPageBean = gcsPageBean;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public PageBean<SurveyReplyer> getSrPageBean() {
		return srPageBean;
	}

	public void setSrPageBean(PageBean<SurveyReplyer> srPageBean) {
		this.srPageBean = srPageBean;
	}

	public String getStrEndTime() {
		return strEndTime;
	}

	public void setStrEndTime(String strEndTime) {
		this.strEndTime = strEndTime;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public List<SurveySelectorDouble> getSsd() {
		return ssd;
	}

	public void setSsd(List<SurveySelectorDouble> ssd) {
		this.ssd = ssd;
	}

	public List<TableData> getTds() {
		return tds;
	}

	public void setTds(List<TableData> tds) {
		this.tds = tds;
	}

	public List<String> getYaxis() {
		return yaxis;
	}

	public void setYaxis(List<String> yaxis) {
		this.yaxis = yaxis;
	}

}
