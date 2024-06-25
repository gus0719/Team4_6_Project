function mInit() {
	document.getElementById("Log_phone").focus();
	if (document.getElementById("msg").value != null) {
		alert(document.getElementById("msg").value);
	}
}

function phoneFind(obj) {
	//document.getElementById("page").value = 'pageSms';
	//document.forms[0].action += page;
	//alert(document.getElementById("page").value);
	//alert(document.getElementById("user_cate").value);
	//alert(document.getElementById("Log_phone").value);
	
	//폰번호값을 히든값에 저장
	document.getElementById("user_phone").value = obj.value;

	//alert(document.getElementById("user_phone").value);

	document.forms[0].submit();
}

function controlSend(page) {
	//document.getElementById("page").value = page;
	document.forms[0].action += page;
	document.getElementsByTagName("form")[0].submit();
}