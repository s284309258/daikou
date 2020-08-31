			 $(function(){
				 var tel_reg = /^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$/;
				 var IDcard = /(^\d{15}$)|(^\d{17}([0-9]|X)$)/
				 $("#verCodeBtn").on("click", function(){
				 var tel = $('#phone').val();
				 if(tel == ''){
					 layer.msg('手机号不能为空');
					 return false
				 }else if(!tel_reg.test(tel)){
					 layer.msg('请输入正确的手机号');
					  return false
				 }else{ getcode()}
				 function getcode(){
			            //获取验证码
			            var colock = '';
			            var num = 60;
			            // $("#verCodeBtn").on("click", function(){
			                $("#verCodeBtn").attr("disabled","true");
			                console.log("222");
			                $("#verCodeBtn").val(num+"秒后获取");
			                colock = setInterval(doLoop, 1000);//一秒一次
			            // })
			            
			            function doLoop(){
			                num--;
			                if(num > 0) {
			                    $("#verCodeBtn").val(num+"秒后获取");
			                }else {
			                    clearInterval(colock);//
			                    $("#verCodeBtn").removeAttr("disabled");
			                    $("#verCodeBtn").val("获取验证码");
			                    num = 30;
			                }
			            }
						
											 
						}
						})
						
			        });