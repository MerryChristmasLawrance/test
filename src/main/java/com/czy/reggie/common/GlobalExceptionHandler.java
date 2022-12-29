//package com.czy.reggie.common;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.sql.SQLIntegrityConstraintViolationException;
//
////全局异常处理器
//@RestControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
//    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
//        log.error(ex.getMessage());
//        return R.error("失败了");
//    }
//}
