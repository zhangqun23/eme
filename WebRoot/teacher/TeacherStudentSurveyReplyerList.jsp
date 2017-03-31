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
<link rel="stylesheet" href="css/teacher_information.css" />
<link rel="stylesheet" href="css/teaching_management.css" />


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
							<label>问卷受访者列表</label>
						</div>
						<div class="div-inf-tbl">
							<div class="div-tchr-detail">
								<span style="font-size: 17px;">问卷名称：<s:property
										value="survey.title" /><span id="surveyId"
									style="display: none"><s:property
											value="survey.surveyId" /></span></span>
								<table class="table table-bordered table-condensed"
									id="surveyReplyerList">
									<thead>
										<tr>
											<th width="80px">学号</th>
											<th width="80px">姓名</th>
											<th width="80px">班级</th>
											<th width="160px">填写时间</th>
										</tr>
									</thead>
									<tbody>
										<s:iterator value="srPageBean.list" var="sr">
											<tr>
												<td><s:property value="#sr.student.stuSchNum" /></td>
												<td><s:property value="#sr.student.stuName" /></td>
												<td><s:property value="#sr.student.clazz.claName" /></td>
												<td><s:property
														value="%{getText('{0,date,yyyy-MM-dd  HH:mm:ss}',{#sr.replyTime})}" /></td>
											</tr>
										</s:iterator>
									</tbody>
								</table>
								<div>
									<input type=button class="btn btn-bottom" onclick="upPage()"
										id="upPage" value="上一页">&nbsp;&nbsp;<span id="page"><s:property
											value="srPageBean.page" /></span>&nbsp;&nbsp;<input type="button"
										class="btn btn-bottom" onclick="downPage()" id="downPage"
										value="下一页"><span class="left-distance">共&nbsp;&nbsp;<span
										id="totalPage"><s:property value="srPageBean.totalPage" /></span>&nbsp;&nbsp;页
									</span>
								</div>
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
		//查找下一页或上一页问卷列表
		function selectSurveyReplyers(page) {
			$("#surveyReplyerList tbody").html("");
			$.getJSON("Json_selectSurveyReplyersById", {
				page : page,
				surveyId : $("#surveyId").text()
			}, function(data) {
				$("#page").html(data.srPageBean.page);
				$("#totalPage").html(data.srPageBean.totalPage);
				if (data.srPageBean.list.length == 0) {
					alert("未找到相关数据！");
				} else {
					$.each(data.srPageBean.list, function(i, value) {
						var replyDate = value.replyTime.substr(0,
								value.replyTime.indexOf('T'));
						var replyTime = value.replyTime.substr(value.replyTime
								.indexOf('T') + 1, value.replyTime.length);
						var replyDateTime = replyDate + "  " + replyTime;

						$("#surveyReplyerList").append(
								"<tr><td>" + value.student.stuSchNum
										+ "</td><td>" + value.student.stuName
										+ "</td><td>"
										+ value.student.clazz.claName
										+ "</td><td>" + replyDateTime
										+ "</td></tr>");
					});
				}

			});
		}
		//上一页
		function upPage() {
			var page = parseInt($("#page").html());
			if (page == 1) {
				alert("这已经是第一页！");
			} else {
				selectSurveyReplyers(page - 1);
			}
		}

		//下一页
		function downPage() {
			var totalPage = parseInt($("#totalPage").html());
			var page = parseInt($("#page").html());
			if (page == totalPage) {
				alert("这已经是最后一页！");
			} else {
				selectSurveyReplyers(page + 1);
			}
		}
	</script>
</body>
</html>
