<%--
  Created by IntelliJ IDEA.
  User: 25816
  Date: 2021/7/17
  Time: 10:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>


    <script>
        $(function () {
            //进入修改界面，显示修改之前有的信息
            //病人信息
            $("#edit-patientType").val("${patient.patientType}");
            $("#edit-ageType").val("${patient.ageType}");
            $("#edit-age").val("${patient.age}");
            $("#edit-birthDate").val("${patient.birthDate}");
            $("#edit-address").val("${patient.address}");
            $("#edit-gender").val("${patient.gender}");
            $("#edit-pregnancy").val("${patient.pregnancy}");
            $("#edit-IDType").val("${patient.IDType}");//傻逼idea怎么把IDType字段改成了idtype，我透李奶奶的
            $("#edit-IDNumber").val("${patient.IDNumber}");
            $("#edit-phoneNumber").val("${patient.phoneNumber}");
            $("#edit-healthCareType").val("${patient.healthCareType}");
            //临床医生工号
            $("#edit-department").val("${studyinfo.department}");
            $("#edit-emergency").val("${studyinfo.emergency}");
            $("#edit-clinicianID").val("${studyinfo.clinicianID}");
            $("#edit-bodyParts").val("${studyinfo.bodyParts}");
            $("#edit-modality").val("${studyinfo.modality}");
            $("#edit-studyDevice").val("${studyinfo.studyDevice}");
            $("#edit-requestedProcedureDescription").val("${studyinfo.requestedProcedureDescription}");

            $(".time").datetimepicker({
                minView: "month",
                language:  'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                endDate: new Date(),
                pickerPosition: "top-left"
            });

            $("#submit_check").click(function(){

                //发出传统请求，提交表单
                if($("#create-projection").val()==""||$("#create-projection").val()==null){
                    alert("请填写投照方式");
                    return false;
                }else if($("#create-useConsumables").val()==""||$("#create-useConsumables").val()==null){
                    alert("请填写投照方式");
                    return false;
                }else if($("#create-requestedProcedureDescription").val()==""||$("#create-requestedProcedureDescription").val()==null){
                    alert("请填写计划程序的相关描述");
                    return false;
                }
                <%--//id="projection"使用耗材useConsumables,scheduledProcedure-StepDescription--%>
                $.ajax({
                    url : "workbench/Check/Submit.do",
                    data : {
                        "accessionNumber" : $("#create-accessionNumber").text().trim(),
                        "technicianID" : $("#create-technicianID").text().trim(),
                        "projection" : $("#create-projection").val().trim(),
                        "useConsumables" : $("#create-useConsumables").val().trim(),
                        "scheduledProcedure-StepDescription" : $("#create-requestedProcedureDescription").val().trim(),
                        "flag" : true
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
                            window.location.href = "workbench/check/index.jsp";
                        }
                        else
                        {
                            alert("提交失败");
                        }

                    }
                })

            })
            $("#reject_check").click(function(){
                $.ajax({
                    url : "workbench/Check/Reject.do",
                    data : {
                        "accessionNumber" : $("#create-accessionNumber").text().trim(),
                        "technicianID" : $("#create-technicianID").text().trim()
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
                            window.location.href = "workbench/check/index.jsp";
                        }
                        else
                        {
                            alert("提交失败");
                        }

                    }
                })

            })
            //检查完成必须设置一下权限，只有提交了才可以检查完成
            $("#finish_check").click(function(){

                /*必须先提交才能检查完成*/
                if("${studyinfo.status}".indexOf("5")!=-1)
                {
                    $.ajax({
                        url : "workbench/Check/Finish.do",
                        data : {
                            "accessionNumber" : $("#create-accessionNumber").text().trim(),
                            "technicianID" : $("#create-technicianID").text().trim()
                        },
                        type : "post",
                        dataType : "json",
                        async: true,
                        success : function (data) {
                            /*tianjia*/
                            if(data.success)
                            {
                                alert("提交成功");
                                window.location.href = "workbench/check/index.jsp";
                            }
                            else
                            {
                                alert("提交失败");
                            }

                        }
                    })
                }
                else{
                    alert("您还未提交检查信息！");
                }

            })
        })
        //正在检查，把状态改成5，需修改，把状态改成4

    </script>

</head>
<body>

