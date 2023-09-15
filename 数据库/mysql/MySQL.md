> 数据库操作语言可以分为四种分别是DML,DCL,DDL

1. DML：增加，修改，删除，查询
2. DCL：用户，权限，事务
3. DDL：逻辑库，数据表，视图，索引

注意：

1. SQL语句不区分大小写，但是字符串区分大小写
```sql
SELECT "HelloWorld";
```

2. SQL语句必须以分号结尾
3. SQL语句中的空白和换行没有限制，但是不能破坏语法
# DML
## 数据库
### 创建数据库
```sql
# 创建数据库
CREATE DATABASE 库名;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672724252489-782b3bae-b791-46b6-b130-827f5ccea105.png#averageHue=%23f7f6f5&clientId=u892ca0d1-2d02-4&from=paste&height=400&id=uf239ab17&originHeight=400&originWidth=915&originalType=binary&ratio=1&rotation=0&showTitle=false&size=40900&status=done&style=none&taskId=ub3a6b560-73db-467e-a25f-3bd1d92f2de&title=&width=915)
### 删除数据库
```sql
# 删除数据库
DROP DATABASE 库名;
```

![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672724300268-c5203dfc-012f-48ad-aedb-d6f8f162568c.png#averageHue=%23f7f6f5&clientId=u892ca0d1-2d02-4&from=paste&height=391&id=u7a90a4ee&originHeight=391&originWidth=805&originalType=binary&ratio=1&rotation=0&showTitle=false&size=40510&status=done&style=none&taskId=ue5326053-c178-4fcc-8750-df30e17842f&title=&width=805)
## 数据库表表
### 创建表
```sql
CREATE TABLE 表名(
字段名 属性 (约束) ,
字段名 属性 (约束) ,
字段名 属性 (约束) ,
...
);
```
> 图中的INT ，VARCHAR(20)表示数据类型，PRIMARY KEY，NOT NULL表示约束


![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672724819679-51ad4668-98dd-4419-b7ec-5666158e3786.png#averageHue=%23fbfbfa&clientId=u892ca0d1-2d02-4&from=paste&height=1106&id=u946e8a50&originHeight=1106&originWidth=1031&originalType=binary&ratio=1&rotation=0&showTitle=false&size=87296&status=done&style=none&taskId=uada5511a-d13d-49e7-8b49-a31cfbdd2ad&title=&width=1031)
### 显示表结构，详情，建表语句，删除表
```sql
# 显示表结构
SHOW tables;

# 显示表详情
DESC 表名;

# 显示建表语句
SHOW CREATE TABLE 表名;

