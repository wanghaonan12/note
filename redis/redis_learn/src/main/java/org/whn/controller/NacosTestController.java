package org.whn.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: WangHn
 * @Date: 2024/1/27 14:32
 * @Description: nacos 测试 controller
 */
@Controller
@RequestMapping("/nacos")
@ApiModel(value = "nacos测试",description = "nacos测试")
public class NacosTestController {

    @NacosValue(value = "${my:default}", autoRefreshed = true)
    private String useLocalCache;

    @GetMapping("/get")
    @ResponseBody
    @ApiOperation(value = "nacos get")
    public String get() {
        return useLocalCache;
    }
}