<div style="position:  relative; left: 30px;">
    <h3>检查信息</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" class="btn btn-primary" id="submit_check">提交检查</button>
        <button type="button" class="btn btn-danger" id="reject_check">驳回检查</button>
        <button type="button" class="btn btn-primary" id="finish_check">检查完成</button>
        <button type="button" class="btn btn-default" onclick="window.location.href='workbench/check/index.jsp';">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form action="workbench/register/update.do" id="registerForm" method="post" class="form-horizontal" role="form" style="position: relative; top: -30px;">

    <div class="form-group">
        <label  class="col-sm-2 control-label">检查号</label>
        <div class="col-sm-10" style="width: 300px;">
            <text class="form-control" id="create-accessionNumber">${studyinfo.accessionNumber}
            </text>
        </div>
    </div>

    <div class="form-group">
        <label  class="col-sm-2 control-label">检查技师</label>
        <div class="col-sm-10" style="width: 300px;">
            <text class="form-control" id="technicianName">${user.name}</text>
        </div>
        <div class="col-sm-10" style="width: 300px;">
            <text class="form-control" id="create-technicianID">${user.id}</text>
        </div>
    </div>
    <%--<div class="form-group">
        <label for="create-check_info" class="col-sm-2 control-label" >检查信息备注<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea type="text" class="form-control" id="create-check_info" placeholder="请输入内容"></textarea>
        </div>
    </div>--%>

    <div style="position:  relative; left: 30px;">
        <h4>病人信息详情</h4>
        <hr style="position: relative; top: -40px;">
    </div>

    <div class="form-group">
        <label  class="col-sm-2 control-label">病人姓名<span style="font-size: 15px; color: #ff0000;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text  class="form-control"  id="edit-patientname" >${patient.name}
            </text>
        </div>
        <label  class="col-sm-2 control-label">姓名拼音<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text  class="form-control" id="edit-patientnamePinYin" >${patient.namePinYin}
            </text>
        </div>
    </div>

    <div class="form-group">
        <label for="edit-IDType" class="col-sm-2 control-label">证件类型<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-IDType" name="IDType">
                <option></option>
                <option>身份证</option>
                <option>护照</option>
                <option>港澳通行证</option>
            </select>
        </div>
        <label  class="col-sm-2 control-label">证件号<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text  class="form-control" id="edit-IDNumber" >${patient.IDNumber}
            </text>
        </div>
    </div>

    <div class="form-group">
        <label  class="col-sm-2 control-label">住院科室<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text  class="form-control" id="edit-inpatientDepartment" >${patient.inpatientDepartment}
            </text>
        </div>
        <label for="edit-patientType" class="col-sm-2 control-label">病人类型<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-patientType" name="patientType">
                <option></option>
                <option>急诊</option>
                <option>门诊</option>
                <option>住院</option>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label  class="col-sm-2 control-label">住院号</label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="edit-inpatientNumber" name="inpatientNumber">
                ${patient.inpatientNumber}
            </text>
        </div>
        <label  class="col-sm-2 control-label">住院床号</label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="edit-inpatientBedNumber" name="inpatientBedNumber">
                ${patient.inpatientBedNumber}
            </text>
        </div>
    </div>

    <div class="form-group">
        <label for="edit-ageType" class="col-sm-2 control-label">年龄类型<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-ageType" name="ageType">
                <option></option>
                <option>年</option>
                <option>月</option>
                <option>日</option>
            </select>
        </div>
        <label for="edit-healthCareType" class="col-sm-2 control-label">医保类型<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-healthCareType" name="healthCareType">
                <option></option>
                <option>城镇职工医疗保险</option>
                <option>城乡居民医疗保险</option>
                <option>无</option>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label  class="col-sm-2 control-label">年龄<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="edit-age" name="age">${patient.age}
            </text>
        </div>
        <label  class="col-sm-2 control-label">出生日期</label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control time" id="edit-birthDate" name="birthDate">
                ${patient.birthDate}
            </text>
        </div>
    </div>

    <div class="form-group">
        <label for="edit-gender" class="col-sm-2 control-label">性别<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-gender" name="gender">
                <option></option>
                <option>M</option>
                <option>F</option>
            </select>
        </div>
        <label for="edit-pregnancy" class="col-sm-2 control-label">怀孕情况<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-pregnancy" name="pregnancy">
                <option></option>
                <option>是</option>
                <option>否</option>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label  class="col-sm-2 control-label">手机号<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="edit-phoneNumber" name="phoneNumber">
                ${patient.phoneNumber}</text>
        </div>
        <label  class="col-sm-2 control-label">地址</label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="edit-address" name="address">${patient.address}
            </text>
        </div>
    </div>

    <div style="position:  relative; left: 30px;">
        <h4>检查信息详情</h4>
        <hr style="position: relative; top: -40px;">
    </div>

    <div class="form-group">
        <label for="edit-emergency" class="col-sm-2 control-label">是否急诊<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-emergency" name="emergency">
                <option></option>
                <option>是</option>
                <option>否</option>
            </select>
        </div>
        <label  class="col-sm-2 control-label">检查科室<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="edit-department" name="department">
                ${studyinfo.department}</text>
        </div>
    </div>

    <div class="form-group">
        <label  class="col-sm-2 control-label">临床医生工号<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <text type="text" class="form-control" id="edit-clinicianID" name="clinicianID">
                ${studyinfo.clinicianID}</text>
        </div>
        <label for="edit-bodyParts" class="col-sm-2 control-label">检查位置<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-bodyParts" name="bodyParts">
                <option></option>
                <c:forEach items="${bodyPartsList}" var="t">
                    <option value="${t.value}">${t.text}</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label for="edit-modality" class="col-sm-2 control-label">模式(检查设备类型)<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-modality" name="modality">
                <option></option>
                <c:forEach items="${modalityList}" var="t">
                    <option value="${t.value}">${t.text}</option>
                </c:forEach>
            </select>
        </div>
        <label for="edit-studyDevice" class="col-sm-2 control-label">检查设备<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-studyDevice" name="studyDevice">
                <option></option>
                <c:forEach items="${deviceList}" var="t">
                    <option value="${t.id}">${t.name}(${t.room})</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label for="create-requestedProcedureDescription" class="col-sm-2 control-label">请求过程描述</label>
        <div class="col-sm-10" style="width: 64%;">
            <textarea class="form-control" rows="5" id="create-requestedProcedureDescription" name="requestedProcedureDescription">${studyinfo.scheduledProcedureStepDescription}</textarea>
        </div>
    </div>
    <%--//id="projection"使用耗材useConsumables,scheduledProcedure-StepDescription--%>
    <div class="form-group">
        <label for="create-projection" class="col-sm-2 control-label">投照方式<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-projection" name="emergency">
                <div>${studyinfo.projection}</div>
                <option>矢状面</option>
                <option>冠状面</option>
                <option>横截面</option>
            </select>
        </div>
        <label for="create-useConsumables" class="col-sm-2 control-label">使用耗材<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-useConsumables" name="department" value="${studyinfo.useConsumables}">
        </div>
    </div>
</form>
</body>
</html>
