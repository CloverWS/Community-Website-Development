package com.nowcoder.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling // 加上这个注解才能启动定时任务
@EnableAsync
public class ThreadPoolConfig {

    // 写这个配置类的作用是为了正确注入ThreadPoolTaskScheduler，
    // 如果没有这个配置类，则会报错。（@Autowired时）

}
