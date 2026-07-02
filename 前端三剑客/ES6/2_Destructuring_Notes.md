
# 解构赋值（Destructuring Assignment）

ES6 中引入了解构赋值，它是一种简洁的方式，可以从数组或对象中提取值，并赋值给变量。

## 1. 数组解构

### 基本用法
从数组中提取值，按照对应位置进行赋值。
```javascript
    // 数组解构 获取指定位置的元素
const [a, b] = [1, 2];
console.log("\n 数组解构 获取指定位置的元素\n")
console.log(a);  // 1
console.log(b);  // 2
```

### 跳过元素
你可以跳过数组中的某些元素：
```javascript
    // 跳过指定位置的元素
const [c, , d] = [1, 2, 3];
console.log("\n 跳过指定位置的元素\n")
console.log(c);  // 1
console.log(d);  // 3
```

### 剩余元素
使用剩余操作符 (`...`) 将剩余元素收集到一个数组中。
```javascript
    // 获取数组剩余元素
const [e, ...f] = [1, 2, 3, 4];
console.log("\n 获取数组剩余元素\n")
console.log(e);    // 1
console.log(f); // [2, 3, 4]
```

## 2. 对象解构

### 基本用法
从对象中提取值并赋值给同名变量。
```javascript
    // 对象解构
const j = { name: 'Alice', age: 25 };
const { name: k, age: l } = j;
console.log("\n 对象解构\n")
console.log(k); // Alice
console.log(l);  // 25
```

### 赋值给新变量名
通过使用冒号，可以将提取的值赋值给不同的变量名。
```javascript
    // 对象属性解构重命名
const { name: m, age: n } = j;
console.log("\n 对象属性解构重命名\n")
console.log(m); // Alice
console.log(n);  // 25
```

### 默认值
如果提取的变量不存在，可以为其设置默认值。
```javascript
    // 对象解构获取属性 并设置默认值
const { name: o, gender = 'female' } = { name: 'Alice' };
console.log("\n 对象解构获取属性 并设置默认值\n")
console.log(o);   // Alice
console.log(gender); // female
```

## 3. 嵌套解构

### 数组中的嵌套解构
```javascript
    // 嵌套解构
const [g, [h, i]] = [1, [2, 3]];
console.log("\n 嵌套解构\n")
console.log(g); // 1
console.log(h); // 2
console.log(i); // 3
```

### 对象中的嵌套解构
```javascript
    // 对象解构获取嵌套对象属性
const p = {
  name: 'Alice',
  address: {
    city: 'Wonderland',
    zip: 12345
  }
};
const { address: { city: q, zip: r } } = p;
console.log("\n 对象解构获取嵌套对象属性\n")
console.log(q); // Wonderland
console.log(r);  // 12345
```

## 4. 函数参数中的解构

你可以在函数参数中直接使用解构赋值。
### 数组参数解构
```javascript
    // 函数参数解构
function sum([s, t]) {
  return s + t;
}
console.log("\n 函数参数解构\n")
console.log(sum([1, 2])); // 3
```

### 对象参数解构
```javascript
function greet({ name, age }) {
  console.log(\`Hello, my name is \${name} and I am \${age} years old.\`);
}
greet({ name: 'Alice', age: 25 });
// 输出: Hello, my name is Alice and I am 25 years old.
```

## 5. 解构的用途

- **交换变量值**：
  ```javascript
    // 临时变量交换
    let u = 1, v = 2;
    [u, v] = [v, u];
    console.log("\n解构用途 临时变量交换\n")
    console.log(u); // 2
    console.log(v); // 1
  ```

- **从函数返回多个值**：
  ```javascript
    // 函数返回值解构
    function getCoordinates() {
        return [10, 20];
    }
    console.log("\n解构用途 函数返回值解构\n")
    const [w, x] = getCoordinates();
    console.log(`x=${w}, y=${x}`); // 10 20
  ```

- **处理函数返回的对象**：
  ```javascript
    // 对象解构
    function getPerson() {
        return { name: 'Alice', age: 25 };
    }
    const { name: y, age: z } = getPerson();
    console.log("\n解构用途 对象解\n")
    console.log(y, z); // Alice 25
  ```

- **快速提取嵌套数据**：
  从复杂的对象结构中提取所需的部分数据。
  ```javascript
    // 嵌套对象解构
    const a1 = {
        name: 'Alice',
        address: {
            city: 'Wonderland',
            zip: 12345
        }
    };
    const { address: { city: b1 } } = a1;
    console.log("\n解构用途 嵌套对象解\n")
    console.log(b1); // Wonderland
  ```

## 6. 解构的注意事项

- **避免无效解构**：
  如果试图从 `null` 或 `undefined` 中解构，会抛出错误。
  ```javascript
  const { prop } = null;  // TypeError: Cannot destructure property `prop` of 'null' as it is null.
  ```

- **解构空对象或数组**：
  当解构空数组或对象时，使用默认值可以避免 `undefined` 的问题。
  ```javascript
    const [c1 = 1] = [];
    console.log(c1); // 1
  ```

  ```javascript
    const { name: d1 = 'Unknown' } = {};
    console.log(d1); // Unknown
  ```
