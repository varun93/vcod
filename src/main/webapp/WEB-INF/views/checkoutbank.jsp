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
					alert("You are being redirected to bank site ! ");
					document.getElementById('formACS').submit();
				} catch (e) {
					document.getElementById('proceed-btn').style.display = 'block';
				}
			}
            
		</script>
	</head>
	<body onload="setTimeout(formSubmit(), 2000);" >
	<br /><br /><br /><br /><br /><br /><br /><br />
	<center><h1>VCOD</h1></center>
		<form name="formACS" id="formACS" action="http://localhost/sbi/merchant.php" target="_parent"
			method="post">
			
				<input type="hidden" name="EncryptTrans"
					value="${EncryptTrans}" id="EncryptTrans" />
				
				<input type="hidden" name="merchIdVal"
					value="${merchIdVal}" id="merchIdVal" />
			
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
<header class="clearfix norm-hd">
  <table class="header-table" cellspacing="0" border="0">
    <tbody>
      <tr>
        <td class="mer-logo-wrap hd-logo">
          
          <img src="/images/flipkart-logo-blue.png" alt="merchant-logo" />
        
      </td>
      <td class="payz-logo-wrap hd-logo">
        <img src="/payment/images/payzippy-logo.png" alt="payzippy-logo" class="payzippy-header-logo" />
        
      
    </td>
  </tr>
</tbody>
</table>
</header>
<center>
			<section class="main-info info">
          	
            
              Redirecting to your bank's web-site for Net Banking authentication...
            
            
     
			</section>
			<p class="no-re-warn not" id="proceed-btn"><a href="javascript:" onclick="document.formACS.submit();">Click here to proceed &gt;</a></p>
	
			

		</div>
</center>
	</body>
</html>
