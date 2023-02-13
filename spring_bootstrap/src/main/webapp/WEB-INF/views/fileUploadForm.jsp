<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3>multipartFile</h3>
<form enctype="multipart/form-data" action="multipartFile" method="post">
	제목 : <input type="text" name="title" /><br/>
	파일 : <input type="file" name="file" /><br/>
	<input type="submit" value="전송" />
</form>

<h3>MultipartHttpServletRequest</h3>
<form enctype="multipart/form-data" action="multipartHttpServletRequest" method="post">
	제목 : <input type="text" name="title" /><br/>
	파일 : <input type="file" name="file" /><br/>
	<input type="submit" value="전송" />
</form>

<h3>커맨드 객체 사용</h3>
<form action="commandObject" method="post" enctype="multipart/form-data">
	제목 : <input type="text" name="title" /> <br/>
	파일 : <input type="file" name="file" /> <br/>
	<input type="submit" value="전송" />
</form>


</body>
</html>