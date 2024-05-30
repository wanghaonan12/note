package com.whn.singleton;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 16:44
 * @Description: 大臣是天天要面见皇帝，今天见的皇帝和昨天的，前天不一样那就出问题了！
 */
@Slf4j
public class Minister {
    /**
     * @param args
     */
    public static void main(String[] args) {
        //第一天
        Emperor emperor1 = Emperor.getInstance("whn");
        emperor1.emperorInfo(); //第一天见的皇帝叫什么名字呢？
        //第二天
        Emperor emperor2 = Emperor.getInstance("123");
        emperor2.emperorInfo();
        //第三天
        Emperor emperor3 = Emperor.getInstance("456");
        emperor3.emperorInfo();
        //三天见的皇帝都是同一个人，荣幸吧！

        //true
        log.info((emperor1 == emperor2)+"");


    }
}
