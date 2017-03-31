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

<title>学生参与项目页面</title>
<meta charset="UTF-8">

<link rel="stylesheet" href="css/bootstrap.css" />
<link rel="stylesheet" href="css/bootstrap-responsive.css" />
<link rel="stylesheet" href="css/common.css" />
<link rel="stylesheet" href="css/student_goal.css" />
<link rel="stylesheet" href="css/teacher_information.css" />
<link rel="stylesheet" href="css/teaching_management.css" />
<script type="text/javascript" src="js/jquery1.12.1.js"></script>

<script type="text/javascript">
	var msg = "${requestScope.Message}";
	if (msg != "") {
		alert(msg);
	}
<%request.removeAttribute("Message");%>
	//显示后将request里的Message清空，防止回退时重复显示。
</script>
</head>

<body>
	<%@ include file="/include/header.jsp"%>
	<%@ include file="/include/student_main_nav.jsp"%>
	<div class="content">
		<div class="container">
			<div class="row">
				<%@ include file="/include/stuLeftBar.jsp"%>
				<div class="span9">
					<div class="row">
						<div class="span9 div-content-white-bgr" style="min-height: 440px">
							<div class="div-inf-bar">
								<label>学生参与项目</label>
							</div>
							<div class="div-inf-tbl">
							<label class="lable-add" style="margin-bottom:10px"><a
									href="Student_Award_Add_selectItemEvaType">添加</a></label>
								<div>
								<table class="table table-bordered table-condensed"
									id="studentItemList">
									<thead>
										<tr>
											<th>项目编号</th>
											<th>项目名称</th>
											<th>审核状态</th>
											<th>得分</th>
											<th>操作</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<s:iterator value="siPageBean.list" var="i">
											<tr>
												<td><s:property value="#i.itemNum" /></td>
												<td><s:property value="#i.itemName" /></td>
												<td><s:property value="#i.itemState" /></td>
												<s:if test="#i.itemState=='已驳回'">
													<td>无效</td>
												</s:if>
												<s:if test="#i.itemState!='已驳回'">
													<td><s:property value="#i.itemScore" /></td>
												</s:if>
												<td><a onclick="return confirm('确认删除？')"
													href="Student_Portfolio_Activity_deleteItem?itemId=<s:property value="#i.stuItemId"/>">删除</a></td>
												<td><a
													href="Student_Award_Info_selectItemInfo?itemId=<s:property value="#i.stuItemId"/>">详情</a></td>
											</tr>
										</s:iterator>
									</tbody>
								</table>
								
									<input type=button class="btn btn-bottom" onclick="upPage()"
										id="upPage" value="上一页">&nbsp;&nbsp;<span id="page"><s:property
											value="siPageBean.page" /></span>&nbsp;&nbsp;<input type="button"
										class="btn btn-bottom" onclick="downPage()" id="downPage"
										value="下一页"><span class="left-distance">共&nbsp;&nbsp;<span
										id="totalPage"><s:property value="siPageBean.totalPage" /></span>&nbsp;&nbsp;页
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


	<script type="text/javascript" src="js/bootstrap.js"></script>
	<script>
		$(function() {
			$(".container").css("min-height",
					$(document).height() - 90 - 88 - 41 + "px");//container的最小高度为“浏览器当前窗口文档的高度-header高度-footer高度”
		});

		function upPage() {
			var page = parseInt($("#page").html());
			if (page == 1) {
				alert("这已经是第一页！");
			} else {
				selectStudentItems(page - 1);
			}
		}
		function downPage() {
			var totalPage = parseInt($("#totalPage").html());
			var page = parseInt($("#page").html());
			if (page == totalPage) {
				alert("这已经是最后一页！");
			} else {
				selectStudentItems(page + 1);
			}
		}

		function selectStudentItems(page) {
			$("#studentItemList tbody").html("");
			$
					.getJSON(
							"Json_selectItem",
							{
								page : page
							},
							function(data) {
								$("#page").html(data.siPageBean.page);
								$("#totalPage").html(data.siPageBean.totalPage);
								$
										.each(
												data.siPageBean.list,
												function(i, value) {

													$("#studentItemList")
															.append(
																	"<tr><td>"
																			+ value.itemNum
																			+ "</td><td>"
																			+ value.itemName
																			+ "</td><td>"
																			+ value.itemState
																			+ "</td><td>"
																			+ value.itemScore
																			+ "</td><td><a onclick='return confirm('确认删除？')' href='Student_Portfolio_Activity_deleteItem?itemId="
																			+ value.stuItemId
																			+ "'>删除</a></td><td><a href='Student_Award_Info_selectItemInfo?itemId="
																			+ value.stuItemId
																			+ "'>详情</a></td></tr>");
												});
							});
		}
	</script>
</body>
</html>
