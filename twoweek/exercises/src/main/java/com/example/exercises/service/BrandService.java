package com.example.exercises.service;

import com.example.exercises.pojo.Brand;
import com.example.exercises.pojo.PageBean;

import java.util.List;

public interface BrandService {
    List<Brand> selectAll();
    void add(Brand brand);
    void deleteByIds(int[] ids);
    PageBean<Brand>selectByPage(int currentPage ,int pageSize);
    PageBean<Brand> selectByPageAndCondition(int currentPage,int pageSize,Brand brand);

}
