package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.constants.JobConstants;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.dao.entity.VmCloudDisk;
import com.fit2cloud.dao.entity.VmCloudImage;
import com.fit2cloud.dao.entity.VmCloudServer;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.service.ISyncProviderService;
import com.fit2cloud.service.IVmCloudDiskService;
import com.fit2cloud.service.IVmCloudImageService;
import com.fit2cloud.service.IVmCloudServerService;
import io.reactivex.rxjava3.functions.BiFunction;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @Author:张少虎
 * @Date: 2022/9/21  11:37 AM
 * @Version 1.0
 * @注释:
 */
@Service
public class SyncProviderServiceImpl implements ISyncProviderService {
    @Resource
    private IBaseCloudAccountService cloudAccountService;
    @Resource
    private IVmCloudServerService vmCloudServerService;
    @Resource
    private IVmCloudImageService vmCloudImageService;
    @Resource
    private IVmCloudDiskService vmCloudDiskService;

    @Override
    public void syncCloudServer(String cloudAccountId, List<Credential.Region> regions) {
        CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
        Arrays.stream(ProviderConstants.values()).filter(providerConstants -> providerConstants.name().equals(cloudAccount.getPlatform())).findFirst().ifPresent(providerConstants -> {
            Class<? extends ICloudProvider> cloudProvider = ProviderConstants.valueOf(cloudAccount.getPlatform()).getCloudProvider();
            for (Credential.Region region : regions) {
                List<F2CVirtualMachine> f2CVirtualMachines = exec(cloudProvider, getParams(cloudAccount.getCredential(), region.getRegionId()), ICloudProvider::listVirtualMachine);
                List<VmCloudServer> vmCloudServers = f2CVirtualMachines.stream().map(f2CVirtualMachine -> toVmCloudServer(f2CVirtualMachine, cloudAccountId)).toList();
                for (VmCloudServer vmCloudServer : vmCloudServers) {
                    vmCloudServerService.saveOrUpdate(vmCloudServer, new LambdaQueryWrapper<VmCloudServer>().eq(VmCloudServer::getAccountId, vmCloudServer.getAccountId()).eq(VmCloudServer::getInstanceId, vmCloudServer.getInstanceId()));
                }
            }
        });
    }


    @Override
    public void syncCloudServer(Map<String, Object> params) {
        String cloudAccountId = getCloudAccountId(params);
        List<Credential.Region> regions = getRegions(params);
        if (params.containsKey(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()) && params.containsKey(JobConstants.CloudAccount.REGIONS.name())) {
            syncCloudServer(cloudAccountId, regions);
        }
    }


    @Override
    public void syncCloudImage(String cloudAccountId, List<Credential.Region> regions) {
        CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
        Arrays.stream(ProviderConstants.values()).filter(providerConstants -> providerConstants.name().equals(cloudAccount.getPlatform())).findFirst().ifPresent(providerConstants -> {
            Class<? extends ICloudProvider> cloudProvider = ProviderConstants.valueOf(cloudAccount.getPlatform()).getCloudProvider();
            for (Credential.Region region : regions) {
                List<F2CImage> images = exec(cloudProvider, getParams(cloudAccount.getCredential(), region.getRegionId()), ICloudProvider::listImage);
                List<VmCloudImage> vmCloudImages = images.stream().map(img -> toVmImage(img, region, cloudAccountId)).toList();
                for (VmCloudImage vmCloudImage : vmCloudImages) {
                    vmCloudImageService.saveOrUpdate(vmCloudImage, new LambdaQueryWrapper<VmCloudImage>().eq(VmCloudImage::getAccountId, vmCloudImage.getAccountId()).eq(VmCloudImage::getImageId, vmCloudImage.getImageId()));
                }
            }
        });
    }

    @Override
    public void syncCloudImage(Map<String, Object> params) {
        String cloudAccountId = getCloudAccountId(params);
        List<Credential.Region> regions = getRegions(params);
        if (params.containsKey(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()) && params.containsKey(JobConstants.CloudAccount.REGIONS.name())) {
            syncCloudImage(cloudAccountId, regions);
        }
    }


    @Override
    public void syncCloudDisk(String cloudAccountId, List<Credential.Region> regions) {
        CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
        Arrays.stream(ProviderConstants.values()).filter(providerConstants -> providerConstants.name().equals(cloudAccount.getPlatform())).findFirst().ifPresent(providerConstants -> {
            Class<? extends ICloudProvider> cloudProvider = ProviderConstants.valueOf(cloudAccount.getPlatform()).getCloudProvider();
            for (Credential.Region region : regions) {
                List<F2CDisk> disks = exec(cloudProvider, getParams(cloudAccount.getCredential(), region.getRegionId()), ICloudProvider::listDisk);
                List<VmCloudDisk> vmCloudDisks = disks.stream().map(img -> toVmDisk(img, region, cloudAccountId)).toList();
                for (VmCloudDisk vmCloudDisk : vmCloudDisks) {
                    vmCloudDiskService.saveOrUpdate(vmCloudDisk, new LambdaQueryWrapper<VmCloudDisk>().eq(VmCloudDisk::getAccountId, vmCloudDisk.getAccountId()).eq(VmCloudDisk::getDiskId, vmCloudDisk.getImageId()));
                }
            }
        });
    }


