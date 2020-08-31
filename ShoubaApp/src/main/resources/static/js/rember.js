window.onload = function(){
	var oForm = document.getElementById('myForm');
	var oUser = document.getElementById('login_name');
	var oPswd = document.getElementById('login_pwd');
	var oRemember = document.getElementById('rember');
	//页面初始化时，如果帐号密码cookie存在则填充
	if (getCookie('username') && getCookie('password')) {
		oUser.value = getCookie('username');
		oPswd.value = getCookie('password');
		oRemember.checked = true;
		 $('.rember-txt').addClass("mark");
		
	}
	//复选框勾选状态发生改变时，如果未勾选则清除cookie
	oRemember.onchange = function() {
		if (!this.checked) {
			delCookie('username');
			delCookie('password');
			 $('.rember-txt').removeClass("mark");
		}
	};
	//表单提交事件触发时，如果复选框是勾选状态则保存cookie
	oForm.onclick = function() {
		if (oRemember.checked) {
			setCookie('username', oUser.value, 7); //保存帐号到cookie，有效期7天
			setCookie('password', oPswd.value, 7); //保存密码到cookie，有效期7天
		}
	};
};
//设置cookie
function setCookie(name, value, day) {
	var date = new Date();
	date.setDate(date.getDate() + day);
	document.cookie = name + '=' + value + ';expires=' + date;
};
//获取cookie
function getCookie(name) {
	var reg = RegExp(name + '=([^;]+)');
	var arr = document.cookie.match(reg);
	if (arr) {
		return arr[1];
	} else {
		return '';
	}
};
//删除cookie
function delCookie(name) {
	setCookie(name, null, -1);
};