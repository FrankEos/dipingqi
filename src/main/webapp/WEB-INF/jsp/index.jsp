<%@page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>数据采集后台管理系统</title>
<mete chatset = "utf-8"></mete>
<%@include file="common.jsp"%>
<script type="text/javascript" src="js/index.js"></script>
</script>
</head>

<body class="easyui-layout">
	<div class="ui-header"
		data-options="region:'north',split:true,border:false"
		style="height:40px;overflow: hidden;">

		<div class="ui-login">
			<div class="ui-login-info">
				欢迎 <span class="orange">${user.userName}</span>登录系统
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
				 <a href="javascript:void(0)" plain="true" onclick="upwd()">修改密码</a> | <a class="logout-btn"
					href="<%=contextPath%>/logout">退出</a>
			</div>
		</div>
	</div>
	<!-- 树形菜单 -->
	<div data-options="region:'west',split:true,title:'数据采集后台管理系统'"
		style="width:200px;">
		<div id="tree-box" class="easyui-accordion"
			data-options="fit:true,border:false">
			<c:forEach var="item" items="${menus}">
				<div title="${item.name}">
					<c:forEach var="node" items="${item.sonMenus}">
						<a class="menu-item" href="${node.link}">${node.name}</a>
					</c:forEach>
				</div>
			</c:forEach>
		</div>
	</div>

	<!-- 中间内容页面 -->
	<div data-options="region:'center'">
		<div class="easyui-tabs" id="tab-box"
			data-options="fit:true,border:false">
			<div title="欢迎页" style="padding:20px;overflow:hidden;">
				<div style="margin-top:20px;">
					<h3>简要说明</h3>
					<ul>
						<li>使用Java平台,采用SpringMVC+Mybatis等主流框架</li>
						<li>数据库:使用免费MYSQL</li>
						<li>前端:使用Jquery和Easyui技术.界面清晰简洁,易操作.</li>
						<li>权限:对菜单,按钮控制.仅展示有权限的菜单和按钮.</li>
						<li>拦截:对所有无权限URL进行拦截,防止手动发送HTTP请求,确保系统全性.</li>
						<li>代码生成：根据表生成对应的Bean,Service,Mapper,Action,XML等，提高开发效率.</li>
					</ul>
				</div>

			
			</div>
		</div>
	</div>
	
	<!--  modify password start -->
	<div id="modify-pwd-win"  class="easyui-dialog" buttons="#editPwdbtn" title="修改用户密码" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:350px;height:200px;">
		<form id="pwdForm" class="ui-form" method="post">
     		 <input class="hidden" name="id">
     		 <input class="hidden" name="userName" value="111">
     		 <div class="ui-edit">
	           <div class="fitem">  
	              <label>旧密码:</label>  
	              <input id="paswd" name="password" type="password" class="easyui-validatebox"  data-options="required:true"/>
	           </div>
	            <div class="fitem">  
	               <label>新密码:</label>  
	               <input id="newPwd" name="newPwd" type="password" class="easyui-validatebox" data-options="required:true" />
	           </div> 
	           <div class="fitem">  
	               <label>重复密码:</label>  
	              <input id="rpwd" name="rpwd" type="password" class="easyui-validatebox"   required="required" validType="equals['#newPwd']" />
	           </div> 
	         </div>
     	 </form>
     	 <div id="editPwdbtn" class="dialog-button" >  
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="save()" id="btn-pwd-submit">提交</a>  
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#modify-pwd-win').dialog('close')" id="btn-pwd-close">关闭</a>  
         </div>
	</div>
	<script type="text/javascript">
		function upwd(){
			$('#modify-pwd-win').dialog('open').dialog('setTitle', '密码修改');
			$('#pwdForm').form('clear');
		}
		
		function save() {
			$('#pwdForm').form('submit', {
				url :'upwd',
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(result) {
					var result = eval('(' + result + ')');
					if (result.resultCode != 0) {
						if(result.resultCode == 101002){
							alert("原密码输入有误，请重新输入！");
						}else{
							alert("用户未登录,请先登录！");
							window.location = "<%=contextPath%>/preLogin";
						}
					} else {
						$('#modify-pwd-win').dialog('close'); // close the dialog
						$('#dg').datagrid('reload'); // reload the user data
					}
				}
			});
		}
	</script>
</body>
</html>