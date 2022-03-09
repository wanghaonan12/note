package com.whn.config;

import com.whn.pojo.People;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangRich
 * 实现功能
 */
@Configuration
public class MyAutoConfiguration {
    static {
        System.out.println("MyAutoConfiguration init .....");
    }
    @Bean
    public People people(){
        return new People();
    }
}
