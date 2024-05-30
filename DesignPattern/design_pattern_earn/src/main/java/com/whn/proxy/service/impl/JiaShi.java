package com.whn.proxy.service.impl;

import com.whn.proxy.service.KindWomen;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 16:22
 * @Description:
 */
@Slf4j
public class JiaShi implements KindWomen {
    @Override
    public void makeEyesWithMan() {
        log.info("贾氏抛媚眼");
    }

    @Override
    public void happyWithMan() {
        log.info("贾氏正在Happy中......");
    }
}
