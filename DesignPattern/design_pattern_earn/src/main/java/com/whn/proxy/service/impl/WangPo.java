package com.whn.proxy.service.impl;

import com.whn.proxy.service.KindWomen;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 16:05
 * @Description: 王婆这个人老聪明了，她太老了，是个男人都看不上,但是她有智慧有经验呀，她作为一类女人的代理！
 */
@Slf4j
public class WangPo implements KindWomen {
    private KindWomen kindWomen;

    /**
     * 默认的话，是潘金莲的代理
     */
    public WangPo(){
        this.kindWomen = new PanJinLian();
    }

    /**
     * 她可以是KindWomen的任何一个女人的代理，只要你是这一类型
     * @param kindWomen
     */
    private WangPo(KindWomen kindWomen){
        this.kindWomen = kindWomen;
    }

    /**
     * 联系贾氏的王婆
     * @return
     */
    public static WangPo WangPoContactJiaShi(){
        return new WangPo(new JiaShi());
    }
    /**
     * 王婆这么大年龄了，谁看她抛媚眼？！
     */
    @Override
    public void makeEyesWithMan() {
        this.kindWomen.makeEyesWithMan();
    }

    /**
     * 自己老了，干不了，可以让年轻的代替
     */
    @Override
    public void happyWithMan() {
        this.kindWomen.happyWithMan();
    }
}
