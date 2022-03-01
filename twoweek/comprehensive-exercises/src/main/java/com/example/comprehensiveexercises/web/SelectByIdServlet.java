package com.example.comprehensiveexercises.web;

import com.example.comprehensiveexercises.entity.Brand;
import com.example.comprehensiveexercises.service.BrandService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
@WebServlet("/selectById")
public class SelectByIdServlet extends HttpServlet {
    private final BrandService brandService=new BrandService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
this.doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        log.info(id);
        Brand brand = brandService.selectById(Integer.parseInt(id));
        request.setAttribute("brand",brand);
        request.getRequestDispatcher("update.jsp").forward(request,response);
    }
}
