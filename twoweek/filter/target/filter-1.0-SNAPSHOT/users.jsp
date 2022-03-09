<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: wangRich
  Date: 2022/3/4
  Time: 18:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>进入游戏大厅</title>
</head>
<body>
<h2>游戏大厅用户</h2>
<h2>
    <%
        application.getAttribute("sessionMap");

//    response.setIntHeader("Refresh",5);
    %>

</h2>

<h2>admin</h2>

<%--<ul>--%>
<%--    <%--%>
<%--        for (Map.Entry<String,Object> entry:sessionMap.entrySet()) {--%>
<%--    %>--%>
<%--    <li>--%>
<%--        <%=entry.getValue()%>--%>
<%--    </li>--%>
<%--    <%--%>
<%--        }--%>
<%--    %>--%>
<%--</ul>--%>
</body>
</html>
