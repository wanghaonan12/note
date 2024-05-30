package com.whn.strategy.service.impl;

import com.whn.strategy.service.IStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 15:46
 * @Description: 求吴国太开个绿灯
 */
@Slf4j
public class GivenGreenLight implements IStrategy {
    @Override
    public void operate() {
        log.info("求吴国太开个绿灯,放行！");
    }
}
