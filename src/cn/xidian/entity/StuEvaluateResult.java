package cn.xidian.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "stuevaluateresult")
public class StuEvaluateResult {
	private Integer stuEvaluateResultId;
	private Double mScore;
	private String schoolYear;
	private ItemEvaluateType itemEvaluateType;
	private Clazz clazz;
	private Student student;

	@Id
	@GeneratedValue
	public Integer getStuEvaluateResultId() {
		return stuEvaluateResultId;
	}

	public void setStuEvaluateResultId(Integer stuEvaluateResultId) {
		this.stuEvaluateResultId = stuEvaluateResultId;
	}

	public Double getmScore() {
		return mScore;
	}

	public void setmScore(Double mScore) {
		this.mScore = mScore;
	}

	@ManyToOne
	@JoinColumn(name = "itemEvaTypeId")
	public ItemEvaluateType getItemEvaluateType() {
		return itemEvaluateType;
	}

	public void setItemEvaluateType(ItemEvaluateType itemEvaluateType) {
		this.itemEvaluateType = itemEvaluateType;
	}

	@ManyToOne
	@JoinColumn(name = "claId")
	public Clazz getClazz() {
		return clazz;
	}

	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}

	@ManyToOne
	@JoinColumn(name = "stuId")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

}
