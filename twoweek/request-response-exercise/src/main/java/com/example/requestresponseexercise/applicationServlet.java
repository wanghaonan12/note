package com.example.requestresponseexercise;
import com.alibaba.fastjson.JSON;
import entity.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/login")
public class applicationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
//        System.out.println(account);
//        System.out.println(password);
        User user = new User(account, password);
        response.setContentType("application/json;charset:utf-8");
        request.setCharacterEncoding("utf-8");
        response.getWriter().println(JSON.toJSONString(user));
    }
}
