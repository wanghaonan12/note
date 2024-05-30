package com.whn.adapter.dto;

import com.whn.adapter.service.IUserInfo;
import com.whn.adapter.service.impl.OuterUser;

/**
 * @Author: WangHn
 * @Date: 2024/5/30 10:56
 * @Description: 把OuterUser包装成UserInfo
 */
public class OuterUserInfo extends OuterUser implements IUserInfo {
    @Override
    public String getUserName() {
        return super.getUserBaseInfo().get("userName");
    }

    @Override
    public String getHomeAddress() {
        return super.getUserHomeInfo().get("homeAddress");
    }

    @Override
    public String getMobileNumber() {
        return super.getUserBaseInfo().get("mobileNumber");
    }

    @Override
    public String getOfficeTelNumber() {
        return super.getUserOfficeInfo().get("officeTelNumber");
    }

    @Override
    public String getJobPosition() {
        return super.getUserOfficeInfo().get("jobPosition");
    }

    @Override
    public String getHomeTelNumber() {
        return super.getUserHomeInfo().get("homeTelNumbner");
    }
}
