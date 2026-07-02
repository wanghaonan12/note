# 函数优化：普通构造函数与箭头函数结合解构表达式

## 1. 普通构造函数
使用传统的构造函数来创建对象实例的方式。

```javascript
// 普通构造函数
function logPersonName(persion) {
    console.log(`name=${persion.name}`);
}
```
## 2. 使用箭头函数和解构表达式的优化
使用箭头函数结合解构表达式来简化构造过程，提高可读性。
  ```javascript
// 使用箭头函数和解构
const logPersonName=({name})=>console.log(`name=${name}`);
``` 
