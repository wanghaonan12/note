package com.whn.proxy.service.impl;

import com.whn.proxy.service.KindWomen;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 16:05
 * @Description: 定一个潘金莲是什么样的人
 */
@Slf4j
public class PanJinLian implements KindWomen {
    @Override
    public void makeEyesWithMan() {
        log.info("潘金莲抛媚眼");
    }

    @Override
    public void happyWithMan() {
        log.info("潘金莲在和男人做那个.....");
    }
}
