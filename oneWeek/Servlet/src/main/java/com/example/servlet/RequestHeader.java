package com.example.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

@WebServlet("/RequestHeader")
public class RequestHeader extends HttpServlet {
    @Override
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // 获取请求头数据
    //1.获取所有请求头名称
    Enumeration<String> headerNames = request.getHeaderNames();
    //2.遍历
    //hasMoreElements:拥有更多的元素
        while (headerNames.hasMoreElements()) {
        //name:头名称的下一个元素
        String name = headerNames.nextElement();
        //根据名称获取请求头的值
        String value = request.getHeader(name);
        //输出元素和对应的值
        System.out.println(name + "---" + value);
    }
            //获取请求头数据:user-agent
            String agent = request.getHeader("user-agent");
            //判断--- 请求头数据 ---的浏览器版本
            //contains：包含
            if (agent.contains("Chrome")) {
                //谷歌
                System.out.println("谷歌浏览器...");
            } else if (agent.contains("Firefox")) {
                //火狐
                System.out.println("火狐浏览器...");
            }else if (agent.contains("Edg")) {
                //火狐
                System.out.println("Edge览器...");
            }else{
                //火狐
                System.out.println("...");
            }
            //request设置键值对
            request.setAttribute("hobbies","看电视");
            //request获取设置或是系统自带的键值
            System.out.println(request.getAttribute("hobbies"));

        //post 获取请求参数
        //根据参数名称获取参数值
        System.out.println("根据参数名称获取参数值");
        String username = request.getParameter("username");
        System.out.println(username);
        //根据参数名称获取参数值的数组
        String[] hobbies = request.getParameterValues("hobby");
        for (String hobby : hobbies) {
            System.out.println(hobby);
        }

        //获取所有请求的参数名称
        System.out.println("***********************");
        System.out.println("获取所有请求的参数名称");
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            System.out.println(name);
            String value = request.getParameter(name);
            System.out.println(value);
            System.out.println("----------------");
        }

        // 获取所有参数的map集合
        System.out.println("***********************");
        System.out.println("获取所有参数的map集合");
        Map<String, String[]> parameterMap = request.getParameterMap();
        //遍历
        Set<String> keySet = parameterMap.keySet();
        for (String name : keySet) {
            //获取键获取值
            String[] values = parameterMap.get(name);
            System.out.println(name);
            for (String value : values) {
                System.out.println(value);
            }
            System.out.println("-----------------");
        }

        //1.设置流的编码
        request.setCharacterEncoding("utf-8");

    }
    @Override
    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
