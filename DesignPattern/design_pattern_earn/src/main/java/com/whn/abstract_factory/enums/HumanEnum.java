package com.whn.abstract_factory.enums;

/**
 * @Author: WangHn
 * @Date: 2024/5/28 18:22
 * @Description: * 世界上有哪些类型的人，列出来 JDK 1.5开始引入enum类型也是目的的，吸引C程序员转过来
 */
public enum HumanEnum {
    /**
     * 把世界上所有人类型都定义出来
     */
    YELLOW_MAN_HUMAN("com.whn.abstract_factory.service.impl.YellowHuman"),

    YELLOW_WOMAN_HUMAN("com.whn.abstract_factory.service.impl.YellowWomanHuman"),

    WHITE_WOMAN_HUMAN("com.whn.abstract_factory.service.impl.WhiteWomanHuman"),

    WHITE_MAN_HUMAN("com.whn.abstract_factory.service.impl.WhiteHuman"),

    BLACK_WOMAN_HUMAN("com.whn.abstract_factory.service.impl.BlackWomanHuman"),

    BLACK_MAN_HUMAN("com.whn.abstract_factory.service.impl.BlackHuman");
    private String value = "";
    /**
     * 定义构造函数，目的是Data(value)类型的相匹配
     */
    private HumanEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
