package com.fit2cloud.quartz;

import com.fit2cloud.autoconfigure.PluginsContextHolder;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import com.fit2cloud.dao.entity.CloudAccount;
import com.fit2cloud.service.ICloudAccountService;
import jakarta.annotation.Resource;
import jdk.jfr.Name;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author:张少虎
 * @Date: 2022/9/18  1:00 AM
 * @Version 1.0
 * @注释: 校验云账号定时任务
 */
@Component
@Name("校验云账号定时任务")
public class VerificationCloudAccountJob extends AsyncJob implements Job {
    @Resource
    private ICloudAccountService cloudAccountService;

    @Override
    @SneakyThrows
    protected void run(Map<String, Object> map) {
        List<CloudAccount> list = cloudAccountService.list();
        for (CloudAccount cloudAccount : list) {

            Optional<IBaseCloudProvider> first = PluginsContextHolder.getExtensions(IBaseCloudProvider.class).stream()
                    .filter(p -> p.getCloudAccountMeta().platform.equals(cloudAccount.getPlatform()))
                    .findFirst();

            if (first.isPresent()) {
                Credential verification = first.get()
                        .getCloudAccountMeta().credential
                        .getConstructor()
                        .newInstance()
                        .deCode(cloudAccount.getCredential());
                try {
                    verification.verification();
                    cloudAccount.setState(true);
                    cloudAccountService.updateById(cloudAccount);
                } catch (Exception e) {
                    cloudAccount.setState(false);
                    cloudAccountService.updateById(cloudAccount);
                }
            }

        }
    }
}
