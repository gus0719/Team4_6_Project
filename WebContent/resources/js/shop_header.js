let menu;

function mMenu(){
	menu = document.getElementsByTagName("table");
	
	if(menu[0].style.display=='block'){
		menu[0].style.display = 'none';
	}else {
		menu[0].style.display='block';
	}
}

const mEnterSearch = keycode => {
	if(keycode == 13) {
		alert(keycode);
		mSearch();
	}
}
function mSearch(path){
	document.forms[0].action = path + "/search";
	if(document.getElementById("search").value!=""){	
		document.getElementsByTagName("form")[0].submit();
	}else{
		document.getElementsByTagName("input")[1].focus();
	}
}

function header(page){
	let vForm = document.getElementsByTagName("form");
	vForm[0].action += page;
	vForm[0].submit();
}

/* 상품명 자동 완성 ==============
*/
function autoCompleteName(contextPath, search) {
	let ul = document.getElementById("items");
	ul.innerHTML = "";
	ul.style.display = "block";
	let xhttp = new XMLHttpRequest();
	xhttp.onload = function() {
		//document.getElementById("itemLists").value = this.responseText;
		let itemLists = this.responseText.split(",");
		
		for(let idx = 0; idx < itemLists.length; idx++) {
			let li = document.createElement("li");
			let txt = document.createTextNode(itemLists[idx]);
			li.appendChild(txt);
			li.addEventListener('click', function () {
				document.getElementById("search").value = li.innerText;
				ul.style.display = "none";
			});
			ul.appendChild(li);
		}
		if(document.querySelector("#items > li").innerHTML.search(/없어요/igm) > 0){
			ul.style.display = "none";
		}
		if(search.value.length == 0) {
			ul.style.display = "none";
		}
	};
	xhttp.open("GET", contextPath + "/autoCompleteSearch?search=" + search.value);
	xhttp.send();
}