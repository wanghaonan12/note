package com.example.comprehensiveexercises.mapper;

import com.example.comprehensiveexercises.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("SELECT * FROM tb_user WHERE username=#{username} AND password =#{password}")
    User findUser(@Param("username") String username, @Param("password") String password);

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    @Select("SELECT * FROM tb_user WHERE username =#{username}")
    User selectByUserName(String username);

    /**
     * 添加用户
     * @param user
     */
    @Insert("INSERT INTO tb_user VALUES(null,#{username},#{password})")
    void add(User user);
}
