package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.base.mapper.BaseVmCloudDiskMapper;
import com.fit2cloud.base.mapper.BaseVmCloudServerMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.Good;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.ProviderConstants;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
@RequestMapping("/api/server/catalog")
@Validated
@Api("创建虚拟机")
public class VmCloudServerCatalogController {

    @Resource
    private IBaseCloudAccountService cloudAccountService;

    @Resource
    private BaseVmCloudServerMapper cloudServerMapper;

    @Resource
    private BaseVmCloudDiskMapper diskMapper;

    @GetMapping("/form/{cloudAccountId}")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:CREATE')")
    public ResultHolder<FormObject> getCreateServerForm(@PathVariable String cloudAccountId) throws Exception {
        CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
        Class<? extends ICloudProvider> cloudProvider = ProviderConstants.valueOf(cloudAccount.getPlatform()).getCloudProvider();
        return ResultHolder.success(cloudProvider.getConstructor().newInstance().getCreateServerForm());
    }

    @GetMapping("/goods")
    @PreAuthorize("hasAnyCePermission('CLOUD_SERVER:CREATE')")
    public ResultHolder<List<Good>> listGoods() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            List<CloudAccount> accounts = cloudAccountService.list();
            Map<String, Good> map = new HashMap<>();
            List<CompletableFuture<Good>> futureList = new ArrayList<>();

            accounts.forEach(cloudAccount -> {
                futureList.add(
                        CompletableFuture
                                .supplyAsync(() -> {
                                    Good good = new Good();
                                    good.setId(cloudAccount.getId());
                                    try {
                                        good.setServerCount(cloudServerMapper.selectCount(new LambdaQueryWrapper<VmCloudServer>().eq(VmCloudServer::getAccountId, cloudAccount.getId())));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        good.setDiskCount(diskMapper.selectCount(new LambdaQueryWrapper<VmCloudDisk>().eq(VmCloudDisk::getAccountId, cloudAccount.getId())));
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
                            .setDiskCount(good.getDiskCount());
                }
                goods.add(result);
            });

            return ResultHolder.success(goods);
        } finally {
            executorService.shutdown();
        }
    }


}
