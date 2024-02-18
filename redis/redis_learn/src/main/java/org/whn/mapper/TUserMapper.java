package org.whn.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.whn.po.TUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author wangRich
* @description 针对表【t_user】的数据库操作Mapper
* @createDate 2024-02-18 13:35:14
* @Entity generator.po.TUser
*/
@Mapper
public interface TUserMapper extends BaseMapper<TUser> {

}




