<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="sample.spring.dto.ShopUserDAO" %>
<%@ page import="sample.spring.dto.ShopUserDTO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 
    prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" 
    prefix="sql" %> 
 
<!-- SQL DB 연동 -->
<sql:setDataSource var="SQLdata" 
	url="jdbc:oracle:thin:@localhost:1521:XE"
	driver="oracle.jdbc.driver.OracleDriver" 
	user="hr" password="hr"/>

<!-- Shop_User  -->
<sql:query var="sql" dataSource="${SQLdata}">
	select * from shop_user where user_id = ?
	<sql:param value="${user_id}"/>
</sql:query>
  
  <%
  ShopUserDAO dao = new ShopUserDAO();
  ShopUserDTO dto = new ShopUserDTO();
  
  request.setCharacterEncoding("UTF-8");
  String contextPath = request.getContextPath();
  String prjPath = contextPath + "/shopFrame";
  ArrayList<Object> myPage = (ArrayList<Object>)request.getSession().getAttribute("myPage");
  %>
<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
	<link rel="stylesheet" href="resources/css/shop_myPage.css">
	<script src="resources/js/shop_myPage.js"></script>
</head>
<body onLoad="mInit()">

<%--
세션에 저장된 user_id의 속성값을 받아서 고객에 대한 정보 출력 알고리즘 작성
--%>
	<div id="wrapper">
	
	<c:forEach var="row" begin="0" end="1" items="${sql.rowsByIndex }">
	
		<c:set var="rank">
			${row[6] = row[6].split(1)}<br>
		</c:set>
		
		<form method="GET"  action="<%=contextPath%>/">
		<input type="hidden" id="checkbox" name="checkbox" value="${row[6][1]}">
		<input type="hidden" id="column" name="column" value="">
		<input type="hidden" id="up_column" name="up_column" value="">
		<input type="hidden" id="userDel" name="userDel" value="${userDel}">
		<input type="hidden" id="check" name="check" value="${row[6][1]}">
		<input type="hidden" name="page" value="pageMy">
		<input type="hidden" id="user_id" name="user_id" value="">
		
			<div class="header">
				<div class="logo"><img src="resources/img/logo.png" onclick="Index()"></div>
				<div class="nav1">
					<p>고객센터</p>
					<p>마이페이지</p>
					<p>관심</p>
					<p>알림</p>
					<p>로그인</p>
				</div>
				<div class="nav2">
					<p>HOME</p>
					<p onclick = "location.href = '<%=contextPath%>/buyList'">STYLE</p>
					<p>SHOP</p>
					<img src="resources/img/icon/search.png">
				</div>
			</div>
			<div class="main">
				<h1>로그인 정보<br></h1>
				
				<h3>내 계정   유형 : ${row[6][0]}</h3>
				<!-- 아이디 및 비밀번호 -->
				<p>아이디</p> <!-- 변경 X -->
				<div><input type="text" id="id" class="txt" value="${row[0]}" disabled></input><%--<input type="button" value="변경" onclick="mChange(1)">--%></div>

				<p>비밀번호</p>
				<div><input type="text" class="txt" value="${row[1]}" disabled><input type="button" value="변경" onclick="mChange(2)"></div>

				<h3>개인 정보</h3>
				<!-- 이름 , 이메일 , 휴대폰 , 주소 -->
				<p>이름</p>
				<div><input type="text" class="txt" value="${row[2]}" disabled><input type="button" value="변경" onclick="mChange(3)"></div>
				
				<p>이메일</p>
				<div><input type="text" class="txt" value="${row[4]}" disabled><input type="button" value="변경" onclick="mChange(4)"></div>

				<p>휴대폰</p>
				<div><input type="text" class="txt" value="${row[3]}" disabled><input type="button" value="변경" onclick="mChange(5)"></div>

				<p>주소</p>
				<div><input type="text" class="txt" value="${row[5]}" disabled><input type="button" value="변경" onclick="mChange(6)"></div>

				<h4>광고성 정보 동의</h4>
				<!-- 광고의 문자 메세지 동의와 이메일 동의를 합침 -->
				<div class="chk1">문자 메세지 및 이메일
					<label for="msgtrue" onclick="check('true')">수신동의</label>
					<input type="radio" id="msgtrue" name="msg" onclick="check('true')">
					<label for="msgfalse" onclick="check('false')">수신거부</label>
					<input type="radio" id="msgfalse" name="msg" onclick="check('false')">
				</div>
				<br>
				<span onclick="userDel()">회원탈퇴</span>
			</div>
		</form>
	</c:forEach>
	</div>
</body>
</html>