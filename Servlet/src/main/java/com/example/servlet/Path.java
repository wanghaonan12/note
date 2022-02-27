package com.example.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/Path")
public class Path extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       /*
        1. 获取请求方式 ：GET
           String getMethod()
        2. 获取虚拟目录：/day02
            String getContextPath()
        3. 获取Servlet路径: /requestDemo1
           String getServletPath()
        4. 获取get方式请求参数：name=zhangsan
           String getQueryString()
        5. 获取请求URI：/day02/demo1
           String getRequestURI():		/day02/requestDemo1
           StringBuffer getRequestURL()  :http://localhost:8080/day02/requestDemo1
        6. 获取协议及版本：HTTP/1.1
           String getProtocol()
        7. 获取客户机的IP地址：
           String getRemoteAddr()
         */
        //1. 获取请求方式 ：GET
        String method = request.getMethod();
        System.out.println(method);
        //2. 获取虚拟目录：/day02
        String contextPath = request.getContextPath();
        System.out.println(contextPath);
        //3. 获取Servlet路径: /requestDemo1
        String servletPath = request.getServletPath();
        System.out.println(servletPath);
        //4. 获取get方式请求参数：name=zhangsan
        String queryString = request.getQueryString();
        System.out.println(queryString);
        //5. 获取请求URI：/day02/requestDemo1
        String requestUri = request.getRequestURI();
        StringBuffer requestUrl = request.getRequestURL();
        System.out.println(requestUri);
        System.out.println(requestUrl);
        //6. 获取协议及版本：HTTP/1.1
        String protocol = request.getProtocol();
        System.out.println(protocol);
        //7. 获取客户机的IP地址：
        String remoteAddress = request.getRemoteAddr();
        System.out.println(remoteAddress);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
