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

							<form
								action="TeacherStudent_Survey_List_modifySurvey?surveyId=<s:property
										value="survey.surveyId" />"
								method="post" class="form-horizontal"
								onsubmit="javascript:return isEmpty1()">
								<div>
									<h3 class="title_center">
										<input type="text" class="input-l" name="survey.title"
											id="input-title" value="<s:property value="survey.title" />">
									</h3>
									<p class="top-distance para-indent">
										<textarea class="textarea-l" id="text-content"
											name="survey.discribe"><s:property
												value="survey.discribe" /></textarea>
									</p>
									<p class="top-distance para-indent align_right">
										<select class="top-distance" id="respondent"
											name="survey.respondent">
											<option value="1" <s:if test="survey.respondent==1">selected</s:if>>学生</option>
											<option value="2" <s:if test="survey.respondent==2">selected</s:if>>老师</option>
						 					<option value="3" <s:if test="survey.respondent==3">selected</s:if>>全部</option>
										</select><br>
									</p>
									<p class="top-distance para-indent align_right">
										<input type="text" class="input-l" name="survey.sponsor"
											id="input-sponsor"
											value="<s:property value="survey.sponsor" />">
									</p>
								</div>
								<hr />
								<div id="div-content" class="top-distance">
									<s:iterator value="surveyQuestions" var="sq" status="status">
										<section>
											<div class="control-group">
												<s:if test="#sq.type==1||#sq.type==2">
													<div class="ques"
														id="ques<s:property value="%{#status.count}" />">

														<div onmouseover='showDel(this)'
															onmouseout='hideDel(this)'>

															<span id="Q<s:property value="%{#status.count}" />">Q<s:property
																	value="%{#status.count}" />:
															</span><input type='text'
																id="quesname<s:property value="%{#status.count}" />"
																value="<s:property
												value="#sq.content" />"
																class='input-long question-style' placeholder='问卷题目'>
															<img id="
				<s:property value="%{#status.count}" />"
																class='small_img' src='img/delquest.gif' alt='删除'
																onclick='delQuestion(this)'>
														</div>
														<div id="sel<s:property value="%{#status.count}" />">

															<s:generator val="#sq.selectors" separator="_" id="s" />
															<s:iterator status="st" value="#request.s" id="selector">
																<s:if test="#sq.type==1">

																	<div onmouseover='showDel(this)'
																		onmouseout='hideDel(this)'
																		class="selector
				<s:property value="%{#status.count}"/> selector-style">
																		<input type="radio"
																			name="S<s:property value="%{#status.count}" />"><input
																			type='text' placeholder='选项'
																			class="ST<s:property value="%{#status.count}" />
				 left_distance"
																			value="<s:property
																value="selector" />">
																		<img id="<s:property value="%{#status.count}" />"
																			name="1" class='small_img' src='img/addsel.png'
																			alt='添加' onclick='addSelector(this)'> <img
																			class="image<s:property value="%{#status.count}" />
				small_img"
																			name="image<s:property value="%{#status.count}" />"
																			src='img/delsel.gif' alt='删除'
																			onclick='delSelector(this)'>
																	</div>

																</s:if>
																<s:if test="#sq.type==2">
																	<div id="sel<s:property value="%{#status.count}" />">
																		<div onmouseover='showDel(this)'
																			onmouseout='hideDel(this)'
																			class="selector
				<s:property value="%{#status.count}"/> selector-style">
																			<input type="checkbox"
																				name="S<s:property value="%{#status.count}" />"><input
																				type='text' placeholder='选项'
																				class="ST<s:property value="%{#status.count}" />
				 left_distance"
																				value="<s:property
																value="selector" />">
																			<img id="<s:property value="%{#status.count}" />"
																				name="2" class='small_img' src='img/addsel.png'
																				alt='添加' onclick='addSelector(this)'> <img
																				class="image<s:property value="%{#status.count}" />
				small_img"
																				name="image<s:property value="%{#status.count}" />"
																				src='img/delsel.gif' alt='删除'
																				onclick='delSelector(this)'>
																		</div>
																	</div>
																</s:if>
															</s:iterator>
														</div>
														<div>
															<input type='hidden'
																id="AST<s:property value="%{#status.count}" />"><input
																type='hidden'
																id="QT<s:property value="%{#status.count}" />"
																value="<s:if test="#sq.type==1">1</s:if><s:if test="#sq.type==2">2</s:if>">
														</div>

													</div>
												</s:if>
												<s:if test="#sq.type==3">
													<section>
														<div class="control-group">
															<div class="ques"
																id="ques<s:property value="%{#status.count}" />">
																<div onmouseover='showDel(this)'
																	onmouseout='hideDel(this)'>
																	<span id='Q"
				+ quesNum
				+ "'>Q<s:property
																			value="%{#status.count}" />:
																	</span><input type='text'
																		id="quesname<s:property value="%{#status.count}" />"
																		value="<s:property
												value="#sq.content" />"
																		class='input-long question-style' placeholder='问卷题目'>
																	<img id="<s:property value="%{#status.count}" />"
																		class='small_img' src='img/delquest.gif' alt='删除'
																		onclick='delQuestion(this)'>
																</div>
																<div id="sel<s:property value="%{#status.count}" />"></div>
																<div
																	class="selector<s:property value="%{#status.count}" /> selector-style">
																	<textarea placeholder='请填写内容'
																		class="ST<s:property value="%{#status.count}" /> left_distance"
																		style='width: 72%; height: 100px' readonly></textarea>
																	<input type='hidden'
																		id="AST<s:property value="%{#status.count}" />"><input
																		type='hidden'
																		id="QT<s:property value="%{#status.count}" />"
																		value="3">
																</div>
															</div>
														</div>
													</section>
												</s:if>
											</div>
										</section>
									</s:iterator>
								</div>
								<div class="submit_top">
									<input type="hidden" name="survey.surveyId"
										value="<s:property
										value="survey.surveyId" />">
									<span>问卷有效时间：</span>
									<input
										type="text" name="survey.startTime" id="startTime"
										style="width: 110px" value="<s:property
														value="%{getText('{0,date,yyyy-MM-dd}',{survey.startTime})}"/>"
										onFocus="WdatePicker(WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd',readOnly:'true'})) ">至<input
										type="text" name="survey.endTime" id="endTime"
										style="width: 110px"  value="<s:property
														value="%{getText('{0,date,yyyy-MM-dd}',{survey.endTime})}"/>"
										onFocus="WdatePicker(WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd',readOnly:'true'})) ">
									
									<%--  <input type="date" name="survey.startTime"
										id="startTime" value="<s:property
														value="%{getText('{0,date,yyyy-MM-dd}',{survey.startTime})}"/>" />至
									<input type="date" name="survey.endTime" id="endTime" value="<s:property
														value="%{getText('{0,date,yyyy-MM-dd}',{survey.endTime})}"/>"/>  --%>
								</div>
								<div class="right_align"><input
										type="submit" class="btn" name="submit" id="submit"
										value="保    存" onclick="linksel()" style="margin-left: 30px"></div>
							</form>
						</div>
						<hr>
						<div class="selectdiv_style">
							<div style="margin-left: 15%">
								<input type="button" name="addQues" id="1" value="单  选"
									class="btn select_style" onclick="addQuestion(this)"> <input
									type="button" name="addQues" id="2" value="多  选"
									class="btn select_style" onclick="addQuestion(this)"><input
									type="button" name="addQues" id="3" value="文字题"
									class="btn select_style" onclick="addQuestion(this)">
							</div>
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
		function isEmpty1() {
			var title = $("#input-title").val();
			var content = $("#text-content").val();
			var sponsor = $("#input-sponsor").val();
			if (title.trim().length == 0 || content.trim().length == 0
					|| sponsor.trim().length == 0) {
				alert("请将问卷设计填写完整！");
				return false;
			}
			var ques = document.getElementsByClassName("ques").length; // 已有多少个问题
			if (ques == 0) {
				alert("请添加问题！");
				return false;
			} else {
				var list = document.getElementsByTagName("input");
				for (var i = 0; i < list.length; i++) {
					// 判断是否为文本框
					if (list[i].type == "text") {
						// 判断文本框是否为空
						if (list[i].value == "") {
							alert("请将问卷设计填写完整！");
							return false;
						}
					}
				}
			}
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			if (startTime == "" || endTime == "") {
				alert("请选择开始和结束时间！");
				return false;
			} else {
				var date1 = new Date(startTime);
				var date2 = new Date(endTime);
				if (date1.getTime() > date2.getTime()) {
					alert("结束时间应大于开始时间！");
					return false;
				}
			}
			return true;
		}
	</script>
</body>
</html>
