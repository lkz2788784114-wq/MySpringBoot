package org.example.test2.exception;

import org.example.test2.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 拦截所有未知的运行时异常 (RuntimeException)
     * 比如著名的 NullPointerException，或者你写的 10 / 0
     */
    @ExceptionHandler(RuntimeException.class) // 指定要拦截的异常类型
    public Result<String> handleRuntimeException(RuntimeException e) {
        // 1. 在控制台打印真正的错误堆栈，留给后端自己排查 bug 用的
        e.printStackTrace();

        // 2. 给前端返回一个极其优雅的 JSON，绝对不能把原生报错堆栈丢给前端！
        return Result.error(500, "服务器开小差了，请稍后再试");
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgumentException(IllegalArgumentException e) {
        // 对于参数错误，我们把具体的错误信息 (e.getMessage()) 告诉前端
        return Result.error(400, "参数错误：" + e.getMessage());
    }
}

