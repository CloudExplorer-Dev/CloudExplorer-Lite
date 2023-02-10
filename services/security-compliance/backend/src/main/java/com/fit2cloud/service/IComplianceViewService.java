package com.fit2cloud.service;

import com.fit2cloud.controller.request.view.ComplianceCountRequest;
import com.fit2cloud.controller.request.view.ComplianceGroupRequest;
import com.fit2cloud.controller.response.view.ComplianceViewCountResponse;
import com.fit2cloud.controller.response.view.ComplianceViewGroupResponse;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/10  12:12}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface IComplianceViewService {


    /**
     * 分组聚合数据
     *
     * @param request 请求对象
     * @return 分组数据
     */
    List<ComplianceViewGroupResponse> group(ComplianceGroupRequest request);

    /**
     * 获取聚合数据
     *
     * @param request 请求对象
     * @return 聚合数据
     */
    ComplianceViewCountResponse getComplianceViewCountResponse(ComplianceCountRequest request);
}
