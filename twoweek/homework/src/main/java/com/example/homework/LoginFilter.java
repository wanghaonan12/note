package com.example.homework;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//@WebFilter(filterName = "LoginFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
        System.out.println("LoginFilter初始化.....");
    }

    public void destroy() {
        System.out.println("LoginFilter销毁...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
//        强转HttpServletRequest
        HttpServletRequest request1=(HttpServletRequest) request;
        String[] urls={"/login.jsp","img","words.html","user.jsp","sign.html","index.html","/css/","/js","loginServlet","register.jsp","registerServlet","verifyCodeServlet"};
        String url = request1.getRequestURL().toString();
        for (String u:urls) {
            if (url.contains(u)) {
                chain.doFilter(request1, response);
                return;
            }
        }
        HttpSession session = request1.getSession();
        Object user = session.getAttribute("user");
        if (user != null) {
            chain.doFilter(request, response);
        } else {
            String msg = "当前用户没有登陆";
            request1.getRequestDispatcher("/login.jsp").forward(request1,response);
        }
    }
}
