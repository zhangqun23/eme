package cn.xidian.dao;

import java.util.Date;
import java.util.List;

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

public interface SurveyDao {

	boolean createSurvey(Survey survey);

	boolean addQuestion(SurveyQuestion sq);

	boolean updateSurvey(Survey survey);

	boolean addSelector(SurveySelector surveySelector);

	List<Survey> selectAllSurveys(Teacher teacher);

	List<Survey> selectPublishedSurveys(Teacher teacher);

	List<Survey> findSurveys(Teacher teacher, Integer begin, Integer limit);

	List<Survey> findPublishedSurveys(Teacher teacher, Integer begin, Integer limit);

	Survey selectSurveyById(Integer surveyId);

	List<SurveyQuestion> selectQuestionBysurveyId(Integer surveyId);

	boolean updateSelectorNum(Integer surveyId, Integer questionId, Integer selectorNum);

	SurveyQuestion selectQuestionById(Integer questionId);

	boolean addTextAnswer(TextAnswer textAnswer);

	boolean updateSurveySumById(Integer surveyId);

	boolean addSurveyReplyer(SurveyReplyer surveyReplyer);

	List<SurveySelector> selectSurveySelectors(Integer surveyId, Integer questionId);

	boolean publishSurvey(Integer surveyId);

	boolean deleteSurvey(Integer surveyId);

	List<GradeClazzSurvey> selectStuSurveys(Integer role, Integer claId, String grade);

	List<GradeClazzSurvey> findStuSurveys(Integer role, Integer begin, Integer limit, Integer claId, String grade);

	List<Survey> selectTchrSurveys(Integer role);

	List<Survey> findTchrSurveys(Integer role, Integer begin, Integer limit);

	boolean overSurvey(Integer surveyId);

	List<TextAnswer> selectSurveyTextResult(Integer surveyId, Integer questionId);

	List<TextAnswer> findSurveyTextResult(Integer surveyId, Integer questionId, Integer begin, Integer limit);

	boolean addLimitForSurvey(GradeClazzSurvey gradeClazzSurvey);

	List<SurveyReplyer> selectStuSurveyReplayer(Integer surveyId, Integer stuId);

	List<SurveyReplyer> selectTeaSurveyReplayer(Integer surveyId, Integer tchrId);

	List<SurveyReplyer> selectSurveyReplyerById(Integer surveyId);

	List<SurveyReplyer> findSurveyReplyerById(Integer surveyId, Integer begin, Integer limit);

	boolean updateSurveyDate(Integer surveyId, Date endTime);

	boolean addSelectorDouble(SurveySelectorDouble surveySelectorDouble);

	boolean saveSurveySelectorRelate(SurveySelectorRelate surveySelectorRelate);

	boolean updateSelectorRelateNum(Integer selectorId, Integer selectorDoubleId);

	SurveySelector selectSurveySelector(Integer surveyId, Integer questionId, Integer selectorNum);

	SurveySelectorDouble selectSurveySelectorDouble(Integer surveyId, Integer questionId, Integer selectorNum);

	List<SurveySelectorDouble> selectSurveySelectorDoubles(Integer surveyId, Integer questionId);

	List<SurveySelectorRelate> selectSurveySelectorRelates(Integer selectorDoubleId);
}
