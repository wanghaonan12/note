package com.example.response;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/CharSet")
public class CharSet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取流对象之前，设置流的默认编码：ISO-8859-1 设置为：utf-8
        // response.setCharacterEncoding("utf-8");
        //告诉浏览器，服务器发送的消息体数据的编码，建议浏览器使用该编码解码
        //response.setHeader("content-type","text/html;charset=utf-8");
        //简单形式，设置编码
        resp.setContentType("text/html;charset=utf-8");
        //1.获取字符输出流
        PrintWriter pw = resp.getWriter();
        //2.输出数据
        //pw.write("<h1>hello response</h1>");
        pw.write("你好啊 response");
    }
}
