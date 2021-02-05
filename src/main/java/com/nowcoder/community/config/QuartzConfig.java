package com.nowcoder.community.config;

import com.nowcoder.community.quartz.AlphaJob;
import com.nowcoder.community.quartz.PostScoreRefreshJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;


// 配置 -> 一次读取，封装到数据库中 -> 以后只去数据库中调用
@Configuration
public class QuartzConfig {


    // ----------------------------------------DEMO---------------------------------------------------------------
    // FactoryBean: 作用，简化Bean的实例化过程；
    // 1. 通过FactoryBean封装了某些Bean实例化过程
    // 2. 将FactoryBean装配到Spring容器里
    // 3. 将FactoryBean注入给其他的Bean。
    // 4. 该Bean得到的时FactoryBean所管理的对象实例。



    // 配置JobDetail
    // @Bean
    public JobDetailFactoryBean alphaJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphaJob");
        factoryBean.setGroup("alphaJobGroup");
        factoryBean.setDurability(true); // 保存持久的
        factoryBean.setRequestsRecovery(true); // 任务是否可以被恢复
        return factoryBean;
    }


    // 配置Trigger ： 两种方式 1. Trigger（SimpleTriggerFactoryBean），2. Trigger（CronTriggerFactoryBean）
    // @Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(alphaJobDetail);
        factoryBean.setName("alphaTrigger");
        factoryBean.setGroup("alphaTriggerGroup");
        factoryBean.setRepeatInterval(3000); // 执行频率（ms）
        factoryBean.setJobDataMap(new JobDataMap());// 存储Job的状态，默认类型JobDataMap
        return factoryBean;
    }
    // -------------------------------------------------------------------------------------------------------

    // ---------------------------刷新帖子任务------------------------------------------------------------

    // 帖子分数刷新
    @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PostScoreRefreshJob.class);
        factoryBean.setName("postScoreRefreshJob");
        factoryBean.setGroup("communityJobGroup");
        factoryBean.setDurability(true); // 保存持久的
        factoryBean.setRequestsRecovery(true); // 任务是否可以被恢复
        return factoryBean;
    }


    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail){
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(postScoreRefreshJobDetail);
        factoryBean.setName("postScoreRefreshTrigger");
        factoryBean.setGroup("communityTriggerGroup");
        factoryBean.setRepeatInterval(1000 * 60 * 5); // 执行频率（ms） 每5分钟执行一次
        factoryBean.setJobDataMap(new JobDataMap());// 存储Job的状态，默认类型JobDataMap
        return factoryBean;
    }

    // -------------------------------------------------------------------------------------------------------

}
