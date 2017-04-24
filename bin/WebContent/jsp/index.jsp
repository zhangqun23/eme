<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<jsp:include page="/jsp/top.jsp" />
<section class="main" ng-app="index" ng-controller="IndexController">
	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="#">首页</a></li>
		</ul>
	</div>

	<div class="mainindex">
		<div class="welinfo">
			<span><img src="${ctx}/images/sun.png" alt="天气" /></span> <b>欢迎使用酒店大数据统计分析系统</b>
			<!-- <a href="#">帐号设置</a> -->
		</div>

		<div class="xline"></div>
		<br> <br>
		<div class="welinfo">
			<span><img src="${ctx}/images/dp.png" alt="提醒" /></span> <b>员工工作量</b>
		</div>
		<div id="chartContainer"></div>

		<div class="welinfo">
			<span><img src="${ctx}/images/dp.png" alt="提醒" /></span> <b>员工工作量明细</b>
		</div>


	</div>
</section>
<jsp:include page="/jsp/left.jsp" />
<jsp:include page="/jsp/footer.jsp" />
<script src="${ctx}/js/app/index.js"></script>

</body>
</html>