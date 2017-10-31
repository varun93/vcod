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
	<body onload="formSubmit();" >
		<form name="formACS" id="formACS" action="http://localhost/bank/merchant.php" target="_parent"
			method="post">
			
				<input type="hidden" name="orderid"
					value="${order.order_id}" id="orderid" />
				
				<input type="hidden" name="merchantid"
					value="${bank.bank_merchant_id}" id="merchantid" />
			
			
				<input type="hidden" name="amount"
					value="${order.amount}" id="amount" />
		
				<input type="hidden" name="successurl"
					value="http://localhost:8080/Controller/success"/>
					
					<input type="hidden" name="failurl"
					value="http://localhost:8080/Controller/fail" />	
			
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

			<section class="main-info info">
          
            
              Redirecting to your bank's web-site for Net Banking authentication...
            
            
          
				<div class="progress progress-striped active">
					<div class="bar" style="width: 100%;"></div>
				</div>
                <div id="delay-message" style="font-size: 14px; color: red; margin-top: 1em;"></div>
			</section>
			<p class="no-re-warn not" id="proceed-btn"><a href="javascript:" onclick="document.formACS.submit();">Click here to proceed &gt;</a></p>
			<form id="ReturnMerchant" name="ReturnMerchant" method="POST" action="https://www.flipkart.com/pgresponse/flipkart?merchant_key_id=payment&transaction_response_code=CANCELLED_BY_USER&transaction_time=2014-06-04+12%3A30%3A19&bank_name=SBI&transaction_response_message=Transaction+has+been+cancelled+by+user&payment_instrument=NET&version=Cv1&udf2=pay_zippy_api_key&udf1=SI47874D6637474FB990E488509236F8F2&payzippy_transaction_id=PZT14060412301924999&hash_method=MD5&fraud_action=accept&merchant_transaction_id=TX40604083603&transaction_amount=1089400&is_international=false&merchant_id=mp_flipkart&transaction_currency=INR&transaction_auth_state=FAILED&pg_trackid=PZT14060412301924999&hash=b28a2d7181a08874e7d9d9bd7c904e64&pg_id=payzippy&pg_mid=mp_flipkart&transaction_status=FAILED&fraud_details=&payment_method=NET">
        <p class="inter no-re-warn return-flkp" id="return-to-mer-link" ><a href="javascript:" target="_parent" onclick="document.getElementById('ReturnMerchant').submit();" >Click here</a> to cancel and return to Flipkart.com</p>
    </form>

		</div>
		<script>
			if (!(self === window.top)) {
				var el = document.getElementById('detect-iframe');
				if (el) {
					el.className += ' iframe';
				}				
			}
		</script>
	</body>
</html>
