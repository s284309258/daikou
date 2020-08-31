 function seaImg(){
	    $(".mask-img").on("click",function(e){
	      $(".mask-img").css("display","none");
	      $(".picture").css("display","none");      
	    })
	    var imgs = $('.seaImg img')
	    var images;
	    imgs.on('click',function(e){
	      var father = (e.currentTarget).parentNode; //当前点击图片的父元素
	      var att = father.attributes.id; //父元素自己的属性id
	      var image = '#' + att + ' img'
	      images = $(image)  //jquer获取id下的所有img
	      $(".mask-img").css("display","block");
	      $(".picture").css("display","block");     
	      $(".img").attr("src",e.currentTarget.src);
	      if(e.currentTarget == images[0]){
	        $(".left").css("display","none");
	      }else{
	        $(".left").css("display","block");
	      }      
	      if(e.currentTarget == images[images.length-1]){
	        $(".right").css("display","none");       
	      }else{
	        $(".right").css("display","block");      
	      }
	    })
	    　　　　　　　　　　//左点击事件，当图片为第一张的时候左边的箭头点击图片隐藏
	    $(".left").on("click",function(){ 
	      var imgSrc = $(".img").attr("src");
	      $(".right").css("display","block");    
	      for(var i = 0 ; i<images.length; i++){   
	        if(imgSrc == images[i].src){
	          if(imgSrc == images[1].src){
	            $(".left").css("display","none");
	          }
	          var j = i;
	          $(".img").attr("src",images[j-1].src);
	        }
	 
	      }
	    })
	    　　　　　　　　　//右点击事件， 当图片为最后一张的时候右边箭头点击图片隐藏
	    $(".right").on("click",function(){
	      var imgSrc = $(".img").attr("src");
	      $(".left").css("display","block");     
	      for(var i = 0 ; i<images.length; i++){       
	        if(imgSrc == images[i].src){
	          if(imgSrc == imgs[images.length-2].src){
	            $(".right").css("display","none");
	          }
	          var j = i;
	          $(".img").attr("src",images[j+1].src);
	        }
	      }
	    })
	     
	  }
	   seaImg()