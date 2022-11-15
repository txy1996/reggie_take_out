package com.txy.reggie.common;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO 基于ThreadLocal封装工具类,用户保存和获取当前登录用户id
 * @date 2022/11/9 18:06
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();


    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
