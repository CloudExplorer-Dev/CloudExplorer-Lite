package com.fit2cloud;

import com.fit2cloud.common.scheduler.SchedulerService;
import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;

/**
 * @Author:张少虎
 * @Date: 2022/9/8  5:39 PM
 * @Version 1.0
 * @注释:
 */
@SpringBootTest(classes = ManagementApplication.class)
@TestPropertySource(locations = {
        "classpath:commons.properties",
        "file:${ce.config.file}"
})
public class SchedulerTest {

    @Resource
    private Scheduler scheduler;
    @Resource
    private SchedulerService schedulerService;
    private String jobName = "测试任务";
    private String groupName = "组1";


    @Test
    public void deleteJob() throws SchedulerException {
        schedulerService.deleteJob(jobName, groupName);
    }
}
