package cn.xidian.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.aspectj.weaver.ast.Var;
import org.springframework.stereotype.Component;

import cn.xidian.dao.ClazzDao;
import cn.xidian.dao.SurveyDao;
import cn.xidian.entity.Clazz;
import cn.xidian.entity.GradeClazzSurvey;
import cn.xidian.entity.PageBean;

import cn.xidian.entity.Survey;
import cn.xidian.entity.SurveyQuestion;
import cn.xidian.entity.SurveyReplyer;
import cn.xidian.entity.SurveySelector;
import cn.xidian.entity.SurveySelectorDouble;
import cn.xidian.entity.SurveySelectorRelate;
import cn.xidian.entity.Teacher;
import cn.xidian.entity.TextAnswer;
import cn.xidian.service.SurveyService;
import cn.xidian.utils.PageUtils;

@Component
public class SurveyServiceImpl implements SurveyService {

	private SurveyDao surveyDao;

	@Resource(name = "surveyDaoImpl")
	public void setSurveyDao(SurveyDao surveyDao) {
		this.surveyDao = surveyDao;
	}

	private ClazzDao clazzDao;

	@Resource(name = "clazzDaoImpl")
	public void setClazzDao(ClazzDao clazzDao) {
		this.clazzDao = clazzDao;
	}

	@Override
	public boolean createSurvey(Survey survey) {
		// TODO Auto-generated method stub
		return surveyDao.createSurvey(survey);
	}

	@Override
	public boolean addQuestion(List<SurveyQuestion> qs, Survey survey) {
		// TODO Auto-generated method stub
		/*
		 * Calendar calendar = new GregorianCalendar();
		 * calendar.setTime(survey.getEndTime());
		 * System.out.println("修改前"+calendar.getTime());
		 * calendar.add(calendar.HOUR_OF_DAY, 24);// 把日期往后增加一天.整数往后推,负数往前移动
		 * System.out.println("修改后"+calendar.getTime());
		 * survey.setEndTime(calendar.getTime()); // 这个时间就是日期往后推一天的结果
		 */ surveyDao.updateSurvey(survey);
		Iterator<SurveyQuestion> itqs = qs.iterator();
		while (itqs.hasNext()) {
			SurveyQuestion sq = new SurveyQuestion();
			sq = itqs.next();
			sq.setSurvey(survey);
			Integer type = sq.getType();
			if (type == 4) {
				String selectors = sq.getSelectors();
				String[] aa = selectors.split("#");
				sq.setSelectors(aa[0]);
				sq.setRowSelectors(aa[1]);
			}
			surveyDao.addQuestion(sq);
			String[] chrstr = sq.getSelectors().split("_");
			List<SurveySelector> ssList = new LinkedList<SurveySelector>();
			for (int i = 0; i < chrstr.length; i++) {
				SurveySelector surveySelector = new SurveySelector();
				surveySelector.setContent(chrstr[i]);
				surveySelector.setSelectorNum(i + 1);
				surveySelector.setSurvey(survey);
				surveySelector.setSurveyQuestion(sq);
				surveySelector.setSumNum(0);
				surveyDao.addSelector(surveySelector);
				ssList.add(surveySelector);
			}
			if (type == 4) {
				String selectors = sq.getRowSelectors();
				String[] rows = selectors.split("_");
				for (int j = 0; j < rows.length; j++) {
					SurveySelectorDouble ssd = new SurveySelectorDouble();

					ssd.setContent(rows[j]);
					ssd.setSelectorNum(j + 1);
					ssd.setSurvey(survey);
					ssd.setSurveyQuestion(sq);
					ssd.setSumNum(0);
					surveyDao.addSelectorDouble(ssd);
					for (int k = 0; k < ssList.size(); k++) {
						SurveySelectorRelate ssr = new SurveySelectorRelate();
						ssr.setSumNum(0);
						ssr.setSurveySelector(ssList.get(k));
						ssr.setSurveySelectorDouble(ssd);
						surveyDao.saveSurveySelectorRelate(ssr);
					}
				}
			}
		}
		return true;
	}

	@Override
	public PageBean<Survey> selectAllSurveys(Teacher teacher, Integer page) {
		// TODO Auto-generated method stub
		List<Survey> surveys = surveyDao.selectAllSurveys(teacher);
		PageBean<Survey> pageBean = PageUtils.page(page, surveys.size(), 10);
		List<Survey> s = surveyDao.findSurveys(teacher, pageBean.getBegin(), pageBean.getLimit());
		pageBean.setList(s);
		return pageBean;
	}

