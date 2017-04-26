package cn.xidian.dao.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import cn.xidian.dao.SurveyDao;
import cn.xidian.entity.GradeClazzSurvey;
import cn.xidian.entity.Survey;
import cn.xidian.entity.SurveyQuestion;
import cn.xidian.entity.SurveyReplyer;
import cn.xidian.entity.SurveySelector;
import cn.xidian.entity.SurveySelectorDouble;
import cn.xidian.entity.SurveySelectorRelate;
import cn.xidian.entity.Teacher;
import cn.xidian.entity.TextAnswer;

@Component("surveyDaoImpl")
public class SurveyDaoImpl implements SurveyDao {
	private SessionFactory sessionFactory;

	@Resource(name = "sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public boolean createSurvey(Survey survey) {
		// TODO Auto-generated method stub
		currentSession().save(survey);

		return true;
	}

	@Override
	public boolean addQuestion(SurveyQuestion sq) {

		currentSession().save(sq);
		return true;
	}

	@Override
	public boolean updateSurvey(Survey survey) {
		// TODO Auto-generated method stub
		String sql = "update Survey s set s.startTime=?,s.endTime=? where s.surveyId=? ";
		Query query = currentSession().createQuery(sql);
		query.setDate(0, survey.getStartTime());
		query.setDate(1, survey.getEndTime());
		query.setInteger(2, survey.getSurveyId());
		query.executeUpdate();
		return true;
	}

