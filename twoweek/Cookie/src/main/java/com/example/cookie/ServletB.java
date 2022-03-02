package com.example.cookie;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/ServletB")
@Slf4j
public class ServletB extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies
             ) {
            String name = cookie.getName();
            if ("username".equals(name)){
                String value = cookie.getValue();
                log.info("name="+value);
                break;
            }
        }
        String value="你好";
//        编码设置
        value= URLEncoder.encode(value, StandardCharsets.UTF_8);
//        输出到屏幕
        response.getWriter().write(value);
//        设置服务器编码格式
        response.setContentType("text/plain;charset=UTF-8");
//        解码
        value= URLDecoder.decode(value,StandardCharsets.UTF_8);
        response.getWriter().write(value);

    }
}
