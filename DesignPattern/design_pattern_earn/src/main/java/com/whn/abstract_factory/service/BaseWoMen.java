package com.whn.abstract_factory.service;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/28 17:21
 * @Description: 女性抽象类
 */
@Slf4j
public abstract class BaseWoMen implements Human {
    @Override
    public void sex() {
        log.info("女性人类--------------------------");
    }
}
