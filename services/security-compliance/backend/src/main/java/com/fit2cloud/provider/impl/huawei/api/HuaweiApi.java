package com.fit2cloud.provider.impl.huawei.api;

import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.provider.impl.huawei.entity.request.ListEcsInstanceRequest;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.model.ListServersDetailsResponse;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  12:01}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class HuaweiApi {
    /**
     * 获取华为云ecs实例列表
     *
     * @param request 请求对象
     * @return ecs实例列表
     */
    public static List<ServerDetail> listEcsInstance(ListEcsInstanceRequest request) {
        EcsClient ecsClient = request.getCredential().getEcsClient(request.getRegionId());
        request.setLimit(PageUtil.DefaultPageSize);
        request.setOffset(PageUtil.DefaultCurrentPage);
        return PageUtil.page(request, req -> {
                    try {
                        return ecsClient.listServersDetails(request);
                    } catch (Exception e) {
                        ReTryException.throwHuaweiReTry(e);
                        throw new Fit2cloudException(10000, "获取数据失败" + e.getMessage());
                    }
                },
                ListServersDetailsResponse::getServers,
                (req, res) -> req.getLimit() <= res.getServers().size(),
                req -> req.setOffset(req.getOffset() + 1));
    }
}
