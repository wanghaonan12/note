package com.example.comprehensiveexercises.mapper;

import jakarta.servlet.annotation.WebInitParam;

public interface UserMapper {
    @Select("SELECT *FROM tb_user WHERE username=#{username} AND password =#{password}")
    findUser(@Param("uaername") String username,@Param("password") String password);
}
