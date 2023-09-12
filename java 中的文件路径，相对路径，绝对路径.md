# java 中的文件路径，相对路径，绝对路径

## 相对路径和绝对路径

### 绝对路径（Absolute Path）

绝对路径（Absolute Path）：绝对路径是从文件系统的根目录开始的完整路径。它指定了文件或目录在文件系统中的确切位置。在不同的操作系统上，根目录的表示方式可能不同。例如，在Windows系统上，绝对路径可以以盘符（如C:\）开始，而在Unix或Linux系统上，绝对路径以斜杠（/）开始。

示例：
- Windows 绝对路径：C:\Users\Username\Documents\file.txt
- Unix/Linux 绝对路径：/home/username/Documents/file.txt

使用绝对路径可以准确地指定文件或目录的位置，无论当前工作目录在哪里，都可以找到它们。

### 相对路径（Relative Path）

相对路径（Relative Path）：相对路径是相对于当前工作目录的路径。当前工作目录是指在执行Java程序时所在的目录。相对路径不提供完整的文件系统路径，而是根据当前工作目录确定文件或目录的位置。

示例：
- 当前工作目录：/home/username/Documents
- 相对路径：file.txt

相对路径基于当前工作目录，通过指定文件或目录与当前目录之间的相对位置来定位它们。相对路径更灵活，因为它们可以适应不同的工作环境和文件结构。

需要注意的是，相对路径也可以使用特殊符号来表示位置关系：
- `.`：表示当前目录。
- `..`：表示上级目录。

示例：
- 当前工作目录：/home/username/Documents
- 相对路径：../Images/image.jpg

上述示例中的相对路径`../Images/image.jpg`表示在当前目录的上一级目录下的Images目录中的image.jpg文件。

---

- 绝对路径是基于文件系统的完整路径，可以从根目录开始定位文件或目录。

- 相对路径是相对于当前工作目录的路径，根据当前目录和文件/目录之间的位置关系来定位。

选择使用绝对路径还是相对路径取决于你的需求和场景。如果你需要在不同的环境中移植代码或与其他系统进行交互，建议使用相对路径，因为它更具灵活性。如果你需要确保代码在特定的位置找到文件或目录，或者需要操作特定的文件系统位置，那么使用绝对路径会更合适。

## 默认文件路径

在Java中，当你创建一个文件而没有显式地指定路径时，它将默认存放在当前工作目录下。当前工作目录是指在执行Java程序时所在的目录。它可以是你启动程序的命令行或终端窗口所在的目录，或者是IDE（集成开发环境）中配置的工作目录。

你可以使用以下代码来获取当前工作目录的路径：

```java
String currentWorkingDirectory = System.getProperty("user.dir");
System.out.println("当前工作目录：" + currentWorkingDirectory);
```

当你创建一个文件时，如果没有指定完整的路径，Java将在当前工作目录下创建该文件。例如，如果当前工作目录是`/home/username/Documents`，而你只指定文件名为`file.txt`，那么Java将在`/home/username/Documents`目录下创建名为`file.txt`的文件。如果你希望在特定的目录中创建文件，可以通过指定路径来实现。例如：

```java
File file = new File("/path/to/directory/file.txt");
```

上述代码将创建一个名为`file.txt`的文件，它将位于`/path/to/directory/`目录下。

---

**注意：**路径中的斜杠方向在不同操作系统上可能会有所不同。在Windows上，路径使用反斜杠（\），而在Unix和Linux上，路径使用正斜杠（/）。

---

如果在创建文件时没有指定完整的路径，Java将默认将文件存放在当前工作目录下。你可以使用`System.getProperty("user.dir")`来获取当前工作目录的路径。如果你希望在特定的目录中创建文件，需要显式地指定完整的路径。

## `target`目录层级

当Java项目中包含静态资源文件时，在打包后的文件层级中，静态资源通常被放置在特定的目录下。以下是一个示例的目录图，展示了包含静态资源的项目的文件层级，并添加了注释说明：

```
根目录
├── bin/                 // 包含编译后的可执行文件（class文件）
│   └── com/
│       └── example/
│           └── YourClass.class
├── lib/                 // 包含项目所依赖的外部JAR文件
│   ├── library1.jar
│   └── library2.jar
├── resources/           // 包含项目的资源文件
│   ├── config.properties
│   └── static/
│       ├── styles.css   // 静态CSS文件
│       ├── script.js    // 静态JavaScript文件
│       └── images/
│           ├── logo.png // 图像文件
│           └── banner.jpg
└── yourproject.jar       // 可执行的JAR文件，打包了项目的类和资源文件
```

解释：

- `bin/`目录：包含编译后的可执行文件（class文件）。类文件按照包结构组织，例如`com/example/YourClass.class`。
- `lib/`目录：包含项目所依赖的外部JAR文件。这些JAR文件提供了项目所需的额外功能和库。
- `resources/`目录：包含项目的资源文件。
  - `config.properties`：示例配置文件。
  - `static/`目录：用于存放静态资源文件的目录。
    - `styles.css`：示例静态CSS文件。
    - `script.js`：示例静态JavaScript文件。
    - `images/`目录：包含图像文件的目录。
      - `logo.png`：示例图像文件。
      - `banner.jpg`：示例图像文件。
- `yourproject.jar`：可执行的JAR文件，打包了项目的类和资源文件。该文件包含了项目的所有依赖项，并且可以在Java运行环境中执行。

请注意，实际项目中的静态资源文件可能包括其他类型的文件，例如HTML文件、字体文件等，具体取决于项目的需求。示例目录图仅提供了一种基本的结构，你可以根据实际项目的静态资源组织方式进行调整。

---

**注意**

如果你没有将静态资源放在`src/main/resources`目录下，而是放在其他位置，例如`src/main/webapp`目录下，那么在打包后，静态资源的位置将取决于打包方式和构建工具的配置。

1. 打包为JAR文件：
   - 如果你使用`mvn package`或`./gradlew build`等命令将Spring Boot应用程序打包为JAR文件，并将静态资源放在`src/main/webapp`目录下，那么这些静态资源文件将不会被默认包含在生成的JAR文件中。
   - JAR文件中只会包含`src/main/resources`目录下的资源文件，因此`src/main/webapp`目录下的静态资源不会被打包进JAR文件。

2. 打包为WAR文件：
   - 如果你将Spring Boot应用程序打包为WAR文件，并将静态资源放在`src/main/webapp`目录下，那么在打包后，这些静态资源将位于WAR文件的根目录下的`webapp`目录中。
   - 例如，`src/main/webapp/style.css`文件将被打包到WAR文件中的`/style.css`位置。

需要注意的是，如果你的项目结构和静态资源文件位置不符合默认约定，你可能需要进行相应的配置来告诉构建工具如何处理这些静态资源文件。具体的配置方式取决于所使用的构建工具和打包方式。

总结：如果你将静态资源放在`src/main/webapp`目录下，打包为JAR文件时这些资源文件不会被包含在JAR文件中。如果打包为WAR文件，静态资源将位于WAR文件的根目录下的`webapp`目录中。对于非默认位置的静态资源文件，你可能需要进行额外的配置来告诉构建工具如何处理这些文件。