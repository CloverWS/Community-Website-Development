package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class AlphaAspect {

    // execution(* : 返回值， * 表示任何类型
    //           com.nowcoder.community.service.* ： 表示service包下的所有类
    //           .* ： 所有的方法
    //           (..) ： 所有的参数
    //           )
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut(){
        System.out.println("pointcut");
    }

    // 执行joinPotion前执行
    @Before("pointcut()")
    public void before(){
//        System.out.println("before");
    }

    // 执行joinPotion后执行
    @After("pointcut()")
    public void after(){
//        System.out.println("after");
    }

    // 返回值时执行
    @AfterReturning("pointcut()")
    public void afterReturning(){
        System.out.println("afterReturning");
    }

    // 抛异常时执行
    @AfterThrowing("pointcut()")
    public void afterThrowing(){
        System.out.println("afterThrowing");
    }

    // 前后都执行（after与 before合并）
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        // 调用目标组件的方法， 返回原始方法的返回值
        Object obj = joinPoint.proceed();
        System.out.println("around after");
        return obj;
    }


}
