package cn.xidian.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "textanswer")
public class TextAnswer {
	private Integer textAnswerId;
	private String answerContent;
	private Survey survey;
	private SurveyQuestion surveyQuestion;
	private String  remark;

	@Id
	@GeneratedValue
	public Integer getTextAnswerId() {
		return textAnswerId;
	}

	public void setTextAnswerId(Integer textAnswerId) {
		this.textAnswerId = textAnswerId;
	}

	public String getAnswerContent() {
		return answerContent;
	}

	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}

	@ManyToOne
	@JoinColumn(name = "surveyId")
	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	@ManyToOne
	@JoinColumn(name = "questionId")
	public SurveyQuestion getSurveyQuestion() {
		return surveyQuestion;
	}

	public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
		this.surveyQuestion = surveyQuestion;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
