<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>index</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
     <style type="text/css">
	    body {
			margin: 0;
			background: rgba(247, 250, 251, 1) url('./img/homebackground.png') left top repeat-x;
			min-width: 1000px;
			font-family:Microsoft YaHei;
		}
		.only-one {
			position:absolute;
			top: 28%;
			width: 540px;
			left:50%;
			margin-left:-270px
		}
		.sysNameImg {
			height: 35px;
		}
		table {
			font-size:1.5em;
  			text-align:center;
  			padding-top:15px;
  			padding-bottom:4px;
		}
		td {
			position: relative;
			top: 20px;
			width: 540px;
			height: 60px;
  			border:1.2px solid;
  			border-style: outset
		}
		.next-table{
			position: absolute;
			margin-top: 20px;
		}
		a {
			text-decoration:none;
		}
		a:link {
			color: #000000;
		}
		a:hover {
			color:#dddddd;
		}
		a:visited {
			color: #000000;
		}
	</style>
  </head>
  
  <body>
	  <div class="only-one">
	  		<img class="sysNameImg" src="img/sysName.png">
	  		<table>
	  			<tr>
	  				<td><a href="Major___selectMajorInf">2015级入口</a></td>
	  			</tr>
	  			<tr class="next-table">
	  				<td><a href="http://localhost:8080/eme2016/Major___selectMajorInf">2016级入口</a></td>
	  			</tr>
	  		</table>
		</div>
 </body>
</html>
