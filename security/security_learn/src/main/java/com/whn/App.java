package com.whn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

/**
 * @Author: WangHn
 * @Date: 2024/4/23 10:23
 * @Description: ${Description}
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(App.class, args);
        String[] beanDefinitionNames = run.getBeanDefinitionNames();
        Arrays.stream(beanDefinitionNames).filter(s->s.contains("org.springframework.security")).forEach(System.out::println);
    }
}