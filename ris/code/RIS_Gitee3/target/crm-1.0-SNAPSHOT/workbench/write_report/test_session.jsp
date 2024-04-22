<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
<form method=POST action="test_session.jsp">
    请输入用户名： <input type=text name="name"> <input type=submit
                                                 value="提交信息">
</form>

<div class="form-group">
    <%--疾病部位从数据词典中获取，通过疾病部位再获取相对应的疾病名称列表可以选择--%>
    <label for="create-bodyPart" class="col-sm-2 control-label">疾病部位<span style="font-size: 15px; color: red;">*</span></label>
    <div class="col-sm-10" style="width: 300px;">
        <select class="form-control" id="create-bodyPart" name="body">
            <option></option>
            <c:forEach items="${bodyPartsList}" var="t">
                <option value="${t.value}">${t.text}</option>
            </c:forEach>
        </select>
    </div>
    <label for="create-diseaseName" class="col-sm-2 control-label">疾病名称<span style="font-size: 15px; color: red;">*</span></label>
    <div class="col-sm-10" style="width: 300px;">
        <select  class="form-control" id="create-diseaseName" name="bodyParts" >

        </select>
    </div>
</div>
</body>
</html>