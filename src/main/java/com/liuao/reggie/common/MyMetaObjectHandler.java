package com.liuao.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据对象处理器
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        log.info("当前前程id是:"+Thread.currentThread().getId());
        ThreadLocal<Long> threadLocal = new ThreadLocal<>();
        if(metaObject.hasSetter("createTime")){
            metaObject.setValue("createTime", LocalDateTime.now());
        }
        if(metaObject.hasSetter("updateTime")){
            metaObject.setValue("updateTime", LocalDateTime.now());
        }
        System.out.println("BaseContext.getThreadLocal():"+BaseContext.getThreadLocal());
        if(metaObject.hasSetter("createUser")){
            metaObject.setValue("createUser", BaseContext.getThreadLocal());
        }
        if(metaObject.hasSetter("updateUser")){
            metaObject.setValue("updateUser", BaseContext.getThreadLocal());
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        log.info("当前前程id是:"+Thread.currentThread().getId());
//        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐)
//        this.strictUpdateFill(metaObject, "updateUser", Long.class, 1L); // 起始版本 3.3.0(推荐)
        if(metaObject.hasSetter("updateTime")){
            metaObject.setValue("updateTime", LocalDateTime.now());
        }
        if(metaObject.hasSetter("updateUser")){
            metaObject.setValue("updateUser", 1L);
        }
    }
}
