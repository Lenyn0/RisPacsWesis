<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        %>
        <!DOCTYPE html>
<html lang="en">
<head>
   <base href="<%=basePath%>">
   <meta charset="utf-8" />
   <title></title>

   <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
   <script src="https://www.jq22.com/jquery/jquery-migrate-1.2.1.min.js"></script>
   <script type="text/javascript" src="jquery/jquery.jqprint-0.3.js"></script>

   <script type="text/javascript">

      function getUrlParam(name){
         var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
         var r = window.location.search.substr(1).match(reg);
         // ECMAScript v3 已从标准中删除了 unescape() 函数，并反对使用它，因此应该用 decodeURI() 和 decodeURIComponent() 取而代之。
         // if (r != null) return unescape(r[2]); return null;
         if (r != null) return decodeURI(r[2]); return null;//解码
      }

      $(function(){
         $("#print").click(function(){

               $("#ddd").jqprint();

             var id=getUrlParam("id");
             $.ajax({
                       url : "workbench/Report/printReport.do",
                       data : {
                           "id" : id
                       },
                       type : "post",
                       dataType : "text",
                       async: true,
                       success : function (data) {

                          if(data!='true')
                           {
                               // alert("打印失败");
                           }
                       }

                   })
         })

      });

   </script>

</head>

<button id="print" type="button"  class="btn btn-primary" >打印</button>
<button type="button" class="btn btn-default" id="cancel" onclick="window.location.href = 'workbench/report_review/index.jsp';">取消</button>

<div id="ddd" align="center">
<div>
   <div style="letter-spacing: 15px; position: relative; text-align: center; font-size: 25px; letter-spacing: 3px;">
      <h3>XXX医院<br>检查报告单</h3>
   </div>
</div>

<div style="width:900px; ">
   <hr align="center" />
</div>

   <table border="0" cellspacing="12" style=" width: 900px;height: 250px ">
      <tr style="text-align: left;">
         <td>姓名：</td>
         <td>${patient.name}</td>
         <td>性别：</td>
         <td>${patient.gender}（M男/F女）</td>
      </tr>
      <tr style="text-align: left;">
         <td>年龄：</td>
         <td>${patient.age}岁</td>
         <td>电话：</td>
         <td>${patient.phoneNumber}</td>
      </tr>
      <tr style="text-align: left;">
         <td>患者ID：</td>
         <td>${patient.id}</td>
         <td>病区/床号</td>
         <td>${patient.inpatientBedNumber}</td>
      </tr>
      <tr style="text-align: left;">
         <td>检查号：</td>
         <td>${studyinfo.accessionNumber}</td>
         <td>检查部位：</td>
         <td>${report.bodyPart}</td>
      </tr>
      <tr style="text-align: left;">
         <td>申请科室：</td>
         <td>${patient.inpatientDepartment}</td>
         <td>检查日期：</td>
         <td>${studyinfo.scheduledProcedureStepStartDate}</td>
      </tr>
   </table>


   <div style="width:900px; ">
      <hr align="center" />
   </div>

<table border="0" cellspacing="12" style=" width: 900px;">
   <tr style="text-align: left;">
      <td>影像学所见：</td>
   </tr>
   <tr height="200px">
      <td>${report.imagingFindings}</td>
   </tr>
</table>

   <div style="width:900px; ">
      <hr align="center" />
   </div>

<table border="0" cellspacing="12" style=" width: 900px;">
   <tr style="text-align: left;">
      <td width="100px">诊断意见：</td>
   </tr>
   <tr height="150px">
      <td>${report.diagnosticOpinion}</td>
   </tr>
</table>
<table border="0" cellspacing="12" style=" width: 900px;">
   <tr style="text-align: left;">
      <td>诊断医生：${createUserName}</td>
      <td>审核医生：${AuditorName}</td>
      <td>报告日期：${report.createTime}</td>
   </tr>
</table>
   <div style="width:900px; ">
      <hr align="center" />
   </div>
<div style="width: 900px;">
   本报告仅供临床医师参考，不做疾病证明，本报告签字后生效。
</div>
</div>

</body>
</html>