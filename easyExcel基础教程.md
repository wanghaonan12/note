# esayExcel基础教程

## 简介

Java解析、生成Excel比较有名的框架有Apache poi、jxl。但他们都存在一个严重的问题就是非常的耗内存，poi有一套SAX模式的API可以一定程度的解决一些内存溢出的问题，但POI还是有一些缺陷，比如07版Excel解压缩以及解压后存储都是在内存中完成的，内存消耗依然很大。
easyexcel重写了poi对07版Excel的解析，一个3M的excel用POI sax解析依然需要100M左右内存，改用easyexcel可以降低到几M，并且再大的excel也不会出现内存溢出；03版依赖POI的sax模式，在上层做了模型转换的封装，让使用者更加简单方便

---

- 官方网站：https://easyexcel.opensource.alibaba.com/
- github地址：https://github.com/alibaba/easyexcel
- gitee地址：https://gitee.com/easyexcel/easyexcel
- 开发文档地址：[关于Easyexcel | Easy Excel (alibaba.com)](https://easyexcel.opensource.alibaba.com/docs/current/)
- 语雀地址：[EasyExcel（文档已经迁移） (yuque.com)](https://www.yuque.com/easyexcel/doc/easyexcel)

## pom依赖

```xml
<!--    阿里巴巴  easy excel-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easyexcel</artifactId>
    <version>3.1.0</version>
</dependency>
<!--        json 格式化-->
<dependency>
    <groupId>com.alibaba.fastjson2</groupId>
    <artifactId>fastjson2</artifactId>
    <version>2.0.12</version>
</dependency>
```

## 读

## 写

### easyExcel官网案例

```java
/**
 * 文件下载（失败了会返回一个有部分数据的Excel）
 * <p>1. 创建excel对应的实体对象 参照{@link DownloadData}
 * <p>2. 设置返回的 参数
 * <p>3. 直接写，这里注意，finish的时候会自动关闭OutputStream,当然你外面再关闭流问题不大
 */
@GetMapping("download")
public void download(HttpServletResponse response) throws IOException {
    // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
    response.setContentType("application/vnd.ms-excel");
    response.setCharacterEncoding("utf-8");
    // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
    String fileName = URLEncoder.encode("测试", "UTF-8");
    response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
    EasyExcel.write(response.getOutputStream(), DownloadData.class).sheet("模板").doWrite(data());
}

/**
 * 文件上传
 * <p>1. 创建excel对应的实体对象 参照{@link UploadData}
 * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link UploadDataListener}
 * <p>3. 直接读即可
 */
@PostMapping("upload")
@ResponseBody
public String upload(MultipartFile file) throws IOException {
    EasyExcel.read(file.getInputStream(), UploadData.class, new UploadDataListener(uploadDAO)).sheet().doRead();
    return "success";
}
```

