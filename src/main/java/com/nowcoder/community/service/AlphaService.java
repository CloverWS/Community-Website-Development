package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service //服务类组件

//@Scope("prototype")//默认singleton：单例, prototype : getBean()可以创建多次实例（默认只创建一次实例，一次销毁）
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    public AlphaService(){
        System.out.println("new instance of AlphaService");
    }

    @PostConstruct //init()方法在构造之后调用
    public void init(){
        System.out.println("Initialising");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("Destroy AlphaService");
    }

    public String find(){
        return alphaDao.select();
    }
}
