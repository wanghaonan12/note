package org.whn.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whn.canal.RedisCanalClientExample;

/**
 * @Author: WangHn
 * @Date: 2024/1/27 14:58
 * @Description: OrderController
 */
@RestController
@Slf4j
@ApiModel(value = "redis canal 同步",description = "redis canal 同步")
@RequestMapping("/canal")
public class CanalController
{

    @Autowired
    private RedisCanalClientExample redisCanalClientExample;

    @ApiOperation("执行canal")
    @PostMapping(value = "/run")
    public void run()
    {
        redisCanalClientExample.canalExecution();
    }

}