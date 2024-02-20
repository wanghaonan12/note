package org.whn.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whn.po.Product;
import org.whn.service.impl.JHSServiceImpl;

import java.util.List;

/**
 * @Author: WangHn
 * @Date: 2024/1/27 14:58
 * @Description: OrderController
 */
@RestController
@Slf4j
@ApiModel(value = "JHS 互斥更新",description = "JHS 互斥更新")
@RequestMapping("/jhs")
public class JHSController
{

    @Autowired
    private JHSServiceImpl jhsServiceImpl;

    @ApiOperation("getProductS 没有互斥")
    @PostMapping(value = "/getProductS/{page}/{size}")
    public List<Product> getProductS(@PathVariable  int page,@PathVariable int size)
    {
       return jhsServiceImpl.getProductS(page,size);
    }


    @ApiOperation("getProductS 互斥更新")
    @PostMapping(value = "/getProductSAB/{page}/{size}")
    public List<Product> getProductSAB(@PathVariable  int page, @PathVariable int size)
    {
       return jhsServiceImpl.getProductSAB(page,size);
    }

}