package com.whn.adapter.service.impl;

import com.whn.adapter.service.IOuterUser;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: WangHn
 * @Date: 2024/5/30 10:28
 * @Description: 外系统的用户信息的实现类
 */
public class OuterUser implements IOuterUser {
    @Override
    public Map<String, String> getUserBaseInfo() {
        Map<String, String> baseInfoMap = new HashMap<>(2);
        baseInfoMap.put("userName", "这个员工叫混世魔王....");
        baseInfoMap.put("mobileNumber", "这个员工电话是....");
        return baseInfoMap;
    }

    @Override
    public Map<String, String> getUserHomeInfo() {
        Map<String, String> homeInfo = new HashMap<>(2);
        homeInfo.put("homeTelNumbner", "员工的家庭电话是....");
        homeInfo.put("homeAddress", "员工的家庭地址是....");
        return homeInfo;
    }

    @Override
    public Map<String, String> getUserOfficeInfo() {
        Map<String, String> officeInfo = new HashMap<>(2);
        officeInfo.put("jobPosition", "这个人的职位是BOSS...");
        officeInfo.put("officeTelNumber", "员工的办公电话是....");
        return officeInfo;
    }
}
