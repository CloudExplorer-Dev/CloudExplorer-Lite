package com.fit2cloud.common.job_record;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/10  16:05}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class JobRecordParam<T> {
    private Integer code;

    private String message;

    private T data;


    public JobRecordParam<T> message(String message) {
        this.message = message;
        return this;
    }

    public JobRecordParam<T> code(Integer code) {
        this.code = code;
        return this;
    }

    public JobRecordParam<T> data(T data) {
        this.data = data;
        return this;
    }

    public static <T> JobRecordParam<T> success(T data) {
        return new JobRecordParam<T>()
                .code(HttpStatus.OK.value())
                .data(data);
    }


    public static <T> JobRecordParam<T> error(String message) {
        return new JobRecordParam<T>()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(message);
    }

    public static <T> JobRecordParam<T> error(Integer code, String message) {
        return new JobRecordParam<T>()
                .code(code)
                .message(message);
    }

    public static <T> JobRecordParam<T> error(T data, String message) {
        return new JobRecordParam<T>()
                .data(data)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(message);
    }
}
