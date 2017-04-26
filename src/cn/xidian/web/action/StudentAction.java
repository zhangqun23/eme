package cn.xidian.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.xidian.entity.Course;
import cn.xidian.entity.GradeClazzSurvey;
import cn.xidian.entity.ItemEvaluatePoint;
import cn.xidian.entity.ItemEvaluateScore;
import cn.xidian.entity.ItemEvaluateType;
import cn.xidian.entity.ItemFile;
import cn.xidian.entity.PageBean;
import cn.xidian.entity.Student;
import cn.xidian.entity.StudentActivity;
import cn.xidian.entity.StudentCourse;
import cn.xidian.entity.StudentItem;
import cn.xidian.entity.Survey;
import cn.xidian.entity.SurveyQuestion;
import cn.xidian.entity.SurveyReplyer;
import cn.xidian.entity.SurveySelector;
import cn.xidian.entity.Teacher;
import cn.xidian.entity.TeachingTarget;
import cn.xidian.entity.TeachingTargetEvaluate;
import cn.xidian.entity.TextAnswer;
import cn.xidian.entity.User;
import cn.xidian.exception.CourseNotExistException;
import cn.xidian.exception.CursRulesNotExistException;
import cn.xidian.service.CourseService;
import cn.xidian.service.StudentActivityService;
import cn.xidian.service.StudentCourseService;
import cn.xidian.service.StudentItemService;
import cn.xidian.service.StudentService;
import cn.xidian.service.SurveyService;
import cn.xidian.service.TeachingTargetService;
import cn.xidian.web.bean.B1;
import cn.xidian.web.bean.B2;
import cn.xidian.web.service.CourseTargetDetailService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component(value = "StudentAction")
@Scope("prototype")
public class StudentAction extends ActionSupport implements RequestAware {
	private Student s;
	private Map<String, Object> request;
	private String clazz;
	private String password;
	private Course course;
	// 学生目标
	private String shortGoal;
	private String midGoal;
	private String longGoal;
	// 参加活动
	private List<StudentActivity> orgActivities = new LinkedList<StudentActivity>();// 组织活动
	private List<StudentActivity> attendActivities = new LinkedList<StudentActivity>();// 参与活动
	private List<StudentActivity> socialActivities = new LinkedList<StudentActivity>();// 社会实践
	private StudentActivity activity;
	private Integer actId;
	// 参与项目及获奖
	private List<ItemFile> itemFiles = new LinkedList<ItemFile>();
	private StudentItem item;
	private Integer itemId;
	private static Integer itemid;
	private PageBean<StudentItem> siPageBean;
	// 成绩查询
	private String year;
	private String term;
	private List<StudentCourse> stuCurs;
	// 文件上传
	private File[] file;
	private String[] fileFileName;
	private List<ItemFile> allFile;

	// 学生项目的管理
	private List<ItemEvaluateType> itemEvaluateTypes;
	private List<ItemEvaluatePoint> itemEvaluatePoints;
	private List<ItemEvaluateScore> itemEvaluateScores;
	private ItemEvaluateType itemEvaluateType;
	private ItemEvaluatePoint itemEvaluatePoint;
	private ItemEvaluateScore itemEvaluateScore;

	// 学生评估管理
	private List<StudentCourse> studentCourses;
	private PageBean<StudentCourse> pageBean;
	private Integer page;
	// 问卷添加
	private PageBean<Survey> suPageBean;
	private Survey survey;
	private Integer surveyId;
	private Teacher teacher;
	private Student student;
	private List<SurveyQuestion> surveyQuestions;
	private List<SurveySelector> surveySelectors;
	private List<TextAnswer> textAnswers;
	private Integer role;
	private String message;
	private PageBean<GradeClazzSurvey> gcsPageBean;

	// 获得学生一门课程的达成度b1、b2的详细列表
	String stuCursId;
	private List<B1> claCursB1s = new LinkedList<B1>();
	private List<B2> claCursB2s = new LinkedList<B2>();

	Map<String, Object> session = ActionContext.getContext().getSession();
	User tUser = (User) session.get("tUser");

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

	private StudentActivityService studentActivityService;

	@Resource(name = "studentActivityServiceImpl")
	public void setStudentActivityService(StudentActivityService studentActivityService) {
		this.studentActivityService = studentActivityService;
	}

	private StudentItemService studentItemService;

	@Resource(name = "studentItemServiceImpl")
	public void setStudentItemService(StudentItemService studentItemService) {
		this.studentItemService = studentItemService;
	}

	private StudentCourseService studentCourseService;

