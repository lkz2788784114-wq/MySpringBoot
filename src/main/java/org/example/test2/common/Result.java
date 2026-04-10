package org.example.test2.common;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T>{
    private Integer code; // 状态码：比如 200 代表成功，500 代表失败，400 代表参数错误
    private String msg;   // 给前端看的提示信息
    private T data;       // 真正要返回的数据

    // 成功时的快捷方法
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = "操作成功";
        result.data = data;
        return result;
    }

    // 失败时的快捷方法
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        result.data = null; // 失败了通常没有业务数据
        return result;
    }

    // ...记得生成 get 和 set 方法！
}
