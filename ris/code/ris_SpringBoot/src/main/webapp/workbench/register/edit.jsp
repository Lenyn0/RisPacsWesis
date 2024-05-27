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
        const accessionNumber = sessionStorage.getItem('accessionNumber');
        sessionStorage.removeItem('accessionNumber');

        $.ajax({
            url : "workbench/register/edit.do",
            data : {
                "accessionNumber" : accessionNumber
            },
            type : "get",
            dataType : "json",
            success : function (result) {
                var data= result.data;

                $("#edit-accessionNumber").val(data.studyinfo.accessionNumber);
                $("#edit-status").val(data.studyinfo.status);

                $("#edit-patientname").val(data.patient.name);
                $("#edit-patientnamePinYin").val(data.patient.namePinYin);
                $("#edit-patientType").val(data.patient.patientType);
                $("#edit-inpatientNumber").val(data.patient.inpatientNumber);
                $("#edit-inpatientDepartment").val(data.patient.inpatientDepartment);
                $("#edit-inpatientBedNumber").val(data.patient.inpatientBedNumber);
                $("#edit-ageType").val(data.patient.ageType);
                $("#edit-age").val(data.patient.age);
                $("#edit-birthDate").val(data.patient.birthDate);
                $("#edit-address").val(data.patient.address);
                $("#edit-gender").val(data.patient.gender);
                $("#edit-pregnancy").val(data.patient.pregnancy);
                $("#edit-idType").val(data.patient.idType);
                $("#edit-idNumber").val(data.patient.idNumber);
                $("#edit-phoneNumber").val(data.patient.phoneNumber);
                $("#edit-healthCareType").val(data.patient.healthCareType);
                //临床医生工号
                $("#edit-department").val(data.studyinfo.department);
                $("#edit-emergency").val(data.studyinfo.emergency);
                $("#edit-clinicianID").val(data.studyinfo.clinicianID);
                $("#edit-bodyParts").val(data.studyinfo.bodyParts);
                $("#edit-modality").val(data.studyinfo.modality);
                $("#edit-studyDevice").val(data.studyinfo.studyDevice);
                $("#edit-requestedProcedureDescription").val(data.studyinfo.requestedProcedureDescription);
            }
        })


        $(".time").datetimepicker({
            minView: "month",
            language:  'zh-CN',
            format: 'yyyy-mm-dd',
            autoclose: true,
            todayBtn: true,
            endDate: new Date(),
            pickerPosition: "top-left"
        });
        $("#edit-birthDate").datetimepicker().on('changeDate',function(){
            const birthday = new Date($("#edit-birthDate").val());
            const today = new Date();
            const days = parseInt((today-birthday)/(1*24*60*60*1000));
            if(today.getFullYear()-birthday.getFullYear()>1){
                //年份相差2则年龄类型一定是年
                $("#edit-ageType").attr("readonly",false);
                $("#edit-ageType").val("年");
                $("#edit-ageType").attr("readonly",true);
                $("#edit-age").attr("readonly",false);
                if((today.getMonth()>birthday.getMonth())||(today.getMonth()==birthday.getMonth()&&today.getDate()>=birthday.getDate())){
                    $("#edit-age").val(today.getFullYear()-birthday.getFullYear());
                }else{
                    $("#edit-age").val(today.getFullYear()-birthday.getFullYear()-1);
                }
                $("#edit-age").attr("readonly",true);
            }else if(today.getFullYear()-birthday.getFullYear()==1){
                //年份相差1则年龄类型须继续判断
                if((today.getMonth()>birthday.getMonth())||(today.getMonth()==birthday.getMonth()&&today.getDate()>=birthday.getDate())){
                    //如果生日的月日小于今天的月日则年龄类型为年
                    $("#edit-ageType").attr("readonly",false);
                    $("#edit-ageType").val("年");
                    $("#edit-ageType").attr("readonly",true);
                    $("#edit-age").attr("readonly",false);
                    $("#edit-age").val(today.getFullYear()-birthday.getFullYear());
                    $("#edit-age").attr("readonly",true);
                }else if(today.getDate()>=birthday.getDate()){
                    //如果生日的日小于今天的日则年龄类型为月
                    $("#edit-ageType").attr("readonly",false);
                    $("#edit-ageType").val("月");
                    $("#edit-ageType").attr("readonly",true);
                    $("#edit-age").attr("readonly",false);
                    $("#edit-age").val(12-birthday.getMonth()+today.getMonth());
                    $("#edit-age").attr("readonly",true);
                }else{
                    //如果生日的日大于今天的日则年龄类型为月或天
                    if(11-birthday.getMonth()+today.getMonth()>1){
                        //此时年龄类型为月
                        $("#edit-ageType").attr("readonly",false);
                        $("#edit-ageType").val("月");
                        $("#edit-ageType").attr("readonly",true);
                        $("#edit-age").attr("readonly",false);
                        $("#edit-age").val(11-birthday.getMonth()+today.getMonth());
                        $("#edit-age").attr("readonly",true);
                    }
                    else{
                        //此时年龄类型为天
                        $("#edit-ageType").attr("readonly",false);
                        $("#edit-ageType").val("天");
                        $("#edit-ageType").attr("readonly",true);
                        $("#edit-age").attr("readonly",false);
                        $("#edit-age").val(days);
                        $("#edit-age").attr("readonly",true);
                    }
                }
            }else{
                //年份相等则年龄类型须继续判断
                if(today.getMonth()==birthday.getMonth()){
                    //此时年龄类型为天
                    $("#edit-ageType").attr("readonly",false);
                    $("#edit-ageType").val("天");
                    $("#edit-ageType").attr("readonly",true);
                    $("#edit-age").attr("readonly",false);
                    $("#edit-age").val(days);
                    $("#edit-age").attr("readonly",true);
                }else if(today.getMonth()-birthday.getMonth()==1){
                    if(today.getDate()>=birthday.getDate()){
                        //此时年龄类型为月
                        $("#edit-ageType").attr("readonly",false);
                        $("#edit-ageType").val("月");
                        $("#edit-ageType").attr("readonly",true);
                        $("#edit-age").attr("readonly",false);
                        $("#edit-age").val(today.getMonth()-birthday.getMonth());
                        $("#edit-age").attr("readonly",true);
                    }else{
                        //此时年龄类型为天
                        $("#edit-ageType").attr("readonly",false);
                        $("#edit-ageType").val("天");
                        $("#edit-ageType").attr("readonly",true);
                        $("#edit-age").attr("readonly",false);
                        $("#edit-age").val(days);
                        $("#edit-age").attr("readonly",true);
                    }
                }else{
                    if(today.getDate()>=birthday.getDate()){
                        $("#edit-ageType").attr("readonly",false);
                        $("#edit-ageType").val("月");
                        $("#edit-ageType").attr("readonly",true);
                        $("#edit-age").attr("readonly",false);
                        $("#edit-age").val(today.getMonth()-birthday.getMonth());
                        $("#edit-age").attr("readonly",true);
                    }else{
                        $("#edit-ageType").attr("readonly",false);
                        $("#edit-ageType").val("月");
                        $("#edit-ageType").attr("readonly",true);
                        $("#edit-age").attr("readonly",false);
                        $("#edit-age").val(today.getMonth()-birthday.getMonth()-1);
                        $("#edit-age").attr("readonly",true);
                    }
                }
            }
        })
        //为保存按钮绑定事件，执行交易添加操作
        $("#updateBtn").click(function () {
            //发出传统请求，提交表单
            $("#registerForm").submit();
        })
    })
