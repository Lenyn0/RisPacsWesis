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
        //为签到按钮绑定事件
        $("#signinBtn").click(function () {
            var $xz = $("input[name=xz]:checked");
            if($xz.length==0){
                alert("请选择需要签到预约的记录");
            }else if($xz.length>1){
                alert("只能选择一条预约记录进行签到");
                //肯定只选了一条
            }else{
                if(confirm("确定签到所选中的预约记录吗？")){
                    var accessionNumber = $xz.val();
                    //url:workbench/activity/delete.do?id=xxx&id=xxx&id=xxx
                    //拼接参数
                    $.ajax({
                        url : "workbench/sign_in/signin.do",
                        data : {
                            "accessionNumber" : accessionNumber
                        },
                        type : "post",
                        dataType : "json",
                        success : function (data) {
                            /*
                                data
                                    {"success":true/false}
                             */
                            if(data.success){
                                //签到成功后
                                //回到第一页，维持每页展现的记录数
                                pageList(1,$("#signinPage").bs_pagination('getOption', 'rowsPerPage'));
                            }else{
                                alert("签到失败");
                            }
                        }
                    })
                }
            }
        })
        //页面加载完毕后触发一个方法
        //默认展开列表的第一页，每页展现两条记录
        pageList(1,5);
        //为查询按钮绑定事件，触发pageList方法
        $("#searchBtn").click(function () {
            $("#hidden-patientname").val($.trim($("#search-patientname").val()));
            $("#hidden-department").val($.trim($("#search-department").val()));
            $("#hidden-scheduledProcedureStepStartDate").val($.trim($("#search-scheduledProcedureStepStartDate").val()));
            $("#hidden-scheduledProcedureStepStartTime").val($.trim($("#search-scheduledProcedureStepStartTime").val()));
            $("#hidden-clinicianID").val($.trim($("#search-clinicianID").val()));
            pageList(1,5);

        })
        //为取消按钮绑定事件，执行取消预约的操作
        $("#cancelBtn").click(function () {
            var $xz = $("input[name=xz]:checked");
            if($xz.length==0){
                alert("请选择需要取消预约的记录");
            }else if($xz.length>1){
                alert("只能选择一条预约记录进行取消");
                //肯定只选了一条
            }else{
                var cancellationReason = prompt("取消原因");
                if(cancellationReason){
                    var accessionNumber = $xz.val();
                    //url:workbench/activity/delete.do?id=xxx&id=xxx&id=xxx
                    //拼接参数
                    $.ajax({
                        url : "workbench/sign_in/cancel.do",
                        data : {
                            "accessionNumber" : accessionNumber,
                            "cancellationReason" : cancellationReason,
                            //取消人员的id
                            "id" : "${user.id}"
                        },
                        type : "post",
                        dataType : "json",
                        success : function (data) {
                            /*
                                data
                                    {"success":true/false}
                             */
                            if(data.success){
                                //删除成功后
                                //回到第一页，维持每页展现的记录数
                                pageList(1,$("#signinPage").bs_pagination('getOption', 'rowsPerPage'));
                            }else{
                                alert("删除取消预约失败");
                            }
                        }
                    })
                }
            }
        })
        //为修改按钮绑定事件，打开修改操作的模态窗口
        $(".time").datetimepicker({
            minView: "hour",
            language:  'zh-CN',
            format: 'yyyy-mm-dd hh:ii',
            autoclose: true,
            todayBtn: true,
            startDate: new Date(),
            pickerPosition: "bottom-right"
        });
        $("#editBtn").click(function () {
            var $xz = $("input[name=xz]:checked");
            if($xz.length==0){
                alert("请选择需要修改的预约记录");
            }else if($xz.length>1){
                alert("只能选择一条预约记录进行修改");
                //肯定只选了一条
            }else{
                var accessionNumber = $xz.val();
                $.ajax({
                    url : "workbench/sign_in/edit.do",
                    data : {
                        "accessionNumber" : accessionNumber
                    },
                    type : "get",
                    dataType : "json",
                    success : function (data) {
                        $("#edit-accessionNumber").val(data.studyinfo.accessionNumber);
                        $("#edit-patientname").val(data.patient.name);
                        $("#edit-department").val(data.studyinfo.department);
                        $("#edit-scheduledProcedureStepStart0").val(data.studyinfo.scheduledProcedureStepStartDate+" "+data.studyinfo.scheduledProcedureStepStartTime);
                        //所有的值都填写好之后，打开修改操作的模态窗口
                        $("#editscheduledProcedureStepStartModal").modal("show");
                    }
                })
            }
        })
        $("#updateBtn").click(function () {
            var scheduledProcedureStepStart = $.trim($("#edit-scheduledProcedureStepStart").val());
            if(scheduledProcedureStepStart==""){
                $("#msg").html("预约时间不能为空");
                //如果账号密码为空，则需要及时强制终止该方法
                return false;
            }
            $.ajax({
                url : "workbench/sign_in/updatescheduledProcedureStepStart.do",
                data : {
                    "accessionNumber" : $.trim($("#edit-accessionNumber").val()),
                    "scheduledProcedureStepStart" : scheduledProcedureStepStart
                },
                type : "post",
                dataType : "json",
                success : function (data) {
                    /*
                        data
                            {"success":true/false}
                     */
                    if(data.success){
                        /*
                            修改操作后，应该维持在当前页，维持每页展现的记录数
                         */
                        pageList($("#signinPage").bs_pagination('getOption', 'currentPage'),$("#signinPage").bs_pagination('getOption', 'rowsPerPage'));
                        //关闭修改操作的模态窗口
                        $("#editscheduledProcedureStepStartModal").modal("hide");
                    }else{
                        alert("修改预约时间失败");
                    }
                }
            })
        })
    });
    function pageList(pageNo,pageSize) {
        //将全选的复选框的√干掉
        //$("#qx").prop("checked",false);
        //查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中
        $("#search-patientname").val($.trim($("#hidden-patientname").val()));
        $("#search-department").val($.trim($("#hidden-department").val()));
        $("#search-scheduledProcedureStepStartDate").val($.trim($("#hidden-scheduledProcedureStepStartDate").val()));
        $("#search-scheduledProcedureStepStartTime").val($.trim($("#hidden-scheduledProcedureStepStartTime").val()));
        $("#search-clinicianID").val($.trim($("#hidden-clinicianID").val()));
        $.ajax({
            url : "workbench/sign_in/pageList.do",
            data : {
                "pageNo" : pageNo,
                "pageSize" : pageSize,
                "patientname" : $.trim($("#search-patientname").val()),
                "department" : $.trim($("#search-department").val()),
                "scheduledProcedureStepStartDate" : $.trim($("#search-scheduledProcedureStepStartDate").val()),
                "scheduledProcedureStepStartTime" : $.trim($("#search-scheduledProcedureStepStartTime").val()),
                "clinicianID" : $.trim($("#search-clinicianID").val()),
                "registrarID": "${user.id}"
            },
            type : "get",
            dataType : "json",
            success : function (data) {
                var html = "";
                $.each(data.dataList,function (i,n) {
                    html += '<tr class="active">';
                    html += '<td><input type="checkbox" name="xz" value="'+n.accessionNumber+'"/></td>';
                    //html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
                    html += '<td>'+n.patientID+'</td>';
                    html += '<td>'+n.department+'</td>';
                    html += '<td>'+n.clinicianID+'</td>';
                    html += '<td>'+n.scheduledProcedureStepStartDate+'</td>';
                    html += '<td>'+n.scheduledProcedureStepStartTime+'</td>';
                    html += '</tr>';
                })
                $("#signinBody").html(html);
                //计算总页数
                var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
                //数据处理完毕后，结合分页查询，对前端展现分页信息
                $("#signinPage").bs_pagination({
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

<input type="hidden" id="hidden-patientname"/>
<input type="hidden" id="hidden-department"/>
<input type="hidden" id="hidden-scheduledProcedureStepStartDate"/>
<input type="hidden" id="hidden-scheduledProcedureStepStartTime"/>
<input type="hidden" id="hidden-clinicianID"/>



<!-- 修改预约时间的模态窗口 -->
<div class="modal fade" id="editscheduledProcedureStepStartModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">更新预约时间</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">

                    <input type="hidden" id="edit-id"/>

                    <div class="form-group">
                        <input type="hidden" id="edit-accessionNumber" name="accessionNumber">
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

                    <div class="form-group">
                        <label for="edit-patientname" class="col-sm-2 control-label">病人名称</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-patientname" disabled>
                        </div>
                        <label for="edit-department" class="col-sm-2 control-label">检查科室</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-department" name="department" disabled>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-scheduledProcedureStepStart0" class="col-sm-2 control-label">旧预约时间</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-scheduledProcedureStepStart0" name="scheduledProcedureStepStart0" disabled>
                        </div>
                        <label for="edit-scheduledProcedureStepStart" class="col-sm-2 control-label">新预约时间<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" autocomplete="off" required="required" id="edit-scheduledProcedureStepStart" name="scheduledProcedureStepStart">
                        </div>
                    </div>

                    <%--<div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <!--

                                关于文本域textarea：
                                    （1）一定是要以标签对的形式来呈现,正常状态下标签对要紧紧的挨着
                                    （2）textarea虽然是以标签对的形式来呈现的，但是它也是属于表单元素范畴
                                            我们所有的对于textarea的取值和赋值操作，应该统一使用val()方法（而不是html()方法）

                            -->
                            <textarea class="form-control" rows="3" id="edit-description">123</textarea>
                        </div>
                    </div>--%>

                </form>
                <label for="msg" class="col-sm-2 control-label"></label>
                <div class="col-sm-10" style="width: 300px;">
                    <span id="msg" style="color: red"></span>
                </div>
                <br>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="updateBtn">更新</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>预约列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">
        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">病人姓名</div>
                        <input class="form-control" type="text" id="search-patientname">
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">科室</div>
                        <input class="form-control" type="text" id="search-department">
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">预约日期</div>
                        <input class="form-control" type="text" id="search-scheduledProcedureStepStartDate">
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">预约时间</div>
                        <input class="form-control" type="text" id="search-scheduledProcedureStepStartTime">
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">临床医生姓名</div>
                        <input class="form-control" type="text" id="search-clinicianID">
                    </div>
                </div>
                <button type="button" id="searchBtn" class="btn btn-default">查询</button>
            </form>
        </div>
        <div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <!--

                    点击创建按钮，观察两个属性和属性值

                    data-toggle="modal"：
                        表示触发该按钮，将要打开一个模态窗口

                    data-target="#createActivityModal"：
                        表示要打开哪个模态窗口，通过#id的形式找到该窗口


                    现在我们是以属性和属性值的方式写在了button元素中，用来打开模态窗口
                    但是这样做是有问题的：
                        问题在于没有办法对按钮的功能进行扩充

                    所以未来的实际项目开发，对于触发模态窗口的操作，一定不要写死在元素当中，
                    应该由我们自己写js代码来操作

                -->
                <button type="button" class="btn btn-primary" id="signinBtn"><span class="glyphicon glyphicon-check"></span> 签到</button>
                <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改预约时间</button>
                <button type="button" class="btn btn-danger" id="cancelBtn"><span class="glyphicon glyphicon-minus"></span> 取消预约</button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" style="visibility:hidden"/></td>
                    <td>病人姓名</td>
                    <td>检查科室</td>
                    <td>临床医生姓名</td>
                    <td>预约日期</td>
                    <td>预约时间</td>
                </tr>
                </thead>
                <tbody id="signinBody"></tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">

            <div id="signinPage"></div>

        </div>

    </div>

</div>
</body>
</html>