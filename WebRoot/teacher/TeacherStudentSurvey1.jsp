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
							<label>创建全新问卷</label>
						</div>
						<div class="div-inf-tbl">
							<form action="TeacherStudent_Survey_2_createSurvey" method="post"
								class="form-horizontal form-add" enctype="multipart/form-data"
								onsubmit="javascript:return isEmpty(1)">
								<label class="text-size">问卷标题</label><input type="text"
									class="input-l" name="survey.title" id="input-title"><label
									class="text-size top-distance">问卷描述</label>
								<textarea class="textarea-l" id="text-content"
									name="survey.discribe"></textarea>
								<label class="text-size top-distance">问卷调查对象</label><select
									class="top-distance" id="respondent" name="survey.respondent">
									<option value="1">学生</option>
									<option value="2">教师</option>
									<option value="3">全体师生</option>
								</select><br> <label class="text-size top-distance">问卷发起单位</label><input
									type="text" class="input-l" name="survey.sponsor"
									id="input-sponsor"><br>
								<div class="right_align">
									<input type="submit" class="btn top-distance" name="submit"
										id="submit" value="创建问卷">
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

		function isEmpty() {
			var title = $("#input-title").val();
			var content = $("#text-content").val();
			var sponsor = $("#input-sponsor").val();
			if (title.trim().length != 0 && content.trim().length != 0
					&& sponsor.trim().length != 0) {
				return true;
			} else {
				alert("输入框不能为空！");
				return false;
			}
		}
	</script>
</body>
</html>
