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
        //默认展开列表的第一页，每页展现两条记录
        pageList(1,5);
        //为查询按钮绑定事件，触发pageList方法
        $("#searchBtn").click(function () {
            /*点击查询按钮的时候，我们应该将搜索框中的信息保存起来,保存到隐藏域中*/
            $("#hidden-patientname").val($.trim($("#search-patientname").val()));
            $("#hidden-department").val($.trim($("#search-department").val()));
            $("#hidden-clinicianID").val($.trim($("#search-clinicianID").val()));
            $("#hidden-emergency").val($.trim($("#search-emergency").val()));
            pageList(1,5);
        })
        //为全选的复选框绑定事件，触发全选操作
        $("#qx").click(function () {
            $("input[name=xz]").prop("checked",this.checked);
        })
        //以下这种做法是不行的
        /*$("input[name=xz]").click(function () {
            alert(123);
        })*/
        //因为动态生成的元素，是不能够以普通绑定事件的形式来进行操作的
        /*
            动态生成的元素，我们要以on方法的形式来触发事件
            语法：
                $(需要绑定元素的有效的外层元素).on(绑定事件的方式,需要绑定的元素的jquery对象,回调函数)
         */
        $("#registerBody").on("click",$("input[name=xz]"),function () {
            $("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
        })
        //为删除按钮绑定事件，执行登记信息删除操作
        $("#deleteBtn").click(function () {
            //找到复选框中所有挑√的复选框的jquery对象
            var $xz = $("input[name=xz]:checked");
            if($xz.length==0){
                alert("请选择需要删除的登记记录");
                //肯定选了，而且有可能是1条，有可能是多条
            }else{
                if(confirm("确定删除所选中的登记记录吗？")){
                    //url:workbench/activity/delete.do?id=xxx&id=xxx&id=xxx
                    //拼接参数
                    var param = "";
                    //将$xz中的每一个dom对象遍历出来，取其value值，就相当于取得了需要删除的记录的id
                    for(var i=0;i<$xz.length;i++){
                        param += "accessionNumber="+$($xz[i]).val();
                        //如果不是最后一个元素，需要在后面追加一个&符
                        if(i<$xz.length-1){
                            param += "&";
                        }
                    }
                    $.ajax({
                        url : "workbench/register/delete.do",
                        data : param,
                        type : "post",
                        dataType : "json",
                        success : function (result) {
                            /*
                                data
                                    {"success":true/false}
                             */
                            var data = result;
                            if(data.success){
                                //删除成功后
                                //回到第一页，维持每页展现的记录数
                                pageList(1,$("#registerPage").bs_pagination('getOption', 'rowsPerPage'));
                            }else{
                                alert("删除登记记录失败");
                            }
                        }
                    })
                }
            }
        })
        //为修改按钮绑定事件，执行修改登记信息操作
        $("#editBtn").click(function () {
            var $xz = $("input[name=xz]:checked");
            if($xz.length==0){
                alert("请选择需要修改的记录");
            }else if($xz.length>1){
                alert("只能选择一条记录进行修改");
                //肯定只选了一条
            }else{
                var accessionNumber = $xz.val();
                //使用传统请求
                //window.location.href = "workbench/register/edit.do?accessionNumber="+accessionNumber;
                sessionStorage.setItem('accessionNumber', accessionNumber);
                window.location.href = "workbench/register/edit.jsp";
            }
        })
        //为预约按钮绑定时间
        $(".time").datetimepicker({
            minView: "hour",
            language:  'zh-CN',
            format: 'yyyy-mm-dd hh:ii',
            autoclose: true,
            todayBtn: true,
            startDate: new Date(),
            pickerPosition: "bottom-right"
        });
        $("#scheduleBtn").click(function () {
            var $xz = $("input[name=xz]:checked");
            if($xz.length==0){
                alert("请选择需要预约的登记记录");
            }else if($xz.length>1){
                alert("只能选择一条记录进行预约");
                //肯定只选了一条
            }else{
                var accessionNumber = $xz.val();
                $.ajax({
                    url : "workbench/register/schedule.do",
                    data : {
                        "accessionNumber" : accessionNumber
                    },
                    type : "get",
                    dataType : "json",
                    success : function (result) {
                        var data = result.data;
                        $("#schedule-accessionNumber").val(data.studyinfo.accessionNumber);
                        $("#schedule-patientname").val(data.patient.name);
                        $("#schedule-department").val(data.studyinfo.department);
                        //所有的值都填写好之后，打开修改操作的模态窗口
                        $("#scheduledProcedureStepStartModal").modal("show");
                    }
                })
            }
        })
        //为更新按钮绑定事件，执行市场活动的修改操作
        /*
            在实际项目开发中，一定是按照先做添加，再做修改的这种顺序
            所以，为了节省开发时间，修改操作一般都是copy添加操作
         */
        $("#appointmentBtn").click(function () {
            var scheduledProcedureStepStart = $.trim($("#schedule-scheduledProcedureStepStart").val());
            if(scheduledProcedureStepStart==""){
                $("#msg").html("预约时间不能为空");
                //如果账号密码为空，则需要及时强制终止该方法
                return false;
            }
            $.ajax({
                url : "workbench/register/appointment.do",
                data : {
                    "accessionNumber" : $.trim($("#schedule-accessionNumber").val()),
                    "scheduledProcedureStepStart" : scheduledProcedureStepStart
                },
                type : "post",
                dataType : "json",
                success : function (data) {
                    if(data.success){
                        pageList($("#registerPage").bs_pagination('getOption', 'currentPage'),$("#registerPage").bs_pagination('getOption', 'rowsPerPage'));
                        //关闭修改操作的模态窗口
                        $("#scheduledProcedureStepStartModal").modal("hide");
                    }else{
                        alert("预约失败");
                    }
                }
            })
        })
    });
    function pageList(pageNo,pageSize) {
        //将全选的复选框的√干掉
        $("#qx").prop("checked",false);
        //查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中
        $("#search-patientname").val($.trim($("#hidden-patientname").val()));
        $("#search-department").val($.trim($("#hidden-department").val()));
        $("#search-clinicianID").val($.trim($("#hidden-clinicianID").val()));
        $("#search-emergency").val($.trim($("#hidden-emergency").val()));
        $.ajax({
            url : "workbench/register/pageList.do",
            data : {
                "pageNo" : pageNo,
                "pageSize" : pageSize,
                "patientname" : $.trim($("#search-patientname").val()),
                "department" : $.trim($("#search-department").val()),
                "clinicianID" : $.trim($("#search-clinicianID").val()),
                "emergency" : $.trim($("#search-emergency").val())
                //"registrarID": "${user.id}"
            },
            type : "get",
            dataType : "json",
            success : function (result ) {
                /*
                    data
                        我们需要的：登记信息列表
                        [{登记1},{2},{3}] List<StudyInfo> sList
                        一会分页插件需要的：查询出来的总记录数
                        {"total":100} int total
                        {"total":100,"dataList":[{登记1},{2},{3}]}
                 */
                var data = result.data;
                var html = "";
                //每一个n就是每一个登记对象
                $.each(data.dataList,function (i,n) {
                    html += '<tr class="active">';
                    html += '<td><input type="checkbox" name="xz" value="'+n.accessionNumber+'"/></td>';
                    //html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
                    html += '<td>'+n.name+'</td>';
                    html += '<td>'+n.department+'</td>';
                    html += '<td>'+n.clinicianID+'</td>';
                    html += '<td>'+n.emergency+'</td>';
                    if(n.status=="1"){
                        const str1 = "待预约"
                        html += '<td>'+str1+'</td>';
                    }else if(n.status=="4"){
                        const str2 = "待修改"
                        html += '<td>'+str2+'</td>';
                    }else {
                        const str3 = "待登记"
                        html += '<td>'+str3+'</td>';
                    }
                    html += '</tr>';
                })
                $("#registerBody").html(html);
                //计算总页数
                var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
                //数据处理完毕后，结合分页查询，对前端展现分页信息
                $("#registerPage").bs_pagination({
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
    <input type="hidden" id="hidden-patientname"/>
    <input type="hidden" id="hidden-department"/>
    <input type="hidden" id="hidden-clinicianID"/>
    <input type="hidden" id="hidden-emergency"/>
    <!-- 预约时间的模态窗口 -->
    <div class="modal fade" id="scheduledProcedureStepStartModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">预约检查时间</h4>
                </div>
                <div class="modal-body">

                    <form class="form-horizontal" role="form">

                        <input type="hidden" id="schedule-id"/>

                        <div class="form-group">
                            <input type="hidden" id="schedule-accessionNumber" name="accessionNumber">
                        </div>

                        <div class="form-group">
                            <label for="schedule-registrarID" class="col-sm-2 control-label">登记员</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input class="form-control" id="schedule-registrarname" name="registrarname" value="${user.name}" disabled>
                            </div>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="hidden" class="form-control" id="schedule-registrarID" name="registrarID" value="${user.id}">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="schedule-patientname" class="col-sm-2 control-label">病人名称</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="schedule-patientname" disabled>
                            </div>
                            <label for="schedule-department" class="col-sm-2 control-label">检查科室</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="schedule-department" name="department" disabled>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="schedule-scheduledProcedureStepStart" class="col-sm-2 control-label">预约时间<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control time" autocomplete="off" id="schedule-scheduledProcedureStepStart" name="scheduledProcedureStepStart">
                            </div>
                        </div>
                    </form>
                    <label for="msg" class="col-sm-2 control-label"></label>
                    <div class="col-sm-10" style="width: 300px;">
                        <span id="msg" style="color: red"></span>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="appointmentBtn">确定</button>
                </div>
                <br>
            </div>
        </div>
    </div>
    <div>
        <div style="position: relative; left: 10px; top: -10px;">
            <div class="page-header">
                <h3>登记列表</h3>
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
                            <div class="input-group-addon">临床医生姓名</div>
                            <input class="form-control" type="text" id="search-clinicianID">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group">
                            <div class="input-group-addon">急诊</div>
                            <select class="form-control" id="search-emergency">
                                <option></option>
                                <option>是</option>
                                <option>否</option>
                            </select>
                        </div>
                    </div>
                    <button type="button" id="searchBtn" class="btn btn-default">查询</button>
                </form>
            </div>
            <div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
                <div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-primary" onclick="window.location.href='workbench/register/save.jsp';"><span class="glyphicon glyphicon-plus"></span> 创建</button>
                    <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
                    <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
                    <button type="button" class="btn btn-default" id="scheduleBtn"><span class="glyphicon glyphicon-time"></span> 预约</button>
                </div>
            </div>
            <div style="position: relative;top: 10px;">
                <table class="table table-hover">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td><input type="checkbox" id="qx"/></td>
                        <td>病人姓名</td>
                        <td>科室</td>
                        <td>临床医生姓名</td>
                        <td>急诊</td>
                        <td>状态</td>
                    </tr>
                    </thead>
                    <tbody id="registerBody">
                        <%--这里是登记列表，需要从后台拿数据再写入--%>
                    </tbody>
                </table>
            </div>
            <div style="height: 50px; position: relative;top: 30px;">
                <div id="registerPage">
                    <%--翻页插件--%>
                </div>
            </div>
        </div>
    </div>
</body>
</html>