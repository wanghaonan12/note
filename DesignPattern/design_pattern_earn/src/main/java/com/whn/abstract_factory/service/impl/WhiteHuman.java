package com.whn.abstract_factory.service.impl;

import com.whn.abstract_factory.service.BaseMen;
import com.whn.abstract_factory.service.Human;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 17:42
 * @Description: 白色人种，这个翻译的不准确，将就点吧
 */
@Slf4j
public class WhiteHuman extends BaseMen implements Human {

    @Override
    public void cry() {
       log.info("白色人种会哭");
    }

    @Override
    public void laugh() {
       log.info("白色人种会大笑，幸福呀！");
    }
    @Override
    public void talk() {
       log.info("白色人种会说话，一般说的都是双字节");
    }
}
