package com.whn.abstract_factory.factory;

import com.whn.abstract_factory.enums.HumanEnum;
import com.whn.abstract_factory.service.Human;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 17:47
 * @Description: 工厂类
 */
@Slf4j
public class HumanManFactory extends BaseHumanFactory implements HumanFactory{

    @Override
    public Human createYellowHuman() {
        return super.createHuman(HumanEnum.YELLOW_MAN_HUMAN);
    }

    @Override
    public Human createWhiteHuman() {
        return super.createHuman(HumanEnum.WHITE_MAN_HUMAN);
    }

    @Override
    public Human createBlackHuman() {
        return super.createHuman(HumanEnum.BLACK_MAN_HUMAN);
    }
}
