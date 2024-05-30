package com.whn.strategy;

import com.whn.strategy.service.IStrategy;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 15:49
 * @Description: 计谋有了，那还要有锦囊
 */
public class Context {
    /**
     * 构造函数，你要使用那个妙计
     */
    private IStrategy straegy;
    public Context(IStrategy strategy){
        this.straegy = strategy;
    }

    /**
     * 使用计谋了，看我出招了
     */
    public void operate(){
        this.straegy.operate();
    }
}
