<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>list</title>
<%@include file="../common.jsp"%>
</head>
<body>
	<table id="dg" title="处理器列表" class="easyui-datagrid" 
		url="listPortalProcessorData" toolbar="#tb" pagination="true"
		pageList="[15,20,30,50,100,500,1000]" idField="regx_id" rownumbers="true"
		fitColumns="true" singleSelect="false" remoteSort="false" 
		multiSort="false" striped="true">
		<thead>
			<tr>
				<th field="ck" checkbox="true"></th>
				<th field="portal_id" width="10%" sortable="true">站点ID</th>
				<th field="portal_name" width="15%" sortable="true">站点名称</th>
				<th field="url_regx" width="25%" sortable="true">URL正则表达式</th>
				<th field="proc_class" width="25%" sortable="true" formatter="formatProcessorClass">处理器类名</th>
				<th field="rule_file" width="20%" sortable="true">规则文件名</th>
			</tr>
		</thead>
	</table>

	<div id="tb" style="padding:5px;height:auto">
		<div id="toolbar">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-add" plain="true" onclick="addPortalProcessor()">新建</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-edit" plain="true" onclick="editPortalProcessor()">编辑</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true" onclick="destroyPortalProcessor()">删除</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-back" plain="true" onclick="javascript:history.back()">返回</a>
		</div>
	</div>

	<div id="dlg" class="easyui-dialog" style="width:500px;height:280px;padding:10px 20px" closed="true" buttons="#dlg-buttons" data-options="region:'center'">
		<div class="ftitle">处理器信息</div>
		<form id="fm" method="post" novalidate>
			<input name="portal_id" class="hidden">
			<input name="regx_id" class="hidden">
			<div class="fitem">
				<label>处理器:</label> 
				<select id="proc_class" name="proc_class" class="easyui-combobox" required="true"
					 textField="proc_name" 
					 valueField="proc_class" 
					 url="queryProcessorList" 
					 panelHeight="auto" 
					 style="width:220px;"></select>
			</div>
			<div class="fitem">
				<label>正则表达式:</label> <input name="url_regx" class="easyui-validatebox" required="true" size="50">
			</div>
			<div class="fitem">
				<label>规则文件:</label> <input name="rule_file" class="easyui-validatebox" required="true" size="40">
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" id="saveBtn" onclick="savePortalProcessor()" >Save</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" id="cancelBtn" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	</div>
	<script type="text/javascript">
		var url;
		function addPortalProcessor() {
			$('#dlg').dialog('open').dialog('setTitle', '新增处理器');
			$('#fm').form('clear');
			url = 'savePortalProcessor';
		}
		function editPortalProcessor() {
			var row = $('#dg').datagrid('getSelected');
			if (row) {
				$('#dlg').dialog('open').dialog('setTitle', '编辑处理器');
				$('#proc_class').combobox('select',row.proc_class);
				$('#fm').form('load', row);
				url = 'savePortalProcessor';
			} else {
				$.messager.confirm('Confirm','Please choose the data you want to edit.');
			}
		}
		function savePortalProcessor() {
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
		function destroyPortalProcessor() {
			var ss = getSelectedIds();
			if (ss.length > 0) {
				$.messager.confirm('Confirm',
						'Are you sure you want to destroy this data?',
						function(r) {
							if (r) {
								$.messager.progress();
								$.post('deletePortalProcessor', {
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
				ss.push(row.regx_id);
			}
			return ss;
		}

		function loaddata() {
			$('#dg').datagrid('reload', {});
		}
		
		
		function formatProcessorClass(val, row, index) {
			var proc_class = row.proc_class;
			return "<a href='#' >"+proc_class+"</a>";
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