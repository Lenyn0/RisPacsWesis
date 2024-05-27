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

	<script>
		$(function () {
			if(window.top!=window){
				window.top.location=window.location;
			}
			//页面加载完毕后，将用户文本框中的内容清空
			$("#loginAct").val("");
			//页面加载完毕后，让用户的文本框自动获得焦点
			$("#loginAct").focus();
			//为登录按钮绑定事件，执行登录操作
			$("#submitBtn").click(function () {
				login();
			})
			//为当前登录也窗口绑定敲键盘事件
			//event:这个参数可以取得我们敲的是哪个键
			$(window).keydown(function (event) {
				//alert(event.keyCode);
				//如果取得的键位的码值为13，表示敲的是回车键
				if(event.keyCode==13){
					login();
				}
			})
			$("#FaceLoginBtn").click(function(){
				$("#msg2").html("");
				getMedia();
				$("#FaceLoginModal").modal("show");
				faceLogin.alarm_Timer = setInterval(faceLogin, 2000);
			})
			$("#closeFaceLoginModal").click(function(){
				clearInterval(faceLogin.alarm_Timer);
				closeMedia();
				$("#FaceLoginModal").modal("hide");
			})
			$("#cancelBtn").click(function(){
				clearInterval(faceLogin.alarm_Timer);
				closeMedia();
				$("#FaceLoginModal").modal("hide");
			})
			/*$("#submitFaceBtn").click(function(){
				//获得Canvas对象
				let video = document.getElementById("video");
				let canvas = document.getElementById("canvas");
				let ctx = canvas.getContext('2d');
				ctx.drawImage(video, 0, 0, 300, 300);
				var formData = new FormData();
				var base64File = canvas.toDataURL();
				formData.append("image", base64File);
				$.ajax({
					url : "settings/user/faceLogin.do",
					data : formData,
					type : "post",
					contentType: false,
					processData: false,
					async: false,
					dataType : "json",
					success: function (data) {
						if (data.code == 0) {
							clearInterval(faceLogin.alarm_Timer);
							window.location.href = "workbench/index.jsp";
						} else {
							$("#msg2").html(data.message);
						}
					}/!*,
					error: function (error) {
						alert(JSON.stringify(error))
					}*!/
				})
			})*/
		})
		//普通的自定义的function方法，一定要写在$(function(){})的外面
		function login() {
			//alert("登录操作123");
			//验证账号密码不能为空
			//取得账号密码
			//将文本中的左右空格去掉，使用$.trim(文本)
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());
			if(loginAct=="" || loginPwd==""){
				$("#msg").html("账号密码不能为空");
				//如果账号密码为空，则需要及时强制终止该方法
				return false;
			}
			//去后台验证登录相关操作
			$.ajax({
				url : "settings/user/login.do",
				data : {
					"loginAct" : loginAct,
					"loginPwd" : loginPwd
				},
				type : "post",
				dataType : "json",
				success : function (data) {
					/*
						data
							{"success":true/false,"msg":"哪错了"}
					 */
					//如果登录成功
					if(data.success){
						//跳转到工作台的初始页（欢迎页）
						window.location.href = "workbench/index.jsp";
						//如果登录失败
					}else{
						$("#msg").html(data.msg);
					}
				}
			})
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
		function faceLogin(){
			//获得Canvas对象
			let video = document.getElementById("video");
			let canvas = document.getElementById("canvas");
			let ctx = canvas.getContext('2d');
			ctx.drawImage(video, 0, 0, 300, 300);
			var formData = new FormData();
			var base64File = canvas.toDataURL();
			formData.append("image", base64File);
			$.ajax({
				url : "settings/user/faceLogin.do",
				data : formData,
				type : "post",
				contentType: false,
				processData: false,
				async: false,
				dataType : "json",
				success: function (data) {
					if (data.code == 0) {
						clearInterval(faceLogin.alarm_Timer);
						window.location.href = "workbench/index.jsp";
					} else {
						$("#msg2").html(data.message);
					}
				}
			})
		}
	</script>

</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
	<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
	<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">RIS &nbsp;<span style="font-size: 12px;">&copy;2024&nbsp;北京邮电大学</span></div>
</div>

<!-- 人脸登录的模态窗口 -->
<div class="modal fade" id="FaceLoginModal" role="dialog">
	<div class="modal-dialog" role="document" style="width: 21.7%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" id="closeFaceLoginModal">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title">人脸识别登录</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" role="form">
					<div id="mainDiv"><%--人脸识别的图像采集区域--%></div>
				</form>
				<label for="msg" class="col-sm-2 control-label"></label>
				<div class="col-sm-10" style="width: 300px;">
					<span id="msg2" style="color: red"></span>
				</div>
				<br>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" id="cancelBtn">取消</button>
				<%--<button type="button" class="btn btn-primary" id="submitFaceBtn">录入</button>--%>
			</div>
		</div>
	</div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
	<div style="position: absolute; top: 0px; right: 60px;">
		<div class="page-header">
			<h1>登录</h1>
		</div>
		<form action="workbench/index.jsp" class="form-horizontal" role="form">
			<div class="form-group form-group-lg">
				<div style="width: 350px;">
					<input class="form-control" type="text" placeholder="用户名" id="loginAct">
				</div>
				<div style="width: 350px; position: relative;top: 20px;">
					<input class="form-control" type="password" placeholder="密码" id="loginPwd">
				</div>
				<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
					<span id="msg" style="color: red"></span>
				</div>
				<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
<%--				<button type="button" id="FaceLoginBtn" class="btn btn-link btn-lg btn-block"  style="width: 125px; left:225px; position: relative;top: 45px;">人脸登录</button>--%>
			</div>
		</form>
	</div>
</div>
</body>
</html>