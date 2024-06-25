var sendImg;
var sendSize;
var sendCode;
var sendCount;
var sendName;

function mInit(path){
	document.forms[1].action = path;
	sendImg = document.getElementById("sendImg");
	sendSize = document.getElementById("sendSize");
	sendCode = document.getElementById("sendCode");
	sendCount = document.getElementById("sendCount");
	sendName = document.getElementById("sendName");
}
function buyNow(pdCode, idx, pdImg){
	sendImg.value = pdImg;
	sendSize.value = "270";
	sendCode.value = pdCode;
	sendCount.value = document.getElementsByClassName("items_name")[idx].innerHTML.split(/x/ig)[1].trim();
	sendName.value = document.getElementsByClassName("items_price")[idx].innerHTML;
	//document.getElementById("pagePd").value = "pageBuy";
	//document.forms[1].action = "./shop_buy.jsp";
	document.forms[1].action += "/buy";
	document.forms[1].submit();
}
function goExplain(pdCode, msg){
	if(msg.search(/삭제/ig) >= 0){
		alert("삭제된 상품입니다.");
		return;
	}
	sendCode.value = pdCode;
	sendCount.value = 1;
	//document.getElementById("pagePd").value = "pageExplain";
	document.forms[1].action += "/explain";
	document.forms[1].submit();
}
function deleteItem(path, pdCode){
	sendCode.value = pdCode;
	document.forms[1].action += "/delCart";
	document.forms[1].submit();
}