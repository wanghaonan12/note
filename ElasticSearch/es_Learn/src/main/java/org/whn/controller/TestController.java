package org.whn.controller;

import io.swagger.annotations.ApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whn.service.Impl.TestServiceImpl;

import java.io.IOException;

/**
 * @Author: WangHn
 * @Date: 2024/4/3 17:23
 * @Description: 测试controller
 */
@ApiModel(value = "test Controller",description = "测试controller")
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestServiceImpl testServiceImpl;
    @GetMapping("/")
    public String getTest(){
        try {
            testServiceImpl.testSearch();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "controller test";
    }
}
