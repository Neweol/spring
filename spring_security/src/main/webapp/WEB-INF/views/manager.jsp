<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>manager.jsp</h1>
<h1>${loginUser.name }님 환영합니다.</h1>

<a href="<%=request.getContextPath() %>">[/index]</a>

	<ul>
		<li><a href="<%=request.getContextPath() %>/home">/home</a></li>
		<li><a href="<%=request.getContextPath() %>/member">/member</a></li>
		<li><a href="<%=request.getContextPath() %>/manager">/manager</a></li>
		<li><a href="<%=request.getContextPath() %>/admin">/admin</a></li>
	</ul>





</body>
</html>