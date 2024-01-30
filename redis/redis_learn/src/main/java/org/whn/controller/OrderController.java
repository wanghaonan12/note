package org.whn.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.whn.service.OrderService;

import javax.annotation.Resource;

/**
 * @Author: WangHn
 * @Date: 2024/1/27 14:58
 * @Description: OrderController
 */
@RestController
@Slf4j
@ApiModel(value = "订单接口",description = "订单接口")
public class OrderController
{
    @Resource
    private OrderService orderService;

    @ApiOperation("新增订单")
    @GetMapping(value = "/order/add")
    public String addOrder()
    {
      return  orderService.addOrder();
    }


    @ApiOperation("按orderId查订单信息")
    @GetMapping(value = "/order/{id}")
    public String findUserById(@PathVariable Integer id)
    {
        return orderService.getOrderById(id);
    }
}