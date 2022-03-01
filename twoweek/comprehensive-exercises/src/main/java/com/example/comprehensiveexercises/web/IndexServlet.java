package com.example.comprehensiveexercises.web;

import com.example.comprehensiveexercises.entity.Brand;
import com.example.comprehensiveexercises.service.BrandService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    private final BrandService brandService=new BrandService();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //调用BrandService完成查询
        List<Brand> brands=brandService.selectAll();
//        存入request域中
        request.setAttribute("brands",brands);
//        转发到brand.jsp
        request.getRequestDispatcher("index.jsp").forward(request,response);
    }
}
