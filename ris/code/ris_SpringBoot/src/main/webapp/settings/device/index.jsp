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

    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">
    $(function(){
        pageList(1,5);
        $("#searchBtn").click(function () {
            /*点击查询按钮的时候，我们应该将搜索框中的信息保存起来,保存到隐藏域中*/
            $("#hidden-devicename").val($.trim($("#search-devicename").val()));
            $("#hidden-aet").val($.trim($("#search-aet").val()));
            $("#hidden-room").val($.trim($("#search-room").val()));
            pageList(1,5);
        })
        $("#create-devicename").on("input propertychange",function(){
            if($("#create-devicename").val()==""){
                $("#msg").html("设备名称不能为空");
                $("#saveBtn").attr("disabled",true);
            }else if($("#create-devicename").val()!=""&&$("#create-aet").val()!=""&&$("#create-port").val()!=""){
                $("#msg").html("");
                $("#saveBtn").attr("disabled",false);
            }else{
                $("#msg").html("");
                $("#saveBtn").attr("disabled",true);
            }
        })
        $("#create-aet").on("input propertychange",function(){
            if($("#create-aet").val()==""){
                $("#msg").html("aet不能为空");
                $("#saveBtn").attr("disabled",true);
            }else if($("#create-devicename").val()!=""&&$("#create-aet").val()!=""&&$("#create-port").val()!=""){
                $("#msg").html("");
                $("#saveBtn").attr("disabled",false);
            }else{
                $("#msg").html("");
                $("#saveBtn").attr("disabled",true);
            }
        })
        $("#create-port").on("input propertychange",function(){
            if($("#create-port").val()==""){
                $("#msg").html("端口不能为空");
                $("#saveBtn").attr("disabled",true);
            }else if($("#create-devicename").val()!=""&&$("#create-aet").val()!=""&&$("#create-port").val()!=""){
                $("#msg").html("");
                $("#saveBtn").attr("disabled",false);
            }else{
                $("#msg").html("");
                $("#saveBtn").attr("disabled",true);
            }
        })
        $("#saveBtn").click(function(){
            $("#deviceForm").submit();
        })
        //为全选的复选框绑定事件，触发全选操作
        $("#qx").click(function () {
            $("input[name=xz]").prop("checked",this.checked);
        })
        $("#deviceBody").on("click",$("input[name=xz]"),function () {
            $("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
        })
        $("#deleteBtn").click(function () {
            //找到复选框中所有挑√的复选框的jquery对象
            var $xz = $("input[name=xz]:checked");
            if($xz.length==0){
                alert("请选择需要删除的设备");
                //肯定选了，而且有可能是1条，有可能是多条
            }else{
                if(confirm("确定删除所选中的设备吗？")){
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
                        url : "settings/device/delete.do",
                        data : param,
                        type : "post",
                        dataType : "json",
                        success : function (data) {
                            if(data.success){
                                //删除成功后
                                //回到第一页，维持每页展现的记录数
                                pageList(1,$("#devicePage").bs_pagination('getOption', 'rowsPerPage'));
                            }else{
                                alert("删除设备失败");
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
        $("#search-devicename").val($.trim($("#hidden-devicename").val()));
        $("#search-aet").val($.trim($("#hidden-aet").val()));
        $("#search-room").val($.trim($("#hidden-room").val()));
        $.ajax({
            url : "settings/device/pageList.do",
            data : {
                "pageNo" : pageNo,
                "pageSize" : pageSize,
                "devicename" : $.trim($("#search-devicename").val()),
                "aet" : $.trim($("#search-aet").val()),
                "room" : $.trim($("#search-room").val())
            },
            type : "get",
            dataType : "json",
            success : function (result) {
                var data = result.data;
                var html = "";
                $.each(data.dataList,function (i,n) {
                    html += '<tr class="active">';
                    html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
                    html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'settings/device/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
                    html += '<td>'+n.aet+'</td>';//这里是idea的bug，不能用n.aet
                    html += '<td>'+n.room+'</td>';
                    html += '</tr>';
                })
                $("#deviceBody").html(html);
                //计算总页数
                var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
                //数据处理完毕后，结合分页查询，对前端展现分页信息
                $("#devicePage").bs_pagination({
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
<input type="hidden" id="hidden-devicename"/>
<input type="hidden" id="hidden-aet"/>
<input type="hidden" id="hidden-room"/>
<!-- 创建设备的模态窗口 -->
<div class="modal fade" id="createDeviceModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">新增设备</h4>
            </div>
            <div class="modal-body">

                <form action="settings/device/save.do" id="deviceForm" method="post" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-devicename" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" autocomplete="off" id="create-devicename" name="devicename">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-aet" class="col-sm-2 control-label">aet<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" autocomplete="off" id="create-aet" name="aet">
                        </div>
                        <label for="create-port" class="col-sm-2 control-label">端口<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" autocomplete="off" id="create-port" name="port">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-ip" class="col-sm-2 control-label">IP</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" autocomplete="off" id="create-ip" name="ip">
                        </div>
                        <label for="create-room" class="col-sm-2 control-label">房间</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" autocomplete="off" id="create-room" name="room">
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
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveBtn" disabled="disabled">保存</button>
            </div>
        </div>
    </div>
</div>
<div>
    <div style="position: relative; left: 30px; top: -10px;">
        <div class="page-header">
            <h3>设备列表</h3>
        </div>
    </div>
</div>

<div class="btn-toolbar" role="toolbar" style="position: relative; height: 80px; left: 30px; top: -10px;">
    <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

        <div class="form-group">
            <div class="input-group">
                <div class="input-group-addon">名称</div>
                <input class="form-control" type="text" id="search-devicename">
            </div>
        </div>
        <div class="form-group">
            <div class="input-group">
                <div class="input-group-addon">aet</div>
                <input class="form-control" type="text" id="search-aet">
            </div>
        </div>
        <div class="form-group">
            <div class="input-group">
                <div class="input-group-addon">房间</div>
                <input class="form-control" type="text" id="search-room">
            </div>
        </div>
        <button type="button" class="btn btn-default" id="searchBtn">查询</button>
    </form>
</div>


<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px; width: 110%; top: 20px;">
    <div class="btn-group" style="position: relative; top: 18%;">
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createDeviceModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
        <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
    </div>

</div>

<div style="position: relative; left: 30px; top: 40px; width: 110%">
    <table class="table table-hover">
        <thead>
        <tr style="color: #B3B3B3;">
            <td><input type="checkbox" id="qx"/></td>
            <%--<td>序号</td>--%>
            <td>名称</td>
            <td>aet</td>
            <td>房间</td>
        </tr>
        </thead>
        <tbody id="deviceBody">

        </tbody>
    </table>
</div>

<div style="height: 50px; position: relative;top: 30px; left: 30px;">
    <div id="devicePage">
        <%--翻页插件--%>
    </div>
</div>

</body>
</html>