<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: wangRich
  Date: 2022/3/5
  Time: 12:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>游戏大厅</title>
</head>
<body>
<h2>在线用户</h2>
<%
    Map<String, Object> sessionMap = (Map<String, Object>) application.getAttribute("sessionMap");
    response.setIntHeader("Refresh",2);
%>
<ul>
    <%
        for (Map.Entry<String,Object> entry:sessionMap.entrySet()){
    %>
    <li>
        <%=entry.getValue()%>
    </li>
    <%
        }
    %>
</ul>
</body>
</html>
