package com.example.requestresponseexercise;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

//添加注解使用part获取字节
@MultipartConfig
@WebServlet("/param")
public class getParameter extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//获取传参的部分字符串
        String queryString = request.getQueryString();
//        按照&字符分割字符串
//        <a href="/param?name=张三&age=20">点击跳转传参</a>
        String[] split = queryString.split("&");
        HashMap<String, String> map = new HashMap<>(4);
        for (String s:split) {
//            按照=字符分割字符串
            String[] split1 = s.split("=");
//            按照%字符分割字符串
            if (split1[1].contains("%")) {
//                设置解码格式
                split1[1]= URLDecoder.decode(split1[1], StandardCharsets.UTF_8);
            }
//          参数对存⼊map
            map.put(split1[0], split1[1]);
        }
//          将结果以JSON格式回显给客户端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(map));
        response.getWriter().close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }
}
