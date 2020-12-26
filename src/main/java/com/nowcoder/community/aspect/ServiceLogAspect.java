package com.nowcoder.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
@Aspect
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut(){

    }

    // 执行joinPotion前执行
    @Before("pointcut()")
    public void before(JoinPoint joinPoint){
        // 实现目标 ： 用户[1.2.3.4],在[xxx时间]，访问了什么功能[com.nowcoder.community.service.xxx()]

        // 获取当前的request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取当前ip
        String ip = request.getRemoteHost();

        // 记录当前时间
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // 得到类名 + 得到方法名
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();

        logger.info(String.format("用户[%s],在[%s],访问了[%s]", ip, now, target));

    }

    // 执行joinPotion后执行
    @After("pointcut()")
    public void after(){
//        System.out.println("after");
    }
}
