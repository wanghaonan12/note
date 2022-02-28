package com.example.request;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//配置网络请求设置
@WebServlet( "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
//        在初始话init中每次启动都指出发一次
        message = "Servlet";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        doGet,dopost等会在浏览器中做出相应得操作后做出相应
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
//        destory会在关闭的时候做出动作
    }
}