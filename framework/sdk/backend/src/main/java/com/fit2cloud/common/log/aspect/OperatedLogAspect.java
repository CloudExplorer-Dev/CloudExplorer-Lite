package com.fit2cloud.common.log.aspect;

import com.fit2cloud.autoconfigure.ServerInfo;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.annotation.OperatedLogFieldConver;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.conver.ResourceConvert;
import com.fit2cloud.common.log.entity.OperatedLogVO;
import com.fit2cloud.common.log.utils.IpUtil;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.log.utils.SpelUtil;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.SensitiveFieldUtils;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

/**
 * @author jianneng
 * @date 2022/9/15 10:26
 **/
@Aspect
@Component
public class OperatedLogAspect {

    @Pointcut("@annotation(com.fit2cloud.common.log.annotation.OperatedLog)")
    public void annotation() {

    }

    @Around("annotation()")
    public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable {
        Long startTime = System.currentTimeMillis();
        Object res = null;
        ResultHolder errorResult = ResultHolder.success("ok");
        try{
            res = pjd.proceed();
            if (res instanceof ResultHolder) {
                errorResult = (ResultHolder) res;
            }
        }catch (Exception e){
            if(e instanceof Fit2cloudException){
                Fit2cloudException fit2cloudException = (Fit2cloudException)e;
                errorResult.setCode(fit2cloudException.getCode());
                errorResult.setMessage(fit2cloudException.getMessage());
            }else{
                errorResult.setMessage(e.getMessage());
            }
        }
        Long endTime = System.currentTimeMillis();
        saveLog(pjd,endTime-startTime,errorResult);
        //请求完成后清理MDC
        MDC.clear();
        if(errorResult.getCode()!=200){
            throw new Fit2cloudException(errorResult.getCode(),errorResult.getMessage());
        }
        return res;
    }

    private void saveLog(ProceedingJoinPoint pjd, Long time ,ResultHolder errorResult){
        try {
            MethodSignature methodSignature = (MethodSignature) pjd.getSignature();
            Method method = methodSignature.getMethod();
            Object[] args = pjd.getArgs();
            OperatedLog annotation = method.getAnnotation(OperatedLog.class);
            // 日志注解内容
            if(annotation != null){
                OperatedLogVO logVO = createLog(time, errorResult);
                String paramStr = "";
                // 操作
                logVO.setOperated(annotation.operated().getOperate());
                String operatedName = OperatedTypeEnum.getDescriptionByOperate(logVO.getOperated());
                logVO.setOperatedName(operatedName);
                // 参数解析
                paramStr = JsonUtil.toJSONString(args);
                if(StringUtils.isNotEmpty(annotation.content())){
                    String content = "";
                    try{
                        content = OperatedTypeEnum.getDescriptionByOperate(annotation.content());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if(StringUtils.isEmpty(content)){
                        // 操作内容解析
                        logVO.setContent(SpelUtil.getElValueByKey(pjd,annotation.content()));
                    }
                }
                // 资源类型
                logVO.setResourceType(annotation.resourceType().getName());
                if(StringUtils.isNotEmpty(annotation.resourceId())){
                    // 资源ID,这里特殊处理，到时候查询用到
                    logVO.setResourceId(SpelUtil.getElValueByKey(pjd,annotation.resourceId())+"@"+annotation.resourceType().getCode());
                }
                if(StringUtils.isNotEmpty(annotation.joinResourceId())){
                    // 关联资源ID
                    logVO.setJoinResourceId(SpelUtil.getElValueByKey(pjd,annotation.joinResourceId()));
                }
                // 配置请求信息
                extractRequestInfo(errorResult, logVO, paramStr);
                Class logvoClass = logVO.getClass();
                Field[] fields = logvoClass.getDeclaredFields();
                for(Field field:fields){
                    OperatedLogFieldConver fieldAnnotation = field.getAnnotation(OperatedLogFieldConver.class);
                    if(Objects.nonNull(fieldAnnotation)){
                        String a = CommonUtil.exec(fieldAnnotation.conver(), logVO.getResourceId(), ResourceConvert::conver);
                        logVO.setResourceName(a);
                    }
                }
                // 上下文设置
                setMDC(logVO);
                LogUtil.info(JsonUtil.toJSONString(logVO));
            }
        } catch (Exception e) {
            LogUtil.error("记录日志失败:{}",e.getMessage());
        }
    }


    /**
     * 请求头信息以及请求结果
     * @param errorResult
     * @param logVO
     * @param paramStr
     */
    private void extractRequestInfo(ResultHolder errorResult, OperatedLogVO logVO, String paramStr) {
        // 请求头信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logVO.setUrl(request.getRequestURL().toString());
        logVO.setMethod(request.getMethod());
        logVO.setParams(paramStr);
        logVO.setSourceIp(IpUtil.getIpAddress(request));
        if(errorResult.getCode()!=200){
            logVO.setResponse(errorResult.getMessage());
        }
        if(StringUtils.isNotEmpty(logVO.getParams())){
            logVO.setParams(SensitiveFieldUtils.desensitization(logVO.getParams()));
        }
    }

    @NotNull
    private OperatedLogVO createLog(Long time, ResultHolder errorResult) {
        OperatedLogVO logVO = new OperatedLogVO();
        logVO.setModule(ServerInfo.moduleInfo.getName());
        logVO.setRequestTime(new Date().getTime());
        logVO.setTime(time);
        logVO.setStatus(errorResult.getCode()==200?1:0);
        logVO.setCode(errorResult.getCode());
        UserDto userDto = CurrentUserUtils.getUser();
        logVO.setUser(userDto.getName());
        logVO.setUserId(userDto.getId());
        return logVO;
    }

    private void setMDC(OperatedLogVO logVO){
        MDC.put("module",logVO.getModule());
        MDC.put("operated",logVO.getOperated());
        MDC.put("operatedName",logVO.getOperatedName());
        MDC.put("resourceId",logVO.getResourceId());
        MDC.put("resourceName",logVO.getResourceName());
        MDC.put("resourceType",logVO.getResourceType());
        MDC.put("joinResourceId",logVO.getJoinResourceId());
        MDC.put("user",logVO.getUser());
        MDC.put("userId",logVO.getUserId());
        MDC.put("url",logVO.getUrl());
        MDC.put("content",logVO.getContent());
        MDC.put("requestTime",String.valueOf(logVO.getRequestTime()));
        MDC.put("method",logVO.getMethod());
        MDC.put("params",logVO.getParams());
        MDC.put("status",String.valueOf(logVO.getStatus()));
        MDC.put("sourceIp",logVO.getSourceIp());
        MDC.put("time",String.valueOf(logVO.getTime()));
        MDC.put("code",String.valueOf(logVO.getCode()));
        MDC.put("response",logVO.getResponse());
    }


}
