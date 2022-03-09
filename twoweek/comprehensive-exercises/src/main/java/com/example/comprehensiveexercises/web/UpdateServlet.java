package com.example.comprehensiveexercises.web;

import com.example.comprehensiveexercises.entity.Brand;
import com.example.comprehensiveexercises.service.BrandService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/updateBrand")
public class UpdateServlet extends HttpServlet {
    private final BrandService brandService=new BrandService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        System.out.println(id);
        String brandName = request.getParameter("brandName");
        String companyName = request.getParameter("companyName");
        String ordered = request.getParameter("ordered");
        String description = request.getParameter("description");
        String status = request.getParameter("status");

//        封装Brand对象
        Brand brand = new Brand();
        brand.setOrdered(Integer.parseInt(ordered));
        brand.setId(Integer.parseInt(id));
        brand.setStatus(Integer.parseInt(status));
        brand.setBrandName(brandName);
        brand.setDescription(description);
        brand.setBrandName(brandName);
        System.out.println(brand);

        brandService.update(brand);
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath+"/index");


    }
}
