<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<form enctype="multipart/form-data" action="multipartFile" method="post">
	제목 : <input type="text" name="title" /><br/>
	파일 : <input type="file" name="file" /><br/>
	<input type="submit" value="전송" />
</form>

</body>
</html>