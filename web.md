# 快捷键

- alt+enter:调出快捷编写模式
- ctrl+o:调出可重写方法
- tab:切换选项
- alt+ins:在文件夹上是调出新建菜单/调出构造方法
- ctrl+y:删除行
- .var:快速生成(对象/属性)
- ctrl+shift+enter:智能补全
- ctrl+d:复制行
- ctrl+tab:快速切换文件窗口
- ctrl+p:列出方法的输入参数
- ctrl+w:选中单个单词
- ctrl+shift+"-/+":一键收起/一键展开
- ctrl+e:最近使用过的文件

# request&response

## request

```java
//服务器跳转重定向路径-->target目录下的路径,网页地址不变
request.getResquestDispatcher("路径").forword(req,resp);
//获取请求方式
request.getMetgod();
//获取虚拟目录/hello
request.getContextPath();
//获取Servlet路径
request.getServletPath();
//获取get方法请求参数(暴露在路径上的):name=zhangsan&age=22(获取的是一串字符需要后期分割处理)
request.getQueryString();
//获取js/json/html文件中的标签的name一样的值
request.getParameter("name的属性值");
//设置/获取键为**的属性值
request.setAttribute("username",username);
request.getAttribute("username");
//获取urL/uri(url统一资源定位符---整个网址的网页路径部分/uri统一资源标识符---减去网络地址和端口号剩下的)
request.getRequestURl();
request.getRequestURL();
//获取协议及版本
request.getProtocol();//HTTP/1.1
//获取客户机IP地址
request.getRemoteAddr();
```



## response

```java
//客户端跳转重定向路径-->target目录下的路径,网页地址改变,可以访问改变后的网页的内容
response.sendRrirdct("路径");
//设置客户端输出类型(applicable/json;text/html;text/plain)
response.setContentType("输出类型");
response.setContentType("输出类型,编码格式");
//设置服务器端的编码格式
response.setCharacterEncoding("UTF-8");

```

利用注解设置初始化

![](C:\Users\wang rich\Desktop\Snipaste_2022-03-05_23-03-47.png)



# AJAX

```javascript
let xHttp = new XMLHttpRequest();//创建对象
xHttp.open("GET(请求类型)","http://localhost/ajaxServlet(请求目标地址)")//发送请求
xHttp.send();
xHttp.onreadystatechange=function(){}//获取请求

<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
axios({
    method:"get",
    url:"http://localhost/ajax-demo?username="+username
}).then(function(resp){
    alert('123')
})

axios({
    method:"post",
    url:"http://localhost/ajax-demo",
    data:"username="+username
}).then(function(resp){
    alert('123')
})

//---------------------------------简-写-------------------

axios.get("/path").then((resp)=>{
    //执行语句
    alert('123')
})

axios.post("/path","value").then((resp)=>{
    //执行语句
    alert('123')
})
```

# json/xml/js类型转换

```javascript
const jsonObj=JSON.parse(jsonStr) //转成js对象
const objStr=JSON.stringify(obj)//转成json对象
```

