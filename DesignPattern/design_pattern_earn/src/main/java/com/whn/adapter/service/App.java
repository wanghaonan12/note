package com.whn.adapter.service;

import com.whn.adapter.dto.OuterUserInfo;
import com.whn.adapter.service.impl.UserInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/29 17:46
 * @Description: 这就是我们具体的应用了，比如老板要查所有的20-30的女性信息
 */
@Slf4j
public class App {
    public static void main(String[] args) {
        //没有与外系统连接的时候，是这样写的
        IUserInfo youngGirl = new UserInfo();
        int count = 101;
        //从数据库中查到101个
        for (int i = 0; i < count; i++) {
            log.info(youngGirl.getMobileNumber());
        }

        log.info("\n\n其他系统数据库的女孩-----------------------------------------------------");
        IUserInfo outerUserInfo = new OuterUserInfo();
        //从数据库中查到101个
        for (int i = 0; i < count; i++) {
            log.info(outerUserInfo.getMobileNumber());
        }
    }
}
