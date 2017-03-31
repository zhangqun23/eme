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

<title>查看学生信息</title>
<meta charset="UTF-8">

<link rel="stylesheet" href="css/bootstrap.css" />
<link rel="stylesheet" href="css/bootstrap-responsive.css" />
<link rel="stylesheet" href="css/common.css" />
<link rel="stylesheet" href="css/teacher_information.css" />
<link rel="stylesheet" href="css/student_information.css" />
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
					<div class="row">
						<div class="span9 div-content-white-bgr">
							<div class="div-inf-bar">
								<label>学生信息</label>
							</div>
							<!-- 学生基本信息 -->

							<div class="div-inf-tbl">
								<table class="table table-bordered">
									<tbody>
										<tr>
											<td>学号</td>
											<td id="stuSchNum"><s:property value="s.stuSchNum" /></td>
											<td>姓名</td>
											<td><s:property value="s.stuName" /></td>
											<td class="img-stu" rowspan=4><img
												src="stuImg/<s:property value="s.stuSchNum"/>.jpg" /></td>
										</tr>
										<tr>
											<td>性别</td>
											<td><s:if test="%{s.stuGender==true}">男</s:if> <s:else>女</s:else></td>
											<td>生日</td>
											<td><s:property value="s.stuBirthday" /></td>
										</tr>
										<tr>
											<td>籍贯</td>
											<td><s:property value="s.stuNativePlace" /></td>
											<td>民族</td>
											<td><s:property value="s.stuNation" /></td>
										</tr>
										<tr>
											<td>身份证</td>
											<td><s:property value="s.stuIdentNum" /></td>
											<td>班级</td>
											<td><s:property value="s.clazz.claName" /></td>
										</tr>
										<tr>
											<td>入学日期</td>
											<td><s:property value="s.stuAttendDate" /></td>
											<td>学制</td>
											<td colspan=2><s:property value="s.stuSchLength" /></td>
										</tr>
										<tr>
											<td>手机</td>
											<td><s:property value="s.stuPhone" /></td>
											<td>宿舍电话</td>
											<td colspan=2><s:property value="s.stuDomiPhone" /></td>
										</tr>
										<tr>
											<td>邮箱</td>
											<td><s:property value="s.stuMail" /></td>
											<td>通信地址</td>
											<td colspan=2><s:property value="s.stuCommAddr" /></td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class="div-inf-tbl">
								<h5>获奖情况</h5>

								<table class="table table-bordered " id="itemStuList">
									<thead>
										<tr>
											<th>项目编号</th>
											<th>项目名称</th>
											<th>审核状态</th>
											<th>得分</th>
											<th>审核</th>
										</tr>
									</thead>
									<tbody>
										<s:iterator value="siPageBean.list" var="i">
											<tr>
												<td><s:property value="#i.itemNum" /></td>
												<td><s:property value="#i.itemName" /></td>
												<td><s:property value="#i.itemState" /></td>
												<s:if test="#i.itemState=='已驳回'"><td>无效</td></s:if>
												<s:if test="#i.itemState!='已驳回'"><td><s:property value="#i.itemScore" /></td></s:if>
												
												<td><a
													href="TeacherStudent_Item_1_selectItemInfo?itemId=<s:property value="#i.stuItemId"/>">详情审核</a></td>
											</tr>
										</s:iterator>
									</tbody>
								</table>
								<div>
									<input type=button class="btn btn-bottom" onclick="upPage()"
										id="upPage" value="上一页">&nbsp;&nbsp;<span id="page"><s:property
											value="siPageBean.page" /></span>&nbsp;&nbsp;<input type="button"
										class="btn btn-bottom" onclick="downPage()" id="downPage"
										value="下一页"><span class="left-distance">共&nbsp;&nbsp;<span
										id="totalPage"><s:property value="siPageBean.totalPage" /></span>&nbsp;&nbsp;页
									</span> 
								</div>
							</div>
							<div class="div-inf-tbl">
								<h5>自我介绍</h5>
								<div class="div-tchr-self-intr-content">
									<label><s:property value="s.selfIntroduce" /></label>
								</div>
							</div>
							<div class="div-inf-tbl">
								<h5>英文自我介绍</h5>
								<div class="div-tchr-self-intr-content">
									<label><s:property value="s.selfEngIntroduce" /></label>
								</div>
							</div>
							<!-- 学生基本信息完 -->
							<!-- 学生目标 -->
							<div class="div-inf-tbl">
								<h5>短期目标</h5>
								<div class="div-tchr-self-intr-content">
									<label><s:property value="s.shortGoal" /></label>
								</div>
								<h5>中期目标</h5>
								<div class="div-tchr-self-intr-content">
									<label><s:property value="s.midGoal" /></label>
								</div>
								<h5>长期目标</h5>
								<div class="div-tchr-self-intr-content">
									<label><s:property value="s.longGoal" /></label>
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
	<script src="js/stuListPage.js"></script>
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
			var stuSchNum=$("#stuSchNum").html();
			$("#itemStuList tbody").html("");
			$
					.getJSON(
							"Json_selectItem",
							{
								page : page,
								stuNum:stuSchNum
							},
							function(data) {
								$("#page").html(data.siPageBean.page);
								$("#totalPage").html(data.siPageBean.totalPage);
								$
										.each(
												data.siPageBean.list,
												function(i, value) {

													$("#itemStuList")
															.append(
																	"<tr><td>"
																			+ value.itemNum
																			+ "</td><td>"
																			+ value.itemName
																			+ "</td><td>"
																			+ value.itemState
																			+ "</td><td>"
																			+ value.itemScore
																			+ "</td><td><a href='TeacherStudent_Item_1_selectItemInfo?itemId="
																			+ value.stuItemId
																			+ "'>详情审核</a></td></tr>");
												});
							});
		}
	</script>
</body>
</html>
