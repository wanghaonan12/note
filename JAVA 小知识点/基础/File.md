# File
## File类常用方法

**文件操作**

| 方法                | 描述                                                         |
|---------------------|--------------------------------------------------------------|
| `exists()`          | 判断文件是否存在                                             |
| `isFile()`          | 判断路径是否为文件                                           |
| `isDirectory()`     | 判断路径是否为目录                                           |
| `getName()`         | 返回文件或目录的名称                                         |
| `getPath()`         | 返回文件或目录的路径                                         |
| `getParent()`       | 返回父目录的路径                                             |
| `canRead()`         | 判断文件是否可读                                             |
| `canWrite()`        | 判断文件是否可写                                             |
| `canExecute()`      | 判断文件是否可执行                                           |
| `length()`          | 返回文件的大小（字节数）                                     |
| `lastModified()`    | 返回文件的最后修改时间                                       |
| `createNewFile()`   | 创建新文件                                                   |
| `delete()`          | 删除文件                                                     |
| `renameTo(File)`    | 重命名文件                                                   |
| `list()`            | 返回目录下的文件和目录名列表（不包括子目录）                   |
| `listFiles()`       | 返回目录下的文件和目录列表（不包括子目录），返回File对象数组    |
| `mkdir()`           | 创建单个目录                                                 |
| `mkdirs()`          | 创建多级目录                                                 |
| `delete()`          | 删除文件或目录                                               |

**文件夹操作**

| 方法                | 描述                                                         |
|---------------------|--------------------------------------------------------------|
| `exists()`          | 判断文件夹是否存在                                           |
| `isDirectory()`     | 判断路径是否为目录                                           |
| `getName()`         | 返回文件夹的名称                                             |
| `getPath()`         | 返回文件夹的路径                                             |
| `getParent()`       | 返回父目录的路径                                             |
| `canRead()`         | 判断文件夹是否可读                                           |
| `canWrite()`        | 判断文件夹是否可写                                           |
| `canExecute()`      | 判断文件夹是否可执行                                         |
| `list()`            | 返回文件夹下的文件和目录名列表（不包括子目录）                 |
| `listFiles()`       | 返回文件夹下的文件和目录列表（不包括子目录），返回File对象数组  |
| `mkdir()`           | 创建单个目录                                                 |
| `mkdirs()`          | 创建多级目录                                                 |
| `delete()`          | 删除文件夹                                                   |

这些方法可用于文件和文件夹的创建、删除、重命名、判断状态等操作。请注意，Java 7及更高版本中引入了新的NIO.2类库，如Path和Files类，提供更强大和灵活的文件操作功能。

### 初始化代码，添加需要用到的文件

```java
public class Code_00_init {
    public static void main(String[] args) throws IOException {
        File file = new File("java8/src/main/java/com/whn/File/IO/IOFile.txt");
        File file2 = new File("java8/src/main/java/com/whn/File/document_object_txt.txt");
        List<String> content = Arrays.asList("枫桥夜泊","【作者】张继 【朝代】唐","月落乌啼霜满天，江枫渔火对愁眠。","姑苏城外寒山寺，夜半钟声到客船。");
        FileWriter fileWriter = new FileWriter(file);
        FileWriter fileWriter2 = new FileWriter(file2);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter2);
        content.forEach(s -> {
            try {
                bufferedWriter2.write(s);
                bufferedWriter.write(s);
                bufferedWriter.write("\n");
                bufferedWriter2.write("\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        bufferedWriter.close();
        bufferedWriter2.close();
    }
}

```

### file类案例代码

```java public class Code_01_File {
    public static void main(String[] args) throws IOException {

        File file = new File("java8/src/main/java/com/whn/File/document_object_txt.txt");
//      获取一些信息属性
        System.out.println("document_object_txt:"+file.exists());
        System.out.println("文件是否可读"+file.canRead());
        System.out.println("文件的名字"+file.getName());
        System.out.println("文件的绝对位置"+file.getAbsolutePath());
        System.out.println("文件的相对位置"+file.getPath());
        System.out.println("是否是文件"+file.isFile());
        System.out.println("文件大小"+file.length());
        System.out.println("是否有抽象路径名存在 并且 允许应用程序执行文件"+file.canExecute());
        System.out.println("是否是文件"+file.isDirectory());
        System.out.println("是否隐藏"+file.isHidden());
        System.out.println("是否可写"+file.canWrite());
        System.out.println("获取上级目录"+file.getParent());
//        文件的创建与删除
        file.delete();
        if (!file.exists()) {
            System.out.println("文件不存在创建新文件...");
            file.createNewFile();
        }
//        创建文件夹
        String dir = "java8/src/main/java/com/whn/File/test1/test2";
        String dir2 = "java8/src/main/java/com/whn/File/test3/test4";
        String dir3 = "java8/src/main/java/com/whn/File/test4";
        File file1 = new File(dir);
        File file2 = new File(dir2);
        File file3 = new File(dir3);
//        如果删除的是文件夹 ，但是文件夹中有文件存在无法删除
        file3.delete();
        if (!file1.exists()) {
            System.out.println("创建文件夹...");
//            创建单个文件夹由于test1不存在，无法创建
            file1.mkdir();
//            创建多层
            file2.mkdirs();
        }

        File[] files = file3.listFiles();//获取文件加下的所有文件名
        for (File file4 : files) {
            System.out.println(file4.getName());
        }
        String[] list = file3.list();//获取文件名字
        Arrays.stream(list).forEach(System.out::println);
    }
}
```

