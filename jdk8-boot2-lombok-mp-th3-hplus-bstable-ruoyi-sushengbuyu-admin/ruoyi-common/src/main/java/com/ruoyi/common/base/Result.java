package com.ruoyi.common.base;

import java.io.Serializable;
import com.ruoyi.common.enums.BusinessStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 控制层通用返回结果
 * @author sushengbuyu
 * @date 2018/9/5 16:03
 */
@Getter
@EqualsAndHashCode
public class Result<T> implements Serializable {
    private long code;
    private T data;
    private String msg;

    private Result() {
    }

    private Result(long code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static <T> Result<T> success() {
        return new Result<>(BusinessStatus.SUCCESS.ordinal(), null, "操作成功");
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>(BusinessStatus.SUCCESS.ordinal(), null, msg);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(BusinessStatus.SUCCESS.ordinal(), data, "");
    }

    public static <T> Result<T> failed() {
        return new Result<>(BusinessStatus.FAIL.ordinal(), null, "");
    }

    public static <T> Result<T> failed(String msg) {
        return new Result<>(BusinessStatus.FAIL.ordinal(), null, msg);
    }

    public static <T> Result<T> failed(long code, String msg) {
        return new Result<>(code, null, msg);
    }

}

