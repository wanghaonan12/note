package com.whn.controller;

import com.whn.service.impl.InventoryServiceImpl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: WangHn
 * @Date: 2024/1/27 14:58
 * @Description: OrderController
 */
@RestController
@Slf4j
@ApiModel(value = " RedLock",description = " RedLock")
@RequestMapping("/redlock")
public class JHSController
{

    @Autowired
    private InventoryServiceImpl inventoryServiceImpl;

    @ApiOperation("getProductS 没有互斥")
    @GetMapping(value = "/multiLock")
    public void getProductS()
    {
        inventoryServiceImpl.multiLock();
    }

}