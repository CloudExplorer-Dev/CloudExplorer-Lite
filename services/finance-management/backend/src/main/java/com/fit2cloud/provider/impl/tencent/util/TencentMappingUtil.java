package com.fit2cloud.provider.impl.tencent.util;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.constants.BillModeConstants;
import com.fit2cloud.provider.impl.tencent.entity.csv.TencentCsvModel;
import com.fit2cloud.provider.impl.tencent.entity.request.SyncBillRequest;
import com.tencentcloudapi.billing.v20180709.models.BillDetail;
import com.tencentcloudapi.billing.v20180709.models.BillDetailComponent;
import com.tencentcloudapi.billing.v20180709.models.BillTagInfo;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/18  2:00 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TencentMappingUtil {

    /**
     * 讲存储桶文件对象转换为系统账单文件对象
     *
     * @param csvModel 存储桶文件对象
     * @param regions  区域数据
     * @param request  请求对象
     * @return 系统账单对象
     */
    public static CloudBill toCloudBillByBucket(TencentCsvModel csvModel, List<Credential.Region> regions, SyncBillRequest request) {
        CloudBill cloudBill = new CloudBill();
        cloudBill.setId(UUID.randomUUID().toString().replace("-", ""));
        cloudBill.setProjectId(csvModel.getEntryName());
        cloudBill.setProjectName(csvModel.getEntryName());
        cloudBill.setRegionId(csvModel.getRegion());
        cloudBill.setRegionName(csvModel.getRegion());
        regions.stream().filter(r -> StringUtils.equals(r.getName(), csvModel.getRegion().trim())).findFirst().ifPresent(r -> cloudBill.setRegionId(r.getRegionId()));
        cloudBill.setZone(csvModel.getAvailabilityZone());
        cloudBill.setResourceId(csvModel.getResourceId());
        cloudBill.setResourceName(csvModel.getInstanceName());
        cloudBill.setBillMode(toBillMode(csvModel.getBillingMode()));
        cloudBill.setUsageStartDate((CommonUtil.getLocalDateTime(csvModel.getStartTime(), "yyyy-MM-dd HH:mm:ss")));
        cloudBill.setUsageEndDate(CommonUtil.getLocalDateTime(csvModel.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
        cloudBill.setBillingCycle((CommonUtil.getLocalDateTime(request.getMonth(), "yyyy-MM")));
        cloudBill.setDeductionDate((CommonUtil.getLocalDateTime(csvModel.getDeductionTime(), "yyyy-MM-dd HH:mm:ss")));
        cloudBill.setTags(MapUtils.isEmpty(csvModel.getTag()) ? new HashMap<>() : (Map) csvModel.getTag());
        cloudBill.setPayAccountId(csvModel.getPayerUin());
        cloudBill.setProductName(csvModel.getProductName());
        cloudBill.setProductDetail(csvModel.getSubProductName());
        cloudBill.setProductId(csvModel.getProductName());
        cloudBill.setTotalCost(BigDecimal.valueOf(csvModel.getComponentOriginalPrice()));
        cloudBill.setRealTotalCost(BigDecimal.valueOf(csvModel.getDiscountTotalPrice()));
        cloudBill.setProvider(PlatformConstants.fit2cloud_tencent_platform.name());
        return cloudBill;
    }

    /**
     * 将腾讯云账单对象转换为系统账单对象
     *
     * @param item 腾讯云账单对象
     * @return 系统账单对象
     */
    public static CloudBill toCloudBill(BillDetail item, SyncBillRequest request) {
        CloudBill cloudBill = new CloudBill();
        cloudBill.setId(UUID.randomUUID().toString().replace("-", ""));
        cloudBill.setProjectId(item.getProjectId().toString());
        cloudBill.setProjectName(item.getProjectName());
        cloudBill.setRegionId(item.getRegionId());
        cloudBill.setRegionName(item.getRegionName());
        cloudBill.setZone(item.getZoneName());
        cloudBill.setResourceId(item.getResourceId());
        cloudBill.setResourceName(item.getResourceName());
        cloudBill.setBillMode(toBillMode(item.getPayModeName()));
        cloudBill.setBillingCycle(CommonUtil.getLocalDateTime(request.getMonth(), "yyyy-MM"));
        cloudBill.setUsageStartDate(CommonUtil.getLocalDateTime(item.getFeeBeginTime(), "yyyy-MM-dd HH:mm:ss"));
        cloudBill.setUsageEndDate(CommonUtil.getLocalDateTime(item.getFeeEndTime(), "yyyy-MM-dd HH:mm:ss"));
        cloudBill.setDeductionDate(CommonUtil.getLocalDateTime(item.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
        cloudBill.setTags(Arrays.stream(item.getTags()).collect(Collectors.toMap(BillTagInfo::getTagKey, BillTagInfo::getTagValue)));
        cloudBill.setPayAccountId(item.getPayerUin());
        cloudBill.setProductName(item.getBusinessCodeName());
        cloudBill.setProductDetail(item.getProductCodeName());
        cloudBill.setProductId(item.getBusinessCode());
        cloudBill.setProvider(PlatformConstants.fit2cloud_tencent_platform.name());
        BillDetailComponent[] componentSet = item.getComponentSet();
        DoubleSummaryStatistics cost = Arrays.stream(componentSet).collect(Collectors.summarizingDouble(c -> Double.parseDouble(c.getCost())));
        DoubleSummaryStatistics realTotalCost = Arrays.stream(componentSet).collect(Collectors.summarizingDouble(c -> Double.parseDouble(c.getRealCost())));
        cloudBill.setTotalCost(BigDecimal.valueOf(cost.getSum()));
        cloudBill.setRealTotalCost(BigDecimal.valueOf(realTotalCost.getSum()));
        return cloudBill;
    }


    /**
     * 计费模式。
     * <p>
     * 1：包年/包月
     * 3：按需
     * 10：预留实例
     * 将华为云的计费模式转换为云管计费模式
     *
     * @param payModeName 华为云计费模式
     * @return 云管计费模式
     */
    private static String toBillMode(String payModeName) {
        if (payModeName.equals("包年包月")) {
            return BillModeConstants.MONTHLY.name();
        } else if (payModeName.equals("按量计费")) {
            return BillModeConstants.ON_DEMAND.name();
        } else {
            return BillModeConstants.OTHER.name();
        }
    }


}
