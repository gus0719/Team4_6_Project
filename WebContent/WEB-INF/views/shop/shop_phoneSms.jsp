<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    	<%
	request.setCharacterEncoding("utf-8");
	String cate = "phone";
	String msg = request.getParameter("msg");
	String contextPath = request.getContextPath();

	//css
	String cssPath = contextPath + "/resources/css/shop_idfind.css";
	//javaScript
	String javaScriptPath = contextPath + "/resources/js/shop_phoneSms.js";
	//img
	String imgPath1 = contextPath + "/resources/img/icon/search.png";
	String imgPath2 = contextPath + "/resources/img/logo.png";
	%>
<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>인증 페이지</title>
	<link rel="stylesheet" href="<%=cssPath%>">
	<script src="<%=javaScriptPath%>"></script>

</head>
<body onLoad="mInit()">
	<div id="wrapper">
		<form method = "post" action = "<%=contextPath%>/pageSms">
		<input type="hidden"  name="user_phone" id="user_phone" value="">
		<input type="hidden" name="user_cate" id="user_cate" value="<%=cate%>">
		<input type="hidden" value="pageSms" name="page" id="page">
		<input type="hidden" value="<%= (msg != null) ? msg : "휴대폰 인증 페이지입니다" %>" id="msg">
			<div class="header">
				<!--<div class="logo"><img src = "./../img/logo.png"></div>-->
				<div class="nav1">
					<p>고객센터</p>
					<p>마이페이지</p>
					<p>관심</p>
					<p>알림</p>
					<p>로그인</p>
				</div>
				<div class="nav2">
					<p onclick = "location.href = '<%=contextPath%>/mainPage'">HOME</p>
					<p>STYLE</p>
					<p>SHOP</p>
					<img src="<%=imgPath1%>">
				</div>
			</div>
			<div class="main">
				<div class="logo" onclick = "location.href = '<%=contextPath%>/mainPage'"><img src = "<%=imgPath2%>"><br>휴대폰 인증</div>
				<div class="login">
				<!--  휴대폰번호  -->
					<p>휴대폰번호</p>
					<input type="text" id="Log_phone" value = "" placeholder="휴대폰 번호 예 ) 01012345678">
				<!--   버튼 -->
					<input type = "button" value = "문자 발송하기" onclick = "phoneFind(Log_phone)">
				</div>
			</div>
		</form>
	</div>
</body>
</html>