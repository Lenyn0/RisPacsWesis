<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


    <script type="text/javascript">

        $(function(){
            //为修改按钮绑定事件，执行修改登记信息操作
            $("#searchBtn").click(function () {

                $("#hidden-studyID").val($.trim($("#search-studyID").val()));
                $("#hidden-department").val($.trim($("#search-department").val()));
                $("#hidden-patientName").val($.trim($("#search-patientName").val()));
                $("#hidden-emergency").val($.trim($("#search-emergency").val()));

                pageList(1,2);

            })

        });

        function transfer(wtt) {
            var str=wtt.split(',');
            //使用传统请求
            window.location.href = "workbench/check/detail.do?accessionNumber="+str[0];
        }

        function pageList(pageNo, pageSize) {
            //检查号，病人名，科室，是否急诊，状态
            //accessionNumber
            //patientID转化为病人名
            //department
            //emergency
            //status
            $("#qx").prop("checked", false);

            //查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中
            $("#search-studyID").val($.trim($("#hidden-studyID").val()));
            $("#search-department").val($.trim($("#hidden-department").val()));
            $("#search-patientName").val($.trim($("#hidden-patientName").val()));
            $("#search-emergency").val($.trim($("#hidden-emergency").val()));

            var html = "";
            $.ajax({
                url: "workbench/check/pageList_check.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    // 有时间可能会做查询。
                    "accessionNumber" : $.trim($("#search-studyID").val()),
                    "department" : $.trim($("#search-department").val()),
                    "patientName" : $.trim($("#search-patientName").val()),
                    "emergency" : $.trim($("#search-emergency").val())
                },
                type: "get",
                dataType: "json",
                success: function (result) {
                    var data = result.data;
                    $.each(data.dataList, function (i,n) {
                        //检查号，病人号，科室，是否急诊，检查状态
                        //将等待检查的记录输出
                        if (n.status == "3") {
                            //alert("显示status为3（等待检查）的数据");
                            const wait01 = "等待检查";
                            html += "<tr class=\"active\">";
                            html += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"transfer(";
                            var temp = "\'" + n.accessionNumber+","+n.patientID + ","+n.name +","+wait01 + "\'";
                            html += temp + ")\">" + n.accessionNumber + "</a></td>";
                            /*html += "<td id='patientID'>" + n.patientID + "</td>";*/
                            html += "<td id='name'>" + n.name + "</td>";
                            html += "<td>" + n.department + "</td>";
                            html += "<td>" + n.emergency + "</td>";
                            html += "<td>" + wait01 + "</td>";
                            html += "</tr>";
                        }else if (n.status == "5") {
                            //alert("显示status为3（等待检查）的数据");
                            const wait01 = "正在检查";
                            html += "<tr class=\"active\">";
                            html += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"transfer(";
                            var temp = "\'" + n.accessionNumber+","+n.patientID + ","+n.name +","+wait01 + "\'";
                            html += temp + ")\">" + n.accessionNumber + "</a></td>";
                            /*html += "<td id='patientID'>" + n.patientID + "</td>";*/
                            html += "<td id='name'>" + n.name + "</td>";
                            html += "<td>" + n.department + "</td>";
                            html += "<td>" + n.emergency + "</td>";
                            html += "<td>" + wait01 + "</td>";
                            html += "</tr>";
                        }

                    })

                    $("#check_body").html(html);

                    //计算总页数
                    var totalPages = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;
                    //数据处理完毕后，结合分页查询，对前端展现分页信息
                    $("#checkPage").bs_pagination({
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

                        //该回调函数是在，点击分页组件的时候触发的
                        onChangePage: function (event, data) {
                            pageList(data.currentPage, data.rowsPerPage);
                        }
                    });

                }
            })
        }

        $(document).ready(function () {
            pageList(1, 2);
        });

    </script>

</head>

<body>

<input type="hidden" id="hidden-studyID"/>
<input type="hidden" id="hidden-department"/>
<input type="hidden" id="hidden-patientName"/>
<input type="hidden" id="hidden-emergency"/>

<%--填写报告模块，自己实现--%>
<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>等待检查信息列表</h3>
        </div>
    </div>
</div>

<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">检查号</div>
                        <input class="form-control" type="text" id="search-studyID">
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
                        <div class="input-group-addon">病人名</div>
                        <input class="form-control" type="text" id="search-patientName" />
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">是否急诊</div>
                        <input class="form-control" type="text" id="search-emergency">
                    </div>
                </div>

                <button type="button" id="searchBtn" class="btn btn-default">查询</button>

            </form>
        </div>

        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td>检查号</td>
                    <td>病人名</td>
                    <td>科室</td>
                    <td>是否急诊</td>
                    <td>检查状态</td>
                    <!--检查状态只有等待检查，等待修改的信息隐藏-->
                </tr>
                </thead>
                <tbody id="check_body">
                
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 20px;">

            <div id = "checkPage"></div>

        </div>
    </div>
</div>

</body>
</html>