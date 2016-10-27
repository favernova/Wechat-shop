<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
</head>
    <form name="regform" action="register.do" method="post">
    用户名 : <input type="text" name="userName">
    <br>
    密码 : <input type="password" name="password"> 
    <br>
    再次输入密码 : <input type="password" name="password2"> 
    <br>
    邮箱：<input type="text" name="email"> 
    <br>
    地址: <input type="text" name="address"><br>
    昵称: <input type="text" name="nickName"><br>
<!--     头像: <input type="file" name=photo><br> -->
    <input type="submit" value="注册"><br>
    ${error}
    </form>
        <a href="login">我已经注册过</a>
</body>
</html>