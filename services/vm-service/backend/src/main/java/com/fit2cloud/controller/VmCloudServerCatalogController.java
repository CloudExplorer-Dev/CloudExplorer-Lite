package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fit2cloud.autoconfigure.PluginsContextHolder;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudDiskMapper;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.Good;
import com.fit2cloud.vm.ICloudProvider;
import com.fit2cloud.vm.constants.F2CDiskStatus;
import com.fit2cloud.vm.constants.F2CInstanceStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
@RequestMapping("/api/server/catalog")
@Validated
@Tag(name = "创建云主机")
public class VmCloudServerCatalogController {

    @Resource
    private IBaseCloudAccountService cloudAccountService;

    @Resource
    private BaseVmCloudServerMapper cloudServerMapper;

    @Resource
    private BaseVmCloudDiskMapper diskMapper;

    @GetMapping("/form/{cloudAccountId}")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:CREATE')")
    public ResultHolder<FormObject> getCreateServerForm(@PathVariable String cloudAccountId) throws Exception {
        CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
        ICloudProvider platformExtension = PluginsContextHolder.getPlatformExtension(ICloudProvider.class, cloudAccount.getPlatform());
        return ResultHolder.success(platformExtension.getCreateServerForm());
    }

    @GetMapping("/goods")
    @PreAuthorize("@cepc.hasAnyCePermission('CLOUD_SERVER:CREATE')")
    public ResultHolder<List<Good>> listGoods() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            List<CloudAccount> accounts = cloudAccountService.listSupportCloudAccount(new LambdaQueryWrapper<CloudAccount>().eq(CloudAccount::getState, true));
            Map<String, Good> map = new HashMap<>();
            List<CompletableFuture<Good>> futureList = new ArrayList<>();

            accounts.forEach(cloudAccount -> {
                futureList.add(
                        CompletableFuture
                                .supplyAsync(() -> {
                                    Good good = new Good();
                                    good.setId(cloudAccount.getId());
                                    try {
                                        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<VmCloudServer>()
                                                .eq(VmCloudServer::getAccountId, cloudAccount.getId())
                                                .notIn(VmCloudServer::getInstanceStatus, Arrays.asList(
                                                        F2CInstanceStatus.Deleted.name(),
                                                        F2CInstanceStatus.Failed.name(),
                                                        F2CInstanceStatus.Creating.name()));
                                        good.setServerCount(cloudServerMapper.selectCount(queryWrapper));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<VmCloudDisk>()
                                                .eq(VmCloudDisk::getAccountId, cloudAccount.getId())
                                                .notIn(VmCloudDisk::getStatus, Arrays.asList(
                                                        F2CDiskStatus.DELETED,
                                                        F2CDiskStatus.CREATING));
                                        good.setDiskCount(diskMapper.selectCount(queryWrapper));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        good.setBalance(cloudAccountService.getAccountBalance(cloudAccount.getId()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return good;
                                }, executorService)
                                .whenComplete(((result, error) -> {
                                    map.put(result.getId(), result);
                                })));
            });

            CompletableFuture<Void> allTask = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
            allTask.join();

            List<Good> goods = new ArrayList<>();
            accounts.forEach(cloudAccount -> {
                Good result = new Good();
                BeanUtils.copyProperties(cloudAccount, result);
                Good good = map.get(cloudAccount.getId());

                if (good != null) {
                    result.setBalance(good.getBalance())
                            .setServerCount(good.getServerCount())
                            .setDiskCount(good.getDiskCount())
                            .setPublicCloud(PluginsContextHolder.getPlatformExtension(IBaseCloudProvider.class, cloudAccount.getPlatform()).getCloudAccountMeta().publicCloud)
                    ;
                }
                goods.add(result);
            });

            return ResultHolder.success(goods);
        } finally {
            executorService.shutdown();
        }
    }


}
