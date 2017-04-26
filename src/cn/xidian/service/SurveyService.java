package cn.xidian.service;

import java.util.List;

import org.omg.PortableInterceptor.INACTIVE;

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

public interface SurveyService {

	boolean createSurvey(Survey survey);

	boolean addQuestion(List<SurveyQuestion> qs, Survey survey);

	PageBean<Survey> selectAllSurveys(Teacher teacher, Integer page);

	PageBean<Survey> selectPublishedSurveys(Teacher teacher, Integer page);

	Survey selectSurveyById(Integer surveyId);

	List<SurveyQuestion> selectQuestionBysurveyId(Integer surveyId);

	boolean addSurveyDone(SurveyReplyer surveyReplyer, List<SurveySelector> surveySelectors,
			List<TextAnswer> textAnswers, Survey survey);

	boolean addSurveyReplyer(SurveyReplyer surveyReplyer);

	List<SurveySelector> selectSurveySelectors(Integer surveyId, Integer questionId);

	boolean publishSurvey(Integer surveyId);

	boolean deleteSurvey(Integer surveyId);

	PageBean<GradeClazzSurvey> selectStuSurveys(Integer role, Integer page, Integer claId, String grade);

	PageBean<Survey> selectTchrSurveys(Integer role, Integer page);

	boolean overSurvey(Integer surveyId);

	PageBean<TextAnswer> selectSurveyTextResult(Integer page, Integer surveyId, Integer questionId);

	boolean addLimitForSurvey(GradeClazzSurvey gradeClazzSurvey);

	List<SurveyReplyer> selectSurveyReplayer(Integer surveyId, Integer userId, String userRole);

	PageBean<SurveyReplyer> selectSurveyReplyerById(Integer surveyId, Integer page);

	boolean updateSurveyDate(Integer surveyId, String strEndTime);

	List<SurveySelectorDouble> selectSurveySelectorDoubles(Integer surveyId, Integer questionId);

	List<SurveySelectorRelate> selectSurveySelectorRelates(Integer selectorDoubleId);

}
