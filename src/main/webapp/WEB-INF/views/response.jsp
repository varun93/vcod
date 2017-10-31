<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<script type="text/javascript">
			(function () {
				var tags = ['article', 'aside', 'figcaption', 'figure', 'footer', 'header', 'hgroup', 'nav', 'section'],
					i;
				for(i = 0; i < tags.length; i++) {
					document.createElement(tags[i]);
				}		
			})();
			function formSubmit() {
				try {
					//throw new Error('fdf');
					alert("Processing payment. . You are being redirected to Merchant site flipkart site ! ");
					document.getElementById('formACS').submit();
				} catch (e) {
					document.getElementById('proceed-btn').style.display = 'block';
				}
			}
            
		</script>
	</head>
	<body onload="formSubmit();" >
	<br /><br /><br /><br /><br /><br /><br /><br />
	<center><h1>VCOD</h1></center>
		<form name="formACS" id="formACS" action="${url}" target="_parent"
			method="post">
			
				<input type="hidden" name="EncryptResponse"
					value="${EncryptResponse}" id="EncryptResponse" />
		</form>
	
		<div class="wrap" id="detect-iframe" >
                      
                      

<style>
.clr {
	overflow: hidden;
}
.mer-logo-wrap {
  text-align: left;
  padding-left: 35px;
}
.header-table {
  width: 100%;
}
.header-table td {
  vertical-align: middle;
  width: 49.9%;
}
.mer-logo-wrap > img {
  max-height: 75px;
}
.payz-logo-wrap {
  text-align: right;
  padding-right: 35px;
}
.norm-hd {
  padding: 15px 0;
  position: relative;
  z-index: 1000;
  border-bottom: 1px solid #e5e5e5;
}
</style>

			<section class="main-info info">
          <center>
            
              Redirecting to your Merchant site . . Please wait .. 
            
            </center>
          
				<div class="progress progress-striped active">
					<div class="bar" style="width: 100%;"></div>
				</div>
                <div id="delay-message" style="font-size: 14px; color: red; margin-top: 1em;"></div>
			</section>
			

		</div>
	</body>
</html>
