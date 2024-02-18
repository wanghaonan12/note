package org.whn.service;

import org.whn.po.TUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author wangRich
 * @description 针对表【t_user】的数据库操作Service
 * @createDate 2024-02-18 13:35:14
 */
public interface TUserService extends IService<TUser> {
    TUser findUserById(Long id);

    TUser findUserById2(Long id);

    boolean addUser(String name);

    boolean deleteUser(Long id);

    boolean updateUser(TUser user);
}
