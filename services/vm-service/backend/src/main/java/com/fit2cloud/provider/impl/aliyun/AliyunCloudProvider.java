package com.fit2cloud.provider.impl.aliyun;

import com.aliyun.ecs20140526.models.DescribeRegionsResponseBody;
import com.aliyun.ecs20140526.models.DescribeSecurityGroupsResponseBody;
import com.aliyun.ecs20140526.models.DescribeZonesResponseBody;
import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CNetwork;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.impl.aliyun.api.AliyunSyncCloudApi;
import com.fit2cloud.provider.impl.aliyun.constants.*;
import com.fit2cloud.provider.impl.aliyun.entity.AliyunInstanceType;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunVmCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  10:55 AM
 * @Version 1.0
 * @注释:
 */
public class AliyunCloudProvider extends AbstractCloudProvider<AliyunVmCredential> implements ICloudProvider {
    // For Create VM [START]
    @Override
    public FormObject getCreateServerForm() {
        return FormUtil.toForm(AliyunVmCreateRequest.class);
    }

    public F2CVirtualMachine createVirtualMachine(String req) {
        return AliyunSyncCloudApi.createVirtualMachine(JsonUtil.parseObject(req, AliyunVmCreateRequest.class));
    }

    @Override
    public F2CVirtualMachine getSimpleServerByCreateRequest(String req) {
        return AliyunSyncCloudApi.getSimpleServerByCreateRequest(JsonUtil.parseObject(req, AliyunVmCreateRequest.class));
    }

    public List<DescribeRegionsResponseBody.DescribeRegionsResponseBodyRegionsRegion> getRegions(String req) {
        return AliyunSyncCloudApi.getRegions(JsonUtil.parseObject(req, AliyunGetRegionRequest.class));
    }

    public List<DescribeZonesResponseBody.DescribeZonesResponseBodyZonesZone> getZones(String req) {
        return AliyunSyncCloudApi.getZones(JsonUtil.parseObject(req, AliyunGetZoneRequest.class));
    }

    /**
     * 获取付费方式
     *
     * @param req
     * @return
     */
    public List<Map<String, String>> getChargeType(String req) {
        List<Map<String, String>> result = new ArrayList<>();
        for (AliyunChargeType chargeType : AliyunChargeType.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("id", chargeType.getId());
            map.put("name", chargeType.getName());
            result.add(map);
        }
        return result;
    }

    /**
     * 获取付费周期
     *
     * @param req
     * @return
     */
    public List<Map<String, Object>> getPeriodOption(String req) {
        List<Map<String, Object>> periodList = new ArrayList<>();
        for (AliyunPeriodOption option : AliyunPeriodOption.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("period", option.getPeriod());
            map.put("periodDisplayName", option.getPeriodDisplayName());
            periodList.add(map);
        }
        return periodList;
    }

    /**
     * 获取操作系统类型
     *
     * @param req
     * @return
     */
    public List<Map<String, String>> getOsTypes(String req) {
        List<Map<String, String>> result = new ArrayList<>();
        for (AliyunOSType type : AliyunOSType.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("id", type.getDisplayValue());
            map.put("name", type.getDisplayValue());
            result.add(map);
        }
        return result;
    }

    /**
     * 获取带宽计费类型
     *
     * @param req
     * @return
     */
    public List<Map<String, String>> getBandwidthChargeTypes(String req) {
        List<Map<String, String>> result = new ArrayList<>();
        for (AliyunBandwidthType type : AliyunBandwidthType.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("id", type.getId());
            map.put("name", type.getName());
            result.add(map);
        }
        return result;
    }

    /**
     * 获取网络
     *
     * @param req
     * @return
     */
    public List<F2CNetwork> getNetworks(String req) {
        return AliyunSyncCloudApi.getNetworks(JsonUtil.parseObject(req, AliyunGetVSwitchRequest.class));
    }

    /**
     * 获取安全组
     *
     * @param req
     * @return
     */
    public List<DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup> getSecurityGroups(String req) {
        return AliyunSyncCloudApi.getSecurityGroups(JsonUtil.parseObject(req, AliyunGetSecurityGroupRequest.class));
    }

    /**
     * 获取实例类型
     *
     * @param req
     * @return
     */
    public List<AliyunInstanceType> getInstanceTypes(String req) {
        return AliyunSyncCloudApi.getInstanceTypes(JsonUtil.parseObject(req, AliyunGetAvailableResourceRequest.class));
    }

    /**
     * 获取实例类型
     *
     * @param req
     * @return
     */
    public List<F2CImage> getImages(String req) {
        return AliyunSyncCloudApi.getImages(JsonUtil.parseObject(req, AliyunGetImageRequest.class));
    }

    /**
     * 获取登录方式
     *
     * @param req
     * @return
     */
    public List<Map<String, String>> getLoginTypes(String req) {
        List<Map<String, String>> result = new ArrayList<>();
        for (AliyunLoginType type : AliyunLoginType.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("id", type.getId());
            map.put("name", type.getName());
            result.add(map);
        }
        return result;
    }

    /**
     * 获取登录用户
     *
     * @param req
     * @return
     */
    public String getLoginUser(String req) {
        AliyunVmCreateRequest request = JsonUtil.parseObject(req, AliyunVmCreateRequest.class);
        if (request.getOs() != null && request.getOs().toLowerCase().indexOf("windows") > -1) {
            return "Administrator";
        }
        return "root";
    }

