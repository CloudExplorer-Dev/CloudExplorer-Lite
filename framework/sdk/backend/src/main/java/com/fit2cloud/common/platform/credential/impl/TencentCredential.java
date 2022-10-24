package com.fit2cloud.common.platform.credential.impl;

import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.platform.credential.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/6  3:29 PM
 * @Version 1.0
 * @注释:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TencentCredential implements Credential {
    /**
     * 密钥id
     */
    @Form(inputType = InputType.Text, label = "secretId", description = "访问令牌")
    private String secretId;
    /**
     * 密钥
     */
    @Form(inputType = InputType.Password, label = "secretKey", description = "访问密钥")
    private String secretKey;

    @Override
    public boolean verification() {
        try {
            regions();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @SneakyThrows
    @Override
    public List<Region> regions() {
        try {
            com.tencentcloudapi.common.Credential cred = new com.tencentcloudapi.common.Credential(this.secretId, this.secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            CvmClient client = new CvmClient(cred, "", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DescribeRegionsRequest req = new DescribeRegionsRequest();
            // 返回的resp是一个DescribeRegionsResponse的实例，与请求对象对应
            DescribeRegionsResponse describeRegionsResponse = client.DescribeRegions(req);
            return Arrays.stream(describeRegionsResponse.getRegionSet()).map(r -> {
                Region region = new Region();
                region.setRegionId(r.getRegion());
                region.setName(r.getRegionName());
                return region;
            }).toList();
        } catch (Exception e) {
            throw new Fit2cloudException(100001, "云账号校验失败");
        }

    }
}