	@Override
	public boolean addSelector(SurveySelector surveySelector) {
		// TODO Auto-generated method stub
		currentSession().save(surveySelector);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Survey> selectAllSurveys(Teacher teacher) {
		// TODO Auto-generated method stub
		String sql = "from Survey where tchrId=? and delState=1 order by surveyId desc";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, teacher.getTchrId());
		List<Survey> surveys = query.list();
		return surveys;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Survey> findSurveys(Teacher teacher, Integer begin, Integer limit) {
		// TODO Auto-generated method stub
		String sql = "from Survey where tchrId=? and delState=1 order by surveyId desc";
		Query query = currentSession().createQuery(sql).setFirstResult(begin).setMaxResults(limit);
		query.setInteger(0, teacher.getTchrId());
		List<Survey> surveys = query.list();
		return surveys;
	}

	@Override
	public Survey selectSurveyById(Integer surveyId) {
		// TODO Auto-generated method stub
		String sql = "from Survey where surveyId=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		Survey survey = new Survey();
		survey = (Survey) query.uniqueResult();
		return survey;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyQuestion> selectQuestionBysurveyId(Integer surveyId) {
		// TODO Auto-generated method stub
		String sql = "from SurveyQuestion where surveyId=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		List<SurveyQuestion> surveyQuestions = query.list();
		return surveyQuestions;
	}

	@Override
	public boolean updateSelectorNum(Integer surveyId, Integer questionId, Integer selectorNum) {
		// TODO Auto-generated method stub
		String sql = "update SurveySelector  set sumNum=sumNum +'1' where surveyId=? and questionId=? and selectorNum=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		query.setInteger(1, questionId);
		query.setInteger(2, selectorNum);
		query.executeUpdate();
		return true;
	}

	@Override
	public SurveyQuestion selectQuestionById(Integer questionId) {
		// TODO Auto-generated method stub
		String sql = "from SurveyQuestion where questionId=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, questionId);
		SurveyQuestion surveyQuestion = new SurveyQuestion();
		surveyQuestion = (SurveyQuestion) query.uniqueResult();
		return surveyQuestion;
	}

	@Override
	public boolean addTextAnswer(TextAnswer textAnswer) {
		// TODO Auto-generated method stub
		currentSession().save(textAnswer);
		return true;
	}

	@Override
	public boolean updateSurveySumById(Integer surveyId) {
		// TODO Auto-generated method stub
		String sql = "update Survey set sumNum=sumNum+'1' where surveyId=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		query.executeUpdate();
		return true;
	}

	@Override
	public boolean addSurveyReplyer(SurveyReplyer surveyReplyer) {
		// TODO Auto-generated method stub
		currentSession().save(surveyReplyer);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveySelector> selectSurveySelectors(Integer surveyId, Integer questionId) {
		// TODO Auto-generated method stub
		String sql = "from SurveySelector where surveyId=? and questionId=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		query.setInteger(1, questionId);
		List<SurveySelector> surveySelectors = query.list();
		return surveySelectors;
	}

	@Override
	public boolean publishSurvey(Integer surveyId) {
		// TODO Auto-generated method stub
		String sql = "update Survey set state= 1 where surveyId =?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		query.executeUpdate();
		return true;
	}

	@Override
	public boolean deleteSurvey(Integer surveyId) {
		// TODO Auto-generated method stub
		String sql = "update Survey set delState=0 where surveyId=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		query.executeUpdate();
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GradeClazzSurvey> selectStuSurveys(Integer role, Integer claId, String grade) {
		// TODO Auto-generated method stub
		String sql = "from GradeClazzSurvey gcs where   claId=? and gcs.survey.state=? and delState=1 order by gcs.survey.surveyId desc";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, claId);
		query.setInteger(1, 1);
		List<GradeClazzSurvey> gcs = query.list();
		return gcs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GradeClazzSurvey> findStuSurveys(Integer role, Integer begin, Integer limit, Integer claId,
			String grade) {
		// TODO Auto-generated method stub

		String sql = "from GradeClazzSurvey gcs where claId=? and gcs.survey.state=? and delState=1 order by gcs.survey.surveyId desc";
		Query query = currentSession().createQuery(sql).setFirstResult(begin).setMaxResults(limit);
		query.setInteger(0, claId);
		query.setInteger(1, 1);
		List<GradeClazzSurvey> gcs = query.list();
		return gcs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Survey> selectTchrSurveys(Integer role) {
		// TODO Auto-generated method stub
		String sql = "from Survey where delState=1 and state=1 and (respondent=? or respondent=?) order by surveyId desc";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, role);
		query.setInteger(1, 3);
		List<Survey> surveys = query.list();
		return surveys;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Survey> findTchrSurveys(Integer role, Integer begin, Integer limit) {
		// TODO Auto-generated method stub

		String sql = "from Survey where delState=1 and state=1 and (respondent=? or respondent=?) order by surveyId desc";
		Query query = currentSession().createQuery(sql).setFirstResult(begin).setMaxResults(limit);
		query.setInteger(0, role);
		query.setInteger(1, 3);
		List<Survey> surveys = query.list();
		return surveys;
	}

	@Override
	public boolean overSurvey(Integer surveyId) {
		// TODO Auto-generated method stub
		String sql = "update Survey set state=2 where surveyId=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		query.executeUpdate();
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TextAnswer> selectSurveyTextResult(Integer surveyId, Integer questionId) {
		// TODO Auto-generated method stub
		String sql = "from TextAnswer where surveyId=? and questionId=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		query.setInteger(1, questionId);
		List<TextAnswer> textAnswers = query.list();
		return textAnswers;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TextAnswer> findSurveyTextResult(Integer surveyId, Integer questionId, Integer begin, Integer limit) {
		// TODO Auto-generated method stub
		String sql = "from TextAnswer where surveyId=? and questionId=?";
		Query query = currentSession().createQuery(sql).setFirstResult(begin).setMaxResults(limit);
		query.setInteger(0, surveyId);
		query.setInteger(1, questionId);
		List<TextAnswer> textAnswers = query.list();
		return textAnswers;
	}

	@Override
	public boolean addLimitForSurvey(GradeClazzSurvey gradeClazzSurvey) {
		// TODO Auto-generated method stub
		currentSession().save(gradeClazzSurvey);
		return true;
	}

	@Override
	public List<Survey> selectPublishedSurveys(Teacher teacher) {
		// TODO Auto-generated method stub
		String sql = "from Survey where tchrId=? and (state=1 or state=2)and delState=1 order by surveyId desc";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, teacher.getTchrId());
		List<Survey> surveys = query.list();
		return surveys;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Survey> findPublishedSurveys(Teacher teacher, Integer begin, Integer limit) {
		// TODO Auto-generated method stub
		String sql = "from Survey where tchrId=? and (state=1 or state=2) and delState=1 order by surveyId desc";
		Query query = currentSession().createQuery(sql).setFirstResult(begin).setMaxResults(limit);
		query.setInteger(0, teacher.getTchrId());
		List<Survey> surveys = query.list();
		return surveys;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyReplyer> selectStuSurveyReplayer(Integer surveyId, Integer stuId) {
		// TODO Auto-generated method stub
		String sql = "from SurveyReplyer where surveyId=? and stuId=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		query.setInteger(1, stuId);
		List<SurveyReplyer> surveyReplyers = query.list();
		return surveyReplyers;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyReplyer> selectTeaSurveyReplayer(Integer surveyId, Integer tchrId) {

		String sql = "from SurveyReplyer where surveyId=? and tchrId=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		query.setInteger(1, tchrId);
		List<SurveyReplyer> surveyReplyers = query.list();
		return surveyReplyers;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyReplyer> selectSurveyReplyerById(Integer surveyId) {
		// TODO Auto-generated method stub
		String sql = "from SurveyReplyer where surveyId=? group by stuId";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		List<SurveyReplyer> surveyReplyers = query.list();
		return surveyReplyers;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveyReplyer> findSurveyReplyerById(Integer surveyId, Integer begin, Integer limit) {
		// TODO Auto-generated method stub
		String sql = "from SurveyReplyer where surveyId=? group by stuId order by stuId ";
		Query query = currentSession().createQuery(sql).setFirstResult(begin).setMaxResults(limit);
		query.setInteger(0, surveyId);
		List<SurveyReplyer> surveyReplyers = query.list();
		return surveyReplyers;
	}

	@Override
	public boolean updateSurveyDate(Integer surveyId, Date endTime) {
		// TODO Auto-generated method stub

		String sql = "update Survey set endTime=? where surveyId=?";
		Query query = currentSession().createQuery(sql);
		query.setDate(0, endTime);
		query.setInteger(1, surveyId);
		query.executeUpdate();
		return true;
	}

	@Override
	public boolean addSelectorDouble(SurveySelectorDouble surveySelectorDouble) {
		// TODO Auto-generated method stub
		currentSession().save(surveySelectorDouble);
		return true;
	}

	@Override
	public boolean saveSurveySelectorRelate(SurveySelectorRelate surveySelectorRelate) {
		// TODO Auto-generated method stub
		currentSession().save(surveySelectorRelate);
		return true;
	}

	@Override
	public boolean updateSelectorRelateNum(Integer selectorId, Integer selectorDoubleId) {
		// TODO Auto-generated method stub
		String sql = "update SurveySelectorRelate  set sumNum=sumNum+1 where selectorId=? and selectorDoubleId=? ";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, selectorId);
		query.setInteger(1, selectorDoubleId);
		query.executeUpdate();
		return true;
	}

	@Override
	public SurveySelector selectSurveySelector(Integer surveyId, Integer questionId, Integer selectorNum) {
		// TODO Auto-generated method stub
		String sql = "from SurveySelector where surveyId=? and questionId=? and selectorNum=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		query.setInteger(1, questionId);
		query.setInteger(2, selectorNum);
		SurveySelector ss = (SurveySelector) query.uniqueResult();
		return ss;
	}

	@Override
	public SurveySelectorDouble selectSurveySelectorDouble(Integer surveyId, Integer questionId, Integer selectorNum) {
		// TODO Auto-generated method stub
		String sql = "from SurveySelectorDouble where surveyId=? and questionId=? and selectorNum=?";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		query.setInteger(1, questionId);
		query.setInteger(2, selectorNum);
		SurveySelectorDouble ssd = (SurveySelectorDouble) query.uniqueResult();
		return ssd;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveySelectorDouble> selectSurveySelectorDoubles(Integer surveyId, Integer questionId) {
		// TODO Auto-generated method stub
		String sql = "from SurveySelectorDouble where surveyId=? and questionId=? order by selectorDoubleId";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, surveyId);
		query.setInteger(1, questionId);
		List<SurveySelectorDouble> ssd = query.list();
		return ssd;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SurveySelectorRelate> selectSurveySelectorRelates(Integer selectorId) {
		// TODO Auto-generated method stub
		String sql = "from SurveySelectorRelate where selectorId=? order by selectorDoubleId";
		Query query = currentSession().createQuery(sql);
		query.setInteger(0, selectorId);
		List<SurveySelectorRelate> ssr = query.list();
		return ssr;
	}

}
