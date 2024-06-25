<%@ page import="java.util.List"%>
<%@ page import="sample.spring.dto.ShopProductDTO"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"
	prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql"
	prefix="sql"%>
<!DOCTYPE html>
<%
request.setCharacterEncoding("UTF-8");
String contextPath = request.getContextPath();
HashMap<String, Object> categoryMap = (HashMap<String, Object>) request.getAttribute("categoryMap");
%>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>카테고리</title>
<link rel="stylesheet" href="resources/css/shop_headerContainer.css">
<link rel="stylesheet" href="resources/css/shop_category.css">
<script src="resources/js/shop_category.js"></script>
<%
// 카테고리 =================
String product_category = request.getParameter("category");
//브랜드 =======
String product_brand = request.getParameter("brand");

/* List<String> brands = (List<String>) categoryMap.get("brands"); // 브랜드 출력, distinct 브랜드
List<ShopProductDTO> items = (List<ShopProductDTO>) categoryMap.get("items"); // 카테고리와 일치하는 상품들 반환
// 숫자 형식 변환
DecimalFormat noFormat = new DecimalFormat(); */
%>
</head>
<body>
	<sql:setDataSource
		var="formatNo"
		driver="oracle.jdbc.driver.OracleDriver"
		url="jdbc:oracle:thin:@localhost:1521:XE"
		user="hr"
		password="hr"
		/>
	<div id="container">
		<%@ include file="./shop_header.jsp"%>
	</div>
	<div id="wrapper">
		<div class="categoryTop">
			<div class="cateLocation">
				<c:forEach
					var="brand"
					items="${categoryMap.get('brands')}"
					>
					<div class="brand"
					onclick="selectBrand('<%=contextPath%>', '<%=product_category%>', '${brand}')">${brand}</div>
				</c:forEach>
				<%--
				for (int i = 0; i < brands.size(); i++) {
				--%>
				<%--
				<div class="brand"
					onclick="selectBrand('<%=contextPath%>', '<%=product_category%>', '<%=brands.get(i)%>')"><%=brands.get(i)%></div>
				}
				--%>
			</div>
		</div>
		<form method="get">
			<input type="hidden" name="product_code" id="product_code">
			<div class="product">
				<ul id="cart_items">
					<%-- 상품 출력 ================================================== --%>
					<c:forEach var="item" items="${categoryMap.get('items')}">
						<c:forTokens var="imgArr" items= "${item.product_img}" delims=",[]" varStatus="status">
   	                		<c:if test="${status.index eq 0}">
       	            			<c:set var="img" value="${imgArr}"/>
           	        		</c:if>
               	    	</c:forTokens>
						<li	onclick="goWithProductCode('<%=contextPath%>', '${item.product_code}')">
							<img src="resources/img${img}">
							<span class="items_name">${item.product_name}</span>
							<sql:query
	                           	dataSource="${formatNo}"
	                           	var="result"
	                           	sql="select TO_CHAR('${item.product_price}', '999,999,999') from dual"
	                           	/>
	                        <c:forEach var="resultRow" items="${result.rowsByIndex}">
	                        	<c:forEach var="price" items="${resultRow}">
									<span class="items_price">${price}원</span>
								</c:forEach>
							</c:forEach>
						</li>
					</c:forEach>
					<%--
					for (int idx = 0; idx < items.size(); idx++) {
						String imgArrText = items.get(idx).getProduct_img();
						String[] imgArr = imgArrText.substring(imgArrText.indexOf("[") + 1, imgArrText.lastIndexOf("]")).split(","); // 배열 이미지 추출
					--%>
					<%--
					<li	onclick="goWithProductCode('<%=contextPath%>', '<%=items.get(idx).getProduct_code()%>')">
						<img src="<%=prjPath%>/img<%=imgArr[0]%>">
						<span class="items_name"><%=items.get(idx).getProduct_name()%></span>
						<span class="items_price"><%=noFormat.format(items.get(idx).getProduct_price()) + "원"%></span>
					</li>
					--%>
					<%--
					}
					--%>
				</ul>
			</div>
		</form>
	</div>
</body>
</html>