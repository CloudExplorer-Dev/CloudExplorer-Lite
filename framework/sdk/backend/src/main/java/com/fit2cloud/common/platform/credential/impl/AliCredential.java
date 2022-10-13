package com.fit2cloud.common.platform.credential.impl;

import com.aliyun.ecs20140526.Client;
import com.aliyun.ecs20140526.models.DescribeRegionsRequest;
import com.aliyun.ecs20140526.models.DescribeRegionsResponse;
import com.aliyun.ecs20140526.models.DescribeRegionsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.annotaion.From;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.platform.credential.Credential;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;

/**
 * @Author:张少虎
 * @Date: 2022/9/6  2:55 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class AliCredential implements Credential {
    /**
     * 访问令牌
     */
    @From(inputType = InputType.Text, label = "AccessKeyId", description = "访问令牌")
    private String accessKeyId;
    /**
     * 访问密钥
     */
    @From(inputType = InputType.Password, label = "AccessKeySecret", description = "访问令牌")
    private String accessKeySecret;

    @Override
    public boolean verification() {
        try {
            regions();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Region> regions() {
        try {
            Config config = new Config()
                    .setAccessKeyId(accessKeyId)
                    .setAccessKeySecret(accessKeySecret)
                    .setEndpoint("ecs.aliyuncs.com");
            Client client = new Client(config);
            DescribeRegionsResponse describeRegionsResponse = client.describeRegions(new DescribeRegionsRequest());
            List<DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRegion> regions = describeRegionsResponse.getBody().getRegions().getRegion();
            return regions.stream().map(region -> {
                Region result = new Region();
                result.setRegionId(region.regionId);
                result.setName(region.localName);
                return result;
            }).toList();
        } catch (Exception e) {
            throw new Fit2cloudException(100001, "云账号校验失败");
        }
    }
}
