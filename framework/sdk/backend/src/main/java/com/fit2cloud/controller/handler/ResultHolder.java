package com.fit2cloud.controller.handler;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Accessors(chain = true)
@Data
public class ResultHolder<T> {

    private String requestId;
    private Integer code;

    private String message;

    private T data;


    public ResultHolder<T> message(String message) {
        this.message = message;
        return this;
    }

    public ResultHolder<T> code(Integer code) {
        this.code = code;
        return this;
    }

    public ResultHolder<T> data(T data) {
        this.data = data;
        return this;
    }

    public static <T> ResultHolder<T> success(T data) {
        return new ResultHolder<T>()
                .code(HttpStatus.OK.value())
                .data(data);
    }

    public static <T> ResultHolder<T> error(T data) {
        return new ResultHolder<T>()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .data(data);
    }

    public static <T> ResultHolder<T> error(String message){
        return new ResultHolder<T>()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(message);
    }
    public static <T> ResultHolder<T> error(Integer code,String message){
        return new ResultHolder<T>()
                .code(code)
                .message(message);
    }

}
