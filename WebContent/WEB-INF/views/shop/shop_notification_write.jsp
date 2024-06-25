<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항</title>
<%
    //경로 설정
    	String contextPath = request.getContextPath();
		String cssConPath = contextPath + "/resources/css/shop_headerContainer.css";
		String cssMainPath = contextPath + "/resources/css/shop_notification.css";
%>
<link rel="stylesheet" href="resources/css/shop_headerContainer.css">
<link rel="stylesheet" href="resources/css/shop_notification.css">

<%
request.setCharacterEncoding("utf-8");
%>
</head>
<body>
	<div id="container">
		<%@ include file="shop_header.jsp"%>
	</div>
	<div id="wrapper">
	<form action="<%=contextPath%>/shop_notification_writepro" method="get">
	<!-- <input type = "hidden" name = "page" value = "write"> -->
		<h1>공지사항 입력하기</h1>
		<div class="blackstick"></div>
		<div class="notification_head">			
			<p>
				제목: <input type="text" class="title" name="notification_Subject" placeholder="제목을 입력하세요">
			</p>
		</div>
		<div class="notification_content">
			<textarea class="contentwrite" name="notification_Content" rows="10" cols="40" placeholder="내용을 입력하세요"></textarea>
		</div>
		<div class="buttons">
			<button type="submit" class="buts">작성</button> 
			<a class="buts" href="<%=contextPath%>/shop_notification">목록 돌아가기</a>
		</div>
	</form>
	</div>
</body>
</html>
