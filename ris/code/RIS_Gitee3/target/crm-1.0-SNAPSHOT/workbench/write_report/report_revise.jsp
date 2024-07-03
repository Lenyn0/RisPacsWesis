<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
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
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <script type="text/javascript">

        $(function(){

            /*$("#create-diseaseName").mousemove(function (){
                var body = $("#create-bodyPart").text();
                //通过走后台的方式获得一个标签的值
                $.ajax({
                    url : "workbench/Report/get_bodypart_from_Session.do",
                    data : {
                        "body" : body
                    },
                    type : "get",
                    dataType : "json",
                    async: true,
                    success : function (data) {
                        var html = "<option></option>";
                        $.each(data,function(i,n){
                            html += "<option value='"+n.name+"'>"+n.name+"</option>";
                            $("#create-diseaseName").html(html);
                        })
                    }
                })

            });*/
            $("#create-diseaseName").change(function (){
                var name = $("#create-diseaseName").children('option:selected').val();
                //通过走后台的方式获得一个标签的值
                $.ajax({
                    url : "workbench/Report/get_diseaseDescription.do",
                    data : {
                        "name" : name
                    },
                    type : "get",
                    dataType : "text",
                    async: true,
                    success : function (data) {
                        $("#create-diseaseDescription").html(data);

                    }

                })

            });
            //从后台取数据铺值到报告表格中
            //疾病描述是一对一的关系，后期优化可以直接写在配置文件里面。

            //为保存按钮添加事件，执行添加操作
            $("#saveBtn").click(function(){

                //发出传统请求，提交表单
                if($("#create-diseaseName").html()==""||$("#create-diseaseName").html()==null){
                    alert("请填写疾病名称");
                    return false;
                }
                if($("#create-bodyPart").html()==""||$("#create-bodyPart").html()==null){
                    alert("请填写疾病部位");
                    return false;
                }
                if($("#create-positive").html()==""||$("#create-positive").html()==null){
                    alert("请填写是否阳性");
                    return false;
                }
                if($("#create-diseaseDescription").val()==""||$("#create-diseaseDescription").val()==null){
                    alert("请填写疾病描述");
                    return false;
                }
                if($("#create-imagingFindings").val()==""||$("#create-imagingFindings").val()==null){
                    alert("请填写影像学所见");
                    return false;
                }
                if($("#create-diseaseDescription").val()==""||$("#create-diseaseDescription").val()==null){
                    alert("请填写诊断意见");
                    return false;
                }

                $.ajax({

                    url : "workbench/Report/save.do",
                    data : {
                        "id" : $("#create-id").text(),
                        "studyID" :$("#create-studyID").text(),
                        "patientID" :$("#create-patientID").text(),
                        "createUserID" :$("#create-createUserID").val(),
                        "auditorID" :$("#create-auditorID").val(),
                        "imagingFindings" : $("#create-imagingFindings").val(),
                        "diagnosticOpinion" : $("#create-diagnosticOpinion").val(),
                        "bodyPart" : $("#create-bodyPart").text(),
                        "diseaseName" :$("#create-diseaseName").val(),
                        "diseaseDescription" : $("#create-diseaseDescription").val(),
                        "positive" : $("#create-positive").val(),
                        "flag" : true//用来判断是修改还是删除
                    },
                    type : "post",
                    dataType : "json",
                    async: true,
                    success : function (data) {
                        /*tianjia*/
                        if(data.success)
                        {
                            //添加成功之后，列表少一条记录，局部刷新列表（还没有实现），跳回填写报告页面
                            alert("提交成功");
                            window.location.href = "workbench/write_report/index.jsp";

                        }
                        else
                        {
                            alert("提交失败");
                        }

                    }
                })

            })

        });

        $(document).ready(function(){

            //从index.jsp界面获取检查号和病人号和病人名称以及报告医生
            const db = window.localStorage;
            const studyID = db.getItem("studyID");
            const patientID = db.getItem("patientID");
            const name = db.getItem("name");

            //获取传过来的值
            //从index.jsp界面获取检查号和病人号和病人名
            $("#create-studyID").html(studyID);
            $("#create-patientID").html(patientID);
            $("#name").html(name);
            //清除缓存


            //从数据库获得相关数据的值
            $.ajax({
                url : "workbench/Report/get_report0.do",
                data : {
                    "studyID": studyID
                },
                type : "get",
                dataType : "json",
                /*async: true,*/
                async: false, /*这里设置为false*/
                success : function (data) {
                    $.ajax({
                        url : "workbench/Report/get_bodypart_from_Session.do",
                        data : {
                            "body" : data.bodyPart
                        },
                        type : "get",
                        dataType : "json",
                        async: false, /*这里设置为false*/
                        success : function (data1) {
                            var html = "<option></option>";
                            $.each(data1,function(i,n){
                                html += "<option value='"+n.name+"'>"+n.name+"</option>";
                            })
                            $("#create-diseaseName").html(html);
                        }
                    })

                    /*alert(data.diseaseName);*/
                    $("#create-id").html(data.id);
                    $("#create-diseaseDescription").html(data.diseaseDescription);
                    $("#create-imagingFindings").html(data.imagingFindings);
                    $("#create-diagnosticOpinion").html(data.diagnosticOpinion);
                    $("#create-bodyPart").html(data.bodyPart);
                    $("#create-diseaseName").val(data.diseaseName);
                    $("#create-positive").val(data.positive);
                    /*疾病描述，影像学所见，诊断意见
                    create-diseaseDescription
                    create-imagingFindings
                    create-diagnosticOpinion*/
                }

            })

            //获得病人的年龄，性别
            //获得病人号，病人名，年龄，性别
            $.ajax({
                url : "workbench/Report/get_age_gender.do",
                data : {
                    "patientID" : patientID
                },
                type : "get",
                dataType : "json",
                async: true,
                success : function (data) {
                    /*alert(data);*/
                    $("#age").html(data.age);
                    $("#gender").html(data.gender);
                }

            })

            //获得投照方式，使用耗材，计划的程序步骤的相关描述
            //id="projection"使用耗材useConsumables,scheduledProcedure-StepDescription
            $.ajax({
                url : "workbench/Report/get_data_from_studyInfo.do",
                data : {
                    "studyID" : studyID
                },
                type : "get",
                dataType : "json",
                async: true,
                success : function (data) {
                    $("#projection").html(data.projection);
                    $("#useConsumables").html(data.useConsumables);
                    $("#scheduledProcedureStepDescription").html(data.scheduledProcedureStepDescription);
                }

            })

            //清除缓存
            db.removeItem('id');
            db.removeItem('studyID');
            db.removeItem('patientID');
            db.removeItem('name');
        });

        $(document).ready(function() {
            // 定时器每5秒向Servlet发送异步请求
            setInterval(function() {
                $.ajax({
                    url: "workbench/Report/sent_python_message.do",
                    type: "get",
                    dataType: "text",
                    success: function(data) {
                        if (data.trim() !== '') { // 检查收到的数据是否不为空
                            $("#create-imagingFindings").html(data); // 更新页面上的消息内容
                        }
                    }
                });
            }, 5000); // 每5秒执行一次
        });

    </script>


