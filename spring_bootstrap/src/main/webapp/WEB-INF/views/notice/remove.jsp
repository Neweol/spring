<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script>
	alert("${notice.nno}");
	
	window.onload=function(){
		
		CloseWindow();
		window.opener.loacation.reload();
	}
</script>
    