	@Override
	public Survey selectSurveyById(Integer surveyId) {
		// TODO Auto-generated method stub
		return surveyDao.selectSurveyById(surveyId);
	}

	@Override
	public List<SurveyQuestion> selectQuestionBysurveyId(Integer surveyId) {
		// TODO Auto-generated method stub
		return surveyDao.selectQuestionBysurveyId(surveyId);
	}

	@Override
	public boolean addSurveyDone(SurveyReplyer surveyReplyer, List<SurveySelector> surveySelectors,
			List<TextAnswer> textAnswers, Survey survey) {
		// TODO Auto-generated method stub

		// 存储选择题的答案结果
		Iterator<SurveySelector> itqs = surveySelectors.iterator();
		while (itqs.hasNext()) {
			SurveySelector sq = new SurveySelector();
			sq = itqs.next();
			String[] chrstr = sq.getRemark().split("#");
			SurveyQuestion sQuestion = new SurveyQuestion();
			sQuestion = surveyDao.selectQuestionById(Integer.parseInt(chrstr[0]));
			if (sQuestion.getType() == 4) {
				for (int i = 1; i < chrstr.length; i++) {
					String[] sel = chrstr[i].split("_");
					SurveySelector ss = surveyDao.selectSurveySelector(survey.getSurveyId(),
							Integer.parseInt(chrstr[0]), Integer.parseInt(sel[1]));
					SurveySelectorDouble ssd = surveyDao.selectSurveySelectorDouble(survey.getSurveyId(),
							Integer.parseInt(chrstr[0]), Integer.parseInt(sel[0]));
					surveyDao.updateSelectorRelateNum(ss.getSelectorId(), ssd.getSelectorDoubleId());
				}
			} else {
				for (int i = 1; i < chrstr.length; i++) {
					surveyDao.updateSelectorNum(survey.getSurveyId(), Integer.parseInt(chrstr[0]),
							Integer.parseInt(chrstr[i]));
				}
			}

		}
		// 存储文本问题的答案
		if (textAnswers != null) {
			Iterator<TextAnswer> itta = textAnswers.iterator();
			while (itta.hasNext()) {
				String str = itta.next().getRemark();
				TextAnswer ta = new TextAnswer();
				SurveyQuestion surveyQuestion = new SurveyQuestion();
				surveyQuestion = surveyDao.selectQuestionById(Integer.parseInt(str.substring(0, str.indexOf("#"))));
				ta.setAnswerContent(str.substring(str.indexOf("#") + 1, str.length()));
				ta.setSurvey(survey);
				ta.setSurveyQuestion(surveyQuestion);
				surveyDao.addTextAnswer(ta);
			}
		}
		// 给问卷添加次数
		surveyDao.updateSurveySumById(survey.getSurveyId());
		System.out.println("测试");
		surveyDao.addSurveyReplyer(surveyReplyer);

		return true;
	}

	@Override
	public boolean addSurveyReplyer(SurveyReplyer surveyReplyer) {
		// TODO Auto-generated method stub
		return surveyDao.addSurveyReplyer(surveyReplyer);
	}

	@Override
	public List<SurveySelector> selectSurveySelectors(Integer surveyId, Integer questionId) {
		// TODO Auto-generated method stub
		return surveyDao.selectSurveySelectors(surveyId, questionId);
	}

	@Override
	public boolean publishSurvey(Integer surveyId) {
		// TODO Auto-generated method stub
		return surveyDao.publishSurvey(surveyId);
	}

	@Override
	public boolean deleteSurvey(Integer surveyId) {
		// TODO Auto-generated method stub

		return surveyDao.deleteSurvey(surveyId);
	}

	@Override
	public PageBean<GradeClazzSurvey> selectStuSurveys(Integer role, Integer page, Integer claId, String grade) {
		// TODO Auto-generated method stub

		List<GradeClazzSurvey> gradeClazzSurveys = surveyDao.selectStuSurveys(role, claId, grade);
		PageBean<GradeClazzSurvey> pageBean = PageUtils.page(page, gradeClazzSurveys.size(), 10);
		List<GradeClazzSurvey> gcs = surveyDao.findStuSurveys(role, pageBean.getBegin(), pageBean.getLimit(), claId,
				grade);
		pageBean.setList(gcs);
		return pageBean;

	}

	@Override
	public PageBean<Survey> selectTchrSurveys(Integer role, Integer page) {
		// TODO Auto-generated method stub

		List<Survey> surveys = surveyDao.selectTchrSurveys(role);
		PageBean<Survey> pageBean = PageUtils.page(page, surveys.size(), 10);
		List<Survey> s = surveyDao.findTchrSurveys(role, pageBean.getBegin(), pageBean.getLimit());
		pageBean.setList(s);

		return pageBean;

	}

