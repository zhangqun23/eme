<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<title>问卷调查设计</title>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/bootstrap.css" />
<link rel="stylesheet" href="css/bootstrap-responsive.css" />
<link rel="stylesheet" href="css/common.css" />
<link rel="stylesheet" href="css/survey.css" />


</head>

<body>
	<%@ include file="/include/header.jsp"%>
	<%@ include file="/include/teacher_main_nav.jsp"%>
	<div class="content">
		<div class="container">
			<div class="row">
				<%@ include file="/include/tchrLeftBar.jsp"%>
				<div class="span9">
					<div class="span9 div-content-white-bgr">
						<div class="div-inf-bar">

							<label>问卷调查</label>
						</div>
						<div class="div-inf-tbl">
							<div>
								<h3 class="title_center">
									<s:property value="survey.title" />
								</h3>
								<p class="top-distance para-indent">
									<s:property value="survey.discribe" />
								</p>
								<p class="top-distance para-indent align_right">
									<s:property value="survey.sponsor" />
								</p>
							</div>
							<hr />
							<form
								action="TeacherStudent_Survey_List_addSurveyDone?surveyId=<s:property
										value="survey.surveyId" />"
								method="post" class="form-horizontal"
								onsubmit="javascript:return isEmpty()">
								<s:iterator value="surveyQuestions" var="sq" status="status">
									<ul class="question-style top-distance">
										<label>Q<s:property value="%{#status.count}" />： <s:property
												value="#sq.content" /> <s:if test="#sq.type==2">（多选）</s:if></label>
										<s:if test="#sq.type==1||#sq.type==2">
											<s:generator val="#sq.selectors" separator="_" id="s" />
											<s:iterator status="st" value="#request.s" id="selector">
												<li class="li_style selector-style"><s:if
														test="#sq.type==1">
														<input type="radio" class="radio"
															name="<s:property value="%{#status.count}" />"
															id="<s:property value="%{#st.count}" />">
														<span class="left_distance"><s:property
																value="selector" /></span>
													</s:if> <s:if test="#sq.type==2">
														<input type="checkbox" class="checkbox"
															name="<s:property value="%{#status.count}" />"
															id="<s:property value="%{#st.count}" />">
														<span class="left_distance"><s:property
																value="selector" /></span>
													</s:if></li>
											</s:iterator>
											<input type="hidden" class="selected"
												id="seled<s:property value="%{#status.count}" />"
												value="<s:property value="#sq.questionId" />">
											<!-- 获取选中的选项的selectorNum -->
										</s:if>
										<s:if test="#sq.type==3">
											<li class="li_style selector-style"><textarea
													name="<s:property value="%{#status.count}" />"
													placeholder='请填写内容' class='textarea left_distance'
													style='width: 72%; height: 100px'></textarea></li>
											<input type="hidden" class="textAnswer"
												id="text<s:property value="%{#status.count}" />"
												value="<s:property value="#sq.questionId" />">
										</s:if>
										<s:if test="#sq.type==4">
											<li class="li_style selector-style">
												<table class="table table-bordered table-condensed wjTable"
													id="table<s:property value="%{#status.count}" />">
													<tr>
														<td class="tdOne">选项</td>
														<s:generator val="#sq.selectors" separator="_" id="s" />
														<s:iterator status="st" value="#request.s" id="selector">
															<td><s:property value="selector" /></td>
														</s:iterator>
													</tr>
													<s:generator val="#sq.rowSelectors" separator="_" id="t" />
													<s:iterator status="tt" value="#request.t" id="rowSelector">
														<tr>
															<td><s:property value="rowSelector" /></td>
															<s:generator val="#sq.selectors" separator="_" id="s" />
															<s:iterator status="st" value="#request.s" id="selector">
																<td><input type="radio" class="radio"
																	id="<s:property value="%{#tt.count}" />_<s:property value="%{#st.count}" />"
																	name="<s:property value="%{#status.count}" />_<s:property value="%{#tt.count}" />" /></td>
															</s:iterator>
													</s:iterator>
													</tr>
												</table> <input type="hidden" class="selected"
												id="seled<s:property value="%{#status.count}" />"
												value="<s:property value="#sq.questionId" />"> <input
												type="hidden"
												id="selQuesId<s:property value="%{#status.count}" />"
												value="<s:property value="#sq.questionId" />"> <input
												type="hidden"
												id="quesType<s:property value="%{#status.count}" />"
												value="<s:property value="#sq.type" />">
											</li>
										</s:if>
									</ul>

								</s:iterator>
								<%-- <input type="text" name="survey.surveyId"
									value="<s:property value="survey.surveyId" />">
								<!-- 获取问卷的ID --> --%>
								<!-- <input type="submit" class="btn" value="提交问卷"
									onclick="linkSelSubmit()">   -->
								<div class="top-distance">
									<span>问卷有效日期：</span><input type="text" name="startTime"
										style="width: 100px;" readonly id="startTime"
										value="<s:property  value="%{getText('{0,date,yyyy-MM-dd}',{survey.startTime})}"/>">至<input
										type="text" name="endTime" style="width: 100px;" readonly
										id="endTime"
										value="<s:property  value="%{getText('{0,date,yyyy-MM-dd}',{survey.endTime})}"/>">
								</div>
							</form>
							<div class="right_align">

								<s:if test="survey.state!=1">
									<s:if test="survey.respondent==1">
										<input type="button" class="btn" name="publish" id="publish"
											data-toggle="modal" data-target="#myModal"
											onclick="getSurveyId(<s:property value="survey.surveyId" />)"
											value="发    布">
									</s:if>
									<s:if test="survey.respondent==2">
										<input type="button" class="btn" name="publish" id="publish"
											onclick="publishSurvey(<s:property value="survey.surveyId" />)"
											value="发    布">
									</s:if>
									<s:if test="survey.respondent==3">
										<input type="button" class="btn" name="publish" id="publish"
											data-toggle="modal" data-target="#myModal"
											onclick="getSurveyId(<s:property value="survey.surveyId" />)"
											value="发    布">
									</s:if>

									<s:if test="survey.state==0">
										<a
											href="TeacherStudent_Survey_Modify_selectSurveyById?surveyId=<s:property value="survey.surveyId" />&type=modify"
											class="btn">编辑</a>
									</s:if>
									<s:if test="survey.state==1">
										<a data-toggle="modal" data-target="#judgeModal"
											onclick="getSurveyId(<s:property value="survey.surveyId" />)"
											class="btn">编辑</a>
									</s:if>
									<s:if test="survey.state==2">
										<a data-toggle="modal" data-target="#judgeModal"
											onclick="getSurveyId(<s:property value="survey.surveyId" />)"
											class="btn">编辑</a>
									</s:if>




								</s:if>
							</div>
						</div>
					</div>
					<!-- 模态框，用于添加参与活动信息 -->
					<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
						aria-labelledby="myModalLabel" aria-hidden="true"
						style="display: none">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal"
										aria-hidden="true">×</button>
									<h5 class="modal-title">设定问卷对象</h5>
								</div>
								<div class="modal-body">
									<form action="TeacherStudent_Survey_List_addLimitForSurvey"
										method="post" class="form-horizontal form-add"
										enctype="multipart/form-data"
										onsubmit="javascript:return isEmpty(1)">
										<div class="control-group">
											<label class="control-label">年级：</label>
											<div class="controls">
												<input type="text" name="gcs.grade" id="surveyGrade"
													style="width: 40px" onchange=""
													onFocus="WdatePicker(WdatePicker({lang:'zh-cn',dateFmt:'yyyy',readOnly:'true'})) ">
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">班级：</label>
											<div class="controls">
												<select id="surveyClazz" name="gcs.clazz.claId">
													<option value="0">全部</option>
													<s:iterator value="allClazz" var="ac">
														<option value="<s:property value="#ac.claId" />"><s:property
																value="#ac.claName" /></option>
													</s:iterator>
												</select>
											</div>
										</div>
										<input type="hidden" name="gcs.survey.surveyId" id="surveyId">

										<div class="div-btn">
											<input type="submit" value="提交" class="btn">
										</div>
									</form>
								</div>

							</div>
						</div>
					</div>
					<!-- 模态框，用于添加参与活动信息完 -->
					<!-- 模态框，用于添加参与活动信息 -->
					<div class="modal fade" id="judgeModal" tabindex="-1" role="dialog"
						aria-labelledby="myModalLabel" aria-hidden="true"
						style="display: none">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal"
										aria-hidden="true">×</button>
									<h5 class="modal-title">是否编辑？</h5>
								</div>
								<div class="modal-body">
									<form action="TeacherStudent_Survey_Modify_selectSurveyById"
										method="post" class="form-horizontal form-add"
										enctype="multipart/form-data">
										<div class="control-group">
											<label class="">您的问卷已发布或已结束，如果提交修改，之前的统计数据将会丢失！
												是否进行问卷修改？</label>

										</div>

										<input type="hidden" name="surveyId" id="survey_id"> <input
											type="hidden" name="type" value="modify">

										<div class="div-btn">
											<input type="submit" value="确认" class="btn">
										</div>
									</form>
								</div>

							</div>
						</div>
					</div>
					<!-- 模态框，用于添加参与活动信息完 -->
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/include/footer.jsp"%>
	<script type="text/javascript" src="js/jquery1.12.1.js"></script>
	<script type="text/javascript" src="js/bootstrap.js"></script>
	<script type="text/javascript" src="js/survey.js"></script>
	<script src="js/My97DatePickerBeta/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
		var msg = "${requestScope.Message}";
		if (msg != "") {
			alert(msg);
		}
	<%request.removeAttribute("Message");%>
		//显示后将request里的Message清空，防止回退时重复显示。

		$(function() {
			$(".container").css("min-height",
					$(document).height() - 90 - 88 - 41 + "px");//container的最小高度为“浏览器当前窗口文档的高度-header高度-footer高度”
		});

		
		//发布问卷
		function publishSurvey(surveyId) {
			$.getJSON("Json_publishSurvey", {
				surveyId : surveyId
			}, function(data) {
				alert("发布成功");
			});
		}
		//获取问卷ID
		function getSurveyId(surveyId){
			$("#surveyId").val(surveyId);
			$("#survey_id").val(surveyId);
		}
		// 非空判断
		function isEmpty() {
			var grade = document.getElementById("surveyGrade");
			var clazz = document.getElementById("surveyClazz");
			
			if (grade.value.toString().trim().length != 0
					&& clazz.value.toString().trim().length != 0) {
				
				return true;
			} else {
				
				alert("输入框不能为空！");
				return false;
			}

		}
	</script>
</body>
</html>
