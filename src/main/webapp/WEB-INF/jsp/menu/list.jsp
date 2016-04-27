<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>list</title>
<%@include file="../common.jsp"%>
</head>
<body>
	<table id="dg" title="菜单列表" class="easyui-datagrid" url="listData"
		toolbar="#tb" pagination="true" pageList="[15,20,30,50,100,500,1000]"
		idField="id" rownumbers="true" fitColumns="true" singleSelect="false"
		remoteSort="false" multiSort="false" striped="true">
		<thead>
			<tr>
				<!-- <th data-options="field:'id',checkbox:true"></th> -->
				<th field="ck" checkbox="true"></th>
				<th field="id" width="10" sortable="true">id</th>
				<th field="pid" width="10" sortable="true">父id</th>
				<th field="name" width="20" sortable="true">菜单名</th>
				<th field="link" width="20" sortable="true">link</th>
				<th field="status" width="10" sortable="true" formatter="showFlag">状态</th>
			</tr>
		</thead>
	</table>

	<div id="tb" style="padding: 5px; height: auto">
		<div>
			菜单名: &nbsp;&nbsp;&nbsp;<input id="namekey" class="easyui-validatebox"
				style="width: 120px" plain="true">&nbsp;&nbsp;&nbsp;&nbsp;<a
				href="#" class="easyui-linkbutton" iconCls="icon-search"
				onclick="loaddata()">查询</a> <a href="#" class="easyui-linkbutton"
				iconCls="icon-cancel" onclick="clearcondition()">清除</a>
		</div>

		<div id="toolbar">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-add" plain="true" onclick="newMenu()">新建</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-edit" plain="true" onclick="editMenu()">编辑</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true" onclick="destroyMenu()">删除</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-ok" plain="true" onclick="copyMenu()">复制</a>
		</div>
	</div>

	<div id="dlg" class="easyui-dialog"
		style="width: 400px; height: 280px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons" data-options="region:'center'">
		<div class="ftitle">菜单信息</div>
		<form id="fm" method="post" novalidate>
			<input name="id" class="hidden">
			<div class="fitem">
				<label>父id:</label> <input name="pid" class="easyui-validatebox"
					required="true">
			</div>
			<div class="fitem">
				<label>菜单名:</label> <input name="name" class="easyui-validatebox"
					required="true">
			</div>
			<div class="fitem">
				<label>link:</label> <input name="link" class="easyui-validatebox">
			</div>
			<div class="fitem">
				<label>status:</label> <input name="status"
					class="easyui-validatebox" required="true">
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-ok" onclick="saveMenu()">Save</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	</div>
	<script type="text/javascript">
		var url;
		function newMenu() {
			$('#dlg').dialog('open').dialog('setTitle', '新建菜单');
			$('#fm').form('clear');
			url = 'save';
		}
		function editMenu() {
			var row = $('#dg').datagrid('getSelected');
			if (row) {
				$('#dlg').dialog('open').dialog('setTitle', '编辑菜单');
				$('#fm').form('load', row);
				url = 'save';
			} else {
				$.messager.confirm('Confirm',
						'Please choose the data you want to edit.');
			}
		}
		function saveMenu() {
			$('#fm').form('submit', {
				url : url,
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(result) {
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
		function destroyMenu() {
			var ss = getSelectedIds();
			// $.messager.alert('Info', ss.join('<br/>'));
			// var row = $('#dg').datagrid('getSelected');
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

		function copyMenu() {
			var ss = getSelectedIds();
			if (ss.length > 0) {
				$.messager.confirm('Confirm',
						'Are you sure you want to copy this data?',
						function(r) {
							if (r) {
								$.messager.progress();
								$.post('copy', {
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
						'Please choose the data you want to copy?');
			}
		}

		function getSelectedIds() {
			var ss = [];
			var rows = $('#dg').datagrid('getSelections');
			for (var i = 0; i < rows.length; i++) {
				var row = rows[i];
				ss.push(row.id);
			}
			return ss;
		}

		function loaddata() {
			$('#dg').datagrid('reload', {
				name : $("#namekey").val()
			});
		}

		function clearcondition() {
			$("#namekey").val("");

			loaddata();
		}

		function showFlag(val, row) {
			var flag = row.status;
			if (flag == 1) {
				return '正常';
			} else {
				return '异常';
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