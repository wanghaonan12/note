package com.whn.safety;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author: WangHn
 * @Date: 2024/3/16 11:04
 * @Description: 线程安全
 */
public class ThreadSafety {
    public static void main(String[] args) {
        //ArrayList<String> list = new ArrayList<>();
        //for (int i = 0; i < 10; i++) {
        //    int finalI = i;
        //    new Thread(() -> {
        //        list.add(finalI +"");
        //        System.out.println(list);
        //    }).start();
        //}

        //Vector<String> list = new Vector<>();
        //for (int i = 0; i < 10; i++) {
        //    int finalI = i;
        //    new Thread(() -> {
        //        list.add(finalI +"");
        //        System.out.println(list);
        //    }).start();
        //}

        //List<Object> list = Collections.synchronizedList(new ArrayList<>());
        //for (int i = 0; i < 10; i++) {
        //    int finalI = i;
        //    new Thread(() -> {
        //        list.add(finalI +"");
        //        System.out.println(list);
        //    }).start();
        //}

        //List<String> list = new CopyOnWriteArrayList<>();
        //for (int i = 0; i < 10; i++) {
        //    int finalI = i;
        //    new Thread(() -> {
        //        list.add(finalI +"");
        //        System.out.println(list);
        //    }).start();
        //}

         List<String> list = new CopyOnWriteArrayList<>();
        Set<String> set = new CopyOnWriteArraySet<>();
        Map<String,Object> map = new ConcurrentHashMap<String,Object>();

    }
}
