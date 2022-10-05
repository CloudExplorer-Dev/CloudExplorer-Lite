package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.autoconfigure.ServerInfo;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.vo.Form;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.constants.CloudAccountConstants;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.cloud_account.AddCloudAccountRequest;
import com.fit2cloud.controller.request.cloud_account.CloudAccountRequest;
import com.fit2cloud.controller.request.cloud_account.UpdateCloudAccountRequest;
import com.fit2cloud.controller.request.cloud_account.UpdateJobsRequest;
import com.fit2cloud.controller.response.cloud_account.CloudAccountJobDetailsResponse;
import com.fit2cloud.controller.response.cloud_account.PlatformResponse;
import com.fit2cloud.dao.entity.CloudAccount;
import com.fit2cloud.dao.mapper.CloudAccountMapper;
import com.fit2cloud.request.cloud_account.CloudAccountModuleJob;
import com.fit2cloud.request.cloud_account.SyncRequest;
import com.fit2cloud.response.cloud_account.SyncResource;
import com.fit2cloud.service.ICloudAccountService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class CloudAccountServiceImpl extends ServiceImpl<CloudAccountMapper, CloudAccount> implements ICloudAccountService {
    @Resource(name = "securityContextWorkThreadPool")
    private DelegatingSecurityContextExecutor securityContextWorkThreadPool;
    @Resource
    private RestTemplate restTemplate;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private IBaseCloudAccountService baseCloudAccountService;

    private final static String httpPrefix = "https://";
    /**
     * 获取模块云账号任务
     */
    private final BiFunction<String, String, ResultHolder<CloudAccountModuleJob>> getCloudAccountJobApi = (String moduleName, String accountId) -> {
        List<ServiceInstance> instances = discoveryClient.getInstances(moduleName);
        ServiceInstance serviceInstance = instances.stream().findAny().get();
        final String url = httpPrefix + moduleName + "/" + moduleName + "/api/base/cloud_account/job/" + accountId;
        ResponseEntity<ResultHolder<CloudAccountModuleJob>> cloudAccountJob = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<ResultHolder<CloudAccountModuleJob>>() {
        });
        return cloudAccountJob.getBody();
    };

    /**
     * 初始化指定模块云账号定时任务
     */
    private final BiConsumer<String, String> initCloudAccountModuleJob = (String moduleName, String accountId) -> {
        final String url = httpPrefix + moduleName + "/" + moduleName + "/api/base/cloud_account/job_init/" + accountId;
        ResponseEntity<ResultHolder<Object>> exchange = restTemplate.exchange(url, HttpMethod.POST, HttpEntity.EMPTY, new ParameterizedTypeReference<ResultHolder<Object>>() {
        });
        if (!Objects.requireNonNull(exchange.getBody()).getCode().equals(200)) {
            throw new Fit2cloudException(exchange.getBody().getCode(), exchange.getBody().getMessage());
        }
    };

    /**
     * 修改定时任务
     */
    private final BiFunction<CloudAccountModuleJob, String, CloudAccountModuleJob> updateJob = (CloudAccountModuleJob cloudAccountModuleJob, String accountId) -> {
        final String url = httpPrefix + cloudAccountModuleJob.getModule() + "/" + cloudAccountModuleJob.getModule() + "/api/base/cloud_account/job/" + accountId;
        HttpEntity<CloudAccountModuleJob> cloudAccountModuleJobHttpEntity = new HttpEntity<>(cloudAccountModuleJob);
        ResponseEntity<ResultHolder<CloudAccountModuleJob>> exchange = restTemplate.exchange(url, HttpMethod.PUT, cloudAccountModuleJobHttpEntity, new ParameterizedTypeReference<ResultHolder<CloudAccountModuleJob>>() {
        });
        if (!Objects.requireNonNull(exchange.getBody()).getCode().equals(200)) {
            throw new Fit2cloudException(exchange.getBody().getCode(), exchange.getBody().getMessage());
        }
        return exchange.getBody().getData();
    };

    /**
     * 获取同步任务资源
     */
    private final Function<String, List<SyncResource>> getResourceJob = (String module) -> {
        final String url = httpPrefix + module + "/" + module + "/api/base/cloud_account/job/resource";

        ResponseEntity<ResultHolder<List<SyncResource>>> exchange = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<ResultHolder<List<SyncResource>>>() {
        });
        if (!Objects.requireNonNull(exchange.getBody()).getCode().equals(200)) {
            throw new Fit2cloudException(exchange.getBody().getCode(), exchange.getBody().getMessage());
        }
        return exchange.getBody().getData();
    };

    /**
     * 发送请求给各个模块触发同步任务
     */
    private final BiFunction<String, SyncRequest, Boolean> sync = (String module, SyncRequest request) -> {
        final String url = httpPrefix + module + "/" + module + "/api/base/cloud_account/sync";
        HttpEntity<SyncRequest> requestHttpEntity = new HttpEntity<>(request);
        ResponseEntity<ResultHolder<Boolean>> exchange = restTemplate.exchange(url, HttpMethod.POST, requestHttpEntity, new ParameterizedTypeReference<ResultHolder<Boolean>>() {
        });
        if (!Objects.requireNonNull(exchange.getBody()).getCode().equals(200)) {
            throw new Fit2cloudException(exchange.getBody().getCode(), exchange.getBody().getMessage());
        }
        return exchange.getBody().getData();
    };

    @Override

    public IPage<CloudAccount> page(CloudAccountRequest cloudAccountRequest) {
        Page<CloudAccount> cloudAccountPage = PageUtil.of(cloudAccountRequest, CloudAccount.class, new OrderItem("create_time", true));
        LambdaQueryWrapper<CloudAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(cloudAccountRequest.getName()), CloudAccount::getName, cloudAccountRequest.getName()).in(CollectionUtils.isNotEmpty(cloudAccountRequest.getPlatform()), CloudAccount::getPlatform, cloudAccountRequest.getPlatform()).in(CollectionUtils.isNotEmpty(cloudAccountRequest.getState()), CloudAccount::getState, cloudAccountRequest.getState()).in(CollectionUtils.isNotEmpty(cloudAccountRequest.getStatus()), CloudAccount::getStatus, cloudAccountRequest.getStatus()).between(CollectionUtils.isNotEmpty(cloudAccountRequest.getUpdateTime()), CloudAccount::getUpdateTime, CollectionUtils.isNotEmpty(cloudAccountRequest.getUpdateTime()) ? simpleDateFormat.format(cloudAccountRequest.getUpdateTime().get(0)) : "", CollectionUtils.isNotEmpty(cloudAccountRequest.getUpdateTime()) ? simpleDateFormat.format(cloudAccountRequest.getUpdateTime().get(1)) : "").between(CollectionUtils.isNotEmpty(cloudAccountRequest.getCreateTime()), CloudAccount::getCreateTime, CollectionUtils.isNotEmpty(cloudAccountRequest.getCreateTime()) ? simpleDateFormat.format(cloudAccountRequest.getCreateTime().get(0)) : "", CollectionUtils.isNotEmpty(cloudAccountRequest.getCreateTime()) ? simpleDateFormat.format(cloudAccountRequest.getCreateTime().get(1)) : "");
        return page(cloudAccountPage, wrapper);
    }

    @Override
    public List<PlatformResponse> getPlatforms() {
        return Arrays.stream(PlatformConstants.values()).map(platform -> {
            PlatformResponse platformResponse = new PlatformResponse();
            Class<? extends Credential> credentialClass = platform.getCredentialClass();
            platformResponse.setLabel(platform.getMessage());
            platformResponse.setField(platform.name());
            try {
                List<Form> form = credentialClass.getConstructor().newInstance().toForm();
                platformResponse.setCredentialFrom(form);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return platformResponse;
        }).toList();
    }

    @Override
    public CloudAccount save(AddCloudAccountRequest addCloudAccountRequest) {
        CloudAccount cloudAccount = new CloudAccount();
        // 获取区域信息
        addCloudAccountRequest.getCredential().regions();
        cloudAccount.setCredential(addCloudAccountRequest.getCredential().enCode());
        cloudAccount.setPlatform(addCloudAccountRequest.getPlatform());
        cloudAccount.setName(addCloudAccountRequest.getName());
        cloudAccount.setState(addCloudAccountRequest.getCredential().verification());
        cloudAccount.setStatus(CloudAccountConstants.Status.INIT);
        save(cloudAccount);
        initCloudJob(cloudAccount.getId());
        return getById(cloudAccount.getId());
    }


    /**
     * 启动定时任务
     */
    private void initCloudJob(String accountId) {
        HashSet<String> ids = new HashSet<>(discoveryClient.getServices());
        ids.add(ServerInfo.module);
        ids.stream().map(server -> CompletableFuture.runAsync(() -> this.initCLoudModuleJob(accountId, server), securityContextWorkThreadPool)).map(CompletableFuture::join).toList();
    }

    @Override
    public List<Credential.Region> listRegions(String accountId) {
        CloudAccount cloudAccount = getById(accountId);
        PlatformConstants platformConstants = PlatformConstants.valueOf(cloudAccount.getPlatform());
        try {
            return platformConstants.getCredentialClass().getConstructor().newInstance().deCode(cloudAccount.getCredential()).regions();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CloudAccountJobDetailsResponse jobs(String accountId) {
        CloudAccountJobDetailsResponse cloudAccountJobDetailsResponse = new CloudAccountJobDetailsResponse();
        HashSet<String> ids = new HashSet<>(discoveryClient.getServices());
        ids.add(ServerInfo.module);
        List<CloudAccountModuleJob> cloudAccountModuleJobs = ids.stream().map(moduleName -> CompletableFuture.supplyAsync(() -> this.getCloudModuleJob(accountId, moduleName), securityContextWorkThreadPool)).map(CompletableFuture::join).filter(Objects::nonNull).toList();
        cloudAccountJobDetailsResponse.setCloudAccountModuleJobs(cloudAccountModuleJobs);
        return cloudAccountJobDetailsResponse;
    }


    /**
     * 获取不同模块到云账号定时任务
     *
     * @param acloudAccountId 云账号id
     * @param moduleName      模块名称
     * @return 模块的云账号定时任务
     */
    private CloudAccountModuleJob getCloudModuleJob(String acloudAccountId, String moduleName) {
        // 如果是当前模块,直接获取
        if (moduleName.equals(ServerInfo.module)) {
            return baseCloudAccountService.getCloudAccountJob(acloudAccountId);
        } else {
            ResultHolder<CloudAccountModuleJob> apply = getCloudAccountJobApi.apply(moduleName, acloudAccountId);
            if (apply.getCode().equals(200)) {
                return apply.getData();
            }
            throw new Fit2cloudException(apply.getCode(), apply.getMessage());
        }
    }

    private void initCLoudModuleJob(String cloudAccountId, String moduleName) {
        if (moduleName.equals(ServerInfo.module)) {
            baseCloudAccountService.initCloudAccountJob(cloudAccountId);
        } else {
            initCloudAccountModuleJob.accept(moduleName, cloudAccountId);
        }
    }

    private CloudAccountModuleJob updateCloudModuleJob(CloudAccountModuleJob cloudAccountModuleJob, String acloudAccountId, String moduleName) {
        if (moduleName.equals(ServerInfo.module)) {
            return baseCloudAccountService.updateJob(cloudAccountModuleJob, acloudAccountId);
        } else {
            return updateJob.apply(cloudAccountModuleJob, acloudAccountId);
        }
    }


    @Override
    public CloudAccountJobDetailsResponse updateJob(UpdateJobsRequest updateJobsRequest) {
        HashSet<String> ids = new HashSet<>(discoveryClient.getServices());
        ids.add(ServerInfo.module);
        List<CloudAccountModuleJob> cloudAccountModuleJobs = ids.stream().map(module -> {
            Optional<CloudAccountModuleJob> first = updateJobsRequest.getCloudAccountModuleJobs().stream().filter(moduleJob -> moduleJob.getModule().equals(module)).findFirst();
            return first.map(cloudAccountModuleJob -> this.updateCloudModuleJob(cloudAccountModuleJob, updateJobsRequest.getCloudAccountId(), module)).orElse(null);
        }).filter(Objects::nonNull).toList();
        CloudAccountJobDetailsResponse cloudAccountJobDetailsResponse = new CloudAccountJobDetailsResponse();
        cloudAccountJobDetailsResponse.setCloudAccountModuleJobs(cloudAccountModuleJobs);
        return cloudAccountJobDetailsResponse;
    }


    @Override
    public CloudAccount update(UpdateCloudAccountRequest updateCloudAccountRequest) {
        CloudAccount cloudAccount = new CloudAccount();
        // 校验ak sk
        updateCloudAccountRequest.getCredential().regions();
        cloudAccount.setCredential(updateCloudAccountRequest.getCredential().enCode());
        cloudAccount.setPlatform(updateCloudAccountRequest.getPlatform());
        cloudAccount.setName(updateCloudAccountRequest.getName());
        cloudAccount.setState(updateCloudAccountRequest.getCredential().verification());
        cloudAccount.setId(updateCloudAccountRequest.getId());
        updateById(cloudAccount);
        return this.getById(updateCloudAccountRequest.getId());
    }

    @Override
    public boolean delete(String accountId) {
        return removeById(accountId);
    }

    @Override
    public boolean delete(ArrayList<String> cloudAccountIds) {
        for (String cloudAccountId : cloudAccountIds) {
            delete(cloudAccountId);
        }
        return true;
    }

    @Override
    public CloudAccount verification(String accountId) {
        CloudAccount cloudAccount = new CloudAccount() {{
            setId(accountId);
            setState(true);
        }};
        try {
            listRegions(accountId);
        } catch (Exception e) {
            cloudAccount.setState(false);
            updateById(cloudAccount);
            throw e;
        }
        updateById(cloudAccount);
        // 校验成功更新数据
        return getById(accountId);
    }

    @Override
    public List<SyncResource> getModuleResourceJob() {
        HashSet<String> ids = new HashSet<>(discoveryClient.getServices());
        ids.add(ServerInfo.module);
        return ids.stream().map(this::getSyncResource).flatMap(Collection::stream).filter(Objects::nonNull).toList();
    }

    @Override
    public void sync(SyncRequest syncRequest) {
        HashSet<String> ids = new HashSet<>(discoveryClient.getServices());
        ids.add(ServerInfo.module);
        ids.stream().map(moduleId -> CompletableFuture.runAsync(() -> sync(moduleId, syncRequest), securityContextWorkThreadPool)).map(CompletableFuture::join).toList();
    }

    private void sync(String moduleId, SyncRequest syncRequest) {
        Optional.of(syncRequest).map(sync -> {
            List<SyncRequest.Job> jobs = sync.getSyncJob().stream().filter(j -> j.getModule().equals(moduleId)).toList();
            if (CollectionUtils.isNotEmpty(jobs)) {
                SyncRequest r = new SyncRequest();
                r.setCloudAccountId(syncRequest.getCloudAccountId());
                r.setSyncJob(jobs);
                r.setRegions(syncRequest.getRegions());
                return r;
            } else {
                return null;
            }
        }).ifPresent(moduleSyncRequest -> sync.apply(moduleId, moduleSyncRequest));

    }

    /**
     * 获取同步数据
     *
     * @param module 模块名称
     * @return 模块的任务
     */
    public List<SyncResource> getSyncResource(String module) {
        if (module.equals(ServerInfo.module)) {
            return baseCloudAccountService.getModuleResourceJob();
        } else {
            return getResourceJob.apply(module);
        }
    }


}
