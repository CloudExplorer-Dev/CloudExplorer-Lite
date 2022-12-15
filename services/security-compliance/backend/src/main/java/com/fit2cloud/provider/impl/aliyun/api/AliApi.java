package com.fit2cloud.provider.impl.aliyun.api;

import com.aliyun.ecs20140526.Client;
import com.aliyun.ecs20140526.models.DescribeInstancesResponseBody;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListEcsInstancesRequest;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  11:16}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliApi {

    /**
     * 获取阿里云实例列表
     *
     * @param request 请求对象
     * @return 阿里云ecs实例列表
     */
    public static List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance> listECSInstance(ListEcsInstancesRequest request) {
        Client ecsClient = request.getCredential().getEcsClient(request.getRegionId());
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(PageUtil.DefaultPageSize);
        return PageUtil.page(request, req -> {
            try {
                return ecsClient.describeInstancesWithOptions(request, new RuntimeOptions());
            } catch (Exception e) {
                ReTryException.throwReTry(e);
                SkipPageException.throwSkip(e);
                throw new Fit2cloudException(10002, "获取阿里云云主机列表失败" + e.getMessage());
            }
        }, res -> res.getBody().getInstances().instance, (req, res) -> res.getBody().getPageSize() <= res.getBody().getInstances().instance.size(), req -> req.setPageNumber(req.getPageNumber() + 1));
    }
}
