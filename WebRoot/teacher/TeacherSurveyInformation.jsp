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
							<div style="font-size: 17px; color: red; display: none" id="warn">你已做过该问卷，不能重复填写！</div>
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
								action="Teacher_Survey_List1_addTchrSurveyDone?surveyId=<s:property
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
											<input type="hidden"
												id="selQuesId<s:property value="%{#status.count}" />"
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

											<input type="hidden"
												id="textQuesId<s:property value="%{#status.count}" />"
												value="<s:property value="#sq.questionId" />">

										</s:if>
									</ul>

								</s:iterator>
								<%-- <input type="text" name="survey.surveyId"
									value="<s:property value="survey.surveyId" />">
								<!-- 获取问卷的ID --> --%>

								<div class="top-distance">
									<span>问卷有效日期：</span><input type="text" name="startTime"
										style="width: 100px;" readonly id="startTime"
										value="<s:property  value="%{getText('{0,date,yyyy-MM-dd}',{survey.startTime})}"/>">至<input
										type="text" name="endTime" style="width: 100px;" readonly
										id="endTime"
										value="<s:property  value="%{getText('{0,date,yyyy-MM-dd}',{survey.endTime})}"/>">
								</div>
								<div class="top-distance right_align">
									<input type="submit" class="btn" id="submitSurvey" value="提交问卷"
										onclick="linkSelSubmit()">
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/include/footer.jsp"%>
	<script type="text/javascript" src="js/jquery1.12.1.js"></script>
	<script type="text/javascript" src="js/bootstrap.js"></script>
	<script type="text/javascript" src="js/survey.js"></script>
	<script type="text/javascript">
		var msg = "${requestScope.Message}";
		if (msg != "") {
			alert(msg);
		}
	<%request.removeAttribute("Message");%>
		//显示后将request里的Message清空，防止回退时重复显示。

		$(function() {
			$.getJSON("Json_selectSurveyReplayer", {
				surveyId : sessionStorage.getItem('surveyId')
			}, function(data) {
				if (data.amount > 0) {
					$("#submitSurvey").hide();
					$("#warn").show();
				} else {
					$("#submitSurvey").show();
					$("#warn").hide();
				}
			});
			$(".container").css("min-height",
					$(document).height() - 90 - 88 - 41 + "px");//container的最小高度为“浏览器当前窗口文档的高度-header高度-footer高度”
		});

		function linkSelSubmit() {

			var selecteds = document.getElementsByClassName("selected");
			for (var l = 1; l <= selecteds.length; l++) {
				document.getElementById("seled" + l + "").value = document
						.getElementById("selQuesId" + l + "").value;
			}
			//获取选中的单选
			var radios = document.getElementsByClassName("radio");
			for (var i = 0; i < radios.length; i++) {
				if (radios[i].checked) {
					document.getElementById("seled" + radios[i].name + "").value += "#"
							+ radios[i].id;
				}
			}
			//获取选中的多选
			var checkboxs = document.getElementsByClassName("checkbox");
			for (var j = 0; j < checkboxs.length; j++) {
				if (checkboxs[j].checked) {
					document.getElementById("seled" + checkboxs[j].name + "").value += "#"
							+ checkboxs[j].id;
				}
			}

			//获取文字问题的内容
			var texts = document.getElementsByClassName("textarea");
			for (var m = 0; m < texts.length; m++) {
				if (texts[m].value.trim() != "") {
					document.getElementById("text" + texts[m].name + "").value = document
							.getElementById("textQuesId" + texts[m].name + "").value;
					document.getElementById("text" + texts[m].name + "").value += "#"
							+ texts[m].value;
				}

			}
			//设置选择题name属性
			var selected = document.getElementsByClassName("selected");
			for (var k = 0; k < selected.length; k++) {
				selected[k].setAttribute("name", "surveySelectors[" + k
						+ "].remark");
			}
			//设置文本问题的name属性
			var textAnswer = document.getElementsByClassName("textAnswer");
			for (var n = 0; n < textAnswer.length; n++) {
				textAnswer[n].setAttribute("name", "textAnswers[" + n
						+ "].remark");
			}

		}
		//判断是否把问卷填写完整
		function isEmpty() {
			var selected = document.getElementsByClassName("selected");
			for (var k = 0; k < selected.length; k++) {
				if (selected[k].value.indexOf('#') < 0) {
					var num = selected[k].id.substr(5, selected[k].id.length);
					alert("第" + num + "题未做！请将问卷填写完整！");
					return false;
				}

			}
			//设置文本问题的name属性
			var textAnswer = document.getElementsByClassName("textAnswer");
			for (var n = 0; n < textAnswer.length; n++) {
				if (textAnswer[n].value.indexOf('#') < 0) {
					var num = textAnswer[n].id.substr(4,
							textAnswer[n].id.length);
					alert("第" + num + "题未做！请将问卷填写完整！");
					return false;
				}

			}
			return true;
		}
		//发布问卷
		function publishSurvey(surveyId) {
			$.getJSON("Json_publishSurvey", {
				surveyId : surveyId
			}, function(data) {
				alert("发布成功");
			});
		}
	</script>
</body>
</html>
