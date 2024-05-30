package com.whn.abstract_factory.factory;

import com.whn.abstract_factory.enums.HumanEnum;
import com.whn.abstract_factory.service.Human;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/28 17:37
 * @Description: 工厂抽象类
 */
@Slf4j
public abstract class BaseHumanFactory implements HumanFactory {
    /**
     * 给定一个性别人种，创建一个人类出来 专业术语是产生产品等级
     * @param humanEnum
     * @return
     */
    protected Human createHuman(HumanEnum humanEnum) {
        Human human = null;

        //如果传递进来不是一个Enum中具体的一个Element的话，则不处理
        if (!humanEnum.getValue().isEmpty()) {
            try {
                //直接产生一个实例
                human = (Human)
                        ClassLoader.getSystemClassLoader().loadClass(Class.forName(humanEnum.getValue()).getName()).getConstructor().newInstance();
            } catch (Exception e) {
                //因为使用了enum，这个种异常情况不会产生了，除非你的enum有问题；
                log.error(e.getMessage());
                log.error("exception message", e);
            }
        }
        return human;
    }
}
