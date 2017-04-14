package cn.xidian.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "surveyselectorrelate")
public class SurveySelectorRelate {
	private Integer relateId;// 选项ID
	private SurveySelector surveySelector;
	private SurveySelectorDouble surveySelectorDouble;
	private Integer sumNum;
	private String remark;// 选项备注

	@Id
	@GeneratedValue
	public Integer getRelateId() {
		return relateId;
	}

	public void setRelateId(Integer relateId) {
		this.relateId = relateId;
	}

	@ManyToOne
	@JoinColumn(name = "selectorId")
	public SurveySelector getSurveySelector() {
		return surveySelector;
	}

	public void setSurveySelector(SurveySelector surveySelector) {
		this.surveySelector = surveySelector;
	}

	@ManyToOne
	@JoinColumn(name = "selectorDoubleId")
	public SurveySelectorDouble getSurveySelectorDouble() {
		return surveySelectorDouble;
	}

	public void setSurveySelectorDouble(SurveySelectorDouble surveySelectorDouble) {
		this.surveySelectorDouble = surveySelectorDouble;
	}

	public Integer getSumNum() {
		return sumNum;
	}

	public void setSumNum(Integer sumNum) {
		this.sumNum = sumNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
