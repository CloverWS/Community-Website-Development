package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 作用于方法
@Retention(RetentionPolicy.RUNTIME)// 注解作用在程序运行时
public @interface LoginRequired {


}
