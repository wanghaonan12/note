package com.whn.abstract_factory.service;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/28 17:21
 * @Description: 男性抽象类
 */
@Slf4j
public abstract class BaseMen implements Human {
    @Override
    public void sex() {
        log.info("男性人类");
    }
}
