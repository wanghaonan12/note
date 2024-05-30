package com.whn.strategy.service.impl;

import com.whn.strategy.service.IStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 15:46
 * @Description: 找乔国老帮忙，使孙权不能杀刘备
 */
@Slf4j
public class BackDoor implements IStrategy {
    @Override
    public void operate() {
        log.info("找乔国老帮忙，让吴国太给孙权施加压力");
    }
}
