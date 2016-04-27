<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>list</title>
<%@include file="../common.jsp"%>
</head>
<body>
	<table id="dg" title="站点任务列表" class="easyui-datagrid" 
		url="listData" toolbar="#tb" pagination="true" checkOnSelect="false" 
		pageList="[15,20,30,50,100,500,1000]" idField="id" rownumbers="true"
		fitColumns="true" singleSelect="true" remoteSort="false" 
		multiSort="false" striped="true">
		<thead>
			<tr>
				<th field="ck" checkbox="true"></th>
				<th field="id" width="8%" sortable="true">ID</th>
				<th field="portal_name" width="10%" sortable="true" formatter="formatPortalName">站点名称</th>
				<th field="portal_desc" width="15%" sortable="true">站点描述</th>
				<th field="max_thread" width="10%" sortable="true">线程数</th>
				<th field="lastStartTime" width="10%" sortable="true" formatter="formatLastStartTime">上次采集时间</th>
				<th field="nextStartTime" width="10%" sortable="true" formatter="formatNextStartTime">下次采集时间</th>
				<th field="cycle" width="10%" sortable="true">采集周期(h)</th>
				<th field="isAllByHand" width="10%" sortable="true"formatter="isAllByHand">全量采集模式</th>
				<th field="incrementPageCount" width="10%" sortable="true">增量采集页数</th>
				<th field="show_flag" width="10%" sortable="true" formatter="showFlag">状态</th>
				<th field="_operate" width="10%" sortable="true" formatter="formatOper">操作</th>
			</tr>
		</thead>
	</table>

	<div id="tb" style="padding:5px;height:auto">
		<div>
			站点名称: &nbsp;&nbsp;&nbsp;<input id="portal_name" class="easyui-validatebox"
				style="width:120px" plain="true">&nbsp;&nbsp;&nbsp;&nbsp;<a href="#"
				class="easyui-linkbutton" iconCls="icon-search" onclick="loaddata()">查询</a>
			<a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
				onclick="clearcondition()">清除</a>
		</div>

		<div id="toolbar">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-add" plain="true" onclick="addPortal()">新建</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-edit" plain="true" onclick="editPortal()">编辑</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true" onclick="destroyPortal()">删除</a>
		</div>
	</div>
	<div id="status_dlg" class="easyui-dialog" style="width:740px;height:260px;padding:10px 20px" closed="true" buttons="#status_dlg-buttons" data-options="region:'center'">
		<div class="ftitle">站点状态信息</div>
		<div>
			
			<input id="portalId" name="portalId" class="hidden">
			<table  class="gridtable">
				<tr>
					<td style="text-align:right;"><label>站点名称</label></td><td><label id="lab_portalName"></label></td>
					<td style="text-align:right;"><label>运行状态</label></td><td><label id="lab_runState" style="color:red;"></label></td>
					<td style="text-align:right;"><label>下次启动时间</label></td><td><label id="lab_nextStartTime"></label></td>
				</tr>
				<tr>
					<td style="text-align:right;"><label>启动时间</label></td><td><label id="lab_startTime"></label></td>
					<td style="text-align:right;"><label>运行时间</label></td><td><label id="lab_runTime"></label></td>
					<td style="text-align:right;"><label>IP锁定或断网</label></td><td><label id="lab_ipLock"></label></td>
				</tr>
				<tr>
					<td style="text-align:right;"><label>当前/最大线程</label></td><td><label id="lab_maxThread"></label></td>
					<td style="text-align:right;"><label>已处理/全部Url</label></td><td><label id="lab_proUrl"></label></td>
					<td style="text-align:right;"><label>未处理/全部Url</label></td><td><label id="lab_noProUrl"></label></td>
				</tr>
			</table>
		</div>
	</div>
	<div id="status_dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" id="startBtn" onclick="reStartAll()" >Re-Start-All</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" id="startBtn" onclick="reCrakContent()" >Re-Crak-Content</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" id="startBtn" onclick="startPortal()" >Start</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-no" id="stopBtn" onclick="stopPortal()" >Stop</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" id="cancelBtn" onclick="closeStatusDlg()">Cancel</a>
	</div>

	<div id="dlg" class="easyui-dialog" style="width:420px;height:360px;padding:10px 20px" closed="true" buttons="#dlg-buttons" data-options="region:'center'">
		<div class="ftitle">站点信息</div>
		<form id="fm" method="post" novalidate>
			<input name="id" class="hidden">
			<div class="fitem">
				<label>站点名称:</label> <input name="portal_name" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>站点描述:</label> <input name="portal_desc" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>线程数:</label> <input name="max_thread" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>采集周期:</label> <input name="cycle" class="easyui-validatebox" required="true">(h)
			</div>
			<div class="fitem">
				<label>状态:</label> 
				<select name="show_flag" class="easyui-combobox"  required="true" valueField="show_flag">
					<option value="1">有效</option>
					<option value="0">无效</option>
				</select>
			</div>
			<div class="fitem">
				<label>全量采集模式:</label>
				<select name="isAllByHand" class="easyui-combobox"  required="true" valueField="isAllByHand">
					<option value="0">手动全量</option>
					<option value="1">自动全量</option>
				</select>
			</div>
			<div class="fitem">
				<label>增量采集页数:</label> <input name="incrementPageCount" class="easyui-validatebox" required="true">
			</div>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" id="saveBtn" onclick="savePortal()" >Save</a> 
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" id="cancelBtn" onclick="javascript:$('#dlg').dialog('close')">Cancel</a>
	</div>
	<script type="text/javascript">
		var url;
		function addPortal() {
			$('#dlg').dialog('open').dialog('setTitle', '新建站点');
			$('#fm').form('clear');
			url = 'save';
		}
		function editPortal() {
			var row = $('#dg').datagrid('getSelected');
			if (row) {
				$('#dlg').dialog('open').dialog('setTitle', '编辑站点');
				$('#fm').form('load', row);
				url = 'save';
			} else {
				$.messager.confirm('Confirm','Please choose the data you want to edit.');
			}
		}
		function savePortal() {
			$('#fm').form('submit', {
				url : url,
				onSubmit : function() {
					$('#saveBtn').linkbutton('disable');
					$('#cancelBtn').linkbutton('disable');
					return $(this).form('validate');
				},
				success : function(result) {
					$('#saveBtn').linkbutton('enable');
					$('#cancelBtn').linkbutton('enable');
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
		function destroyPortal() {
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
				ss.push(row.id);
			}
			return ss;
		}

		function loaddata() {
			$('#dg').datagrid('reload', {
				portal_name : $("#portal_name").val()
			});
		}

		function clearcondition() {
			$("#portal_name").val("");
			loaddata();
		}

		function formatOper(val,row,index){
			var portalId = row.id;
			return "&nbsp;&nbsp;<a href='listContent?portalId="+portalId+"'>内容</a>&nbsp;&nbsp;<a href='listPortalProcessor?portalId="+portalId+"'>处理器</a>";
		}
		function formatPortalName(val, row, index) {
			var portal_name = row.portal_name;
			var flag = row.show_flag;
			var portalId = row.id;
			if (flag == 1) {
				return "<a href='#' onclick='showPortalStatus("+portalId+")' style='color:red;'>"+portal_name+"</a>";
			} else if (flag == 2) {
				return "<a href='#' onclick='showPortalStatus("+portalId+")' style='color:green;'><strong>"+portal_name+"<strong></a>";
			} else {
				return portal_name;
			}
		}
		
		function showFlag(val, row) {
			var flag = row.show_flag;
			if (flag == 1) {
				return "<font color='red'>有效</font>";
			}else if(flag == 2){
				return "<font color='green'>运行中</font>";
			}else {
				return "无效";
			}
		}
		
		function isAllByHand(val, row) {
			var flag = row.isAllByHand;
			if (flag == 1) {//0:手动全量，自动增量，1：自动全量，手动增量
				return "自动全量";
			} else {
				return "手动全量";
			}
		}
		
		function formatIsEPG(val, row) {
			var flag = row.isEPG;
			if (flag == 0) {//0:是，1:否
				return "是";
			} else {
				return "否";
			}
		}
		
		function formatLastStartTime(val, row) {
			var longTime = row.lastStartTime;
			if(longTime==0){
				return '';
			} else{
				return getStrTime(longTime)
			}
		}
		
		function formatNextStartTime(val, row) {
			var longTime = row.nextStartTime;
			if(longTime==0){
				return '';
			} else{
				return getStrTime(longTime)
			}
		}
		
		
		
		

		var xmlHttp;        //用于保存XMLHttpRequest对象的全局变量
		function mAjax(url, callback) {
// 			alert("url:"+url);
		    if (window.XMLHttpRequest) {							//根据window.XMLHttpRequest对象是否存在使用不同的创建方式
		       xmlHttp = new XMLHttpRequest();						//FireFox、Opera等浏览器支持的创建方式
		    } else {
		       xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");	//IE浏览器支持的创建方式
		    } 
		    
		    xmlHttp.onreadystatechange = callback;					//设置回调函数
		    xmlHttp.open("GET", url, true);
		    xmlHttp.setRequestHeader("If-Modified-Since","0"); 
		    xmlHttp.send(null);
		}
		


		var interval_id;
		
		function showPortalStatus(portalId){
			$("#portalId").val(portalId);
			var url = "status?portalId=" + portalId;
			mAjax(url,statusCallBack);
			$('#status_dlg').dialog({ modal:true });
			$('#status_dlg').dialog('open').dialog('setTitle', '状态');
			interval_id = setInterval("mAjax('"+ url +"', "+ statusCallBack +")", 5000);
		}
		
		function closeStatusDlg(){
			clearInterval(interval_id);
			$('#status_dlg').dialog('close');
		}
		
		function reStartAll(){
			$('#startBtn').linkbutton('disable');
			var portalId = $("#portalId").val();
			var url = "reStartAll?portalId=" + portalId;
			console.log("===>"+url)
			// mAjax(url,null);
		}
		
		function reCrakContent(){
			$('#startBtn').linkbutton('disable');
			var portalId = $("#portalId").val();
			var url = "reCrakContent?portalId=" + portalId;
			console.log("===>"+url)
			// mAjax(url,null);
		}
		
		function startPortal(){
			$('#startBtn').linkbutton('disable');
			var portalId = $("#portalId").val();
			var url = "start?portalId=" + portalId;
			mAjax(url,null);
		}
		
		function stopPortal(){
			$('#stopBtn').linkbutton('disable');
			var portalId = $("#portalId").val();
			var url = "stop?portalId=" + portalId;
			mAjax(url,null);
		}
		

		//获取状态信息的回调函数
		function statusCallBack() {
		    if (xmlHttp.readyState == 4) {
		        var status = eval("("+ xmlHttp.responseText +")");
		        if(status.runState == 0){
					$('#stopBtn').linkbutton('disable');
					$('#startBtn').linkbutton('enable');
		        }else{
					$('#startBtn').linkbutton('disable');
					$('#stopBtn').linkbutton('enable');
		        }
				$("#lab_portalName").text(status.portal.portal_name);
				$("#lab_runState").text(status.runState == 0 ? '停止' : '运行');
				$("#lab_nextStartTime").text(getStrTime(status.portal.nextStartTime));
				$("#lab_startTime").text(status.startTime == 0 ? '' : getStrTime(status.startTime));
				$("#lab_runTime").text(parseInt(status.runTime / 3600000) + '时' + parseInt((status.runTime / 60000) % 60) + '分');
				$("#lab_ipLock").text(status.ipLock ? '非正常' : '正常');
				$("#lab_maxThread").text(status.curThread+"/"+status.maxThread);
				$("#lab_proUrl").text(status.proUrl+"/"+status.allUrl);
				$("#lab_noProUrl").text(status.allUrl-status.proUrl+"/"+status.allUrl);
		    }
		}
		
		
		
		function doMerger(){
			mAjax("merger",showMessage);
		}
		
		function downLogo(){
			mAjax("logodown",showMessage);
		}
		
		function showMessage(){
		    if (xmlHttp.readyState == 4) {
		    	alert(xmlHttp.responseText);
		    }
		}
		
		
		function getStrTime(longTime){
	        var dateValue = new Date(longTime);
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

table.gridtable {
	font-family: verdana,arial,sans-serif;
	color:#333333;
	border-width: 1px;
	border-color: #eee;
	border-collapse: collapse;
}
table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #eee;
	background-color: #dedede;
}
table.gridtable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #eee;
	background-color: #ffffff;
}
</style>
</body>
</html>