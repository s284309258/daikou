var lang=""

	//是否存在cookie
	if(document.cookie.indexOf("talk")==-1){
		lang="cn";
	}else{
		// 获取语言
		lang=getCookie(talk);
	}
	//console.log(lang);
	
var getCode="获取验证码",lateReGetCode="s后重新获取",pleaEnter11Tel="请输入11位手机号",TelNotPattern="手机号格式不对";
pleaEnter620Pwd="由6到20位由字母和数字组成的密码"

if(lang=="en"){
	getCode="Get Code";
	lateReGetCode="s Later get";
	pleaEnter11Tel="Please enter 11 mobile phone numbers";
	TelNotPattern="A password consisting of 6-20 bits of letters and numbers";
	pleaEnter620Pwd="Please enter a username (6-20 bits)"
	
	}
console.log()
//获取验证码
function getcode(obj){
	$(obj).attr("disabled", "disabled")
	var timer = null,
	num = 60;
	timer = setInterval(function() {
		num--;
	//	console.log(num)
		if(num < 0) {
			$(obj).val(getCode);
			$(obj).text(getCode);
			$(obj)[0].removeAttribute("disabled");
			clearInterval(timer);
		} else {
			//			console.log(num);
			$(obj).val(num + lateReGetCode);
			$(obj).text(num + lateReGetCode);
			}
		}, 1000)
}




//验证js

//验证是否为空
function isKong(value,str){
	if(value==""){
		xtip.msg(str, { icon: 'e'});
		return false;
	}
	return value;
}


//手机号验证
function telCheck(value){
	console.log(value)
	var regex =  /^[1][3,4,5,6,7,8,9][0-9]{9}$/
	if(value==""){
		xtip.msg($(".tel").attr("placeholder"), { icon: 'e'});
		return false;
	}
	if(value.length!=11){
		xtip.msg(pleaEnter11Tel, { icon: 'e'});
		return false;
	}
	if(!regex.test(value)){
		xtip.msg(TelNotPattern, { icon: 'e'});
			return false;
		}
	return value;
}


//email格式是否正确
 function isEmail(value) {
    if(!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/g.test(value)){
    	layer.msg("邮箱格式不正确", { icon: 2,time: 2000});
    	return false;
    }
 	return value;
}
 
 //请输入6到20位由字母和数字组成的密码
function CheckPw(value){
		if(value==""){
			layer.msg($(".pwd").attr("placeholder"), { icon: 2,time: 2000});
			return false;
		}
		var regg = /^(?!\D+$)(?![^a-zA-Z]+$)\S{6,20}$/;
		if(!regg.test(value)) {
			layer.msg(pleaEnter620Pwd, { icon: 2,time: 2000});
			return false;
		}
		return value;
	}

// 6位密码数字不能重复
function Check_payPwd(value){
		if(value.length !=6){
			layer.msg("长度不能操过6位");
			return false;
		}	
		var reg1=/([0-9])\1{2}/
		var reg2=/([0-9])\1{3}/
		var reg3=/([0-9])\1{4}/
		var reg4=/([0-9])\1{5}/
		var reg5=/([0-9])\1{6}/
		if(reg1.test(value) || reg2.test(value) || reg3.test(value) || reg4.test(value) || reg5.test(value)){
			layer.msg("6位密码数字不能重复");
			return false;
		}
		return value;
	}

//身份证号码格式是否正确
function  isIdCard(value) {
	if(value==""){
		layer.msg("请输入身份证号", { icon: 2,time: 2000});
		return false;
	}
    if(!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/g.test(value)){
    	layer.msg("身份证号格式不正确", { icon: 2,time: 2000});
    	return false;
    }
    return value;
}
//银行卡号是否正确
function  isCard(value) {
	if(value==""){
		layer.msg("请输入银行卡号", { icon: 2,time: 2000});
		return false;
	}
	var reg=/^([1-9]{1})(\d{14}|\d{18})$/;
    if(!reg.test(value)){
    	layer.msg("银行卡号格式不正确", { icon: 2,time: 2000});
    	return false;
    }
    return value;
}








