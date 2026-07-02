# AbstractProcessor（注解处理器）

> `AbstractProcessor` 是 Java 编译器提供的一个抽象类，它是创建 **注解处理器（Annotation Processor）** 的基础。

## 简介

### **📘 通俗解释：**

在 Java 编译时，`AbstractProcessor` 可以用来：

- 找出代码中使用了某些注解的类、方法、字段
- 分析它们的结构
- 甚至在**编译阶段生成额外代码**，例如自动生成 getter、setter、toString 等（这就是像 Lombok 那样的功能）

### 常见用途：

| 场景         | 用法示例                                             |
| ------------ | ---------------------------------------------------- |
| 自动生成代码 | 自动生成 DTO、Mapper、Builder 类等                   |
| 编译期校验   | 比如 @Autowired 的类是否被正确注入                   |
| 框架扩展     | Dagger、Room、Lombok、ButterKnife 等都用到注解处理器 |

### 🧩 AbstractProcessor 核心结构：

- `init(ProcessingEnvironment)`：初始化工具环境（例如获取 Messager、Filer）
- `getSupportedAnnotationTypes()`：声明你处理哪些注解（可用注解方式简化）
- `process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)`：每一轮注解扫描的主处理逻辑

## 案例演示

接下来 我们有一个小案例，就是通过注解在编译的时候自动添加实体类的`ToString`方法。

![image-20250711142015710](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20250711142015710.png)

### 项目目录

```shell
── auto-to-string-processor
│   ├── pom.xml
│   └── src
│      └── main
│          ├── java
│          │   └── com
│          │       └── whn
│          │           └── processor
│          │               ├── AutoToString.java # 自定的注解
│          │               └── ToStringProcessor.java # 主要的实现方法
│          └── resources
└── example-app
    ├── pom.xml
    └── src
       └── main
           ├── java
           │   └── com
           │       └── whn
           │           ├── App.java #测试的启动程序
           │           └── User.java #测试的实体类
           └── resources
```

### 依赖

`auto-to-string-processor`

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>

<dependencies>
    <dependency>
        <groupId>com.google.auto.service</groupId>
        <artifactId>auto-service</artifactId>
        <version>1.1.1</version>
        <scope>provided</scope> <!-- 编译时使用，不打包进最终应用 -->
    </dependency>
</dependencies>
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>17</source>
                <target>17</target>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <!-- 设置注解处理器的依赖路径，让 auto-service 生效 -->
                <annotationProcessorPaths>
                    <path>
                        <groupId>com.google.auto.service</groupId>
                        <artifactId>auto-service</artifactId>
                        <version>1.1.1</version>
                    </path>
                </annotationProcessorPaths>
                <compilerArgs>
                  <!--导入 javac 工具包-->
                    <arg>--add-exports</arg>
                    <arg>jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED</arg>
                    <arg>--add-exports</arg>
                    <arg>jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED</arg>
                    <arg>--add-exports</arg>
                    <arg>jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED</arg>
                    <arg>--add-exports</arg>
                    <arg>jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED</arg>
                    <arg>--add-exports</arg>
                    <arg>jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED</arg>
                </compilerArgs>
            </configuration>
        </plugin>
    </plugins>
