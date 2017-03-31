package cn.xidian.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "surveyreplyer")
public class SurveyReplyer {

	private Integer surveyReplyId;
	private Teacher teacher;
	private Student student;            
	private Survey survey;
	private Date replyTime;
	private String remark;

	@Id
	@GeneratedValue
	public Integer getSurveyReplyId() {
		return surveyReplyId;
	}

	public void setSurveyReplyId(Integer surveyReplyId) {
		this.surveyReplyId = surveyReplyId;
	}

	@ManyToOne
	@JoinColumn(name="tchrId")
	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	@ManyToOne
	@JoinColumn(name="stuId")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@ManyToOne
	@JoinColumn(name="surveyId")
	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