## IO流

【1】File类：封装文件/目录的各种信息，对目录/文件进行操作，但是我们不可以获取到文件/目录中的内容。
【2】I/O ： Input/Output的缩写，用于处理设备之间的数据的传输。
【3】形象理解：IO流 当做一根 “管”：其中输入输出是相对于程序的输入输出，从文件到程序叫输入，从程序到文件叫输出

![image-20230717141219998](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230717141219998.png)

【4】IO流体系结构

![image-20230717141321869](https://wang-rich.oss-cn-hangzhou.aliyuncs.com/md/image-20230717141321869.png)

【5】**不要用字符流操作非文本文件如：pg,  .mp3  ,   .mp4 , .doc  , .ppt**

字符流是按照字符编码进行读取和写入操作的，它们假设数据是由字符组成的，并在读取和写入时进行字符编码的转换。这适用于文本文件，其中数据以字符形式表示，并使用特定的字符编码（如UTF-8、GBK等）进行编码和解码。

然而，非文本文件是以字节的形式存储的，其中包含了二进制数据和各种格式的文件结构。使用字符流读取非文本文件时，它会尝试将字节数据按照字符编码进行解码，这可能会导致数据损坏或产生意外的结果。同样，使用字符流写入非文本文件时，它会尝试将字符数据按照字符编码转换为字节数据，这也会导致数据损坏或不可用。

### 案例1：使用字节流复制文件

```java
public class Code_01_IO {
    public static void main(String[] args) throws Exception {
//        读取文件位置
        File file = new File("java8/src/main/java/com/whn/File/IO/IOFile.txt");
//        复制文件位置
        File copy_file = new File("java8/src/main/java/com/whn/File/IO/IOFile_cop2.txt");
//        文件不存则新建
        if (!copy_file.exists()) {
            try {
                copy_file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
//        输入流
        InputStream in = new FileInputStream(file);
        InputStreamReader is = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(is);
//        输出流
        OutputStream ou = new FileOutputStream(copy_file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(ou);
        BufferedWriter bw = new BufferedWriter(outputStreamWriter);
//        缓冲区
        char[] c = new char[10 * 2];
        int read = br.read(c);
        while (read != -1) {
            System.out.println(c);
            bw.write(c, 0, read);
            read = br.read(c);
        }
//       逆序关闭流
        bw.close();
        outputStreamWriter.close();
        ou.close();
        br.close();
        is.close();
        in.close();
    }
}
```

### 案例2：使用字符流复制文件

```java
public class Code_02_IO {
    public static void main(String[] args) throws Exception {
//        读取文件位置
        File file = new File("java8/src/main/java/com/whn/File/IO/IOFile.txt");
//        复制文件位置
        File copy_file = new File("java8/src/main/java/com/whn/File/IO/IOFile_copy.txt");
//        文件不存则新建
        if (!copy_file.exists()) {
            try {
                copy_file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
//        输入流
        InputStream is = null;
//        输出流
        OutputStream os = null;
//        缓冲输入流
        BufferedInputStream bufferedInputStream = null;
//        缓冲输出流
        BufferedOutputStream bufferedOutputStream = null;
        is = new FileInputStream(file);
        bufferedInputStream = new BufferedInputStream(is);
        os = new FileOutputStream(copy_file);
        bufferedOutputStream = new BufferedOutputStream(os);
//        缓冲数组
        byte[] b = new byte[8*4];
        int read = bufferedInputStream.read(b);
        while (read != -1) {
//            输出流输出
            bufferedOutputStream.write(b, 0, read);
//            输入流读取文件
            read = bufferedInputStream.read(b);
        }
//      逆序关闭流操作
        if (!Objects.isNull(bufferedOutputStream)) {
            bufferedOutputStream.close();
        }
        if (!Objects.isNull(os)) {
            os.close();
        }
        if (!Objects.isNull(bufferedInputStream)) {
            bufferedInputStream.close();
        }
        if (!Objects.isNull(is)) {
            is.close();
        }
    }
}
```

