### Promise 是什么

在 JavaScript 中，`Promise` 是用于处理异步操作的对象。它代表一个在未来可能完成或失败的操作，并且允许我们以一种更优雅、清晰的方式处理异步代码，避免了传统的嵌套回调（"回调地狱"）。

### 1. Promise 的三种状态

一个 `Promise` 对象有三种可能的状态：

1. **Pending（进行中）**：初始状态，操作尚未完成。
2. **Fulfilled（已成功）**：操作成功完成，Promise 已经返回结果。
3. **Rejected（已失败）**：操作失败，Promise 已经返回错误。

一旦 `Promise` 状态变为 `fulfilled` 或 `rejected`，它就不能再改变。

### 2. 创建 Promise

`Promise` 构造函数接收一个**执行函数**，该函数包含两个参数：`resolve` 和 `reject`，分别用于表示异步操作成功或失败时要执行的回调。

```javascript
const myPromise = new Promise((resolve, reject) => {
    let success = true; // 假设的异步操作状态

    if (success) {
        resolve('操作成功'); // 当异步操作成功时，调用 resolve
    } else {
        reject('操作失败'); // 当异步操作失败时，调用 reject
    }
});
```

### 3. 使用 `then()` 和 `catch()`

我们通过 `then()` 方法来处理 `Promise` 成功时的结果，而通过 `catch()` 方法来处理 `Promise` 失败时的错误。

```javascript
myPromise.then(result => {
    console.log(result);  // 输出: 操作成功
}).catch(error => {
    console.log(error);  // 输出: 操作失败（如果失败了）
});
```

### 4. 链式调用

`Promise` 的一个强大特性是可以通过链式调用来处理多个异步操作的结果。每个 `then()` 方法都返回一个新的 `Promise`，因此我们可以串联多个 `then()`。

```javascript
new Promise((resolve, reject) => {
    resolve(10); // 第一个 Promise 成功，传递值 10
})
.then(value => {
    console.log(value);  // 输出: 10
    return value * 2;    // 返回 20，传递给下一个 then
})
.then(value => {
    console.log(value);  // 输出: 20
    return value * 3;    // 返回 60，传递给下一个 then
})
.then(value => {
    console.log(value);  // 输出: 60
})
.catch(error => {
    console.log(error);  // 如果任意 then 出错，则 catch 捕获
});
```

### 5. `Promise.all()` 和 `Promise.race()`

#### 5.1 `Promise.all()`

- **描述**：接受一个包含多个 `Promise` 的数组，只有当所有 `Promise` 都成功时，才会返回一个新的 `Promise`，其值是所有 `Promise` 成功结果的数组。如果任意一个 `Promise` 失败，则整个 `Promise.all()` 立即失败。
- **语法**：`Promise.all([promise1, promise2, ...])`

```javascript
const p1 = Promise.resolve(3);
const p2 = Promise.resolve(5);
const p3 = Promise.resolve(8);

Promise.all([p1, p2, p3])
    .then(results => {
        console.log(results);  // 输出: [3, 5, 8]
    })
    .catch(error => {
        console.log(error);  // 如果任意一个 Promise 失败，这里捕获错误
    });
```

#### 5.2 `Promise.race()`

- **描述**：同样接受一个包含多个 `Promise` 的数组，返回第一个完成的 `Promise`，不论是 `fulfilled`（成功）还是 `rejected`（失败），它都会返回该 `Promise` 的结果。
- **语法**：`Promise.race([promise1, promise2, ...])`

```javascript
const p1 = new Promise((resolve) => setTimeout(resolve, 500, 'one'));
const p2 = new Promise((resolve) => setTimeout(resolve, 100, 'two'));

Promise.race([p1, p2])
    .then(value => {
        console.log(value);  // 输出: two，因为 p2 先完成
    });
```

### 6. Promise 的实际应用场景

#### 6.1 异步 HTTP 请求

在现代 JavaScript 开发中，`Promise` 常用于处理像 `fetch` 这样的异步 HTTP 请求：

```javascript
 console.log("使用 Promise 读取本地 JSON 文件------------------------")
const fetchLocalJSON = (filePath) => new Promise((resolve, reject) => {
    // 使用 fetch API 请求文件 项目中会使用 axios 替换 fetch
    fetch(filePath)
        .then(response => {
            if (!response.ok) {
                reject('网络响应失败');
            }
            return response.json(); // 将响应数据解析为 JSON
        })
        .then(data => resolve(data)) // 成功解析 JSON 后执行 resolve
        .catch(error => reject(error)); // 处理任何错误
});


// 使用 Promise 链结合 fetch 第一次请求
fetchLocalJSON('5_data_person_10.json')
    .then(serverResponse => {
        // 处理第一次服务器返回的数据
        console.log('第一次服务器返回的结果:', serverResponse);
        // 第二次请求
        return fetchLocalJSON('5_data_person_20.json')
    })
    .then(serverResponse => {
        // 处理第二次服务器返回的数据
        console.log('第二次服务器返回的结果:', serverResponse);
        // 第三次请求,不存在会报错并在catch中捕获
        return fetchLocalJSON('5_data_person_30.json')
    })
    .catch(error => {
        // 处理请求中错误
        console.error('发生错误啦啦啦:', error);
    })
```

#### 6.2 定时操作

`Promise` 也可以用于处理定时器操作，如 `setTimeout`，在一定时间后返回结果：

```javascript
const wait = ms => new Promise(resolve => setTimeout(resolve, ms));
wait(1000).then(() => console.log('1 秒后执行的代码'));
```