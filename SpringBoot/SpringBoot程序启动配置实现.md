# SpringBoot监听事件

实现了`ApplicationListener`接口用于监听应用启动完成的事件。在应用启动完成后，会尝试获取一个配置的首页地址，并根据这个地址在浏览器中打开对应的页面。这个过程支持多种操作系统，包括Linux、Windows和Mac。如果无法识别操作系统，或者在打开浏览器的过程中出现异常，会记录相应的日志信息。

`ApplicationListener`这个接口允许我们监听特定类型的事件。在这里，我们关注的是`ApplicationStartedEvent`事件，即当Spring Boot应用成功启动后触发的事件。

![image-20240319114956883](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240319114956883.png)

`ApplicationListener`监听的事件继承与`ApplicationEvent`,他又30多个实现类

```java

@SpringBootApplication
public class App implements ApplicationListener<ApplicationStartedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        // 启动SpringApplication
        SpringApplication.run(App.class, args);
    }

    /**
     * 当Spring应用启动完成时触发的事件处理方法。尝试在浏览器中打开指定的首页页面。
     *
     * @param event Spring应用启动完成事件
     */
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        // 尝试获取配置的首页地址
        String indexPage = event.getApplicationContext().getEnvironment().getProperty("offline.index");
        // 验证首页地址是否合法
        if (!StringUtils.hasText(indexPage) || !indexPage.startsWith("http")) {
            logger.warn("非法的首页地址：{}", indexPage);
            return;
        }
        try {
            // 获取操作系统类型
            String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
            String cmd;
            // 根据操作系统类型选择打开浏览器的方式
            if (os.contains("linux")) {
                cmd = "xdg-open ";
            } else if (os.contains("windows")) {
                cmd = "cmd /c start ";
            } else if (os.contains("mac")) {
                cmd = "open ";
            } else {
                logger.error("不支持的操作系统类型：{}，请手动输入地址：{}", os, indexPage);
                return;
            }
            // 执行命令，在浏览器中打开首页地址
            Runtime.getRuntime().exec(cmd + indexPage);
        } catch (Exception e) {
            logger.error("打开浏览器异常，请手动输入地址：{}", indexPage);
        }
    }
}

```

## 自定义监控

1. 自定义监听事件对象

```java
@Getter
@Setter
@ToString
public class MyApplicationEvent extends ApplicationEvent {

    private Integer age;

    private String name;

    /**
     * 需要重写构造方法
     * @param source
     * @param name
     * @param age
     */
    public MyApplicationEvent(Object source, String name, Integer age) {
        super(source);
        this.name = name;
        this.age = age;
    }
}
```

2. 容器内添加监听器实现，这里添加了泛型`MyApplicationEvent`所以只监听他的事件

```java
package com.lsqingfeng.springboot.applicationEvent;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @className: MyApplicationEventListener
 * @description:事件监听器
 * @author: sh.Liu
 * @date: 2022-03-23 14:50
 */
@Component
public class MyApplicationEventListener implements ApplicationListener<MyApplicationEvent> {

    @Override
    public void onApplicationEvent(MyApplicationEvent event) {
        System.out.println("收到消息：" + event);
    }
}

```

测试

```java
package com.lsqingfeng.springboot.controller;

import com.lsqingfeng.springboot.applicationEvent.MyApplicationEvent;
import com.lsqingfeng.springboot.base.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: ApplicationEventController
 * @description:
 * @author: sh.Liu
 * @date: 2022-03-23 15:21
 */
@RestController
@RequestMapping("event")
public class ApplicationEventController {

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/push")
    public Result pushEvent(){
        MyApplicationEvent myApplicationEvent = new MyApplicationEvent(this,"zhangsan", 10);
        //发布事件
        applicationContext.publishEvent(myApplicationEvent);
        return Result.success();
    }

    @RequestMapping("/push2")
    public Result pushEvent2(){
        //发布事件
        applicationContext.publishEvent("大家好");
        return Result.success();
    }
}
```

![image-20240319135554080](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240319135554080.png)

## 注解实现监听器

之前在第二部以实现类的方式实现了监听事件，现在使用注解实现监听器

```java
package com.lsqingfeng.springboot.applicationEvent;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @className: MyApplicationEventListener2
 * @description: 注解实现监听器
 * @author: sh.Liu
 * @date: 2022-03-23 15:56
 */
@Component
public class MyApplicationEventListener2 {

    @EventListener
    public void onEvent(MyApplicationEvent event){
        System.out.println("收到消息2：" + event);
    }
}
```

