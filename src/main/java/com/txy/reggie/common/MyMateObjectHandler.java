package com.txy.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO 自定义原数据处理器
 * @date 2022/11/9 17:38
 */
@Component
@Slf4j
public class MyMateObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        Long userId = BaseContext.getCurrentId();
        metaObject.setValue("createUser", userId);
        metaObject.setValue("updateUser", userId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]");
        log.info(metaObject.toString());
        long id = Thread.currentThread().getId();
        log.info("线程id为: {}",id);
        Long userId = BaseContext.getCurrentId();
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", userId);
    }
}
