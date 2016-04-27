<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>网络爬虫后台管理系统 - 登陆页面</title>
<%@include file="common.jsp"%>
<script language="JavaScript">
//判断当前窗口是否有顶级窗口，如果有就让当前的窗口的地址栏发生变化，
//这样就可以让登陆窗口显示在整个窗口了
function loadTopWindow(){
	if (window.top!=null && window.top.document.URL!=document.URL){
		window.top.location= document.URL; 
	}
}
</script>
</head>
<body id=userlogin_body onload="loadTopWindow()">
	<div id=user_login>
		<dl>
		<form id="fm1" method="post" novalidate>
		
			<dd id=user_top>
				<ul>
					<li class=user_top_l></li>
					<li class=user_top_c></li>
					<li class=user_top_r></li>
				</ul>
			</dd>
			<dd id=user_main>
				<ul>
					<li class=user_main_l></li>
					<li class=user_main_c>
						<div class=user_main_box>
							<ul>
								<li class=user_main_text>用户名：</li>
								<li class=user_main_input>
									<input
									class="txtusernamecssclass easyui-validatebox"
									data-options="required:true,missingMessage:'账号不能为空.'"
									name="userName" value="" maxlength="20" onkeydown="KeyDown()">
									</li>
							</ul>
							<ul>
								<li class=user_main_text>密 码：</li>
								<li class=user_main_input>
								<input
									class="txtpasswordcssclass easyui-validatebox"
									data-options="required:true,missingMessage:'密码不能为空.'"
									type="password" name="password" value="" onkeydown="KeyDown()"></li>
							</ul>
							<ul>
								<li class=user_main_text>验证码：</li>
								<li class=user_main_input>
								 <a href="javascript:change()">
								<img id="codeimg" class="vc-pic" width="65" height="23" title="点击刷新验证码" src="ImageServlet?time=new Date().getTime()"></a>
								<input class="vc-text easyui-validatebox" data-options="required:true,missingMessage:'验证码不能为空.'" maxlength="4" type="text" name="verifyCode">
									
								</li>
							</ul>
						</div>
					</li>
				</ul>
			</dd>
		</form>
		<li class="user_main_r" style="list-style-type: none;"><input
			class="ibtnentercssclass"
			style="border-top-width: 0px; border-left-width: 0px; border-bottom-width: 0px; border-right-width: 0px"
			type=image src="images/login/user_botton.gif" onclick="login();"></li>
		<dd id=user_bottom>
			<ul>
				<li class=user_bottom_l></li>
				<li class=user_bottom_c><span style="margin-top: 10px">技术支持：曦威胜（深圳）技术有限公司
				</li>
				<li class=user_bottom_r></li>
			</ul>
		</dd>
	</dl>
	</div>
		<script type="text/javascript">
		function change(){
			var getimagecode=document.getElementById("codeimg");  
		    getimagecode.src="ImageServlet?time="+new Date().getTime();  
		}
		function login() {
			$('#fm1').form('submit', {
				url : 'login',
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(result) {
					var result = eval('('+result+')');
					if (result.resultCode!= 0) {
						$.messager.show({
							title : 'Error',
							msg :result.resultDesc
						});
					} else {
						window.location = "<%=contextPath%>/index";
						}
					}
				});
			}
		
		</script>
		
		<script type="text/javascript">
			if(document.addEventListener){//如果是Firefox
				document.addEventListener("keypress",fireFoxHandler, true);
			}else{
				document.attachEvent("onkeypress",ieHandler);
				}
			function fireFoxHandler(evt){
				//alert("firefox");
				if(evt.keyCode==13){
					login();//你的代码
					}
				}
			function ieHandler(evt){
				//alert("IE");
				if(evt.keyCode==13){
					login();//你的代码
					}
				}
			</script>
			
		<style type="text/css">
#fm {
	margin: 0;
	padding: 10px 30px;
}

.ftitle {
	font-size: 14px;
	font-weight: bold;
	padding: 5px 0;
	margin-bottom: 10px;
	border-bottom: 1px solid #ccc;
}

.fitem {
	margin-bottom: 5px;
}

.fitem label {
	display: inline-block;
	width: 80px;
}
</style>
</body>
</html>