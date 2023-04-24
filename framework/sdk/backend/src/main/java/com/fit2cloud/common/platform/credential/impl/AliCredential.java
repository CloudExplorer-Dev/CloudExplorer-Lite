package com.fit2cloud.common.platform.credential.impl;

import com.aliyun.ecs20140526.Client;
import com.aliyun.ecs20140526.models.DescribeRegionsRequest;
import com.aliyun.ecs20140526.models.DescribeRegionsResponse;
import com.aliyun.ecs20140526.models.DescribeRegionsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.platform.credential.Credential;
import lombok.Data;

import java.util.List;

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
    @Form(inputType = InputType.Text, label = "AccessKey ID", hint = "{\"title\": \"子账号权限要求\",\"content\": \"授权范围：整个云账号\\n授权策略：AliyunECSFullAccess\\n\\tAliyunEIPFullAccess\\n\\tReadOnlyAccess\"}")
    private String accessKeyId;
    /**
     * 访问密钥
     */
    @Form(inputType = InputType.Password, label = "AccessKey Secret", extraInfo = "{\"text\":\"如何获取AccessKey？\",\"url\":\"https://help.aliyun.com/document_detail/116410.html\"}")
    private String accessKeySecret;

    @Override
    public boolean verification() {
        try {
            regions();
        } catch (Exception e) {
            throw new Fit2cloudException(100001, "云账号校验失败:" + e.getMessage());
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
                result.setEndpoint(region.getRegionEndpoint());
                return result;
            }).toList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