    @Override
    public void syncCloudDisk(Map<String, Object> params) {
        String cloudAccountId = getCloudAccountId(params);
        List<Credential.Region> regions = getRegions(params);
        if (params.containsKey(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name()) && params.containsKey(JobConstants.CloudAccount.REGIONS.name())) {
            syncCloudDisk(cloudAccountId, regions);
        }
    }

    /**
     * 执行函数
     *
     * @param providerClass 执行处理器
     * @param req           请求参数画
     * @param exec          执行函数
     * @param <T>           执行函数返回对象
     * @return 执行函数返回对象泛型
     */
    private <T> List<T> exec(Class<? extends ICloudProvider> providerClass, String req, BiFunction<ICloudProvider, String, List<T>> exec) {
        ICloudProvider iCloudProvider = null;
        try {
            iCloudProvider = providerClass.getConstructor().newInstance();
            return exec.apply(iCloudProvider, req);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取函数执行参数
     *
     * @param credential 认证信息
     * @param region     区域信息
     * @return json参数
     */
    private String getParams(String credential, String region) {
        HashMap<String, String> params = new HashMap<>();
        params.put("credential", credential);
        params.put("regionId", region);
        return JsonUtil.toJSONString(params);
    }

    /**
     * 获取云账号
     *
     * @param map 参数Map
     * @return 云账号
     */
    private String getCloudAccountId(Map<String, Object> map) {
        return (String) map.get(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name());
    }

    /**
     * 获取同步区域
     *
     * @param map 参数Map
     * @return 同步区域
     */
    private List<Credential.Region> getRegions(Map<String, Object> map) {
        // todo 这个地方不能强转,强转后会出现线程阻塞
        String jsonString = JsonUtil.toJSONString(map.get(JobConstants.CloudAccount.REGIONS.name()));
        return JsonUtil.parseArray(jsonString, Credential.Region.class);
    }

    /**
     * 将虚拟机同步对象转换为实体对
     *
     * @param f2CVirtualMachine 同步对象
     * @param cloudAccountId    实体对象
     * @return 实体对象
     */
    private VmCloudServer toVmCloudServer(F2CVirtualMachine f2CVirtualMachine, String cloudAccountId) {
        VmCloudServer vmCloudServer = new VmCloudServer();
        BeanUtils.copyProperties(f2CVirtualMachine, vmCloudServer);
        vmCloudServer.setAccountId(cloudAccountId);
        vmCloudServer.setInstanceUuid(f2CVirtualMachine.getInstanceUUID());
        vmCloudServer.setInstanceName(f2CVirtualMachine.getName());
        vmCloudServer.setRemoteIp(f2CVirtualMachine.getRemoteIP());
        vmCloudServer.setIpArray(JsonUtil.toJSONString(f2CVirtualMachine.getIpArray()));
        return vmCloudServer;
    }

    /**
     * 将虚拟机磁盘对象转换实体对象
     *
     * @param disk           磁盘对象
     * @param region         区域对象
     * @param cloudAccountId 云账号
     * @return 实体对象
     */
    private VmCloudDisk toVmDisk(F2CDisk disk, Credential.Region region, String cloudAccountId) {
        VmCloudDisk vmCloudDisk = new VmCloudDisk();
        vmCloudDisk.setAccountId(cloudAccountId);
        vmCloudDisk.setDescription(disk.getDescription());
        vmCloudDisk.setRegion(region.getRegionId());
        vmCloudDisk.setDiskId(disk.getDiskId());
        vmCloudDisk.setDiskName(disk.getDiskName());
        vmCloudDisk.setDiskType(disk.getDiskType());
        vmCloudDisk.setBootable(disk.isBootable());
        vmCloudDisk.setCategory(disk.getCategory());
        vmCloudDisk.setDatastoreId(disk.getDatastoreUniqueId());
        vmCloudDisk.setDevice(disk.getDevice());
        vmCloudDisk.setProjectId(disk.getProjectId());
        vmCloudDisk.setDeleteWithInstance(disk.getDeleteWithInstance());
        vmCloudDisk.setInstanceUuid(disk.getInstanceUuid());
        vmCloudDisk.setSize(disk.getSize());
        vmCloudDisk.setStatus(disk.getStatus());
        vmCloudDisk.setDiskChargeType(disk.getDiskChargeType());
        vmCloudDisk.setZone(disk.getZone());
        return vmCloudDisk;
    }

    /**
     * 将同步镜像转化为实体对象
     *
     * @param image          同步镜像对象
     * @param region         区域对象
     * @param cloudAccountId 云账号id
     * @return 实例对象
     */

    private VmCloudImage toVmImage(F2CImage image, Credential.Region region, String cloudAccountId) {
        VmCloudImage vmCloudImage = new VmCloudImage();
        vmCloudImage.setRegion(region.getRegionId());
        vmCloudImage.setRegionName(region.getName());
        vmCloudImage.setImageId(image.getId());
        vmCloudImage.setDiskSize(image.getDiskSize());
        vmCloudImage.setDescription(image.getDescription());
        vmCloudImage.setImageName(image.getName());
        vmCloudImage.setAccountId(cloudAccountId);
        vmCloudImage.setOs(image.getOs());
        return vmCloudImage;
    }


}
