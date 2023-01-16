package com.fit2cloud.provider.impl.huawei.entity.credential;

import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.impl.huawei.entity.credential.HuaweiBaseCredential;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.GlobalCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.css.v1.region.CssRegion;
import com.huaweicloud.sdk.dcs.v2.DcsClient;
import com.huaweicloud.sdk.dcs.v2.region.DcsRegion;
import com.huaweicloud.sdk.dds.v3.DdsClient;
import com.huaweicloud.sdk.dds.v3.region.DdsRegion;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.region.EcsRegion;
import com.huaweicloud.sdk.eip.v3.EipClient;
import com.huaweicloud.sdk.eip.v3.region.EipRegion;
import com.huaweicloud.sdk.elb.v2.region.ElbRegion;
import com.huaweicloud.sdk.evs.v2.EvsClient;
import com.huaweicloud.sdk.evs.v2.region.EvsRegion;
import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.region.IamRegion;
import com.huaweicloud.sdk.rds.v3.RdsClient;
import com.huaweicloud.sdk.rds.v3.region.RdsRegion;
import com.huaweicloud.sdk.css.v1.*;
import com.huaweicloud.sdk.elb.v3.*;
import com.huaweicloud.sdk.vpc.v3.VpcClient;
import com.huaweicloud.sdk.vpc.v3.region.VpcRegion;
import com.obs.services.ObsClient;

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

    private ICredential getGlobalAuth() {
        try {
            return new GlobalCredentials().withAk(getAk()).withSk(getSk());
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


    /**
     * 获取 EVS(云硬盘) 客户端
     *
     * @param region 区域
     * @return 华为云EVS(云硬盘)客户端
     */
    public EvsClient getEvsClient(String region) {
        try {
            return EvsClient.newBuilder()
                    .withCredential(getAuth())
                    .withRegion(EvsRegion.valueOf(region))
                    .build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }

    /**
     * 获取 ELB(负载均衡) 客户端
     *
     * @param region 区域
     * @return 华为云ELB(负载均衡) 客户端
     */
    public ElbClient getElbClient(String region) {
        try {
            return ElbClient.newBuilder()
                    .withCredential(getAuth())
                    .withRegion(ElbRegion.valueOf(region))
                    .build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }

    /**
     * 获取 Eip(弹性公网ip) 客户端
     *
     * @param region 区域
     * @return Eip(弹性公网ip) 客户端
     */
    public EipClient getEipClient(String region) {
        try {
            return EipClient.newBuilder()
                    .withCredential(getAuth())
                    .withRegion(EipRegion.valueOf(region))
                    .build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }

    /**
     * 获取 VPC 客户端
     *
     * @param region 区域
     * @return Vpc 客户端
     */
    public VpcClient getVpcClient(String region) {
        try {
            return VpcClient.newBuilder()
                    .withCredential(getAuth())
                    .withRegion(VpcRegion.valueOf(region))
                    .build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }

    /**
     * 获取认证服务客户端
     *
     * @return 用户认证服务客户端
     */
    public IamClient getIamClient() {
        try {
            return IamClient.newBuilder()
                    .withCredential(getGlobalAuth())
                    .withRegion(IamRegion.valueOf("cn-east-2"))
                    .build();
        } catch (Exception e) {
            SkipPageException.throwHuaweiSkip(e);
            throw e;
        }
    }

    public ObsClient getObsClient() {
        return new ObsClient(this.getAk(), this.getSk(), "https://obs.cn-east-3.myhuaweicloud.com");
    }

    public ObsClient getObsClient(String region) {
        return new ObsClient(this.getAk(), this.getSk(), String.format("https://obs.%s.myhuaweicloud.com", region));
    }

}
