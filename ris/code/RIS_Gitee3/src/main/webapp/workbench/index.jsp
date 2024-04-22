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
	//页面加载完毕
	$(function(){
		//导航中所有文本颜色为黑色
		$(".liClass > a").css("color" , "black");
		//默认选中导航菜单中的第一个菜单项
		$(".liClass:first").addClass("active");
		//第一个菜单项的文字变成白色
		$(".liClass:first > a").css("color" , "white");
		//给所有的菜单项注册鼠标单击事件
		$(".liClass").click(function(){
			//移除所有菜单项的激活状态
			$(".liClass").removeClass("active");
			//导航中所有文本颜色为黑色
			$(".liClass > a").css("color" , "black");
			//当前项目被选中
			$(this).addClass("active");
			//当前项目颜色变成白色
			$(this).children("a").css("color","white");
		});
		//在页面加载完毕后，在工作区打开相应的页面
		window.open("workbench/main/index.jsp","workareaFrame");
		$("#personaldata").click(function showpersonaldata(){
			var html = "";
			if("${user.privileges}".indexOf("1")!=-1){
				html += "登记员,";
			}
			if("${user.privileges}".indexOf("2")!=-1){
				html += "检查技师,";
			}
			if("${user.privileges}".indexOf("3")!=-1){
				html += "报告医生,";
			}
			if("${user.privileges}".indexOf("4")!=-1){
				html += "报告审核医生,";
			}
			if("${user.privileges}".indexOf("6")!=-1){
				html += "科室主任,";
			}
			if("${user.privileges}".indexOf("5")!=-1){
				html += "系统管理员,";
			}
			html = html.slice(0,html.length-1);
			$("#showprivileges").html(html);
			$("#myInformation").modal("show");
		})
        $("#oldPwd").on("input propertychange",function(){
            if($("#oldPwd").val()==""){
                $("#msg").html("原密码不能为空");
                $("#editPwdBtn").attr("disabled",true);
            }else if($("#oldPwd").val()!=""&&$("#newPwd").val()!=""&&$("#confirmPwd").val()!=""&&$("#newPwd").val()==$("#confirmPwd").val()){
				$("#msg").html("");
				$("#editPwdBtn").attr("disabled",false);
			}else{
				$("#msg").html("");
				$("#editPwdBtn").attr("disabled",true);
			}
        })
		$("#newPwd").on("input propertychange",function(){
			if($("#newPwd").val()==""){
				$("#msg").html("新密码不能为空");
				$("#editPwdBtn").attr("disabled",true);
			}else if($("#oldPwd").val()!=""&&$("#newPwd").val()!=""&&$("#confirmPwd").val()!=""&&$("#newPwd").val()==$("#confirmPwd").val()){
				$("#msg").html("");
				$("#editPwdBtn").attr("disabled",false);
			}else{
				$("#msg").html("");
				$("#editPwdBtn").attr("disabled",true);
			}
		})
		$("#confirmPwd").on("input propertychange",function(){
			if($("#oldPwd").val()!=""&&$("#newPwd").val()!=""&&$("#confirmPwd").val()!=""&&$("#newPwd").val()==$("#confirmPwd").val()){
				$("#msg").html("");
				$("#editPwdBtn").attr("disabled",false);
			}else if($("#newPwd").val()!=$("#confirmPwd").val()){
                $("#msg").html("两次密码不一致");
                $("#editPwdBtn").attr("disabled",true);
            }
		})
		$("#editPwdBtn").click(function(){
            var oldPwd = $.trim($("#oldPwd").val());
            var newPwd = $.trim($("#newPwd").val());
            $.ajax({
                url : "settings/user/editPwd.do",
                data : {
					"loginAct" : "${user.loginAct}",
                    "oldPwd" : oldPwd,
                    "newPwd" : newPwd
                },
                type : "post",
                dataType : "json",
                success : function (data) {
                    if(data.success){
                        //跳转到登录页
                        window.location.href = "login.jsp";
                        //如果登录失败
                    }else{
                        $("#msg").html("");
                        $("#msg").html(data.msg);
                    }
                }
            })
		})
		$("#addFaceBtn").click(function(){
			$("#msg2").html("");
			getMedia();
			$("#addFaceModal").modal("show");
		})
		$("#closeAddFaceModal").click(function(){
			closeMedia();
			$("#addFaceModal").modal("hide");
		})
		$("#cancelBtn").click(function(){
			closeMedia();
			$("#addFaceModal").modal("hide");
		})
		$("#submitFaceBtn").click(function(){
			let mainComp = $("#mainDiv");

			//获得Canvas对象
			let video = document.getElementById("video");
			let canvas = document.getElementById("canvas");
			let ctx = canvas.getContext('2d');
			ctx.drawImage(video, 0, 0, 300, 300);
			var formData = new FormData();
			var base64File = canvas.toDataURL();
			formData.append("image", base64File);
			formData.append("id", "${user.id}");
			$.ajax({
				url : "settings/user/addFace.do",
				data : formData,
				type : "post",
				contentType: false,
				processData: false,
				async: false,
				dataType : "json",
				success: function (data) {
					if (data.code == 0) {
						$("#msg2").css("color","blue");
						$("#msg2").html("人脸录入成功！");
					} else {
						$("#msg2").css("color","red");
						$("#msg2").html(data.message);
					}
				}/*,
				error: function (error) {
					alert(JSON.stringify(error))
				}*/
			})
		})
	});
	function judgeprivilege(pri,page){
		if("${user.privileges}".indexOf(pri)!=-1){
			if(pri!='5'){
			    window.open("workbench/"+page+"/index.jsp","workareaFrame");
            }else{
				window.open("settings/index.jsp","workareaFrame");
            }
		}else{
			alert("哎啊不对不对身份不对,你不能进来！");
		}
	}
	function getMedia() {
		$("#mainDiv").empty();
		let videoComp = " <video id='video' width='300px' height='300px' autoplay='autoplay' style='margin-top: 20px'></video>" +
				"<canvas id='canvas' width='300px' height='300px' style='display: none'></canvas>";
		$("#mainDiv").append(videoComp);

		let constraints = {
			video: {width: 300, height: 300},
			audio: false
		};
		//获得video摄像头区域
		let video = document.getElementById("video");
		//这里介绍新的方法，返回一个 Promise对象
		// 这个Promise对象返回成功后的回调函数带一个 MediaStream 对象作为其参数
		// then()是Promise对象里的方法
		// then()方法是异步执行，当then()前的方法执行完后再执行then()内部的程序
		// 避免数据没有获取到
		let promise = navigator.mediaDevices.getUserMedia(constraints);
		promise.then(function (MediaStream) {
			video.srcObject = MediaStream;
			video.play();
		});
	}
	function closeMedia(){
		let video = document.getElementById("video");
		let tracks = video.srcObject.getTracks();
		$.each(tracks,function(i,v){
			v.stop();
		})
	}
