package com.whn.singleton;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 16:55
 * @Description: 解决异步时的异常问题
 */
@Slf4j
public class Emperor2 {
    private String name;
    /**
     * 直接创建一个皇帝出来
     */
    private static final Emperor2 emperor = new Emperor2("富贵");

    private Emperor2(String name) {
        //世俗和道德约束你，目的就是不让你产生第二个皇帝
        this.name = name;
    }

    public static Emperor2 getInstance() {
        return emperor;
    }

    /**
     * 定义一个皇帝放在那里，然后给这个皇帝名字
     */
    public void emperorInfo() {
        log.info("我就是你们的皇帝...." + this.name);
    }
}

