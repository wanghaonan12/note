package com.example.comprehensiveexercises.service;

import com.example.comprehensiveexercises.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/check")
public class CheckUserServlet extends HttpServlet {
    UserService userService=new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
//        Object attribute = request.getAttribute("username");//获取get的网页传入值
        System.out.println("11111111111111111111111111111111111111111111111111111111111111111111111111");
        System.out.println(username);
//        System.out.println(attribute);//获取标签属性值
        User user =new User();
        user.setUsername(username);
        boolean flag = userService.register(user);
        System.out.println(flag);
        response.getWriter().write(String.valueOf(flag));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
