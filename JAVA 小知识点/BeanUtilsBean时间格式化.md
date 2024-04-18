# BeanUtilsBean转Class

```java
import org.apache.commons.beanutils.BeanUtilsBean;

public static void main(String[] args) {
    OrderVO vo = new OrderVO();
    Map<String, Object> orderMap = new HashMap<>();
    orderMap.put("id", "123456789");
    orderMap.put("orderDate","2024-02-02");

    // 填充vo对象的属性值，来源为map中的键值对
    BeanUtilsBean.populate(vo, orderMap);
}
```

## 程序报错

在字符串转换成日期时出现错误

> org.apache.commons.beanutils.ConversionException: DateConverter does not support default String to 'Date' conversion.

## 处理

> 创建新的转换器替换之前的默认转换器

```java
import org.apache.commons.beanutils.BeanUtilsBean;

public static void main(String[] args) {
    OrderVO vo = new OrderVO();
    Map<String, Object> orderMap = new HashMap<>();
    orderMap.put("id", "123456789");
    orderMap.put("orderDate","2024-02-02");


    // 创建一个日期时间转换器并设置日期格式
    DateTimeConverter dtConverter = new DateConverter();
    dtConverter.setPattern("yyyy-MM-dd");

    // 创建一个转换工具bean，用于注册自定义的日期转换器，并取消默认的日期转换器注册
    ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
    // 取消默认转换器注册
    convertUtilsBean.deregister(java.util.Date.class); 
    // 注册自定义日期转换器
    convertUtilsBean.register(dtConverter, java.util.Date.class); 
    // 使用转换工具初始化BeanUtilsBean，以便在填充对象属性时使用自定义的转换器
    BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());
    // 填充vo对象的属性值，来源为map中的键值对
    beanUtilsBean.populate(vo, orderMap);
}
```

完成！！！