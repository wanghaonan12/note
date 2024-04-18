package org.whn.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.whn.po.User;

/**
 * @Author: WangHn
 * @Date: 2024/4/11 15:56
 * @Description: es User dao操作类
 */
@Repository
public interface UserDao extends ElasticsearchRepository<User,String> {
}
