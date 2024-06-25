<%@page import="java.net.URLEncoder"%>
<%@page import="sample.spring.dto.ShopNotificationDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항</title>
<link rel="stylesheet" href="resources/css/shop_headerContainer.css">
<link rel="stylesheet" href="resources/css/shop_notification.css">
<jsp:useBean id="dao" class="sample.spring.dto.ShopNotificationDAO" />
<%
request.setCharacterEncoding("utf-8");
String notification_No = request.getParameter("notification_No");
ShopNotificationDTO dto = new ShopNotificationDTO();
dto = dao.bodSelect(Integer.parseInt(notification_No));
String contextPath = request.getContextPath();
%>

</head>
<body>
	<div id="container">
		<%@ include file="./shop_header.jsp"%>
	</div>
	<form name="frmsubmit" method="get">
	<div id="wrapper">
		<h1>공지사항</h1>
		<div class="blackstick"></div>
		<div class="notification_head">
			<input type = "hidden" name = "notification_No" value = "<%= dto.getNotification_No()%>">
			<Strong class="sort"><%=dto.getNotification_Subject()%></Strong>
			<input type = "hidden" name = "notification_Subject" value = "<%= dto.getNotification_Subject()%>">
			<div class="title_box">
				<p class="right">
					작성일자 :
					<%=dto.getNotification_Date()%></p>
					<input type = "hidden" name = "notification_date" value = "<%= dto.getNotification_Date()%>">
			</div>
		</div>
		<div class="notification_content">
			<div class="content">
				<%=dto.getNotification_Content()%>
			</div>
			<input type = "hidden" name = "notification_Content" value = "<%= dto.getNotification_Content()%>">
		</div>
		</form>
		<div class="buttons">			
			<span class="buts"
				onclick = "delUpd('<%=contextPath%>/shop_notification_update')">수정</span>
			<span class="buts"
				onclick = "delUpd('<%=contextPath%>/shop_notification_delete')">삭제</span>
			<a class="buts" href="<%=contextPath%>/shop_notification">목록보기</a>
		</div>
	</div>
	<script>
let path = document.forms[1];
function delUpd(category) {
	if(category=="<%=contextPath%>/shop_notification_delete"){		
		path.action = "<%=contextPath%>/shop_notification_delete";		
	}else{		
		path.action= "<%=contextPath%>/shop_notification_update";
	}
	document.forms[1].submit();
}
</script>

</body>
</html>