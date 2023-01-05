package com.fit2cloud.provider.impl.huawei.entity.credential;

import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.impl.huawei.entity.credential.HuaweiBaseCredential;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.css.v1.region.CssRegion;
import com.huaweicloud.sdk.dcs.v2.DcsClient;
import com.huaweicloud.sdk.dcs.v2.region.DcsRegion;
import com.huaweicloud.sdk.dds.v3.DdsClient;
import com.huaweicloud.sdk.dds.v3.region.DdsRegion;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.region.EcsRegion;
import com.huaweicloud.sdk.rds.v3.RdsClient;
import com.huaweicloud.sdk.rds.v3.region.RdsRegion;
import com.huaweicloud.sdk.css.v1.*;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  12:03}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class HuaweiSecurityComplianceCredential extends HuaweiBaseCredential {

    /**
     * 获取认证对象
     *
     * @return 认证对象
     */
    private ICredential getAuth() {
        try {
            return new BasicCredentials().withAk(getAk()).withSk(getSk());
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }

    /**
     * 获取EcsClient
     *
     * @param region 区域
     * @return EcsClient
     */
    public EcsClient getEcsClient(String region) {
        try {
            return EcsClient.newBuilder().withCredential(getAuth()).withRegion(EcsRegion.valueOf(region)).build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }

    /**
     * 获取 华为云 Rds(MySQL,PostGreSQL,SQLServer)客户端
     *
     * @param region 区域
     * @return Rds客户端
     */
    public RdsClient getRdsClient(String region) {
        try {
            return RdsClient.newBuilder()
                    .withCredential(getAuth())
                    .withRegion(RdsRegion.valueOf(region))
                    .build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }

    /**
     * 获取华为云 Dcs(Redis) 客户端
     *
     * @param region 区域
     * @return 华为云Dcs(Redis) 客户端
     */
    public DcsClient getDcsClient(String region) {
        try {
            return DcsClient.newBuilder()
                    .withCredential(getAuth())
                    .withRegion(DcsRegion.valueOf(region))
                    .build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }

    /**
     * 获取华为云 Dds(Mongodb) 客户端
     *
     * @param region 区域
     * @return 华为云Mongodb客户端
     */
    public DdsClient getDdsClient(String region) {
        try {
            return DdsClient.newBuilder()
                    .withCredential(getAuth())
                    .withRegion(DdsRegion.valueOf(region))
                    .build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }

    /**
     * 获取华为云 Css(Elasticsearch) 客户端
     *
     * @param region 区域
     * @return 华为云Elasticsearch客户端
     */
    public CssClient getCssClient(String region) {
        try {
            return CssClient.newBuilder()
                    .withCredential(getAuth())
                    .withRegion(CssRegion.valueOf(region))
                    .build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }
}