# 删除表
DROP TABLE 表名;
```

![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672725078491-fcd45c3a-4c78-4807-8b04-5ff12b8a787e.png#averageHue=%23fcfcfb&clientId=u892ca0d1-2d02-4&from=paste&height=598&id=ua21cb3df&originHeight=598&originWidth=1674&originalType=binary&ratio=1&rotation=0&showTitle=false&size=69676&status=done&style=none&taskId=u5bc49038-1b52-45f8-af0d-2430d361cf5&title=&width=1674)<br />![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672725085117-628052f7-706b-4643-95f1-1dc66c7b1a6f.png#averageHue=%23fcfbfb&clientId=u892ca0d1-2d02-4&from=paste&height=783&id=u0363d84a&originHeight=783&originWidth=685&originalType=binary&ratio=1&rotation=0&showTitle=false&size=136596&status=done&style=none&taskId=u0a874b87-5deb-4135-ae87-1b525230e29&title=&width=685)
### 为已经创建的表添加字段
> 需要修改表的时候都是使用ALTER TABLE

```sql
# 为已经创建的表添加字段alter改变表
ALTER TABLE 表名
ADD 字段名 数据类型 约束,
ADD 字段名 数据类型 约束,
...;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672725365329-93340948-281e-4b63-b56b-1da7bf0ca90f.png#averageHue=%23fcfbfb&clientId=u892ca0d1-2d02-4&from=paste&height=944&id=u95387580&originHeight=944&originWidth=681&originalType=binary&ratio=1&rotation=0&showTitle=false&size=182270&status=done&style=none&taskId=u15d235f4-a411-4a9a-be7c-21abb6fa83e&title=&width=681)
### 修改表字段属性等
```sql
ALTER TABLE student
MODIFY 要修改的字段名 属性 [约束];
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672725737586-0e4c0827-e92a-4ce5-8072-cf0fd6f53e1f.png#averageHue=%23fbfaf9&clientId=u892ca0d1-2d02-4&from=paste&height=404&id=u59fa33e3&originHeight=404&originWidth=840&originalType=binary&ratio=1&rotation=0&showTitle=false&size=36641&status=done&style=none&taskId=u893ca371-08b1-4132-8422-53bcd97a56b&title=&width=840)
### 修改表字段名
```sql
ALTER TABLE student
CHANGE 修前字段名 修改后的名称 VARCHAR(20) NOT NULL DEFAULT("1") COMMENT '年级';
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672725831628-0d4ea3a1-f28c-4431-bd4c-78661e303b83.png#averageHue=%23faf9f8&clientId=u892ca0d1-2d02-4&from=paste&height=359&id=u7418ec44&originHeight=359&originWidth=752&originalType=binary&ratio=1&rotation=0&showTitle=false&size=29175&status=done&style=none&taskId=u3a8855f9-2831-4f7b-8cf5-ce2e0e9999d&title=&width=752)
### 删除字段
```sql
ALTER TABLE 表名
DROP 字段名,
DROP 字段名;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672725904856-cdc41ba1-c12b-4668-869b-e18c767dfd6f.png#averageHue=%23fbfaf9&clientId=u892ca0d1-2d02-4&from=paste&height=349&id=ud6b5154a&originHeight=349&originWidth=738&originalType=binary&ratio=1&rotation=0&showTitle=false&size=22417&status=done&style=none&taskId=ud4201ca3-bdef-43f9-8912-6bf57e10d69&title=&width=738)

### 添加字段约束
> 字段约束可以约束字段的内容大小，是否为空，是否为正等

#### 创建表时添加约束
PRIMARY KEY（主键约束） ,NOT NULL（非空约束）,UNSIGNED（无符号约束）,UNIQUE（唯一约束）
```sql
CREATE TABLE t_dept(
id INT UNSIGNED PRIMARY KEY COMMENT '主键id无符号int类型数据，主键约束',
deptno VARCHAR(20) NOT NULL COMMENT '非空约束',
dname VARCHAR(20) UNIQUE COMMENT '唯一约束'
);
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672726379497-f1d68845-60dd-47f9-9a6b-0b00ecf3038d.png#averageHue=%23fbfafa&clientId=u892ca0d1-2d02-4&from=paste&height=406&id=u9acdda48&originHeight=406&originWidth=786&originalType=binary&ratio=1&rotation=0&showTitle=false&size=34820&status=done&style=none&taskId=uf72173b2-43e8-4783-a103-c5402af3b07&title=&width=786)
#### 创建表后添加约束
```sql
ALTER TABLE student
MODIFY age INT UNSIGNED NOT NULL;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672726594637-cd95e3b9-9f75-4552-a7cd-75eafc405872.png#averageHue=%23faf9f8&clientId=u892ca0d1-2d02-4&from=paste&height=383&id=ue078682a&originHeight=383&originWidth=715&originalType=binary&ratio=1&rotation=0&showTitle=false&size=28742&status=done&style=none&taskId=uaa5b9bc5-6b7a-4e6f-b095-770e6be5a02&title=&width=715)
### 添加外键约束
> 外键约束时建立在两个表的基础上，且两个表的属性有属于关系，比如员工属于一个部门，说明这个部门必须存在，所以就存在了外键约束。外键约束存在，如果员工与所在的本需要删除，但是因为外加约束的存在，删除不了，会报错，只有删除了所有使用此条数据的员工才能删除。但是外键约束，我们通常不用，你想如果表多了，每个表都有一个外键约束，就可能形成一种现象就是每张表都有外键约束，那样数据就只可以增加不能删除，影响了数据的可操作性。

![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672726996568-ef2519a1-3828-4b69-9357-0178013780c8.png#averageHue=%23fbfbfa&clientId=u892ca0d1-2d02-4&from=paste&height=472&id=u7807a1ea&originHeight=472&originWidth=771&originalType=binary&ratio=1&rotation=0&showTitle=false&size=37531&status=done&style=none&taskId=u55f20817-75c7-4269-95fb-5b00a38e436&title=&width=771)<br />![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672727045465-bf06f290-d151-4649-9e64-50ca18d4d124.png#averageHue=%23f8f7f6&clientId=u892ca0d1-2d02-4&from=paste&height=308&id=uc59046f2&originHeight=308&originWidth=809&originalType=binary&ratio=1&rotation=0&showTitle=false&size=26435&status=done&style=none&taskId=u5eb314e9-512b-4449-a8ac-202272d2b55&title=&width=809)
### 数据库自动更新字段
在创建时赋予属性字段的时候
```sql
DEFAULT CURRENT_TIMESTAMP
```
<br />表示当插入数据的时候，该字段默认值为当前时间
```sql
ON UPDATE CURRENT_TIMESTAMP
```
<br />表示每次更新这条数据的时候，该字段都会更新成当前时间<br />这两个操作是mysql数据库本身在维护，所以可以根据这个特性来生成【创建时间】和【更新时间】两个字段，且不需要代码来维护
```sql
CREATE TABLE `content` (
    `content` varchar(255) DEFAULT '' COMMENT '内容',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
navicat可视化界面可以看到<br />![](https://cdn.nlark.com/yuque/0/2022/png/25418009/1672456779453-09b21dd2-b10d-4c29-909d-5ba28c00f989.png#averageHue=%23f9eeed&clientId=u4c278cf7-ac1a-4&from=paste&id=apLdH&originHeight=108&originWidth=540&originalType=url&ratio=1&rotation=0&showTitle=false&status=done&style=none&taskId=uf1cd2737-edbb-4544-93bb-aa1bd0d73ce&title=)<br />设置具体的默认时间
```sql
TIMESTAMP DEFAULT 'yyyy-mm-dd hh:mm:ss'
```
## 数据查询
> 最基本的查询语句是由 SELECT 和 FROM 关键字组成的。SELECT 显示的字段名称 FROM 是选择查询的表 
> 接下来我们会对下表进行查询操作

![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677226444285-e2969520-1cd0-477f-a2aa-c9bd3e66d76e.png#averageHue=%23f6f5f3&clientId=u13f03d49-e217-4&from=paste&height=385&id=u85d3e554&originHeight=385&originWidth=584&originalType=binary&ratio=1&rotation=0&showTitle=false&size=27067&status=done&style=none&taskId=ufac4aa28-827f-4760-b67b-ca1651f9316&title=&width=584)
### 基本查询（指定字段显示，起别名，分页）

1. 对整张表进行查询不做任何操作
```sql
SELECT * FROM `student` ;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677226540761-55520f40-347a-4d7e-8492-ae560cf659ac.png#averageHue=%23f6f5f3&clientId=u13f03d49-e217-4&from=paste&height=386&id=u25ee67b2&originHeight=386&originWidth=644&originalType=binary&ratio=1&rotation=0&showTitle=false&size=28338&status=done&style=none&taskId=ub623e7d2-a47e-42d3-bf5b-f41b83122e0&title=&width=644)

2. 查询并选择显示的字段
```sql
SELECT name, sex ,age FROM `student` ;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677226612006-24b01d51-94dc-402e-bdaf-ea97bfac5c77.png#averageHue=%23f7f6f4&clientId=u13f03d49-e217-4&from=paste&height=279&id=ud95e7cc7&originHeight=279&originWidth=335&originalType=binary&ratio=1&rotation=0&showTitle=false&size=12551&status=done&style=none&taskId=u697e3de2-ac6b-4b39-8fc6-2f020b84a39&title=&width=335)

3. 指定字段显示并使用 AS 起别名
```sql
SELECT name AS 姓名, sex AS 性别,age AS 年纪 FROM `student` ;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677226733913-7ebfc857-984e-461a-a6fd-49b4137ac9dd.png#averageHue=%23f6f5f3&clientId=u13f03d49-e217-4&from=paste&height=249&id=ue665b102&originHeight=249&originWidth=309&originalType=binary&ratio=1&rotation=0&showTitle=false&size=11227&status=done&style=none&taskId=ubc31bedc-b6c1-421f-b71b-6a6696dd15d&title=&width=309)

4. 数据分页查询 LIMIT 起始位置，偏移量 
```sql
SELECT name AS 姓名, sex AS 性别,age AS 年纪 FROM `student` LIMIT 0,5;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677226775911-ce15e865-b8b2-4069-ba43-126a0ff59740.png#averageHue=%23f7f6f5&clientId=u13f03d49-e217-4&from=paste&height=233&id=u3600db11&originHeight=233&originWidth=345&originalType=binary&ratio=1&rotation=0&showTitle=false&size=10212&status=done&style=none&taskId=u4bec205c-25c8-4976-9443-1dfa84491a5&title=&width=345)
> 数据库分页简写起始位置为0可以不写 与上一条语句作用一样

```sql
SELECT name AS na, sex ,age FROM `student` LIMIT 5;
```
#### 排序（ORDER BY）
> 1. 排序 ORDER BY 关键字   ASC升学（默认值） DESC降序
> 2. 如果排序列是**数字类型**，数据库就**按照数字大小排序**，如果是**日期类型**就**按照日期大小排序**，如果是**字符串**就**按照字符集序号排序**

```sql
SELECT name AS na, sex ,age FROM `student` ORDER BY age ASC LIMIT 5 ;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677227057377-8b4862b8-8796-4c9e-82fc-493576331d3b.png#averageHue=%23f8f7f6&clientId=u13f03d49-e217-4&from=paste&height=201&id=u30783c10&originHeight=201&originWidth=353&originalType=binary&ratio=1&rotation=0&showTitle=false&size=8603&status=done&style=none&taskId=u3c9d1dd7-3585-4632-af15-eb6b14a706f&title=&width=353)
> 我们可以使用ORDER BY规定首要排序条件和次要排序条件。数据库会先按照首要排序条件排序，如果遇到首要排序内容相同的记录那么就会启用次要排序条件接着排序。
> 
> 下面代码会在年纪一样的时候，根据姓名进行升序排序

```sql
SELECT * FROM `student` ORDER BY age ASC, name ;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677227123939-4a453eca-2c0d-4c90-8338-19437ff70f83.png#averageHue=%23f7f5f3&clientId=u13f03d49-e217-4&from=paste&height=379&id=u095f64ae&originHeight=379&originWidth=601&originalType=binary&ratio=1&rotation=0&showTitle=false&size=27239&status=done&style=none&taskId=uc53b6862-342d-40df-b1d2-1b2d75729f3&title=&width=601)
#### 去除重复数据 （DISTINCT）
他会查到学生表里不同的年纪
```sql
 SELECT  DISTINCT age FROM student;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677227283647-93274955-c996-4ab6-b414-aa7d2e0f3641.png#averageHue=%23fbfbfa&clientId=u13f03d49-e217-4&from=paste&height=196&id=u386c3924&originHeight=196&originWidth=224&originalType=binary&ratio=1&rotation=0&showTitle=false&size=3574&status=done&style=none&taskId=ueaaeffa0-2989-46ff-b115-e62cdd6c10b&title=&width=224)<br /> 注意 DISTINCT 关键字这只能查询一列重复数据，如果查询多列就失效了
```sql
-- 	DISTINCT 关键字只能在 SELECT 子句中使用一次
--  SELECT sex, DISTINCT age FROM student; 无法执行
--  SELECT DISTINCT sex, DISTINCT age FROM student; 无法执行
```
### 条件查询
> 很多时候，用户感兴趣的并不是逻辑表里的全部记录，而只是它们当中能够满足某一种或某几种条件的记录，这类条件要用 **WHERE **子句来实现数据的筛选

年纪大于18的学生
```sql
SELECT * FROM student WHERE age>18;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677227540817-ed651fc2-f6fc-426f-9977-4cc5415b2d87.png#averageHue=%23f6f4f3&clientId=u13f03d49-e217-4&from=paste&height=386&id=u3a5c81f3&originHeight=386&originWidth=644&originalType=binary&ratio=1&rotation=0&showTitle=false&size=29707&status=done&style=none&taskId=uaeeefee2-fd0e-4da4-b6ee-9565df3fa2e&title=&width=644)
#### IFNULL(空字符替换)
>  IFNULL(字段名, 替换值) 下面代码意思如果 age 字段中有null值或是为空就会将查到的数据替换成0

```sql
SELECT 10*IFNULL(null,0);
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677227807198-4fd91daf-535b-46d5-b22d-7e19de15c4d3.png#averageHue=%23fbfaf9&clientId=u13f03d49-e217-4&from=paste&height=98&id=ufda77ae8&originHeight=98&originWidth=225&originalType=binary&ratio=1&rotation=0&showTitle=false&size=1717&status=done&style=none&taskId=u3b9cd8eb-0644-4a84-aae0-e9e69881c54&title=&width=225)
#### DATEDIFF(比较时间差)
> -- 比较前者时间与后者时间的相差天数

```sql
SELECT DATEDIFF(NOW(),'2021-05-18 07:58:35')
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677227922327-291eae75-b138-402d-96b7-5bc6fb1bb1bc.png#averageHue=%23f9f8f7&clientId=u13f03d49-e217-4&from=paste&height=119&id=u02367867&originHeight=119&originWidth=322&originalType=binary&ratio=1&rotation=0&showTitle=false&size=3829&status=done&style=none&taskId=u1eae3716-be08-4575-b266-d750773778a&title=&width=322)
#### 逻辑查询
> 1. 逻辑运算符 AND(与，age>10 AND sex='男') OR(或,age>10 OR sex='男') NOT(非，NOT age>10) XOR(异或，age>10 XOR sex='男')
> 
> 2. 异或解释： a XOR b 当a b两值不同时为 true ，或同时为 false 时返回true

![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677227642827-9d53770b-57c3-41e4-a537-daa79d93957b.png#averageHue=%23dbdbdb&clientId=u13f03d49-e217-4&from=paste&height=352&id=uea3a18b9&originHeight=352&originWidth=1190&originalType=binary&ratio=1&rotation=0&showTitle=false&size=112016&status=done&style=none&taskId=u8ca6aa2f-6c5b-4747-97bb-95aa585812c&title=&width=1190)<br />年纪大于18的男生
```sql
SELECT * FROM student WHERE age>18 AND sex='男';
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677227583621-b913bc8d-7817-4bf3-b958-0497274c9fd0.png#averageHue=%23f8f7f5&clientId=u13f03d49-e217-4&from=paste&height=247&id=u42ddebfd&originHeight=247&originWidth=626&originalType=binary&ratio=1&rotation=0&showTitle=false&size=15395&status=done&style=none&taskId=ua3bc2077-dd05-4a3e-afe8-fb93b1f15fc&title=&width=626)<br />查询结果是小与或等于18的男生，和大于18的女生
```sql
SELECT * FROM student WHERE age>18 XOR sex='男';
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677228195940-be40efc7-112c-4a54-aca6-311c9434f71f.png#averageHue=%23f8f7f5&clientId=u13f03d49-e217-4&from=paste&height=350&id=u0e882230&originHeight=350&originWidth=654&originalType=binary&ratio=1&rotation=0&showTitle=false&size=24335&status=done&style=none&taskId=ue42613cb-94bc-4db6-bdb6-db15651a1b3&title=&width=654)

#### 比较运算符
> -- 比较运算符 >大于 <小于 >=大于等于 <=小于等于 =等于 !=不等于 IN包含

![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677227997327-a7a69c80-1c17-49cb-8734-8cac310fad9f.png#averageHue=%23dfdfdf&clientId=u13f03d49-e217-4&from=paste&height=630&id=u1e1165ae&originHeight=630&originWidth=1298&originalType=binary&ratio=1&rotation=0&showTitle=false&size=150397&status=done&style=none&taskId=ub385807a-328e-40c7-a39b-1e70f3cc21a&title=&width=1298)
```sql
SELECT * FROM student WHERE age!='18' AND  clazz IN ( '会计 ','幼师');
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677228055949-17ce7fa5-e960-47d8-9802-e94be6c7f22f.png#averageHue=%23f8f7f6&clientId=u13f03d49-e217-4&from=paste&height=192&id=ua9fb0335&originHeight=192&originWidth=582&originalType=binary&ratio=1&rotation=0&showTitle=false&size=12572&status=done&style=none&taskId=u990c8eed-2e39-45ec-9f0a-7e30308aad2&title=&width=582)
> -- 还有一些特殊的比较运算符 IS NULL(为空，count IS NULL)  IS NOT NULL(不为空 ，count IS NOT NULL) BETWEEN AND(范围 , count BETWEEN  200 AND 300)  LIKE(模糊查询, c_name LIKE "%A%") REGEXP(正则表达式查询 c_name REGEXP "[a-z]{4}")

![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677228089346-456be741-3415-435e-b66f-71f7bb9ebe6d.png#averageHue=%23dbdbdb&clientId=u13f03d49-e217-4&from=paste&height=521&id=u22d29d48&originHeight=521&originWidth=1496&originalType=binary&ratio=1&rotation=0&showTitle=false&size=180702&status=done&style=none&taskId=uc61e24e0-a902-4681-a9ef-165f306821b&title=&width=1496)
### 高级查询
> 聚合函数
> 聚合函数在数据的查询分析中，应用十分广泛。聚合函数可以对数据求和、求最大值和最小值、求平均值等等

#### AVG平均值
求男学生的平均年龄
```sql
SELECT AVG(IFNULL(age,18)) AS '平均年龄' FROM student WHERE sex='男';
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677228351021-f67f96a8-7e40-4791-9dbf-315a86ed8aea.png#averageHue=%23f9f8f7&clientId=u13f03d49-e217-4&from=paste&height=85&id=uc45ee23d&originHeight=85&originWidth=183&originalType=binary&ratio=1&rotation=0&showTitle=false&size=1896&status=done&style=none&taskId=ufcaace77-39ed-4f84-9e80-e06afc807a2&title=&width=183)
#### SUM求和
年纪求和
```sql
SELECT SUM(IFNULL(age,18)) AS '年纪和' FROM student ;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677228442476-2565933c-e168-4835-ba0c-de42b4eb52bc.png#averageHue=%23faf9f8&clientId=u13f03d49-e217-4&from=paste&height=91&id=uaa519575&originHeight=91&originWidth=156&originalType=binary&ratio=1&rotation=0&showTitle=false&size=1518&status=done&style=none&taskId=u81adb646-fc06-4e61-aa9c-3cc55374e78&title=&width=156)
#### MAX最大值
-- 年纪最大
```sql
SELECT MAX(IFNULL(age,18)) AS '最大年纪' FROM student ;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677228493307-d3e73097-6aa3-450a-8d1c-c5aad4aad657.png#averageHue=%23f5f5f3&clientId=u13f03d49-e217-4&from=paste&height=132&id=ua784a4e4&originHeight=132&originWidth=217&originalType=binary&ratio=1&rotation=0&showTitle=false&size=3691&status=done&style=none&taskId=ubbc33af1-b87f-4416-a2de-48ca89d7fa0&title=&width=217)
#### COUNT计数
> -- COUNT(*)用于获得包含空值的记录数，COUNT(列名)用于获得包含非空值的记录数。

 获取有多少个不同的年级
```sql
SELECT COUNT(DISTINCT grade) FROM student ;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677228571922-8c9a105e-3dcf-429c-bc9d-fe11b560fc62.png#averageHue=%23f7f6f5&clientId=u13f03d49-e217-4&from=paste&height=137&id=u909641b4&originHeight=137&originWidth=316&originalType=binary&ratio=1&rotation=0&showTitle=false&size=4842&status=done&style=none&taskId=u81ad954f-ea8a-4f1f-871c-8d957625150&title=&width=316)
####  GROUP BY 分组
> 1. 默认情况下汇总函数是对全表范围内的数据做统计GROUP BY子句的作用是通过一定的规则将一个数据集划分成若干个小的区域，然后针对每个小区域分别进行数据汇总处理
> 2. 查询语句中如果含有GROUP BY子句，那么SELECT-子句中的内容就必须要遵守规定：SELECT子句中可以包括聚合函数，或者GROUP BY子句的分组列（被 GROUP BY 用来分组的字段可以出现在select中），其余内容均不可以出现在SELECT子句中

对性别进行分组，并统计数量
```sql
SELECT sex,COUNT(sex) FROM student GROUP BY sex;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677228686378-a8193e1b-5fdf-473d-a943-d9f286f7744c.png#averageHue=%23faf9f8&clientId=u13f03d49-e217-4&from=paste&height=178&id=uc78f3613&originHeight=178&originWidth=336&originalType=binary&ratio=1&rotation=0&showTitle=false&size=5777&status=done&style=none&taskId=u6ea27817-d42e-455d-bf3a-e56a9fd1716&title=&width=336)<br />查询会计班的男生和女生的数量 
```sql
SELECT sex, COUNT(sex) FROM student WHERE clazz = '会计' GROUP BY sex ORDER BY sex;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677228716197-3f7fb900-9803-4717-abd5-5b70a2e7d3b4.png#averageHue=%23f2f1f1&clientId=u13f03d49-e217-4&from=paste&height=177&id=u8943ea0f&originHeight=177&originWidth=312&originalType=binary&ratio=1&rotation=0&showTitle=false&size=5537&status=done&style=none&taskId=u74c8ba06-b23f-4ce0-935f-d8d900ccb25&title=&width=312)
>  GROUP_CONCAT 函数可以把分组查询中的某个字段拼接成一个字符串 

```sql
SELECT clazz , GROUP_CONCAT(name) FROM student  GROUP BY clazz ;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677228953715-4c9ffd07-0a33-48c0-824c-36dc61ce8879.png#averageHue=%23f1eeeb&clientId=u13f03d49-e217-4&from=paste&height=187&id=u26cfba0a&originHeight=187&originWidth=396&originalType=binary&ratio=1&rotation=0&showTitle=false&size=14366&status=done&style=none&taskId=ue4e406d5-0717-46d9-a99c-b2b49065832&title=&width=396)
> GROUP BY 搭档 HAVING , HAVING 是在 GROUP BY 之后执行筛选.

筛选班级人数大于4人的班级
```sql
SELECT clazz AS '班级' , COUNT(name) AS '班级人数',GROUP_CONCAT(name) AS '班级人员姓名' FROM student GROUP BY clazz HAVING COUNT(name)>4;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677229022897-3011460f-cb2d-48da-8a88-a92e34377e47.png#averageHue=%23f7f5f4&clientId=u13f03d49-e217-4&from=paste&height=126&id=u549d14c5&originHeight=126&originWidth=591&originalType=binary&ratio=1&rotation=0&showTitle=false&size=8926&status=done&style=none&taskId=u320bbac3-3049-4cfa-b756-fdb8ed1bdcf&title=&width=591)
> 没用小技巧 GROUP BY 可以传数字,它会根据这个数字的对应位子的字段进行分组 , 此时 HAVING 也是可以使用的

```sql
SELECT * ,COUNT(*) FROM student  GROUP BY 5 HAVING COUNT(*) > 4;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677229057808-38438219-7ca7-4196-b305-fc49dbf1fa58.png#averageHue=%23faf9f8&clientId=u13f03d49-e217-4&from=paste&height=132&id=ub462efe2&originHeight=132&originWidth=814&originalType=binary&ratio=1&rotation=0&showTitle=false&size=9390&status=done&style=none&taskId=u0ef1fd09-af77-4305-a71f-5486b53f045&title=&width=814)
### 多表查询
> 多表查询分为: 内连接,外连接(左连接,右连接)
> 外连接分为:左连接,右连接

使用的表<br />![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677229126924-5c5c53e3-ab72-4ccc-a6fc-78a707829726.png#averageHue=%23f7f6f4&clientId=u13f03d49-e217-4&from=paste&height=551&id=u00c0dd37&originHeight=551&originWidth=338&originalType=binary&ratio=1&rotation=0&showTitle=false&size=22613&status=done&style=none&taskId=uea836c84-2b10-4662-bb0a-11c9c064bd8&title=&width=338)![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677229134539-de020b48-39dc-4f1f-8512-5ee6cc67e56b.png#averageHue=%23f6f5f3&clientId=u13f03d49-e217-4&from=paste&height=150&id=u688aad3d&originHeight=150&originWidth=307&originalType=binary&ratio=1&rotation=0&showTitle=false&size=5422&status=done&style=none&taskId=u01faa729-4548-418e-ad19-84ed9dd536e&title=&width=307)
#### 内连接
> -- 从多张表中提取数据，必须指定关联的条件。如果不定义关联条件就会出现无条件连接，两张表的数据会交叉连接，产生笛卡尔积。想了解笛卡尔可以点击--------->[笛卡尔积连接](https://www.jianshu.com/p/cf0d47d96911)

```sql
SELECT * FROM t_emp , t_dept;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677230991060-0cf9ed7f-8661-47b7-8590-ae178cbdb422.png#averageHue=%23f7f5f4&clientId=u0bb56fb8-3c39-4&from=paste&height=425&id=uafcb000b&originHeight=425&originWidth=660&originalType=binary&ratio=1&rotation=0&showTitle=false&size=33958&status=done&style=none&taskId=u0606ebf4-1ffa-4a26-8164-35b54210ddd&title=&width=660)<br />这里形成的原因是每一条员工的数据都会拼接一条部门数据导致的<br />![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677231204902-45dd9f1c-b8bf-465d-9f0f-7f8f555f1eec.png#averageHue=%23fbfbfa&clientId=u0bb56fb8-3c39-4&from=paste&height=676&id=u3226b134&originHeight=676&originWidth=865&originalType=binary&ratio=1&rotation=0&showTitle=false&size=135473&status=done&style=none&taskId=u6a9985e5-588b-409b-9297-4899deefb88&title=&width=865)<br />多表查询显示部门员工数据
> 这里使用的是内连接 只不过添加了一条员工的deptno与部门id对应的查询条件去除掉了多余的数据

```sql
SELECT  e.id ,e.emp_name,d.dname FROM t_emp e JOIN t_dept d ON e.deptno=d.id ORDER BY e.id ASC;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677229104470-09764fc5-ef43-450b-8c99-aba9f80a8b51.png#averageHue=%23f5f4f2&clientId=u13f03d49-e217-4&from=paste&height=384&id=uf5ee96c1&originHeight=384&originWidth=369&originalType=binary&ratio=1&rotation=0&showTitle=false&size=19986&status=done&style=none&taskId=u56e22c52-f702-4496-9422-f2272e47fe4&title=&width=369)

#### 外连接-左连接
**注意:左右连接中的左右**是看 **JOIN 关键字的左右两边**,如果左右两边表的位置调换一下位置结果与更换左右的效果是一样的.
> 在 LEFT JOIN 左边的表里面数据全被全部查出来，右边的数据只会查出符合ON后面的符合条件的数据，不符合的会用NULL代替。

下面是左连接,以**左表员工表为基础显示**,即便**没有对应的部门**,也会显示出来而部门信息**用null代替**
```sql
SELECT  e.id ,e.emp_name,d.dname FROM t_emp e left JOIN t_dept d ON e.deptno=d.id ORDER BY e.id ASC;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677232316255-54ad0d28-f56e-452c-a86e-4d523fde46f5.png#averageHue=%23f6f5f3&clientId=u0bb56fb8-3c39-4&from=paste&height=405&id=u11b5100d&originHeight=405&originWidth=419&originalType=binary&ratio=1&rotation=0&showTitle=false&size=22908&status=done&style=none&taskId=ubc3e196c-1dc1-4939-babd-4f50e8d4913&title=&width=419)
#### 外连接-右连接
> 与 LEFT JOIN 正好相反，右边的数据会会全部查出来，左边只会查出ON后面符合条件的数据，不符合的会用NULL代替

现在则是右连接因为没有员工是小卖部的所以员工信息为null
```sql
SELECT  e.id ,e.emp_name,d.dname FROM t_emp e RIGHT JOIN t_dept d ON e.deptno=d.id ORDER BY e.id ASC;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1677232490948-901f9702-b38b-4f2c-ab39-e8de9d475a76.png#averageHue=%23f8f7f6&clientId=u0bb56fb8-3c39-4&from=paste&height=429&id=u3b6c75f5&originHeight=429&originWidth=504&originalType=binary&ratio=1&rotation=0&showTitle=false&size=23750&status=done&style=none&taskId=u9eb7c504-5693-4649-8cb6-a3547610ff5&title=&width=504)
# DDL
## 索引
> 创建索引可以增加数据查询的速度，但是会增加数据插入时的消耗，因为索引时将索引字段创建一个二叉树，然后每次根据次字段查询的时候才会变快。

### 创建表时创建索引
```sql
CREATE TABLE t_message(
id INT UNSIGNED COMMENT '主键',
content VARCHAR(200) NOT NULL COMMENT '内容',
type ENUM('通知','公告') NOT NULL COMMENT '消息类型',
create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，自动填充',
INDEX 索引名称可以不填写（默认为字段名）  (创建索引的字段，注意添加括号)
);
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672727930438-34994cbe-247e-4b14-88ed-ea4a5e4c69e9.png#averageHue=%23fbfaf9&clientId=u892ca0d1-2d02-4&from=paste&height=431&id=ua8854dd7&originHeight=431&originWidth=852&originalType=binary&ratio=1&rotation=0&showTitle=false&size=40721&status=done&style=none&taskId=ue883e947-993b-4e79-acb7-2c72c1d11d3&title=&width=852)
### 查看表的索引
```sql
# 查看索引
SHOW INDEX FROM t_message;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672728049551-8e2710db-c7ff-41fd-8afd-216e93cd6046.png#averageHue=%23fbfafa&clientId=u892ca0d1-2d02-4&from=paste&height=254&id=u66ec12cf&originHeight=254&originWidth=917&originalType=binary&ratio=1&rotation=0&showTitle=false&size=16002&status=done&style=none&taskId=u5389f1fc-e32d-4dfd-a650-e4be3ccf4b4&title=&width=917)
### 创建表后创建索引
```sql
CREATE INDEX 索引名 ON 创建索引字段所在的表名(创建索引的字段名); 
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672728139504-187e1fee-8aec-4055-b1b7-4ab823433c0d.png#averageHue=%23fbfaf9&clientId=u892ca0d1-2d02-4&from=paste&height=281&id=udd142c51&originHeight=281&originWidth=868&originalType=binary&ratio=1&rotation=0&showTitle=false&size=19960&status=done&style=none&taskId=u094f6aa3-1494-4b65-98d6-0ac2fb8b3fe&title=&width=868)
### 删除索引
```sql
DROP INDEX 删除的索引名 ON 索引所在的表名;
```
![image.png](https://cdn.nlark.com/yuque/0/2023/png/25418009/1672728192393-01cb96b5-d9fe-4a8c-b8b3-cd2760ad444c.png#averageHue=%23fbfafa&clientId=u892ca0d1-2d02-4&from=paste&height=247&id=ue3d8e5f7&originHeight=247&originWidth=889&originalType=binary&ratio=1&rotation=0&showTitle=false&size=15908&status=done&style=none&taskId=u3d941bd5-78e3-47e9-a959-8fc595b88d1&title=&width=889)
# 数据库学习代码
```sql
-- 创建数据库
CREATE DATABASE demo;

-- 删除数据库
DROP DATABASE demo;

-- 使用demo表
USE demo;

-- 创建数据表
CREATE TABLE student(
id INT UNSIGNED PRIMARY KEY COMMENT '主键',
name VARCHAR(20) NOT NULL COMMENT '姓名',
sex ENUM("男","女") NOT NULL COMMENT '性别'
)COMMENT '学生表';

-- 显示表结构
SHOW tables;

-- 显示表详情
DESC student;

-- 显示建表语句
SHOW CREATE TABLE student;

-- 删除表
DROP TABLE student;

-- 为已经创建的表添加字段alter改变表
ALTER TABLE student
ADD age INT NOT NULL COMMENT '年纪',
ADD clazz VARCHAR(5) NOT NULL COMMENT '班',
ADD aclazz VARCHAR(5) NOT NULL COMMENT '年级';

-- 修改字段类型和约束（刚刚再添加的时候添加了连个clazz，现在修改并修改约束和字段名）
-- 这里修改了约束并且赋了默认值
ALTER TABLE student
MODIFY aclazz VARCHAR(20) NOT NULL DEFAULT("1") COMMENT '年级';
DESC student;

-- 修改字段名称约束等
ALTER TABLE student
CHANGE aclazz grade VARCHAR(20) NOT NULL DEFAULT("1") COMMENT '年级';
DESC student;

-- 删除字段
ALTER TABLE student
DROP sex,
DROP age;
DESC student;

-- 字段约束
-- 主键约束 ，非空约束，唯一约束
CREATE TABLE t_dept(
id INT UNSIGNED PRIMARY KEY COMMENT '主键id无符号int类型数据，主键约束',
deptno VARCHAR(20) NOT NULL COMMENT '非空约束',
dname VARCHAR(20) UNIQUE COMMENT '唯一约束'
);
DESC t_dept;

-- 在创建完成表之后添加约束
ALTER TABLE student
MODIFY age INT UNSIGNED NOT NULL;
DESC student;

-- 添加外键约束
CREATE TABLE t_emp(
id INT PRIMARY KEY COMMENT  '主键',
emp_name VARCHAR(20) NOT NULL COMMENT '员工名称',
deptno INT UNSIGNED COMMENT '部门名称编号与部门名称主键形成外键',
FOREIGN KEY (deptno) REFERENCES t_dept(id)
);
DESC t_emp;

-- 创建表时创建索引
CREATE TABLE t_message(
id INT UNSIGNED COMMENT '主键',
content VARCHAR(200) NOT NULL COMMENT '内容',
type ENUM('通知','公告') NOT NULL COMMENT '消息类型',
create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，自动填充',
INDEX ind_type  (type)
);
DESC t_message;

-- 查看索引
SHOW INDEX FROM t_message;

-- 创建表后创建索引
CREATE INDEX ind_clazz ON student(clazz); 
SHOW INDEX FROM student;

-- 删除索引
DROP INDEX ind_clazz ON student;
SHOW INDEX FROM student;

-- 最基本的查询语句是由SELECT和FROM关键字组成的
SELECT * FROM `student` ;

-- 指定字段显示
SELECT name, sex ,age FROM `student` ;

-- 指定字段显示并使用 AS 起别名
SELECT name AS 姓名, sex AS 性别,age AS 年纪 FROM `student` ;
-- 执行顺序： 读取 SQL 语句->选择数据源 FROM -> 选择输出内容 SELECT

-- 数据分页查询 LIMIT 起始位置，偏移量 
SELECT name AS 姓名, sex AS 性别,age AS 年纪 FROM `student` LIMIT 0,5;

-- 数据库分页简写起始位置为0可以不写
SELECT name AS na, sex ,age FROM `student` LIMIT 5;

-- 排序 ORDER BY 关键字   ASC升学（默认值） DESC降序
-- 如果排序列是数字类型，数据库就按照数字大小排序，如果是日期类型就按照日期大小排序，如果是字符串就按照字符集序号排序
SELECT name AS na, sex ,age FROM `student` ORDER BY age ASC LIMIT 5 ;

-- 我们可以使用ORDER BY规定首要排序条件和次要排序条件。数据库会先按照首要排序条件排序，如果遇到首要排序内容相同的记录那么就会启用次要排序条件接着排序。
SELECT * FROM `student` ORDER BY age ASC, name ;

-- 在这里我们要注意排序和分页的顺序排序 GROUP BY 放在 LIMIT 之前。执行顺序 FROM -> SELECT -> ORDER BY -> LIMIT

-- 去除重复数据 DISTINCT
 SELECT  DISTINCT age FROM student;
 
--  注意 DISTINCT 关键字这只能查询一列重复数据，如果查询多列就失效了
  SELECT  DISTINCT age, name FROM student;
	
-- 	DISTINCT 关键字只能咋爱 SELECT 子句中使用一次
--  SELECT sex, DISTINCT age FROM student; 无法执行
--  SELECT DISTINCT sex, DISTINCT age FROM student; 无法执行

-- 条件查询
SELECT * FROM student WHERE age>18;

-- 逻辑查询 AND OR
SELECT * FROM student WHERE age>18 AND sex='男';

-- IFNULL(字段名, 替换值) 下面代码意思如果 age 字段中有null值或是为空就会将查到的数据替换成0
SELECT 10*IFNULL(null,0);

-- 比较前者时间与后者时间的相差天数
SELECT DATEDIFF(NOW(),'2021-05-18 07:58:35')

-- 比较运算符 >大于 <小于 >=大于等于 <=小于等于 =等于 !=不等于 IN包含
SELECT * FROM student WHERE age!='18' AND  clazz IN ( '会计 ','幼师');

-- 还有一些特殊的比较运算符 IS NULL(为空，count IS NULL)  IS NOT NULL(不为空 ，count IS NOT NULL) BETWEEN AND(范围 , count BETWEEN  200 AND 300)  LIKE(模糊查询, c_name LIKE "%A%") REGEXP(正则表达式查询 c_name REGEXP "[a-z]{4}")


-- 逻辑运算符 AND(与，age>10 AND sex='男') OR(或,age>10 OR sex='男') NOT(非，NOT age>10) XOR(异或，age>10 XOR sex='男')
-- 异或解释： a XOR b 当a b两值不同时为 true ，或同时为 false 时返回true
SELECT * FROM student WHERE age>18 XOR sex='男';-- 查询结果是小与或等于18的男生，和大于18的女生

 
-- WHERE子句中，条件执行的顺序是从左到右的。所以我们应该把索引条件，或者筛选掉记录最多的条件写在最左侧
SELECT * FROM student WHERE sex='男' AND age > 20;


-- 高级查询
-- 聚合函数
-- 聚合函数在数据的查询分析中，应用十分广泛。聚合函数可以对数据求和、求最大值和最小值、求平均值等等
-- 求男学生的平均年龄
SELECT AVG(IFNULL(age,18)) AS '平均年龄' FROM student WHERE sex='男';

-- 年纪求和
SELECT SUM(IFNULL(age,18)) AS '年纪和' FROM student ;

-- 年纪最大的人
SELECT MAX(IFNULL(age,18)) AS '最大年纪' FROM student ;

-- COUNT(*)用于获得包含空值的记录数，COUNT(列名)用于获得包含非空值的记录数。

-- 获取有多少个不同的年级
SELECT COUNT(DISTINCT grade) FROM student ;
SELECT COUNT(*) FROM student ;
SELECT * FROM student ;

-- 分组 GROUP BY
-- 为什么要分组？
-- 默认情况下汇总函数是对全表范围内的数据做统计GROUP BY子句的作用是通过一定的规则将一个数据集划分成若干个小的区域，然后针对每个小区域分别进行数据汇总处理
-- 查询语句中如果含有GROUP BY子句，那么SELECT-子句中的内容就必须要遵守规定：SELECT子句中可以包括聚合函数，或者GROUP BY子句的分组列（被 GROUP BY 用来分组的字段可以出现在select中），其余内容均不可以出现在SELECT子句中

-- 对性别进行分组，并统计数量
SELECT sex,COUNT(sex) FROM student GROUP BY sex;

-- 查询会计班的男生和女生的数量 
SELECT sex, COUNT(sex) FROM student WHERE clazz = '会计' GROUP BY sex ORDER BY sex;

-- GROUP_CONCAT 函数可以把分组查询中的某个字段拼接成一个字符串 
SELECT clazz , GROUP_CONCAT( name ) FROM student  GROUP BY clazz ;

-- GROUP BY 搭档 HAVING , HAVING 是在 GROUP BY 之后执行筛选.
-- 筛选班级人数大于4人的班级
SELECT clazz AS '班级' , COUNT(name) AS '班级人数',GROUP_CONCAT(name) AS '班级人员姓名' FROM student GROUP BY clazz HAVING COUNT(name)>4;

-- 没用小技巧 GROUP BY 可以传数字,它会根据这个数字的对应位子的字段进行分组 , 此时 HAVING 也是可以使用的
SELECT * ,COUNT(*) FROM student  GROUP BY 5 HAVING COUNT(*) > 4;

-- 从多张表中提取数据，必须指定关联的条件。如果不定义关联条件就会出现无条件连接，两张表的数据会交叉连接，产生笛卡尔积。
SELECT  e.id ,e.emp_name,d.dname FROM t_emp e JOIN t_dept d ON e.deptno=d.id ORDER BY e.id ASC;

-- 什么是连接 : 连接就是将多张不同的表进行结合搜索,然后获取指定条件下的数据

-- 内连接 在没有条件的情况下会形成广义笛卡尔积
SELECT * FROM t_emp , t_dept;
-- 左连接
SELECT  e.id ,e.emp_name,d.dname FROM t_emp e LEFT JOIN t_dept d ON e.deptno=d.id ORDER BY e.id ASC;
-- 右连接
SELECT  e.id ,e.emp_name,d.dname FROM t_emp e RIGHT JOIN t_dept d ON e.deptno=d.id ORDER BY e.id ASC;

```
# 数据库结构及数据
```sql
/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3306
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 24/02/2023 17:58:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `sex` enum('男','女') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '性别',
  `age` int UNSIGNED NOT NULL,
  `clazz` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班',
  `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '_utf8mb4\\\'1\\\'' COMMENT '年级',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, '郭岚', '女', 18, '会计', '');
INSERT INTO `student` VALUES (2, '黄杰宏', '男', 20, '幼师', '2019');
INSERT INTO `student` VALUES (3, '高安琪', '女', 19, '电器', '2019');
INSERT INTO `student` VALUES (4, '陆安琪', '男', 21, '会计', '2018');
INSERT INTO `student` VALUES (5, '龚詩涵', '男', 20, '电器', '2019');
INSERT INTO `student` VALUES (6, '田晓明', '女', 20, '会计', '2018');
INSERT INTO `student` VALUES (7, '常詩涵', '女', 19, '幼师', '2019');
INSERT INTO `student` VALUES (8, '姜岚', '女', 22, '大数据', '2018');
INSERT INTO `student` VALUES (9, '杜嘉伦', '女', 20, '幼师', '2018');
INSERT INTO `student` VALUES (10, '冯嘉伦', '男', 22, '幼师', '2019');
INSERT INTO `student` VALUES (11, '郑璐', '男', 20, '电器', '2019');
INSERT INTO `student` VALUES (12, '罗震南', '男', 20, '电器', '2019');
INSERT INTO `student` VALUES (13, '雷子异', '男', 18, '会计', '2018');
INSERT INTO `student` VALUES (14, '高嘉伦', '女', 22, '大数据', '2018');
INSERT INTO `student` VALUES (15, '戴子韬', '女', 20, '计算机', '2019');
INSERT INTO `student` VALUES (16, '郑云熙', '女', 18, '电器', '2019');
INSERT INTO `student` VALUES (17, '夏杰宏', '女', 19, '大数据', '2019');
INSERT INTO `student` VALUES (18, '钱安琪', '男', 21, '电器', '2019');
INSERT INTO `student` VALUES (19, '袁秀英', '女', 19, '幼师', '2018');
INSERT INTO `student` VALUES (20, '王嘉伦', '女', 21, '计算机', '2018');

-- ----------------------------
-- Table structure for t_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_dept`;
CREATE TABLE `t_dept`  (
  `id` int UNSIGNED NOT NULL COMMENT '主键id无符号int类型数据，主键约束',
  `deptno` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '非空约束',
  `dname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '唯一约束',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `dname`(`dname` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_dept
-- ----------------------------
INSERT INTO `t_dept` VALUES (1, 'IT0785', '外销部');
INSERT INTO `t_dept` VALUES (2, 'CM9883', '销售部');
INSERT INTO `t_dept` VALUES (3, 'UT3910', '工程部');
INSERT INTO `t_dept` VALUES (4, 'UY3698', '小卖部');

-- ----------------------------
-- Table structure for t_emp
-- ----------------------------
DROP TABLE IF EXISTS `t_emp`;
CREATE TABLE `t_emp`  (
  `id` int NOT NULL COMMENT '主键',
  `emp_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工名称',
  `deptno` int UNSIGNED NULL DEFAULT NULL COMMENT '部门名称编号与部门名称主键形成外键',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `deptno`(`deptno` ASC) USING BTREE,
  CONSTRAINT `t_emp_ibfk_1` FOREIGN KEY (`deptno`) REFERENCES `t_dept` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_emp
-- ----------------------------
INSERT INTO `t_emp` VALUES (1, '宋秀英', 3);
INSERT INTO `t_emp` VALUES (2, '何秀英', 2);
INSERT INTO `t_emp` VALUES (3, '丁岚', 2);
INSERT INTO `t_emp` VALUES (4, '陶晓明', 3);
INSERT INTO `t_emp` VALUES (5, '范岚', 1);
INSERT INTO `t_emp` VALUES (6, '常秀英', 1);
INSERT INTO `t_emp` VALUES (7, '王宇宁', 2);
INSERT INTO `t_emp` VALUES (8, '王震南', 2);
INSERT INTO `t_emp` VALUES (9, '赵詩涵', 2);
INSERT INTO `t_emp` VALUES (10, '卢子韬', 2);
INSERT INTO `t_emp` VALUES (11, '钱睿', 1);
INSERT INTO `t_emp` VALUES (12, '顾宇宁', 1);
INSERT INTO `t_emp` VALUES (13, '卢睿', 1);
INSERT INTO `t_emp` VALUES (14, '邹岚', 1);
INSERT INTO `t_emp` VALUES (15, '段睿', 1);
INSERT INTO `t_emp` VALUES (16, '谢嘉伦', 2);
INSERT INTO `t_emp` VALUES (17, '廖震南', 2);
INSERT INTO `t_emp` VALUES (18, '尹致远', 1);
INSERT INTO `t_emp` VALUES (19, '罗子异', 1);
INSERT INTO `t_emp` VALUES (20, '夏杰宏', 3);
INSERT INTO `t_emp` VALUES (21, '王浩南', NULL);

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message`  (
  `id` int UNSIGNED NULL DEFAULT NULL COMMENT '主键',
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `type` enum('通知','公告') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，自动填充',
  INDEX `ind_type`(`type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_message
-- ----------------------------
INSERT INTO `t_message` VALUES (401, '中国东莞坑美十五巷862号26室', '公告', '2018-01-15 05:42:54');
INSERT INTO `t_message` VALUES (826, '中国东莞环区南街二巷465号36室', '公告', '2010-04-05 00:24:42');
INSERT INTO `t_message` VALUES (92, '中国北京市延庆区028县道888号20室', '通知', '2021-08-06 00:38:53');
INSERT INTO `t_message` VALUES (870, '中国成都市锦江区红星路三段334号17栋', '公告', '2018-02-26 08:15:37');
INSERT INTO `t_message` VALUES (809, '中国深圳福田区深南大道535号16号楼', '通知', '2021-05-18 07:58:35');
INSERT INTO `t_message` VALUES (695, '中国北京市东城区东单王府井东街894号42室', '通知', '2016-02-07 08:33:35');
INSERT INTO `t_message` VALUES (520, '中国成都市锦江区人民南路四段723号2室', '公告', '2016-03-18 12:47:14');
INSERT INTO `t_message` VALUES (18, '中国中山紫马岭商圈中山五路300号12室', '通知', '2005-05-02 08:36:08');
INSERT INTO `t_message` VALUES (4, '中国上海市闵行区宾川路580号26号楼', '通知', '2010-12-16 11:13:14');
INSERT INTO `t_message` VALUES (276, '中国成都市锦江区红星路三段581号31栋', '公告', '2015-11-18 05:33:19');
INSERT INTO `t_message` VALUES (229, '中国上海市闵行区宾川路898号39室', '通知', '2010-10-08 11:29:01');
INSERT INTO `t_message` VALUES (631, '中国北京市房山区岳琉路656号16室', '通知', '2022-08-14 09:07:37');
INSERT INTO `t_message` VALUES (798, '中国中山紫马岭商圈中山五路393号3室', '公告', '2013-06-13 09:50:04');
INSERT INTO `t_message` VALUES (946, '中国广州市天河区天河路560号9号楼', '通知', '2008-02-10 05:08:45');
INSERT INTO `t_message` VALUES (784, '中国成都市锦江区红星路三段173号38栋', '公告', '2013-02-02 21:58:00');
INSERT INTO `t_message` VALUES (577, '中国成都市成华区玉双路6号173号48栋', '公告', '2016-09-30 07:55:22');
INSERT INTO `t_message` VALUES (39, '中国北京市西城区西長安街891号华润大厦29室', '公告', '2009-11-24 23:38:43');
INSERT INTO `t_message` VALUES (620, '中国广州市越秀区中山二路9号17室', '通知', '2013-06-25 21:34:44');
INSERT INTO `t_message` VALUES (921, '中国北京市海淀区清河中街68号521号28楼', '通知', '2021-01-27 13:32:27');
INSERT INTO `t_message` VALUES (547, '中国中山京华商圈华夏街631号6楼', '公告', '2011-09-01 09:26:57');

SET FOREIGN_KEY_CHECKS = 1;

```
