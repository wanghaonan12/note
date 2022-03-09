package com.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
@WebFilter( urlPatterns = "/hello.jsp")
public class LoginFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
        System.out.println("LoginFilter初始化");

    }

    public void destroy() {
        System.out.println("LoginFilter关闭");
    }

    @Override
    public void doFilter(ServletRequest Servletrequest, ServletResponse Servletresponse, FilterChain chain) throws ServletException, IOException {
//        登陆状态逻辑代码
//        将ServletRequest强转为
        HttpServletRequest request=(HttpServletRequest) Servletrequest;
//        判断访问路径是否和登录有关
//        在数组中存储登录和注册资源路径
        String[] urls={"/login.jsp","img","/css/","/js","loginServlet","register.jsp","registerServlet","verifyCodeServlet"};
//        获取当前路径
        String url = request.getRequestURL().toString();
//        循环遍历获取需要方形的资源
        for (String u:urls) {
//            判断当前访问资源路径是否含有需要放心资源
//            比如当前访问的资源路径是/page/login. jsp, 而字符串/page/login. jsp 包含了字符串/login. jsp，所以这个字符串就需要放行
            if (url.contains(u)) {
                chain.doFilter(request,Servletresponse);
                return;
            }
        }

//        取出session并去除user
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
//        判断user是否为空
        if (user != null) {
//            登陆过了放行
            chain.doFilter(Servletrequest,Servletresponse);
        } else {
//            没有登陆
           String msg="没有登陆";
            request.setAttribute("msg",msg);
            request.getRequestDispatcher("/data/login.jsp").forward(request, Servletresponse);
        }

    }
}
