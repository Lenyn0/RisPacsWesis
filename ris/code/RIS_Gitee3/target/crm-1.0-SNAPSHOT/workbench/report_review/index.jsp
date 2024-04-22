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
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

    <script type="text/javascript">

        function transfer(wtt) {
            var str=wtt.split(',');
            const db = window.localStorage;
            db.setItem("id", str[0]);
            db.setItem("patientID",str[2]);
            db.setItem("studyID",str[1]);
            //跳转
            window.location.href = "workbench/report_review/report_review.jsp";
        }

        function pageList(pageNo, pageSize) {
            //将全选的复选框的√干掉,复选框失败了
            $("#qx").prop("checked", false);

            //查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中
            $("#search-StudyID").val($.trim($("#hidden-StudyID").val()));
            $("#search-ReportID").val($.trim($("#hidden-ReportID").val()));
            $("#search-status").val($.trim($("#hidden-status").val()));

            var html = "";
            $.ajax({
                url: "workbench/Report/pageList_review.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    // 有时间可能会做查询。
                    "id": $.trim($("#search-ReportID").val()),//报告号
                    "studyID": $.trim($("#search-StudyID").val()),//检查号
                    "reportStatus": $.trim($("#search-status").val())//报告状态
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    $.each(data.dataList, function (i, wtt) {

                            //alert("显示status为6的数据");
                        const wait01 = "等待审核";
                        const wait02 = "审核完成";
                        const wait03 = "等待修改";
                        const wait04 = "完成打印";
                        html += "<tr class=\"active\">";
                        html += "<td><input type=\"checkbox\" name=\"xz\" value=\"" + wtt.id + "\"/></td>";                                                                                                      /* \';">*/
                        html += "<td><a id='id' style=\"text-decoration: none; cursor: pointer;\" onclick=\"transfer(";
                        var temp = "\'"+wtt.id+","+wtt.studyID+","+wtt.patientID+"\'";
                        html += temp + ")\">" + wtt.id + "</a></td>";
                        html += "<td id='studyID'>" + wtt.studyID + "</td>";
                        html += "<td id='patientID'>" + wtt.patientID + "</td>";
                        if (wtt.reportStatus == "1") {
                            html += "<td>" + wait01 + "</td>";
                        }
                        else if(wtt.reportStatus == "3") {
                            html += "<td>" + wait02 + "</td>";
                        }
                        else if(wtt.reportStatus == "2") {
                            html += "<td>" + wait03 + "</td>";
                        }
                        else if(wtt.reportStatus == "4") {
                            html += "<td>" + wait04 + "</td>";
                        }
                            html += "</tr>";

                    })

                    $("#report_body").html(html);

                    //计算总页数
                    var totalPages = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;
                    //数据处理完毕后，结合分页查询，对前端展现分页信息
                    $("#ReportPage").bs_pagination({
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
            //加载报告数据
            pageList(1, 2);
        });

        $(function(){
            $("#searchBtn").click(function () {

                $("#hidden-StudyID").val($.trim($("#search-StudyID").val()));
                $("#hidden-ReportID").val($.trim($("#search-ReportID").val()));
                $("#hidden-status").val($.trim($("#search-status").val()));
                pageList(1,2);

            })

            $("#qx").click(function () {

                $("input[name=xz]").prop("checked",this.checked);

            })
            $("#report_body").on("click",$("input[name=xz]"),function () {

                $("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);

            })

            //为打印按钮绑定事件
            $("#printBtn").click(function () {

                var $xz = $("input[name=xz]:checked");

                if($xz.length==0){

                    alert("请选择需要修改的记录");

                }else if($xz.length>1){

                    alert("只能选择一条记录进行打印");

                    //肯定只选了一条
                }else{
                    //把检查号传进去
                    var id = $xz.val();//报告号
                    window.location.href = "workbench/report_review/report_print.do?id="+id;
                    //应该是只有审核通过的才可以打印吧？

                }

            })

        });

    </script>
</head>
<body>

<input type="hidden" id="hidden-StudyID"/>
<input type="hidden" id="hidden-ReportID"/>
<input type="hidden" id="hidden-status"/>

<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>待审核报告列表</h3>
        </div>
    </div>
</div>

<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">
        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">报告号</div>
                        <input class="form-control" type="text" id="search-ReportID">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">检查号</div>
                        <input class="form-control" type="text" id="search-StudyID">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">检查状态</div>
                        <input class="form-control" type="text" id="search-status" />
                    </div>
                </div>

                <button type="button" id="searchBtn" class="btn btn-default">查询</button>

            </form>
        </div>

        <div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="printBtn"><span class="glyphicon glyphicon-plus"></span>打印</button>
            </div>
        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="qx"/></td>
                    <td>报告号</td>
                    <td>检查号</td>
                    <td>病人号</td>
                    <td>报告状态</td>
                    <!--报告状态包括：待填写和待修改；待审核和审核状态显示在报告审批界面-->
                </tr>
                </thead>
                <tbody id="report_body">

                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 20px;">

                <div id = "ReportPage"></div>

        </div>

    </div>

</div>
</body>
</html>