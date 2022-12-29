package com.czy.reggie.common;


import com.czy.reggie.Exception.BusinessException;
import com.czy.reggie.Exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.SystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//异常处理器
@RestControllerAdvice
@Slf4j
public class ProjectExceptionAdvice {
    //处理系统异常
    @ExceptionHandler(SystemException.class)
    public R<String> doSystemException(SystemException ex){
        //记录日志
        //发送消息给运维
        //发消息给客户
        return R.error(ex.getMessage());
    }

    //业务异常
    @ExceptionHandler(BusinessException.class)
    public R<String> doBusinessException(BusinessException ex){
        //发消息给用户
        return R.error(ex.getMessage());
    }

    //其他异常
    @ExceptionHandler(Exception.class)
    public R<String> doException(Exception ex){
        //这个是新增员工如果用户名重复抛的异常。  如果异常信息字符串包含 "Duplicate entry"  就对它用split()分隔并拼接为
        //字符串msg  (...已存在)  ，最后返回msg
        if (ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg=split[2]+"已存在";
            return R.error(msg);
        }

        return R.error("系统繁忙请稍后");
    }

    //自定义异常处理
    @ExceptionHandler(CustomException.class)
    public R<String> doCustomException(CustomException ex){
        return R.error(ex.getMessage());
    }
}
