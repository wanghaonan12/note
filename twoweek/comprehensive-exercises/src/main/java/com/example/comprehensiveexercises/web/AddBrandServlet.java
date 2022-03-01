package com.example.comprehensiveexercises.web;

import com.example.comprehensiveexercises.entity.Brand;
import com.example.comprehensiveexercises.service.BrandService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
@WebServlet("/addBrand")
public class AddBrandServlet extends HttpServlet {
    private final BrandService brandService=new BrandService();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//接受表单提交数据,封装brand对象
        String brandName = request.getParameter("brandName");
        String companyName = request.getParameter("companyName");
        String ordered = request.getParameter("ordered");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
//        封装Brand对象
        Brand brand = new Brand();
        brand.setBrandName(brandName);
        brand.setDescription(description);
        brand.setCompanyName(companyName);
        brand.setStatus(Integer.parseInt(status));
        brand.setOrdered(Integer.parseInt(ordered));
//        调用service完成添加
        brandService.add(brand);

//      重定向查询所有
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath+"/index");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
