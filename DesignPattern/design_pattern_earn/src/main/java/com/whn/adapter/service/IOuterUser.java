package com.whn.adapter.service;

import java.util.Map;

/**
 * @Author: WangHn
 * @Date: 2024/5/30 10:26
 * @Description: 外系统的人员信息
 */
public interface IOuterUser {
    /**
     * 基本信息，比如名称，性别，手机号码了等
     * @return
     */
    public Map<String,String> getUserBaseInfo();

    /**
     * 工作区域信息
     * @return
     */
    public Map<String,String> getUserOfficeInfo();

    /**
     * 用户的家庭信息
     * @return
     */
    public Map<String,String> getUserHomeInfo();
}
