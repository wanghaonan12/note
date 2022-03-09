package com.example.comprehensiveexercises.service;

import com.example.comprehensiveexercises.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService = new UserService();

    @Test
    void login() {
        User zhangsan = userService.login("zhangsan", "123");
        System.out.println(zhangsan);
    }

    @Test
    void register() {
    }
}