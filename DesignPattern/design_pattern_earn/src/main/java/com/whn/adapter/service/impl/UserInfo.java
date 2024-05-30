package com.whn.adapter.service.impl;

import com.whn.adapter.service.IUserInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/29 17:36
 * @Description: 接口的实现类
 */
@Slf4j
public class UserInfo implements IUserInfo {
    /**
     * 获得家庭地址，下属送礼也可以找到地方
     * @return
     */
    @Override
    public String getHomeAddress() {
        log.info("这里是员工的家庭地址....");
        return null;
    }

    /**
     * 获得家庭电话号码
     * @return
     */
    @Override
    public String getHomeTelNumber() {
        log.info("员工的家庭电话是....");
        return null;
    }

    /**
     * 员工的职位，是部门经理还是小兵
     * @return
     */
    @Override
    public String getJobPosition() {
        log.info("这个人的职位是BOSS....");
        return null;
    }

    /**
     * 手机号码
     * @return
     */
    @Override
    public String getMobileNumber() {
        return "这个人的手机号码是0000....";
    }

    /**
     * 办公室电话，烦躁的时候最好“不小心”把电话线踢掉，我经常这么干，对己对人都有好处
     * @return
     */
    @Override
    public String getOfficeTelNumber() {
        return "办公室电话是....";
    }

    /**
     * 
     * @return
     */
    @Override
    public String getUserName() {
        return "姓名叫做...";
    }
}
