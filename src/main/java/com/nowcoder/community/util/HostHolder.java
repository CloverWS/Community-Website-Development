package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 容器， 持有用户信息，用于代替session对象
 */
@Component
public class HostHolder {

    // 线程隔离
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    // 清除内存中的map，以防过多垃圾
    public void clear(){
        users.remove();
    }

}
