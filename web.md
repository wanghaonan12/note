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
```



## response

```java
```

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

