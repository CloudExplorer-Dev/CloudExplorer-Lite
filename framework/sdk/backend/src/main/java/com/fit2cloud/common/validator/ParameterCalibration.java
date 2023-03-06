package com.fit2cloud.common.validator;

import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.controller.handler.ResultHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * { @Author:张少虎}
 * { @Date: 2022/8/25  10:38 AM}
 * { @Version 1.0 }
 * { @注释: 处理校验返回值}
 */
@RestControllerAdvice
public class ParameterCalibration {
    @ExceptionHandler({BindException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultHolder<?> handleMethodArgumentNotValidException(Exception exception) {
        if (exception instanceof ValidationException validationException) {
            if (exception instanceof ConstraintViolationException constraintViolationException) {
                Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
                Optional<ConstraintViolation<?>> first = constraintViolations.stream().findFirst();
                if (first.isPresent()) {
                    String message = first.get().getMessage();
                    return ResultHolder.error(message);
                }
            }
            String localizedMessage = validationException.getLocalizedMessage();
            return ResultHolder.error(localizedMessage);
        }
        if (exception instanceof BindException bindException) {
            FieldError fieldError = bindException.getFieldError();
            if (Objects.nonNull(fieldError)) {
                return ResultHolder.error(fieldError.getDefaultMessage());
            }
            ObjectError globalError = bindException.getGlobalError();
            if (Objects.nonNull(globalError)) {
                return ResultHolder.error(globalError.getDefaultMessage());
            }
        }
        return ResultHolder.error(exception.getMessage());
    }

    @ExceptionHandler({Fit2cloudException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResultHolder<?> fit2cloudException(Exception exception) {
        if (exception instanceof Fit2cloudException fit2cloudException) {
            return ResultHolder.error(fit2cloudException.getCode(), fit2cloudException.getMessage());
        }
        Throwable fit2cloudThrowable = getFit2cloudException(exception.getCause());
        if (fit2cloudThrowable instanceof Fit2cloudException fit2cloudException) {
            return ResultHolder.error(fit2cloudException.getCode(), fit2cloudException.getMessage());
        }
        return ResultHolder.error("未知错误");
    }

    private Throwable getFit2cloudException(Throwable e) {
        if (e == null) {
            return null;
        }
        if (e instanceof Fit2cloudException fit2cloudException) {
            return fit2cloudException;
        } else {
            return getFit2cloudException(e.getCause());
        }
    }


}
