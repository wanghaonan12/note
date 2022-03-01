package com.example.comprehensiveexercises.service;

import com.example.comprehensiveexercises.entity.Brand;
import com.example.comprehensiveexercises.mapper.BrandMapper;
import com.example.comprehensiveexercises.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;


public class BrandService {

    SqlSessionFactory factory= SqlSessionFactoryUtil.getSqlSessionFactory();

    /**
     * 查询所有
     * @return
     */
        public List<Brand> selectAll(){
//            获取Sqlsession
            SqlSession sqlSession=factory.openSession();
//            获取BrandMapper
            BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);
//            调用方法
            List<Brand> brands=mapper.selectAll();
            sqlSession.close();
            return brands;
        }

    /**
     * 添加
     * @param brand
     */
    public void  add(Brand brand){
//            获取sqlSession
            SqlSession sqlSession = factory.openSession();
//            获取BrandMapper
            BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);
//            调用方法
            mapper.add(brand);
//            提交事务
            sqlSession.commit();
//            释放资源
            sqlSession.close();
        }

    /**
     * 根据id查找
     * @param id
     * @return
     */
    public Brand selectById(int id){
//            获取sqlSession
            SqlSession sqlSession = factory.openSession();
//            获取BrandMapper
            BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);
            Brand brand = mapper.selectById(id);
            sqlSession.close();
            return brand;
        }
        public void update(Brand brand){
            SqlSession sqlSession = factory.openSession();
            BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);
            mapper.update(brand);
            sqlSession.commit();
            sqlSession.close();
        }
}
