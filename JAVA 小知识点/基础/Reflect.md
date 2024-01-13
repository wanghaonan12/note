# Reflect
## 反射的引入
> Java 中的反射机制提供了在运行时动态地获取类的信息和操作类的成员（字段、方法、构造函数等）的能力。反射使得我们可以在编译时不需要知道类的具体信息，而在运行时动态地操作类和对象。

以下是一个案例，说明 Java 中为什么需要反射：

假设我们有一个类 `Person`，它有一些私有字段和方法：

```java
public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private void sayHello() {
        System.out.println("Hello, I'm " + name);
    }
}
```

现在，假设我们想要通过一个字符串来创建一个 `Person` 对象、访问其私有字段和调用私有方法。如果没有反射机制，我们将无法实现这样的操作。

使用反射，我们可以实现以下操作：

```java
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Code_01_ReflectBringIn {
    public static void main(String[] args) throws Exception {
        // 通过字符串创建 Person 对象
        Class<?> personClass = Class.forName("Person");
        Person person = (Person) personClass.getDeclaredConstructor(String.class, int.class)
                                          .newInstance("Alice", 25);

        // 访问私有字段
        Field ageField = personClass.getDeclaredField("age");
        ageField.setAccessible(true);
        int age = (int) ageField.get(person);
        System.out.println("Age: " + age);

        // 调用私有方法
        Method sayHelloMethod = personClass.getDeclaredMethod("sayHello");
        sayHelloMethod.setAccessible(true);
        sayHelloMethod.invoke(person);
    }
}
```

在上述示例中，我们使用反射机制获取了 `Person` 类的信息，并且可以通过字符串动态地创建 `Person` 对象、访问私有字段和调用私有方法。

反射机制的应用场景包括但不限于：
- 动态地创建对象，可以在编译时不确定类的具体类型。
- 动态地操作对象的成员，包括字段的读写和方法的调用。
- 获取类的元数据，如类的注解、接口、父类等。
- 实现框架、库和工具，提供通用的解析和操作能力。

尽管反射机制具有强大的灵活性和动态性，但由于使用反射可能会牺牲一些性能，因此应谨慎使用。在大多数情况下，应优先选择直接访问类的公共接口和成员，只有在必要时才使用反射机制。

## 反射小案例1

Java反射是一种强大的机制，允许我们在运行时获取和操作类的信息。通过反射，我们可以动态地加载类、创建对象、调用方法和访问字段，即使在编译时我们并不知道这些类的具体信息。下面是一些关于Java反射中常用的Class类方法的解释：

1. 获取Class对象
   - `Class.forName(String className)`: 根据类的完全限定名获取对应的Class对象。
   - `obj.getClass()`: 通过已有对象获取对应的Class对象。
   - `ClassName.class`: 直接通过类名获取对应的Class对象。

2. 获取类的信息
   - `getName()`: 获取类的完全限定名。
   - `getSuperclass()`: 获取父类的Class对象。
   - `getInterfaces()`: 获取实现的接口的Class对象数组。
   - `getModifiers()`: 获取类的修饰符，返回一个代表修饰符的整数。

3. 创建对象
   - `newInstance()`: 使用默认构造函数创建对象，要求类必须具有公共的默认构造函数。

4. 获取构造函数
   - `getConstructors()`: 获取类的所有公共构造函数。
   - `getDeclaredConstructors()`: 获取类的所有构造函数，包括公共、私有等。

5. 获取字段
   - `getField(String fieldName)`: 获取指定名称的公共字段。
   - `getFields()`: 获取所有公共字段。
   - `getDeclaredField(String fieldName)`: 获取指定名称的字段，包括私有字段。
   - `getDeclaredFields()`: 获取所有字段，包括私有字段。

6. 获取方法
   - `getMethod(String methodName, Class<?>... parameterTypes)`: 获取指定名称和参数类型的公共方法。
   - `getMethods()`: 获取所有公共方法。
   - `getDeclaredMethod(String methodName, Class<?>... parameterTypes)`: 获取指定名称和参数类型的方法，包括私有方法。
   - `getDeclaredMethods()`: 获取所有方法，包括私有方法。

7. 调用方法
   - `invoke(Object obj, Object... args)`: 调用指定对象上的方法，并传递相应的参数。

8. 获取和设置字段的值
   - `get(Object obj)`: 获取指定对象上字段的值。
   - `set(Object obj, Object value)`: 设置指定对象上字段的值。

9. 获取类上的注解：
   - `getAnnotation(Class<T> annotationClass)`: 获取指定类型的注解。
   - `getAnnotations()`: 获取类上所有的注解。

