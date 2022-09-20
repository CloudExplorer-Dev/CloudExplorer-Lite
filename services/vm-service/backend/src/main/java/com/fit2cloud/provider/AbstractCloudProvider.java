package com.fit2cloud.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.exception.ReTryException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  10:35 AM
 * @Version 1.0
 * @注释:
 */
public abstract class AbstractCloudProvider<C extends Credential> implements ICloudProvider {

    /**
     * 默认第一页
     */
    protected static final Integer DefaultCurrentPage = 1;
    /**
     * 默认每页100条
     */
    protected static final Integer DefaultPageSize = 100;
    /**
     * 同步重试次数
     */
    protected static final Integer DefaultReTry = 5;

    /**
     * 获取认证信息
     *
     * @param credential 认证字符串
     * @return 认证对象
     */
    protected C getCredential(String credential) {
        Type genericSuperclass = getClass().getGenericSuperclass();
        Type trueType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        try {
            return new ObjectMapper().readValue(credential, new ObjectMapper().constructType(trueType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取认证信息
     *
     * @param req 请求对象字符串
     * @return 认证对象
     */
    protected C getCredentialByRequest(String req) {
        ObjectNode jsonNodes = JsonUtil.parseObject(req);
        JsonNode credential = jsonNodes.get("credential");
        if (credential != null) {
            return getCredential(credential.asText());
        }
        throw new Fit2cloudException(1001, "不存在认证对象");
    }

    /**
     * @param request    请求参数
     * @param exec       执行函数
     * @param getList    获取数据
     * @param hasNext    是否有下一条
     * @param next       下一条
     * @param <T>        返回值类型
     * @param <Request>  请求对象类型
     * @param <Response> 接口返回类型
     * @return 分页查询到到全量数据
     */
    public <T, Request, Response> List<T> page(Request request, Function<Request, Response> exec, Function<Response, List<T>> getList, BiFunction<Request, Response, Boolean> hasNext, Consumer<Request> next) {
        ArrayList<T> collector = new ArrayList<>();
        return page(request, exec, getList, hasNext, next, DefaultReTry, collector);
    }

    /**
     * @param request    请求参数
     * @param exec       执行函数
     * @param getList    获取数据
     * @param hasNext    是否有下一条
     * @param next       下一条
     * @param reTry      重试次数
     * @param collector  数据收集器
     * @param <T>        返回值类型
     * @param <Request>  请求对象类型
     * @param <Response> 接口返回类型
     * @return 分页查询到到全量数据
     */
    private <T, Request, Response> List<T> page(Request request, Function<Request, Response> exec, Function<Response, List<T>> getList, BiFunction<Request, Response, Boolean> hasNext, Consumer<Request> next, Integer reTry, List<T> collector) {
        Response response = null;
        try {
            response = exec.apply(request);
        } catch (Exception e) {
            if (reTry > 0 && e instanceof ReTryException) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
                page(request, exec, getList, hasNext, next, reTry - 1, collector);
            } else {
                throw e;
            }
        }
        if (response != null) {
            List<T> responseList = getList.apply(response);
            collector.addAll(responseList);
            if (hasNext.apply(request, response)) {
                next.accept(request);
                page(request, exec, getList, hasNext, next, reTry, collector);
            }
        }

        return collector;
    }

}
