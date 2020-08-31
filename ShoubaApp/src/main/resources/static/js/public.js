	MD5Key='smpz1H26I6lhUhUgZFC1UgNpaee0WA44SCOTNEGAVkeSltyT3V8Xbb84';
	//MD5加密方法
	function md5Method(arys) {
		var timestamp = (new Date()).getTime();
		//（1）排序
		//先用Object内置类的keys方法获取要排序对象的属性名数组，再利用Array的sort方法进行排序
		var newkey = Object.keys(arys).sort();
		var newObj = ''; //创建一个新的对象，用于存放排好序的键值对
		var requestData = "{";
		for (var i = 0; i < newkey.length; i++) {
			//遍历newkey数组
			newObj += [newkey[i]] + '=' + arys[newkey[i]] + '&';
			requestData += "\"" + [newkey[i]] + "\":\"" + arys[newkey[i]] + "\"," //拼接请求参数
		}
		//（2）排序结果
		var sortResult = newObj.substring(0, newObj.length - 1) + "&key=" + MD5Key;
		//（3）获取sign
		var sign = CryptoJS.MD5(sortResult).toString().toUpperCase();
		//（4）拼接请求参数处理并返回
		// requestData += "\"sign\":\"" + sign + "\"}" //拼接请求参数
		return sign;
	}

	//RSA加密方法
	function rsaMethod(arys) {
		//（1）拼接请求参数处理
		var newkey = Object.keys(arys).sort();
		var requestData = "{";
		for (var i = 0; i < newkey.length; i++) {
			requestData += "\"" + [newkey[i]] + "\":\"" + arys[newkey[i]] + "\"," //拼接请求参数
		}
		requestData = requestData.substring(0, requestData.length - 1) + "}"; //拼接请求参数
		//（2）加密
		var encrypt = new JSEncrypt();
		encrypt.setPublicKey(publicKey);
		var encryptData = encrypt.encryptLong(requestData);
		return encryptData;
	}

	function uploadFileAndParams(url,formData){
		$.ajax({
			type:'post',
			dataType:'json',
			url:url,
			cache : false,
			data : formData,
			processData : false, //data值是FormData对象，不需要对数据做处理。
			contentType : false,//设置contentType值，因为是由<form>表单构造的FormData对象，且已经声明了属性enctype="multipart/form-data"，所以这里设置为false。
			success : function(result) {
				if(result.success==true){
					layer.msg(result.msg, {
						icon: 1,//提示的样式
						time: 2000, //2秒关闭（如果不配置，默认是3秒）//设置后不需要自己写定时关闭了，单位是毫秒
						end:function(){
							// window.location.href = "/login";
						}
					});
				}else{
					layer.msg(result.msg, {
						icon: 2,//提示的样式
						time: 2000, //2秒关闭（如果不配置，默认是3秒）//设置后不需要自己写定时关闭了，单位是毫秒
						end:function(){

						}
					});
				}
			},
			error : function(result){
			}
		});
	}