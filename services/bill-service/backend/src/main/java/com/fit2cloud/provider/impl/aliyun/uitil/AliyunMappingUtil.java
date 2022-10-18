package com.fit2cloud.provider.impl.aliyun.uitil;

import com.aliyun.bssopenapi20171214.models.DescribeInstanceBillResponseBody;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.impl.aliyun.entity.request.SyncBillRequest;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/14  5:39 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliyunMappingUtil {

    public static CloudBill toCloudBill(DescribeInstanceBillResponseBody.DescribeInstanceBillResponseBodyDataItems item, SyncBillRequest syncBillRequest) {
        CloudBill cloudBill = new CloudBill();
        String subscriptionType = item.getSubscriptionType();
        cloudBill.setBillMode(subscriptionType);
        cloudBill.setRegion(item.getRegion());
        cloudBill.setZone(item.getZone());
        cloudBill.setTags(toTagsMap(item.getTag()));
        cloudBill.setProjectId(item.getCostUnit());
        cloudBill.setProductName(item.getProductName());
        cloudBill.setReousrceId(item.getInstanceID());
        cloudBill.setProjectName(item.getCostUnit());
        cloudBill.setProductId(item.getProductCode());
        cloudBill.setResourceName(item.getNickName());
        cloudBill.setProvider(PlatformConstants.fit2cloud_ali_platform.name());
        if (StringUtils.isNotEmpty(item.getBillingDate())) {
            cloudBill.setUsageStartDate(CommonUtil.getLocalDateTime(item.getBillingDate(), "yyyy-MM-dd"));
            cloudBill.setBillingCycle(CommonUtil.getLocalDateTime(item.getBillingDate(), "yyyy-MM-dd"));
            cloudBill.setUsageEndDate(CommonUtil.getLocalDateTime(item.getBillingDate(), "yyyy-MM-dd"));
        } else {
            cloudBill.setUsageStartDate(CommonUtil.getLocalDateTime(syncBillRequest.getBillingCycle(), "yyyy-MM"));
            cloudBill.setBillingCycle(CommonUtil.getLocalDateTime(syncBillRequest.getBillingCycle(), "yyyy-MM"));
            cloudBill.setUsageEndDate(CommonUtil.getLocalDateTime(syncBillRequest.getBillingCycle(), "yyyy-MM"));
        }
        cloudBill.setPayAccountId(item.getBillAccountID());
        cloudBill.setTotalCost(BigDecimal.valueOf(item.getPretaxGrossAmount()));
        cloudBill.setRealTotalCost(BigDecimal.valueOf(item.getCashAmount()));
        return cloudBill;
    }

    /**
     * 获取标签map
     *
     * @param tags 标签
     * @return 标签对象
     */
    private static Map<String, Object> toTagsMap(String tags) {
        //  key:testKey value:testValue; key:testKey1 value:testValues1
        if (StringUtils.isNotEmpty(tags)) {
            String[] split = tags.split(";");
            return Arrays.stream(split).flatMap(item -> {
                String[] s = item.split(" ");
                String key = s[0].replace("key:", "").trim();
                String value = s[1].replace("value:", "").trim();
                return new HashMap<String, Object>() {{
                    put(key, value);
                }}.entrySet().stream();
            }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } else {
            return new HashMap<>();
        }
    }

}
