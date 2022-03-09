<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>选择</title>
    <style>
        body{
            background-image: url("/images/2.jpg");
            background-size: cover;
        }
        a{
            color: white;
            margin: 20px;
            display: inline-block;
            width: 250px;
            height: 40px;
            line-height: 40px;
            text-align: center;
            background-color: rgba(126, 125, 123, 0.53);
            text-decoration: none;
        }
    </style>
</head>
<body>
<h1 style="color:white"><%= "选择界面" %></h1>
<br/>
<a href="http://localhost/demo1.html" >读取前端数据</a>
<a href="http://localhost/demo2.html" >将图片转至项目文件夹</a>
<a href="http://localhost/demo3.html" >读取url参数</a>
<a href="http://localhost/demo4.html" >前后分离读取get</a>
<a href="http://localhost/demo5.html" >前后分离读取键值对</a>
<a href="http://localhost/demo6.html" >后端Servlet读取JSON数据</a>
<a href="http://localhost/sendDocunmet.html" >上传文件并在屏幕输出</a>

</body>
</html>