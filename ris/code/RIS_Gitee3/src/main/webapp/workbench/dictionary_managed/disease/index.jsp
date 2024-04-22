<%--
  Created by IntelliJ IDEA.
  User: 25816
  Date: 2021/7/24
  Time: 17:41
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>



    <script type="text/javascript">

        $(function(){

            //为添加按钮绑定事件
            $("#addBtn").click(function () {
                 $("#createDiseaseModal").modal("show");
            })
            //为保存按钮绑定事件
            $("#saveBtn").click(function () {

                $.ajax({

                    url : "workbench/DiseaseDictionary/save.do",
                    data : {
                        "createUserID" : $.trim($("#create-createUserID").text()),
                        "bodyPart" : $.trim($("#create-bodyPart").val()),
                        "name" : $.trim($("#create-name").val()),
                        "description" : $.trim($("#create-description").val())
                    },
                    type : "post",
                    dataType : "json",
                    success : function (data) {

                        if(data) {

                            pageList(1, $("#DiseasePage").bs_pagination('getOption', 'rowsPerPage'));

                            $("#DiseaseAddForm")[0].reset();
                            //关闭添加操作的模态窗口
                            $("#createDiseaseModal").modal("hide");
                            $(".modal-backdrop.fade").hide();

                        }else{

                            alert("添加疾病字典失败");

                        }

                    }

                })

            })
            //为删除按钮绑定事件
            $("#deleteBtn").click(function () {

                //找到复选框中所有挑√的复选框的jquery对象
                var $xz = $("input[name=xz]:checked");

                if($xz.length==0){

                    alert("请选择需要删除的记录");

                    //肯定选了，而且有可能是1条，有可能是多条
                }else{

                    if(confirm("确定删除所选中的记录吗？")){

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

                        //alert(param);
                        $.ajax({

                            url : "workbench/DiseaseDictionary/delete.do",
                            data : param,
                            type : "post",
                            dataType : "json",
                            success : function (data) {

                                alert(data);
                                if(data){
                                    alert("删除成功");
                                    //删除成功后
                                    //回到第一页，维持每页展现的记录数
                                    pageList(1,$("#DiseasePage").bs_pagination('getOption', 'rowsPerPage'));
                                }else{

                                    alert("删除市场活动失败");

                                }


                            }

                        })


                    }




                }


            })
            //为修改按钮绑定事件，打开修改操作的模态窗口
            $("#editBtn").click(function () {

                var $xz = $("input[name=xz]:checked");

                if($xz.length==0){

                    alert("请选择需要修改的记录");

                }else if($xz.length>1){

                    alert("只能选择一条记录进行修改");

                    //肯定只选了一条
                }else{

                    var id = $xz.val();

                    $.ajax({

                        url : "workbench/Dictionary/getAll.do",
                        data : {

                            "id" : id

                        },
                        type : "get",
                        dataType : "json",
                        success : function (data) {

                            $("#edit-bodyPart").val(data.bodyPart);
                            $("#edit-name").val(data.name);
                            $("#edit-description").val(data.description);
                            $("#edit-id").val(data.id);
                            //所有的值都填写好之后，打开修改操作的模态窗口
                            $("#editDiseaseModal").modal("show");

                        }

                    })

                }

            })
            //更新数据（修改）
            $("#updateBtn").click(function () {

                $.ajax({

                    url : "workbench/Disease/update.do",
                    data : {

                        /*modifyUserID
                        bodyPart
                        name
                        description*/
                        "id" : $.trim($("#edit-id").val()),
                        "bodyPart" : $.trim($("#edit-bodyPart").val()),
                        "name" : $.trim($("#edit-name").val()),
                        "description" : $.trim($("#edit-description").val()),
                        "modifyUserID" : $.trim($("#edit-UserID").text())

                    },
                    type : "post",
                    dataType : "json",
                    success : function (data) {

                        if(data){

                            pageList($("#DiseasePage").bs_pagination('getOption', 'currentPage')
                                ,$("#DiseasePage").bs_pagination('getOption', 'rowsPerPage'));


                            //关闭修改操作的模态窗口
                            $("#editDiseaseModal").modal("hide");



                        }else{

                            alert("修改疾病字典失败");

                        }




                    }

                })

            })

            $("#qx").click(function () {

                $("input[name=xz]").prop("checked",this.checked);

            })

            $("#Disease_body").on("click",$("input[name=xz]"),function () {

                $("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);

            })

            $("#searchBtn").click(function () {

                /*

                    点击查询按钮的时候，我们应该将搜索框中的信息保存起来,保存到隐藏域中

                 */

                $("#hidden-DiseaseID").val($.trim($("#search-DiseaseID").val()));
                $("#hidden-bodyParts").val($.trim($("#search-bodyPart").val()));
                $("#hidden-DiseaseName").val($.trim($("#search-DiseaseName").val()));

                pageList(1,2);

            })


        });
        function transfer(wtt) {
            var str=wtt.split(',');
            //待定
        }

        function pageList(pageNo, pageSize) {
            //将全选的复选框的√干掉
            $("#qx").prop("checked", false);

            //查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中
            $("#search-DiseaseID").val($.trim($("#hidden-DiseaseID").val()));
            $("#search-bodyPart").val($.trim($("#hidden-bodyParts").val()));
            $("#search-DiseaseName").val($.trim($("#hidden-DiseaseName").val()));

            //var DiseaseName = $("#search-DiseaseName").children('option:selected').val();
            var html = "";
            $.ajax({
                url: "workbench/Disease/pageList.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    // 有时间可能会做查询。
                    "DiseaseID" : $.trim($("#search-DiseaseID").val()),
                    "bodyParts" : $.trim($("#search-bodyPart").val()),
                    "DiseaseName" : $.trim($("#search-DiseaseName").val())
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    $.each(data.dataList, function (i, Disease) {
                            const wait01 = "等待填写";
                            html += "<tr class=\"active\">";
                            /*input type="checkbox" name="xz" value="'+n.id+'"/*/
                            html += "<td><input type=\"checkbox\" name=\"xz\" value=\"" + Disease.id + "\"/></td>";                                                                                                      /* \';">*/
                            //html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/write_report/report_edit.jsp?id=\'+n.id+\'&patientID=\'+n.patientID;">'+n.accessionNumber+'</a></td>';
                            //html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/write_report/report_edit.jsp\';">'+Disease.accessionNumber+'</a></td>';
                            html += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"transfer(";
                            //const temp="\'"+Disease.accessionNumber+"+"+Disease.patientID+"\'";
                            var temp = "\'" + Disease.id+","+ "\'";
                            html += temp + ")\">" + Disease.id + "</a></td>";
                            html += "<td id='patientID'>" + Disease.name + "</td>";
                            html += "<td id='name'>" + Disease.bodyPart + "</td>";
                            html += "<td>" + Disease.description + "</td>";
                            html += "</tr>";

                    })

                    $("#Disease_body").html(html);

                    //计算总页数
                    var totalPages = data.total % pageSize == 0 ? data.total / pageSize : parseInt(data.total / pageSize) + 1;
                    //数据处理完毕后，结合分页查询，对前端展现分页信息
                    $("#DiseasePage").bs_pagination({
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

<input type="hidden" id="hidden-DiseaseID"/>
<input type="hidden" id="hidden-bodyParts"/>
<input type="hidden" id="hidden-DiseaseName"/>

<%--填写报告模块，自己实现--%>
<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>疾病字典列表</h3>
        </div>
    </div>
</div>

<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">疾病号</div>
                        <input class="form-control" type="text" id="search-DiseaseID">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">疾病名称</div>
                        <input class="form-control" type="text" id="search-DiseaseName">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">疾病部位</div>
                        <select class="form-control" id="search-bodyPart" name="body">
                            <option></option>
                            <c:forEach items="${bodyPartsList}" var="t">
                                <option value="${t.value}">${t.text}</option>
                            </c:forEach>
                        </select> </div>
                </div>

                <button type="button" id="searchBtn" class="btn btn-default">查询</button>

            </form>
        </div>

        <div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
                <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
                <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
            </div>

        </div>

        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <%--input type="checkbox" name="xz" value="'+n.id+'"/--%>
                    <td><input type="checkbox" id="qx"/></td>
                    <td>疾病号</td>
                    <td>疾病名称</td>
                    <td>疾病部位</td>
                    <td>疾病描述</td>
                </tr>
                </thead>
                <tbody id="Disease_body">
                
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 20px;">

            <div id = "DiseasePage"></div>

        </div>
    </div>
</div>

<%--//增加模态窗口Disease--%>
<div class="modal fade" id="createDiseaseModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建疾病信息</h4>
            </div>
            <div class="modal-body">

                <form id="DiseaseAddForm" class="form-horizontal" role="form">
                <%-- 身体部位，疾病名称，疾病描述--%>
                    <div class="form-group">
                        <label  class="col-sm-2 control-label">创建者<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <span class="form-control"  id="create-createUserName" style="text-align: left;/*line-height:3px;*/" disabled>${user.name}</span>
                        </div>
                        <label  class="col-sm-2 control-label">创建者ID<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <span class="form-control"  id="create-createUserID" style="text-align: left;/*line-height:3px;*/" disabled>${user.id}</span>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="create-bodyPart" class="col-sm-2 control-label">疾病部位<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-bodyPart" name="body">
                                <option></option>
                                <c:forEach items="${bodyPartsList}" var="t">
                                    <option value="${t.value}">${t.text}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-name" class="col-sm-2 control-label">疾病名称</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">疾病描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <!--

                    data-dismiss="modal"
                        表示关闭模态窗口

                -->
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editDiseaseModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改疾病信息</h4>
            </div>
            <div class="modal-body">

                <form id="DiseaseEditForm" class="form-horizontal" role="form">
                    <%-- 身体部位，疾病名称，疾病描述--%>
                    <div class="form-group">
                        <label  class="col-sm-2 control-label">修改者<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <span class="form-control"  id="edit-UserName" style="text-align: left;/*line-height:3px;*/" disabled>${user.name}</span>
                        </div>
                        <label  class="col-sm-2 control-label">修改者ID<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <span class="form-control"  id="edit-UserID" style="text-align: left;/*line-height:3px;*/" disabled>${user.id}</span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label  class="col-sm-2 control-label">疾病号<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input class="form-control"  id="edit-id" style="text-align: left;/*line-height:3px;*/" disabled>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-bodyPart" class="col-sm-2 control-label">疾病部位<span style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-bodyPart" name="body">
                                <option></option>
                                <c:forEach items="${bodyPartsList}" var="t">
                                    <option value="${t.value}">${t.text}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-name" class="col-sm-2 control-label">疾病名称</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="edit-description" class="col-sm-2 control-label">疾病描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <!--

                    data-dismiss="modal"
                        表示关闭模态窗口

                -->
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateBtn">保存</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
