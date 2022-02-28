package com.example.comprehensiveexercises.util;

import jdk.internal.loader.Resource;

import java.io.InputStream;

public class SqlSessionFactoryUtil
{
    private static SqlSessionFactory sqlSessionFactory;
    static{
//        静态代码块会随着类的加载自动执行，且只执行一次，在构造方法之前执行
        String resource="mybatis-config.xml";
        InputStream is=Resource.getResourceAsStream(resource);
        new SqlSessionFactoryBuilder().buil(is);
    }
}
