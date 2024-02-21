package org.whn.service;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.whn.po.Customer;

/**
 * @Author: WangHn
 * @Date: 2024/2/21 14:14
 * @Description:
 */
public interface BHGService {

     /**
      * 获取当前的访问用户基数
      * @return
      */
     Integer getUvCount();

     /**
      * 获取指定位置的未知信息
      * @param member 名称
      * @return
      */
     Point position(String member);

     /**
      * 获取指定位置的未知信息的hash值
      * @param member 名称
      * @return
      */
     String hash(String member);

     /**
      * 两个位置的距离
      * @param member1
      * @param member2
      * @return
      */
     Distance distance(String member1, String member2);

     /**
      * 返回指定位置周围的东西
      * @return
      */
     GeoResults radiusByxy();

     /**
      * 返回指定位置，指定半径内的事物
      * @return
      */
     GeoResults radiusByMember();

     /**
      * 通过bitmap制作的过滤器 满足条件则进行查询 不满足则跳过
      * @param customerId
      * @return
      */
     Customer findCustomerByIdWithBloomFilter(Integer customerId);

     /**
      * 添加布隆多滤器的白名单
      * @param customerId
      * @return
      */
     boolean addWhiteList(Integer customerId);
}
