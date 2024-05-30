package com.whn.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author: WangHn
 * @Date: 2024/4/25 16:58
 * @Description: 安全认证配置
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin() // 表单登录
                .and()
                .authorizeRequests() // 认证配置
                .antMatchers("/swagger-ui/**",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/**") //表示配置请求路径
                .permitAll() // 指定 URL 无需保护。
                .anyRequest() // 任何请求
                .authenticated(); // 都需要身份验证
        // 关闭跨站请求防护机制
// 关闭 csrf
        http.csrf().disable();
    }

    // 注入 PasswordEncoder 类到 spring 容器中
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
