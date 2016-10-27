<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
</head>
<body>
	<c:choose> 
		<c:when test="${empty name}">
	    <form name="logform" action="login.do" method="post">
	    UserName : <input type="text" name="userName">
	    <br>
	    Password : <input type="password" name="password"> 
	    <br>
	    <input type="submit" value="提交">
	    <a href="register">注册</a>
	    </form>
	    </c:when>
		<c:otherwise>   
	    您好，${name} 您已经登录
	    <br>
	    <a href="logout">注销</a>
	  	</c:otherwise> 
    </c:choose>
</body>
</html>