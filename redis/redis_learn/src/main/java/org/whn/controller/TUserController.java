package org.whn.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whn.po.TUser;
import org.whn.service.impl.TUserServiceImpl;

/**
 * @Author: WangHn
 * @Date: 2024/1/27 14:58
 * @Description: OrderController
 */
@RestController
@Slf4j
@ApiModel(value = "redis 读写一致性测试",description = "redis 读写一致性测试")
@RequestMapping("/user")
public class TUserController
{

    @Autowired
    private TUserServiceImpl tUserServiceImpl;

    @ApiOperation("新增用户")
    @PostMapping(value = "/add/{name}")
    public boolean addOrder(@PathVariable String name)
    {
      return  tUserServiceImpl.addUser(name);
    }


    @ApiOperation("删除用户")
    @DeleteMapping(value = "/{id}")
    public boolean deleteUser(@PathVariable Long id)
    {
        return tUserServiceImpl.deleteUser(id);
    }

    @ApiOperation("获取用户2")
    @GetMapping(value = "/findUserById/{id}")
    public TUser findUserById(@PathVariable Long id)
    {
        return tUserServiceImpl.findUserById(id);
    }
    @ApiOperation("获取用户2")
    @GetMapping(value = "/findUserById2/{id}")
    public TUser findUserById2(@PathVariable Long id)
    {
        return tUserServiceImpl.findUserById2(id);
    }

}