package com.fit2cloud.provider.impl.aliyun.entity.request;

import com.aliyun.bssopenapi20171214.models.GetPayAsYouGoPriceRequest;
import com.aliyun.bssopenapi20171214.models.GetSubscriptionPriceRequest;
import com.fit2cloud.provider.impl.aliyun.constants.AliyunBandwidthType;
import com.fit2cloud.provider.impl.aliyun.constants.AliyunDiskType;
import com.fit2cloud.provider.impl.aliyun.entity.AliyunPriceModuleConfig;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : LiuDi
 * @date : 2022/12/27 11:58
 */
@Data
public class AliyunPriceRequest extends AliyunVmCreateRequest {
    /**
     * 新购
     */
    public static String NEW_ORDER = "NewOrder";
    /**
     * 升级配置
     */
    public static String UPGRADE = "Upgrade";
    /**
     * 降低配置
     */
    public static String DOWNGRADE = "Downgrade";

    /**
     * 订单类型：新购 or 配置升级
     */
    private String orderType = NEW_ORDER;

    /**
     * 按量付费实例计费
     *
     * @return
     */
    public List<GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList> toPostPaidModuleList() {
        List<GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList> moduleList = new ArrayList<>();
        AliyunPriceModuleConfig config = generateModuleConfig();

        GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList instanceTypeModule = new GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList()
                .setModuleCode("InstanceType")
                .setPriceType("Hour")
                .setConfig(config.getInstanceTypeConfig());
        moduleList.add(instanceTypeModule);

        // 系统盘
        if (StringUtils.isNotBlank(config.getSystemDiskConfig())) {
            GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList systemDiskModule = new GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList()
                    .setModuleCode("SystemDisk")
                    .setPriceType("Hour")
                    .setConfig(config.getSystemDiskConfig());
            moduleList.add(systemDiskModule);
        }

        // 数据盘
        List<String> dataConfigList = config.getDataDiskConfigList();
        if (CollectionUtils.isNotEmpty(dataConfigList)) {
            for (int i = 0; i < dataConfigList.size(); i++) {
                GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList dataDiskModule = new GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList()
                        .setModuleCode("DataDisk")
                        .setPriceType("Hour")
                        .setConfig(dataConfigList.get(i));
                moduleList.add(dataDiskModule);
            }
        }

        // 公网IP
        if (this.getHasPublicIp() != null && this.getHasPublicIp()) {
            if (AliyunBandwidthType.PayByTraffic.getId().equalsIgnoreCase(this.getBandwidthChargeType())) {
                GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList internetTrafficOutModule = new GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList()
                        .setModuleCode("InternetTrafficOut")
                        .setPriceType("Usage")
                        .setConfig(config.getPublicIpConfig());
                moduleList.add(internetTrafficOutModule);
            } else {
                GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList internetMaxBandwidthOutModule = new GetPayAsYouGoPriceRequest.GetPayAsYouGoPriceRequestModuleList()
                        .setModuleCode("InternetMaxBandwidthOut")
                        .setPriceType("Hour")
                        .setConfig(config.getPublicIpConfig());
                moduleList.add(internetMaxBandwidthOutModule);
            }
        }
        return moduleList;
    }

    /**
     * 包年包月实例计费
     *
     * @return
     */
    public List<GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList> toPrePaidModuleList() {
        List<GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList> moduleList = new ArrayList<>();
        AliyunPriceModuleConfig config = generateModuleConfig();

        // 实例类型
        GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList instanceTypeModule = new GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList()
                .setModuleCode("InstanceType")
                .setConfig(config.getInstanceTypeConfig());
        moduleList.add(instanceTypeModule);

        // 系统盘
        if (StringUtils.isNotBlank(config.getSystemDiskConfig())) {
            GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList systemDiskModule = new GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList()
                    .setModuleCode("SystemDisk")
                    .setConfig(config.getSystemDiskConfig());
            moduleList.add(systemDiskModule);
        }

        // 数据盘
        List<String> dataConfigList = config.getDataDiskConfigList();
        if (CollectionUtils.isNotEmpty(dataConfigList)) {
            for (int i = 0; i < dataConfigList.size(); i++) {
                GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList dataDiskModule = new GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList()
                        .setModuleCode("DataDisk")
                        .setConfig(dataConfigList.get(i));
                moduleList.add(dataDiskModule);
            }
        }

        // 公网IP
        if (this.getHasPublicIp() != null && this.getHasPublicIp()) {
            // 按流量计费只能通过后付费询价，此处查按带宽计费的费用
            if (AliyunBandwidthType.PayByBandwidth.getId().equalsIgnoreCase(this.getBandwidthChargeType())) {
                GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList internetMaxBandwidthOutModule = new GetSubscriptionPriceRequest.GetSubscriptionPriceRequestModuleList()
                        .setModuleCode("InternetMaxBandwidthOut")
                        .setConfig(config.getPublicIpConfig());
                moduleList.add(internetMaxBandwidthOutModule);
            }
        }

        return moduleList;
    }

