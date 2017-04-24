<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8" />
<title>酒店大数据统计分析系统</title>
<link href="${ctx}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/admin.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/zhou.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/li.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/css/wang.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	Date.prototype.format = function(format) {
		var date = {
			"M+" : this.getMonth() + 1,
			"d+" : this.getDate(),
			"h+" : this.getHours(),
			"m+" : this.getMinutes(),
			"s+" : this.getSeconds(),
			"q+" : Math.floor((this.getMonth() + 3) / 3),
			"S+" : this.getMilliseconds()
		};
		if (/(y+)/i.test(format)) {
			format = format.replace(RegExp.$1, (this.getFullYear() + '')
					.substr(4 - RegExp.$1.length));
		}
		for ( var k in date) {
			if (new RegExp("(" + k + ")").test(format)) {
				format = format.replace(RegExp.$1,
						RegExp.$1.length == 1 ? date[k] : ("00" + date[k])
								.substr(("" + date[k]).length));
			}
		}
		return format;
	}
</script>
</head>

<body style="background:url(${ctx}/images/topbg.gif) repeat-x;">
	<header>
		<div class="topleft" style="text-align: center;">
			<!-- <span style="font-size: 20px; color: white; margin-top: 34px;">酒店大数据统计分析系统</span> -->
			<a href="#" target="_parent"><img class="img-logo"
				src="${ctx}/images/logo1.png" title="系统首页" /></a>
		</div>



		<div class="topright">
			<ul>
				<li><a href="/HDR/login/logout.do">安全退出</a></li>
			</ul>

			<!-- 	<div class="user">

				<span id="userNum"></span> <i>消息</i><a
					href=""><b
					id="newsNum">0</b></a>
			</div> -->

		</div>

	</header>
	<section class="containner">
		<!-- 加载模态框 -->
		<div class="overlayer"></div>
		<div class="tipLoading">
			<img class="tipimage" src="../images/wait.gif" />
			<div class="tiptext">正在加载，请稍后……</div>
		</div>
		<!-- 加载模态框 -->