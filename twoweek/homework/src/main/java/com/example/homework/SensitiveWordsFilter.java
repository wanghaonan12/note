package com.example.homework;

import jakarta.servlet.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

//@WebFilter(filterName = "SensitiveWordsFilter",urlPatterns = "/*")
public class SensitiveWordsFilter implements Filter {
    private final String methodName="getParameter";
    private final List<String> list=new ArrayList<>();
    public void init(FilterConfig config) throws ServletException {
        try {
            System.out.println("SensitiveWordsFilter初始化...");
            ServletContext servletContext = config.getServletContext();
            System.out.println(servletContext);
            String realPath = servletContext.getRealPath("/敏感词汇.txt");
            BufferedReader br = new BufferedReader(new FileReader(realPath));
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        System.out.println("SensitiveWordsFilter销毁...");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
ServletRequest proxyReq=(ServletRequest)
        Proxy.newProxyInstance(request.getClass().getClassLoader(),request.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ((methodName.equals(method.getName()))) {
                    String value = (String) method.invoke(request, args);
                    if (value != null) {
                        for (String str:list) {
                            if (value.contains(str)) {
                                value=value.replaceAll(str,"***");
                            }
                        }
                    }
                    return value;
                }
                return method.invoke(request,args);
            }
        }) ;
        chain.doFilter(request, response);
    }
}
