package com.fit2cloud.vm;

import com.fit2cloud.base.entity.BillPolicyDetails;
import com.fit2cloud.base.service.IBaseUserService;
import com.fit2cloud.common.constants.ChargeTypeConstants;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.utils.ChargingUtil;
import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.common.utils.ServiceUtil;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.service.TokenPoolService;
import com.fit2cloud.vm.entity.request.RenewInstancePriceRequest;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
        return new RestTemplate();
    }

    /**
     * 获取云账单
     *
     * @param cloudAccountId 云账号id
     * @return 实例账单
     */
    public List<BillPolicyDetails> listBillPolicyDetails(String cloudAccountId) {
        Set<String> servicesExcludeGateway = ServiceUtil.getServicesExcludeGateway();
        if (servicesExcludeGateway.contains("finance-management")) {
            String value = jwt.getValue();
            RestTemplate restTemplate = getRestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
            httpHeaders.set(JwtTokenUtils.TOKEN_NAME, value);
            httpHeaders.set(RoleConstants.ROLE_TOKEN, "ADMIN");
            String url = getHttpUrl(ServiceUtil.getRandomInstance("finance-management"), "finance-management", "/api/billing_policy/calculate_config_price/" + cloudAccountId);

            ResponseEntity<ResultHolder<List<BillPolicyDetails>>> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null, httpHeaders), new ParameterizedTypeReference<>() {
            });
            return Objects.requireNonNull(exchange.getBody()).getData();
        }
        return List.of();
    }

    /**
     * 续费实例价钱询价
     *
     * @param request 请求参数
     * @return 价钱
     */
    public BigDecimal renewInstancePrice(RenewInstancePriceRequest request) {
        if (StringUtils.isEmpty(request.getPeriodNum())) {
            return BigDecimal.ZERO;
        }
        List<BillPolicyDetails> billPolicyDetailsList = listBillPolicyDetails(request.getCloudAccountId());
        BigDecimal sum = billPolicyDetailsList.stream().filter(billPolicyDetails -> StringUtils.equals(billPolicyDetails.getResourceType(), "ECS"))
                .map(billPolicyDetails -> ChargingUtil.getBigDecimal(ChargeTypeConstants.PREPAID.getCode(), Map.of("cpu", request.getCpu(), "memory", request.getMem()), billPolicyDetails))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.multiply(new BigDecimal(request.getPeriodNum()));
    }

    public static String getHttpUrl(ServiceInstance serviceInstance, String module, String apiPath) {
        return String.format("http://%s/%s/%s", serviceInstance.getHost() + ":" + serviceInstance.getPort(), module, apiPath);
    }

}
