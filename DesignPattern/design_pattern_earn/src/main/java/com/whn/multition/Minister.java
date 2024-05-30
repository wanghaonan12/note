package com.whn.multition;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 17:29
 * @Description: 大臣们悲惨了，一个皇帝都伺候不过来了，现在还来了两个个皇帝TND，不管了，找到个皇帝，磕头，请按就成了！
 */
@Slf4j
public class Minister {
    public static void main(String[] args) {
        /**
         * 10个大臣
         */
        int ministerNum = 10;
        //路上随便遇到的皇帝
        for (int i = 0; i < ministerNum; i++) {
            Emperor emperor = Emperor.getInstance();
            log.info("第" + (i + 1) + "个大臣参拜的是：");
            emperor.emperorInfo();
        }
        log.info("------------------------------------------------------------------");
        // 新皇帝找10个人进行刺杀回来的皇帝
        for (int i = 0; i < ministerNum; i++) {
            Emperor emperor = Emperor.getInstance(2);
            log.info("第" + (i + 1) + "个大臣参拜的是：");
            emperor.emperorInfo();
        }
    }
}
