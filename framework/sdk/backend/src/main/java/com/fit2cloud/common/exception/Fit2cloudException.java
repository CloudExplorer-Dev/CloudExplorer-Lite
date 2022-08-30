package com.fit2cloud.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:张少虎
 * @Date: 2022/8/29  5:53 PM
 * @Version 1.0
 * @注释:
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Fit2cloudException  extends RuntimeException{
    /**
     * 异常状态码
     */
    private Integer code;
    /**
     * 异常提示
     */
    private String message;

}
