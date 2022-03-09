package com.example.exercises.service.impl;

import com.example.exercises.pojo.Brand;
import com.example.exercises.pojo.PageBean;
import org.junit.jupiter.api.Test;

import java.util.List;

class BrandServiceImplTest {
private final BrandServiceImpl brandService=new BrandServiceImpl();
    @Test
    void selectAll() {
        List<Brand> brands = brandService.selectAll();
        brands.forEach(System.out::println);
    }

    @Test
    void add() {
        Brand brand = new Brand();
        brand.setBrandName("brandName");
        brand.setDescription("description");
        brand.setOrdered(111);
        brand.setId(1);
        brand.setCompanyName("companyName");
        brandService.add(brand);
    }

    @Test
    void deleteByIds() {
        int [] ids={65,66,69};
        brandService.deleteByIds(ids);
    }


    @Test
    void selectByPage() {
        PageBean<Brand> brandPageBean = brandService.selectByPage(2, 6);
        List<Brand> brands = brandPageBean.getRows();
        brands.forEach(System.out::println);
    }

    @Test
    void selectByPageAndCondition() {
        Brand brand = new Brand();
        brand.setBrandName("华为");
        PageBean<Brand> brandPageBean = brandService.selectByPageAndCondition(1, 10, brand);
        List<Brand> brands = brandPageBean.getRows();
        brands.forEach(System.out::println);
    }
}