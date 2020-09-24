package com.sofency.community.utils;

import com.sofency.community.pojo.User;

/**
 * @author sofency
 * @date 2020/9/24 23:53
 * @package IntelliJ IDEA
 * @description  存储当前线程的用户
 */
public class ThreadLocalUtils {

    private final  static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程中的用户
     * @param user
     */
    public static void setUser(User user){
        userThreadLocal.set(user);
    }

    /**
     * 获取线程中的用户
     * @return
     */
    public static User getUser( ){
        return userThreadLocal.get();
    }

}
