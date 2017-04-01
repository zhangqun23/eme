package cn.xidian.web.bean;

public class WordCourseContent {
	private Integer id;//序号
	private String content;//课程内容
	private String period;//学时
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
