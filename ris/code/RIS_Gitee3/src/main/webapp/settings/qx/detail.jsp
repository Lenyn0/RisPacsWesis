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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/zTree_v3-master/js/jquery.ztree.all.min.js"></script>

<SCRIPT type="text/javascript">
	/*var setting = {
		data: {
			simpleData: {
				enable: true
			}
		}
	};

	
	$(document).ready(function(){
		$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	});*/

	$(function(){
		$(".time").datetimepicker({
			minView: "hour",
			language:  'zh-CN',
			format: 'yyyy-mm-dd hh:ii:ss',
			autoclose: true,
			todayBtn: true,
			startDate: new Date(),
			pickerPosition: "bottom-right"
		});
		$("#editBtn").click(function(){
			$("#edit-loginAct").val("${u.loginAct}");
			$("#edit-username").val("${u.name}");
			$("#edit-email").val("${u.email}");
			$("#edit-phoneNumber").val("${u.phoneNumber}");
			$("#edit-lockState").val("${u.lockState}");
			$("#edit-expireTime").val("${u.expireTime}");
			$("#edit-allowIps").val("${u.allowIps}");
			var str = "";
			if("${u.privileges}".indexOf("登记员")!=-1){
				str += "1";
			}
			if("${u.privileges}".indexOf("检查技师")!=-1){
				str += "2";
			}
			if("${u.privileges}".indexOf("报告医生")!=-1){
				str += "3";
			}
			if("${u.privileges}".indexOf("报告审核医生")!=-1){
				str += "4";
			}
			if("${u.privileges}".indexOf("科室主任")!=-1){
				str += "6";
			}
			if("${u.privileges}".indexOf("系统管理员")!=-1){
				str += "5";
			}
			var $privileges = $("input[name=privilege]");
			for(var i=0;i<$privileges.length;i++){
				if(str.indexOf($($privileges[i]).val())!=-1){
					$($privileges[i]).attr('checked','checked');
				}
			}
			$("#editUserModal").modal("show");
		})
		$("#edit-loginAct").on("input propertychange",function(){
			if($("#edit-loginAct").val()==""){
				$("#msg1").html("账号不能为空");
				$("#updateBtn").attr("disabled",true);
			}else{
				$("#msg1").html("");
				$("#updateBtn").attr("disabled",false);
			}
		})
		$("#updateBtn").click(function(){
			var $privileges = $("input[name=privilege]:checked");
			var str = "";
			for(var i=0;i<$privileges.length;i++){
				str += $($privileges[i]).val();
				//如果不是最后一个元素，需要在后面追加一个,符
				if(i<$privileges.length-1){
					str += ",";
				}
			}
			$("#edit-privileges").val(str);
			if($("#edit-privileges").val()==""){
				return false;
			}
			$("#userForm").submit();
		})
		$("#editPwdBtn").click(function(){
			$("#editUserPwdModal").modal("show");
		})
		$("#edit-loginPwd").on("input propertychange",function(){
			if($("#edit-loginPwd").val()==""){
				$("#msg2").html("登录密码不能为空");
				$("#updatePwdBtn").attr("disabled",true);
			}else if($("#edit-loginPwd").val()==$("#edit-confirmPwd").val()){
				$("#msg2").html("");
				$("#updatePwdBtn").attr("disabled",false);
			}else{
				$("#msg2").html("");
				$("#updatePwdBtn").attr("disabled",true);
			}
		})
		$("#edit-confirmPwd").on("input propertychange",function(){
			if($("#edit-confirmPwd").val()==""){
				$("#msg2").html("确认密码不能为空");
				$("#updatePwdBtn").attr("disabled",true);
			}else if($("#edit-loginPwd").val()==$("#edit-confirmPwd").val()){
				$("#msg2").html("");
				$("#updatePwdBtn").attr("disabled",false);
			}else{
				$("#msg2").html("两次密码不一致");
				$("#updatePwdBtn").attr("disabled",true);
			}
		})
		$("#updatePwdBtn").click(function(){
			$("#userPwdForm").submit();
		})
	})

</SCRIPT>

