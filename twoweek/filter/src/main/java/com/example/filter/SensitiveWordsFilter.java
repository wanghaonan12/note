package com.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;


@WebFilter("/*")
public class SensitiveWordsFilter implements Filter {
//    敏感词汇集合
private final List<String> list=new ArrayList<>();
private final String methodName="getParameter";

    @Override
    public void init(FilterConfig config) {
        //        获取文件真实路径


//        读取文件

        try {
            ServletContext servletContext = config.getServletContext();
            String realPath = servletContext.getRealPath("/WEB-INF/classes/敏感词汇.txt");
            BufferedReader br = new BufferedReader(new FileReader(realPath));
//        将文件的每一行添加到list中
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println(list);
//创建代理对象,增强getparameter方法
        ServletRequest proxyReq=(ServletRequest) Proxy.newProxyInstance(request.getClass().getClassLoader(), request.getClass().getInterfaces(),
                new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//              增强getparameter方法
                if (methodName.equals(method.getName())) {
//                    增强返回值
                    String value=(String) method.invoke(request,args);
                    if (value != null) {
                        for (String str:list) {
                            if (value.contains(str)) {
//                                替换为****
                             value=value.replaceAll(str,"***");

                            }
                        }
                    }
                    return value;
                }
                return method.invoke(request,args);
            }
        });
//        放行
        chain.doFilter(proxyReq,response);
    }
}
