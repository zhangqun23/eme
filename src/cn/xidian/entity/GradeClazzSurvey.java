package cn.xidian.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "grade_clazz_survey")
public class GradeClazzSurvey {

	private Integer GradeClazzSurveyId;// 问卷Id

	private String grade;
	private Clazz clazz;
	private Survey survey;

	@Id
	@GeneratedValue
	public Integer getGradeClazzSurveyId() {
		return GradeClazzSurveyId;
	}

	public void setGradeClazzSurveyId(Integer gradeClazzSurveyId) {
		GradeClazzSurveyId = gradeClazzSurveyId;
	}

	@ManyToOne
	@JoinColumn(name = "claId")
	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@ManyToOne
	@JoinColumn(name = "surveyId")
	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

}
