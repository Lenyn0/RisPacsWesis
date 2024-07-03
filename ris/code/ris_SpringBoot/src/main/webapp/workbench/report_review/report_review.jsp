<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <title>Title</title>

    <script type="text/javascript">

        //准备工作
        $(document).ready(function (){
                //从index.jsp界面获取检查号和病人号
                const db = window.localStorage;
                const id = db.getItem("id");//报告号
                const studyID = db.getItem("studyID");//检查号
                //alert(studyID);
                const patientID = db.getItem("patientID");//病人号
                //alert(patientID);
                //获取传过来的值
                //从index.jsp界面获取检查号和病人号
                $("#id").html(id);
                $("#studyID").html(studyID);
                $("#patientID").html(patientID);

                //清除缓存
                db.removeItem('id');
                db.removeItem('studyID');
                db.removeItem('patientID');

                $.ajax({

                    url : "workbench/Report/get_report.do",
                    data : {
                        //把报告号,检查号，病人号传到后端
                        "id" : id,
                        "studyID" : studyID,
                        "patientID" : patientID
                    },
                    type : "get",
                    dataType : "json",
                    success : function (result) {
                        var data = result.data;
                        //alert(data.createUserID);
                        /*
                            data
                                将报告信息遍历出来
                         */
                        $('#elapsedTimeDisplay').text(data.elapsedTime);
                        var html = "";
                        html += '<div class="form-group">';
                        html += '<label for="id" class="col-sm-2 control-label">报告号</label>';
                        html += '<div class="col-sm-10" style="width: 300px;">';
                        html += '        <text type="text" class="form-control" id="id">'+data.id+'</text>';
                        html += '    </div>';
                        html += '    <label for="studyID" class="col-sm-2 control-label">检查号</label>';
                        html += '    <div class="col-sm-10" style="width: 300px;">';
                        html += '        <text type="text" class="form-control" id="studyID" >'+data.studyID+'</text>';
                        html += '    </div>';
                        html += '</div>';
                        html += '<div class="form-group">';
                        html += '    <label for="createUserID" class="col-sm-2 control-label">报告医生id</label>';
                        html += '    <div class="col-sm-10" style="width:300px;">';
                        html += '        <text type="text" class="form-control" id="createUserID" >'+data.createUserID+'</text>';
                        html += '    </div>';
                        html += '    <label for="patientID" class="col-sm-2 control-label">病人名称</label>';
                        html += '    <div class="col-sm-10" style="width:300px;">';
                        html += '        <text type="text" class="form-control" id="patientID" >'+data.patientID+'</text>';
                        html += '    </div>';
                        html += '</div>';
                        html += '<div class="form-group">';
                        html += '    <label for="createUserID" class="col-sm-2 control-label">病人年龄</label>';
                        html += '    <div class="col-sm-10" style="width:300px;">';
                        html += '        <text type="text" class="form-control" id="age" >'+data.age+'</text>';
                        html += '    </div>';
                        html += '    <label for="patientID" class="col-sm-2 control-label">病人性别</label>';
                        html += '    <div class="col-sm-10" style="width:300px;">';
                        html += '        <text type="text" class="form-control" id="gender" >'+data.gender+'</text>';
                        html += '    </div>';
                        html += '</div>';
                        html += '<div class="form-group">';
                        html += '    <label for="bodyPart" class="col-sm-2 control-label">疾病部位</label>';
                        html += '    <div class="col-sm-10" style="width: 300px;">';
                        html += '        <text type="text" class="form-control" id="bodyPart" >'+data.bodyPart+'</text>';
                        html += '    </div>';
                        html += '    <label for="diseaseName" class="col-sm-2 control-label">疾病名称</label>';
                        html += '    <div class="col-sm-10" style="width: 300px;">';
                        html += '        <text type="text" class="form-control" id="diseaseName" >'+data.diseaseName+'</text>';
                        html += '    </div>';
                        html += '</div>';
                        html += '<div class="form-group">';
                        html += '    <label for="bodyPart" class="col-sm-2 control-label">是否阳性</label>';
                        html += '    <div class="col-sm-10" style="width: 300px;">';
                        html += '        <text type="text" class="form-control" id="positive" >'+data.positive+'</text>';
                        html += '    </div>';
                        html += '    <label for="diseaseName" class="col-sm-2 control-label">投照方式</label>';
                        html += '    <div class="col-sm-10" style="width: 300px;">';
                        html += '        <text type="text" class="form-control" id="projection" >'+data.projection+'</text>';
                        html += '    </div>';
                        html += '</div>';
                        html += '<div class="form-group">';
                        html += '    <label for="bodyPart" class="col-sm-2 control-label">使用耗材</label>';
                        html += '    <div class="col-sm-10" style="width: 300px;">';
                        html += '        <text type="text" class="form-control" id="useConsumables" >'+data.useConsumables+'</text>';
                        html += '    </div>';
                        html += '</div>';
                        html += '<div class="form-group">';
                        html += '    <label for="diseaseDescription" class="col-sm-2 control-label">计划的程序步骤的相关描述</label>';
                        html += '    <div class="col-sm-10" style="width: 64%;">';
                        html += '        <text class="form-control" rows="3" id="scheduledProcedureStepDescription">'+data.scheduledProcedureStepDescription+'</text>';
                        html += '    </div>';
                        html += '</div>';
                        html += '<div class="form-group">';
                        html += '    <label for="diseaseDescription" class="col-sm-2 control-label">疾病描述</label>';
                        html += '    <div class="col-sm-10" style="width: 64%;">';
                        html += '        <text class="form-control" rows="3" id="diseaseDescription">'+data.diseaseDescription+'</text>';
                        html += '    </div>';
                        html += '</div>';
                        html += '<div class="form-group">';
                        html += '    <label for="imagingFindings" class="col-sm-2 control-label">影像学所见</label>';
                        html += '    <div class="col-sm-10" style="width: 64%;">';
                        html += '        <text class="form-control" rows="3" id="imagingFindings">'+data.imagingFindings+'</text>';
                        html += '    </div>';
                        html += '</div>';
                        html += '<div class="form-group">';
                        html += '    <label for="diagnosticOpinion" class="col-sm-2 control-label">诊断意见</label>';
                        html += '    <div class="col-sm-10" style="width: 64%;">';
                        html += '        <text type="text" class="form-control" id="diagnosticOpinion">'+data.diagnosticOpinion+'</text>';
                        html += '    </div>';
                        html += '</div>';

                        $("#report_body").html(html);
                    }

                })

            }
        )

        $(function(){


            //从后台取数据铺值到报告表格中

            //为审核通过添加事件，报告表的报告状态更改为3即可，不用修改study_info表
            $("#pass").click(function(){

                $.ajax({

                    url : "workbench/Report/pass.do",
                    data : {
                        "id" : $("#id").text(),
                        "studyID" : $("#studyID").text(),
                        "auditorID" : $("#auditorID").text()
                        //user.name
                        <%--${user.name}--%>
                    },
                    type : "post",
                    dataType : "json",
                    async: true,
                    success : function (data) {
                        /*tianjia*/
                        if(data.success)
                        {
                            //审核完成之后，列表少一条记录，局部刷新列表（还没有实现），跳回填写报告页面
                            window.location.href = "workbench/report_review/index.jsp";
                        }
                        else
                        {
                            alert("提交失败");
                        }

                    }
                })

            })
            //为审核驳回添加事件，报告表的报告状态更改为2，study_info表的状态更改为4
            $("#reject").click(function(){

                $.ajax({

                    url : "workbench/Report/reject.do",
                    data : {
                        "id" : $("#id").text(),
                        "studyID" : $("#studyID").text(),
                        "auditorID" : $("#auditorID").text()
                    },
                    type : "post",
                    dataType : "json",
                    async: true,
                    success : function (data) {
                        /*tianjia*/
                        if(data.success)
                        {
                            //审核完成之后，列表少一条记录，局部刷新列表（还没有实现），跳回填写报告页面
                            window.location.href = "workbench/report_review/index.jsp";
                        }
                        else
                        {
                            alert("提交失败");
                        }

                    }
                })

            })


        });

    </script>

</head>
<body>

<div style="position:  relative; left: 10%;">
    <h3>报告详情</h3>
    <div id="elapsedTimeDisplay"></div>
    <div style="position: relative; top: -40px; left: 51.5%;">
        <button type="button" class="btn btn-primary" id="pass">审核通过</button>
        <button type="button" class="btn btn-danger" id="reject">审核驳回</button>
        <button type="button" class="btn btn-default" id="cancel" onclick="window.location.href = 'workbench/report_review/index.jsp';">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>

<form class="form-horizontal" role="form" style="position: relative; top: -30px;" id="report_body">

</form>

<label  class="col-sm-2 control-label" style="position: relative; left: 750px;">审核医生 : </label>
<div class="col-sm-10" style="width: 300px;">
    <span style="position: relative; left: 615px;"><u id="auditorID">${user.name}</u></span>
</div>

</body>
</html>