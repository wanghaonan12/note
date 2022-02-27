package com.example.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/TestShare1")
public class TestShare1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("TestShare1...........");
        request.setAttribute("name","WangRich");
        //转发到/TestShare2资源,跨域无法访问,跨域到其他网址
        request.getRequestDispatcher("/TestShare2").forward(request,response);
    }
}
