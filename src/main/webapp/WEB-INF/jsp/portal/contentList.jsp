<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>list</title>
<%@include file="../common.jsp"%>
</head>
<body>
	<table id="dg" title="内容地址列表" class="easyui-datagrid" 
		url="listContentData" toolbar="#tb" pagination="true"
		pageList="[15,20,30,50,100,500,1000]" idField="url_md5" rownumbers="true"
		fitColumns="true" singleSelect="false" remoteSort="false" 
		multiSort="false" striped="true">
		<thead>
			<tr>
				<th field="ck" checkbox="true"></th>
				<th field="url" width="40%" sortable="true" formatter="formatUrl">URL</th>
				<th field="url_type" width="10%" sortable="true" formatter="formatUrlType">URL类型</th>
				<th field="cost_time" width="10%" sortable="true">耗时</th>
				<th field="oper_flag" width="10%" sortable="true" formatter="formatOperFlag">状态</th>
				<th field="tag" width="10%" sortable="true" >Tag</th>
				<th field="update_time" width="15%" sortable="true" formatter="formatUpdateTime">处理时间</th>
			</tr>
		</thead>
	</table>

	<div id="tb" style="padding:5px;height:auto">
		<div id="toolbar">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-add" plain="true" onclick="addContent()">新建</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-edit" plain="true" onclick="editContent()">编辑</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true" onclick="destroyContent()">删除</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-back" plain="true" onclick="javascript:history.back()">返回</a>
		</div>
	</div>

	<div id="dlg" class="easyui-dialog" style="width:520px;height:360px;padding:10px 20px" closed="true" buttons="#dlg-buttons" data-options="region:'center'">
		<div class="ftitle">Content信息</div>
		<form id="fm" method="post" novalidate>
			<input name="url_md5" class="hidden">
			<input name="content_md5" class="hidden">
			<input name="cost_time" class="hidden">
			<input name="update_time" id="update_time" class="hidden">
			<input name="order" class="hidden">
			<input name="bak" class="hidden">
			<div class="fitem">
				<label>URL:</label> <input name="url" class="easyui-validatebox" required="true" size="40">
			</div>
			<div class="fitem">
				<label>URL类型:</label> 
				<select name="url_type" class="easyui-combobox"  required="true" valueField="url_type" >
					<option value="0">ListUrl</option>
					<option value="1">ContentUrl</option>
				</select>
			</div>
			<div id="uptime_div" class="fitem" style="display: none">
				<label>处理时间:</label> <input id="uptime" class="easyui-datetimebox" data-options="formatter:myformatter">
			</div>
			<div class="fitem">
				<label>状态:</label>
				<select name="oper_flag" class="easyui-combobox"  required="true" valueField="oper_flag">
					<option value="1">未处理</option>
					<option value="2">已处理</option>
				</select>
			</div>
			<div class="fitem">
				<label>Tag:</label> <input name="tag" class="easyui-validatebox" required="true">
			</div>
			<div>
				<!-- <label style="color:red;">Tag按照[泰捷_电影]、[泰捷_综艺]、[华数_下载]、[新浪_明星]的格式填写。</label> -->
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" id="saveBtn" onclick="saveContent()" >Save</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" id="cancelBtn" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	</div>
	<script type="text/javascript">
	 
	 
       
		var url;
		var save_flag = false;
		function addContent() {
			$('#dlg').dialog('open').dialog('setTitle', '新增Url');
			$('#fm').form('clear');
			$('#uptime_div').hide();
			url = 'saveContent';
			save_flag = false;
		}
		function editContent() {
			var row = $('#dg').datagrid('getSelected');
			if (row) {
				var url_type = row.url_type;
				if(url_type==0){
					var update_time = row.update_time;
					$('#uptime').datetimebox('setValue',new Date(update_time).format("yyyy-MM-dd hh:mm:ss"))
					$('#uptime_div').show();
					save_flag = true;
				}else{
					$('#uptime_div').hide();
					save_flag = false;
				}
				$('#dlg').dialog('open').dialog('setTitle', '编辑Url');
				$('#fm').form('load', row);
				url = 'saveContent';
			} else {
				$.messager.confirm('Confirm','Please choose the data you want to edit.');
			}
		}
		function saveContent() {
			if(save_flag) $('#update_time').val(Date.parse($('#uptime').datetimebox('getValue')));
			$('#fm').form('submit', {
				url : url,
				onSubmit : function() {
					$('#saveBtn').attr('disabled','disabled');
					$('#cancelBtn').attr('disabled','disabled');
					return $(this).form('validate');
				},
				success : function(result) {
					$('#saveBtn').attr('disabled','');
					$('#cancelBtn').attr('disabled','');
					var result = eval('(' + result + ')');
					if (result.resultCode != 0) {
						$.messager.show({
							title : 'Error',
							msg : result.resultDesc
						});
					} else {
						$('#dlg').dialog('close'); // close the dialog
						$('#dg').datagrid('reload'); // reload the user data
					}
				}
			});
		}
		function destroyContent() {
			var ss = getSelectedIds();
			if (ss.length > 0) {
				$.messager.confirm('Confirm',
						'Are you sure you want to destroy this data?',
						function(r) {
							if (r) {
								$.messager.progress();
								$.post('deleteContent', {
									ids : ss
								}, function(result) {
									if (result.resultCode == 0) {
										$('#dg').datagrid('reload'); // reload the user data
									} else {
										$.messager.show({ // show error message
											title : 'Error',
											msg : result.resultDesc
										});
									}
									$.messager.progress("close");
								}, 'json');
							}
						});
			} else {
				$.messager.confirm('Confirm',
						'Please choose the data you want to destory?');
			}
		}

		function getSelectedIds() {
			var ss = [];
			var rows = $('#dg').datagrid('getSelections');
			for (var i = 0; i < rows.length; i++) {
				var row = rows[i];
				ss.push(row.url_md5);
			}
			return ss;
		}

		function loaddata() {
			$('#dg').datagrid('reload', {});
		}

		function formatUrl(val, row) {
			var url = row.url;
			return "<a target='_Blank' href='"+url+"'>"+url+"</a>";
		}

		function formatUrlType(val, row) {
			var flag = row.url_type;
			if (flag == 0) {
				return "ListUrl";
			} else {
				return "ContentUrl";
			}
		}
		
		function formatOperFlag(val, row) {
			var flag = row.oper_flag;
			if (flag == 1) {
				return "未处理";
			} else {
				return "已处理";
			}
		}
		
		function formatUpdateTime(val,row) {
			var lTime = row.update_time;
	        var dateValue = new Date(lTime);
	        return dateValue.format("yyyy-MM-dd hh:mm:ss");
		}
		
		 //扩展Date的format方法     
	     Date.prototype.format = function (format) {    
	         var o = {    
	             "M+": this.getMonth() + 1,    
	             "d+": this.getDate(),    
	             "h+": this.getHours(),    
	             "m+": this.getMinutes(),    
	             "s+": this.getSeconds(),    
	             "q+": Math.floor((this.getMonth() + 3) / 3),    
	             "S": this.getMilliseconds()    
	         }    
	         if (/(y+)/.test(format)) {    
	             format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));    
	         }    
	         for (var k in o) {    
	             if (new RegExp("(" + k + ")").test(format)) {    
	                 format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));    
	             }    
	         }    
	         return format;    
	     }
		 
		function myformatter(date){
			return date.format("yyyy-MM-dd hh:mm:ss");
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