</script>

</head>
<body>

<div style="position:  relative; left: 30px;">
    <h3>修改检查信息表</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" class="btn btn-primary" id="updateBtn">更新</button>
        <button type="button" class="btn btn-default" onclick="window.location.href='workbench/register/index.jsp';">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form action="workbench/register/update.do" id="registerForm" method="post" class="form-horizontal" role="form" style="position: relative; top: -30px;">

    <div class="form-group">
        <input type="hidden" id="edit-accessionNumber" name="accessionNumber" >
        <input type="hidden" id="edit-status" name="status">
    </div>

    <div class="form-group">
        <label for="edit-registrarID" class="col-sm-2 control-label">登记员</label>
        <div class="col-sm-10" style="width: 300px;">
            <input class="form-control" id="edit-registrarname" name="registrarname" value="${user.name}" disabled>
        </div>
        <div class="col-sm-10" style="width: 300px;">
            <input type="hidden" class="form-control" id="edit-registrarID" name="registrarID" value="${user.id}">
        </div>
    </div>

    <div style="position:  relative; left: 30px;">
        <h4>修改病人信息</h4>
        <hr style="position: relative; top: -40px;">
    </div>

    <div class="form-group">
        <label for="edit-patientname" class="col-sm-2 control-label">病人姓名<span style="font-size: 15px; color: #ff0000;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" autocomplete="off" id="edit-patientname" name="name" placeholder="支持自动补全(不存在则新建)">
        </div>
        <label for="edit-patientnamePinYin" class="col-sm-2 control-label">姓名拼音<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-patientnamePinYin" name="namePinYin" >
        </div>
    </div>

    <div class="form-group">
        <label for="edit-idType" class="col-sm-2 control-label">证件类型<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="edit-idType" name="idType">
                <option></option>
                <option>身份证</option>
                <option>护照</option>
                <option>港澳通行证</option>
            </select>
        </div>
        <label for="edit-idNumber" class="col-sm-2 control-label">证件号<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-idNumber" name="idNumber">
        </div>
    </div>

    <div class="form-group">
        <label for="edit-inpatientDepartment" class="col-sm-2 control-label">住院科室<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-inpatientDepartment" name="inpatientDepartment">
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
        <label for="edit-inpatientNumber" class="col-sm-2 control-label">住院号</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-inpatientNumber" name="inpatientNumber">
        </div>
        <label for="edit-inpatientBedNumber" class="col-sm-2 control-label">住院床号</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-inpatientBedNumber" name="inpatientBedNumber">
        </div>
    </div>

    <div class="form-group">
        <label for="edit-ageType" class="col-sm-2 control-label">年龄类型<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-ageType" name="ageType" readonly="readonly">
            <%--<select class="form-control" id="edit-ageType" name="ageType">
                <option></option>
                <option>年</option>
                <option>月</option>
                <option>日</option>
            </select>--%>
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
        <label for="edit-age" class="col-sm-2 control-label">年龄<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-age" name="age" readonly="readonly">
        </div>
        <label for="edit-birthDate" class="col-sm-2 control-label">出生日期</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control time" id="edit-birthDate" name="birthDate">
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
        <label for="edit-phoneNumber" class="col-sm-2 control-label">手机号<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-phoneNumber" name="phoneNumber">
        </div>
        <label for="edit-address" class="col-sm-2 control-label">地址</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-address" name="address">
        </div>
    </div>

    <div style="position:  relative; left: 30px;">
        <h4>修改检查信息</h4>
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
        <label for="edit-department" class="col-sm-2 control-label">检查科室<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-department" name="department">
        </div>
    </div>

    <div class="form-group">
        <label for="edit-clinicianID" class="col-sm-2 control-label">临床医生工号<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-clinicianID" name="clinicianID">
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
        <label for="edit-requestedProcedureDescription" class="col-sm-2 control-label">请求过程描述</label>
        <div class="col-sm-10" style="width: 64%;">
            <textarea class="form-control" rows="5" id="edit-requestedProcedureDescription" name="requestedProcedureDescription"></textarea>
        </div>
    </div>

</form>
</body>
</html>