<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />


<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">
	$(function(){
		$("#editBtn").click(function(){
			$("#edit-devicename").val("${device.name}");
			$("#edit-aet").val("${device.aet}");
			$("#edit-port").val("${device.port}");
			$("#edit-ip").val("${device.ip}");
			$("#edit-room").val("${device.room}");
			$("#editDeviceModal").modal("show");
		})
		$("#edit-devicename").on("input propertychange",function(){
			if($("#edit-devicename").val()==""){
				$("#msg").html("设备名称不能为空");
				$("#updateBtn").attr("disabled",true);
			}else if($("#edit-devicename").val()!=""&&$("#edit-aet").val()!=""&&$("#edit-port").val()!=""&&$("#edit-ip").val()!=""){
				$("#msg").html("");
				$("#updateBtn").attr("disabled",false);
			}else{
				$("#msg").html("");
				$("#updateBtn").attr("disabled",true);
			}
		})
		$("#edit-aet").on("input propertychange",function(){
			if($("#edit-aet").val()==""){
				$("#msg").html("aet不能为空");
				$("#updateBtn").attr("disabled",true);
			}else if($("#edit-devicename").val()!=""&&$("#edit-aet").val()!=""&&$("#edit-port").val()!=""&&$("#edit-ip").val()!=""){
				$("#msg").html("");
				$("#updateBtn").attr("disabled",false);
			}else{
				$("#msg").html("");
				$("#updateBtn").attr("disabled",true);
			}
		})
		$("#edit-port").on("input propertychange",function(){
			if($("#edit-port").val()==""){
				$("#msg").html("端口不能为空");
				$("#updateBtn").attr("disabled",true);
			}else if($("#edit-devicename").val()!=""&&$("#edit-aet").val()!=""&&$("#edit-port").val()!=""&&$("#edit-ip").val()!=""){
				$("#msg").html("");
				$("#updateBtn").attr("disabled",false);
			}else{
				$("#msg").html("");
				$("#updateBtn").attr("disabled",true);
			}
		})
		$("#edit-ip").on("input propertychange",function(){
			if($("#edit-ip").val()==""){
				$("#msg").html("IP不能为空");
				$("#updateBtn").attr("disabled",true);
			}else if($("#edit-devicename").val()!=""&&$("#edit-aet").val()!=""&&$("#edit-port").val()!=""&&$("#edit-ip").val()!=""){
				$("#msg").html("");
				$("#updateBtn").attr("disabled",false);
			}else{
				$("#msg").html("");
				$("#updateBtn").attr("disabled",true);
			}
		})
		$("#updateBtn").click(function(){
			$("#deviceForm").submit();
		})
	})

</script>

</head>
<body>
<%--修改设备的模态窗口--%>
<div class="modal fade" id="editDeviceModal" role="dialog">
	<div class="modal-dialog" role="document" style="width: 90%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">修改设备</h4>
			</div>
			<div class="modal-body">

				<form action="settings/device/update.do" id="deviceForm" method="post" class="form-horizontal" role="form">

					<div class="form-group">
						<div class="col-sm-10" style="width: 300px;">
							<input type="hidden" class="form-control" id="edit-id" name="id" value="${device.id}">
						</div>
					</div>
					<div class="form-group">
						<label for="edit-devicename" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" autocomplete="off" id="edit-devicename" name="devicename">
						</div>
					</div>
					<div class="form-group">
						<label for="edit-aet" class="col-sm-2 control-label">aet<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" autocomplete="off" id="edit-aet" name="aet">
						</div>
						<label for="edit-port" class="col-sm-2 control-label">端口<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" autocomplete="off" id="edit-port" name="port">
						</div>
					</div>
					<div class="form-group">
						<label for="edit-ip" class="col-sm-2 control-label">IP<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" autocomplete="off" id="edit-ip" name="ip">
						</div>
						<label for="edit-room" class="col-sm-2 control-label">房间</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" autocomplete="off" id="edit-room" name="room">
						</div>
					</div>
				</form>
				<label for="msg" class="col-sm-2 control-label"></label>
				<div class="col-sm-10" style="width: 300px;">
					<span id="msg" style="color: red"></span>
				</div>
				<br>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" id="updateBtn" disabled="disabled">更新</button>
			</div>
		</div>
	</div>
</div>
<div>
	<div style="position: relative; left: 30px; top: -10px;">
		<div class="page-header">
			<h3>设备明细 <small>${device.name}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 80%;">
			<button type="button" class="btn btn-default" onclick=window.location.href="settings/device/index.jsp";><span class="glyphicon glyphicon-arrow-left"></span> 返回</button>
		</div>
	</div>
</div>

<div style="position: relative; left: 60px; top: -50px;">

	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade in active" id="role-info">
			<div style="position: relative; top: 20px; left: -30px;">
				<div style="position: relative; left: 40px; height: 30px; top: 20px;">
					<div style="width: 300px; color: gray;">ID</div>
					<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${device.id}</b></div>
					<div style="height: 1px; width: 600px; background: #D5D5D5; position: relative; top: -20px;"></div>
				</div>
				<div style="position: relative; left: 40px; height: 30px; top: 40px;">
					<div style="width: 300px; color: gray;">名称</div>
					<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${device.name}</b></div>
					<div style="height: 1px; width: 600px; background: #D5D5D5; position: relative; top: -20px;"></div>
				</div>
				<div style="position: relative; left: 40px; height: 30px; top: 60px;">
					<div style="width: 300px; color: gray;">aet</div>
					<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${device.aet}</b></div>
					<div style="height: 1px; width: 600px; background: #D5D5D5; position: relative; top: -20px;"></div>
				</div>
				<div style="position: relative; left: 40px; height: 30px; top: 80px;">
					<div style="width: 300px; color: gray;">端口</div>
					<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${device.port}</b></div>
					<div style="height: 1px; width: 600px; background: #D5D5D5; position: relative; top: -20px;"></div>
				</div>
				<div style="position: relative; left: 40px; height: 30px; top: 100px;">
					<div style="width: 300px; color: gray;">IP</div>
					<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${device.ip}</b></div>
					<div style="height: 1px; width: 600px; background: #D5D5D5; position: relative; top: -20px;"></div>
				</div>
				<div style="position: relative; left: 40px; height: 30px; top: 120px;">
					<div style="width: 300px; color: gray;">房间</div>
					<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${device.room}</b></div>
					<div style="height: 1px; width: 600px; background: #D5D5D5; position: relative; top: -20px;"></div>
					<button style="position: relative; left: 76%; top: -40px;" type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
				</div>
			</div>
		</div>
	</div>
</div>
	
</body>
</html>