    /**
     * 获取密钥对
     *
     * @param req
     * @return
     */
    public List<Map<String, String>> getKeyPairs(String req) {
        return AliyunSyncCloudApi.getKeyPairs(JsonUtil.parseObject(req, AliyunBaseRequest.class));
    }

    /**
     * 基础配置询价
     *
     * @param req
     * @return
     */
    public String calculateConfigPrice(String req) {
        return AliyunSyncCloudApi.calculateConfigPrice(JsonUtil.parseObject(req, AliyunVmCreateRequest.class));
    }

    /**
     * 公网IP流量配置询价
     *
     * @param req
     * @return
     */
    public String calculateTrafficPrice(String req) {
        return AliyunSyncCloudApi.calculateTrafficPrice(JsonUtil.parseObject(req, AliyunVmCreateRequest.class));
    }
    // For Create VM [END]

    @Override
    public List<F2CVirtualMachine> listVirtualMachine(String req) {
        return AliyunSyncCloudApi.listVirtualMachine(JsonUtil.parseObject(req, ListVirtualMachineRequest.class));
    }

    @Override
    public List<F2CImage> listImage(String req) {
        return AliyunSyncCloudApi.listImage(JsonUtil.parseObject(req, ListImageRequest.class));
    }

    @Override
    public List<F2CDisk> listDisk(String req) {
        return AliyunSyncCloudApi.listDisk(JsonUtil.parseObject(req, ListDisksRequest.class));
    }

    @Override
    public boolean powerOff(String req) {
        AliyunInstanceRequest request = JsonUtil.parseObject(req, AliyunInstanceRequest.class);
        request.setForce(true);
        return AliyunSyncCloudApi.powerOff(request);
    }

    @Override
    public boolean powerOn(String req) {
        return AliyunSyncCloudApi.powerOn(JsonUtil.parseObject(req, AliyunInstanceRequest.class));
    }

    @Override
    public boolean shutdownInstance(String req) {
        return AliyunSyncCloudApi.powerOff(JsonUtil.parseObject(req, AliyunInstanceRequest.class));
    }

    @Override
    public boolean rebootInstance(String req) {
        return AliyunSyncCloudApi.rebootInstance(JsonUtil.parseObject(req, AliyunInstanceRequest.class));
    }

    @Override
    public boolean deleteInstance(String req) {
        AliyunInstanceRequest request = JsonUtil.parseObject(req, AliyunInstanceRequest.class);
        request.setForce(true);
        return AliyunSyncCloudApi.deleteInstance(request);
    }

    @Override
    public boolean hardShutdownInstance(String req) {
        AliyunInstanceRequest request = JsonUtil.parseObject(req, AliyunInstanceRequest.class);
        request.setForce(true);
        return AliyunSyncCloudApi.powerOff(request);
    }

    @Override
    public boolean hardRebootInstance(String req) {
        AliyunInstanceRequest request = JsonUtil.parseObject(req, AliyunInstanceRequest.class);
        request.setForce(true);
        return AliyunSyncCloudApi.rebootInstance(request);
    }

    @Override
    public FormObject getCreateDiskForm() {
        return FormUtil.toForm(AliyunCreateDiskForm.class);
    }

    public List<Map<String, String>> getDiskTypes(String req) {
        return AliyunSyncCloudApi.getDiskTypes(JsonUtil.parseObject(req, AliyunGetDiskTypeRequest.class));
    }

    @Override
    public List<Map<String, String>> getDeleteWithInstance(String req) {
        List<Map<String, String>> deleteWithInstance = new ArrayList<>();
        Map<String, String> yes = new HashMap<>();
        yes.put("id", DeleteWithInstance.YES.name());
        yes.put("name", "是");
        deleteWithInstance.add(yes);

        Map<String, String> no = new HashMap<>();
        no.put("id", DeleteWithInstance.NO.name());
        no.put("name", "否");
        deleteWithInstance.add(no);
        return deleteWithInstance;
    }

    @Override
    public List<F2CDisk> createDisks(String req) {
        return AliyunSyncCloudApi.createDisks(JsonUtil.parseObject(req, AliyunCreateDisksRequest.class));
    }

    @Override
    public F2CDisk createDisk(String req) {
        return AliyunSyncCloudApi.createDisk(JsonUtil.parseObject(req, AliyunCreateDiskRequest.class));
    }

    @Override
    public boolean enlargeDisk(String req) {
        return AliyunSyncCloudApi.enlargeDisk(JsonUtil.parseObject(req, AliyunResizeDiskRequest.class));
    }

    @Override
    public boolean attachDisk(String req) {
        return AliyunSyncCloudApi.attachDisk(JsonUtil.parseObject(req, AliyunAttachDiskRequest.class));
    }

    @Override
    public boolean detachDisk(String req) {
        return AliyunSyncCloudApi.detachDisk(JsonUtil.parseObject(req, AliyunDetachDiskRequest.class));
    }

    @Override
    public boolean deleteDisk(String req) {
        return AliyunSyncCloudApi.deleteDisk(JsonUtil.parseObject(req, AliyunDeleteDiskRequest.class));
    }

    @Override
    public List<F2CPerfMetricMonitorData> getF2CPerfMetricMonitorData(String req) {
        return AliyunSyncCloudApi.getF2CPerfMetricList(JsonUtil.parseObject(req, GetMetricsRequest.class));
    }
    @Override
    public List<F2CPerfMetricMonitorData> getF2CDiskPerfMetricMonitorData(String req) {
        return AliyunSyncCloudApi.getF2CDiskPerfMetricList(JsonUtil.parseObject(req, GetMetricsRequest.class));
    }
}
