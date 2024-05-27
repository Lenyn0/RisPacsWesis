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

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">
	$(function(){
		pageList(1,5);
		$("#searchBtn").click(function () {
			/*点击查询按钮的时候，我们应该将搜索框中的信息保存起来,保存到隐藏域中*/
			$("#hidden-username").val($.trim($("#search-username").val()));
			$("#hidden-lockState").val($.trim($("#search-lockState").val()));
			$("#hidden-startTime").val($.trim($("#search-startTime").val()));
			$("#hidden-endTime").val($.trim($("#search-endTime").val()));
			pageList(1,5);
		})
		$(".time").datetimepicker({
			minView: "hour",
			language:  'zh-CN',
			format: 'yyyy-mm-dd hh:ii:ss',
			autoclose: true,
			todayBtn: true,
			startDate: new Date(),
			pickerPosition: "bottom-right"
		});
		$("#create-loginAct").on("input propertychange",function(){
			if($("#create-loginAct").val()==""){
				$("#msg").html("账号不能为空");
				$("#saveBtn").attr("disabled",true);
			}else{
				$("#msg").html("");
				$("#saveBtn").attr("disabled",true);
			}
		})
		$("#create-loginPwd").on("input propertychange",function(){
			if($("#create-loginPwd").val()==""){
				$("#msg").html("登录密码不能为空");
				$("#saveBtn").attr("disabled",true);
			}else{
				$("#msg").html("");
				$("#saveBtn").attr("disabled",true);
			}
		})
		$("#create-confirmPwd").on("input propertychange",function(){
			if($("#create-loginPwd").val()==$("#create-confirmPwd").val()&&$("#create-loginPwd").val()!=""){
				$("#msg").html("");
				$("#saveBtn").attr("disabled",false);
			}else if($("#create-loginPwd").val()!=$("#create-confirmPwd").val()){
				$("#msg").html("两次密码不一致");
				$("#saveBtn").attr("disabled",true);
			}
		})
		$("#saveBtn").click(function(){
			var $privileges = $("input[name=privilege]:checked");
			var str = "";
			for(var i=0;i<$privileges.length;i++){
				str += $($privileges[i]).val();
				//如果不是最后一个元素，需要在后面追加一个,符
				if(i<$privileges.length-1){
					str += ",";
				}
			}
			$("#create-privileges").val(str);
			if($("#create-privileges").val()==""){
				return false;
			}
			$("#userForm").submit();
		})
		//为全选的复选框绑定事件，触发全选操作
		$("#qx").click(function () {
			$("input[name=xz]").prop("checked",this.checked);
		})
		$("#userBody").on("click",$("input[name=xz]"),function () {
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		})
		$("#deleteBtn").click(function () {
			//找到复选框中所有挑√的复选框的jquery对象
			var $xz = $("input[name=xz]:checked");
			if($xz.length==0){
				alert("请选择需要删除的用户");
				//肯定选了，而且有可能是1条，有可能是多条
			}else{
				if(confirm("确定删除所选中的用户吗？")){
					//url:workbench/activity/delete.do?id=xxx&id=xxx&id=xxx
					//拼接参数
					var param = "";
					//将$xz中的每一个dom对象遍历出来，取其value值，就相当于取得了需要删除的记录的id
					for(var i=0;i<$xz.length;i++){
						param += "id="+$($xz[i]).val();
						//如果不是最后一个元素，需要在后面追加一个&符
						if(i<$xz.length-1){
							param += "&";
						}
					}
					$.ajax({
						url : "settings/user/delete.do",
						data : param,
						type : "post",
						dataType : "json",
						success : function (data) {
							if(data.success){
								//删除成功后
								//回到第一页，维持每页展现的记录数
								pageList(1,$("#userPage").bs_pagination('getOption', 'rowsPerPage'));
							}else{
								alert("删除用户失败");
							}
						}
					})
				}
			}
		})
	})
	function pageList(pageNo,pageSize) {
		//将全选的复选框的√干掉
		$("#qx").prop("checked",false);
		//查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中
		$("#search-username").val($.trim($("#hidden-username").val()));
		$("#search-lockState").val($.trim($("#hidden-lockState").val()));
		$("#search-startTime").val($.trim($("#hidden-startTime").val()));
		$("#search-endTime").val($.trim($("#hidden-endTime").val()));
		$.ajax({
			url : "settings/user/pageList.do",
			data : {
				"pageNo" : pageNo,
				"pageSize" : pageSize,
				"username" : $.trim($("#search-username").val()),
				"lockState" : $.trim($("#search-lockState").val()),
				"startTime" : $.trim($("#search-startTime").val()),
				"endTime" : $.trim($("#search-endTime").val())
			},
			type : "get",
			dataType : "json",
			success : function (result) {
				var data = result.data;
				var html = "";
				//每一个n就是每一个登记对象
				$.each(data.dataList,function (i,n) {
					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'settings/user/detail.do?id='+n.id+'\';">'+n.loginAct+'</a></td>';
					html += '<td>'+n.name+'</td>';
					html += '<td>'+n.email+'</td>';
					html += '<td>'+n.expireTime+'</td>';
					html += '<td>'+n.allowIps+'</td>';
					if(n.lockState=="1"){
						const str1 = "启用"
						html += '<td>'+str1+'</td>';
					}else if(n.lockState=="0"){
						const str2 = "锁定"
						html += '<td>'+str2+'</td>';
					}else{
						const str3 = ""
						html += '<td>'+str3+'</td>';
					}
					html += '<td>'+n.createBy+'</td>';
					html += '<td>'+n.createTime+'</td>';
					html += '<td>'+n.editBy+'</td>';
					html += '<td>'+n.editTime+'</td>';
					html += '</tr>';
				})
				$("#userBody").html(html);
				//计算总页数
				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
				//数据处理完毕后，结合分页查询，对前端展现分页信息
				$("#userPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数
					visiblePageLinks: 3, // 显示几个卡片
					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					//该回调函数时在，点击分页组件的时候触发的
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})
	}
</script>

</head>
<body>
	<%--使用隐藏域存放查询框中的数据--%>
	<input type="hidden" id="hidden-username"/>
	<input type="hidden" id="hidden-lockState"/>
	<input type="hidden" id="hidden-startTime"/>
	<input type="hidden" id="hidden-endTime"/>
	<!-- 创建用户的模态窗口 -->
	<div class="modal fade" id="createUserModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">新增用户</h4>
				</div>
				<div class="modal-body">

					<form action="settings/user/save.do" id="userForm" method="post" class="form-horizontal" role="form">

						<div class="form-group">
							<div class="col-sm-10" style="width: 300px;">
								<input type="hidden" class="form-control" id="create-createBy" name="createBy" value="${user.name}">
							</div>
						</div>
						<div class="form-group">
							<label for="create-loginAct" class="col-sm-2 control-label">登录帐号<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" autocomplete="off" id="create-loginAct" name="loginAct">
							</div>
							<label for="create-username" class="col-sm-2 control-label">用户姓名</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" autocomplete="off" id="create-username" name="username">
							</div>
						</div>
						<div class="form-group">
							<label for="create-loginPwd" class="col-sm-2 control-label">登录密码<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="password" class="form-control" autocomplete="off" id="create-loginPwd" name="loginPwd">
							</div>
							<label for="create-confirmPwd" class="col-sm-2 control-label">确认密码<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="password" class="form-control" autocomplete="off" id="create-confirmPwd" name="confirmPwd">
							</div>
						</div>
						<div class="form-group">
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" autocomplete="off" id="create-email" name="email">
							</div>
							<label for="create-phoneNumber" class="col-sm-2 control-label">手机号码</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" autocomplete="off" id="create-phoneNumber" name="phoneNumber">
							</div>
						</div>
						<div class="form-group">
							<label for="create-lockState" class="col-sm-2 control-label">锁定状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-lockState" name="lockState">
									<option></option>
									<option>启用</option>
									<option>锁定</option>
								</select>
							</div>
							<label for="create-expireTime" class="col-sm-2 control-label">失效时间</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" autocomplete="off" id="create-expireTime" name="expireTime">
							</div>
						</div>
						<div class="form-group">
							<label for="create-allowIps" class="col-sm-2 control-label">允许访问的IP</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" autocomplete="off" id="create-allowIps" name="allowIps" style="width: 280%" placeholder="多个用逗号隔开">
							</div>
						</div>
						<div class="form-group">
							<label for="create-privilege" class="col-sm-2 control-label">职位</label>
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
							<input type="hidden"  id="create-privileges" name="privileges">
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
					<button type="button" class="btn btn-primary" id="saveBtn" disabled="disabled">保存</button>
				</div>
			</div>
		</div>
	</div>

	<div>
		<div style="position: relative; left: 30px; top: -10px;">
			<div class="page-header">
				<h3>用户列表</h3>
			</div>
		</div>
	</div>

	<div class="btn-toolbar" role="toolbar" style="position: relative; height: 80px; left: 30px; top: -10px;">
		<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

			<div class="form-group">
				<div class="input-group">
					<div class="input-group-addon">用户姓名</div>
					<input class="form-control" type="text" id="search-username">
				</div>
			</div>
			<div class="form-group">
				<div class="input-group">
					<div class="input-group-addon">锁定状态</div>
					<select class="form-control" id="search-lockState">
						<option></option>
						<option>锁定</option>
						<option>启用</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<div class="input-group">
					<div class="input-group-addon">失效时间</div>
					<input class="form-control time" type="text" autocomplete="off" id="search-startTime">
				</div>
			</div>
			~
			<div class="form-group">
				<div class="input-group">
					<input class="form-control time" type="text" autocomplete="off" id="search-endTime">
				</div>
			</div>

			<button type="button" class="btn btn-default" id="searchBtn">查询</button>

		</form>
	</div>


	<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px; width: 110%; top: 20px;">
		<div class="btn-group" style="position: relative; top: 18%;">
			<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createUserModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
			<button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>

	</div>

	<div style="position: relative; left: 30px; top: 40px; width: 110%">
		<table class="table table-hover">
			<thead>
			<tr style="color: #B3B3B3;">
				<td><input type="checkbox" id="qx"/></td>
				<%--<td>序号</td>--%>
				<td>登录帐号</td>
				<td>用户姓名</td>
				<td>邮箱</td>
				<td>失效时间</td>
				<td>允许访问IP</td>
				<td>锁定状态</td>
				<td>创建者</td>
				<td>创建时间</td>
				<td>修改者</td>
				<td>修改时间</td>
			</tr>
			</thead>
			<tbody id="userBody">
			<%--<tr class="active">
				<td><input type="checkbox" /></td>
				<td>1</td>
				<td><a  href="detail.jsp">zhangsan</a></td>
				<td>张三</td>
				<td>市场部</td>
				<td>zhangsan@bjpowernode.com</td>
				<td>2017-02-14 10:10:10</td>
				<td>127.0.0.1,192.168.100.2</td>
				<td>启用</td>
				<td>admin</td>
				<td>2017-02-10 10:10:10</td>
				<td>admin</td>
				<td>2017-02-10 20:10:10</td>
			</tr>
			<tr>
				<td><input type="checkbox" /></td>
				<td>2</td>
				<td><a  href="detail.jsp">lisi</a></td>
				<td>李四</td>
				<td>市场部</td>
				<td>lisi@bjpowernode.com</td>
				<td>2017-02-14 10:10:10</td>
				<td>127.0.0.1,192.168.100.2</td>
				<td>锁定</td>
				<td>admin</td>
				<td>2017-02-10 10:10:10</td>
				<td>admin</td>
				<td>2017-02-10 20:10:10</td>
			</tr>--%>
			</tbody>
		</table>
	</div>

	<div style="height: 50px; position: relative;top: 30px; left: 30px;">
		<div id="userPage">
			<%--翻页插件--%>
		</div>
	</div>

</body>
</html>