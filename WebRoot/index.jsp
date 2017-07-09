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
    <script>
    	/* window.onload = function() {
    		document.getElementById("toMain").click(); 		
    	} */
    </script> 
  </head>
  
  <body>
  	<a id="toMain" href="Major___selectMajorInf" >2015登录</a><br>
  	<a id="toMain" href="http://localhost:8080/eme2016/Major___selectMajorInf" >2016登录</a><br>
 </body>
</html>
