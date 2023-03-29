package com.fit2cloud.provider.impl.tencent;

import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.platform.credential.impl.TencentCredential;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CNetwork;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.entity.request.BaseDiskRequest;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.impl.tencent.api.TencentSyncCloudApi;
import com.fit2cloud.provider.impl.tencent.constants.TencentChargeType;
import com.fit2cloud.provider.impl.tencent.constants.TencentLoginType;
import com.fit2cloud.provider.impl.tencent.constants.TencentOSType;
import com.fit2cloud.provider.impl.tencent.constants.TencentPeriodOption;
import com.fit2cloud.provider.impl.tencent.entity.TencentDiskTypeDTO;
import com.fit2cloud.provider.impl.tencent.entity.TencentInstanceType;
import com.fit2cloud.provider.impl.tencent.entity.request.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  11:11 AM
 * @Version 1.0
 * @注释:
 */
public class TencentCloudProvider extends AbstractCloudProvider<TencentCredential> implements ICloudProvider {
    // For Create VM [START]
    @Override
    public FormObject getCreateServerForm() {
        return FormUtil.toForm(TencentVmCreateRequest.class);
    }

    @Override
    public F2CVirtualMachine createVirtualMachine(String req) {
        return TencentSyncCloudApi.createVirtualMachine(JsonUtil.parseObject(req, TencentVmCreateRequest.class));
    }

    @Override
    public F2CVirtualMachine getSimpleServerByCreateRequest(String req) {
        return TencentSyncCloudApi.getSimpleServerByCreateRequest(JsonUtil.parseObject(req, TencentVmCreateRequest.class));
    }

    public List<Map<String, String>> getRegions(String req) {
        return TencentSyncCloudApi.getRegions(JsonUtil.parseObject(req, TencentBaseRequest.class));
    }

    public List<Map<String, String>> getZones(String req) {
        return TencentSyncCloudApi.getZones(JsonUtil.parseObject(req, TencentBaseRequest.class));
    }

