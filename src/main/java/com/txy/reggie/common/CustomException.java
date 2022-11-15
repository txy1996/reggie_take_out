package com.txy.reggie.common;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO 自定义业务异常
 * @date 2022/11/9 21:19
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){

        super(message);
    }
}
