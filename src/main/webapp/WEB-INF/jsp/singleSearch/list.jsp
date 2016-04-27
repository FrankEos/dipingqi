<%@page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>list</title>
<%@include file="../common.jsp"%>
</head>
<body>

	<div class="ftitle">单个泰捷专题处理</div>
	<form id="userform" method="post" novalidate>
		<div>请输入采集不全的泰捷专题id：</div>
		<div>
			<input type="text" name="_id" id="_id" style="width:200px" value=""/>
		</div>
		<div>
			<input type="button" name="button" value="确  定" onclick="do_research()"/>
			<input type="button" name="button" value="清  除" onclick="do_clear()"/>
		</div>
	</form>
	
	<script type="text/javascript">
		function do_research() {
            var _id = $("#_id").val();
         	if(_id == '') {
                alert('请输入泰捷专题id');
                $("#_id").focus();
                return;
         	}
         	
         	$('#userform').form('submit', {
				url : 'doSearch',
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(result) {
					var result = eval('(' + result + ')');
					if (result.resultCode != 0) {
						$.messager.show({
							title : 'Error',
							msg :  result.resultDesc
						});
					} else {
						alert(result.resultDesc);
					}
				}
			});
         }
         
         function do_clear() {
         	$("#_id").val('');
         	$("#_id").focus();
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