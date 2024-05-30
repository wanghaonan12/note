package com.whn.singleton;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 16:34
 * @Description: 中国的历史上一般都是一个朝代一个皇帝，有两个皇帝的话，必然要PK出一个皇帝出来
 */
@Slf4j
public class Emperor {
    private String name;
    /**
     * 定义一个皇帝放在那里，然后给这个皇帝名字
     */
    private static Emperor emperor = null;

    private Emperor(String name){
        //世俗和道德约束你，目的就是不让你产生第二个皇帝
        this.name = name;
    }

    public static Emperor getInstance(String name){
        // 但是这种还会出现一种问题，就是在进行大量请求的时候进行下面的 emperor == null 判断，
        // 第一个判断为 true 通过，然后进行创建对象，假设创建对象 Emperor需要0.02s ，在这0.02s内又来了1个请求，就会再一次进去并创建对象
        // 这种问题在逻辑上是看不出来的，那要怎么办呢？解决方法请看 Emperor2
        if(emperor == null){
            //如果皇帝还没有定义，那就定一个
            emperor = new Emperor(name);
        }
        return emperor;
    }

    /**
     * 定义一个皇帝放在那里，然后给这个皇帝名字
     */
    public void emperorInfo(){
        log.info("我就是你们的皇帝...." + this.name);
    }
}
