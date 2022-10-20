package com.fit2cloud.provider.impl.tencent.util;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.tencentcloudapi.billing.v20180709.models.BillDetail;
import com.tencentcloudapi.billing.v20180709.models.BillDetailComponent;
import com.tencentcloudapi.billing.v20180709.models.BillTagInfo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/18  2:00 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TencentMappingUtil {
    /**
     * 将腾讯云账单对象转换为系统账单对象
     *
     * @param item 腾讯云账单对象
     * @return 系统账单对象
     */
    public static CloudBill toCloudBill(BillDetail item) {
        CloudBill cloudBill = new CloudBill();
        cloudBill.setId(UUID.randomUUID().toString().replace("-", ""));
        cloudBill.setProjectId(item.getProjectId().toString());
        cloudBill.setProjectName(item.getProjectName());
        cloudBill.setRegionId(item.getRegionId());
        cloudBill.setRegionName(item.getRegionName());
        cloudBill.setZone(item.getZoneName());
        cloudBill.setReousrceId(item.getResourceId());
        cloudBill.setResourceName(item.getResourceName());
        cloudBill.setBillMode(item.getPayModeName());
        cloudBill.setBillingCycle(CommonUtil.getLocalDateTime(item.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
        cloudBill.setUsageStartDate(CommonUtil.getLocalDateTime(item.getFeeBeginTime(), "yyyy-MM-dd HH:mm:ss"));
        cloudBill.setUsageEndDate(CommonUtil.getLocalDateTime(item.getFeeEndTime(), "yyyy-MM-dd HH:mm:ss"));
        cloudBill.setTags(Arrays.stream(item.getTags()).collect(Collectors.toMap(BillTagInfo::getTagKey, BillTagInfo::getTagValue)));
        cloudBill.setPayAccountId(item.getPayerUin());
        cloudBill.setProductName(item.getProductCodeName());
        cloudBill.setProductId(item.getProductCode());
        cloudBill.setProvider(PlatformConstants.fit2cloud_tencent_platform.name());
        BillDetailComponent[] componentSet = item.getComponentSet();
        DoubleSummaryStatistics cost = Arrays.stream(componentSet).collect(Collectors.summarizingDouble(c -> Double.parseDouble(c.getCost())));
        DoubleSummaryStatistics realTotalCost = Arrays.stream(componentSet).collect(Collectors.summarizingDouble(c -> Double.parseDouble(c.getRealCost())));
        cloudBill.setTotalCost(BigDecimal.valueOf(cost.getSum()));
        cloudBill.setRealTotalCost(BigDecimal.valueOf(realTotalCost.getSum()));
        return cloudBill;
    }


}
