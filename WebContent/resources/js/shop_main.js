var product_code;

function showMsg(msg){
	if(msg != "null" && msg.length != 0) {
		alert(msg);
	}
}

function mainHeight() {
	let popular = document.getElementById("popular_items").offsetHeight;
	let top5 = document.getElementById("top5_items").offsetHeight;
	let interest = (document.getElementById("interest") != null) ? 
					document.getElementById("interest").offsetHeight : 0;
	let oneHundThou = document.getElementById("oneHundThou").offsetHeight;
	
	let mHeight = popular + top5 + interest + oneHundThou;
	document.getElementById("sideCF").style.height = `${mHeight + 500}px`;
	document.getElementById("contents").style.height = `${mHeight}px`;
}

function goExplain(ctrlPath, pdCode){
	document.getElementById("pagePd").value = "pageExplain";
	product_code = document.getElementById("product_code");
	product_code.value = pdCode;
	//document.forms[1].action = ctrlPath + "/CtrlPd.do";
	document.forms[1].submit();
}