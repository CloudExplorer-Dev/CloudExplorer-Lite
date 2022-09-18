package com.fit2cloud.quartz;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.dao.entity.CloudAccount;
import com.fit2cloud.service.ICloudAccountService;
import jdk.jfr.Name;
import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/18  1:00 AM
 * @Version 1.0
 * @注释: 校验云账号定时任务
 */
@Component
@Name("校验云账号定时任务")
public class VerificationCloudAccount implements Job {
    @Resource
    private ICloudAccountService cloudAccountService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<CloudAccount> list = cloudAccountService.list();
        for (CloudAccount cloudAccount : list) {
            PlatformConstants platformConstants = PlatformConstants.valueOf(cloudAccount.getPlatform());
            boolean verification = platformConstants.getCredentialClass().getConstructor().newInstance().verification();
            if (!cloudAccount.getState().equals(verification)) {
                cloudAccount.setState(verification);
                cloudAccountService.updateById(cloudAccount);
            }
        }
    }
}
