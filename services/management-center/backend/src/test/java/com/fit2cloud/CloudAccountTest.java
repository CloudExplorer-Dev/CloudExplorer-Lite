package com.fit2cloud;

import com.fit2cloud.common.scheduler.SchedulerService;
import com.fit2cloud.dao.entity.CloudAccount;
import com.fit2cloud.service.ICloudAccountService;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.ee.jmx.jboss.QuartzService;
import org.quartz.impl.QuartzServer;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
import java.util.*;
import java.util.Calendar;

/**
 * @Author:张少虎
 * @Date: 2022/9/1  5:36 PM
 * @Version 1.0
 * @注释:
 */
@SpringBootTest(classes = ManagementApplication.class)
@TestPropertySource(locations = {
        "classpath:commons.properties",
        "file:${ce.config.file}"
})
public class CloudAccountTest {


    @Resource
    private ICloudAccountService cloudAccountService;

    @Test
    public void save() {
        CloudAccount cloudAccount = new CloudAccount();
        cloudAccount.setId(UUID.randomUUID().toString());
        cloudAccount.setName("超时");
        cloudAccount.setPlatform("xx");
        cloudAccount.setCredential("xxsda");
        cloudAccountService.save(cloudAccount);
    }

}
