package com.atguigu.system.exception;

import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@ControllerAdvice//标识当前类是异常处理器
public class GlobalExceptionHandler {
    //处理异常的方法
    @ExceptionHandler(Exception.class)
    public Result resolveException(Exception e){
        e.printStackTrace();
        return Result.build(null, ResultCodeEnum.SERVICE_ERROR);
    }
    //处理数学异常的方法
    @ExceptionHandler(ArithmeticException.class)
    public Result resolveArithmeticException(ArithmeticException e){
        e.printStackTrace();
        return Result.fail().code(000).message("出现了数学异常");
    }

    //处理自定义异常的方法
    @ExceptionHandler(GuiguException.class)
    public Result resolveGuiguException(GuiguException e){
        e.printStackTrace();
        return Result.fail().message(e.getMessage()).code(e.getCode());
    }

}