	@Override
	public boolean overSurvey(Integer surveyId) {
		// TODO Auto-generated method stub
		return surveyDao.overSurvey(surveyId);
	}

	@Override
	public PageBean<TextAnswer> selectSurveyTextResult(Integer page, Integer surveyId, Integer questionId) {
		// TODO Auto-generated method stub
		List<TextAnswer> textAnswers = surveyDao.selectSurveyTextResult(surveyId, questionId);
		PageBean<TextAnswer> taPageBean = PageUtils.page(page, textAnswers.size(), 5);
		List<TextAnswer> tAnswers = surveyDao.findSurveyTextResult(surveyId, questionId, taPageBean.getBegin(),
				taPageBean.getLimit());
		taPageBean.setList(tAnswers);
		return taPageBean;
	}

	@Override
	public boolean addLimitForSurvey(GradeClazzSurvey gradeClazzSurvey) {
		// TODO Auto-generated method stub
		boolean isSuccess = false;

		if (gradeClazzSurvey.getClazz().getClaId().equals(0)) {
			// 添加全部时的循环
			List<Clazz> clazzs = clazzDao.selectByGrade(gradeClazzSurvey.getGrade());
			for (int i = 0; i < clazzs.size(); i++) {
				GradeClazzSurvey gcs = new GradeClazzSurvey();
				gcs.setGrade(gradeClazzSurvey.getGrade());
				gcs.setClazz(clazzs.get(i));
				gcs.setSurvey(gradeClazzSurvey.getSurvey());
				gradeClazzSurvey.setClazz(clazzs.get(i));
				isSuccess = surveyDao.addLimitForSurvey(gcs);
			}
			surveyDao.publishSurvey(gradeClazzSurvey.getSurvey().getSurveyId());
			return isSuccess;
		} else {
			isSuccess = surveyDao.addLimitForSurvey(gradeClazzSurvey);
			surveyDao.publishSurvey(gradeClazzSurvey.getSurvey().getSurveyId());
			return isSuccess;
		}
	}

	@Override
	public PageBean<Survey> selectPublishedSurveys(Teacher teacher, Integer page) {
		// TODO Auto-generated method stub
		List<Survey> surveys = surveyDao.selectPublishedSurveys(teacher);
		PageBean<Survey> pageBean = PageUtils.page(page, surveys.size(), 10);
		List<Survey> s = surveyDao.findPublishedSurveys(teacher, pageBean.getBegin(), pageBean.getLimit());
		pageBean.setList(s);
		return pageBean;

	}

	@Override
	public List<SurveyReplyer> selectSurveyReplayer(Integer surveyId, Integer userId, String userRole) {
		// TODO Auto-generated method stub
		List<SurveyReplyer> surveyReplyers = null;
		if (userRole == "STUDENT") {
			surveyReplyers = surveyDao.selectStuSurveyReplayer(surveyId, userId);
		}
		if (userRole == "TEACHER") {
			surveyReplyers = surveyDao.selectTeaSurveyReplayer(surveyId, userId);
		}
		return surveyReplyers;
	}

	@Override
	public PageBean<SurveyReplyer> selectSurveyReplyerById(Integer surveyId, Integer page) {
		// TODO Auto-generated method stub
		List<SurveyReplyer> surveyReplyers = surveyDao.selectSurveyReplyerById(surveyId);
		PageBean<SurveyReplyer> pageBean = PageUtils.page(page, surveyReplyers.size(), 20);
		List<SurveyReplyer> sr = surveyDao.findSurveyReplyerById(surveyId, pageBean.getBegin(), pageBean.getLimit());
		pageBean.setList(sr);
		return pageBean;
	}

	@Override
	public boolean updateSurveyDate(Integer surveyId, String strEndTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			surveyDao.updateSurveyDate(surveyId, sdf.parse(strEndTime));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public List<SurveySelectorDouble> selectSurveySelectorDoubles(Integer surveyId, Integer questionId) {
		// TODO Auto-generated method stub
		List<SurveySelectorDouble> ssd = surveyDao.selectSurveySelectorDoubles(surveyId, questionId);
		return ssd;
	}

	@Override
	public List<SurveySelectorRelate> selectSurveySelectorRelates(Integer selectorId) {
		// TODO Auto-generated method stub
		List<SurveySelectorRelate> ssr = surveyDao.selectSurveySelectorRelates(selectorId);
		return ssr;
	}
}
