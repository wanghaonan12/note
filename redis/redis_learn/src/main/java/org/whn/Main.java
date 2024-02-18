package org.whn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: WangHn
 * @Date: 2024/1/27 13:22
 * @Description: ${Description}
 */
@SpringBootApplication
@MapperScan("org.whn.mapper")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}