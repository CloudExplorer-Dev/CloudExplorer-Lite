package com.fit2cloud.common.validator;

import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.controller.handler.ResultHolder;
import io.reactivex.rxjava3.core.Maybe;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.springframework.core.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Optional;
import java.util.Set;

/**
 * @Author:张少虎
 * @Date: 2022/8/25  10:38 AM
 * @Version 1.0
 * @注释:    处理校验返回值
 */
@RestControllerAdvice
public class ParameterCalibration {
    @ExceptionHandler({BindException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultHolder handleMethodArgumentNotValidException(Exception exception) {
        if (exception instanceof ValidationException) {
            if (exception instanceof ConstraintViolationException){
                ConstraintViolationException constraintViolationException=  (ConstraintViolationException)exception;
                Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
                Optional<ConstraintViolation<?>> first = constraintViolations.stream().findFirst();
                if (first.isPresent()){
                    String message = first.get().getMessage();
                    return ResultHolder.error(message);
                }
            }
            ValidationException validationException = (ValidationException) exception;
            String localizedMessage = validationException.getLocalizedMessage();
            return ResultHolder.error(localizedMessage);
        }
        if (exception instanceof BindException) {
            BindException bindException = (BindException) exception;
            FieldError fieldError = bindException.getFieldError();
            String defaultMessage = fieldError.getDefaultMessage();
            return ResultHolder.error(defaultMessage);
        }
        return ResultHolder.error(exception.getMessage());
    }

    @ExceptionHandler({ Fit2cloudException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultHolder fit2cloudException(Exception exception) {
       if (exception instanceof Fit2cloudException){
           Fit2cloudException fit2cloudException = (Fit2cloudException) exception;
           return ResultHolder.error(fit2cloudException.getCode(),fit2cloudException.getMessage());
       }
       return ResultHolder.error("未知错误");
    }


}
