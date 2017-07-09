package cn.xidian.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import cn.xidian.entity.Clazz;
import cn.xidian.entity.GradeClazzSurvey;
import cn.xidian.web.bean.EvaluateResult;
import cn.xidian.entity.ItemEvaluatePoint;
import cn.xidian.entity.ItemEvaluateScore;
import cn.xidian.entity.ItemEvaluateType;
import cn.xidian.entity.ItemFile;
import cn.xidian.entity.PageBean;
import cn.xidian.entity.StuEvaluateResult;
import cn.xidian.entity.Student;
import cn.xidian.entity.StudentItem;
import cn.xidian.entity.Survey;
import cn.xidian.entity.SurveyQuestion;
import cn.xidian.entity.SurveyReplyer;
import cn.xidian.entity.SurveySelector;
import cn.xidian.entity.Teacher;
import cn.xidian.entity.TextAnswer;
import cn.xidian.entity.User;
import cn.xidian.exception.StudentExistsException;
import cn.xidian.exception.StudentNotExistException;
import cn.xidian.service.AdminStudentService;
import cn.xidian.service.ClazzService;
import cn.xidian.service.StudentItemService;
import cn.xidian.service.StudentService;
import cn.xidian.service.SurveyService;
import cn.xidian.service.TeacherService;
import cn.xidian.service.TeacherStudentService;
import cn.xidian.web.bean.AdminStuLimits;
import cn.xidian.web.service.UploadActionService;

@SuppressWarnings("serial")
@Component(value = "TeacherStudentAction")
@Scope("prototype")
public class TeacherStudentAction extends ActionSupport implements RequestAware {
	private List<Student> students;
	private Student s;
	private List<StudentItem> items = new LinkedList<StudentItem>();
	private String schNum;
	private Clazz clazz = new Clazz();
	private Integer clazzId;
	private List<ItemFile> allFile;
	private StudentItem item;
	private Integer itemId;
	private static Integer itemid;
	private List<Clazz> allClazz;
	private AdminStuLimits limits;
	private Teacher teacher;
	// 学生评估汇总添加
	private EvaluateResult evaluateResult;
	private Integer evaluateResultId;
	private String schoolYear;
	private Integer stuId;
	private Integer claId;
	private Integer page;
	/* 上传头像 */
	private String uploadUrl;
	private File file;
	/* 上传头像 */
	// 项目信息
	private ItemEvaluateType itemEvaluateType;
	private ItemEvaluatePoint itemEvaluatePoint;
	private ItemEvaluateScore itemEvaluateScore;
	private PageBean<StudentItem> siPageBean;
	private List<ItemEvaluateType> itemEvaluateTypes;
	private List<StuEvaluateResult> stuEvaluateResults;
	private Double[] MaxScoreArr;
	private Double[] ScoreArr;

	// 调查问卷添加
	private Survey survey;
	private Integer surveyId;
	private List<SurveyQuestion> qs;
	private List<Survey> surveys;
	private PageBean<Survey> suPageBean;
	private List<SurveyQuestion> surveyQuestions;
	private List<SurveySelector> surveySelectors;
	private List<TextAnswer> textAnswers;
	private Student student;
	private String type;
	private GradeClazzSurvey gcs;
	private PageBean<SurveyReplyer> srPageBean;
	private String message;

	private Map<String, Object> request;
	Map<String, Object> session = ActionContext.getContext().getSession();
	// 获取登陆者的账号
	User tUser = (User) session.get("tUser");

	private StudentItemService studentItemService;

	@Resource(name = "studentItemServiceImpl")
	public void setStudentItemService(StudentItemService studentItemService) {
		this.studentItemService = studentItemService;
	}

	AdminStudentService adminStudentService;

	@Resource(name = "adminStudentServiceImpl")
	public void setAdminStudentService(AdminStudentService adminStudentService) {
		this.adminStudentService = adminStudentService;
	}

	TeacherStudentService teacherStudentService;

	@Resource(name = "teacherStudentServiceImpl")
	public void setTeacherStudentService(TeacherStudentService teacherStudentService) {
		this.teacherStudentService = teacherStudentService;
	}

	TeacherService teacherService;

