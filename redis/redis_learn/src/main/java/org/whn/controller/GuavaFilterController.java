package org.whn.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.whn.service.impl.GuavaBloomFilterServiceImpl;

/**
 * @Author: WangHn
 * @Date: 2024/1/27 14:58
 * @Description: GuavaFilterController
 */
@RestController
@Slf4j
@ApiModel(value = "guava 过滤器测试",description = "guava 过滤器测试")
@RequestMapping("/guava")
public class GuavaFilterController
{

    @Autowired
    private GuavaBloomFilterServiceImpl guavaBloomFilterService;

    @ApiOperation("初始化")
    @PostMapping(value = "/init")
    public void guavaInit()
    {
        guavaBloomFilterService.reload();
    }


    @ApiOperation("测试在数据集中的存在情况")
    @GetMapping(value = "/{count}")
    public void testFilterData(@PathVariable Integer count)
    {
         guavaBloomFilterService.testFilterData(count);
    }

    @ApiOperation("测试指定数据")
    @GetMapping(value = "/guavaFilter/{id}")
    public boolean guavaFilter(@PathVariable Integer id)
    {
        return guavaBloomFilterService.guavaFilter(id);
    }

}