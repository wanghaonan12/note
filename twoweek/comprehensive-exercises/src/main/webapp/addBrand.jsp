<%--
  Created by IntelliJ IDEA.
  User: wang rich
  Date: 2022/3/1
  Time: 13:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加品牌</title>
    <meta charset="UTF-8">
    <link href="css/style.css" rel="stylesheet">
    <style>
        table{
            border: 1px solid #ccc;

        }
        table tb{
            border: none;
            padding:10px;
        }
    </style>
</head>
<body>
<h1>添加品牌</h1>
    <div class="container">
        <form action="${pageContext.request.contextPath}/addBrand" method="post">
            <table>
                <tr>
                    <td>
                        <label for="brandName">
                            品牌名称
                        </label>
                    </td>
                    <td><input name="brandName" id="brandName" type="text"></td>
                </tr>
                <tr>
                    <td>
                        <label for="ordered">
                            排序
                        </label>
                    </td>
                    <td><input name="ordered" id="ordered" type="text"></td>
                </tr>
                <tr>
                    <td>
                        <label for="description">
                            描述信息
                        </label>
                    </td>
                    <td>
                        <textarea rows=" 10" cols="50" name="description" id="description">

                        </textarea>
                    </td>
                </tr>
                <tr>
                    <td>
                        状态
                    </td>
                    <td>
                        <input name="status" type="radio" id="ok" value="1" checked>
                        <label for="ok">
                            启用
                        </label>
                    </td>
                </tr>
            </table>
            <div style="padding-left: 20px">
                <input type="submit" value="提交">
            </div>
        </form>
    </div>


</body>
</html>
