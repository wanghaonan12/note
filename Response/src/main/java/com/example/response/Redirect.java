package com.example.response;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet( "/Redirect")
public class Redirect extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ResponseDemo1........");
        //访问/responseDemo1，会自动跳转到/responseDemo2资源
        //1. 设置状态码为302
        response.setStatus(302);
        //2.设置响应头location
        response.setHeader("location", "/day03/responseDemo2");
        request.setAttribute("msg", "response");
        //动态获取虚拟目录
        String contextPath = request.getContextPath();
        //简单的重定向方法
        response.sendRedirect(contextPath + "/responseDemo2");
        // 重定向可以跨域访问
        //跨域访问地址改变,可以访问其他网址的服务器资源
        //response.sendRedirect("https://www.baidu.cn");

        //转发
//        request.getRequestDispatcher("/hello-servlet").forward(request, response);
        //转发不会改变地址但是可以访问request的资源
    }
}
