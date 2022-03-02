package com.example.comprehensiveexercises.web;

import com.example.comprehensiveexercises.entity.User;
import com.example.comprehensiveexercises.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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
//        是否勾选"记住我"
        String remember = request.getParameter("remember");
        System.out.println(remember+"---------------------");
//        调整service登录
        User user = userService.login(username, password);
//        判断
        if (user!=null){
//            非空
//            是否记住我
            if ("on".equals(remember)){
                System.out.println("需要记录Cookie");
//                创建对象
                Cookie cUserName = new Cookie("username", username);
                Cookie cPassWord = new Cookie("password", password);
                //设置存活时间
                cPassWord.setMaxAge(60*60*24*7);
                cUserName.setMaxAge(60*60*24*7);
//                发送
                response.addCookie(cPassWord);
                response.addCookie(cUserName);
            }
//            将用户信息记录在session中
            HttpSession session = request.getSession();
            session.setAttribute("user",user);
            String contextPath = request.getContextPath();
            System.out.println(contextPath);
            response.sendRedirect(contextPath+"/index");
        }else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<script>alert('用户名或密码错误');location='login.html';</script>");
        }
    }
}
