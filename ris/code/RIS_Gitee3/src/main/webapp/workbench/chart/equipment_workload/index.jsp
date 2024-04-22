<%--
  Created by IntelliJ IDEA.
  User: 25816
  Date: 2021/7/18
  Time: 10:00
  To change this template use File | Settings | File Templates.
--%>
<%--
  Created by IntelliJ IDEA.
  User: 25816
  Date: 2021/7/18
  Time: 9:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

    /*

        需求：设备工作量统计表

            根据检查信息表study_info中的不同的设备的数量进行一个统计，最终形成一个漏斗图（倒三角）
            （注意：检查信息表中设备对应的是设备的id，因此需要将其id转化为名称）

            将统计出来的阶段的数量比较多的，往上面排列
            将统计出来的阶段的数量比较少的，往下面排列

            sql:
                按照名称进行分组

                resultType="map"

                select

                studyDevice,count(*)

                from tbl_study_info

                group by studyDevice



     */

%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>设备工作量统计报表</title>
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
    <script src="ECharts/echarts.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>


    <script>

        $(function () {

            $("#searchBtn").click(function (){
                $.ajax({

                    url : "workbench/chart/equipment_workload/getCharts.do",
                    data : {
                        /*flag = re
                        startData
                        endData =*/
                        "flag" : "1",
                        "startDate" : $.trim($("#search-startTime").val()),
                        "endDate" : $.trim($("#search-endTime").val())
                    },
                    type : "post",
                    dataType : "json",
                    success : function (data) {

                        var myChart = echarts.init(document.getElementById('main'));

                        //我们要画的图
                        var option = {
                            title: {
                                text: '设备工作量统计图',
                                subtext: '统计不同设备工作量的柱状图'
                            },
                            toolbox: { //可视化的工具箱
                                show: true,
                                feature: {
                                    dataView: { //数据视图
                                        show: true
                                    },
                                    restore: { //重置
                                        show: true
                                    },
                                    dataZoom: { //数据缩放视图
                                        show: true
                                    },
                                    saveAsImage: {//保存图片
                                        show: true
                                    },
                                    magicType: {//动态类型切换
                                        type: ['bar', 'line']
                                    }
                                }
                            },
                            /*grid:{left:'2%',right:'2%',bottom:'0%',containLabel: true},*/
                            grid: {
                                left: '15%',
                                bottom:'35%'
                            },
                            xAxis: {
                                type: 'category',
                                data: data.name,
                                // -----------------------------------------------------横坐标垂直显示
                                axisLabel: {
                                    interval:0,rotate:20
                                },

                                // -----------------------------------------------------横坐标垂直显示
                            },
                            yAxis: {
                                type: 'value'
                            },
                            series: [{
                                data: data.value,
                                type: 'bar',
                                showBackground: true,
                                backgroundStyle: {
                                    color: 'rgba(100, 149, 237, 0.2)'
                                }
                            }]
                        };
                        myChart.setOption(option);

                    }

                })
            })
            $("#searchBtn2").click(function (){
                var time = $.trim($("#select_limits").val());
                var flag = "1";
                /*写的获取前端的值为1是指定日期 为2是近一周 3是近一月 4是仅一季度 5是近一年*/
                if(time=="近一周")flag = "2";
                else if(time=="近一个月")flag = "3";
                else if(time=="近一个季度")flag = "4";
                else if(time=="近半年")flag = "6";
                else if(time=="近一年")flag = "5";
                $.ajax({

                    url : "workbench/chart/equipment_workload/getCharts.do",
                    data : {
                        /*flag = re
                        startData
                        endData =*/
                        "flag" : flag,
                        "startDate" : $.trim($("#search-startTime").val()),
                        "endDate" : $.trim($("#search-endTime").val())
                    },
                    type : "post",
                    dataType : "json",
                    success : function (data) {

                        var myChart = echarts.init(document.getElementById('main'));

                        //我们要画的图
                        var option = {
                            title: {
                                text: '设备工作量统计图',
                                subtext: '统计不同设备工作量的柱状图'
                            },
                            toolbox: { //可视化的工具箱
                                show: true,
                                feature: {
                                    dataView: { //数据视图
                                        show: true
                                    },
                                    restore: { //重置
                                        show: true
                                    },
                                    dataZoom: { //数据缩放视图
                                        show: true
                                    },
                                    saveAsImage: {//保存图片
                                        show: true
                                    },
                                    magicType: {//动态类型切换
                                        type: ['bar', 'line']
                                    }
                                }
                            },
                            /*grid:{left:'2%',right:'2%',bottom:'0%',containLabel: true},*/
                            grid: {
                                left: '15%',
                                bottom:'35%'
                            },
                            xAxis: {
                                type: 'category',
                                data: data.name,
                                // -----------------------------------------------------横坐标垂直显示
                                axisLabel: {
                                    interval:0,rotate:20
                                },

                                // -----------------------------------------------------横坐标垂直显示
                            },
                            yAxis: {
                                type: 'value'
                            },
                            series: [{
                                data: data.value,
                                type: 'bar',
                                showBackground: true,
                                backgroundStyle: {
                                    color: 'rgba(100, 149, 237, 0.2)'
                                }
                            }]
                        };

                        myChart.setOption(option);

                    }

                })
            })


        })

    </script>
</head>
<body>

<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>设备工作量统计图表</h3>
        </div>
    </div>
</div>

<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 18%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">起始日期</div>
                        <input class="form-control time" type="text" id="search-startTime">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control time" type="text" id="search-endTime">
                    </div>
                </div>

                <button type="button" id="searchBtn" class="btn btn-default">查询</button>

            </form>

            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">选择范围</div>
                        <select  class="form-control" id="select_limits" >
                            <option ></option>
                            <option >近一周</option>
                            <option >近一个月</option>
                            <option >近一个季度</option>
                            <option >近半年</option>
                            <option >近一年</option>
                        </select>
                    </div>
                </div>

                <button type="button" id="searchBtn2" class="btn btn-default">查询</button>

            </form>

        </div>


    </div>
</div>
<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<div id="main" style="width: 600px;height:400px;"></div>

</body>
</html>


