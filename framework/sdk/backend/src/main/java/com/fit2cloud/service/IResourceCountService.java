package com.fit2cloud.service;

import com.fit2cloud.response.cloud_account.ResourceCountResponse;

import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/10/9 2:42 PM
 */
public interface IResourceCountService {
    /**
     * 获取资源计数
     * @param accountId
     * @return
     */
    List<ResourceCountResponse> count(String accountId);
}
