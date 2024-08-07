package com.whn.template_method.impl;

import com.whn.template_method.HummerModel;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/30 11:27
 * @Description: H2和H2有什么差别，还真不知道，真没接触过悍马
 */
@Slf4j
public class HummerH2Model extends HummerModel {

    @Override
    public void alarm() {
        log.info("悍马H2鸣笛...");
    }

    @Override
    public void engineBoom() {
        log.info("悍马H2引擎声音是这样在...");
    }

    @Override
    public void start() {
        log.info("悍马H2发动...");
    }

    @Override
    public void stop() {
        log.info("悍马H2停车...");
    }

    /**
     * 这个方法是很有意思的，它要跑，那肯定要启动，停止了等，也就是要调其他方法
     */
    //@Override
    //public void run() {
    //
    //    //先发动汽车
    //    this.start();
    //
    //    //引擎开始轰鸣
    //    this.engineBoom();
    //
    //    //然后就开始跑了，跑的过程中遇到一条狗挡路，就按喇叭
    //    this.alarm();
    //
    //    //到达目的地就停车
    //    this.stop();
    //}


}
