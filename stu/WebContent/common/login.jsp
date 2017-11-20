<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="meta.jsp"%>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link href="${contextPath }/static/css/H-ui.login.css" rel="stylesheet"
	type="text/css" />
<title>后台登录</title>
<script type="text/javascript" src="${contextPath }/common/js/login.js"></script>
<script type="text/javascript" src="${contextPath }/common/js/register.js"></script>
<script type="text/javascript">
	function register() {
		layer_show("修改密码","${contextPath }/common/register.jsp","400","450");
	}
</script>

</head>
<body>
	<input type="hidden" id="TenantId" name="TenantId" value="" />
	<div class="header"><h1>学生成绩管理系统</h1></div>
	<div class="loginWraper">
		<div id="loginform" class="loginBox">
			<form class="form form-horizontal"
				action="${contextPath }/LoginController/login.do" method="post"
				onsubmit="return login();">
				<div class="row cl">
					<label class="form-label col-3"><i class="Hui-iconfont">&#xe60d;</i></label>
					<div class="formControls col-8">
						<input id="" name="username" type="text" placeholder="账户"
							class="input-text size-L">
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-3"><i class="Hui-iconfont">&#xe60e;</i></label>
					<div class="formControls col-8">
						<input id="" name="password" type="password" placeholder="密码"
							class="input-text size-L">
					</div>
				</div>
				<div class="row">
					<div class="formControls col-8 col-offset-3">
						<!-- <input name="" type="submit" class="btn btn-success radius size-L"
							value="&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;"> <input 
							name="" type="reset" class="btn btn-primary radius size-L"
							value="&nbsp;取&nbsp;&nbsp;&nbsp;&nbsp;消&nbsp;"> -->
						<button type="submit" name="login"
							class="btn btn-success radius size-L" value="1">登录</button>
						<button type="reset" class="btn btn-warnning radius size-L">取消</button>

					</div>
				</div>
			</form>
<!-- 			<div class="row cl">
				<div class="formControls col-8 col-offset-3">
					<button id="register" onclick="register();"
						class="btn btn-primary radius size-L" value="2">注册</button>
				</div>
			</div> -->

		</div>
	</div>
	<div class="footer"></div>
<!-- 	<script>
		var _hmt = _hmt || [];
		(function() {
			var hm = document.createElement("script");
			hm.src = "//hm.baidu.com/hm.js?080836300300be57b7f34f4b3e97d911";
			var s = document.getElementsByTagName("script")[0];
			s.parentNode.insertBefore(hm, s);
		})();
		var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://"
				: " http://");
		document
				.write(unescape("%3Cscript src='"
						+ _bdhmProtocol
						+ "hm.baidu.com/h.js%3F080836300300be57b7f34f4b3e97d911' type='text/javascript'%3E%3C/script%3E"));
	</script> -->
</body>
</html>