</build>
```

`example-app`

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>

<dependencies>
  <!--导入 自动编译的工具包-->
    <dependency>
        <groupId>com.whn</groupId>
        <artifactId>auto-to-string-processor</artifactId>
        <version>1.0-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>17</source>
                <target>17</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### Java类

#### AutoToString自定义注解

```java
/**
 * @author whn
 * @time 2025/7/10
 * @description toString注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AutoToString {
}

```

#### AbstractProcessor 实现类（主要）

```java
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.whn.processor.AutoToString")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ToStringProcessor extends AbstractProcessor {

    private JavacTrees trees;      // 提供 AST 抽象语法树的访问入口
    private TreeMaker treeMaker;   // 构造新的 AST 节点的工具类
    private Names names;           // 创建标识符（变量名、方法名等）

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        ProcessingEnvironment realEnv = jbUnwrap(ProcessingEnvironment.class, processingEnv);
        this.trees = JavacTrees.instance(realEnv);
        this.treeMaker = TreeMaker.instance(((JavacProcessingEnvironment) realEnv).getContext());
        this.names = Names.instance(((JavacProcessingEnvironment) realEnv).getContext());
    }

    @SuppressWarnings("unchecked")
    private static <T> T jbUnwrap(Class<? extends T> iface, T wrapper) {
        T unwrapped = null;
        try {
            final Class<?> apiWrappers = wrapper.getClass().getClassLoader().loadClass("org.jetbrains.jps.javac.APIWrappers");
            final java.lang.reflect.Method unwrapMethod = apiWrappers.getDeclaredMethod("unwrap", Class.class, Object.class);
            unwrapped = iface.cast(unwrapMethod.invoke(null, iface, wrapper));
        } catch (Throwable ignored) {}
        return unwrapped != null ? unwrapped : wrapper;
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(AutoToString.class)) {
            if (!(element instanceof TypeElement)) continue;

            // 获取语法树表示的类定义（ClassDecl）
            JCTree.JCClassDecl classDecl = (JCTree.JCClassDecl) trees.getTree(element);

            java.util.List<VariableElement> fields = element.getEnclosedElements().stream()
                    .filter(e -> e instanceof VariableElement)
                    .map(e -> (VariableElement) e)
                    .toList();

            // 初始字符串，例如：User [
            JCTree.JCExpression expr = treeMaker.Literal(element.getSimpleName() + " [");

            for (int i = 0; i < fields.size(); i++) {
                VariableElement field = fields.get(i);
                String fieldName = field.getSimpleName().toString();

                // 拼接字段名，例如：+ "name="
                expr = treeMaker.Binary(JCTree.Tag.PLUS, expr, treeMaker.Literal(fieldName + "="));

                // 拼接字段值，例如：+ name
                expr = treeMaker.Binary(JCTree.Tag.PLUS, expr, treeMaker.Ident(names.fromString(fieldName)));

                if (i < fields.size() - 1) {
                    // 拼接逗号，例如：+ ", "
                    expr = treeMaker.Binary(JCTree.Tag.PLUS, expr, treeMaker.Literal(", "));
                }
            }

// 末尾补上 "]"
            expr = treeMaker.Binary(JCTree.Tag.PLUS, expr, treeMaker.Literal("]"));

// 构造 return 语句
            JCTree.JCStatement returnStatement = treeMaker.Return(expr);

            // 创建方法体 block {...}
            JCTree.JCBlock body = treeMaker.Block(0, List.of(returnStatement));
            // 创建方法声明 public String toString() { ... }
            JCTree.JCMethodDecl method = treeMaker.MethodDef(
                    treeMaker.Modifiers(Flags.PUBLIC),
                    names.fromString("toString"),
                    treeMaker.Ident(names.fromString("String")),
                    List.nil(), List.nil(), List.nil(),
                    body, null
            );

            // 将生成的方法插入类中
            classDecl.defs = classDecl.defs.append(method);
        }
        return true;
    }
}
```

#### 测试实体类

```java
@AutoToString
public class User {
    public String name;
    public int age;
}
```

#### 测试类

```java
public class App {
    public static void main(String[] args) {
        User user = new User();
        user.name = "Alice";
        user.age = 30;
        // toString 方法是处理器生成的类中实现的
        System.out.println(user);
    }
}
```

![image-20250711143604308](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20250711143604308.png)

## `AbstractProcessor`调试

> `AbstractProcessor` 是在编译的时触发的，所以平时的`debug` 不会生效。需要连接 java 虚拟机在编译的时候进行调试

参考博客： https://www.cnblogs.com/flyingskya/p/10970350.html

### 创建远程Debug调试连接

![image-20250711144025328](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20250711144025328.png)

1. Debug 名称（无所谓随便起）
2. Attach to remote JVM （远程 JVM 启动 默认就好）
3. Port （端口，随便给一个不占用的）

### 配置JVM编译参数调整 idea 配置

1. idea 按住 ctrl+shift+A弹出面板

2. 输入`EditCustom VM Opitions`

3. 回车，就能看到下图后面的页面

4. 输入参数(**注意端口和上面配置的端口保持一致**)

   ```properties
   -Dcompiler.process.debug.port=5006
   ```

![image-20250711144312321](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20250711144312321.png)

5. idea 按住 ctrl+shift+A弹出面板

6. 输入`Debug Build Process`

7. 开启配置如下图所示

   ![image-20250711145248834](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20250711145248834.png)

### Build Project 并进行断点调试

1. Build Project

   ![image-20250711145456418](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20250711145456418.png)

   > 
   >
   > ![image-20250711145708319](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20250711145708319.png)由于开启了 远程 JVM 启动的调试，所以会需要进行连接才会进行编译，所以这里不需要担心，编译过快没有连接导致错过，调试的断点

2. 启动远程调试连接

   ![image-20250711145928712](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20250711145928712.png)

   这样就可以看到了

一个很牛批的代码可以研究一下！！！

```java
@SupportedAnnotationTypes(“org.springframework.cloud.openfeign.FeignClient”)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class FeignClientProcessor extends AbstractProcessor {

    final static String WEB_ANNOTATION_PREFIX = “org.springframework.web.bind.annotation”;

    final static String WEB_ANNOTATION_REQUEST_MAPPING = “org.springframework.web.bind.annotation.RequestMapping”;

    final static java.util.List<String> REQUEST_MAPPINGS = Arrays.asList(
            “org.springframework.web.bind.annotation.RequestMapping”,
            “org.springframework.web.bind.annotation.GetMapping”,
            “org.springframework.web.bind.annotation.PutMapping”,
            “org.springframework.web.bind.annotation.PostMapping”,
            “org.springframework.web.bind.annotation.PatchMapping”,
            “org.springframework.web.bind.annotation.DeleteMapping”
            );

    final static String WEB_ANNOTATION_REQUEST_BODY = “org.springframework.web.bind.annotation.ResponseBody”;

    final static Set<String> BASE_TYPE_STRING = new HashSet<>();

    final static Set<String> SERVLET_TYPE_OBJECT = new HashSet<>();

    static {
        BASE_TYPE_STRING.add(String.class.getName());
        BASE_TYPE_STRING.add(Integer.class.getName());
        BASE_TYPE_STRING.add(BigInteger.class.getName());
        BASE_TYPE_STRING.add(BigDecimal.class.getName());
        BASE_TYPE_STRING.add(Number.class.getName());
        BASE_TYPE_STRING.add(Boolean.class.getName());
        BASE_TYPE_STRING.add(Double.class.getName());
        BASE_TYPE_STRING.add(Short.class.getName());
        BASE_TYPE_STRING.add(Float.class.getName());
        BASE_TYPE_STRING.add(Long.class.getName());
        BASE_TYPE_STRING.add(Date.class.getName());
        BASE_TYPE_STRING.add(Timestamp.class.getName());
        BASE_TYPE_STRING.add(java.sql.Date.class.getName());
        BASE_TYPE_STRING.add(java.security.Timestamp.class.getName());
        BASE_TYPE_STRING.add(char.class.getName());
        BASE_TYPE_STRING.add(boolean.class.getName());
        BASE_TYPE_STRING.add(short.class.getName());
        BASE_TYPE_STRING.add(float.class.getName());
        BASE_TYPE_STRING.add(double.class.getName());
        BASE_TYPE_STRING.add(int.class.getName());
        BASE_TYPE_STRING.add(long.class.getName());
    }

    static {
        SERVLET_TYPE_OBJECT.add(“javax.servlet.http.HttpServletRequest”);
        SERVLET_TYPE_OBJECT.add(“javax.servlet.http.HttpServletResponse”);
    }

    Messager messager;
    JavacTrees javacTrees;
    TreeMaker treeMaker;
    Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.javacTrees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> eles = roundEnv.getElementsAnnotatedWith(FeignClient.class);
        eles.forEach(ele -> {
            // 输出类名
            this.messager.printMessage(Diagnostic.Kind.NOTE, ele.getSimpleName(), ele);
            // 处理方法加上responseBody
            JCTree jcTree = javacTrees.getTree(ele);
            jcTree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    try {
                        // 在类上添加requestmapping，路劲为feginclient的path
                        FeignClient feignClient = ele.getAnnotation(FeignClient.class);
                        // 类加注解
                        if (!feignClient.path().isEmpty()) {
                            treeMaker.pos = jcClassDecl.pos;
                            if (!classContainRequestMappingAnnotation(jcClassDecl, WEB_ANNOTATION_REQUEST_MAPPING)) {
                                JCTree.JCAnnotation jcAnnotation = treeMaker.Annotation(
                                        processComponent(WEB_ANNOTATION_REQUEST_MAPPING),
                                        List.of(treeMaker.Assign(treeMaker.Ident(names.fromString(“value”)), treeMaker.Literal(feignClient.path())))
                                );
                                jcClassDecl.mods.annotations = jcClassDecl.mods.annotations.append(jcAnnotation);
                            }
                        }
                        // 处理feignclient的path
                        for (JCTree.JCAnnotation annotation : jcClassDecl.mods.annotations) {
                            if (FeignClient.class.getName().equals(annotation.type.toString())) {
                                annotation.accept(new TreeTranslator(){
                                    @Override
                                    public void visitAnnotation(JCTree.JCAnnotation jcAnnotation) {
                                        // 处理args
                                        List<JCTree.JCExpression> args = List.nil();
                                        for (JCTree.JCExpression arg : jcAnnotation.args) {
                                            JCTree.JCAssign jcAssign = (JCTree.JCAssign) arg;
                                            if (!”path”.equals(jcAssign.lhs.toString())) {
                                                args = args.append(arg);
                                            }
                                        }
                                        jcAnnotation.args = args;
                                        // 处理attribute
                                        List<Pair<Symbol.MethodSymbol, Attribute>> values = List.nil();
                                        for (Pair<Symbol.MethodSymbol, Attribute> value : jcAnnotation.attribute.values) {
                                            if (!”path”.equals(value.fst.name.toString())) {
                                                values = values.append(value);
                                            }
                                        }
                                        jcAnnotation.attribute  = new Attribute.Compound(jcAnnotation.attribute.type, values);
                                        super.visitAnnotation(jcAnnotation);
                                    }
                                });
                                break;
                            }
                        }
                        // 处理方法
                        for (JCTree tree : jcClassDecl.getMembers()) {
                            // 如果当前成员是方法，则处理
                            if (Tree.Kind.METHOD.equals(tree.getKind())) {
                                processMethod((JCTree.JCMethodDecl) tree);
                            }
                        }
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        throw e;
                    }
                    super.visitClassDef(jcClassDecl);
                }
            });
        });
        return true;
    }

    /**
     * 处理方法
     *
     * @param jcMethodDecl
     */
    void processMethod(JCTree.JCMethodDecl jcMethodDecl) {
        treeMaker.pos = jcMethodDecl.pos;
        // 如果方法没有requestmapping注解，则自动添加
        if (!methodContainRequestMappingAnnotation(jcMethodDecl)) {
            JCTree.JCAnnotation jcAnnotationReqMap = treeMaker.Annotation(
                    processComponent(WEB_ANNOTATION_REQUEST_MAPPING),
                    List.of(treeMaker.Assign(
                            treeMaker.Ident(names.fromString(“value”)),
                            treeMaker.Literal(“/“ + jcMethodDecl.name.toString())))
            );
            jcMethodDecl.mods.annotations = jcMethodDecl.mods.annotations.append(jcAnnotationReqMap);
        }
        if (!methodContainRequestMappingAnnotation(jcMethodDecl)) {
            // 给方法上加ResponseBody注解
            JCTree.JCAnnotation jcAnnotation = treeMaker.Annotation(
                    processComponent(WEB_ANNOTATION_REQUEST_BODY),
                    List.nil()
            );
            jcMethodDecl.mods.annotations = jcMethodDecl.mods.annotations.append(jcAnnotation);
        }
        // 获取输入参数
        List<JCTree.JCVariableDecl> args = jcMethodDecl.params;
        if (!args.isEmpty()) {
            int i = 0;
            for (JCTree.JCVariableDecl jcVariableDecl : args) {
                if (argContainWebAnnotation(jcVariableDecl)) {
                    continue;
                }
                // 追加requestparam或者requestBody
                if (processArgsAnnotation(jcVariableDecl)) {
                    i++;
                }
                if (i > 1) {
                    throw new IllegalStateException(String.format(“Method %s has multiple entity parameters”, jcMethodDecl.name.toString()));
                }
            }
        }
    }

    /**
     * 处理参数的注解
     *
     * @param jcVariableDecl
     * @return
     */
    boolean processArgsAnnotation(JCTree.JCVariableDecl jcVariableDecl) {
        boolean isResponseBody = false;
        treeMaker.pos = jcVariableDecl.pos;
        // 判断类型怎样取值
        String type = jcVariableDecl.vartype.type.toString();
        JCTree.JCAnnotation jcAnnotation;
        if (BASE_TYPE_STRING.contains(type)) {
            jcAnnotation = treeMaker.Annotation(
                    processComponent(“org.springframework.web.bind.annotation.RequestParam”),
                    List.of(
                            treeMaker.Assign(
                                    treeMaker.Ident(names.fromString(“value”)),
                                    treeMaker.Literal(jcVariableDecl.name.toString())
                            ),
                            treeMaker.Assign(
                                    treeMaker.Ident(names.fromString(“required”)),
                                    treeMaker.Literal(false)
                            )
                    ));
            jcVariableDecl.mods.annotations = jcVariableDecl.mods.annotations.append(jcAnnotation);
        } else if(!SERVLET_TYPE_OBJECT.contains(type)) {
            jcAnnotation = treeMaker.Annotation(processComponent(“org.springframework.web.bind.annotation.RequestBody”), List.nil());
            jcVariableDecl.mods.annotations = jcVariableDecl.mods.annotations.append(jcAnnotation);
            isResponseBody = true;
        }
        return isResponseBody;
    }

    /**
     * 判断是不是springweb的参数注解
     *
     * @param jcVariableDecl
     * @return
     */
    boolean argContainWebAnnotation(JCTree.JCVariableDecl jcVariableDecl) {
        List<JCTree.JCAnnotation> jcAnnotations = jcVariableDecl.mods.annotations;
        if (!jcAnnotations.isEmpty()) {
            for (JCTree.JCAnnotation jcAnnotation : jcAnnotations) {
                if (jcAnnotation.type.toString().startsWith(WEB_ANNOTATION_PREFIX)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断方法有没有该注解
     *
     * @param jcMethodDecl
     * @return
     */
    boolean methodContainRequestMappingAnnotation(JCTree.JCMethodDecl jcMethodDecl) {
        List<JCTree.JCAnnotation> jcAnnotations = jcMethodDecl.mods.annotations;
        if (!jcAnnotations.isEmpty()) {
            for (JCTree.JCAnnotation jcAnnotation : jcAnnotations) {
                if(jcAnnotation.getAnnotationType() instanceof JCTree.JCIdent){
                    JCTree.JCIdent jcIdent = (JCTree.JCIdent) jcAnnotation.getAnnotationType();
                    if (REQUEST_MAPPINGS.contains(jcIdent.sym.toString())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断类有没有该注解
     *
     * @param jcClassDecl
     * @param annotationString
     * @return
     */
    boolean classContainRequestMappingAnnotation(JCTree.JCClassDecl jcClassDecl, String annotationString) {
        List<JCTree.JCAnnotation> jcAnnotations = jcClassDecl.mods.annotations;
        if (!jcAnnotations.isEmpty()) {
            for (JCTree.JCAnnotation jcAnnotation : jcAnnotations) {
                if(jcAnnotation.getAnnotationType() instanceof JCTree.JCIdent){
                    JCTree.JCIdent jcIdent = (JCTree.JCIdent) jcAnnotation.getAnnotationType();
                    if (annotationString.equals(jcIdent.sym.toString())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 处理组件
     *
     * @param components
     * @return
     */
    JCTree.JCExpression processComponent(String components) {
        String[] compArr = components.split(“\\.”);
        JCTree.JCExpression jcExpression = treeMaker.Ident(names.fromString(compArr[0]));
        for (int i = 1; i < compArr.length; i++) {
            jcExpression = treeMaker.Select(jcExpression, names.fromString(compArr[i]));
        }
        return jcExpression;
    }
}
```

