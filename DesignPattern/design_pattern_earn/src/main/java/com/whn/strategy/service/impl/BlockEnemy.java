package com.whn.strategy.service.impl;

import com.whn.strategy.service.IStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 15:47
 * @Description: 孙夫人断后，挡住追兵
 */
@Slf4j
public class BlockEnemy implements IStrategy {
    @Override
    public void operate() {
        log.info("孙夫人断后，挡住追兵");
    }
}
