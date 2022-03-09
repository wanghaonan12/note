package com.example.comprehensiveexercises.web;

import com.example.comprehensiveexercises.entity.User;
import com.example.comprehensiveexercises.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService=new UserService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//获取用户名和密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

//        获取用户输入验证码
        String inputCode = request.getParameter("inputCode");
//        程序生成的验证码
        HttpSession session = request.getSession();
        String verifyCode =(String) session.getAttribute("verifyCode");
        response.setContentType("text/html;charset=UTF-8");
        String contextPath = request.getContextPath();
//        忽略大小写
        if (!verifyCode.equalsIgnoreCase(inputCode)){
            response.getWriter().write("<script>alert('验证码错误');location='/register.html';</script>");
        }
//        调用service注册
        boolean flag=userService.register(user);
//        判断注册是否成功
        if(flag){
            response.getWriter().write("<script>alert('注册成功');location='/login.html';</script>");
        }else {
            response.getWriter().write("<script>alert('用户已存在');location='/register.html';</script>");
        }

    }
}
