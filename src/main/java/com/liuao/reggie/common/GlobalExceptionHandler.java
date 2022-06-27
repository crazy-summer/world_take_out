package com.liuao.reggie.common;

import com.liuao.reggie.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody// 这个注解一开始忘了写了，导致异常还是抛出了
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e){
        log.info("异常处理方法:"+e.getMessage());
        if(e.getMessage().contains("Duplicate entry")){
            if(e.getMessage().contains("for key 'idx_category_name'")){
                return R.error("菜品名称不能重复");
            }
        }
        return R.error(e.getMessage());
    }

    /**
     * 异常处理方法
     */
    @ExceptionHandler(CustomException.class)
    public R<String> CustomExceptionHandler(CustomException e){
        log.info("异常处理方法:"+e.getMessage());

        return R.error(e.getMessage());
    }
}
