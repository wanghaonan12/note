# 对象操作优化

## 对象操作方法

```javascript
const person = {
    name: 'Alice',
    age: 25,
    languages: ['English', 'Spanish']
};

console.log(Object.keys(person)); // 输出: [ 'name', 'age', 'languages' ]
console.log(Object.values(person)); // 输出: [ 'Alice', 25, [ 'English', 'Spanish' ] ]
console.log(Object.entries(person)); // 输出: [ [ 'name', 'Alice' ], [ 'age', 25 ], [ 'languages', [ 'English', 'Spanish' ] ] ]
```

- Object.keys(): 获取对象所有属性字段,以列表的形式返回
- Object.values(): 获取对象所有值,以列表的形式返回
- Object.entries(): 获取键值对 二维数组的形式返回 ,二级数组的 分别以属性名和值的顺序存在

## 合并对象

```javascript
const target = { a: 1 };
const source_b = { b: 2 };
const source_c = { c: 3 };

console.log("合并对象!");
Object.assign(target, source_b, source_c);
console.log(target); // 输出: { a: 1, b: 2, c: 3 }
```

使用场景:

- **动态构建对象**：在需要动态构建对象时，可以使用 `Object.assign()` 合并多个对象。
- **设置默认属性**：可以先创建一个默认对象，然后通过 `Object.assign()` 合并用户自定义的设置，覆盖默认属性。

------

## 浅拷贝与深拷贝

### 浅copy

```javascript
const father = {
    name: 'father',
    age: 80,
    languages: ['English', 'Spanish'],
    info: person
};

// 浅拷贝
const shallowCopy = Object.assign({}, father);
console.log("浅拷贝");
console.log(shallowCopy); // 输出: { name: 'father', age: 80, languages: [ 'English', 'Spanish' ], info: { name: 'Alice', age: 25, languages: [ 'English', 'Spanish' ] } }
```

> 浅拷贝只复制对象的第一层属性，对于引用类型（如对象和数组），复制的是引用，改变引用会影响所有指向该属性的对象。

### 深copy

> 深拷贝会复制对象及其所有嵌套对象，确保每个对象都互不影响。

```javascript
const father = {
    name: 'father',
    age: 80,
    languages: ['English', 'Spanish'],
    info: person
};

// 深拷贝
const deepCopy = JSON.parse(JSON.stringify(father));
console.log("深拷贝");
console.log(deepCopy); // 输出: { name: 'father', age: 80, languages: [ 'English', 'Spanish' ], info: { name: 'Alice', age: 25, languages: [ 'English', 'Spanish' ] } }
```

注意:

- 深拷贝不支持函数、`undefined`、`Symbol` 等类型。使用 `JSON` 方法时，函数和特殊值会丢失。
- 复杂对象的深拷贝可以使用第三方库如 `lodash` 提供的 `_.cloneDeep()` 方法。

> 注意: 当对象浅 copy 时,对象源属性中的对象属性被修改, copy 出来的也会被修改

```javascript
// 修改原对象属性
person.name = "富贵";
console.log("属性修改----");
console.log("浅拷贝");
console.log(shallowCopy.info); // 输出: { name: '富贵', age: 25, languages: [ 'English', 'Spanish' ] }

console.log("深拷贝");
console.log(deepCopy.info); // 输出: { name: 'Alice', age: 25, languages: [ 'English', 'Spanish' ] }
```

------

## 对象声明与方法

```javascript
const name = 'rich';
const age = 18;
const fugui = { name, age }; // 使用 ES6 的对象字面量简写
console.log(fugui); // 输出: { name: 'rich', age: 18 }
```

####  对象方法定义

```javascript
    // 对象方法声明
    const person_method = {
        name: 'Alice',
        age: 25,
        // 常规方法定义
        sayNameAndAge: function () {
            console.log(`My name is ${this.name}, and I am ${this.age} years old.`);
        },
        // 简写方法定义
        eat(food) {
            console.log(`Hello, my name is ${this.name}. I like eat ${food} `);
        },
        // 箭头函数方法定义 注意这里的 person_method.age 是当前对象
        sayAge: (realAge) => {
            console.log(`I am not ${this.age} years old , is ${realAge} years old`);
        }
    };
```

- **常规方法定义**：使用 `function` 关键字，`this` 指向调用该方法的对象。在上述示例中，`this.name` 和 `this.age` 分别返回 `Alice` 和 `25`。
- **简写方法定义**：ES6 提供的语法，简化了方法的书写。功能与常规方法定义相同。
- **箭头函数**：与普通函数不同，箭头函数没有自己的 `this`，它会从外部作用域继承 `this`。在这里，`sayAge` 方法中的 `this` 会指向 `person_method` 对象。

**注意事项**

> 使用箭头函数作为对象方法时要小心，可能导致 `this` 的上下文问题。在大多数情况下，推荐使用常规方法定义来确保 `this` 的正确指向。

------

## 对象扩展运算符

扩展运算符（`...`）是 ES6 引入的一种语法，用于展开数组或对象，提供了更简洁的对象合并和复制方式。

### 对象扩展运算符示例

```javascript
const person_extend = {
    name: 'Alice',
    languages: ['English', 'Spanish']
};

const person_extend_2 = {
    name: 'Bob',
    age: 30,
    ...person_extend // 如果存在相同的属性，后面的会覆盖前面的属性
};

console.log(person_extend_2); // 输出: { name: 'Alice', age: 30, languages: [ 'English', 'Spanish' ] }
```

### 对象合并示例

```javascript
const name_age = { name: 'Alice', age: 25 };
const languages = { languages: ['English', 'Spanish'] };
const person_merge = { ...name_age, ...languages };

console.log(person_merge); // 输出: { name: 'Alice', age: 25, languages: [ 'English', 'Spanish' ] }
```

------

## 数组方法

`map()` 方法

```javascript
const numbers = [1, 2, 3, 4, 5];

// map 方法：将每个元素乘以 2
const squaredNumbers = numbers.map(number => number * 2);
console.log(squaredNumbers); // 输出: [2, 4, 6, 8, 10]
```

> `map()` 创建一个新数组，原数组不变。适用于需要对数组元素进行变换的场景，如转换数据格式、提取某个属性等。

 `reduce()` 方法

```javascript
// reduce 方法：计算数组的总和
const sum = numbers.reduce((accumulator, currentValue) => accumulator + currentValue, 0);
console.log(sum); // 输出: 15
```

> `reduce()` 适合用于聚合数据，如求和、求最大值、统计频次等，能够灵活处理多种数据处理逻辑。

 `filter()` 方法

```javascript
const evenNumbers = numbers.filter(number => number % 2 === 0);
console.log(evenNumbers); // 输出: [2, 4]
```

`find()` 方法

```javascript
const foundNumber = numbers.find(number => number > 3);
console.log(foundNumber); // 输出: 4
```

 `forEach()` 方法

```javascript
numbers.forEach(number => console.log(number)); // 输出: 1, 2, 3, 4, 5（逐行输出）
```

