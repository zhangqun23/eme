package cn.xidian.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "survey")
public class Survey {
	private Integer surveyId;// 问卷Id
	private String title;// 问卷标题
	private String discribe;// 问卷大致描述
	private Date createTime;// 问卷创建时间
	private Date startTime;// 问卷开始时间
	private Date endTime;// 问卷截至时间
	private String sponsor;// 问卷发起单位
	private Integer state;// 问卷状态0表示待发布，1表示已发布，2表示已结束
	private Teacher teacher;// 问卷创建人
	private String remark;// 问卷备注
	private Integer sumNum;// 问卷被做了几次
	private Integer delState;// 问卷是否删除0表示已删除，1表示未删除
	private Integer respondent;// 问卷调查对象1代表学生2代表老师3代表既有老师又有学生

	@Id
	@GeneratedValue
	public Integer getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Integer surveyId) {
		this.surveyId = surveyId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDiscribe() {
		return discribe;
	}

	public void setDiscribe(String discribe) {
		this.discribe = discribe;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Temporal(TemporalType.DATE)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	@ManyToOne
	@JoinColumn(name = "tchrId")
	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	@Temporal(TemporalType.DATE)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getSumNum() {
		return sumNum;
	}

	public void setSumNum(Integer sumNum) {
		this.sumNum = sumNum;
	}

	public Integer getDelState() {
		return delState;
	}

	public void setDelState(Integer delState) {
		this.delState = delState;
	}

	public Integer getRespondent() {
		return respondent;
	}

	public void setRespondent(Integer respondent) {
		this.respondent = respondent;
	}

}
