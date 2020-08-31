 var demo_h5_upload_ops = {
     init: function() {
         this.eventBind();
     },
     eventBind: function() {
         var that = this;
         $("#card_poto1").change(function() {
             var reader = new FileReader();
             reader.onload = function(e) {
                 that.compress(this.result);
             };
             reader.readAsDataURL(this.files[0]);
         });
     },
     compress: function(res) {
         var that = this;
         var img = new Image(),
             maxH = 300;

         img.onload = function() {
             var cvs = document.createElement('canvas'),
                 ctx = cvs.getContext('2d');

             if (img.height > maxH) {
                 img.width *= maxH / img.height;
                 img.height = maxH;
             }
             cvs.width = img.width;
             cvs.height = img.height;

             ctx.clearRect(0, 0, cvs.width, cvs.height);
             ctx.drawImage(img, 0, 0, img.width, img.height);
             var dataUrl = cvs.toDataURL('image/jpeg', 1);
             $(".img_wrap").attr("src", dataUrl);
             $(".img_wrap").show();
             // 上传略
             that.upload(dataUrl);
         };

         img.src = res;
     },
     upload: function(image_data) {
         /*这里写上次方法,图片流是base64_encode的*/
     }
 };
 /*2*/
 var demo_h5_upload_ops1 = {
     init: function() {
         this.eventBind();
     },
     eventBind: function() {
         var that = this;
         $("#card_poto2").change(function() {
             var reader = new FileReader();
             reader.onload = function(e) {
                 that.compress(this.result);
             };
             reader.readAsDataURL(this.files[0]);
         });
     },
     compress: function(res) {
         var that = this;
         var img = new Image(),
             maxH = 300;
 
         img.onload = function() {
             var cvs = document.createElement('canvas'),
                 ctx = cvs.getContext('2d');
 
             if (img.height > maxH) {
                 img.width *= maxH / img.height;
                 img.height = maxH;
             }
             cvs.width = img.width;
             cvs.height = img.height;
 
             ctx.clearRect(0, 0, cvs.width, cvs.height);
             ctx.drawImage(img, 0, 0, img.width, img.height);
             var dataUrl = cvs.toDataURL('image/jpeg', 1);
             $(".three").attr("src", dataUrl);
             $(".three").show();
             // 上传略
             that.upload(dataUrl);
         };
 
         img.src = res;
     },
     upload: function(image_data) {
         /*这里写上次方法,图片流是base64_encode的*/
     }
 };
 /**/
 /*3*/
 var demo_h5_upload_ops2 = {
     init: function() {
         this.eventBind();
     },
     eventBind: function() {
         var that = this;
         $("#acc_poto1").change(function() {
             var reader = new FileReader();
             reader.onload = function(e) {
                 that.compress(this.result);
             };
             reader.readAsDataURL(this.files[0]);
         });
     },
     compress: function(res) {
         var that = this;
         var img = new Image(),
             maxH = 300;
 
         img.onload = function() {
             var cvs = document.createElement('canvas'),
                 ctx = cvs.getContext('2d');
 
             if (img.height > maxH) {
                 img.width *= maxH / img.height;
                 img.height = maxH;
             }
             cvs.width = img.width;
             cvs.height = img.height;
 
             ctx.clearRect(0, 0, cvs.width, cvs.height);
             ctx.drawImage(img, 0, 0, img.width, img.height);
             var dataUrl = cvs.toDataURL('image/jpeg', 1);
             $(".four").attr("src", dataUrl);
             $(".four").show();
             // 上传略
             that.upload(dataUrl);
         };
 
         img.src = res;
     },
     upload: function(image_data) {
         /*这里写上次方法,图片流是base64_encode的*/
     }
 };
 /**/
 /*4*/
 var demo_h5_upload_ops3 = {
     init: function() {
         this.eventBind();
     },
     eventBind: function() {
         var that = this;
         $("#acc_poto2").change(function() {
             var reader = new FileReader();
             reader.onload = function(e) {
                 that.compress(this.result);
             };
             reader.readAsDataURL(this.files[0]);
         });
     },
     compress: function(res) {
         var that = this;
         var img = new Image(),
             maxH = 300;
 
         img.onload = function() {
             var cvs = document.createElement('canvas'),
                 ctx = cvs.getContext('2d');
 
             if (img.height > maxH) {
                 img.width *= maxH / img.height;
                 img.height = maxH;
             }
             cvs.width = img.width;
             cvs.height = img.height;
 
             ctx.clearRect(0, 0, cvs.width, cvs.height);
             ctx.drawImage(img, 0, 0, img.width, img.height);
             var dataUrl = cvs.toDataURL('image/jpeg', 1);
             $(".five").attr("src", dataUrl);
             $(".five").show();
             // 上传略
             that.upload(dataUrl);
         };
 
         img.src = res;
     },
     upload: function(image_data) {
         /*这里写上次方法,图片流是base64_encode的*/
     }
 };
 /**/
 /*5*/
 var demo_h5_upload_ops4 = {
     init: function() {
         this.eventBind();
     },
     eventBind: function() {
         var that = this;
         $("#person_poto").change(function() {
             var reader = new FileReader();
             reader.onload = function(e) {
                 that.compress(this.result);
             };
             reader.readAsDataURL(this.files[0]);
         });
     },
     compress: function(res) {
         var that = this;
         var img = new Image(),
             maxH = 300;
 
         img.onload = function() {
             var cvs = document.createElement('canvas'),
                 ctx = cvs.getContext('2d');
 
             if (img.height > maxH) {
                 img.width *= maxH / img.height;
                 img.height = maxH;
             }
             cvs.width = img.width;
             cvs.height = img.height;
 
             ctx.clearRect(0, 0, cvs.width, cvs.height);
             ctx.drawImage(img, 0, 0, img.width, img.height);
             var dataUrl = cvs.toDataURL('image/jpeg', 1);
             $(".two").attr("src", dataUrl);
             $(".two").show();
             // 上传略
             that.upload(dataUrl);
         };
 
         img.src = res;
     },
     upload: function(image_data) {
         /*这里写上次方法,图片流是base64_encode的*/
     }
 };
 /**/

 $(document).ready(function() {
     demo_h5_upload_ops.init();
	  demo_h5_upload_ops1.init();
	  demo_h5_upload_ops2.init();
	   demo_h5_upload_ops3.init();
	   demo_h5_upload_ops4.init();
 });