    private AliyunPriceModuleConfig generateModuleConfig() {
        AliyunPriceModuleConfig config = new AliyunPriceModuleConfig();

        // 实例类型
        String imageOs = this.getOs().indexOf("win") > 0 ? "windows" : "linux";
        if (getInstanceType() != null) {
            String instanceTypeConfig = "ImageOs:" + imageOs + "," +
                    "IoOptimized:none" + "," +
                    "Region:" + this.getRegionId() + "," +
                    //"InstanceTypeFamily:" + this.getInstanceTypeDTO().getInstanceTypeFamily() + "," +
                    "InstanceType:" + getInstanceType();
            config.setInstanceTypeConfig(instanceTypeConfig);
        }

        // 磁盘
        if (CollectionUtils.isNotEmpty(this.getDisks())) {
            // 系统盘
            AliyunCreateDiskForm systemDisk = this.getDisks().get(0);
            String systemDiskConfig = "Region:" + this.getRegionId() + "," +
                    "SystemDisk.Category:" + systemDisk.getDiskType() + "," +
                    "SystemDisk.Size:" + systemDisk.getSize();
            if (AliyunDiskType.CLOUD_ESSD.getId().equalsIgnoreCase(systemDisk.getDiskType())) {
                String performanceLevel = StringUtils.isBlank(systemDisk.getPerformanceLevel()) ? "PL1" : systemDisk.getPerformanceLevel();
                systemDiskConfig = systemDiskConfig + "," + "SystemDisk.PerformanceLevel:" + performanceLevel;
            }
            config.setSystemDiskConfig(systemDiskConfig);

            // 数据盘
            List<String> dataDiskConfigList = new ArrayList();
            for (int i = 1; i < this.getDisks().size(); i++) {
                String dataDiskConfig = "Region:" + this.getRegionId() + "," +
                        "DataDisk.Category:" + this.getDisks().get(i).getDiskType() + "," +
                        "DataDisk.Size:" + this.getDisks().get(i).getSize();
                if (AliyunDiskType.CLOUD_ESSD.getId().equalsIgnoreCase(this.getDisks().get(i).getDiskType())) {
                    String performanceLevel = StringUtils.isBlank(this.getDisks().get(i).getPerformanceLevel()) ? "PL1" : this.getDisks().get(i).getPerformanceLevel();
                    dataDiskConfig = dataDiskConfig + "," + "DataDisk.PerformanceLevel:" + performanceLevel;
                }
                dataDiskConfigList.add(dataDiskConfig);
            }
            config.setDataDiskConfigList(dataDiskConfigList);
        }

        // 公网IP
        if (this.getHasPublicIp() != null && this.getHasPublicIp()) {
            // 按流量计费只能通过后付费询价
            if (AliyunBandwidthType.PayByTraffic.getId().equalsIgnoreCase(this.getBandwidthChargeType())) {
                // 公网流量：按流量计费 XX元/GB
                String internetTrafficOutConfig = "Region:" + this.getRegionId() + "," +
                        "InternetTrafficOut:1";
                config.setPublicIpConfig(internetTrafficOutConfig);
            } else {
                // 公网带宽：InternetMaxBandwidthOut 单位 KBPS;bandwidth 单位 MBPS
                String internetMaxBandwidthOutConfig = "Region:" + this.getRegionId() + "," +
                        "InternetMaxBandwidthOut:" + Integer.valueOf(this.getBandwidth()) * 1024;
                config.setPublicIpConfig(internetMaxBandwidthOutConfig);
            }
        }
        return config;
    }
}
