<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


    <script type="text/javascript">

        $(function () {
            $("#qx").click(function () {

                $("input[name=xz]").prop("checked", this.checked);

            })
            $("#report_body").on("click", $("input[name=xz]"), function () {

                $("#qx").prop("checked", $("input[name=xz]").length == $("input[name=xz]:checked").length);

            })

            $("#searchBtn").click(function () {

                /*

                    点击查询按钮的时候，我们应该将搜索框中的信息保存起来,保存到隐藏域中

                 */

                $("#hidden-studyID").val($.trim($("#search-studyID").val()));
                $("#hidden-patientID").val($.trim($("#search-patientID").val()));
                $("#hidden-patientName").val($.trim($("#search-patientName").val()));
                $("#hidden-status").val($.trim($("#search-status").val()));

                pageList(1, 2);

            })
            //为修改按钮绑定事件，打开修改操作的模态窗口
            $("#editBtn").click(function () {

                var $xz = $("input[name=xz]:checked");

                if ($xz.length == 0) {

                    alert("请选择需要修改的记录");

                } else if ($xz.length > 1) {

                    alert("只能选择一条记录进行修改");

                    //肯定只选了一条
                } else {
                    //将需要修改的绑定的ID传进去
                    var studyID = $xz.val();
                    const db1 = window.localStorage;
                    db1.setItem("studyID", studyID);//检查号
                }

            })

            $("#createBtn").click(function () {

                var $xz = $("input[name=xz]:checked");

                if ($xz.length == 0) {

                    alert("请选择需要修改的记录");

                } else if ($xz.length > 1) {

                    alert("只能选择一条记录进行修改");

                    //肯定只选了一条
                } else {
                    //将需要修改的绑定的检查号ID传进去
                    var studyID = $xz.val();
                    //alert(patientID);
                    const db1 = window.localStorage;
                    db1.setItem("studyID", studyID);
                }

            })

        });

        //onclick="transfer('wtt.accessionNumber, wtt.patientID ,wtt.name ,wait01')
        function transfer(wtt) {
            var str = wtt.split(',');
            const db = window.localStorage;
            db.setItem("studyID", str[0]);
            db.setItem("patientID", str[1]);
            db.setItem("name", str[2]);
            //获取properties配置文件中的属性值
            <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%--            <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>--%>
<%--            <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
            <%
                // properties 配置文件名称
                ResourceBundle res = ResourceBundle.getBundle("ris");
            %>
            var dcm4cheeIP = "<%=res.getString("dcm4cheeIP")%>";
            // alert('str[4]');
            // alert(str[4]);
            window.open("http://"+dcm4cheeIP+":8080/weasis-pacs-connector/weasis?&cdb&studyUID=" + str[4], "_parent");
            //跳转
            if (str[3] == "等待填写") {
                window.location.href = "workbench/write_report/report_edit.jsp";
            } else {
                window.location.href = "workbench/write_report/report_revise.jsp";
            }

        }

        function pageList(pageNo, pageSize) {
            //将全选的复选框的√干掉
            $("#qx").prop("checked", false);

            //查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中
            $("#search-studyID").val($.trim($("#hidden-studyID").val()));
            $("#search-patientID").val($.trim($("#hidden-patientID").val()));
            $("#search-patientName").val($.trim($("#hidden-patientName").val()));
            $("#search-status").val($.trim($("#hidden-status").val()));

            var html = "";
            $.ajax({
                url: "workbench/Report/pageList.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    // 有时间可能会做查询。
                    "studyID": $.trim($("#search-studyID").val()),
                    "patientID": $.trim($("#search-patientID").val()),
                    "patientName": $.trim($("#search-patientName").val()),
                    "status": $.trim($("#search-status").val())
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    $.each(data.dataList, function (i, wtt) {
                        if (wtt.status == "6") {
                            //alert("显示status为6的数据");
                            const wait01 = "等待填写";
                            html += "<tr class=\"active\">";
                            /*input type="checkbox" name="xz" value="'+n.id+'"/*/
                            //html += "<td><input type=\"checkbox\" name=\"xz\" value=\"" + wtt.accessionNumber + "\"/></td>";                                                                                                      /* \';">*/
                            //html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/write_report/report_edit.jsp?id=\'+n.id+\'&patientID=\'+n.patientID;">'+n.accessionNumber+'</a></td>';
                            //html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/write_report/report_edit.jsp\';">'+wtt.accessionNumber+'</a></td>';
                            html += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"transfer(";
                            //const temp="\'"+wtt.accessionNumber+"+"+wtt.patientID+"\'";
                            var temp = "\'" + wtt.accessionNumber + "," + wtt.patientID + "," + wtt.name + "," + wait01 + "," + wtt.studyInstanceUID + "\'";
                            html += temp + ")\">" + wtt.accessionNumber + "</a></td>";
                            html += "<td id='patientID'>" + wtt.patientID + "</td>";
                            html += "<td id='name'>" + wtt.name + "</td>";
                            html += "<td>" + wtt.department + "</td>";
                            html += "<td>" + wtt.emergency + "</td>";
                            html += "<td>" + wait01 + "</td>";
                            html += "</tr>";
                        } else if (wtt.status == "8") {
                            //alert("显示status为6的数据");
                            const wait01 = "等待修改";
                            html += "<tr class=\"active\">";
                            //html += "<td><input type=\"checkbox\" name=\"xz\" value=\"" + wtt.accessionNumber + "\"/></td>";                                                                                                      /* \';">*/
                            //html += "<td><a style=\"text-decoration: none; cursor: pointer;\">" + wtt.accessionNumber + "</a></td>";
                            html += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"transfer(";
                            //const temp="\'"+wtt.accessionNumber+"+"+wtt.patientID+"\'";
                            var temp = "\'" + wtt.accessionNumber + "," + wtt.patientID + "," + wtt.name + "," + wait01 + "," + wtt.studyInstanceUID + "\'";
                            html += temp + ")\">" + wtt.accessionNumber + "</a></td>";
                            html += "<td>" + wtt.patientID + "</td>";
                            html += "<td>" + wtt.name + "</td>";
                            html += "<td>" + wtt.department + "</td>";
                            html += "<td>" + wtt.emergency + "</td>";
                            html += "<td>" + wait01 + "</td>";
                            html += "</tr>";
                        }

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
            pageList(1, 2);
        });
        //为全选的复选框绑定事件，触发全选操作


    </script>

</head>

<body>

<input type="hidden" id="hidden-studyID"/>
<input type="hidden" id="hidden-patientID"/>
<input type="hidden" id="hidden-patientName"/>
<input type="hidden" id="hidden-status"/>

<%--填写报告模块，自己实现--%>
<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>待写报告列表</h3>
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
                        <div class="input-group-addon">病人号</div>
                        <input class="form-control" type="text" id="search-patientID">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">病人名</div>
                        <input class="form-control" type="text" id="search-patientName"/>
                    </div>
                </div>
                <%--<div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">检查状态</div>
                        <input class="form-control" type="text" id="search-status">
                    </div>
                </div>--%>

                <button type="button" id="searchBtn" class="btn btn-default">查询</button>

            </form>
        </div>

        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <%--<button type="button" class="btn btn-primary" onclick="window.location.href='workbench/write_report/report_editByButton.jsp';" id="createBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
                <button type="button" class="btn btn-default" onclick="window.location.href='workbench/write_report/report_revise.jsp';" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>--%>
                <%--<button type="button" class="btn btn-primary" onclick="window.location.href='workbench/write_report/test_session.jsp';"><span class="glyphicon glyphicon-plus"></span> 测试</button>--%>
                <%--<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>--%>
            </div>
        </div>

        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <%--input type="checkbox" name="xz" value="'+n.id+'"/--%>
                    <%--<td><input type="checkbox" id="qx"/></td>--%>
                    <td>检查号</td>
                    <td>病人号</td>
                    <td>病人名</td>
                    <td>科室</td>
                    <td>是否急诊</td>
                    <td>检查状态</td>
                    <!--报告状态包括：待填写和待修改；待审核和审核状态显示在报告审批界面-->
                </tr>
                </thead>
                <tbody id="report_body">
                <%-- <tr>
                     <td><input type="checkbox" /></td>
                     <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/write_report/report_edit.jsp';">0001</a></td>
                     <td>1234</td>
                     <td>2564</td>
                     <td>代填写</td>
                     <td>代填写</td>
                 </tr>--%>
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 20px;">

            <div id="ReportPage"></div>

        </div>
    </div>
</div>

</body>
</html>