</head>
<body>

	<!-- 分配许可的模态窗口 -->
	<%--<div class="modal fade" id="assignRoleForUserModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">为<b>张三</b>分配角色</h4>
				</div>
				<div class="modal-body">
					<table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
						<tr>
							<td width="42%">
								<div class="list_tit" style="border: solid 1px #D5D5D5; background-color: #F4F4B5;">
									张三，未分配角色列表
								</div>
							</td>
							<td width="15%">
								&nbsp;
							</td>
							<td width="43%">
								<div class="list_tit" style="border: solid 1px #D5D5D5; background-color: #F4F4B5;">
									张三，已分配角色列表
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<select size="15" name="srcList" id="srcList"
									style="width: 100%" multiple="multiple">
									<option>
										总裁
									</option>
									<option>
										市场部普通职员
									</option>
									<option>
										市场总监
									</option>
									<option>
										销售部销售员
									</option>
									<option>
										销售总监
									</option>
								</select>
							</td>
							<td>
								<p align="center">
									<a href="javascript:void(0);" title="分配角色"><span class="glyphicon glyphicon-chevron-right" style="font-size: 20px;"></span></a>
								</p>
								<br><br>
								<p align="center">
									<a href="javascript:void(0);" title="撤销角色"><span class="glyphicon glyphicon-chevron-left" style="font-size: 20px;"></span></a>
								</p>
							</td>
							<td>
								<select name="destList" size="15" multiple="multiple"
									id="destList" style="width: 100%">
									<option>
										副总裁
									</option>
								</select>
							</td>
						</tr>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>--%>

	<!-- 编辑用户的模态窗口 -->
	<div class="modal fade" id="editUserModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改用户</h4>
				</div>
				<div class="modal-body">
				
					<form action="settings/user/update.do" id="userForm" method="post" class="form-horizontal" role="form">

						<div class="form-group">
							<div class="col-sm-10" style="width: 300px;">
								<input type="hidden" class="form-control" id="edit-editBy" name="editBy" value="${user.name}">
							</div>
							<div class="col-sm-10" style="width: 300px;">
								<input type="hidden" class="form-control" id="edit-id" name="id" value="${u.id}">
							</div>
						</div>
						<div class="form-group">
							<label for="edit-loginAct" class="col-sm-2 control-label">登录帐号<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" autocomplete="off" id="edit-loginAct" name="loginAct">
							</div>
							<label for="edit-username" class="col-sm-2 control-label">用户姓名</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" autocomplete="off" id="edit-username" name="username">
							</div>
						</div>
						<div class="form-group">
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" autocomplete="off" id="edit-email" name="email">
							</div>
							<label for="edit-phoneNumber" class="col-sm-2 control-label">手机号码</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" autocomplete="off" id="edit-phoneNumber" name="phoneNumber">
							</div>
						</div>
						<div class="form-group">
							<label for="edit-lockState" class="col-sm-2 control-label">锁定状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-lockState" name="lockState">
								  <option></option>
								  <option>启用</option>
								  <option>锁定</option>
								</select>
							</div>
							<label for="edit-expireTime" class="col-sm-2 control-label">失效时间</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-expireTime" name="expireTime">
							</div>
						</div>
						<div class="form-group">
							<label for="edit-allowIps" class="col-sm-2 control-label">允许访问的IP</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" autocomplete="off" id="edit-allowIps" style="width: 280%" placeholder="多个用逗号隔开" name="allowIps">
							</div>
						</div>
						<div class="form-group">
							<label for="edit-privilege" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 800px;">
								<input type="checkbox" name="privilege" value="1"><label>登记员&nbsp&nbsp</label>
								<input type="checkbox" name="privilege" value="2"><label>检查技术&nbsp&nbsp</label>
								<input type="checkbox" name="privilege" value="3"><label>报告医生&nbsp&nbsp</label>
								<input type="checkbox" name="privilege" value="4"><label>报告审核医生&nbsp&nbsp</label>
								<input type="checkbox" name="privilege" value="5"><label>科室主任&nbsp&nbsp</label>
								<input type="checkbox" name="privilege" value="6"><label>系统管理员&nbsp&nbsp</label>
							</div>
						</div>
						<div class="form-group">
							<input type="hidden"  id="edit-privileges" name="privileges">
						</div>
					</form>
					<label for="msg1" class="col-sm-2 control-label"></label>
					<div class="col-sm-10" style="width: 300px;">
						<span id="msg1" style="color: red"></span>
					</div>
					<br>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改用户密码的模态窗口 -->
	<div class="modal fade" id="editUserPwdModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改密码</h4>
				</div>
				<div class="modal-body">

					<form action="settings/user/editPwdByAdmin.do" id="userPwdForm" method="post" class="form-horizontal" role="form">

						<div class="form-group">
							<div class="col-sm-10" style="width: 300px;">
								<input type="hidden" class="form-control" id="edit-editPwdBy" name="editPwdBy" value="${user.name}">
							</div>
							<div class="col-sm-10" style="width: 300px;">
								<input type="hidden" class="form-control" id="edit-PwdOfid" name="PwdOfid" value="${u.id}">
							</div>
						</div>
						<div class="form-group">
							<label for="edit-loginPwd" class="col-sm-2 control-label">登录密码<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="password" class="form-control" autocomplete="off" id="edit-loginPwd" name="loginPwd">
							</div>
							<label for="edit-confirmPwd" class="col-sm-2 control-label">确认密码<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="password" class="form-control" autocomplete="off" id="edit-confirmPwd" name="confirmPwd">
							</div>
						</div>
					</form>
					<label for="msg2" class="col-sm-2 control-label"></label>
					<div class="col-sm-10" style="width: 300px;">
						<span id="msg2" style="color: red"></span>
					</div>
					<br>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updatePwdBtn" disabled="disabled">更新</button>
				</div>
			</div>
		</div>
	</div>

	<div>
		<div style="position: relative; left: 30px; top: -10px;">
			<div class="page-header">
				<h3>用户明细 <small>${u.name}</small></h3>
			</div>
			<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 80%;">
				<button type="button" class="btn btn-default" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left"></span> 返回</button>
			</div>
		</div>
	</div>
	
	<div style="position: relative; left: 60px; top: -50px;">
		
		<div id="myTabContent" class="tab-content">
			<div class="tab-pane fade in active" id="role-info">
				<div style="position: relative; top: 20px; left: -30px;">
					<div style="position: relative; left: 40px; height: 30px; top: 20px;">
						<div style="width: 300px; color: gray;">登录帐号</div>
						<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${u.loginAct}</b></div>
						<div style="height: 1px; width: 600px; background: #D5D5D5; position: relative; top: -20px;"></div>
					</div>
					<div style="position: relative; left: 40px; height: 30px; top: 40px;">
						<div style="width: 300px; color: gray;">用户姓名</div>
						<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${u.name}</b></div>
						<div style="height: 1px; width: 600px; background: #D5D5D5; position: relative; top: -20px;"></div>
					</div>
					<div style="position: relative; left: 40px; height: 30px; top: 60px;">
						<div style="width: 300px; color: gray;">邮箱</div>
						<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${u.email}</b></div>
						<div style="height: 1px; width: 600px; background: #D5D5D5; position: relative; top: -20px;"></div>
					</div>
					<div style="position: relative; left: 40px; height: 30px; top: 80px;">
						<div style="width: 300px; color: gray;">失效时间</div>
						<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${u.expireTime}</b></div>
						<div style="height: 1px; width: 600px; background: #D5D5D5; position: relative; top: -20px;"></div>
					</div>
					<div style="position: relative; left: 40px; height: 30px; top: 100px;">
						<div style="width: 300px; color: gray;">允许访问IP</div>
						<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${u.allowIps}</b></div>
						<div style="height: 1px; width: 600px; background: #D5D5D5; position: relative; top: -20px;"></div>
					</div>
					<div style="position: relative; left: 40px; height: 30px; top: 120px;">
						<div style="width: 300px; color: gray;">锁定状态</div>
						<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${u.lockState}</b></div>
						<div style="height: 1px; width: 600px; background: #D5D5D5; position: relative; top: -20px;"></div>
					</div>
					<div style="position: relative; left: 40px; height: 30px; top: 140px;">
						<div style="width: 300px; color: gray;">职位</div>
						<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${u.privileges}</b></div>
						<div style="height: 1px; width: 600px; background: #D5D5D5; position: relative; top: -20px;"></div>
						<button style="position: relative; left: 76%; top: -40px;" type="button" class="btn btn-default" id="editPwdBtn"><span class="glyphicon glyphicon-edit"></span> 修改密码</button>
						<button style="position: relative; left: 76%; top: -40px;" type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
					</div>
				</div>
			</div>
			<div class="tab-pane fade" id="permission-info">
				<div style="position: relative; top: 20px; left: 0px;">
					<ul id="treeDemo" class="ztree" style="position: relative; top: 15px; left: 15px;"></ul>
					<div style="position: relative;top: 30px; left: 76%;">
						<button type="button" class="btn btn-default" data-toggle="modal" data-target="#assignRoleForUserModal"><span class="glyphicon glyphicon-edit"></span> 分配角色</button>
					</div>
				</div>
			</div>
		</div>
	</div>	
	
</body>
</html>