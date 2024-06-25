<%@page import="java.net.URLEncoder"%>
<%@page import="sample.spring.dto.ShopNotificationDTO"%>
<%@ page import="java.util.List"%>
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
		List<ShopNotificationDTO> dtoL = (List<ShopNotificationDTO>) request.getAttribute("dtoL");
		ShopNotificationDTO dto = (dtoL != null && !dtoL.isEmpty()) ? dtoL.get(0) : null;
%>
	<link rel = "stylesheet" href = "resources/css/shop_headerContainer.css">
    <link rel = "stylesheet" href = "resources/css/shop_notification.css">    
<%
request.setCharacterEncoding("utf-8");
%>
</head>
<body>
	<div id="container">
		<%@ include file="./shop_header.jsp"%>
	</div>
	<div id="wrapper">
	<form action="<%=contextPath%>/shop_notification_updatepro" method="get">
	<!-- <input type = "hidden" name = "page" value = "update"> -->
		<h1>공지사항 수정하기</h1>
		<div class="blackstick"></div>
		<div class="notification_head">
		<input type="hidden" name="notification_No" value=<%=dto.getNotification_No() %>>
			<p>			
			
				제목 : <input type="text" name="notification_Subject"	value="<%=dto.getNotification_Subject() %>">
			
			</p>
		</div>
		<div class="notification_content">
			<div class="content">				
				<textarea class="contentwrite" name="notification_Content" rows=10 cols=40 placeholder="내용을 입력하세요"><%=dto.getNotification_Content() %></textarea>
			</div>
		</div>
        <div class="buttons">
            <button type="submit" class="buts">수정</button>            
            <a class="buts" href="<%=contextPath%>/shop_notification">목록보기</a>
        </div>
        </form>
    </div>        
</body>
</html>