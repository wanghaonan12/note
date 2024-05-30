package com.whn.template_method;

import com.whn.template_method.impl.HummerH1Model;
import com.whn.template_method.impl.HummerH2Model;

/**
 * @Author: WangHn
 * @Date: 2024/5/30 11:54
 * @Description: 客户开始使用这个模型
 */
public class Client {
    public static void main(String[] args) {
        //客户开着H1型号，出去遛弯了
        HummerModel h1 = new HummerH1Model();
        h1.run(); //汽车跑起来了；

        //客户开H2型号，出去玩耍了
        HummerModel h2 = new HummerH2Model();
        h2.run();
    }
}
