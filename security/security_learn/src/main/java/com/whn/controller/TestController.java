package com.whn.controller;

import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: WangHn
 * @Date: 2024/3/25 16:36
 * @Description: 测试controller
 */
@ApiModel(value = "test Controller",description = "测试controller")
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/")
    public String getTest(){
        return "controller test";
    }

    @GetMapping("/math/{num}/{num2}")
    public String Calculate(@PathVariable(value = "num") Integer num,@PathVariable(value = "num2") Integer num2){
        return (num/num2)+"";
    }
}
