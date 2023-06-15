package com.fit2cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.BillPolicy;
import com.fit2cloud.base.entity.BillPolicyCloudAccountMapping;
import com.fit2cloud.controller.request.BillingPolicyRequest;
import com.fit2cloud.controller.request.LinkCloudAccountRequest;
import com.fit2cloud.controller.response.BillingPolicyDetailsResponse;
import com.fit2cloud.controller.response.CloudAccountResponse;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/31  18:55}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface IBillingPolicyService extends IService<BillPolicy> {

    List<BillPolicy> listLastPolicy();

    /**
     * 查询当前策略可关联的云账号
     *
     * @param billingPolicy 计费策略
     * @return 可关联的云账号列表
     */
    List<CloudAccountResponse> listCloudAccountByPolicy(String billingPolicy);

    /**
     * 更新计费策略
     *
     * @param billingPolicyId 计费策略id
     * @param request         计费策略对象
     */
    void updateBillingPolicy(String billingPolicyId, BillingPolicyRequest request);

    /**
     * @param billingPolicyId 策略id
     * @return 策略数据
     */
    BillingPolicyDetailsResponse detailsBillingPolicy(String billingPolicyId);

    /**
     * 创建策略
     *
     * @param request 请求对象
     */
    BillPolicy createBillingPolicy(BillingPolicyRequest request);

    /**
     * 关联云账号
     *
     * @param request 请求对象
     */
    List<BillPolicyCloudAccountMapping> linkCloudAccount(LinkCloudAccountRequest request);

    /**
     * 删除一个策略
     *
     * @param billingPolicyId 策略id
     * @return 是否成功
     */
    boolean remove(String billingPolicyId);

}
