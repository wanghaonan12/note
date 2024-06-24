# Gradle安装

**gradle 项目中如果存在`gradle`文件可以不用安装 gradle 。这是项目中自带的 `gradle`。并指定了 `gradle` 的版本信息，防止版本不兼容问题**

## 版本选则

SpringBoot 官方指出的 Gradle 插件需要 **gradle6.8** 版本及以上，且 Idea 与 Gradle 也有兼容问题。Idea版本不宜用过老的版本。可以在 Idea 安装目录下`/plugins/gradle/lib`找到对应的 Gradle 最适应的版本。

![image-20240613142155125](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240613142155125.png)

[Gradle 官方下载地址](https://gradle.org/releases/?_gl=1*lc4mmy*_ga*MTAyODgwNzgzNS4xNzE3NTU4MDY1*_ga_7W7NC6YNPT*MTcxODI1OTc3Ni43LjEuMTcxODI1OTc4NS41MS4wLjA.)

## 环境变量配置

![image-20240613142618321](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240613142618321.png)

`GRADLE_HOME`:Gradle 的根目录

`GRADLE_USER_HOME`: 指定的仓库地址。

![image-20240613142908409](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240613142908409.png)

**安装认证**

```bash
gradle -version
```

![image-20240613143511386](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240613143511386.png)

**配置国内镜像**

将一下内容放在Gradle安装目录下的	`\init.d\init.gradle`文件中

```json
allprojects {
    repositories { 
        mavenLocal() 
        maven { name "Alibaba" ; url "https://maven.aliyun.com/repository/public" } 
        maven { name "Bstek" ; url "https://nexus.bsdn.org/content/groups/public/" } 
        mavenCentral()
        }

    buildscript {
        repositories { 
            maven { name "Alibaba" ; url 'https://maven.aliyun.com/repository/public' } 
            maven { name "Bstek" ; url 'https://nexus.bsdn.org/content/groups/public/' } 
            maven { name "M2" ; url 'https://plugins.gradle.org/m2/' }
        }
    }
}
```

- `repositories`:项目依赖镜像地址 
  - `mavenLocal() `：本地 maven 仓库
  - `maven`：自定义镜像仓库
  - `mavenCentral()`：maven中央仓库
- `buildscript`：构建脚本依赖镜像地址

# Gradle 项目

![image-20240613145801559](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240613145801559.png)

在 Gradle 项目中 `gradle` , `gradlew` , `gradle.bat` 这几个文件是非必要文件，在创建项目的时候会生成指定版本的 `Gradle `环境 ，其中在 `/gradle/wrapper/gradle-wrapper.properties` 中有该项目的 `Gradle` 的版本信息，这也正是没有`Gradle`环境也可以使用`Gradle`构建项目的原因。

## HelloWord创建

![image-20240613152352956](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240613152352956.png)

---

## `build.gradle` 和 `setting.gradle`文件内容讲解

`Gradle` 使用 `Groovy` 语法，其与`Java`大相径庭,如有兴趣可以阅览

[Groovy 教程_w3cschool](https://www.w3cschool.cn/groovy/)

[Groovy 中文文档 (juzi214032.github.io)](https://juzi214032.github.io/groovy-chinese/)

[Apache Groovy ](https://www.groovy-lang.org/)

工程创建完成后看一下`build.gradle` 和 `setting.gradle`

![image-20240613154143375](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240613154143375.png)

`Maven` 中项目的 `GAV` 分别对应成 `Gradle`中的 `GNV`

| Maven                                   | Gradle                           |
| --------------------------------------- | -------------------------------- |
| <groupId>com.pde.pdes</groupId>         | group = 'com.whn'                |
| <artifactId>pdes-p10-build</artifactId> | rootProject.name = 'gradle_code' |
| <version>10.1.0-RELEASE</version>       | version = '1.0-SNAPSHOT'         |
| <plugins>...</plugins>                  | plugins{}                        |
| <dependencies> ... </dependencies>      | dependencies{}                   |

## `gradle-wrapper.properties` 文件内容讲解

![image-20240613155725122](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240613155725122.png)

| 字段名           | 说明                                               |
| ---------------- | -------------------------------------------------- |
| distributionBase | 下载的Gradle压缩包解压后存储的主目录               |
| distributionPath | 相对于distributionBase的解压后的Gradle压缩包的路径 |
| zipStoreBase     | 同distributionBase,只不过是存放zip压缩包的         |
| zipStorePath     | 同distributionPath,只不过是存放zip压缩包的         |
| distributionUrl  | Gradle压缩包的下载地址                             |

指定的`Gradle` 版本的下载和解压位置

![image-20240613160712034](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240613160712034.png)

## SpringBoot项目演示

### `build.gradle` 文件

```json
// 添加插件
plugins {
    // 添加Spring Boot 插件
    id 'org.springframework.boot' version '2.6.13'
    // 添加 spring Boot 的依赖管理插件
    id 'io.spring.dependency-management' version '1.0.15.RELEASE'
    id 'java'
}

group = 'com.whn'
version = '0.0.1-SNAPSHOT'

// 添加一个配置 定义 annotationProcessor 添加的依赖 都属于  compileOnly
configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

// 设置仓库
repositories {
    mavenCentral()
}
// 扩展相当于 <properties>
ext {
    // 设置一个版本号 等于 maven 中 <springCloudVersion>2021.0.5</springCloudVersion>
    set('springCloudVersion', "2021.0.5")
}

dependencies {
//    正常的项目依赖
//    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-web'

// 编译依赖
    compileOnly 'org.projectlombok:lombok'
// 注解依赖
    annotationProcessor 'org.projectlombok:lombok'
// 测试依赖
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'
}

// 这就不用多说了吧
dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}
```

| 依赖类型                        | 描述                                                         |
| ------------------------------- | ------------------------------------------------------------ |
| `implementation`                | 项目内部实现的一部分，但不暴露给使用此项目的其他项目。       |
| `api`                           | 不仅在当前项目中使用，还会传递给使用该项目的其他项目。       |
| `compileOnly`                   | 只在编译时可用，在运行时不会包含。                           |
| `runtimeOnly`                   | 只在运行时可用，在编译时不可用。                             |
| `testImplementation`            | 仅在测试代码中使用，不会包含在最终的生产代码中。             |
| `testRuntimeOnly`               | 仅在测试的运行时使用。                                       |
| `annotationProcessor`           | 用于注解处理器，在编译时会执行注解处理。                     |
| `integrationTestImplementation` | 仅在集成测试代码中使用，不会包含在最终的生产代码中。         |
| `integrationTestRuntimeOnly`    | 仅在集成测试的运行时使用。                                   |
| `project`                       | 指向同一多项目构建中的其他子项目。                           |
| `files`                         | 直接指向本地文件系统上的 jar 文件。                          |
| `platform`                      | 用于对一组依赖项进行管理，确保在所有项目中使用相同的版本。   |
| `enforcedPlatform`              | 类似于平台依赖项，但强制所有模块使用指定的版本，无法被其他版本替换。 |

### 启动文件`GradleLearnApplication`

```java
@SpringBootApplication
public class GradleLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(GradleLearnApplication.class, args);
    }

}
```

### PathVariableController

```java
@RestController
@RequestMapping
    public class PathVariableController {
    // http://127.0.0.1:8080/user/123/roles/222
    @GetMapping(value = "/user/{userId}/roles/{roleId}")
    public String getLogin(@PathVariable("userId") String userId, @PathVariable("roleId") String roleId) {
        return "User Id : " + userId + " Role Id : " + roleId;
    }
    // http://127.0.0.1:8080/javabeat/somewords
    @GetMapping(value = "/javabeat/{regexp1:[a-z-]+}")
    public String getRegExp(@PathVariable("regexp1") String regexp1) {
        return "URI Part : " + regexp1;
    }
}
```

> 项目启动 一点毛病没有！！！！

![image-20240613164905350](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240613164905350.png)

## SpringCloud项目演示

### **项目结构**

> 演示服务的依赖关系，除 `service_order` 其他工程均为空工程

```tex
├─gradle
├─microservice_bean
├─microservice_common
├─microservice_gateway
└─microservice_service
    ├─service_order
    └─service_user
```

### **根目录`build.gradle`**

```json
description 'Gradle 微服务实战父工程'

//构建 Gradle 脚本自身需要的资源，可以声明的资源包括依赖项、第三方插件、maven仓库地址等。
buildscript {
    ext {
        springBootVersion = '2.2.1.RELEASE'
        springCloudVersion = 'Hoxton.RELEASE'
        springCloudAlibabaVersion = '0.2.2.RELEASE'
    }
    //设置仓库
    repositories {
        maven { url 'https://maven.aliyun.com/nexus/content/groups/public/' }
        maven { url 'https://repo.spring.io/milestone' }
    }
// 使用 插件统一管理 SpringBoot的版本信息
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    }
}

//配置全局, 包括root项目和子项目
allprojects {
    // gav 中的 g 和 v
    group 'com.atguigu'
    version '1.0-SNAPSHOT'

    //配置编码格式
    tasks.withType(JavaCompile) {
        options.encoding = "UTF-8"
    }
    //设置仓库
    repositories {
        maven { url 'https://maven.aliyun.com/nexus/content/groups/public/' }
        maven { url 'https://repo.spring.io/milestone' }
    }
}


// 引入 gradle 脚本
apply from: 'version.gradle'

//配置所有子项目
subprojects {
    // 配置导入的插件
    apply plugin: 'java'
    apply plugin: 'java-library' //api
    apply plugin: 'io.spring.dependency-management'

    // 指定 Java 版本
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8


    //公用的依赖
    dependencies {
        testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'
        testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'
        implementation 'org.projectlombok:lombok:1.18.32'
    }

    // 使用 JUnit Platform 来运行测试
    test {
        useJUnitPlatform()
    }


    // dependencyManagement版本统一管理，类似于父maven的dependencyManagement
    dependencyManagement {
        dependencies {
            //  遍历导入的 version.gradle 中定义的依赖版本
            for (depJar in rootProject.ext.dependencies) {
                dependency depJar.value
            }
        }
        imports {
            mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
            mavenBom "org.springframework.cloud:spring-cloud-alibaba-dependencies:${springCloudAlibabaVersion}"
        }
    }
}


project(':microservice_bean') {
    description("微服务实战之bean层:存放表对应的实体类")
}

project(":microservice_common") {
    description("微服务实战之公共模块:存放微服务常用的工具类")
    //依赖
    dependencies {
        api 'com.alibaba:fastjson'
        api 'io.springfox:springfox-swagger2'
        api 'io.springfox:springfox-swagger-ui'
        api 'io.jsonwebtoken:jjwt'
}

project(":microservice_service") {
    description("微服务实战之服务模块：存放各个微服务模块")
    apply plugin: 'org.springframework.boot'

    subprojects {
        apply plugin: 'java-library'
        apply plugin: 'org.springframework.boot'

        dependencies {
            api 'org.springframework.boot:spring-boot-starter-web'
            api project(':microservice_bean')
            api project(':microservice_common')
        }
    }
}
```

> 子工程的配置都写在了根目录的配置文件里面，所以子项目的`build.gardle`中都是空的

### **根目录`setting.gradle`**

```json
// 父项目名称
rootProject.name = 'microservice'
// 子项目信息，
include 'microservice_bean'
include 'microservice_common'
include 'microservice_gateway'
include 'microservice_service'

// 预防空指针报错的异常处理 防止 service_user 不存在
findProject(':microservice_service:service_user')?.name = 'service_user'
include 'microservice_service:service_user'
findProject(':microservice_service:service_order')?.name = 'service_order'
include 'microservice_service:service_order'
```

### **根目录自定义文件`version.gradle`**

```json
// 依赖版本管理
ext {
    version = [
            "fastjsonVersion"   : "1.2.72",
            "mybatisPlus" : "3.0.5",
            "mysql" : "5.1.46",
            "swaggerVersion": "2.7.0",
            "jjwtVersion": "0.7.0"
    ]

    dependencies = [
            "fastjson"                      : "com.alibaba:fastjson:${version.fastjsonVersion}",
            "mybatis-plus-boot-starter"     : "com.baomidou:mybatis-plus-boot-starter:${version.mybatisPlus}",
            "mysql"                         : "mysql:mysql-connector-java:${version.mysql}",
            "swagger"                       : "io.springfox:springfox-swagger2:${version.swaggerVersion}",
            "swaggerUI"                     : "io.springfox:springfox-swagger-ui:${version.swaggerVersion}",
            "jjwt"                          : "io.jsonwebtoken:jjwt:${version.jjwtVersion}"
    ]
}
```

### `service_order` 服务demo

启动服务

```java
@SpringBootApplication
public class OrderMainStarterApp {
    public static void main(String[] args) {
            SpringApplication.run(OrderMainStarterApp.class,args);
    }
}
```

**controller**

```java
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    //http://localhost:7778/order/info/123
    @GetMapping("/info/{id}")
    public List<OrderInfo> getOrderList(@PathVariable("id") Integer id){
        return orderService.getUserInfo(id);
    }
}
```

**实体类**

```java
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo implements Serializable {
    private Integer oid;
    private Integer uid;
    private String productName;
}
```

**Service**

```java
@Service
public class OrderService {
    public List<OrderInfo> getUserInfo(Integer uid) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOid(1);
        orderInfo.setUid(uid);
        orderInfo.setProductName("phone");
        return Collections.singletonList(orderInfo);
    }
}
```

**yml**

```yml
server:
  port: 7778
```

**接口演示**

![image-20240614110607992](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240614110607992.png)

# 注意

> Idea 中配置的 Gradle 是控制台页面的配置 和 Terminal 里面使用的可能不是同一个版本的 Gradle。
>
> 如下配置中 控制台使用的是项目中 Gradle 的版本，在 Terminal 使用的是本地的 Gradle 的版本

![image-20240614110630716](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/img/image-20240614110630716.png)