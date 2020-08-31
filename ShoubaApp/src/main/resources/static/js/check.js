	// 非空验证/
function isKong(value,str){
	if(value==""){
		layer.msg(str);
		return false;
	}
	return value;
}

// 手机号验证/]
function telcheck(value){
	var tel = /^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$/;
	if(!tel.test(value)){
		layer.msg('手机号格式有误');
		return false
	}
	return value;
}
// 身份证号码正则/
function Idcard(value){
	let _IDRe18 = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;	
	// 校验身份证：
	if(!_IDRe18.test(value)) {
		layer.msg('请填写正确的身份证号');
		return false
	}
	return value;
}
// 银行卡号
 function regTest(value){
 var regExp = /^([1-9]{1})(\d{15}|\d{18})$/; 
 if(!regExp.test(value) ) {
 	layer.msg('请填写正确的银行卡号');
 	return false
 }
 	return value;
 } 
// 金额/
function moneyreg(value){
	var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
	 if(!isNum.test(value)) {	
		 layer.msg('金额为任意正整数，正小数（小数位不超过2位）');
	   return false;	
	}
		return value;
}
	
	