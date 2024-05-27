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

            //从后台取数据铺值到报告表格中

            //为保存按钮添加事件，执行添加操作
            $("#saveBtn").click(function(){

                $.ajax({

                    url : "workbench/Report/save.do",
                    data : {
                        "id" : $("#creat-id").text(),
                        "studyID" :$("#creat-studyID").text(),
                        "patientID" :$("#creat-patientID").text(),
                        "createUserID" :$("#creat-createUserID").text(),
                        "auditorID" :$("#creat-auditorID").val(),
                        "imagingFindings" : $("#creat-imagingFindings").val(),
                        "diagnosticOpinion" : $("#creat-diagnosticOpinion").val(),
                        "bodyPart" : $("#creat-bodyPart").val(),
                        "diseaseName" :$("#creat-diseaseName").val(),
                        "diseaseDescription" : $("#creat-diseaseDescription").val(),
                        "positive" : $("#creat-positive").val()
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

            //获得一个随机ID
            $.ajax({
                url : "workbench/Report/get_data.do",
                data : {  },
                type : "get",
                dataType : "text",
                async: true,
                success : function (data) {
                    $("#creat-id").html(data);
                }

            })

            //获得疾病字典部位信息列表
            $.ajax({
                url : "workbench/Report/get_dictionary.do",
                data : {  },
                type : "get",
                dataType : "json",
                async: true,
                success : function (data) {
                    var html = "<option></option>";
                    $.each(data,function(i,n){

                        html += "<option value='"+n.text+"'>"+n.value+"</option>";
                        $("#creat-bodyPart").html(html);

                    })
                }
            })

            //获得疾病字典名称信息列表
            $.ajax({
                url : "workbench/Report/get_disease_dictionary.do",
                data : {  },
                type : "get",
                dataType : "json",
                async: true,
                success : function (data) {
                    var html = "<option></option>";
                    $.each(data,function(i,n){
                        html += "<option value='"+n.id+"'>"+n.name+"</option>";
                        $("#creat-diseaseName").html(html);
                    })
                }
            })

            //从index.jsp界面获取检查号和病人号和病人名称以及报告医生
            const db = window.localStorage;
            const studyID = db.getItem("studyID");

            //获取传过来的值
            //从index.jsp界面获取检查号和病人号和病人名,病人号和病人名没有办法获取正确的值，失败。
            $("#creat-studyID").html(studyID);

            //获得病人号，病人名，年龄，性别
            $.ajax({
                url : "workbench/Report/get_age_gender.do",
                data : {
                    "studyID" : studyID
                },
                type : "get",
                dataType : "json",
                async: true,
                success : function (data) {
                    /*alert(data);*/
                    $("#creat-patientID").html(data.patientID);
                    $("#name").html(data.name);
                    $("#age").html(data.age);
                    $("#gender").html(data.gender);
                }

            })

            //清除缓存
            db.removeItem('studyID');
            //获得投照方式，使用耗材，计划的程序步骤的相关描述
            //id="projection"使用耗材useConsumables,scheduledProcedure-StepDescription
            $.ajax({
                url : "workbench/Report/get_data_from_studyInfo.do",
                data : {
                    "creat-studyID" : $("#creat-studyID").text(),
                    "projection" : $("#projection").text(),
                    "useConsumables" : $("#useConsumables").text(),
                    "scheduledProcedureStepDescription" : $("#scheduledProcedureStepDescription").text()
                },
                type : "get",
                dataType : "json",
                async: true,
                success : function (data) {
                    /*alert(data);*/
                    $("#projection").html(data.projection);
                    $("#useConsumables").html(data.useConsumables);
                    $("#scheduledProcedureStepDescription").html(data.scheduledProcedureStepDescription);
                }

            })
        });

    </script>


</head>
<body>

<!-- 填写报告表单 -->

<%--
   1.报告医生的姓名是自动填充的，不是从后台获得的；
   2.需要添加一些东西。
病人表——病人性别，年龄
检查信息表——计划的程序步骤的相关描述，投照方式，使用耗材--%>
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
        <label  class="col-sm-2 control-label">报告号<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="creat-id" ></text>
        </div>
        <label  class="col-sm-2 control-label">检查号</label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="creat-studyID"></text>
        </div>
    </div>

    <div class="form-group">
        <label  class="col-sm-2 control-label">病人号<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="creat-patientID" ></text>
        </div>

        <label  class="col-sm-2 control-label">病人名<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="name" ></text>
        </div>
    </div>

    <div class="form-group">
        <label  class="col-sm-2 control-label">病人年龄<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="age" ></text>
        </div>

        <label  class="col-sm-2 control-label">病人性别<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="gender" ></text>
        </div>
    </div>

    <div class="form-group">
        <label for="creat-diseaseName" class="col-sm-2 control-label">疾病名称<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select  class="form-control" id="creat-diseaseName" >
            </select>
        </div>

        <label for="creat-bodyPart" class="col-sm-2 control-label">疾病部位<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="creat-bodyPart">
            </select>
        </div>
    </div>

    <div class="form-group">
        <label <%--for="creat-createUserID"--%> class="col-sm-2 control-label">报告医生<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;/*line-height: 10px*/">
            <span class="form-control"  id="creat-createUserID" style="text-align: left;/*line-height:3px;*/">${user.name}</span>
        </div>
        <label for="creat-positive" class="col-sm-2 control-label">是否阳性<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="creat-positive" value="否">
        </div>
    </div>
<%--投照方式projection	 使用耗材useConsumables--%>
    <div class="form-group">
        <label  class="col-sm-2 control-label">投照方式<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text class="form-control"  id="projection" style="text-align: left;"></text>
        </div>
        <label  class="col-sm-2 control-label">使用耗材<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="useConsumables" value="否"></text>
        </div>
    </div>

    <%--计划的程序步骤的相关描述--%>
    <div class="form-group">
        <label  class="col-sm-2 control-label">计划的程序步骤的相关描述</label>
        <div class="col-sm-10" style="width: 70%;">
            <text class="form-control" rows="3" id="scheduledProcedureStepDescription"></text>
        </div>
    </div>

    <div class="form-group">
        <label for="creat-diseaseDescription" class="col-sm-2 control-label">疾病描述</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="creat-diseaseDescription"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="creat-imagingFindings" class="col-sm-2 control-label">影像学所见</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="creat-imagingFindings"></textarea>
        </div>
    </div>


    <div class="form-group">
        <label for="creat-diagnosticOpinion" class="col-sm-2 control-label">诊断意见</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea type="text" class="form-control" id="creat-diagnosticOpinion"></textarea>
        </div>
    </div>

</form>
</body>
</html>