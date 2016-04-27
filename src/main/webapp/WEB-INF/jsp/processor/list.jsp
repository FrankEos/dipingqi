<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>list</title>
<%@include file="../common.jsp"%>
</head>
<body>
	<table id="dg" title="处理器列表" class="easyui-datagrid" 
		url="listData" toolbar="#tb" pagination="true"
		pageList="[15,20,30,50,100,500,1000]" idField="proc_class" rownumbers="true"
		fitColumns="true" singleSelect="false" remoteSort="false" 
		multiSort="false" striped="true">
		<thead>
			<tr>
				<th field="ck" checkbox="true"></th>
				<th field="proc_class" width="40%" sortable="true" formatter="formatProcessorClass">处理器类名</th>
				<th field="proc_name" width="30%" sortable="true">处理器名称</th>
				<th field="proc_descr" width="30%" sortable="true">处理器描述</th>
			</tr>
		</thead>
	</table>

	<div id="tb" style="padding:5px;height:auto">
		<div>
			处理器名称: &nbsp;&nbsp;&nbsp;<input id="proc_name" class="easyui-validatebox"
				style="width:120px" plain="true">&nbsp;&nbsp;&nbsp;&nbsp;<a href="#"
				class="easyui-linkbutton" iconCls="icon-search" onclick="loaddata()">查询</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
				onclick="clearcondition()">清除</a>
		</div>

		<div id="toolbar">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-add" plain="true" onclick="addProcessor()">新建</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-edit" plain="true" onclick="editProcessor()">编辑</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true" onclick="destroyProcessor()">删除</a>
		</div>
	</div>

	<div id="dlg" class="easyui-dialog" style="width:600px;height:240px;padding:10px 20px" closed="true" buttons="#dlg-buttons" data-options="region:'center'">
		<div class="ftitle">处理器信息</div>
		<form id="fm" method="post" novalidate>
			<input id="save_flag" name="save_flag" class="hidden">
			<div class="fitem">
				<label>处理器类名:</label> <input id="proc_class" name="proc_class" class="easyui-validatebox" required="true" size="45">
			</div>
			<div class="fitem">
				<label>处理器名称:</label> <input name="proc_name" class="easyui-validatebox" required="true" size="45">
			</div>
			<div class="fitem">
				<label>处理器描述:</label> <input name="proc_descr" class="easyui-validatebox" required="true" size="45">
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" id="saveBtn" onclick="saveProcessor()" >Save</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" id="cancelBtn" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	</div>
	<script type="text/javascript">
		var url;
		function addProcessor() {
			$('#dlg').dialog('open').dialog('setTitle', '新建处理器');
			$('#fm').form('clear');
			$('#proc_class').removeAttr('readonly');
			$("#save_flag").val("add");
			url = 'save';
		}
		function editProcessor() {
			var row = $('#dg').datagrid('getSelected');
			if (row) {
				$('#dlg').dialog('open').dialog('setTitle', '编辑处理器');
				$('#fm').form('load', row);
				$('#proc_class').attr('readonly','readonly');
				$("#save_flag").val("edit");
				url = 'save';
			} else {
				$.messager.confirm('Confirm','Please choose the data you want to edit.');
			}
		}
		function saveProcessor() {
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
		function destroyProcessor() {
			var ss = getSelectedIds();
			if (ss.length > 0) {
				$.messager.confirm('Confirm',
						'Are you sure you want to destroy this data?',
						function(r) {
							if (r) {
								$.messager.progress();
								$.post('delete', {
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
				ss.push(row.proc_class);
			}
			return ss;
		}

		function loaddata() {
			$('#dg').datagrid('reload', {
				proc_name : $("#proc_name").val()
			});
		}

		function clearcondition() {
			$("#proc_name").val("");

			loaddata();
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