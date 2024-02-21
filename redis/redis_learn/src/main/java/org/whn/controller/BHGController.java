package org.whn.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.whn.po.Customer;
import org.whn.service.impl.BHGServiceImpl;
import org.springframework.data.geo.Point;

/**
 * @Author: WangHn
 * @Date: 2024/1/27 14:58
 * @Description: GuavaFilterController
 */
@RestController
@Slf4j
@ApiModel(value = "bhg Controller",description = "bhg Controller")
@RequestMapping("/bhg")
public class BHGController
{

    @Autowired
    private BHGServiceImpl bhgService;

    @ApiOperation("获取当前访问的UV数量")
    @RequestMapping(value = "/getUvCount",method = RequestMethod.GET)
    public Integer getUvCount()
    {
        return bhgService.getUvCount();
    }

    @ApiOperation("添加坐标geoadd")
    @RequestMapping(value = "/geoadd",method = RequestMethod.GET)
    public String geoAdd()
    {
        return bhgService.geoAdd();
    }

    @ApiOperation("获取经纬度坐标geopos")
    @RequestMapping(value = "/geopos",method = RequestMethod.GET)
    public Point position(String member)
    {
        return bhgService.position(member);
    }

    @ApiOperation("获取经纬度生成的base32编码值geohash")
    @RequestMapping(value = "/geohash",method = RequestMethod.GET)
    public String hash(String member)
    {
        return bhgService.hash(member);
    }

    @ApiOperation("获取两个给定位置之间的距离")
    @RequestMapping(value = "/geodist",method = RequestMethod.GET)
    public Distance distance(String member1, String member2)
    {
        return bhgService.distance(member1,member2);
    }

    @ApiOperation("通过经度纬度查找北京王府井附近的")
    @RequestMapping(value = "/georadius",method = RequestMethod.GET)
    public GeoResults radiusByxy()
    {
        return bhgService.radiusByxy();
    }

    @ApiOperation("通过地方查找附近,本例写死天安门作为地址")
    @RequestMapping(value = "/georadiusByMember",method = RequestMethod.GET)
    public GeoResults radiusByMember()
    {
        return bhgService.radiusByMember();
    }

    @ApiOperation("通过布隆过滤器查询 Customer")
    @RequestMapping(value = "/findCustomerByIdWithBloomFilter/{customerId}",method = RequestMethod.GET)
    public Customer findCustomerByIdWithBloomFilter(@PathVariable  Integer customerId)
    {
        return bhgService.findCustomerByIdWithBloomFilter(customerId);
    }

    @ApiOperation("添加布隆过滤器白名单")
    @RequestMapping(value = "/addWhiteList/{customerId}",method = RequestMethod.GET)
    public boolean addWhiteList(@PathVariable  Integer customerId)
    {
        return bhgService.addWhiteList(customerId);
    }

}