package com.fit2cloud.provider.client;

import com.fit2cloud.autoconfigure.RestTemplateConfig;
import com.fit2cloud.base.service.IBaseUserService;
import com.fit2cloud.common.charging.constants.BillingGranularityConstants;
import com.fit2cloud.common.charging.entity.InstanceBill;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.common.utils.ServiceUtil;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.service.TokenPoolService;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/8  17:47}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class PrivateLocalCloudClient {
    private static final String projectNameFormat = "%s(%s)";
    private final KeyValue<String, String> jwt;

    public PrivateLocalCloudClient(String userName) {
        IBaseUserService userService = SpringUtil.getBean(IBaseUserService.class);
        UserDto user = userService.getUserByIdOrEmail(userName);
        KeyValue<String, String> jwt = JwtTokenUtils.createJwtToken(user);
        // 插入redis
        SpringUtil.getBean(TokenPoolService.class).saveJwt(user.getId(), jwt.getKey()).toCompletableFuture().join();
        this.jwt = jwt;
    }

    private RestTemplate getRestTemplate() {
        RestTemplateConfig restTemplateConfig = new RestTemplateConfig();
        return restTemplateConfig.restTemplate();
    }

    /**
     * 获取云账单
     *
     * @param request 请求对象
     * @return 实例账单
     */
    public List<InstanceBill> list(BillRequest request) {
        Set<String> services = ServiceUtil.getServices("gateway", "finance-management");
        String value = jwt.getValue();
        RestTemplate restTemplate = getRestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        httpHeaders.set(JwtTokenUtils.TOKEN_NAME, value);
        String requestUrl = "/api/base/billing/instance_bill" + "?" + request.toRequestParams();
        return services.stream()
                .map(model -> CompletableFuture.supplyAsync(() -> restTemplate.exchange(getHttpUrl(ServiceUtil.getRandomInstance(model), model, requestUrl), HttpMethod.GET, new HttpEntity<>(null, httpHeaders), new ParameterizedTypeReference<ResultHolder<List<InstanceBill>>>() {
                        }))
                ).map(CompletableFuture::join)
                .map(ResponseEntity::getBody).filter(Objects::nonNull)
                .map(ResultHolder::getData)
                .flatMap(List::stream)
                .toList();


    }

    public static String getHttpUrl(ServiceInstance serviceInstance, String module, String apiPath) {
        return String.format("http://%s/%s/%s", serviceInstance.getHost() + ":" + serviceInstance.getPort(), module, apiPath);
    }

    /**
     * 获取云账单
     *
     * @param request 请求
     * @return 列表
     */
    public List<CloudBill> listCloudBill(BillRequest request) {
        return list(request)
                .stream()
                .map(instanceBill -> toCloudBill(instanceBill, request))
                .toList();

    }


    /**
     * 将 InstanceBill 转换为 CloudBill
     *
     * @param bill    私有云计费对象
     * @param request 请求对象
     * @return 云管账单对象
     */
    public static CloudBill toCloudBill(InstanceBill bill, BillRequest request) {
        CloudBill cloudBill = new CloudBill();
        cloudBill.setId(UUID.randomUUID().toString().replace("-", ""));
        cloudBill.setBillMode(bill.getBillMode().name());
        cloudBill.setBillingCycle(CommonUtil.getLocalDateTime(bill.getBillingCycle(), "yyyy-MM"));
        cloudBill.setDeductionDate(CommonUtil.getLocalDateTime(bill.getDeductionTime(), "yyyy-MM-dd"));
        cloudBill.setTags(Map.of());
        cloudBill.setUsageEndDate(CommonUtil.getLocalDateTime(bill.getDeductionTime(), "yyyy-MM-dd"));
        cloudBill.setUsageStartDate(CommonUtil.getLocalDateTime(bill.getDeductionTime(), "yyyy-MM-dd"));
        cloudBill.setResourceName(bill.getResourceName());
        cloudBill.setResourceId(bill.getResourceId());
        cloudBill.setZone(bill.getZone());
        cloudBill.setRegionId(bill.getRegion());
        cloudBill.setRegionName(bill.getRegion());
        cloudBill.setProductId(bill.getProductId());
        cloudBill.setProductName(bill.getProductName());
        cloudBill.setProductDetail(bill.getProductDetail());
        cloudBill.setProvider(request.platform);
        cloudBill.setPayAccountId(bill.getPayAccountId());
        setProject(cloudBill, bill.getOrganizationId(), bill.getOrganizationName(), bill.getWorkspaceId(), bill.getWorkspaceName());
        cloudBill.setRealTotalCost(bill.getTotalPrice());
        cloudBill.setTotalCost(bill.getTotalPrice());
        return cloudBill;
    }

    /**
     * 设置企业项目
     *
     * @param cloudBill        账单对象
     * @param organizationId   组织id
     * @param organizationName 组织名称
     * @param workspaceId      工作空间id
     * @param workspaceName    工作空间名称
     */
    private static void setProject(CloudBill cloudBill,
                                   String organizationId,
                                   String organizationName,
                                   String workspaceId,
                                   String workspaceName) {
        if (StringUtils.isNotEmpty(workspaceId)) {
            cloudBill.setProjectId(workspaceId);
            cloudBill.setProjectName(String.format(projectNameFormat, workspaceName, "工作空间"));
        } else if (StringUtils.isNotEmpty(organizationId)) {
            cloudBill.setProjectId(organizationId);
            cloudBill.setProjectName(String.format(projectNameFormat, organizationName, "组织"));
        } else {
            cloudBill.setProjectId("0");
            cloudBill.setProjectName("默认组织");
        }


    }

    @Getter
    @Setter
    public static class BillRequest {
        /**
         * 月份
         */
        private String month;

        /**
         * 云账号id
         */
        private String cloudAccountId;

        /**
         * 生成粒度
         */
        private BillingGranularityConstants granularity = BillingGranularityConstants.DAY;

        /**
         * 云平台
         */
        private String platform;

        public String toRequestParams() {
            Map<String, String> params = Map.of("month", this.month, "cloudAccountId", this.cloudAccountId,
                    "granularity", this.granularity.name());
            return params
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));
        }
    }
}