</script>

</head>
<body>

	<!-- 我的资料 -->
	<div class="modal fade" id="myInformation" role="dialog">
		<div class="modal-dialog" role="document" style="width: 35%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">我的资料</h4>
				</div>
				<div class="modal-body">
					<div style="position: relative; left: 40px;">
						姓名：<b>${user.name}</b><br><br>
						登录帐号：<b>${user.loginAct}</b><br><br>
						身份：<b id="showprivileges"></b><br><br>
						邮箱：<b>${user.email}</b><br><br>
						失效时间：<b>${user.expireTime}</b><br><br>
						允许访问IP：<b>${user.allowIps}</b>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改密码的模态窗口 -->
	<div class="modal fade" id="editPwdModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 70%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">修改密码</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<div class="form-group">
							<label for="oldPwd" class="col-sm-2 control-label">原密码</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" autocomplete="off" id="oldPwd" style="width: 200%;">
							</div>
						</div>

						<div class="form-group">
							<label for="newPwd" class="col-sm-2 control-label">新密码</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" autocomplete="off" id="newPwd" style="width: 200%;">
							</div>
						</div>

						<div class="form-group">
							<label for="confirmPwd" class="col-sm-2 control-label">确认密码</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" autocomplete="off" id="confirmPwd" style="width: 200%;">
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
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="editPwdBtn" disabled="disabled">修改</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 录入人脸的模态窗口 -->
	<div class="modal fade" id="addFaceModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 21.7%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" id="closeAddFaceModal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">录入人脸</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<div id="mainDiv"><%--人脸识别的图像采集区域--%></div>
					</form>
					<label for="msg" class="col-sm-2 control-label"></label>
					<div class="col-sm-10" style="width: 300px;">
						<span id="msg2"></span>
					</div>
					<br>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" id="cancelBtn">取消</button>
					<button type="button" class="btn btn-primary" id="submitFaceBtn">录入</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 退出系统的模态窗口 -->
	<div class="modal fade" id="exitModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 30%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">离开</h4>
				</div>
				<div class="modal-body">
					<p>您确定要退出系统吗？</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="window.location.href='login.jsp';">确定</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 顶部 -->
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">RIS &nbsp;<span style="font-size: 12px;">&copy;2024&nbsp;北京邮电大学</span></div>
		<div style="position: absolute; top: 15px; right: 15px;">
			<ul>
				<li class="dropdown user-dropdown">
					<a href="javascript:void(0)" style="text-decoration: none; color: white;" class="dropdown-toggle" data-toggle="dropdown">
						<span class="glyphicon glyphicon-user"></span> ${user.name} <span class="caret"></span>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</a>
					<ul class="dropdown-menu">
						<%--<li><a href="settings/index.jsp"><span class="glyphicon glyphicon-wrench"></span> 系统设置</a></li>--%>
						<li><a href="javascript:void(0)" id="personaldata"><span class="glyphicon glyphicon-file"></span> 我的资料</a></li>
						<li><a href="javascript:void(0)" data-toggle="modal" data-target="#editPwdModal"><span class="glyphicon glyphicon-edit"></span> 修改密码</a></li>
						<li><a href="javascript:void(0)" id="addFaceBtn"><span class="glyphicon glyphicon-camera"></span> 录入人脸</a></li>
						<li><a href="javascript:void(0);" data-toggle="modal" data-target="#exitModal"><span class="glyphicon glyphicon-off"></span> 退出</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
	
	<!-- 中间 -->
	<div id="center" style="position: absolute;top: 50px; bottom: 30px; left: 0px; right: 0px;">
	
		<!-- 导航 -->
		<div id="navigation" style="left: 0px; width: 18%; position: relative; height: 100%; overflow:auto;">

			<ul id="no1" class="nav nav-pills nav-stacked">
				<li class="liClass"><a href="workbench/main/index.jsp" target="workareaFrame"><span class="glyphicon glyphicon-home"></span> 工作台</a></li>
				<li class="liClass"><a href="javascript:void(0);" onclick="judgeprivilege('1','register')" target="workareaFrame"><span class="glyphicon glyphicon-tag"></span> 登记</a></li>
				<li class="liClass"><a href="javascript:void(0);" onclick="judgeprivilege('1','sign_in')" target="workareaFrame"><span class="glyphicon glyphicon-check"></span> 签到</a></li>
				<li class="liClass"><a href="javascript:void(0);" onclick="judgeprivilege('2','check')" target="workareaFrame"><span class="glyphicon glyphicon-asterisk"></span> 进行检查</a></li>
				<li class="liClass"><a href="javascript:void(0);" onclick="judgeprivilege('3','write_report')" target="workareaFrame"><span class="glyphicon glyphicon-edit"></span> 填写报告</a></li>
				<li class="liClass"><a href="javascript:void(0);" onclick="judgeprivilege('4','report_review')" target="workareaFrame"><span class="glyphicon glyphicon-ok-sign"></span> 报告审查</a></li>
				<li class="liClass"><%--<a href="javascript:void(0);" onclick="judgeprivilege('6','statistical_chart')" target="workareaFrame"><span class="glyphicon glyphicon-stats"></span> 统计图表</a>
				--%><a href="#no2" class="collapsed" data-toggle="collapse"><span class="glyphicon glyphicon-stats"></span> 统计图表</a>
					<ul id="no2" class="nav nav-pills nav-stacked collapse">
						<li class="liClass"><a href="javascript:void(0);" onclick="judgeprivilege('6','chart/annual_positive_rate')" target="workareaFrame">&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-right"></span> 年度阳性率统计报表</a></li>
						<li class="liClass"><a href="javascript:void(0);" onclick="judgeprivilege('6','chart/diagnostician_wordload')" target="workareaFrame">&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-right"></span> 诊断医生工作量统计报表</a></li>
						<li class="liClass"><a href="javascript:void(0);" onclick="judgeprivilege('6','chart/equipment_workload')" target="workareaFrame">&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-right"></span> 设备工作量统计报表</a></li>
						<li class="liClass"><a href="javascript:void(0);" onclick="judgeprivilege('6','chart/technician_workload')" target="workareaFrame">&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-right"></span> 检查技师工作量统计报表</a></li>
						<li class="liClass"><a href="javascript:void(0);" onclick="judgeprivilege('6','chart/auditor_workload')" target="workareaFrame">&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-right"></span> 审核医生工作量统计报表</a></li>
						<li class="liClass"><a href="javascript:void(0);" onclick="judgeprivilege('6','chart/clinicianID_workload')" target="workareaFrame">&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-right"></span> 登记员工作量统计报表</a></li>
					</ul>
				</li>
				<li class="liClass"><%--<a href="javascript:void(0);" onclick="judgeprivilege('6','statistical_chart')" target="workareaFrame"><span class="glyphicon glyphicon-stats"></span> 统计图表</a>
				--%><a href="#no3" class="collapsed" data-toggle="collapse"><span class="glyphicon glyphicon-list"></span> 字典维护</a>
					<ul id="no3" class="nav nav-pills nav-stacked collapse">
						<li class="liClass"><a href="javascript:void(0);" onclick="judgeprivilege('6','dictionary_managed/value')" target="workareaFrame">&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-right"></span> 身体部位和模式字典管理</a></li>
						<li class="liClass"><a href="javascript:void(0);" onclick="judgeprivilege('6','dictionary_managed/disease')" target="workareaFrame">&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-right"></span> 疾病字典管理</a></li>
					</ul>
				</li>
                <li class="liClass"><a href="javascript:void(0);" onclick="judgeprivilege('5','sys_config')" target="workareaFrame"><span class="glyphicon glyphicon-cog"></span> 系统配置</a></li>
				<%--<li class="liClass"><a href="javascript:void(0);" target="workareaFrame"><span class="glyphicon glyphicon-tag"></span> 动态</a></li>
                <li class="liClass"><a href="javascript:void(0);" target="workareaFrame"><span class="glyphicon glyphicon-time"></span> 审批</a></li>
                <li class="liClass"><a href="javascript:void(0);" target="workareaFrame"><span class="glyphicon glyphicon-user"></span> 客户公海</a></li>
                <li class="liClass"><a href="workbench/activity/index.jsp" target="workareaFrame"><span class="glyphicon glyphicon-play-circle"></span> 市场活动</a></li>
                <li class="liClass"><a href="workbench/clue/index.jsp" target="workareaFrame"><span class="glyphicon glyphicon-search"></span> 线索（潜在客户）</a></li>
                <li class="liClass"><a href="workbench/customer/index.jsp" target="workareaFrame"><span class="glyphicon glyphicon-user"></span> 客户</a></li>
                <li class="liClass"><a href="workbench/contacts/index.jsp" target="workareaFrame"><span class="glyphicon glyphicon-earphone"></span> 联系人</a></li>
                <li class="liClass"><a href="workbench/transaction/index.jsp" target="workareaFrame"><span class="glyphicon glyphicon-usd"></span> 交易（商机）</a></li>
                <li class="liClass"><a href="visit/index.jsp" target="workareaFrame"><span class="glyphicon glyphicon-phone-alt"></span> 售后回访</a></li>
                <li class="liClass">
                    <a href="#no2" class="collapsed" data-toggle="collapse"><span class="glyphicon glyphicon-stats"></span> 统计图表</a>
                    <ul id="no2" class="nav nav-pills nav-stacked collapse">
                        <li class="liClass"><a href="chart/activity/index.jsp" target="workareaFrame">&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-right"></span> 市场活动统计图表</a></li>
                        <li class="liClass"><a href="chart/clue/index.jsp" target="workareaFrame">&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-right"></span> 线索统计图表</a></li>
                        <li class="liClass"><a href="chart/customerAndContacts/index.jsp" target="workareaFrame">&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-right"></span> 客户和联系人统计图表</a></li>
                        <li class="liClass"><a href="workbench/chart/transaction/index.jsp" target="workareaFrame">&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-chevron-right"></span> 交易统计图表</a></li>
                    </ul>
                </li>
                <li class="liClass"><a href="javascript:void(0);" target="workareaFrame"><span class="glyphicon glyphicon-file"></span> 报表</a></li>
                <li class="liClass"><a href="javascript:void(0);" target="workareaFrame"><span class="glyphicon glyphicon-shopping-cart"></span> 销售订单</a></li>
                <li class="liClass"><a href="javascript:void(0);" target="workareaFrame"><span class="glyphicon glyphicon-send"></span> 发货单</a></li>
                <li class="liClass"><a href="javascript:void(0);" target="workareaFrame"><span class="glyphicon glyphicon-earphone"></span> 跟进</a></li>
                <li class="liClass"><a href="javascript:void(0);" target="workareaFrame"><span class="glyphicon glyphicon-leaf"></span> 产品</a></li>
                <li class="liClass"><a href="javascript:void(0);" target="workareaFrame"><span class="glyphicon glyphicon-usd"></span> 报价</a></li>--%>
			</ul>
			
			<!-- 分割线 -->
			<div id="divider1" style="position: absolute; top : 0px; right: 0px; width: 1px; height: 100% ; background-color: #B3B3B3;"></div>
		</div>
		
		<!-- 工作区 -->
		<div id="workarea" style="position: absolute; top : 0px; left: 18%; width: 82%; height: 100%;">
			<iframe style="border-width: 0px; width: 100%; height: 100%;" name="workareaFrame"></iframe>
		</div>
		
	</div>
	
	<div id="divider2" style="height: 1px; width: 100%; position: absolute;bottom: 30px; background-color: #B3B3B3;"></div>
	
	<!-- 底部 -->
	<div id="down" style="height: 30px; width: 100%; position: absolute;bottom: 0px;"></div>
	
</body>
</html>