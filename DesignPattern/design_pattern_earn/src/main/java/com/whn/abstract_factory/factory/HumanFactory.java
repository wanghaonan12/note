package com.whn.abstract_factory.factory;

import com.whn.abstract_factory.service.Human;

/**
 * @Author: WangHn
 * @Date: 2024/5/28 17:34
 * @Description: 这次定一个接口，应该要造不同性别的人，需要不同的生产线那这个八卦炉必须可以制造男人和女人
 */
public interface HumanFactory {
    /**
     * 制造黄色人种
     * @return
     */
    public Human createYellowHuman();

    /**
     * 制造一个白色人种
     * @return
     */
    public Human createWhiteHuman();

    /**
     * 制造一个黑色人种
     * @return
     */
    public Human createBlackHuman();
}