    /**
     * 获取付费方式
     *
     * @param req
     * @return
     */
    public List<Map<String, String>> getChargeType(String req) {
        List<Map<String, String>> result = new ArrayList<>();
        for (TencentChargeType chargeType : TencentChargeType.values()) {
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
        for (TencentPeriodOption option : TencentPeriodOption.values()) {
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
        for (TencentOSType type : TencentOSType.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("id", type.name());
            map.put("name", type.name());
            result.add(map);
        }
        return result;
    }

    /**
     * 获取可选镜像
     *
     * @param req
     * @return
     */
    public List<F2CImage> getImages(String req) {
        return TencentSyncCloudApi.getImages(JsonUtil.parseObject(req, TencentGetImageRequest.class));
    }

    /**
     * 获取实例类型
     *
     * @param req
     * @return
     */
    public List<TencentInstanceType> getInstanceTypes(String req) {
        return TencentSyncCloudApi.getInstanceTypes(JsonUtil.parseObject(req, TencentGetInstanceTypeRequest.class));
    }

    /**
     * 获取磁盘类型
     *
     * @param req
     * @return
     */
    public TencentDiskTypeDTO getDiskTypesForCreateVm(String req) {
        return TencentSyncCloudApi.getDiskTypes(JsonUtil.parseObject(req, TencentGetDiskTypeRequest.class));
    }

    /**
     * 获取网络
     *
     * @param req
     * @return
     */
    public List<F2CNetwork> getNetworks(String req) {
        return TencentSyncCloudApi.getNetworks(JsonUtil.parseObject(req, TencentGetSubnetRequest.class));
    }

    /**
     * 获取安全组
     *
     * @param req
     * @return
     */
    public List<Map<String, String>> getSecurityGroups(String req) {
        return TencentSyncCloudApi.getSecurityGroups(JsonUtil.parseObject(req, TencentBaseRequest.class));
    }

    /**
     * 获取带宽计费类型
     *
     * @param req
     * @return
     */
    public List<Map<String, String>> getBandwidthChargeTypes(String req) {
        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("id", "bandwidth");
        map.put("name", "按固定带宽");
        result.add(map);

        map = new HashMap<>();
        map.put("id", "traffic");
        map.put("name", "按使用流量");
        result.add(map);
        return result;
    }

    /**
     * 获取登录方式
     *
     * @param req
     * @return
     */
    public List<Map<String, String>> getLoginTypes(String req) {
        List<Map<String, String>> result = new ArrayList<>();
        for (TencentLoginType type : TencentLoginType.values()) {
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
        TencentVmCreateRequest request = JsonUtil.parseObject(req, TencentVmCreateRequest.class);
        if (request.getOs() != null && request.getOs().toLowerCase().indexOf("window") > -1) {
            return "Administrator";
        }
        return "root";
    }

    /**
     * 基础配置询价
     *
     * @param req
     * @return
     */
    @Override
    public String calculateConfigPrice(String req) {
        return TencentSyncCloudApi.calculateConfigPrice(JsonUtil.parseObject(req, TencentVmCreateRequest.class));
    }

    /**
     * 公网IP流量配置询价
     *
     * @param req
     * @return
     */
    public String calculateTrafficPrice(String req) {
        return TencentSyncCloudApi.calculateTrafficPrice(JsonUtil.parseObject(req, TencentVmCreateRequest.class));
    }
    // For Create VM [END]

    @Override
    public List<F2CVirtualMachine> listVirtualMachine(String req) {
        return TencentSyncCloudApi.listVirtualMachine(JsonUtil.parseObject(req, ListVirtualMachineRequest.class));
    }

    @Override
    public List<F2CImage> listImage(String req) {
        return TencentSyncCloudApi.listImage(JsonUtil.parseObject(req, ListImageRequest.class));
    }

    @Override
    public List<F2CDisk> listDisk(String req) {
        return TencentSyncCloudApi.listDisk(JsonUtil.parseObject(req, ListDiskRequest.class));
    }

    @Override
    public boolean powerOff(String req) {
        TencentInstanceRequest request = JsonUtil.parseObject(req, TencentInstanceRequest.class);
        request.setForce(true);
        return TencentSyncCloudApi.powerOff(request);
    }

    @Override
    public boolean powerOn(String req) {
        return TencentSyncCloudApi.powerOn(JsonUtil.parseObject(req, TencentInstanceRequest.class));
    }

    @Override
    public boolean shutdownInstance(String req) {
        return TencentSyncCloudApi.powerOff(JsonUtil.parseObject(req, TencentInstanceRequest.class));
    }

    @Override
    public boolean rebootInstance(String req) {
        return TencentSyncCloudApi.rebootInstance(JsonUtil.parseObject(req, TencentInstanceRequest.class));
    }

    @Override
    public boolean deleteInstance(String req) {
        return TencentSyncCloudApi.deleteInstance(JsonUtil.parseObject(req, TencentInstanceRequest.class));
    }

    @Override
    public boolean hardShutdownInstance(String req) {
        TencentInstanceRequest request = JsonUtil.parseObject(req, TencentInstanceRequest.class);
        request.setForce(true);
        return TencentSyncCloudApi.powerOff(request);
    }

    @Override
    public boolean hardRebootInstance(String req) {
        TencentInstanceRequest request = JsonUtil.parseObject(req, TencentInstanceRequest.class);
        request.setForce(true);
        return TencentSyncCloudApi.rebootInstance(request);
    }

    @Override
    public FormObject getCreateDiskForm() {
        return FormUtil.toForm(TencentCreateDiskForm.class);
    }

    public List<Map<String, String>> getFileSystemType(String req) {
        List<Map<String, String>> types = new ArrayList<>();
        Map<String, String> typeOne = new HashMap<>();
        typeOne.put("id", "EXT4");
        typeOne.put("name", "EXT4");
        types.add(typeOne);

        Map<String, String> typeTwo = new HashMap<>();
        typeTwo.put("id", "XFS");
        typeTwo.put("name", "XFS");
        types.add(typeTwo);
        return types;
    }

    public List<TencentDiskTypeDTO.TencentDiskType> getDiskTypesForCreateDisk(String req) {
        return TencentSyncCloudApi.getDataDiskType(JsonUtil.parseObject(req, TencentGetDiskTypeRequest.class));
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
        return TencentSyncCloudApi.createDisks(JsonUtil.parseObject(req, TencentCreateDisksRequest.class));
    }

    @Override
    public F2CDisk createDisk(String req) {
        return TencentSyncCloudApi.createDisk(JsonUtil.parseObject(req, TencentCreateDiskRequest.class));
    }

    @Override
    public boolean enlargeDisk(String req) {
        return TencentSyncCloudApi.enlargeDisk(JsonUtil.parseObject(req, TencentResizeDiskRequest.class));
    }

    @Override
    public F2CDisk attachDisk(String req) {
        return TencentSyncCloudApi.attachDisk(JsonUtil.parseObject(req, TencentAttachDiskRequest.class));
    }

    @Override
    public boolean detachDisk(String req) {
        return TencentSyncCloudApi.detachDisk(JsonUtil.parseObject(req, TencentDetachDiskRequest.class));
    }

    @Override
    public boolean deleteDisk(String req) {
        return TencentSyncCloudApi.deleteDisk(JsonUtil.parseObject(req, TencentDeleteDiskRequest.class));
    }

    @Override
    public List<F2CPerfMetricMonitorData> getF2CPerfMetricMonitorData(String req) {
        return TencentSyncCloudApi.getF2CPerfMetricList(JsonUtil.parseObject(req, GetMetricsRequest.class));
    }

    @Override
    public List<F2CPerfMetricMonitorData> getF2CDiskPerfMetricMonitorData(String req) {
        return TencentSyncCloudApi.getF2CDiskPerfMetricList(JsonUtil.parseObject(req, GetMetricsRequest.class));
    }

    @Override
    public F2CVirtualMachine changeVmConfig(String req) {
        return TencentSyncCloudApi.changeVmConfig(JsonUtil.parseObject(req, TencentUpdateConfigRequest.class));
    }

    @Override
    public FormObject getConfigUpdateForm() {
        return FormUtil.toForm(TencentConfigUpdateForm.class);
    }

    public List<TencentInstanceType> getInstanceTypesForConfigUpdate(String req) {
        return TencentSyncCloudApi.getInstanceTypesForConfigUpdate(JsonUtil.parseObject(req, TencentUpdateConfigRequest.class));
    }

    @Override
    public String calculateConfigUpdatePrice(String req) {
        return TencentSyncCloudApi.calculateConfigUpdatePrice(JsonUtil.parseObject(req, TencentUpdateConfigRequest.class));
    }

    @Override
    public List<F2CDisk> getVmF2CDisks(String req) {
        return TencentSyncCloudApi.getVmF2CDisks(JsonUtil.parseObject(req, BaseDiskRequest.class));
    }
}
