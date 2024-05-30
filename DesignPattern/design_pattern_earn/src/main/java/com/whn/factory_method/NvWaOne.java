package com.whn.factory_method;

import com.whn.factory_method.factory.HumanFactory;
import com.whn.factory_method.service.Human;
import com.whn.factory_method.service.impl.BlackHuman;
import com.whn.factory_method.service.impl.WhiteHuman;
import com.whn.factory_method.service.impl.YellowHuman;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/28 16:18
 * @Description: 首先定义女娲，这真是额的神呀  女娲第一次尝试制作小人
 */
@Slf4j
public class NvWaOne {
    public static void main(String[] args) {

        //女娲第一次造人，试验性质，少造点，火候不足，缺陷产品 
        log.info("------------造出的第一批人是这样的：白人 -----------------"); 
        Human whiteHuman = HumanFactory.createHuman(WhiteHuman.class);
        whiteHuman.cry();
        whiteHuman.laugh();
        whiteHuman.talk();

        //女娲第二次造人，火候加足点，然后又出了个次品，黑人 
        log.info("\n\n------------造出的第二批人是这样的：黑人 -----------------"); 
        Human blackHuman = HumanFactory.createHuman(BlackHuman.class);
        blackHuman.cry();
        blackHuman.laugh();
        blackHuman.talk();

        //第三批人了，这次火候掌握的正好，黄色人种（不写黄人，免得引起歧义），备注：RB人不属于此列
        log.info("\n\n------------造出的第三批人是这样的：黄色人种 -----------------");
        Human yellowHuman = HumanFactory.createHuman(YellowHuman.class);
        yellowHuman.cry();
        yellowHuman.laugh();
        yellowHuman.talk();
    }
}
