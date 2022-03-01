package com.example.comprehensiveexercises.service;

import com.example.comprehensiveexercises.entity.User;
import com.example.comprehensiveexercises.mapper.UserMapper;
import com.example.comprehensiveexercises.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;


public class UserService {

    SqlSessionFactory factory= SqlSessionFactoryUtil.getSqlSessionFactory();
    public User login(String username,String password)
    {
//        获取sqlSession
        SqlSession sqlSession = factory.openSession();
//        userMapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        调用方法
        User user = mapper.findUser(username, password);
//        释放资源
        sqlSession.close();
        return user;
    }
    public boolean register(User user){
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User u = mapper.selectByUserName(user.getUsername());
        if(u==null){
            mapper.add(user);
            sqlSession.commit();
        }
        sqlSession.close();
        return  u==null;
    }
}
