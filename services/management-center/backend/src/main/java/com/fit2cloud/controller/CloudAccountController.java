package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.constants.CloudAccountConstants;
import com.fit2cloud.controller.editor.CredentialEditor;
import com.fit2cloud.controller.editor.OrderEditor;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.cloud_account.*;
import com.fit2cloud.controller.response.cloud_account.CloudAccountJobDetailsResponse;
import com.fit2cloud.controller.response.cloud_account.PlatformResponse;
import com.fit2cloud.dao.entity.CloudAccount;
import com.fit2cloud.dao.entity.Organization;
import com.fit2cloud.dao.mapper.CloudAccountMapper;
import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.service.ICloudAccountService;
import io.swagger.annotations.ApiParam;
import org.springframework.context.MessageSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/8/31  10:49 AM
 * @Version 1.0
 * @注释: 云账号相关接口
 */
@RestController
@RequestMapping("/api/cloud_account")
@Validated
public class CloudAccountController {
    @Resource
    private ICloudAccountService cloudAccountService;

    @GetMapping("page")
    public ResultHolder<IPage<CloudAccount>> page(@Validated CloudAccountRequest cloudAccountRequest) {
        return ResultHolder.success(cloudAccountService.page(cloudAccountRequest));
    }

    @GetMapping("/{id}")
    public ResultHolder<CloudAccount> findCloudAccount(@ApiParam(value = "云账号id", required = true) @CustomValidated(mapper = CloudAccountMapper.class, handler = ExistHandler.class, message = "{i18n.cloud_account_id_not_existent}", exist = false) @PathVariable("id") String id) {
        return ResultHolder.success(cloudAccountService.getById(id));
    }

    @GetMapping("/platform")
    public ResultHolder<List<PlatformResponse>> getPlatform() {
        List<PlatformResponse> platformResponses = cloudAccountService.getPlatforms();
        return ResultHolder.success(platformResponses);
    }

    @PostMapping
    public ResultHolder<CloudAccount> save(@RequestBody AddCloudAccountRequest addCloudAccountRequest) {
        CloudAccount cloudAccount = cloudAccountService.save(addCloudAccountRequest);
        return ResultHolder.success(cloudAccount);
    }

    @PutMapping
    public ResultHolder<CloudAccount> update(@RequestBody UpdateCloudAccountRequest updateCloudAccountRequest) {
        CloudAccount cloudAccount = cloudAccountService.update(updateCloudAccountRequest);
        return ResultHolder.success(cloudAccount);
    }

    @DeleteMapping("/{cloud_account_id}")
    public ResultHolder<Boolean> delete(@ApiParam("云账号id") @PathVariable("cloud_account_id") String accountId) {
        return ResultHolder.success(cloudAccountService.delete(accountId));
    }

    @DeleteMapping
    public ResultHolder<Boolean> deleteBatch(@ApiParam("批量删除组织") @Size(min = 1, message = "最少传入一个云账号id") @RequestBody ArrayList<String> cloudAccountIds) {
        boolean delete = cloudAccountService.delete(cloudAccountIds);
        return ResultHolder.success(delete);
    }

    @GetMapping("/region/{cloud_account_id}")
    public ResultHolder<List<Credential.Region>> region(@ApiParam("云账号id") @PathVariable("cloud_account_id") String accountId) {
        List<Credential.Region> regions = cloudAccountService.listRegions(accountId);
        return ResultHolder.success(regions);
    }

    @GetMapping("/verification/{cloud_account_id}")
    public ResultHolder<CloudAccount> verification(@ApiParam("云账号id") @NotNull(message = "云账号id不能为空")  @PathVariable("cloud_account_id") String accountId) {
        CloudAccount cloudAccount = new CloudAccount() {{
            setId(accountId);
            setState(true);
        }};
        try {
            cloudAccountService.listRegions(accountId);
        } catch (Exception e) {
            cloudAccount.setState(false);
            cloudAccountService.updateById(cloudAccount);
            throw e;
        }
        cloudAccountService.updateById(cloudAccount);
        // 校验成功更新数据
        return ResultHolder.success(cloudAccountService.getById(accountId));
    }

    @GetMapping("/jobs/{cloud_account_id}")
    public ResultHolder<CloudAccountJobDetailsResponse> jobs(@ApiParam("云账号id") @PathVariable("cloud_account_id") String accountId) {
        return ResultHolder.success(cloudAccountService.jobs(accountId));
    }

    @PutMapping("/jobs")
    public ResultHolder<CloudAccountJobDetailsResponse> updateJobs(@RequestBody UpdateJobsRequest updateJobsRequest) {
        return ResultHolder.success(cloudAccountService.updateJob(updateJobsRequest));
    }


    /**
     * 控制器初始化时调用
     * SpringMVC 使用WebDataBinder处理<请求消息,方法入参>的绑定工作
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(OrderRequest.class, new OrderEditor());
        binder.registerCustomEditor(CloudAccountCredentialRequest.class, new CredentialEditor());
        binder.registerCustomEditor(Credential.class, new CredentialEditor());
    }


}
