package com.whn.multition;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 17:08
 * @Description: 中国的历史上一般都是一个朝代一个皇帝，有两个皇帝的话，必然要PK出一个皇帝出来。问题出来了：
 * 如果真在一个时间，中国出现了两个皇帝怎么办？比如明朝土木堡之变后，
 * 明英宗被俘虏，明景帝即位，但是明景帝当上皇帝后乐疯了，
 * 竟然忘记把他老哥明英宗削为太上皇,也就是在这一个多月的时间内，中国竟然有两个皇帝！
 */
@Slf4j
public class Emperor {
    /**
     * 最多只能有连个皇帝
     */
    private static final int MAX_NUM_OF_EMPEROR = 2;
    /**
     * 皇帝叫什么名字
     */
    private static final List<String> EMPEROR_INFO_LIST = new ArrayList<>(MAX_NUM_OF_EMPEROR);
    /**
     * 装皇帝的列表
     */
    private static final List<Emperor> EMPEROR_LIST = new ArrayList<>(MAX_NUM_OF_EMPEROR);

    /**
     * 随机数
     */
    private static final Random RANDOM = new Random();
    /**
     * 正在被人尊称的是那个皇帝
     */

    private static int countNumOfEmperor = 0;

    //先把2个皇帝产生出来
    static {
        //把所有的皇帝都产生出来
        for (int i = 0; i < MAX_NUM_OF_EMPEROR; i++) {
            EMPEROR_LIST.add(new Emperor("皇" + (i + 1) + "帝"));
        }
    }

    /**
     * 就这么多皇帝了，不允许再推举一个皇帝(new 一个皇帝）
     */
    private Emperor() {
        //世俗和道德约束你，目的就是不让你产生第二个皇帝
    }

    private Emperor(String info) {
        EMPEROR_INFO_LIST.add(info);
    }

    /**
     * 路上随便遇到的皇帝
     *
     * @return 皇帝
     */
    @NonNull
    public static Emperor getInstance() {
        //随机拉出一个皇帝，只要是个精神领袖就成
        countNumOfEmperor = RANDOM.nextInt(MAX_NUM_OF_EMPEROR);
        return EMPEROR_LIST.get(countNumOfEmperor);
    }

    /**
     * 有事情找指定的某个皇帝
     *
     * @param count 谁
     * @return 皇帝
     */
    @NonNull
    public static Emperor getInstance(int count) {
        Assert.isTrue(count < EMPEROR_LIST.size() && 0 <= count, "下标越界了，没有这个皇帝！");
        return EMPEROR_LIST.get(count);
    }

    /**
     * 皇帝叫什么名字呀
     */
    public void emperorInfo() {
        log.info(EMPEROR_INFO_LIST.get(countNumOfEmperor));
    }
}
