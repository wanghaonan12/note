package com.whn.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author whn
 * @time 2025/7/10
 * @description toString注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AutoToString {
}
