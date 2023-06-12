package com.fit2cloud.charging;


import com.fit2cloud.autoconfigure.ChargingConfig;
import com.fit2cloud.charging.constants.VmDiskStateConstants;
import com.fit2cloud.charging.constants.VmServerStateConstants;
import com.fit2cloud.charging.entity.GlobalVmServerConfigMeta;
import com.fit2cloud.charging.handler.VmCloudDiskInstanceRecordMappingHandler;
import com.fit2cloud.charging.handler.VmServerInstanceRecordMappingHandler;
import com.fit2cloud.common.charging.entity.BillingFieldMeta;
import com.fit2cloud.common.charging.setting.BillSetting;
import com.fit2cloud.common.charging.setting.impl.SimpleBillingSetting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/30  18:53}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class Charging implements ChargingConfig.Config {
    @Override
    public List<BillSetting> listBillingSetting() {

        SimpleBillingSetting vmServer = SimpleBillingSetting.of("ECS", "云主机", new VmServerInstanceRecordMappingHandler(),
                Map.of("cpu", new BillingFieldMeta(new BigDecimal("0.05"), new BigDecimal("30"), "CPU", "核", "元",
                                Map.of("previewList", List.of(2, 4, 8, 16))),
                        "memory", new BillingFieldMeta(new BigDecimal("0.05"), new BigDecimal("30"), "内存", "GB", "元",
                                Map.of("previewList", List.of(2, 4, 8, 16, 32))
                        )
                ),
                ((config, state) -> {
                    Object shutdownNotBilling = config.getOrDefault("shutdownNotBilling", true);
                    boolean value = true;
                    if (shutdownNotBilling instanceof Boolean s) {
                        value = s;
                    }
                    List<Character> notBilling = new ArrayList<>();
                    notBilling.add(VmServerStateConstants.NotCreate.getCode());
                    notBilling.add(VmServerStateConstants.Deleted.getCode());
                    if (value) {
                        notBilling.add(VmServerStateConstants.Stopped.getCode());
                    }
                    return !notBilling.contains(state.code());
                }
                ), new GlobalVmServerConfigMeta(true));


        SimpleBillingSetting vmDisk = SimpleBillingSetting.of("DISK", "磁盘", new VmCloudDiskInstanceRecordMappingHandler(),
                Map.of("size", new BillingFieldMeta(new BigDecimal("0.01"), new BigDecimal("5"), "磁盘", "GB", "元",
                        Map.of("previewList", List.of(20, 50, 100, 200)))),
                ((config, state) -> !List.of(VmDiskStateConstants.NotCreate.getCode(), VmDiskStateConstants.deleted.getCode()).contains(state.code())), null);
        return List.of(vmServer, vmDisk);
    }
}
