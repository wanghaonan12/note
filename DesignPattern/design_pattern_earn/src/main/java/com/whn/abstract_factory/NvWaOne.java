package com.whn.abstract_factory;

import com.whn.abstract_factory.factory.HumanFactory;
import com.whn.abstract_factory.factory.HumanManFactory;
import com.whn.abstract_factory.factory.HumanWomanFactory;
import com.whn.abstract_factory.service.Human;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/28 16:18
 * @Description: 首先定义女娲，这真是额的神呀  女娲第一次尝试制作小人
 */
@Slf4j
public class NvWaOne {
    public static void main(String[] args) {
        //第一条生产线，女性生产线
        HumanFactory maleHumanFactory = new HumanManFactory();
        //第二条生产线，女性生产线
        HumanFactory femaleHumanFactory = new HumanWomanFactory();
        Human blackHuman = maleHumanFactory.createBlackHuman();
        Human whiteHuman = femaleHumanFactory.createWhiteHuman();
        blackHuman.talk();
        whiteHuman.cry();
    }
}