	@Resource(name = "studentCourseServiceImpl")
	public void setStudentCourseService(StudentCourseService studentCourseService) {
		this.studentCourseService = studentCourseService;
	}

	TeachingTargetService teachingTargetService;

	@Resource(name = "teachingTargetServiceImpl")
	public void setTeachingTargetService(TeachingTargetService teachingTargetService) {
		this.teachingTargetService = teachingTargetService;
	}

	CourseService courseService;

	@Resource(name = "courseServiceImpl")
	public void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}

	public String selectBasicByNum() {
		String schNum = tUser.getSchNum();
		s = studentService.selectInfBySchNum(schNum);
		clazz = s.getClazz().getClaName();
		return "student";
	}

	/* 修改界面获得学生信息 */
	public String selectInfByNum() {
		String schNum = tUser.getSchNum();
		s = studentService.selectInfBySchNum(schNum);
		/* awards = teacherService.loadAwardBySchNum(schNum); */
		return "student";
	}

	public String modifyBasicInf() {
		String schNum = tUser.getSchNum();
		s.setStuSchNum(schNum);
		boolean isSuccess = studentService.updateInfBySchNum(s);
		// s = ssi.selectInfBySchNum(schNum);
		if (isSuccess) {
			request.put("Message", "修改成功！");
			return "student";
		} else {
			request.put("Message", "对不起，修改失败！");
			return "student";
		}
	}

	public String modifyPassword() {
		String schNum = tUser.getSchNum();
		boolean isSuccess = studentService.modifyPassword(schNum, password);
		s = studentService.selectInfBySchNum(schNum);
		if (isSuccess) {
			request.put("Message", "修改密码成功！");
			return "student";
		} else {
			request.put("Message", "对不起，修改密码失败！");
			return "student";
		}
	}

	public String updateShortGoal() {
		String schNum = tUser.getSchNum();
		s = studentService.selectInfBySchNum(schNum);
		s.setShortGoal(shortGoal);
		studentService.updateGoal(s);
		request.put("Message", "修改成功！");
		return "student";
	}

	public String updateMidGoal() {
		String schNum = tUser.getSchNum();
		s = studentService.selectInfBySchNum(schNum);
		s.setMidGoal(midGoal);
		studentService.updateGoal(s);
		request.put("Message", "修改成功！");
		return "student";
	}

	public String updateLongGoal() {
		String schNum = tUser.getSchNum();
		s = studentService.selectInfBySchNum(schNum);
		s.setLongGoal(longGoal);
		studentService.updateGoal(s);
		request.put("Message", "修改成功！");
		return "student";
	}

	/* 学生活动 */
	public String selectActivity() {
		String schNum = tUser.getSchNum();
		List<StudentActivity> sa = studentActivityService.selectByStuNum(schNum);
		for (int i = 0; i < sa.size(); i++) {
			if (sa.get(i).getType().equals("组织活动")) {
				orgActivities.add(sa.get(i));
			}
			if (sa.get(i).getType().equals("参与活动")) {
				attendActivities.add(sa.get(i));
			}
			if (sa.get(i).getType().equals("社会实践")) {
				socialActivities.add(sa.get(i));
			}
		}
		return "student";
	}

	public String deleteActivity() {
		studentActivityService.deleteById(actId);
		request.put("Message", "删除成功！");
		return "stuActList";
	}

	public String addActivity() {
		String schNum = tUser.getSchNum();
		activity.setStudent(studentService.selectInfBySchNum(schNum));
		studentActivityService.add(activity);
		request.put("Message", "添加成功！");
		return "stuActList";
	}

	/* 学生参与项目及获奖 */
	public String selectItem() {

		String schNum = tUser.getSchNum();
		if (page == null) {
			page = 1;
		}
		siPageBean = studentItemService.selectByStuNum(schNum, page);
		/* items = studentItemService.selectByStuNum(schNum); */
		return "student";
	}

	public String createword() {
		item = studentItemService.selectItemInfo(itemid);
		String realpath = "";
		realpath = ServletActionContext.getServletContext().getRealPath("export\\ITEM.doc");
		System.out.println(realpath);
		try {
			InputStream is = new FileInputStream(realpath);
			HWPFDocument doc = new HWPFDocument(is);
			Range range = doc.getRange();
			// 把range范围内的${reportDate}替换为当前的日期
			range.replaceText("${xiangmubianhao}", item.getItemNum());
			range.replaceText("${xiangmumingcheng}", item.getItemName());
			range.replaceText("${zhubandanwei}", item.getItemUnit());
			range.replaceText("${xiangmuleibie}", item.getItemEvaluateType().getItemEvaTypeName());
			range.replaceText("${jiangxiangdengji}", item.getItemEvaluateScore().getItemEvaScoreName());
			range.replaceText("${pinjiazhibiao}", item.getItemEvaluatePoint().getItemEvaPointName());
			range.replaceText("${xiangmubiaozhangduixiang}", item.getItemPrizeObject());
			range.replaceText("${xiangmucanyujuese}", item.getItemRole());
			range.replaceText("${shenhezhuangtai}", item.getItemState());
			range.replaceText("${shenhedefen}", item.getItemScore());
			range.replaceText("${shenheyijian}", item.getNote());
			String filepath = ServletActionContext.getServletContext().getRealPath("exportword\\321.doc");
			OutputStream os = new FileOutputStream(filepath);
			System.out.println(filepath);
			// 把doc输出到输出流中
			doc.write(os);
			is.close();
			os.close();
			request.put("Message", "导出成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "student";
	}

	public String deleteItem() {
		// 判断是否能够删除
		item = studentItemService.selectItemInfo(itemId);
		if (!item.getItemState().toString().equals("待审核")) {
			request.put("Message", "已审核，不允许删除！");
			return "stuItemList";
		}
		// 删除图片
		String realPath = ServletActionContext.getServletContext().getRealPath("/upload");// 实际路径
		itemFiles = studentItemService.selectItemFile(itemId);
		if (studentItemService.deleteFileById(itemId)) {
			if (studentItemService.deleteItemById(itemId)) {
				for (ItemFile element : itemFiles) {
					File file = new File(realPath + "/" + element.getSaveFileName());
					if (file.exists()) {
						file.delete();
					}
				}
				request.put("Message", "删除成功！");
			} else {
				request.put("Message", "删除失败！");
			}
		} else {
			request.put("Message", "删除失败！");
		}
		return "stuItemList";
	}

	public String addItem() throws Exception {
		item.setItemState("待审核");
		item.setNote("无");
		item.setItemScore("0");
		Date date = new Date();
		item.setItemSubmitDate(date);
		String schNum = tUser.getSchNum();
		s = studentService.selectInfBySchNum(schNum);
		item.setStudent(s);
		studentItemService.add(item);
		if (file != null) {
			fileUpload(item);
		}
		request.put("Message", "添加成功！");
		return "stuItemList";
	}

	public void fileUpload(StudentItem item) throws Exception {
		ServletActionContext.getRequest().setCharacterEncoding("UTF-8");
		String realPath = ServletActionContext.getServletContext().getRealPath("/upload");// 实际路径
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String schNum = tUser.getSchNum();
		String fileNewName = schNum + '_' + format.format(new Date());// 给文件新的存储名字
		File savedir = new File(realPath);
		if (!savedir.getParentFile().exists())
			savedir.getParentFile().mkdirs();
		for (int i = 0; i < file.length; i++) {
			ItemFile itemFile = new ItemFile();// 实例化ItemFile
			int random = (int) (1 + Math.random() * 10);
			char c = (char) (int) (Math.random() * 26 + 97);
			String suffix = fileFileName[i].substring(fileFileName[i].lastIndexOf(".") + 1);
			File savefile = new File(savedir, fileNewName + '_' + c + random + '.' + suffix);
			FileUtils.copyFile(file[i], savefile);
			itemFile.setFileName(fileFileName[i]);
			itemFile.setSaveFileName(fileNewName + '_' + c + random + '.' + suffix);
			itemFile.setStudentItem(item);
			itemFile.setFileType(suffix);
			studentItemService.saveAttachment(itemFile);
		}

	}

	// 学生查询成绩
	public String selectStuPer() {
		String schNum = tUser.getSchNum();
		String currTerm = year + "-" + term;
		stuCurs = studentCourseService.selectByNumTerm(schNum, currTerm);
		if (stuCurs.size() == 0) {
			request.put("Message", "没有找到相关信息！");
		}
		return "student";
	}

	// 查看学生获奖情况详情
	public String selectItemInfo() {
		itemid = itemId;
		allFile = studentItemService.selectItemFile(itemId);
		item = studentItemService.selectItemInfo(itemId);
		itemEvaluateType = studentItemService.selectItemEvaType(item.getItemEvaluateType().getItemEvaTypeId());
		itemEvaluatePoint = studentItemService.selectItemEvaPoint(item.getItemEvaluatePoint().getItemEvaPointId());
		itemEvaluateScore = studentItemService.selectItemEvaScore(item.getItemEvaluateScore().getItemEvaScoreId());
		return "student";
	}

	// 在学生项目添加时显示项目类别下拉框
	public String selectItemEvaType() {
		itemEvaluateTypes = studentItemService.selectItemEvaTypes();
		itemEvaluatePoints = studentItemService.selectItemEvaPoints(1);
		itemEvaluateScores = studentItemService.selectItemEvaScoresByPointId(1);
		return "student";
	}

	// 查询学生成绩
	public String selectStuAllGradesById() {
		String schNum = tUser.getSchNum();
		s = studentService.selectInfBySchNum(schNum);
		if (page == null) {
			page = 1;
		}
		pageBean = studentService.selectStuAllGradesById(s.getStuId(), page);
		/*
		 * studentCourses=studentService.selectStuAllGradesById(s.getStuId());
		 */
		return "student";
	}

	// 查询学生问卷列表
	public String selectSurveys() {
		if (page == null) {
			page = 1;
		}
		if (tUser != null) {
			String userRole = tUser.getIdentity().toString();
			Student student = studentService.selectInfBySchNum(tUser.getSchNum());
			if (userRole == "STUDENT") {
				role = 1;
			}
			gcsPageBean = surveyService.selectStuSurveys(role, page, student.getClazz().getClaId(),
					student.getClazz().getClaGrade());
		}
		try {
			if (message != null) {
				message = new String(message.getBytes("ISO-8859-1"), "UTF-8");
				request.put("Message", message);
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "student";
	}

	// 查找某个问卷的全部信息
	public String selectSurveyInfo() {
		survey = surveyService.selectSurveyById(surveyId);
		surveyQuestions = surveyService.selectQuestionBysurveyId(surveyId);
		return "student";
	}

	// 提交学生填写的问卷结果
	public String addSurveyDone() {
		// 判断学生是否已经提交过问卷
		Student student = studentService.selectInfBySchNum(tUser.getSchNum());
		List<SurveyReplyer> replayers = surveyService.selectSurveyReplayer(surveyId, student.getStuId(),
				tUser.getIdentity().toString());
		if (replayers.size() == 0) {// 存储问卷者信息
			if (tUser != null) {
				survey = surveyService.selectSurveyById(surveyId);
				SurveyReplyer surveyReplyer = new SurveyReplyer();
				Date replyTime = new Date();// 做問卷的时间
				surveyReplyer.setStudent(student);
				surveyReplyer.setSurvey(survey);
				surveyReplyer.setReplyTime(replyTime);
				boolean isSuccess = surveyService.addSurveyDone(surveyReplyer, surveySelectors, textAnswers, survey);
				if (isSuccess) {
					request.put("Message", "提交成功！");
					message = "提交成功！！";
				} else {
					request.put("Message", "提交失败！");
					message = "提交失败！！";
				}

			} else {
				request.put("Message", "提交失败,用户信息无效，请重新登陆！");
				message = "提交失败,用户信息无效，请重新登陆！！";
			}

		} else {
			request.put("Message", "提交失败，不能重复填写问卷！");
			message = "提交失败，不能重复填写问卷！";
		}
		return "surveyDone";
	}

	public String updateStuMail() {
		String mail = s.getStuMail();
		String schNum = tUser.getSchNum();
		s = studentService.selectInfBySchNum(schNum);
		s.setStuMail(mail);
		boolean isSuccess = studentService.updateInfBySchNum(s);
		if (isSuccess) {
			request.put("Message", "提交成功！");
			message = "提交成功！！";
		} else {
			request.put("Message", "提交失败！");
			message = "提交失败！！";
		}
		return "student";
	}

	// 学生查询单门课程达成度
	public String getStuCaculateTarget() {
		try {
			StudentCourse sCourse = studentService.getSCourse(Integer.parseInt(stuCursId));
			List<TeachingTarget> targets = teachingTargetService.selectByCursName(sCourse.getCourse().getCursName());
			List<TeachingTargetEvaluate> stuEvaluate = studentService.caculateBySCourse(sCourse);
			CourseTargetDetailService courseTargetDetailService = new CourseTargetDetailService();
			claCursB1s = courseTargetDetailService.getB1(targets, stuEvaluate);
			claCursB2s = studentService.getB2(sCourse, claCursB1s);
			course = courseService.findByName(sCourse.getCourse().getCursName());
		} catch (CursRulesNotExistException e) {
			request.put("Message", e.getMessage());
		}

		return "studentTarget";
	}

	public Student getS() {
		return s;
	}

	public void setS(Student s) {
		this.s = s;
	}

	public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

	public String getShortGoal() {
		return shortGoal;
	}

	public void setShortGoal(String shortGoal) {
		this.shortGoal = shortGoal;
	}

	public String getMidGoal() {
		return midGoal;
	}

	public void setMidGoal(String midGoal) {
		this.midGoal = midGoal;
	}

	public String getLongGoal() {
		return longGoal;
	}

	public void setLongGoal(String longGoal) {
		this.longGoal = longGoal;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public List<StudentActivity> getOrgActivities() {
		return orgActivities;
	}

	public void setOrgActivities(List<StudentActivity> orgActivities) {
		this.orgActivities = orgActivities;
	}

	public List<StudentActivity> getAttendActivities() {
		return attendActivities;
	}

	public void setAttendActivities(List<StudentActivity> attendActivities) {
		this.attendActivities = attendActivities;
	}

	public List<StudentActivity> getSocialActivities() {
		return socialActivities;
	}

	public void setSocialActivities(List<StudentActivity> socialActivities) {
		this.socialActivities = socialActivities;
	}

	public StudentActivity getActivity() {
		return activity;
	}

	public void setActivity(StudentActivity activity) {
		this.activity = activity;
	}

	public StudentItem getItem() {
		return item;
	}

	public void setItem(StudentItem item) {
		this.item = item;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public List<StudentCourse> getStuCurs() {
		return stuCurs;
	}

	public void setStuCurs(List<StudentCourse> stuCurs) {
		this.stuCurs = stuCurs;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<String, Object> getRequest() {
		return request;
	}

	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

	public String excute() {
		return "student";
	}

	public List<ItemFile> getItemFiles() {
		return itemFiles;
	}

	public void setItemFiles(List<ItemFile> itemFiles) {
		this.itemFiles = itemFiles;
	}

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public String[] getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}

	public List<ItemFile> getAllFile() {
		return allFile;
	}

	public void setAllFile(List<ItemFile> allFile) {
		this.allFile = allFile;
	}

	public List<ItemEvaluateType> getItemEvaluateTypes() {
		return itemEvaluateTypes;
	}

	public void setItemEvaluateTypes(List<ItemEvaluateType> itemEvaluateTypes) {
		this.itemEvaluateTypes = itemEvaluateTypes;
	}

	public List<ItemEvaluatePoint> getItemEvaluatePoints() {
		return itemEvaluatePoints;
	}

	public void setItemEvaluatePoints(List<ItemEvaluatePoint> itemEvaluatePoints) {
		this.itemEvaluatePoints = itemEvaluatePoints;
	}

	public List<ItemEvaluateScore> getItemEvaluateScores() {
		return itemEvaluateScores;
	}

	public void setItemEvaluateScores(List<ItemEvaluateScore> itemEvaluateScores) {
		this.itemEvaluateScores = itemEvaluateScores;
	}

	public ItemEvaluateType getItemEvaluateType() {
		return itemEvaluateType;
	}

	public void setItemEvaluateType(ItemEvaluateType itemEvaluateType) {
		this.itemEvaluateType = itemEvaluateType;
	}

	public ItemEvaluatePoint getItemEvaluatePoint() {
		return itemEvaluatePoint;
	}

	public void setItemEvaluatePoint(ItemEvaluatePoint itemEvaluatePoint) {
		this.itemEvaluatePoint = itemEvaluatePoint;
	}

	public ItemEvaluateScore getItemEvaluateScore() {
		return itemEvaluateScore;
	}

	public void setItemEvaluateScore(ItemEvaluateScore itemEvaluateScore) {
		this.itemEvaluateScore = itemEvaluateScore;
	}

	public List<StudentCourse> getStudentCourses() {
		return studentCourses;
	}

	public void setStudentCourses(List<StudentCourse> studentCourses) {
		this.studentCourses = studentCourses;
	}

	public PageBean<StudentCourse> getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean<StudentCourse> pageBean) {
		this.pageBean = pageBean;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public PageBean<StudentItem> getSiPageBean() {
		return siPageBean;
	}

	public void setSiPageBean(PageBean<StudentItem> siPageBean) {
		this.siPageBean = siPageBean;
	}

	public PageBean<Survey> getSuPageBean() {
		return suPageBean;
	}

	public void setSuPageBean(PageBean<Survey> suPageBean) {
		this.suPageBean = suPageBean;
	}

	public List<SurveyQuestion> getSurveyQuestions() {
		return surveyQuestions;
	}

	public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
		this.surveyQuestions = surveyQuestions;
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

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PageBean<GradeClazzSurvey> getGcsPageBean() {
		return gcsPageBean;
	}

	public void setGcsPageBean(PageBean<GradeClazzSurvey> gcsPageBean) {
		this.gcsPageBean = gcsPageBean;
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

	public String getStuCursId() {
		return stuCursId;
	}

	public void setStuCursId(String stuCursId) {
		this.stuCursId = stuCursId;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

}
