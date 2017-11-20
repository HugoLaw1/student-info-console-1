<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="seehope.security.SecurityContext" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/common/meta.jsp" %>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<title>用户管理</title>
<script type="text/javascript" src="${contextPath }/module/user/js/user-list.js"></script>
</head>
<body>
<a class="hidden context"><%=SecurityContext.getCurrentContext() %></a>
<a class="hidden userId"><%=SecurityContext.getCurrentUser().getId() %></a>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 用户中心 <span class="c-gray en">&gt;</span> 用户管理 <a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="pd-20">
	<div class="text-c" id="query"> 用户搜索：
		<input type="text" class="input-text" style="width:250px" placeholder="输入会员名称" id="key" name="key">
		<button type="button" class="btn btn-success radius" id="search" name="search"><i class="Hui-iconfont">&#xe665;</i> 搜用户</button>
	</div>
	<div class="cl pd-5 bg-1 bk-gray mt-20">
	 <span class="l">
	 	<% 
			if(SecurityContext.getCurrentContext().indexOf("4")>=0){
		%>
			<a href="javascript:;" onclick="datasDelete()" class="btn btn-danger radius">
		 		<i class="Hui-iconfont">&#xe6e2;</i> 
		 		批量删除
		 	</a> 
		<% 
			}
			if(SecurityContext.getCurrentContext().indexOf("5")>=0){
		%>
		 	<a href="javascript:;" onclick="member_add('添加用户','UserController/addOrEditUser.do','','510')" class="btn btn-primary radius">
			 	<i class="Hui-iconfont">&#xe600;</i> 
			 	添加用户
		 	</a>
		 <%
			}
		 %>
	 </span> 
	</div>
	<div class="mt-20">
	<table class="table table-border table-bordered table-hover table-bg table-sort">
		<thead>
			<tr class="text-c">
				<th width="25"><input type="checkbox" name="" value=""></th>
				<th width="100">用户名</th>
				<th width="40">手机</th>
				<th width="70">备注</th>
				<th width="10">性别</th>
				<th width="40">状态</th>
				<th width="40">角色</th>
				<th width="120">操作</th>
			</tr>
		</thead>
		<tbody id="user-list">
		</tbody>
	</table>
	</div>
</div>
</body>
</html>