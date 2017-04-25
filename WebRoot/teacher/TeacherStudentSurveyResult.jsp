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
<link rel="stylesheet" href="css/teaching_management.css" />
<link rel="stylesheet" type="text/css"
	href="css/pieChart/jquery.jqChart.css" />
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
							<div style="font-size: 17px; color: red;">
								受访总人数： <a
									href="TeacherStudent_SurveyReplyer_List_selectSurveyReplyerById?surveyId=<s:property value="survey.surveyId" />"><s:property
										value="survey.sumNum" /></a> 人
							</div>
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
							<s:iterator value="surveyQuestions" var="sq" status="status">
								<div onmouseover='showDel(this)' onmouseout='hideDel(this)'>
									<div class="question-style top-distance"
										style="display: inline-block; vertical-align: middle">
										Q
										<s:property value="%{#status.count}" />
										：
										<s:property value="#sq.content" />
										<s:if test="#sq.type==2">
											<span>（多选题）</span>
										</s:if>
										<s:if test="#sq.type==3">
											<span>（文本题）</span>
										</s:if>
										<s:if test="#sq.type==1">
											<img class="small_img show_Pie" src="img/showPie.png"
												onclick="showChart(this,1)"
												name="<s:property value="#sq.questionId" />"
												id="img<s:property value="#sq.questionId" />" />
											<img class="small_img show_Pie" src="img/showPie.png"
												onclick="showPie(this)" style="display: none"
												name="<s:property value="#sq.questionId" />"
												id="image<s:property value="#sq.questionId" />" />
											<img class="small_img" src="img/delsel.gif"
												onclick="hidePie(this)"
												name="<s:property value="#sq.questionId" />" />
										</s:if>
										<s:if test="#sq.type==2">
											<img class="small_img show_Pie" src="img/showPie.png"
												onclick="showChart(this,2)"
												name="<s:property value="#sq.questionId" />"
												id="img<s:property value="#sq.questionId" />" />
											<img class="small_img show_Pie" src="img/showPie.png"
												onclick="showPie(this)" style="display: none"
												name="<s:property value="#sq.questionId" />"
												id="image<s:property value="#sq.questionId" />" />
											<img class="small_img" src="img/delsel.gif"
												onclick="hidePie(this)"
												name="<s:property value="#sq.questionId" />" />
										</s:if>
										<s:if test="#sq.type==3">
											<img class="small_img show_Pie" src="img/showPie.png"
												onclick="showTextChart(<s:property value="#sq.questionId" />,1)"
												name="<s:property value="#sq.questionId" />"
												id="img<s:property value="#sq.questionId" />" />
											<img class="small_img show_Pie" src="img/showPie.png"
												onclick="showPie(this)" style="display: none"
												name="<s:property value="#sq.questionId" />"
												id="image<s:property value="#sq.questionId" />" />
											<img class="small_img" src="img/delsel.gif"
												onclick="hidePie(this)"
												name="<s:property value="#sq.questionId" />" />
										</s:if>
										<s:if test="#sq.type==4">
											<img class="small_img show_Pie" src="img/showPie.png"
												onclick="showTableChart(this,4)"
												name="<s:property value="#sq.questionId" />"
												id="img<s:property value="#sq.questionId" />" />
											<img class="small_img show_Pie" src="img/showPie.png"
												onclick="showPie(this)" style="display: none"
												name="<s:property value="#sq.questionId" />"
												id="image<s:property value="#sq.questionId" />" />
											<img class="small_img" src="img/delsel.gif"
												onclick="hidePie(this)"
												name="<s:property value="#sq.questionId" />" />
										</s:if>

									</div>
									<ul class="question-style">
										<s:if test="#sq.type==1||#sq.type==2">
											<s:generator val="#sq.selectors" separator="_" id="s" />
											<s:iterator status="st" value="#request.s" id="selector">
												<li class="li_style selector-style"><s:if
														test="#sq.type==1">
														<span class="serialNumber"
															id="<s:property value="%{#st.count}" />"></span>
														<span class="left_distance"><s:property
																value="selector" /></span>
													</s:if> <s:if test="#sq.type==2">
														<span class="serialNumber"
															id="<s:property value="%{#st.count}" />"></span>
														<span class="left_distance"><s:property
																value="selector" /></span>
													</s:if></li>
											</s:iterator>
											<!-- 获取选中的选项的selectorNum -->

											<div id="jqChart<s:property value="#sq.questionId"/>"
												class="pieChart_style"></div>
											<input id="svgChart<s:property value="#sq.questionId"/>"
												class="hidden" />

										</s:if>
										<s:if test="#sq.type==3">
											<%-- <li class="li_style selector-style"><textarea
													name="<s:property value="%{#status.count}" />"
													placeholder='请填写内容' class='textarea left_distance'
													style='width: 72%; height: 50px' readonly></textarea></li> --%>
											<div id="jqChart<s:property value="#sq.questionId"/>"
												style="display: none; width: 600px">
												<table class="table table-bordered table-condensed"
													id="textAnswerList<s:property value="#sq.questionId" />">
													<thead>
														<tr>
															<td class="align_left">答案：</td>
														</tr>
													</thead>
													<tbody></tbody>
												</table>
												<div>
													<input type=button class="btn btn-bottom"
														onclick="upPage(this)"
														id="upPage<s:property value="#sq.questionId" />"
														name="<s:property value="#sq.questionId" />" value="上一页">&nbsp;&nbsp;<span
														id="page<s:property value="#sq.questionId" />"></span>&nbsp;&nbsp;<input
														type="button" class="btn btn-bottom"
														onclick="downPage(this)"
														id="downPage<s:property value="#sq.questionId" />"
														name="<s:property value="#sq.questionId" />" value="下一页"><span
														class="left-distance">共&nbsp;&nbsp;<span
														id="totalPage<s:property value="#sq.questionId" />"></span>&nbsp;&nbsp;页
													</span>
												</div>
											</div>
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
												</table>
												<div id="jqChart<s:property value="#sq.questionId"/>"
													class="pieChart_style"></div> <input
												id="svgChart<s:property value="#sq.questionId"/>"
												class="hidden" />
											</li>
										</s:if>
									</ul>

								</div>
							</s:iterator>
						</div>
						<input type="hidden" id="surveyId"
							value="<s:property value="survey.surveyId" />">
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/include/footer.jsp"%>
	<script type="text/javascript" src="js/jquery1.12.1.js"></script>
	<script type="text/javascript" src="js/bootstrap.js"></script>
	<script src="https://code.highcharts.com/highcharts.js"
		type="text/javascript" charset="utf-8"></script>
	<script lang="javascript" type="text/javascript" src="js/chart.js"></script>
	<script type="text/javascript" src="js/surveyResult.js"></script>
	<script src="https://code.highcharts.com/modules/exporting.js"
		type="text/javascript" charset="utf-8"></script>



	<script type="text/javascript">
		var msg = "${requestScope.Message}";
		if (msg != "") {
			alert(msg);
		}
		$(function() {
			$(".container").css("min-height",
					$(document).height() - 90 - 88 - 41 + "px");//container的最小高度为“浏览器当前窗口文档的高度-header高度-footer高度”
			numChangeToCode();
		});
	<%request.removeAttribute("Message");%>
		//显示后将request里的Message清空，防止回退时重复显示。	
	</script>
</body>
</html>
