package org.whn.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whn.service.impl.InventoryServiceImpl;

/**
 * @Author: WangHn
 * @Date: 2024/1/27 14:58
 * @Description: OrderController
 */
@RestController
@Slf4j
@ApiModel(value = "redis lock",description = "redis lock")
@RequestMapping("/redisLock")
public class RedisLockController
{

    @Autowired
    private InventoryServiceImpl inventoryServiceImpl;

    @ApiOperation("自研")
    @PostMapping(value = "/selfResearch")
    public String selfResearch()
    {return inventoryServiceImpl.selfLock();}

    @ApiOperation("redLock")
    @PostMapping(value = "/redLock")
    public String redLock()
    {return inventoryServiceImpl.redLock();}

    @ApiOperation("multiLock")
    @PostMapping(value = "/multiLock")
    public String multiLock()
    {return inventoryServiceImpl.multiLock();}

}