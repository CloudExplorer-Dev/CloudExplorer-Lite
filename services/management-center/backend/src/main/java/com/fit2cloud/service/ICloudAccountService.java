package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.controller.request.cloud_account.*;
import com.fit2cloud.controller.response.cloud_account.CloudAccountJobDetailsResponse;
import com.fit2cloud.controller.response.cloud_account.PlatformResponse;
import com.fit2cloud.controller.response.cloud_account.ResourceCountResponse;
import com.fit2cloud.dao.entity.CloudAccount;
import com.fit2cloud.request.cloud_account.SyncRequest;
import com.fit2cloud.response.cloud_account.SyncResource;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 */
public interface ICloudAccountService extends IService<CloudAccount> {

    /**
     * 分特查询云账号
     *
     * @param cloudAccountRequest 云账号请求对象
     * @return 分页对象
     */
    IPage<CloudAccount> page(CloudAccountRequest cloudAccountRequest);

    /**
     * 获取云账号供应商数据
     *
     * @return 所有的云账号供应商数据
     */
    List<PlatformResponse> getPlatforms();

    /**
     * 插入一条数据
     *
     * @param addCloudAccountRequest 保存云账号参数
     * @return 插入对象
     */
    CloudAccount save(AddCloudAccountRequest addCloudAccountRequest);

    /**
     * 根据云账号获取区域信息
     *
     * @param accountId 根据云账号获取区域信息
     * @return 区域信息
     */
    List<Credential.Region> listRegions(String accountId);


    /**
     * 查询到当前云账号任务信息
     *
     * @param accountId 云账号id
     * @return 云账号任务信息
     */
    CloudAccountJobDetailsResponse jobs(String accountId);

    /**
     * 更新云账号
     *
     * @param updateJobsRequest 云账号任务参数
     * @return 更新后云账号定时任务数据
     */
    CloudAccountJobDetailsResponse updateJob(UpdateJobsRequest updateJobsRequest);


    /**
     * 更新云账号信息
     *
     * @param updateCloudAccountRequest 云账号信息相关参数
     * @return 更新后的云账号信息
     */
    CloudAccount update(UpdateCloudAccountRequest updateCloudAccountRequest);

    /**
     * 删除云账号
     *
     * @param accountId 删除云账号
     * @return 删除
     */
    boolean delete(String accountId);

    /**
     * 删除云账号根据家云账号id
     *
     * @param cloudAccountIds 云账号id
     * @return 是否删除成功
     */
    boolean delete(ArrayList<String> cloudAccountIds);

    /**
     * 校验云账号是否有效
     *
     * @param accountId 需要校验的云账号
     * @return 云账号对象
     */
    CloudAccount verification(String accountId);

    /**
     * 获取所有模块的云账号同步任务资源
     *
     * @return 所有的模块定时任务
     */
    List<SyncResource> getModuleResourceJob();

    /**
     * 同步任务
     *
     * @param syncRequest 同步任务所需参数
     */
    void sync(SyncRequest syncRequest);

    /*
     * 获取云账户余额
     * @param accountId
     * @return 未获取到余额将返回--
     */
    Object getAccountBalance(String accountId);

    /**
     * 更新云账号名称
     *
     * @param updateAccountNameRequest
     * @return
     */
    Boolean updateAccountName(UpdateAccountNameRequest updateAccountNameRequest);

    /**
     * 获取云账号资源计数
     *
     * @param accountId
     * @return
     */
    List<ResourceCountResponse> resourceCount(String accountId);
}
