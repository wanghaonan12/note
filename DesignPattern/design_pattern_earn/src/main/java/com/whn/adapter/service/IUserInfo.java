package com.whn.adapter.service;

/**
 * @Author: WangHn
 * @Date: 2024/5/29 17:34
 * @Description: 用户信息对象
 */
public interface IUserInfo {
    /**
     * 获得用户姓名
     * @return
     */
    public String getUserName();

    /**
     * 获得家庭地址
     * @return
     */
    public String getHomeAddress();

    /**
     * 手机号码，这个太重要，手机泛滥呀
     * @return
     */
    public String getMobileNumber();

    /**
     * 办公电话，一般式座机
     * @return
     */
    public String getOfficeTelNumber();

    /**
     * 这个人的职位是啥
     * @return
     */
    public String getJobPosition();

    /**
     * 获得家庭电话，这个有点缺德，我是不喜欢打家庭电话讨论工作
     * @return
     */
    public String getHomeTelNumber();
}
