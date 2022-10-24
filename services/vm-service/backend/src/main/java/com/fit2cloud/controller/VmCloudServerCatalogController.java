package com.fit2cloud.controller;

import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.ProviderConstants;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/server/catalog")
@Validated
@Api("新建虚拟机")
public class VmCloudServerCatalogController {

    @Resource
    private IBaseCloudAccountService cloudAccountService;

    @GetMapping("/form/{cloudAccountId}")
    public ResultHolder<FormObject> getCreateServerForm(@PathVariable String cloudAccountId) throws Exception {
        CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
        Class<? extends ICloudProvider> cloudProvider = ProviderConstants.valueOf(cloudAccount.getPlatform()).getCloudProvider();
        return ResultHolder.success(cloudProvider.getConstructor().newInstance().getCreateServerForm());
    }


}
