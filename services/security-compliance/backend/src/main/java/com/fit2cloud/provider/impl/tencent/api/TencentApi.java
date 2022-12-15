package com.fit2cloud.provider.impl.tencent.api;

import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.provider.impl.tencent.entity.request.ListCvmInstanceRequest;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.Instance;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  12:01}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TencentApi {
    /**
     * 获取腾讯云服务器实例列表
     *
     * @param request 请求对象
     * @return 腾讯云服务器实例列表
     */
    public static List<Instance> listEcsInstance(ListCvmInstanceRequest request) {
        CvmClient cvmClient = request.getCredential().getCvmClient(request.getRegionId());
        request.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
        request.setLimit(PageUtil.DefaultPageSize.longValue());
        return PageUtil.page(request,
                req -> {
                    try {
                        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
                        BeanUtils.copyProperties(req, describeInstancesRequest);
                        return cvmClient.DescribeInstances(describeInstancesRequest);
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        throw new RuntimeException(e);
                    }
                },
                res -> Arrays.stream(res.getInstanceSet()).toList(),
                (req, res) -> req.getLimit() <= res.getInstanceSet().length,
                req -> req.setOffset(req.getOffset() + req.getLimit()));

    }
}
