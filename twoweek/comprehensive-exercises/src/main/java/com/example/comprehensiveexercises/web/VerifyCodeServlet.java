package com.example.comprehensiveexercises.web;

import com.example.comprehensiveexercises.util.VerifyCodeUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/verifyCode")
public class VerifyCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream os = response.getOutputStream();
        String verifyCode = VerifyCodeUtil.outputVerifyImage(100, 50, os, 4);
        HttpSession session = request.getSession();
        session.setAttribute("verifyCode",verifyCode);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doGet(request, response);
    }
}