</head>
<body>

<!-- 填写报告表单 -->

<div style="position:  relative; left: 10%;">
    <h3>填写报告</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" class="btn btn-primary" id="saveBtn">提交</button>
        <button type="button" class="btn btn-default" onclick="window.location.href = 'workbench/write_report/index.jsp';">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
    <div class="form-group">
        <label  class="col-sm-2 control-label">报告号</label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="create-id" disabled></text>
        </div>
        <label  class="col-sm-2 control-label">检查号</label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="create-studyID" disabled></text>
        </div>
    </div>

    <div class="form-group">
        <label  class="col-sm-2 control-label">病人号</label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="create-patientID" disabled></text>
        </div>

        <label  class="col-sm-2 control-label">病人名</label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="name" disabled></text>
        </div>
    </div>

    <div class="form-group">
        <label  class="col-sm-2 control-label">病人年龄</label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="age" disabled></text>
        </div>

        <label  class="col-sm-2 control-label">病人性别</label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="gender" disabled></text>
        </div>
    </div>

    <div class="form-group">
        <label  class="col-sm-2 control-label">疾病部位<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text class="form-control" id="create-bodyPart" disabled></text>
                <%--<option></option>
                <c:forEach items="${bodyPartsList}" var="t">
                    <option value="${t.value}">${t.text}</option>
                </c:forEach>--%>
        </div>
        <label class="col-sm-2 control-label">疾病名称<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-diseaseName">
                <option></option>
                <option>肝硬化</option>
                <option>否</option>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label for="create-createUserID" class="col-sm-2 control-label">报告医生</label>
        <div class="col-sm-10" style="width: 300px;">
            <span class="form-control"  id="create-createUserID" style="text-align: left;">${user.name}</span>
        </div>
        <label for="create-positive" class="col-sm-2 control-label">是否阳性<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-positive">
                <option></option>
                <option>是</option>
                <option>否</option>
            </select>
        </div>
    </div>
    <%--投照方式projection	 使用耗材useConsumables--%>
    <div class="form-group">
        <label  class="col-sm-2 control-label">投照方式</label>
        <div class="col-sm-10" style="width: 300px;">
            <text class="form-control"  id="projection" style="text-align: left;" disabled></text>
        </div>
        <label  class="col-sm-2 control-label">使用耗材</label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="useConsumables" disabled></text>
        </div>
    </div>

    <%--计划的程序步骤的相关描述--%>
    <div class="form-group">
        <label  class="col-sm-2 control-label">计划的程序步骤的相关描述</label>
        <div class="col-sm-10" style="width: 70%;">
            <text class="form-control" rows="3" id="scheduledProcedureStepDescription" disabled></text>
        </div>
    </div>

    <div class="form-group">
        <label for="create-diseaseDescription" class="col-sm-2 control-label">疾病描述<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="create-diseaseDescription"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label  class="col-sm-2 control-label">影像学所见<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="create-imagingFindings" placeholder="请输入内容"></textarea>
        </div>
    </div>


    <div class="form-group">
        <label for="create-diagnosticOpinion" class="col-sm-2 control-label">诊断意见<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea type="text" class="form-control" id="create-diagnosticOpinion" placeholder="请输入内容"></textarea>
        </div>
    </div>

</form>
</body>
</html>