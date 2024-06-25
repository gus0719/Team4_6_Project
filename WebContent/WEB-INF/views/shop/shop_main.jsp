<%@ page import = "java.util.List" %>
<%@ page import = "java.util.ArrayList" %>
<%@ page import = "java.util.HashMap" %>
<%@ page import = "java.text.DecimalFormat" %>
<%@ page import = "sample.spring.dto.ShopInterestDTO" %>
<%@ page import = "sample.spring.dto.ShopProductDTO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"
	prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql"
	prefix="sql"%>
<!DOCTYPE html>
<%
request.setCharacterEncoding("utf-8");
String contextPath = request.getContextPath();
String prjPath = contextPath + "/shopFrame";
HashMap<String, ArrayList<ShopProductDTO>> mainMap = (HashMap<String, ArrayList<ShopProductDTO>>)request.getAttribute("mainMap");
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>메인페이지</title>
    <link rel = "stylesheet" href = "resources/css/shop_headerContainer.css">
    <link rel = "stylesheet" href = "resources/css/shop_main.css">
    <link rel = "stylesheet" href = "resources/css/shop_main_banner.css">
    <link rel = "stylesheet" href = "resources/css/shop_main_sideCF.css">
    
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200">
    <script src = "resources/js/shop_main_banner.js"></script>
    <script src = "resources/js/shop_main.js"></script>
    <%
    // 숫자 자릿수 구분 포멧
    DecimalFormat noFormat = new DecimalFormat();
    String msg = request.getParameter("msg");
    %>
</head>
<body id = "overflowX" onload = "showMsg('<%=msg%>'), bannerInit('<%=contextPath%>'), mainHeight()">
	  <!-- onscroll="footerShow()"> -->
