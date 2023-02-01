package com.fit2cloud.common.job_record;

import com.fit2cloud.request.pub.PageRequest;
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
    /**
     * 当前环节状态码
     */
    private Integer code;
    /**
     * 任务提示信息,如果code:200 则是成功提示,code:500 错误提示
     */
    private String message;

    /**
     * 用于保存当前任务环节的其他冗余数据
     */
    private T data;

    /**
     * 任务环节
     */
    private JobLink link;

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

    public JobRecordParam<T> link(JobLink link) {
        this.link = link;
        return this;
    }

    public static <T> JobRecordParam<T> success(JobLink link, T data) {
        return new JobRecordParam<T>()
                .code(HttpStatus.OK.value())
                .link(link)
                .data(data);
    }


    public static <T> JobRecordParam<T> error(JobLink link, String message) {
        return new JobRecordParam<T>()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .link(link)
                .message(message);
    }

    public static <T> JobRecordParam<T> error(JobLink link, T data, String message) {
        return new JobRecordParam<T>()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .link(link)
                .data(data)
                .message(message);
    }
}
