# MP 实体包装器

说明：
ew是mapper方法里的@Param(Constants.WRAPPER) Wrapper queryWrapper对象

## 1、${ew.customSqlSegment} 会直接在前面添加 where

```java
List<SysUser> listPage(@Param(Constants.WRAPPER)  QueryWrapper queryWrapper)
```

```xml
@Select(select * from sys_user ${ew.customSqlSegment}）
```

## 2、${ew.sqlSegment} 就只有条件语句

```java
 List<SysUser> listPage@Param(Constants.WRAPPER) QueryWrapper queryWrapper)
```

${ew.sqlSelect} 就是 queryWrapper.select(****) 你所定义需要查询的字段

```xml
 @Select(select * from sys_user  where ${ew.sqlSegment}）
```

### 3、实体包装器判空

```xml
<if test="ew != null">
    <if test="ew.nonEmptyOfWhere">
        AND
    </if>
    ${ew.sqlSegment}
</if>
```

## 实例1

```java
List<SysUser> listPage(@Param(Constants.WRAPPER) QueryWrapper queryWrapper)
```

```xml
<update id="updateById">
    update ${tableName} set ${ew.sqlSet} ${ew.customSqlSegment};
</update>
```

## 实例2

${ew.sqlSet}用于update语句

```java
boolean updateById(@Param("tableName") String tableName,@Param(Constants.WRAPPER) Wrapper wrapper);
```

```xml
<update id="updateUser">
		update sys_user
		set ${ew.sqlSet}
		where ${ew.sqlSegment}
</update>
```

