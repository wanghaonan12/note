# 模版语法

[模板语法 官方文档](https://cn.vuejs.org/guide/essentials/template-syntax.html#text-interpolation)

模版语法已成为前端在数据驱动模式上 V 层最好的实现。

## 插值
``` html
<div id="app">
  <!-- 文本 当对 data.message 发生改变时，对应插值的内容也会自动改变-->
  <fieldset>
    <legend>文本</legend>
    <div>{{message}}</div>
  </fieldset>

  <!-- 纯 HTML {{}} 这种形式最终会被解释成文本，如果要想输入 HTML 结构，使用要用到 v-html="对象"-->
  <fieldset>
    <legend>纯 HTML</legend>
    <div v-html="rawHtml"></div>
  </fieldset>		

  <!-- 属性 元素的任意属性（包含自定义属性）都可以和对象绑定 :属性名(或者 v-bind:属性名)=“对象”-->
  <fieldset>
    <legend>属性</legend>
    <img :src="src" alt="" />
    <img v-bind:src="'../imgs/red.jpg'" alt="" />
  </fieldset>	

  <!-- js 表达式 {{}} 可以用来解释 js 的表达式-->
  <fieldset>
    <legend>js 表达式</legend>
    <div>{{1 + 1}}</div>
    <div>{{status ? 'YES' : 'NO'}}</div>
    <div>{{message.split('').reverse().join('')}}</div>
  </fieldset>	
</div>
```

``` javascript
var vm = new Vue({
  el: '#app',
  data: {
    message: '我是文本',
    rawHtml: '<h1>我是 h1 标签</h1>',
    src: '../imgs/green.jpg',
    status: true,
  }
})
```
## 缩写
### v-bind 缩写
``` html
  <!--完整写法-->
  <img v-bind:src="'../imgs/red.jpg'" alt="" />
  <!--缩写-->
  <img :src="src" alt="" />
```
### v-on 缩写
``` html
  <!--完整语法-->
  <button v-on:click="greet">Greet</button>
  <!--缩写语法-->
  <button @click="greet">Greet</button>  
```

## 指令
指令（Directive），换句话说就是元素的自定义属性，在 Vue 中是以 v- 为前缀的自定义属性，属性值为对象或 js 表达式

### v-text

```html
 <span v-text="msg"></span>
  <!--效果等同于-->
  <!--v-text 的权重高于 {{}}-->
  <span>{{msg}}</span>
```

### v-html

```html
<!--html 为 html 类型的 string 文本-->
<div v-html="html"></div>
```

### v-show

```html
<!--show 的值会直接影响 div 在文档中是否显示-->
  <div v-show="show"></div>
```

### v-if

```html
<!--status 的值会直接影响 div 在文档中是否存在-->
  <div v-if="status"></div>
```

### v-else-if

```html
<div v-if="flag == 1">1</div>
  <!--必须跟 v-if 或者 v-else-if 元素后面-->
  <div v-else-if="flag == 2">2</div>
```

### v-else

```html
<div v-if="flag == 1">1</div>
  <div v-else-if="flag == 2">2</div>
  <!--必须跟 v-if 或者 v-else-if 元素后面-->
  <div v-else>2</div>
```

### v-for

```html
  <!--
    data = 3 
    结果会生成 3 个 div，
    value 的值分类为 1, 2, 3 
    index 的值分别为 0, 1, 2
  -->
  <div v-for="(value, index) in data">
    <span v-text="value"></span>
    <span>{{index}}</span>
  </div>
  <!--也可以这样写-->
  <div v-for="value in data">
    <span v-text="value"></span>
  </div>

  <!--
    data = "abc" 
    结果会生成 data.length 个 div，
    value 的值分类为 a, b, c 
    index 的值分别为 0, 1, 2
  -->
  <div v-for="(value, index) in data">
    <span v-text="value"></span>
    <span>{{index}}</span>
  </div>   
  <!--也可以这样写-->
  <div v-for="value in data">
    <span v-text="value"></span>
  </div>

  <!--
    data = {name: 'dk', age: 18} 
    结果会生成 data 属性个数 个 div，
    value 的值分类为 dk, 18 
    key 的值分别为 name, age
  -->
  <div v-for="(value, key) in data">
    <span v-text="key"></span>
    <span>{{value}}</span>
  </div>
  <!--也可以这样写-->
  <div v-for="value in data">
    <span v-text="value"></span>
  </div>

  <!--
    data = [{name: 'dk1', age: 18}, {name: 'dk2', age: 20}] 
    结果会生成 data.length 个 div，
    obj 的值分类为 data[0], data[1] 
    index 的值分别为0, 1
  -->
  <div v-for="(obj, index) in data">
    <span v-text="JSON.stringify(obj)"></span>
    <span>{{index}}</span>
  </div>    
  <!--也可以这样写-->
  <div v-for="obj in data">
    <span v-text="JSON.stringify(obj)"></span>
  </div>
```

### v-on

```html
  <!--click事件直接绑定一个方法-->
  <button v-on:click="say1">say1</button>
  <!--缩写方式-->
  <!--click事件使用内联语句-->
  <button @click="say2('调用了 say2', $event)">say2</button>     
```

### v-bind

```html
  <img v-bind:src="'imgs/red.jpg'" />
  <!--缩写方式-->
  <img :src="'imgs/yellow.jpg'" />
```

### v-model

```html
  <!--仅限于表单元素，双向绑定-->
  <input type="text" v-model="mess"/>
```

### v-pre

```html
  <!--{{}} 不编译，当字符串输出-->
  <span v-pre>{{mess}}</span>
```

### v-cloak

```html
  <!--
    mess = 'abc'
    span 还没被 vue 解析的时候会显示 {{mess}}
    解析后会显示 123
    用于解决这两个转换的过程不友好的显示
    尤其是在页面加载过慢的情况很容易出现这种情况
  -->
  <span v-cloak>{{mess}}</span>
```

### v-once

```html
  <!--内容只解释一次，当改变 mess 时不会再次映射到 span-->
  <span v-once>{{mess}}</span>
```

# 绑定 class
## 对象语法
v-bind:class="{样式名: 结果为 boolean 的表达式}"，表达式结果为 true，则元素 class="样式名"，否则元素 class=""
``` html
    <div :class="{classNam1: 1 == 1, className2: 1 == 2}"></div>
```
渲染结果
``` html
    <div class="classNam1"></div>
```

也可以写成
``` html
    <div :class="classObject"></div>
```
``` javascript
    data: {
        classObject: {
            className1: false,
            className2: true
        }
    }
```
渲染结果
``` html
    <div class="className2"></div>
```

还可以通过计算属性的方式
``` html
    <div :class="classObjectComputed"></div>
```
``` javascript
    computed: {
        classObjectComputed: function(){
            return{
                className1: true,
                className2: true
            }
        }
    }
```
渲染结果
``` html
    <div class="className1 className2"></div>
```

## 数组语法
v-bind:class="[]"，数组元素可以为表达式，也可以为字符串，如果是字符串则直接输出为样式名
``` html
    <div :class="[class1, class2, 'className3', active ? 'className4' : '']"></div>
```
``` javascript
    data: {
        class1: 'className1',
        class2: 'className2',
        active: true
    }
```
渲染结果
``` html
    <div class="className1 className2 className3 className4"></div>
```

也可以用数据对象的方式，解析的逻辑和对象语法一样
``` html
    <div :class="[{className1: 1 == 1, className2: 1 == 2}, 'className3' ]"></div>
```
渲染结果
``` html
    <div class="className1 className3"></div>
```

# 绑定 style
## 对象语法
在对象当中，CSS 的属性名要用驼峰式表达：fontSize 解析成 font-size
``` html
    <div :style="{color: color, fontSize: fontSize, backgroundColor: '#ccc'}"></div>
```
``` javascript
    data: {
        color: 'red',
        fontSize: '12px'
    }
```
渲染结果
``` html
    <div style="color: red, font-size: 12px; background-color: #ccc"></div>
```
## 数组语法
``` html
    <div :style="[styleObject, {backgroundColor: '#ccc'}]"></div>
```
``` javascript
    data: {
        styleObject: {
            color: 'red',
            fontSize: '12px'
        }
    }
```
渲染结果
``` html
    <div style="color: red, font-size: 12px; background-color: #ccc"></div>
```

# 实例元素 el
实例元素指的是 Vue 实例化时编译的容器元素，或者说是 Vue 作用的元素容器
``` html
    <div id="app"></div>
```
``` javascript
    var vm = new Vue({
        el: '#app'
    })
```
也可以为实例元素指定其它选择器
``` html
    <div class="app"></div>
```
``` javascript
    var vm = new Vue({
        el: '.app'
    })
```
可以有多个实例元素
``` html
    <div id="app1"></div>
    <div id="app2"></div>
```
``` javascript
    var vm = new Vue({
        el: '#app1'
    })
    var vm = new Vue({
        el: '#app2'
    })    
```
如果有多个相同的实例元素则只有第一个起效
``` html
    <div id="app"></div>
    <!--这个只当普通 html 输出，不会被 Vue 编译-->
    <div id="app"></div>
```
``` javascript
    var vm = new Vue({
        el: '#app'
    })
```
也可以在实例化的时候不指定实例元素，后面用 $mount() 手动进行挂载
``` html
    <div id="app"></div>
```
``` javascript
    var vm = new Vue({
        //option
    })

    vm.$mount("#app");
    
```
可以通过实例获取实例元素
``` javascript
    var vm = new Vue({
        el: '#app'
    })

    console.log(vm.$el)
```

# 数据对象 data
在 MVVM 模式中充当着 M(Model) 数据模型层，更多的体现于将 M 层映射到 V 层
``` html
    <div id="app">
        <!--结果为：文本-->
        <span>{{key1}}</span>
        <!--结果为：11-->
        <span>{{key2 + key3}}</span>
        <!--结果为：key4_1-->
        <span>{{key4.key4_1}}</span>
        <!--结果为：{"key5_1": "key5_1"}-->
        <span>{{JSON.stringify(key5[0])}}</span>
    </div>
```
``` javascript
    var array = [{key5_1: "key5_1"}];
    var vm = new Vue({
        el: '#app',
        data: {
            key1: '文本',
            key2: 1,
            key3: 10,
            key4: {
                key4_1: 'key4_1'
            },
            key5: array
        }
    }
```
可以通过实例获取数据对象
``` javascript
    var vm = new Vue({
        el: '#app',
        data: {}
    })

    console.log(vm.$data)
```

# 事件处理器 methods
元素可以通过 v-on:事件 || @事件 进行绑定事件，事件中的 this 默认指向实例 vm
``` html
    <div id="app">
        <input type="button" @click="count += 1" value="监听事件">
        <span>{{count}}</span>
    </div>
```
``` javascript
    var vm = new Vue({
        el: '#app',
        data: {
            count: 0
        }
    })
```
上面的情况仅适用于很简单的处理，复杂的处理可以需要单独处理
``` html
    <div id="app">
        <input type="button" value="确认" @click="counter"/>
        <p>确认按钮被点击了 {{ counter }} 次。</p>
    </div>
```
``` javascript
    var vm = new Vue({
        el: '#app',
        data: {
            count: 0
        },
        methods: {
            counter: function(){
                this.count += 1;
            }
        }
    })
```
在 js 的事件中默认会有个 event 对象，Vue 在事件上对 event 对象进行继承二次封装，改名为 $event，在事件当中默认传过去
``` html
    <div id="app">
        <input type="button" @click="eventer" count="1" value="event 对象">
        <span>{{count}}</span>
    </div>
```
``` javascript
    var vm = new Vue({
        el: '#app',
        data: {
            count: 0
        },
        methods: {
            eventer: function(event){
                var _count = event.target.getAttribute('count') * 1;
                this.count += _count;
                event.target.setAttribute('count', _count);
            }
        }
    })
```
在事件中很多情况下要传参数，Vue 也可以在事件中传参数
``` html
    <div id="app">
        <input type="button" @click="parameter(10, $event)" value="事件传参数">
        <span>{{count}}</span>
    </div>
```
``` javascript
    var vm = new Vue({
        el: '#app',
        data: {
            count: 0
        },
        methods: {
            parameter: function(n, event){
                this.count += n;
                event.target.setAttribute('disabled', true);
            }
        }
    })
```
[事件效果预览](https://dk-lan.github.io/vue-erp/VueBasic/VueBasicOptions/methods.html)

# 计算属性 computed
computed 主要是针对 data 的属性进行操作，this 的指针默认指向实例 vm
``` html
    <p>{{reversedMessage}}</p>
```
``` javascript
    data: {
        message: 'ABC'
    },
    computed: {
        reversedMessage: function(){
            return this.message.split('').reverse().join('')
        }
    }
```
渲染结果为
``` html
    <p>CBA</p>
```
在 computed 的属性中默认有两个属性，一个是 get，我们称之为 getter，另一个是 set，我们称之为 setter，所以也可以这样写：
``` javascript
    data: {
        message: 'ABC'
    },
    computed: {
        reversedMessage: {
            get: function(){
                return this.message.split('').reverse().join('')
            }
        }
    }
```
当我们在 V 层调用 {{reversedMessage}} 的时候会自动触发 reversedMessage.get()

setter 的逻辑也是一样，当改变对应的属性时，会自动触发 set 方法
``` html
    <div id="app">
        <!--调用了 fullName.get()-->
        <p>{{fullName}}</p>
        <input type="text"  v-model="newName">
        <!--changeName 事件改变了 fullName 的值，所以会自动触发 fullName.set()-->
        <input type="button" value="changeName" @click="changeName">
    </div>
```
``` javascript
    var vm = new Vue({
        el: '#app',
        data: {
            firstName:'DK',
            lastName: 'Lan',
            newName: ''
        },
        computed: {
            fullName:{
                get: function(){
                    return this.firstName + '.' + this.lastName
                },
                set: function(newValue){
                    this.firstName = newValue
                }
            }
        },
        methods: {
            changeName: function(){
                this.fullName = this.newName;
            }
        }
    })
```
Vue 在 getter 上面作了基于对应属性的依赖缓存，也就是说多次调用同一个属性，get 只会执行一次。而事件在每次触发时都会被调用，当然在改变该属性值的时候会再次被调用
``` html
    <div id="app">
        <!--fullName.get 只被调用一次-->
        <p>{{fullName}}</p>
        <p>{{fullName}}</p>
        <p>{{fullName}}</p>
        <!--每次点击都会调用 changeName-->
        <input type="button" value="changeName" @click="changeName('Vue')">
    </div>
```
``` javascript
    var vm = new Vue({
        el: '#app',
        data: {
            firstName:'DK',
            lastName: 'Lan',
            newName: ''
        },
        computed: {
            fullName:{
                get: function(){
                    return this.firstName + '.' + this.lastName
                },
                set: function(newValue){
                    this.firstName = newValue
                }
            }
        },
        methods: {
            changeName: function(txt){
                this.newName = txt;
                //如果在这里改变 this.fullName 的值，则会再次自动触发对应的 getter
            }
        }
    })
```
[计算属性效果预览](https://dk-lan.github.io/vue-erp/VueBasic/VueBasicOptions/computed.html)
# 监听器 watch
Vue 提供了对单个属性的监听器，当该属性发生改变的时候，自动触发，此项使用不当会影响性能，所以慎用。
```html
    <input type="text" v-model="a">
    <p>旧值：{{aOldVal}}</p>
    <p>新值：{{aNewVal}}</p>    
```
```javascript
    {
        data: {
            a: 1
        },
        watch: {
            a: function (newVal, oldVal) {
                //自动触发此方法
                console.log('new: %s, old: %s', newVal, oldVal)
            },
        }    
    }
```
也可以把方法放到 data 对象中
```javascript
    {
        data: {
            a: 1,
            changeA: function (newVal, oldVal) {
                //自动触发此方法
                console.log('new: %s, old: %s', newVal, oldVal)
            }
        },
        watch: {
            a: 'changeA'
        }    
    }
```
watch 与 compute 区别：
1. computed 创建新的属性， watch 监听 data 已有的属性
2. compute 会产生依赖缓存
3. 当 watch 监听 computed 时，watch 在这种情况下无效，仅会触发 computed.setter
```javascript
    {
        computed: {
            a: {
                get: function(){
                    return '';
                },
                set: function(newVal){
                    //会触发此项
                    console.log('set val %s', newVal);
                }
            }				 
        },
        watch: {
            a: function(){
                //不会被触发
                console.log('watch');
            }
        }    
    }
```
# 事件修饰符
对事件添加一些通用的限制，比如阻止事件冒泡，Vue 对这种事件的限制提供了特定的写法，称之为修饰符
用法：v-on:事件.修饰符

```html
    <!--阻止事件冒泡.stop-->
    <div id="div1" class="stop" @click.stop="event1(1)">
    <!--使用事件捕获模式.capture-->
    <div id="div4" class="stop" @click.capture="event1(4)">
    <!--事件只作用本身.self，类似于已阻止事件冒泡-->
    <div id="div7" class="stop" @click.self="event1(7)">
    <!--阻止浏览器默认行为.prevent-->
    <a href="https://github.com/dk-lan" target="_blank" @click.prevent="prevent">dk's github</a>
    <!--只作用一次.once-->
    <a href="https://github.com/dk-lan" target="_blank" @click.once="prevent">dk's github</a>
    <!--修饰符可以串联.click.prevent.once-->
    <a href="https://github.com/dk-lan" target="_blank" @click.prevent.once="prevent">dk's github</a>
```
# 组件
组件（Component）是前端在单页面应用（SPA）上最好的一种实现方式，把所有功能模块拆解成单独的组件，每个组件都有独立的作用域，且还可以相互通信



## 认识单页面应用（SPA）
在传统的页面之间跳转，是通过刷新，重新渲染一个页面而实现，在渲染的过程中势必要加载外部资源文件，页面在服务器中渲染出来是通过一系列的生命周期，在这个过程中会因为网速等硬件问题直接影响页面的加载速度，为解决这一问题，前端在新的设计模式上引入了组件的概念，页面之间的跳转变成了组件之间的切换，不需要重新加载整个页面，也不用考虑页面的生命周期，换成组件的生命周期，在性能上大大的提升了。

## Vue 的组件实现
### 全局组件
```html
    <div id="app">
        <!--组件的使用-->
        <global-component></global-component>
    </div>
```
```javascript
    //组件的定义 Vue.component(组件名称, {template})
    Vue.component('global-component', {
        template: '<h1>全局组件</h1>'
    })

    var vm = new Vue({
        el: '#app'
    })
```
最终渲染的效果
```html
    <div id="app">
        <h1>全局组件</h1>
    </div>
```
### 局部组件
```html
    <div id="app">
        <!--组件的使用-->
        <private-component></private-component>
    </div>
```
```javascript
    //组件的定义 Vue.component(组件名称, {template})
    var vm = new Vue({
        el: '#app',
        components:{
            'private-component': {
                template: '<h1>局部组件</h1>'
            }
        }
    })
```
最终渲染的效果
```html
    <div id="app">
        <h1>局部组件</h1>
    </div>
```
### 组件是一个单独的作用域
每个组件都有单独的作用域
```html
    <div id="app">
        <p>{{count}}</p>
        <component1/>
    </div>    
```
```javascript
    var vm = new Vue({
        el: '#app',
        data: {
            count: 10
        },
        methods: {
            increment: function(){
                this.count += 1;
            }
        },
        components:{
            'component1': {
                template: '<button v-on:click="increment">{{ count }}</button>',
                data: function(){
                    //在组件里面 data 一定是 function 并返回一个对象
                    return {
                        count: 0
                    }
                },
                methods: {
                    increment: function(){
                        this.count += 1;
                    }
                }
            }
        }
    })
```
渲染结果为
```html
    <div id="app">
        <p>10</p>
        <!--
            此按钮每次点击都会自增 1，而 p 标签永远都是为 10
            原因为组件的作用域是单独的
        -->
        <button>0</button>
    </div>    
```
[效果预览](https://dk-lan.github.io/vue-erp/VueBasic/Component/component.html)

### 特殊的 HTML 结构中使用 is
比如在下拉列表（select）元素里面，子元素必须为 option，则在使用组件的时候用 is
```html
    <div id="app">
        <select>
            <option is="privateOption"></option>
        </select>
    </div>
```
```javascript
    var vm = new Vue({
        el: '#app',
        components: {
            'privateOption': {
                template: '<option value=1>1</otpion>'
            }
        }
    })
```
渲染结果
```html
    <div id="app">
        <select>
            <option value="1">1</option>
        </select>
    </div>
```

### 动态组件 - :is
```html
<div id="app" style="display: none;">
    <input type="button" value="changeLight" @click="changeLight" />
    <br/>
    <p :is="show"></p>
</div>

<script type="text/javascript">
    var vm = new Vue({
        el: '#app',
        data: {
            show: 'red',
        },
        methods:{
            changeLight: function(){
                this.show = this.show == 'red' ? 'green' : 'red';
            }
        },
        components: {
            red: {
                template: '<h1>Red</h1>'
            },
            green: {
                template: '<h1>Green</h1>'
            }
        }
    })
</script>
```

### 组件属性
组件的属性要先声明后使用，props: ['属性名'...]
```html
    <div id="app">
        <!--组件的使用-->
        <private-component title="组件属性" :text="mess"></private-component>
    </div>
```
```javascript
    //组件的定义 Vue.component(组件名称, {template})
    var vm = new Vue({
        el: '#app',
        data: {
            mess: '-动态属性'
        }
        components:{
            'private-component': {
                template: '<h1>{{title + text}}</h1>',
                props: ['title', 'text']
            }
        }
    })
```
最终渲染的效果
```html
    <div id="app">
        <h1>组件属性-动态属性</h1>
    </div>
```
### 组件自定义事件
和组件属性不一样的在于 <组件名 v-bind:属性名="">，属性名要在组件中先声明再使用：props: ['属性名']
自定义事件：<组件名 v-on:自定义事件名="">，自定义事件名不需要声明，直接用 $emit() 触发
```html
    <div id="app">
        <p>{{total}}</p>
        <incrementTotal v-on:count="incrementTotal"></incrementTotal>
    </div>
```
```javascript
    var vm = new Vue({
        el: '#app',
        data: {
            total: 0
        },
        methods: {
            incrementTotal: function(){
                this.total += 1;
            }
        },
        components: {
            'incrementTotal': {
                template: '<input type="button" @click="incrementTotal" value="Total" />',
                data: function(){
                    return {
                        total: 0
                    }
                },
                methods: {
                    incrementTotal: function(){
                        this.total += 1;
                        this.$emit('count')
                    }
                }
            }
        }
    })
```

### slot 分发内容
Vue 组件默认是覆盖渲染，为了解决这一问题，Vue 提出了 slot 分发内容

```html
    <div id="app">
        <component1>
            <h1>Sam</h1>
            <h1>Lucy</h1>
        </component1>
    </div>
```
```javascript
    Vue.component('component1', {
        template: `
            <div>
                <h1>Tom</h1>
                <slot></slot>
            </div>
        `
    })
```
最终渲染的效果
```html
    <div id="app">
        <component1>
            <h1>Tom</h1>
            <!--
                如果在组件定义的 template 当中没有 <slot></slot>，那么下面两个 h1 标签将不会存在
                换句话说就是 <slot></slot> = <h1>Sam</h1><h1>Lucy</h1>
                <slot></slot>可以放到 <h1>Tom</h1> 上面进行位置调换
            -->
            <h1>Sam</h1>
            <h1>Lucy</h1>
        </component1>
    </div>
```

### 具名 slot
如果要将组件里面不同的子元素放到不同的地方，那就为子元素加上一个属性 slot="名称"，然后在组件定义的时候用名称对应位置 <slot name="名称"></slot>，其它没有 slot 属性的子元素将统一分发到 <slot></slot> 里面
```html
    <div id="app">
        <component1>
            <h1>Sam</h1>
            <h1 slot="lucy">Lucy</h1>
        </component1>
    </div>
```
```javascript
    Vue.component('component1', {
        template: `
            <div>
                <slot name="lucy"></slot>
                <h1>Tom</h1>
                <slot></slot>
            </div>
        `
    })
```
最终渲染的效果
```html
    <div id="app">
        <component1>
            <!--<slot name="lucy"></slot> = <h1 slot="lucy">Lucy</h1>-->
            <h1>Lucy</h1>
            <h1>Tom</h1>
            <!--其它没有 slot 属性的子元素将全部分发到 <slot></slot> 标签-->
            <h1>Sam</h1>
        </component1>
    </div>
```

## 模版写法
```html
	<template id="component1">
		<div>
		    <input type="text" v-model="name"/>
		    <p>{{name}}</p>			
		</div>
	</template>

	<div id="app">
		<component1/>
	</div>     
```
```javascript
    var vm = new Vue({
        el: '#app',
        components: {
            'component1': {
                template: '#component1',
                data: function(){
                    return {name: 'Tom'};
                }
            }
        }
    })
```

# 自定义指令
在某些情况下，Vue 提供给我们的指令是非常不够的，比如有一个输入框需要在点击的时候能弹出日期控件，这个功能 Vue 并不支持，但 Vue 支持我们自定义指令。

自定义指令和定义组件的方式很类式，也是有全局指令和局部指令之分

## 全局指令
```html
    <div id="app">
        <!--在这里使用了一个自定义的指令 v-global-->
        <input type="text" value="" v-global />
    </div>
```
```javascript
    // 注册一个全局自定义指令 v-global，指令名称不用加前缀 v-
    // 当元素使用了 v-global 这个指令时会触发对应的 function
    // 参数 element：使用指令的元素
    Vue.directive('global',  function(element){
        //默认触发钩子函数的 inserted
        element.value = "世界和平";
        element.focus();
    })

    var vm = new Vue({
        el: '#app'
    })
```

## 局部指令
```html
    <div id="app">
        <!--在这里使用了一个自定义的指令 v-private-->
        <input type="text" value="" v-private />
    </div>
```
```javascript
    var vm = new Vue({
        el: '#app',
        directives: {
            //注册一个局布指令 private，指令名称不用加前缀 v-
            // 当元素使用了 v-private 这个指令时会触发对应的 function
            // 参数 element：使用指令的元素
            private: function(element){
                element.style.background = '#ccc';
                element.value = "世界和平";
            }
        }
    })
```
[效果预览](https://dk-lan.github.io/vue-erp/VueBasic/Directive/directive.html)

## 钩子函数
钩子函数可以理解成是指令的生命周期
```html
    <div id="app">
        <!--在这里使用了一个自定义的指令 v-global-->
        <input type="text" v-model="text" v-demo="{color:'red'}">
    </div>
```
```javascript
    Vue.directive('demo', {
        //被绑定元素插入父节点时调用
        //默认触发此方法 Vue.directive('demo',  function(element){})
        //后于 bind 触发 
        //参数 element： 使用指令的元素
        //参数 binding： 使用指令的属性对象
        //参数 vnode： 整个 Vue 实例
        inserted: function(element, binding, vnode){
            console.log('inserted');
        },
        //只调用一次，指令第一次绑定到元素时调用，
        //用这个钩子函数可以定义一个在绑定时执行一次的初始化动作
        //先于 inserted 触发
        bind: function(element, binding, vnode){
            console.log('bind');
            element.style.color = binding.value.color
        },
        //被绑定元素所在的模板更新时调用，而不论绑定值是否变化
        update: function(element, binding, vnode){
            console.log('update');
        },
        //被绑定元素所在模板完成一次更新周期时调用。
        componentUpdated: function(element, binding, vnode){
            console.log('componentUpdated');
        }
    })

    var vm = new Vue({
        el: '#app',
        data:{
            text: '钩子函数'
        }
    })
```
[效果预览](https://dk-lan.github.io/vue-erp/VueBasic/Directive/hook.html)

## 案例：自定义日期控件
这里用 boostrap 的 datepicker 插件实现
```html
    <div id="app">
        <!--直接在 jQuery 环境下使用 datepicker 插件-->
        <input type="text" id="datepicker" data-date-format="yyyy-mm-dd"/>
        <!--使用 Vue 自定义指令 v-datepicker-->
        <input type="text" v-datepicker data-date-format="yyyy-mm-dd"/>
        <input type="button" value="保存" @click="save">
        <span>{{dataform.birthday}}</span>
    </div>
```
```javascript
    //在没有使用 Vue 前，datepicker 插件在 jQuery 的环境下是这样使用
    $('#datepicker').datepicker();

    //使用 Vue 自定义指令 v-datepicker
    Vue.directive('datepicker', function(element, binding, vnode){
        // data = dataform.birthday
        $(element).datepicker({
            language: 'zh-CN',
            pickTime: false,
            todayBtn: true,
            autoclose: true
        }).on('changeDate', function(){
            //由于不是手动在 input 输入值，所以双向绑定 v-model 无效
            //所以需要手动改变实例的数据模型
            var data = $(element).data('model');
            if(data){
                // datas = ['dataform', 'birthday']
                var datas = data.split('.');
                //context = vm
                var context = vnode.context;
                //循环属性自动添加
                datas.map((ele, idx) => {
                    //最后一个属性就直接赋值
                    if(idx == datas.length - 1){
                        context[ele] = element.value
                    } else {
                        //动态添加属性
                        context = context[ele]
                    }
                })
            }
        })
    })

    var vm = new Vue({
        el: '#app',
        data: {
            dataform: {}
        },
        methods: {
            save: function(){
                //使用 $set 更新 dataform
                //更多 $set 的使用在下面继续介绍
                this.$set(this.dataform)
            }
        }
    })
```

## $set 介绍
当实例对象 data 先设置好了结构，比如：data: {dataform: {}}，在后期想添加一个属性 username 时，这个 username 不会自动绑定到视图当中，所以调用 $set(原对象，新属性名，属性值) 进行绑定到视图当中
```html
    <div id="app">
        <input type="button" value="set" @click="set">
        <span>{{dataform.username}}</span>
    </div>
```
```javascript
    var vm = new Vue({
        el: '#app',
        data: {
            dataform: {}
        },
        methods: {
            set: function(){
                // 直接赋值不会显示在视图上
                // this.dataform.username = '123'
                //改用 $set 更新可以在视图上显示
                this.$set(this.dataform, 'username', '123')
            }
        }
    })
```
[效果预览](https://dk-lan.github.io/vue-erp/VueBasic/Directive/datepicker.html)

# 过渡效果

SPA 中组件的切换有一种生硬的隐藏显示感觉，为了更好的用户体验，让组件切换时淡出淡入，Vue 提供了专门的组件 transition。

## 过滤效果应用场景
- 条件渲染 (使用 v-if)
- 条件展示 (使用 v-show)
- 动态组件
- 组件根节点

## 过渡状态
- enter：定义进入过渡的开始状态。在元素被插入时生效。
- endter-active：定义过渡的状态。在元素整个过渡过程中作用，在元素被插入时生效。
- enter-to: 2.1.8版及以上 定义进入过渡的结束状态。
- leave：定义离开过渡的开始状态。在离开过渡被触发时生效。
- leave-active：定义过渡的状态。在元素整个过渡过程中作用，在离开过渡被触发后立即生效。
- leave-to: 2.1.8版及以上 定义离开过渡的结束状态。

每个状态在使用的时候都是在 CSS 中使用，结合组件 transition 的 name 属性。如 `<transition name="fade"></transition>`，对应的是 `fade-` 加上每个状态：`fade-enter`。

## CSS 过渡
使用到组件 `transition` 的属性: `name`。
```html
<style type="text/css" media="screen">
    /*初始状态*/
    .fade-enter{opacity: 0;}
    /*已经准备就绪*/
    .fade-enter-active{transition: all .5s;}
    /*已经消失*/
    .fade-leave-active{opacity: 0; transition: all .5s;}
</style>

<div id="app">
    <input type="button" :value="show ? 'hide' : 'show'" @click="show = !show" />
    <br/>
    <transition name="fade">
        <img src="imgs/green.jpg" v-show="show" />
    </transition>
</div>

<script type="text/javascript">
    var vm = new Vue({
        el: '#app',
        data: {
            show: true
        }
    })
</script>
```
[效果预览](https://github.com/wscats/vue-erp/VueBasic/Transition/01-CSS过渡.html)

## CSS 动画
使用到组件 `transition` 的属性: `name`。
```html
<style type="text/css" media="screen">
    .fade-enter-active{animation: fade-in .5s;}

    .fade-leave-active{animation: fade-out .5s;}

    @keyframes fade-in{
        from{
            opacity: 0;
        }
        to{
            opacity: 1;
        }
    }

    @keyframes fade-out{
        from{opacity: 1;}
        to{opacity: 0;}
    }
</style>

<div id="app">
    <input type="button" :value="show ? 'hide' : 'show'" @click="show = !show" />
    <br/>
    <transition name="fade">
        <img src="imgs/green.jpg" v-if="show" />
    </transition>
</div>

<script type="text/javascript">
    var vm = new Vue({
        el: '#app',
        data: {
            show: true
        }
    })
</script>
```
[效果预览](https://github.com/wscats/vue-erp/VueBasic/Transition/02-CSS动画.html)

## 初始渲染的过滤
第一次加载时的过渡效果，使用到组件 `transition` 的属性: `appear` `appear-class` `appear-active-class`。
```html
<style type="text/css" media="screen">
    .fade-enter{opacity: 0;}
    .fade-enter-active{transition: all 3s;}
</style>

<div id="app">
    <transition appear appear-class="fade-enter" appear-active-class="fade-enter-active">
        <img src="imgs/green.jpg" />
    </transition>
</div>

<script type="text/javascript">
    var vm = new Vue({
        el: '#app'
    })
</script>
```
[效果预览](https://github.com/wscats/vue-erp/VueBasic/Transition/03-初始渲染的过渡.html)

## 多个元素的过河效果
同时生效的进入和离开的过渡不能满足所有要求，所以 Vue 提供了 过渡模式：
- in-out：新元素先进行过渡，完成之后当前元素过渡离开。
- out-in：当前元素先进行过渡，完成之后新元素过渡进入。

使用到组件 `transition` 的属性: `mode`。
```html
<style type="text/css" media="screen">
    .fade-enter{opacity: 0;}
    .fade-enter-active{transition: all .5s;}

    .fade-leave-active{opacity: 0; transition: all .5s;}
</style>

<div id="app">
    <fieldset>
        <legend><h3>mode = in-out</h3></legend>
        <div>
            <input type="button" :value="red ? 'green' : 'red'" @click="red = !red" />
            <br/>
            <transition name="fade" mode="in-out">  
                <img src="imgs/red.jpg" v-if="red" key="red"/>
                <img src="imgs/green.jpg" v-else key="green"/>
            </transition>           
        </div>
    </fieldset>
    <fieldset>
        <legend><h3>mode = out-in</h3></legend>
        <div>
            <input type="button" :value="flag == 1 ? 'green' : flag == 2 ? 'yellw' : 'red'" @click="flag = flag == 1 ? 2 : flag == 2 ? 3 : 1" />
            <br/>
            <transition name="fade" mode="out-in">  
                <img src="imgs/red.jpg" v-if="flag == 1" key="red"/>
                <img src="imgs/green.jpg" v-else-if="flag == 2" key="green"/>
                <img src="imgs/yellow.jpg" v-else key="yellow" />
            </transition>               
        </div>
    </fieldset> 
</div>

<script type="text/javascript">
    var vm = new Vue({
        el: '#app',
        data: {
            red: true,
            flag: 1
        }
    })
</script>
```
[效果预览](https://github.com/wscats/vue-erp/VueBasic/Transition/04-多个元素过渡.html)

## 列表(v-for)的过渡效果
v-for 生成列表过渡效果要使用组件 `transition-group`，组件提供属性 `tag` 表示该组件将会渲染成对应的 DOM 节点，其它的使用和 `transition` 一样。
```html
<style type="text/css" media="screen">
    *{list-style: none;}
    li{width: 300px; margin-bottom: 5px; padding: 10px 20px; background-color: #ccc;}

    .list-enter{opacity: 0; transform: translateX(300px);}
    .list-enter-active{transition: all .5s;}

    .list-leave-active{transition: all .5s; opacity: 0; transform: translateX(-300px);}
</style>

<div id="app">
    <p>
        <input type="button" value="AddItem" @click="addItem">
        <input type="button" value="RemoveItem" @click="removeItem">
    </p>
    <transition-group tag="ul" name="list">
        <li v-for="(item, index) in items" :key="item">Item {{index}}</li>
    </transition-group>
</div>

<script type="text/javascript">
    var vm = new Vue({
        el: '#app',
        data: {
            items: [1,2,3]
        },
        methods: {
            randomIndex: function(){
                return  parseInt(this.items.length * Math.random());
            },
            addItem: function(){
                this.items.splice(this.randomIndex(), 0, this.items.length + 1);
            },
            removeItem: function(){
                this.items.splice(this.randomIndex(), 1);
            }
        }
    })
</script>
```
[效果预览](https://github.com/wscats/vue-erp/VueBasic/Transition/06-列表的进入和离开过渡.html)

## 自定义过渡的类名
我们可以通过以下特性来自定义过渡类名：
- enter-class
- enter-active-class
- enter-to-class (2.1.8+)
- leave-class
- leave-active-class
- leave-to-class (2.1.8+)
他们的优先级高于普通的类名，这对于 Vue 的过渡系统和其他第三方 CSS 动画库，如 Animate.css 结合使用十分有用。
```html
<link rel="stylesheet" type="text/css" href="animate.css">

<div id="app">
    <button @click="show = !show">Toggle render</button>
    <transition enter-active-class="animated jello" leave-active-class="animated bounceOutRight">
        <div v-if="show"><img src="./imgs/green.jpg" /></div>
    </transition>
</div>

<script type="text/javascript">
    var vm = new Vue({
        el: '#app',
        data: {
            show: true
        }
    })
</script>
```
[效果预览](https://github.com/wscats/vue-erp/VueBasic/Transition/07-自定义过渡的类名.html)

## 过渡效果钩子函数
除了用CSS过渡的动画来实现vue的组件过渡，还可以用JavaScript的钩子函数来实现，在钩子函数中直接操作DOM。我们可以在属性中声明以下钩子：
```html
<transition
    v-on:before-enter="beforeEnter"
    v-on:enter="enter"
    v-on:after-enter="afterEnter"
    v-on:enter-cancelled="enterCancelled"
    v-on:before-leave="beforeLeave"
    v-on:leave="leave"
    v-on:after-leave="afterLeave"
    v-on:leave-cancelled="leaveCancelled"
>
</transition>
<script type="text/javascript">
    var vm = new Vue({
        el: '#app',
        methods: {
            // 过渡进入
            // 设置过渡进入之前的组件状态
            beforeEnter: function (el) {
            // ...
            },
            // 设置过渡进入完成时的组件状态
            enter: function (el, done) {
            // ...
            done()
            },
            // 设置过渡进入完成之后的组件状态
            afterEnter: function (el) {
            // ...
            },
            enterCancelled: function (el) {
            // ...
            },
            // 过渡离开
            // 设置过渡离开之前的组件状态
            beforeLeave: function (el) {
            // ...
            },
            // 设置过渡离开完成时地组件状态
            leave: function (el, done) {
            // ...
            done()
            },
            // 设置过渡离开完成之后的组件状态
            afterLeave: function (el) {
            // ...
            },
            // leaveCancelled 只用于 v-show 中
            leaveCancelled: function (el) {
            // ...
            }
        }
    })
</script>
```

# 路由
通过 URL 映射到对应的功能实现，Vue 的路由使用要先引入 vue-router.js

## 基本路由入门
定义 component
```javascript
    const Foo = { template: '<div><h1>Foo Content</h1></div>' };
    const Bar = { template: '<div><h1>Bar Content</h1></div>' };
```
定义路由规则
```javascript
    //每个路由应该映射一个组件。 其中"component" 可以是自定义的组件
    //当 url 为 http://localhost/index.html#/foo 页面会渲染组件 Foo
    //当 url 为 http://localhost/index.html#/bar 页面会渲染组件 Bar
    const routes = [
        {path: '/foo', component: Foo},
        {path: '/bar', component: Bar}
    ]
```
使用
```html
    <div id="app">
        <h1>Hello VueRouter</h1>
        <p>
			<!-- 使用 router-link 组件来导航. -->
			<!-- 通过传入 `to` 属性指定链接. -->
			<!-- <router-link> 默认会被渲染成一个 `<a>` 标签 -->
            <!-- 属性 `to` 对应生成  `<a>` 标签的 `href` 属性-->
            <router-link to="/foo">Foo</router-link>
            <router-link to="/bar">Bar</router-link>
        </p>
        <!--路由匹配的组件在此处渲染-->
        <router-view></router-view>
    </div>
```
```javascript
    const router = new VueRouter({
        routes // （缩写）相当于 routes: routes
    })

    new Vue({
        router
    }).$mount('#app')
```
[效果预览](https://github.com/wscats/vue-erp/VueBasic/Router/router.html)

## 路由参数
```html
    <div id="app">
        <h1>Hello VueRouter</h1>
        <p>
            <router-link to="/user/1">User1</router-link>
            <router-link to="/user/2">User2</router-link>
        </p>
        <router-view></router-view>
    </div>
```
通过对象 $route.params 来获取参数
```javascript
    const User = { template: '<div><h1>{{$route.params.userid}}</h1></div>' };

    const routes = [
        {path: '/user/:userid', component: User}
    ]    

    const router = new VueRouter({
        routes
    })

    new Vue({
        router
    }).$mount('#app')
```
[效果预览](https://github.com/wscats/vue-erp/VueBasic/Router/params.html)

## 嵌套路由
```html
    <div id="app">
        <h1>Hello VueRouter</h1>
        <p>
            <router-link to="/floor1">一楼</router-link>
        </p>
        <router-view></router-view>
    </div>
```
```javascript
    const Floor1 = { 
        template: `
            <div>
                <h1>一楼</h1>
                <router-link to="/floor1/floor2">二楼</router-link>
                <router-view></router-view>
            </div>` 
    };
    const Floor2 = { template: '<div><h1>二楼</h1></div>' };    

    const routes = [
        {
            path: '/floor1',
            component: Floor1,
            children: [{
                // floor2 会被渲染在 Floor1 的 <router-view> 中
                path: 'floor2',
                component: Floor2
            }]
        }
    ]    

    const router = new VueRouter({
        routes
    })

    new Vue({
        router
    }).$mount('#app')
```
[效果预览](https://github.com/wscats/vue-erp/VueBasic/Router/routerChildren.html)

## 编程式导航
用 javascript 跳转路由
```html
    <div id="app">
        <h1>Hello VueRouter</h1>
        <p>
            <!--用 `router-link` 组件进行跳转-->
            <router-link to="/floor1">一楼</router-link>
            <!--编程式导航1：router.replace-->
            <input type="button" value="一楼" @click="router.replace('/floor1')">
            <!--编程式导航2：router.push()-->
            <input type="button" value="一楼" @click="router.push('/floor1')">
            <!--编程式导航3：router.push({})-->
            <input type="button" value="一楼" @click="router.push({path: '/floor1'})">            
        </p>
        <router-view></router-view>
    </div>
```

## 命名路由
在路由映射表中添加属性 name，用以对该路由映射规则命名，在编程式导航跳转路由时可以用 router.push({name: '名称'})
```html
    <div id="app">
        <h1>Hello VueRouter</h1>
        <p>
            <router-link to="/floor1">一楼</router-link>
            <!--编程式导航4：router.push({name: '名称'})-->
            <input type="button" value="一楼" @click="router.push({name: 'floor1'})">             
        </p>
        <router-view></router-view>
    </div>
```
```javascript
    const Floor1 = { 
        template: `
            <div>
                <h1>一楼</h1>
                <router-link to="/floor1/floor2">二楼</router-link>
                <router-view></router-view>
            </div>` 
    };
    const Floor2 = { template: '<div><h1>二楼</h1></div>' };    

    const routes = [
        {
            path: '/floor1',
            component: Floor1,
            name: 'floor1', //命名
            children: [{
                // floor2 会被渲染在 Floor1 的 <router-view> 中
                path: 'floor2',
                component: Floor2,
                name: 'floor2' //命名
            }]
        }
    ]    

    const router = new VueRouter({
        routes
    })

    new Vue({
        router
    }).$mount('#app')
```

## 命名视图
```html
    <div id="app">
        <h1>Hello VueRouter</h1>
        <p>
            <router-view></router-view>
            <router-view name="a"></router-view>
            <router-view name="b"></router-view>
        </p>
    </div>
```
```javascript
    const router = new VueRouter({
        routes: [
            {
                path: '/',
                components: {
                    default: {
                        template: '<h1>defalut router view</h1>'
                    },
                    a: {
                        template: '<h1>a router view</h1>'
                    },
                    b: {
                        template: '<h1>b router view</h1>'
                    }
                }
            }
        ]
    })

    new Vue({
        el: '#app',
        router
    })
```