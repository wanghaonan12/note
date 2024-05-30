package com.whn.factory_method.factory;

import com.whn.factory_method.service.Human;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @Author: WangHn
 * @Date: 2024/5/27 17:47
 * @Description: 工厂类
 */
@Slf4j
public class HumanFactory {

    private HumanFactory() {
        throw new IllegalStateException("Utility class");
    }

    private static HashMap<String,Human> humans = new HashMap<>();

    private static final Random RANDOM = new Random();
    /**
     * 定一个烤箱，泥巴塞进去，人就出来，这个太先进了
     * @param c 类对象 必须是Human的实现类才行
     * @return 制作出来的小人
     */
    public static Human createHuman(Class<? extends Human> c){
        //定义一个类型的人类 
        Human human=null;
        try {
            if(humans.containsKey(c.getSimpleName())){
                human = humans.get(c.getSimpleName());
            }else {
                //产生一个人种
                human = (Human) ClassLoader.getSystemClassLoader().loadClass(c.getName()).getConstructor().newInstance();
                humans.put(c.getSimpleName(), human);
            }
        } catch (InstantiationException e) {
            //你要是不说个人种颜色的话，没法烤，要白的黑，你说话了才好烤
           log.error("必须指定人种的颜色");
        } catch (IllegalAccessException e) {
            //定义的人种有问题，那就烤不出来了，这是...
           log.error("人种定义错误！");
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            //你随便说个人种，我到哪里给你制造去？！
           log.error("混蛋，你指定的人种找不到！");
        }
        return human;
    }

    /**
     * 女娲生气了，把一团泥巴塞到八卦炉，哎产生啥人种就啥人种
     * @return
     */
    public static Human createHuman(List<Class<? extends Human>> concreteHumanList){
        //定义一个类型的人类
        Human human=null;
        //八卦炉自己开始想烧出什么人就什么人
        int rand = RANDOM.nextInt(concreteHumanList.size());
        human = createHuman( concreteHumanList.get(rand));
        return human;
    }
}
