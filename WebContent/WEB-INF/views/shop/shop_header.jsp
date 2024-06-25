<%@ page import = "sample.spring.dto.ShopUserDTO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<%
String headerContext = request.getContextPath();
String prj = request.getContextPath()+"/shopFrame";
%>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Document</title>
<link rel = "stylesheet" href = "resources/css/shop_header.css">
<script src = "resources/js/shop_header.js"></script>
<script src = "resources/js/shop_search.js"></script>
<jsp:useBean id = "userDAO" class ="sample.spring.dto.ShopUserDAO"/>
<%
String user_id = (String)session.getAttribute("user_id");
ShopUserDTO userDTO = userDAO.myPageChk(user_id);
%>
</head>
<body onload = "headerInit()">
	<form method = "get" action="<%=headerContext%>">
	<%--검색어 입력 시 연관성있는 상품명 생성.
	onchange 이벤트로 리스트 생성--%><input type = "hidden" id = "itemLists">
	<div class="header">
		<div class="menu" onclick="mMenu()"></div>
		<div class="select">
			<table>
				<tr>
					<td onclick = "location.href = '<%=headerContext%>/category?category=outer'"><img src="resources/img/icon/coat.png">겉옷</td>
					<td onclick = "location.href = '<%=headerContext%>/category?category=shirt'"><img src="resources/img/icon/clothes.png">상의</td>
					<td onclick = "location.href = '<%=headerContext%>/category?category=pants'"><img src="resources/img/icon/pants.png">하의</td>
					<td onclick = "location.href = '<%=headerContext%>/category?category=shoe'"><img src="resources/img/icon/shoe.png">신발</td>
					<td onclick = "location.href = '<%=headerContext%>/category?category=bag'"><img src="resources/img/icon/bag.png">가방</td>
					<td onclick = "location.href = '<%=headerContext%>/category?category=phone'"><img src="resources/img/icon/phone.png">휴대폰</td>
					<td onclick = "location.href = '<%=headerContext%>/category?category=watch'"><img src="resources/img/icon/watch.png">시계</td>
				</tr>
			</table>
		</div>
		<div class="logo" onclick = "header('/index')"></div>
		<!-- 
		<input type="text" name = "search" id = "search" placeholder="검색어를 입력하세요."
		onkeydown = "mEnterSearch(event.keyCode)" onkeyup = "autoCompleteName('<%=headerContext%>', this)">
		<ul id = "items"></ul>
		<img src="<%=prj%>/img/icon/search.png" id = "searchIcon" onclick = "mSearch()">
		 -->
		<fieldset>
			<input type="text" autocomplete = "off" name = "search" id = "search" placeholder="검색어를 입력하세요."
			onkeydown = "mEnterSearch(event.keyCode)" onkeyup = "autoCompleteName('<%=headerContext%>', this)">
			<img src="resources/img/icon/search.png" id = "searchIcon" onclick = "mSearch('<%=headerContext%>')">
			<ul id = "items"></ul>
		</fieldset>
		<%
		if (user_id != null) {
		%>
		<div class="logout" onclick = "header('/frmLogout')">로그아웃</div>
		<div class="myPage" onclick = "header('/myPage')">내 정보</div>
		<div class="bag" onclick = "header('/cartList')">장바구니</div>
		<%
			if(userDTO.getUser_rank().contains("판매자")){
			%>
			<div class = "seller" onclick = "header('/pageRaiseProduct')">상품등록</div>
			<%
			}
		} else {
		%>
		<div class="login" onclick = "header('/login')">로그인</div>
		<%
		}
		%>
		<div id = "notification" onclick = "header('/shop_notification')">공지사항</div>
	</div>
	</form>
</body>
</html>