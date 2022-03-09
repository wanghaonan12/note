package com.example.exercises.service.impl;

import com.example.exercises.mapper.BrandMapper;
import com.example.exercises.pojo.Brand;
import com.example.exercises.pojo.PageBean;
import com.example.exercises.service.BrandService;
import com.example.exercises.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class BrandServiceImpl implements BrandService {
    SqlSessionFactory factory =SqlSessionFactoryUtil.getSqlSessionFactory();

    @Override
    public List<Brand> selectAll() {
        SqlSession sqlSession = factory.openSession();
        BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);
        List<Brand> brands = mapper.selectAll();
        sqlSession.close();
        return  brands;
    }

    @Override
    public void add(Brand brand) {
        SqlSession sqlSession = factory.openSession();
        BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);
        mapper.add(brand);
        sqlSession.commit();
        sqlSession.close();

    }

    @Override
    public void deleteByIds(int[] ids) {
        SqlSession sqlSession = factory.openSession();
        BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);
        mapper.deleteByIds(ids);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public PageBean<Brand> selectByPage(int currentPage, int pageSize) {
        SqlSession sqlSession = factory.openSession();
        BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);
        int begin=(currentPage-1)*pageSize;
        List<Brand> rows = mapper.selectByPage(begin, pageSize);
        int totalCount = mapper.selectTotalCount();
        PageBean<Brand> pageBean = new PageBean<>();
        pageBean.setRows(rows);
        pageBean.setTotalCount(totalCount);
        sqlSession.close();
        return  pageBean;
    }

    @Override
    public PageBean<Brand> selectByPageAndCondition(int currentPage, int pageSize, Brand brand) {
        SqlSession sqlSession = factory.openSession();
        BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);
        int begin=(currentPage-1)*pageSize;
        String brandName = brand.getBrandName();
        String companyName = brand.getCompanyName();
        if (brandName != null && brandName.length() > 0) {
            brand.setBrandName("%"+brandName+"%");
        }
        if (companyName !=null&& companyName.length()>0) {
            brand.setCompanyName("%"+companyName+"%");
        }
        List<Brand> rows = mapper.selectByPageAndCondition(begin, pageSize, brand);
        int totalCount = mapper.selectTotalCountByCondition(brand);
        PageBean<Brand> pageBean = new PageBean<>();
        pageBean.setRows(rows);
        pageBean.setTotalCount(totalCount);
        sqlSession.close();
        return pageBean;
    }
}
