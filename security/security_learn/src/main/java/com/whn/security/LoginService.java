package com.whn.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: WangHn
 * @Date: 2024/4/25 17:04
 * @Description: 实现用户验证
 */
public class LoginService implements UserDetailsService {
    BCryptPasswordEncoder bCryptPasswordEncoder = new
            BCryptPasswordEncoder();
    Map<String, String> initData = new HashMap<String,String>(){
        {
            put("admin","admin");
            put("user","user");
            put("guest","guest");
        }
    };

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        String s1 = initData.get(s);
        return new User(s,s1, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
    public static void main(String[] args) {
        // 创建密码解析器
        BCryptPasswordEncoder bCryptPasswordEncoder = new
                BCryptPasswordEncoder();
// 对密码进行加密
        String admin = bCryptPasswordEncoder.encode("admin");
        String user = bCryptPasswordEncoder.encode("user");
        String guest = bCryptPasswordEncoder.encode("guest");
// 打印加密之后的数据
        System.out.println("admin加密之后数据：\t"+admin);
        System.out.println("admin加密之后数据：\t"+admin);
        System.out.println("admin加密之后数据：\t"+admin);

//判断原字符加密后和加密之前是否匹配
//        boolean result = bCryptPasswordEncoder.matches("atguigu", atguigu);
// 打印比较结果
//        System.out.println("比较结果：\t"+result);
    }
}