10. 获取字段上的注解：
   - `getField(String fieldName)`: 获取指定名称的公共字段。
   - `getDeclaredField(String fieldName)`: 获取指定名称的字段，包括私有字段。
   - 通过上述方法获取字段对象后，可以使用以下方法获取注解：
     - `getAnnotation(Class<T> annotationClass)`: 获取指定类型的注解。
     - `getAnnotations()`: 获取字段上所有的注解。

11. 获取方法上的注解：
    - `getMethod(String methodName, Class<?>... parameterTypes)`: 获取指定名称和参数类型的公共方法。
    - `getDeclaredMethod(String methodName, Class<?>... parameterTypes)`: 获取指定名称和参数类型的方法，包括私有方法。
    - 通过上述方法获取方法对象后，可以使用以下方法获取注解：
      - `getAnnotation(Class<T> annotationClass)`: 获取指定类型的注解。
      - `getAnnotations()`: 获取方法上所有的注解。

    需要注意的是，上述方法返回的注解是在运行时动态获取的，并且需要通过注解类型的Class对象进行参数化，以指定获取哪种类型的注解。例如，`getAnnotation(MyAnnotation.class)` 将返回类型为 `MyAnnotation` 的注解实例。

    同时，还可以使用以下方法来判断是否存在指定的注解：
    - `isAnnotationPresent(Class<? extends Annotation> annotationClass)`: 判断是否存在指定类型的注解。

    通过获取注解，我们可以在运行时获取类、字段或方法上的元数据信息，从而实现一些基于注解的动态逻辑。

<u>**注意：**需要注意的是，反射操作可能会降低性能，并且在使用时要小心处理异常。另外，访问私有成员时需要通过`setAccessible(true)`来解除访问限制。</u>

这些方法只是Java反射机制中的一部分，提供了基本的功能。通过反射，我们可以更灵活地操作类和对象，实现一些动态和通用的代码逻辑。

> 现在我们需要实现一个功能要求如下：
>
> 1：一个静态方法
> 2：随意传进去一个对象，不论是集合还是单个对象
> 3：解析出对象的属性
> 4：将拥有自定注解属性放在一个 map 集合中，如果传入的是对象集合并根据注解的 value为 2 的字段进行排序

```java
public class Code_02_Practice {
    public static List<List<FieldInfo>> practiceUtils(Class tClass, Object object) {

        List<List<FieldInfo>> result = new ArrayList<>();
        //判断是否是集合类型
        boolean isCollection = object instanceof Collection;
//      判断是对象是否是集合类型
        if (isCollection) {
//            强转成集合进行操作
            try {
                Collection<?> collection = (Collection<?>) object;
                Iterator<?> iterator = collection.iterator();
//                循环比哪里集合
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    List<FieldInfo> fieldAnno = new ArrayList<>();
                    getFieldInfo(tClass, next, fieldAnno);
                    if (!fieldAnno.isEmpty()) {
                        result.add(fieldAnno);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {

            List<FieldInfo> fieldAnno = new ArrayList<>();
            try {
                getFieldInfo(tClass, object, fieldAnno);
                result.add(fieldAnno);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
//        根据value 为2的字段进行排序
        List<List<FieldInfo>> collect = result.stream().sorted((fieldInfoList,fieldInfoList2) -> {
            Integer orderField1=0;
            Integer orderField2=0;
            for (FieldInfo fieldInfo : fieldInfoList) {
                if (fieldInfo.getFieldAnnotationValue().equals("2")) {
                    orderField1=Integer.valueOf(fieldInfo.getFieldValue());
                }
            }
            for (FieldInfo fieldInfo : fieldInfoList2) {
                if (fieldInfo.getFieldAnnotationValue().equals("2")) {
                    orderField2=Integer.valueOf(fieldInfo.getFieldValue());
                }
            }
            return Integer.compare(orderField1,orderField2);
        }).collect(Collectors.toList());
//        返回排序结果
        return collect;
    }

    /**
     *
     *
     * @param tClass 解析对象类的class
     * @param object 需要解析的对象
     * @param fieldAnno 最终存放的字段信息结果
     * @throws IllegalAccessException
     */
    public static void getFieldInfo(Class tClass, Object object, List<FieldInfo> fieldAnno) throws IllegalAccessException {
        //       获取class的所有字段
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            MyAnnotation annotation = field.getAnnotation(MyAnnotation.class);
//           如果该字段拥有自定义注解并且 value = 2
            if (null != annotation) {
                FieldInfo fieldInfo = new FieldInfo();
                // 获取字段的值
                field.setAccessible(true);
                Map<String, Object> map = new HashMap<>();
                fieldInfo.setFieldName(field.getName());
                fieldInfo.setFieldValue(String.valueOf(field.get(object)));
                fieldInfo.setFieldAnnotationKey(annotation.key());
                fieldInfo.setFieldAnnotationValue(String.valueOf(annotation.value()));
                fieldAnno.add(fieldInfo);
            }
        }
    }

    public static void main(String[] args) {
        List<Person> personList = new ArrayList<>();
        personList.add(Person.builder().name("whn").age(18).build());
        personList.add(Person.builder().name("lxz").age(19).build());
        personList.add(Person.builder().name("wb").age(20).build());
        personList.stream().sorted(Comparator.comparing(Person::getAge)).forEach(person -> {
            System.out.println(person);
        });
        System.out.println("");
        List<List<FieldInfo>> lists = practiceUtils(Person.class, personList);
        for (List<FieldInfo> list : lists) {
            list.forEach(System.out::print);
            System.out.println("");
        }
        System.out.println("dog class");
        List<Dog> dogList = new ArrayList<>();
        dogList.add(Dog.builder().breed("杜宾").name("大黄").age(1).build());
        dogList.add(Dog.builder().breed("雪纳瑞").name("大黑").age(3).build());
        dogList.add(Dog.builder().breed("哈士奇").name("黄豆").age(2).build());
        dogList.add(Dog.builder().breed("柯基").name("雪碧").age(1).build());
        List<List<FieldInfo>> lists1 = practiceUtils(Dog.class, dogList);
        for (List<FieldInfo> list : lists1) {
            list.forEach(System.out::print);
            System.out.println("");
        }
    }
}
```

