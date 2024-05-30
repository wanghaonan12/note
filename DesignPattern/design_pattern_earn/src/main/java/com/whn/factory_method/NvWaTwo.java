package com.whn.factory_method;

import com.whn.factory_method.factory.HumanFactory;
import com.whn.factory_method.service.Human;
import com.whn.factory_method.utils.ClassUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Author: WangHn
 * @Date: 2024/5/28 16:18
 * @Description: 首先定义女娲，这真是额的神呀  女娲第二次尝试制作小人
 */
@Slf4j
public class NvWaTwo {
    public static void main(String[] args) {

        List<Class<? extends Human>> concreteHumanList = ClassUtils.getAllClassByInterface(Human.class);
        int count = 10;
        //女娲第二次造人，试验性质，少造点，火候不足，缺陷产品
        for (int i = 0; i < count; i++) {
            Human whiteHuman = HumanFactory.createHuman(concreteHumanList);
            whiteHuman.cry();
            whiteHuman.laugh();
            whiteHuman.talk();
            log.info("-------------------------------------\n\n");
        }
    }
}