<sql:setDataSource
	var="formatNo"
	driver="oracle.jdbc.driver.OracleDriver"
	url="jdbc:oracle:thin:@localhost:1521:XE"
	user="hr"
	password="hr"/>
	<%-- 헤더 영역 --%>
	<div id = "container">
	    <%@ include file = "shop_header.jsp" %>
    </div>
    <%--상품 클릭시 상품번호에 값 전달 후 action 페이지에 전송--%>
    <form method = "get" action = "<%=contextPath%>/explain">
    	<%-- 컨트롤러 페이지 이동 --%>
    	<input type = "hidden" name = "page" id = "pagePd" value = "pageExplain">
    	<input type = "hidden" name = "category" id = "category" value = "">
    	<input type = "hidden" name = "brand" id = "brand" value = "">
    	<%-- 상품 선택 시 전달할 상품코드 --%>
		<input type = "hidden" name = "product_code" id = "product_code" value = "">
        <main id = "wrapper">
        	<%-- 메인 배너 --%>
            <div id = "main_banner">
                <img src = "">
                <span class="material-symbols-outlined" id = "prevBtn" onclick = "prevBtn()">chevron_left</span>
                <span class="material-symbols-outlined" id = "nextBtn" onclick = "nextBtn()">chevron_right</span>
                <div id = "banner_dots"></div>
            </div>
            <section id = "contents">
                <div id = "content_popular">
                    <p class = "title">랜덤상품</p>
                    <ul id = "popular_items">
                    	<%-- // 랜덤 상품 출력 -> 스크립트릿
	                    	List<ShopProductDTO> randomList = mainMap.get("randomList");
	                    	for(int idx = 0; idx < randomList.size(); idx++){
	                    		ShopProductDTO randomPD = randomList.get(idx);
	                    		
	                    		String mainImgText = randomPD.getProduct_img(); // 문자열로 변환한 배열
	                    		
	                     		String[] mainImgArr = mainImgText.substring( // 문자열을 배열로 다시 변환
	                     				mainImgText.indexOf("[") + 1, mainImgText.lastIndexOf("]")
	                     		).split(",");
	                    --%> 
	                    	<%--
	                    	<li onclick = "goExplain('<%=contextPath%>', <%=randomPD.getProduct_code()%>)">
	                    	<img src = "<%=prjPath%>/img/<%=mainImgArr[0]%>">
	                            <span class = "items_name"><%= randomPD.getProduct_name() %></span>
	                            <span class = "items_price"><%= noFormat.format(randomPD.getProduct_price())%>원</span>
	                        </li>
	                        --%>
                    	<%--	
                    	}
                        --%>
                        <%-- JSTL 방식 --%>
                    	<c:set var="randomList" value="${mainMap.get('randomList')}"/>
                    	<c:forEach var="randomPD" items="${randomList}">
	                    	<c:forTokens var="imgArr" items= "${randomPD.product_img}" delims=",[]" varStatus="status">
	                    		<c:if test="${status.index eq 0}">
	                    			<c:set var="img" value="${imgArr}"/>
	                    		</c:if>
	                    	</c:forTokens>
	                    	<li onclick = "goExplain('<%=contextPath%>', ${randomPD.product_code})">
	                            <img src = "resources/img${img}">
	                            <span class = "items_name">${randomPD.product_name}</span>
	                            <sql:query
	                            	dataSource="${formatNo}"
	                            	var="randomResult"
	                            	sql="select TO_CHAR('${randomPD.product_price}', '999,999,999') from dual"
	                            	/>
	                            <c:forEach var = "rdPriceRow" items="${randomResult.rowsByIndex}">
	                            	<c:forEach var = "rdPrice" items = "${rdPriceRow}">
		                            	<span class = "items_price">${rdPrice}원</span>
		                            </c:forEach>
	                            </c:forEach>
	                        </li>
	                    </c:forEach>
                    </ul>
                </div>
                <div id = "content_top5">
                    <p class = "title">구매율 TOP 5</p>
                    <ul id = "top5_items">
                    <%--
                    // 구매율이 높은 상품 위주로 내림차순
                    List<ShopProductDTO> top5Products = mainMap.get("top5Products");
                    int listLen = (top5Products.size() < 5) ? 
                    		top5Products.size() : 5; // 구매 기록이 5 미만이면 사이즈 만큼 길이 지정. 5 이상이면 
                    for(int idx = 0; idx < listLen; idx++){
                 		ShopProductDTO top5 = top5Products.get(idx);
                 		// 이미지 문자열 배열로 split
                 		String mainImgText = top5.getProduct_img();
                 		String[] mainImgArr = mainImgText.substring(
                 				mainImgText.indexOf("[") + 1, mainImgText.lastIndexOf("]")
                 		).split(",");
                    --%>
                    	<%--
                    	<li onclick = "goExplain('<%=contextPath%>', <%=top5.getProduct_code()%>)">
                            <img src = "<%=prjPath%>/img<%=mainImgArr[0]%>">
                            <span class = "items_name"><%= top5.getProduct_name() %></span>
                            <span class = "items_price"><%= noFormat.format(top5.getProduct_price()) %>원</span>
                        </li>
						--%>
                    <%--
                    }
                    --%>
                    <%-- 길이 지정 --%>
                    <c:set var="top5Products" value="${mainMap.get('top5Products')}"/>
                    <c:if test="${top5Products.size() > 5}">
                		<c:set var="len" value="4"/>
                    </c:if>
                    <%-- 가격 자리수 구분 필요 --%>
                    <c:forEach var="top5" items="${top5Products}" varStatus="status" end="${len}">
                    	<c:forTokens var="imgArr" items="${top5.product_img}" delims=",[]" varStatus="imgStatus">
                    		<c:if test="${imgStatus.index eq 0}">
                    			<c:set var="img" value="${imgArr}"/>
                    		</c:if>
                    	</c:forTokens>
                    	<li onclick = "goExplain('<%=contextPath%>', ${top5.product_code})">
                            <img src = "resources/img${img}">
                            <span class = "items_name">${top5.product_name}</span>
                            <sql:query
                            	dataSource="${formatNo}"
                            	var="top5Result"
                            	sql="select TO_CHAR(${top5.product_price}, '999,999,999') from dual"
                            	/>
                            <c:forEach var="top5PriceRow" items="${top5Result.rowsByIndex}">
                            	<c:forEach var="top5Price" items="${top5PriceRow}">
                            		<span class = "items_price">${top5Price}원</span>
                            	</c:forEach>
                            </c:forEach>
                        </li>
                    </c:forEach>
                    </ul>
                </div>
                <%--
                /* 유저의 계정이 있고 관심상품이 있을때 출력 =========================================================================
                */
                List<ShopProductDTO> interests = mainMap.get("interests");
                if(session.getAttribute("user_id") != null && interests.size() != 0){
                --%>
                <c:set var="interests" value="${mainMap.get('interests')}"/>
                <c:if test="${!empty sessionScope.user_id && interests.size() != 0}">
	                <div id = "interestItems">
	                	<p class = "title">관심상품</p>
	                	<ul id = "interest">
	                		<c:forEach var="interestItem" items="${interests}">
		                		<li onclick = "goExplain('<%=contextPath%>', ${interestItem.product_code})">
		                			<c:forTokens var="imgArr" items="${interestItem.product_img}" delims=",[]" varStatus="interestStatus">
		                				<c:if test="${interestStatus.index eq 0}">
		                					<c:set var="img" value="${imgArr}"/>
		                				</c:if>
		                			</c:forTokens>
		                			<img src = "resources/img${img}">
		                            <span class = "items_name">${interestItem.product_name}</span>
		                            <sql:query
		                            	dataSource="${formatNo}"
		                            	var="result"
		                            	sql="select TO_CHAR(${interestItem.product_price}, '999,999,999') from dual"
		                            	/>
		                            <c:forEach var="resultPrice" items="${result.rowsByIndex}">
		                            	<c:forEach var="priceRow" items="${resultPrice}">
		                            		<span class = "items_price">${priceRow}원</span>
		                            	</c:forEach>
		                            </c:forEach>
		                		</li>
	                		</c:forEach>
	                	<%--
	                	for(int idx = 0; idx < interests.size(); idx++){
	                		// 관심상품 출력
	                		ShopProductDTO product = interests.get(idx);
	                		// 이미지 문자열 배열로 split
	                 		String mainImgText = product.getProduct_img();
	                 		String[] mainImgArr = mainImgText.substring(
	                 				mainImgText.indexOf("[") + 1, mainImgText.lastIndexOf("]")
	                 		).split(",");
	                	--%>
	                	<%--
	                		<li onclick = "goExplain('<%=contextPath%>', <%=interests.get(idx).getProduct_code()%>)">
	                			<img src = "<%=prjPath%>/img<%= mainImgArr[0]%>">
	                            <span class = "items_name"><%= interests.get(idx).getProduct_name()%></span>
	                            <span class = "items_price"><%= noFormat.format(product.getProduct_price())%>원</span>
	                		</li>
	                	--%>
	                	<%--
	                	}
	                	--%>
	                	</ul>
	                </div>
                </c:if>
                <%--
                }
                --%>
                <div id = "lessThanPrice">
                	<p class = "title">10만원 이하 상품</p>
                	<ul id = "oneHundThou">
                		<c:forEach var="lessThanItem" items="${mainMap.get('lessThanPrice')}">
                			<li onclick = "goExplain('<%=contextPath%>', ${lessThanItem.product_code})">
                				<c:forTokens var="imgArr" items="${lessThanItem.product_img}" delims=",[]" varStatus="lessThanStatus">
                					<c:if test="${lessThanStatus.index eq 0}">
                						<c:set var="img" value="${imgArr}"/>
	                				</c:if>
	                			</c:forTokens>
               					<img src = "resources/img${img}">
	                            <span class = "items_name">${lessThanItem.product_name}</span>
	                            <sql:query
	                            	dataSource="${formatNo}"
	                            	var="result"
	                            	sql="select TO_CHAR(${lessThanItem.product_price}, '999,999,999') from dual"
	                            	/>
	                            <c:forEach var="resultPrice" items="${result.rowsByIndex}">
	                            	<c:forEach var="priceRow" items="${resultPrice}">
	                            		<span class = "items_price">${priceRow}원</span>
	                            	</c:forEach>
	                            </c:forEach>
	                		</li>
                		</c:forEach>
                		<%--
                		// 매개변수에 가격을 정수로 할당하면 그 이하의 상품들 반환
                		List<ShopProductDTO> lessThanPrice = mainMap.get("lessThanPrice");
                		for(int idx = 0; idx < 15; idx++){ // 15개까지 출
                			// 이미지문자열 배열로 쪼개기
                			String lessThanImgText = lessThanPrice.get(idx).getProduct_img();
                			String[] imgArr = lessThanImgText.substring(
                					lessThanImgText.indexOf("[")+1, lessThanImgText.lastIndexOf("]")
                			).split(",");
                		--%>
                		<%--
                		<li onclick = "goExplain('<%=contextPath%>', <%=lessThanPrice.get(idx).getProduct_code()%>)">
                			<img src = "<%=prjPath%>/img<%=imgArr[0]%>">
                            <span class = "items_name"><%= lessThanPrice.get(idx).getProduct_name() %></span>
                            <span class = "items_price"><%= noFormat.format(lessThanPrice.get(idx).getProduct_price())%>원</span>
                		</li>
                		--%>
                		<%--
                		}
                		--%>
                	</ul>
                </div>
            </section>
            <aside id = "sideCF">
                <div id = "advertisement">
                    <p>ADVERTISEMENT</p>
                    <img src = "resources/img/bag/docs2.jpg">
                    <img src = "resources/img/pants/ck_loseFit_1.jpg">
                    <img src = "resources/img/phone/galaxy_zfold5_iceblue.jpg">
                    <img src = "resources/img/shoe/airforce_0.png">
                </div>
            </aside>
        </main>
    </form>
	<footer id="footContainer">
		<%@ include file = "./shop_footer.jsp"%>
	</footer>
</body>
</html>