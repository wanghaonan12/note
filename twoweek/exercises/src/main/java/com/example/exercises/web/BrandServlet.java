package com.example.exercises.web;

import com.alibaba.fastjson.JSON;
import com.example.exercises.pojo.Brand;
import com.example.exercises.pojo.PageBean;
import com.example.exercises.service.BrandService;
import com.example.exercises.service.impl.BrandServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/brand/*")
public class BrandServlet extends BaseServlet {
    private final BrandService brandService=new BrandServiceImpl();
    public void selectAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        List<Brand> brands = brandService.selectAll();
        String jsonString = JSON.toJSONString(brands);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(jsonString);
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        StringBuilder json = new StringBuilder();
        String  line;
        BufferedReader br = request.getReader();
        while((line = br.readLine())!=null){
            json.append(line);
        }
        Brand brand = JSON.parseObject(json.toString(),Brand.class);
        brandService.add(brand);
        response.getWriter().write("success");
    }

    public void deleteByIds(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        BufferedReader br = request.getReader();
        String params = br.readLine();
        int[] ids = JSON.parseObject(params,int[].class);
        brandService.deleteByIds(ids);
        response.getWriter().write("success");
    }

    public void selectByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        int currentPage = Integer.parseInt(currentPageStr);
        int pageSize = Integer.parseInt(pageSizeStr);
        PageBean<Brand> pageBean = brandService.selectByPage(currentPage,pageSize);
        String jsonString = JSON.toJSONString(pageBean);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
    }

    public void selectByPageAndCondition(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        int currentPage = Integer.parseInt(currentPageStr);
        int pageSize = Integer.parseInt(pageSizeStr);
        StringBuilder json = new StringBuilder();
        String line;
        BufferedReader br = request.getReader();
        while ((line = br.readLine()) != null){
            json.append(line);
        }
        Brand brand = JSON.parseObject(json.toString(),Brand.class);
        PageBean<Brand> pageBean = brandService.selectByPageAndCondition(currentPage,pageSize,brand);
        String jsonString = JSON.toJSONString(pageBean);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
    }
}
