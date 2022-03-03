package com.example.requestresponseexercise;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@MultipartConfig
@WebServlet("/upload1")
public class UploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        设置编码格式
        request.setCharacterEncoding("utf-8");
//        获取account的值
        String account = request.getParameter("account");
//        输出
        System.out.println(account);
//        创建连接
        Part part = request.getPart("file");
//        源文件名
        System.out.println(part.getSubmittedFileName());
//        创建空字符串
        String fileName="";
        if (part.getContentType() != null) {
//            改名
            fileName = UUID.randomUUID()+part.getSubmittedFileName().substring(part.getSubmittedFileName().lastIndexOf("."));
//            获取上下文
            ServletContext context = this.getServletContext();
//            设置上传的路径和文件名
            String realPath = context.getRealPath("upload/" + fileName);
//            字节流输出
            part.write(realPath);
        }
//        上传到屏幕
        PrintWriter writer = response.getWriter();
        writer.write("./upload/"+fileName);
        writer.flush();
        writer.close();


    }
}