	@Resource(name = "teacherServiceImpl")
	public void setTeacherService(TeacherService teacherService) {
		this.teacherService = teacherService;
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

	private ClazzService clazzService;

	@Resource(name = "clazzServiceImpl")
	public void setClazzService(ClazzService clazzService) {
		this.clazzService = clazzService;
	}

	/* 教师查找学生信息列表 */
	public String selectChargeStus() {
		String tchrSchNum = tUser.getSchNum();
		teacher = teacherService.selectInfBySchNum(tchrSchNum);
		allClazz = teacherStudentService.findChargeCla(teacher.getTchrId());
		List<Student> s = new ArrayList<Student>();
		for (Clazz element : allClazz) {
			s.addAll(teacherStudentService.selectChargeStus(element.getClaId()));
		}
		students = s;
		return "teacher";
	}

	// 按条件查询
	public String selectStusByLimits() {
		String tchrSchNum = tUser.getSchNum();
		teacher = teacherService.selectInfBySchNum(tchrSchNum);
		setAllClazz(teacherStudentService.findChargeCla(teacher.getTchrId()));
		Set<Student> stus = teacherStudentService.selectStuLimits(limits, allClazz);
		List<Student> list = new LinkedList<Student>(stus);
		students = list;

		if (students.size() == 0) {
			this.addActionError("对不起，未找到相关信息！");
		}
		return "teacher";
	}

	// 设置教师查看学生信息
	public String selectStudentBySchNum() {
		session.put("stuSchNum", schNum);
		s = adminStudentService.selectStudentBySchNum(schNum);
		setAllClazz(clazzService.findAllCla());
		if (page == null) {
			page = 1;
		}
		siPageBean = studentItemService.selectByStuNum(schNum, page);
		/* setItems(adminStudentService.selectStuItemsBySchNum(schNum)); */
		return "teacher";
	}

	// 添加新学生
	public String addStudent() {
		try {
			clazz.setClaId(clazzId);
			s.setClazz(clazz);
			s.setIsDelete(1);
			boolean isSuccess1 = adminStudentService.addStudent(s);
			boolean isSuccess2 = uploadFile();
			if (isSuccess1 && isSuccess2) {
				request.put("Message", "添加学生成功！");
			} else {
				request.put("Message", "对不起，添加失败！");
			}
		} catch (StudentExistsException e) {
			request.put("Message", e.getMessage());
		}
		setAllClazz(clazzService.findAllCla());
		return "teacherStudentList";
	}

	// 查找所有班级
	public String selectAllClazz() {
		setAllClazz(clazzService.findAllCla());
		return "teacher";
	}

	// 查看学生获奖情况详情
	public String selectItemInfo() {
		itemid = itemId;
		allFile = studentItemService.selectItemFile(itemId);
		item = studentItemService.selectItemInfo(itemId);
		itemEvaluateType = studentItemService.selectItemEvaType(item.getItemEvaluateType().getItemEvaTypeId());
		itemEvaluatePoint = studentItemService.selectItemEvaPoint(item.getItemEvaluatePoint().getItemEvaPointId());
		itemEvaluateScore = studentItemService.selectItemEvaScore(item.getItemEvaluateScore().getItemEvaScoreId());
		return "teacher";
	}

	public String CreateWord() {
		item = studentItemService.selectItemInfo(itemid);
		String realpath = "";
		realpath = ServletActionContext.getServletContext().getRealPath("export\\ITEM.doc");

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

	// 班主任评价学生获奖项目
	public String judgeStuItem() {
		schNum = session.get("stuSchNum").toString();
		boolean isSuccess = adminStudentService.judgeStuItem(item);
		if (isSuccess) {
			request.put("Message", "审核成功");

		} else {
			request.put("Message", "审核失败");
		}
		return "teacherStudentDetail";
	}

	/* 上传头像 */
	public boolean uploadFile() {
		UploadActionService uas = new UploadActionService();
		try {
			String fileName = s.getStuSchNum() + ".jpg";
			uas.upload(file, uploadUrl, fileName); // 自定义方法调用
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/* 上传头像完 */

	// 删除学生
	public String deleteBySchNum() {
		try {
			boolean isSuccess = adminStudentService.deleteStudent(schNum);
			if (isSuccess) {
				request.put("Message", "删除成功！");
			} else {
				request.put("Message", "删除失败！");
			}
		} catch (StudentNotExistException e) {
			request.put("Message", e.getMessage());
		}
		return "teacherStudentList";
	}

	// 修改学生信息
	public String modifyBySchNum() {
		try {
			clazz.setClaId(clazzId);
			s.setClazz(clazz);
			boolean isSuccess = adminStudentService.updateStudent(s);
			if (file != null)
				uploadFile();
			if (isSuccess) {
				request.put("Message", "修改成功！");
			} else {
				request.put("Message", "修改失败！");
			}
		} catch (StudentNotExistException e) {
			request.put("Message", e.getMessage());
		}
		return "teacherStudentList";
	}

	public String selectEvaluateResultById() {
		itemEvaluateTypes = studentItemService.selectItemEvaTypes();
		Double[] arr = new Double[itemEvaluateTypes.size()];
		for (int i = 1; i <= itemEvaluateTypes.size(); i++) {
			List<StuEvaluateResult> sEvaluateResults = teacherStudentService.selectMaxEva(i, schoolYear);
			if (sEvaluateResults.size() != 0) {
				arr[i - 1] = sEvaluateResults.get(0).getmScore();
			}
		}
		MaxScoreArr = arr;
		stuEvaluateResults = studentService.selectStuEvaluateResults(stuId, schoolYear);
		Double[] arr1 = new Double[itemEvaluateTypes.size()];
		int i = 0;
		for (StuEvaluateResult e : stuEvaluateResults) {
			arr1[i] = e.getmScore();
			i++;
		}
		ScoreArr = arr1;
		return "teacher";
	}

	/*
	 * public String selectStusEvaResults() { PageBean<EvaluateResult>
	 * pageBean=teacherStudentService.findByPageCid(claId,page);//根据一级分类查询带分页的商品
	 * //将PageBean存入到值栈中
	 * ActionContext.getContext().getValueStack().set("pageBean", pageBean);
	 * return "teacher"; }
	 */

	// 创建问卷
	public String createSurvey() {

		String tchrSchNum = tUser.getSchNum();
		teacher = teacherService.selectInfBySchNum(tchrSchNum);
		Date createTime = new Date();
		survey.setCreateTime(createTime);
		survey.setState(0);
		survey.setDelState(1);
		survey.setTeacher(teacher);
		survey.setSumNum(0);
		boolean isSuccess = surveyService.createSurvey(survey);
		if (isSuccess) {
			request.put("Message", "创建成功！请设计问卷内容！");
		} else {
			request.put("Message", "创建失败！");
		}
		surveyId = survey.getSurveyId();
		return "teacher";
	}

	// 添加问卷问题
	public String addQuestion() {

		// 添加问卷问题
		boolean isSuccess = surveyService.addQuestion(qs, survey);
		if (isSuccess) {
			request.put("Message", "问卷创建成功！！");
		} else {
			request.put("Message", "问卷创建失败！");
		}
		return "teacherStudentSurveyList";
	}

	public String modifySurvey() {

		// 添加问卷
		String tchrSchNum = tUser.getSchNum();
		teacher = teacherService.selectInfBySchNum(tchrSchNum);
		Date createTime = new Date();
		survey.setCreateTime(createTime);
		survey.setState(0);
		survey.setDelState(1);
		survey.setTeacher(teacher);
		survey.setSumNum(0);
		boolean success = surveyService.createSurvey(survey);
		if (success) {// 添加问卷问题
			boolean isSuccess = false;
			try {
				isSuccess = surveyService.addQuestion(qs, survey);
			} catch (Exception e) {
				surveyService.deleteSurvey(survey.getSurveyId());
			}

			System.out.println(isSuccess);
			if (isSuccess && success) {
				request.put("Message", "问卷修改成功！！");
				message = "问卷修改成功！";
				// 删除之前的问卷
				surveyService.deleteSurvey(surveyId);
			} else {
				request.put("Message", "问卷修改失败！");
				message = "问卷修改失败！";
			}
		} else {
			request.put("Message", "问卷修改失败！");
			message = "问卷修改失败！";
		}

		return "teacherStudentSurveyList";
	}

	// 查找问卷，形成列表
	public String selectAllSurveys() {
		String tchrSchNum = tUser.getSchNum();
		teacher = teacherService.selectInfBySchNum(tchrSchNum);
		if (page == null) {
			page = 1;
		}
		suPageBean = surveyService.selectAllSurveys(teacher, page);
		allClazz = clazzService.findAllCla();

		try {
			if (message != null) {
				message = new String(message.getBytes("ISO-8859-1"), "UTF-8");
				request.put("Message", message);
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "teacher";
	}

	// 查找非待发布的问卷
	public String selectPublishedSurveys() {
		String tchrSchNum = tUser.getSchNum();
		teacher = teacherService.selectInfBySchNum(tchrSchNum);
		if (page == null) {
			page = 1;
		}
		suPageBean = surveyService.selectPublishedSurveys(teacher, page);
		allClazz = clazzService.findAllCla();
		return "teacher";
	}

	// 查看问卷
	public String selectSurveyById() {
		survey = surveyService.selectSurveyById(surveyId);
		surveyQuestions = surveyService.selectQuestionBysurveyId(surveyId);

		if (type.equals("check")) {
			allClazz = clazzService.findAllCla();
		}

		return "teacher";
	}

	public String addSurveyDone() {
		// 存储问卷者信息
		SurveyReplyer surveyReplyer = new SurveyReplyer();
		if (tUser != null) {
			String userRole = tUser.getIdentity().toString();
			survey = surveyService.selectSurveyById(surveyId);

			Date replyTime = new Date();// 做問卷的时间
			if (userRole == "TEACHER") {
				String tchrSchNum = tUser.getSchNum();
				teacher = teacherService.selectInfBySchNum(tchrSchNum);
				surveyReplyer.setTeacher(teacher);
			} else if (userRole == "STUDENT") {
				String stuSchNum = tUser.getSchNum();
				student = studentService.selectInfBySchNum(stuSchNum);
				surveyReplyer.setStudent(student);
			}
			surveyReplyer.setSurvey(survey);
			surveyReplyer.setReplyTime(replyTime);
			/* surveyService.addSurveyReplyer(surveyReplyer); */
		}

		// 存储问卷选择结果
		boolean isSuccess = surveyService.addSurveyDone(surveyReplyer, surveySelectors, textAnswers, survey);
		if (isSuccess) {
			request.put("Message", "提交成功！！");
		} else {
			request.put("Message", "提交失败！");
		}
		return "surveyDone";
	}

	public String addLimitForSurvey() {
		boolean isSuccess = surveyService.addLimitForSurvey(gcs);
		if (isSuccess) {
			request.put("Message", "发布成功！！");
		} else {
			request.put("Message", "发布失败！");
		}
		return "allSurveys";

	}

	public String selectSurveyReplyerById() {
		survey = surveyService.selectSurveyById(surveyId);
		if (page == null) {
			page = 1;
		}
		srPageBean = surveyService.selectSurveyReplyerById(surveyId, page);
		return "teacher";
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public Student getS() {
		return s;
	}

	public void setS(Student s) {
		this.s = s;
	}

	public List<StudentItem> getItems() {
		return items;
	}

	public void setItems(List<StudentItem> items) {
		this.items = items;
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

	public Integer getClazzId() {
		return clazzId;
	}

	public void setClazzId(Integer clazzId) {
		this.clazzId = clazzId;
	}

	public List<ItemFile> getAllFile() {
		return allFile;
	}

	public void setAllFile(List<ItemFile> allFile) {
		this.allFile = allFile;
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

	public List<Clazz> getAllClazz() {
		return allClazz;
	}

	public void setAllClazz(List<Clazz> allClazz) {
		this.allClazz = allClazz;
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

	public AdminStuLimits getLimits() {
		return limits;
	}

	public void setLimits(AdminStuLimits limits) {
		this.limits = limits;
	}

	public Map<String, Object> getRequest() {
		return request;
	}

	public void setRequest(Map<String, Object> request) {
		this.request = request;
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

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public EvaluateResult getEvaluateResult() {
		return evaluateResult;
	}

	public void setEvaluateResult(EvaluateResult evaluateResult) {
		this.evaluateResult = evaluateResult;
	}

	public Integer getEvaluateResultId() {
		return evaluateResultId;
	}

	public void setEvaluateResultId(Integer evaluateResultId) {
		this.evaluateResultId = evaluateResultId;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Integer getClaId() {
		return claId;
	}

	public void setClaId(Integer claId) {
		this.claId = claId;
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

	public List<SurveyQuestion> getQs() {
		return qs;
	}

	public void setQs(List<SurveyQuestion> qs) {
		this.qs = qs;
	}

	public List<Survey> getSurveys() {
		return surveys;
	}

	public void setSurveys(List<Survey> surveys) {
		this.surveys = surveys;
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

	public List<ItemEvaluateType> getItemEvaluateTypes() {
		return itemEvaluateTypes;
	}

	public void setItemEvaluateTypes(List<ItemEvaluateType> itemEvaluateTypes) {
		this.itemEvaluateTypes = itemEvaluateTypes;
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

	public Integer getStuId() {
		return stuId;
	}

	public void setStuId(Integer stuId) {
		this.stuId = stuId;
	}

	public Double[] getScoreArr() {
		return ScoreArr;
	}

	public void setScoreArr(Double[] scoreArr) {
		ScoreArr = scoreArr;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public GradeClazzSurvey getGcs() {
		return gcs;
	}

	public void setGcs(GradeClazzSurvey gcs) {
		this.gcs = gcs;
	}

	public PageBean<SurveyReplyer> getSrPageBean() {
		return srPageBean;
	}

	public void setSrPageBean(PageBean<SurveyReplyer> srPageBean) {
		this.srPageBean = srPageBean;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
