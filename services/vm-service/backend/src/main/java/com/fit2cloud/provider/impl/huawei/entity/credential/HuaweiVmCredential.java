package com.fit2cloud.provider.impl.huawei.entity.credential;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.HuaweiCredential;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.region.EcsRegion;
import com.huaweicloud.sdk.evs.v2.EvsClient;
import com.huaweicloud.sdk.evs.v2.region.EvsRegion;
import com.huaweicloud.sdk.iec.v1.IecClient;
import com.huaweicloud.sdk.iec.v1.region.IecRegion;
import com.huaweicloud.sdk.ims.v2.ImsClient;
import com.huaweicloud.sdk.ims.v2.region.ImsRegion;
import com.huaweicloud.sdk.vpc.v2.VpcClient;

/**
 * @Author:张少虎
 * @Date: 2022/9/22  2:28 PM
 * @Version 1.0
 * @注释:
 */
public class HuaweiVmCredential extends HuaweiCredential implements Credential {
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
     * 获取EvsClient
     *
     * @param region 区域
     * @return EvsClient
     */
    public EvsClient getEvsClient(String region) {
        try {
            return EvsClient.newBuilder().withCredential(getAuth()).withRegion(EvsRegion.valueOf(region)).build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }

    /**
     * 获取IecClient客户端
     *
     * @param region 区域
     * @return IecClient
     */
    public IecClient getIecClient(String region) {
        try {
            return IecClient.newBuilder().withCredential(getAuth()).withRegion(IecRegion.valueOf(region)).build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }

    /**
     * 获取vpc客户端
     *
     * @param region 区域
     * @return vpc客户端
     */
    public VpcClient getVpcClient(String region) {
        try {
            return VpcClient.newBuilder().withCredential(getAuth()).withRegion(IecRegion.valueOf(region)).build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }

    /**
     * 获取镜像相关客户端
     *
     * @param region 区域
     * @return 镜像相关客户端
     */
    public ImsClient getImsClient(String region) {
        try {
            return ImsClient.newBuilder().withCredential(getAuth()).withRegion(ImsRegion.valueOf(region)).build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }
}