运行结果

```cmd
Person(name=whn, age=18)
Person(name=lxz, age=19)
Person(name=wb, age=20)

FieldInfo(fieldName=name, fieldValue=whn, fieldAnnotationKey=name, fieldAnnotationValue=1)FieldInfo(fieldName=age, fieldValue=18, fieldAnnotationKey=age, fieldAnnotationValue=2)
FieldInfo(fieldName=name, fieldValue=lxz, fieldAnnotationKey=name, fieldAnnotationValue=1)FieldInfo(fieldName=age, fieldValue=19, fieldAnnotationKey=age, fieldAnnotationValue=2)
FieldInfo(fieldName=name, fieldValue=wb, fieldAnnotationKey=name, fieldAnnotationValue=1)FieldInfo(fieldName=age, fieldValue=20, fieldAnnotationKey=age, fieldAnnotationValue=2)
dog class
FieldInfo(fieldName=name, fieldValue=大黄, fieldAnnotationKey=name, fieldAnnotationValue=1)FieldInfo(fieldName=age, fieldValue=1, fieldAnnotationKey=age, fieldAnnotationValue=2)
FieldInfo(fieldName=name, fieldValue=雪碧, fieldAnnotationKey=name, fieldAnnotationValue=1)FieldInfo(fieldName=age, fieldValue=1, fieldAnnotationKey=age, fieldAnnotationValue=2)
FieldInfo(fieldName=name, fieldValue=黄豆, fieldAnnotationKey=name, fieldAnnotationValue=1)FieldInfo(fieldName=age, fieldValue=2, fieldAnnotationKey=age, fieldAnnotationValue=2)
FieldInfo(fieldName=name, fieldValue=大黑, fieldAnnotationKey=name, fieldAnnotationValue=1)FieldInfo(fieldName=age, fieldValue=3, fieldAnnotationKey=age, fieldAnnotationValue=2)
```

## 反射小案例2

在第一个案例中我们通过反射获取类的字段，子字段上面的注解，现在我们使用反射创建对象，和调用方法，完成最后使用学习

```java
public class Code_03_Practice {
    public static void main(String[] args) {

        System.out.println("获取类的方法方法");
//      获取使用类的方法需要知道类的名字和参数
        String methodName = "sayHello";
        Person person = new Person();
        person.setAge(14);
        person.setName("whn");
        try {
            Method method = Person.class.getDeclaredMethod(methodName);
//            设置可以访问
            method.setAccessible(true);
            method.invoke(person);
        } catch (Exception e) {
            System.out.println(e);
        }


        System.out.println("反射创建对象");
        try {
//            创建有参构造器将参数类型放进去，注意顺序要对，创建无参的话，可以不用传参
            Constructor<Dog> declaredConstructor = Dog.class.getDeclaredConstructor(String.class,Integer.class,String.class);
            declaredConstructor.setAccessible(true);
            Dog dog = declaredConstructor.newInstance("王富贵", 12, "藏獒");
            System.out.println(dog);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

运行结果

```
获取类的方法方法
Hello, I'm whn,14years old now!
反射创建对象
Dog{name='王富贵', age=12, breed='藏獒'}
```

