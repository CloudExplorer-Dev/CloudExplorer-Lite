package com.fit2cloud.provider.impl.huawei;

import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.entity.request.GetMetricsRequest;
import com.fit2cloud.provider.impl.huawei.api.HuaweiSyncCloudApi;
import com.fit2cloud.provider.impl.huawei.entity.InstanceSpecConfig;
import com.fit2cloud.provider.impl.huawei.entity.NovaAvailabilityZoneDTO;
import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiVmCredential;
import com.fit2cloud.provider.impl.huawei.entity.request.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  11:06 AM
 * @Version 1.0
 * @注释:
 */
public class HuaweiCloudProvider extends AbstractCloudProvider<HuaweiVmCredential> implements ICloudProvider {

    @Override
    public FormObject getCreateServerForm() {
        return FormUtil.toForm(HuaweiVmCreateRequest.class);
    }

    @Override
    public List<F2CVirtualMachine> listVirtualMachine(String req) {
        return HuaweiSyncCloudApi.listVirtualMachine(JsonUtil.parseObject(req, ListVirtualMachineRequest.class));
    }

    @Override
    public List<F2CImage> listImage(String req) {
        return HuaweiSyncCloudApi.lisImages(JsonUtil.parseObject(req, ListImageRequest.class));
    }

    @Override
    public List<F2CDisk> listDisk(String req) {
        return HuaweiSyncCloudApi.listDisk(JsonUtil.parseObject(req, ListDisksRequest.class));
    }

    @Override
    public boolean powerOff(String req) {
        HuaweiInstanceRequest request = JsonUtil.parseObject(req, HuaweiInstanceRequest.class);
        request.setForce(true);
        return HuaweiSyncCloudApi.powerOff(request);
    }

    @Override
    public boolean powerOn(String req) {
        return HuaweiSyncCloudApi.powerOn(JsonUtil.parseObject(req, HuaweiInstanceRequest.class));
    }

    @Override
    public boolean shutdownInstance(String req) {
        return HuaweiSyncCloudApi.powerOff(JsonUtil.parseObject(req, HuaweiInstanceRequest.class));
    }

    @Override
    public boolean rebootInstance(String req) {
        return HuaweiSyncCloudApi.rebootInstance(JsonUtil.parseObject(req, HuaweiInstanceRequest.class));
    }

    @Override
    public boolean deleteInstance(String req) {
        return HuaweiSyncCloudApi.deleteInstance(JsonUtil.parseObject(req, HuaweiInstanceRequest.class));
    }

    @Override
    public boolean hardShutdownInstance(String req) {
        HuaweiInstanceRequest request = JsonUtil.parseObject(req, HuaweiInstanceRequest.class);
        request.setForce(true);
        return HuaweiSyncCloudApi.powerOff(request);
    }

    @Override
    public boolean hardRebootInstance(String req) {
        HuaweiInstanceRequest request = JsonUtil.parseObject(req, HuaweiInstanceRequest.class);
        request.setForce(true);
        return HuaweiSyncCloudApi.rebootInstance(request);
    }

    @Override
    public FormObject getCreateDiskForm() {
        return FormUtil.toForm(HuaweiCreateDiskForm.class);
    }

    public List<Map<String, String>> getDiskTypes(String req) {
        return HuaweiSyncCloudApi.getDiskTypes(JsonUtil.parseObject(req, HuaweiGetDiskTypeRequest.class));
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
        return HuaweiSyncCloudApi.createDisks(JsonUtil.parseObject(req, HuaweiCreateDisksRequest.class));
    }

    @Override
    public F2CDisk createDisk(String req) {
        return HuaweiSyncCloudApi.createDisk(JsonUtil.parseObject(req, HuaweiCreateDiskRequest.class));
    }

    @Override
    public boolean enlargeDisk(String req) {
        return HuaweiSyncCloudApi.enlargeDisk(JsonUtil.parseObject(req, HuaweiResizeDiskRequest.class));
    }

    @Override
    public boolean attachDisk(String req) {
        return HuaweiSyncCloudApi.attachDisk(JsonUtil.parseObject(req, HuaweiAttachDiskRequest.class));
    }

    @Override
    public boolean detachDisk(String req) {
        return HuaweiSyncCloudApi.detachDisk(JsonUtil.parseObject(req, HuaweiDetachDiskRequest.class));
    }

    @Override
    public boolean deleteDisk(String req) {
        return HuaweiSyncCloudApi.deleteDisk(JsonUtil.parseObject(req, HuaweiDeleteDiskRequest.class));
    }

    @Override
    public List<F2CPerfMetricMonitorData> getF2CPerfMetricMonitorData(String req) {
        return HuaweiSyncCloudApi.getF2CPerfMetricList(JsonUtil.parseObject(req, GetMetricsRequest.class));
    }

    public List<NovaAvailabilityZoneDTO> getAvailabilityZone(String req){
        return HuaweiSyncCloudApi.getAvailabilityZone(JsonUtil.parseObject(req,HuaweiVmCreateRequest.class));
    }

    /**
     * 计费模式
     * @return
     */
    public List<Map<String, String>> getBillingMode(String req) {
        List<Map<String, String>> billingModes = new ArrayList<>();
        Map<String, String> defaultMap = new HashMap<>();
        defaultMap.put("id", "0");
        defaultMap.put("name", "按需计费");
        billingModes.add(defaultMap);
        Map<String, String> thinMap = new HashMap<>();
        thinMap.put("id", "1");
        thinMap.put("name", "包年/包月");
        billingModes.add(thinMap);
        return billingModes;
    }

    /**
     * 周期类型
     * @return
     */
    public List<Map<String, String>> getPeriodType(String req) {
        List<Map<String, String>> periodTypes = new ArrayList<>();
        Map<String, String> monthMap = new HashMap<>();
        monthMap.put("id", "month");
        monthMap.put("name", "月");
        periodTypes.add(monthMap);
        Map<String, String> yearMap = new HashMap<>();
        yearMap.put("id", "year");
        yearMap.put("name", "年");
        periodTypes.add(yearMap);
        return periodTypes;
    }

    public InstanceSpecConfig getInstanceSpecTypes(String req){
        return HuaweiSyncCloudApi.getInstanceSpecTypes(JsonUtil.parseObject(req,HuaweiVmCreateRequest.class));
    }

    public List<Map<String, String>> getAllDiskTypes(String req) {
        return HuaweiSyncCloudApi.getAllDiskTypes(JsonUtil.parseObject(req, HuaweiVmCreateRequest.class));
    }

    public static void main(String[] args) {
        String a = "{\"id\":\"0769f3f764f8eeada44b54014224180e\",\"name\":\"华为云-产品\",\"platform\":\"fit2cloud_huawei_platform\",\"credential\":\"{\\\"ak\\\":\\\"GE7J2S8B9AYNG2OO1RHP\\\",\\\"sk\\\":\\\"XQsLdaV3wd1W8SUntVw7FOf2MCphuJpiMZ1uw5Se\\\"}\",\"state\":false,\"createTime\":\"2022-11-11 16:06:35\",\"updateTime\":\"2022-11-15 11:24:23\",\"accountId\":\"0769f3f764f8eeada44b54014224180e\",\"count\":1,\"amount\":0,\"billingMode\":\"0\",\"region\":\"cn-south-1\",\"availabilityZone\":\"cn-south-1c\",\"disks\":[{\"size\":40,\"diskType\":\"ESSD\",\"amountText\":\"\",\"deleteWithInstance\":true}]}";
        System.out.println(JsonUtil.toJSONString(JsonUtil.parseObject(a,HuaweiVmCreateRequest.class)));
    }
 }
