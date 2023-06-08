package com.fit2cloud;

import com.fit2cloud.autoconfigure.ChargingConfig;
import com.fit2cloud.base.mapper.BaseResourceInstanceStateMapper;
import com.fit2cloud.base.service.IBaseBillPolicyService;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.base.service.IBaseWorkspaceService;
import com.fit2cloud.common.charging.constants.BillingGranularityConstants;
import com.fit2cloud.common.charging.entity.InstanceBill;
import com.fit2cloud.common.charging.generation.impl.SimpleBillingGeneration;
import com.fit2cloud.common.charging.instance.impl.SimpleInstanceStateRecorder;
import com.fit2cloud.dto.charging.ChargingModuleInfo;
import com.fit2cloud.service.IVmCloudServerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/17  3:25 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@SpringBootTest(classes = VmServiceApplication.class)
@TestPropertySource(locations = {
        "classpath:commons.properties",
        "file:${ce.config.file}"
})
public class BillingTest {
    @Resource
    private IVmCloudServerService vmCloudServerService;
    @Resource
    private IBaseWorkspaceService workspaceService;
    @Resource
    private IBaseOrganizationService organizationService;
    @Resource
    private BaseResourceInstanceStateMapper resourceInstanceStateMapper;
    @Resource
    private IBaseBillPolicyService iBaseBillPolicyService;
    @Resource
    private RestTemplate restTemplate;


    @Test
    public void saveBillingPolicy() {
        ChargingModuleInfo billSettings = ChargingConfig.getBillSettings();
        List<Void> voids = billSettings.getBillSettings().stream().map(billSetting -> {
            return CompletableFuture.runAsync(() -> {
                SimpleInstanceStateRecorder.of(billSetting).run();
            });
        }).map(CompletableFuture::join).toList();

    }

    @Test
    public void saveBillingState() {
        ChargingModuleInfo billSettings = ChargingConfig.getBillSettings();
        List<List<InstanceBill>> lists = billSettings.getBillSettings().stream().map(billSetting -> {
            return CompletableFuture.supplyAsync(() -> {
                return SimpleBillingGeneration.of(billSetting)
                        .generation("b26d1f637119438902c7b5be586ba76c", "2023-06", BillingGranularityConstants.DAY);

            });
        }).map(CompletableFuture::join).toList();
        System.out.println(lists);

    }
}
