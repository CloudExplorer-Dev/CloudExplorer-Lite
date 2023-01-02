package com.fit2cloud.provider;


import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.provider.entity.*;
import com.fit2cloud.provider.entity.result.CheckCreateServerResult;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  10:34 AM
 * @Version 1.0
 * @注释:
 */
public interface ICloudProvider {

    /**
     * 获取创建云主机的表单数据
     *
     * @return
     */
    FormObject getCreateServerForm();

    /**
     * 根据供应商获取对应云平台处理器
     *
     * @param platform 供应商
     * @return 处理器
     */
    static Class<? extends ICloudProvider> of(String platform) {
        return Arrays.stream(ProviderConstants.values()).filter(providerConstants -> providerConstants.name().equals(platform)).findFirst().orElseThrow(() -> new RuntimeException("不支持的云平台")).getCloudProvider();
    }

    /**
     * 获取云平台云主机
     *
     * @param req 请求参数
     * @return 云平台云主机
     */
    List<F2CVirtualMachine> listVirtualMachine(String req);

    /**
     * 获取云平台镜像
     *
     * @param req 请求参数
     * @return 云平台镜像
     */
    List<F2CImage> listImage(String req);

    /**
     * 获取云品台磁盘
     *
     * @param req 请求参数
     * @return 云平台磁盘
     */
    List<F2CDisk> listDisk(String req);

    /**
     * 获取云平台宿主机
     *
     * @param req 请求参数
     * @return 云平台宿主机
     */
    List<F2CHost> listHost(String req);

    /**
     * 获取存储器
     *
     * @param req 请求参数
     * @return 云平台存储器
     */
    List<F2CDatastore> listDataStore(String req);

    /**
     * 云主机关闭电源
     *
     * @param req
     * @return
     */
    boolean powerOff(String req);

    /**
     * 云主机打开电源
     *
     * @param req
     * @return
     */
    boolean powerOn(String req);

    /**
     * 云主机关机
     *
     * @param req
     */
    boolean shutdownInstance(String req);

    /**
     * 云主机重启
     *
     * @param req
     * @return
     */
    boolean rebootInstance(String req);

    /**
     * 删除云主机
     *
     * @param req
     * @return
     */
    boolean deleteInstance(String req);

    /**
     * 云主机关机
     *
     * @param req
     */
    boolean hardShutdownInstance(String req);

    /**
     * 云主机重启
     *
     * @param req
     * @return
     */
    boolean hardRebootInstance(String req);

    /**
     * 获取创建云磁盘的表单数据
     *
     * @return
     */
    FormObject getCreateDiskForm();

    /**
     * 获取磁盘类型
     *
     * @param getZonesRequest
     * @return
     */
    List<Map<String, String>> getDiskTypes(String getZonesRequest);

    /**
     * 获取是否删除磁盘选项
     *
     * @return
     */
    List<Map<String, String>> getDeleteWithInstance(String req);

    /**
     * 创建磁盘
     *
     * @param req
     * @return
     */
    List<F2CDisk> createDisks(String req);

    /**
     * 创建磁盘
     *
     * @param req
     * @return
     */
    F2CDisk createDisk(String req);

    /**
     * 删除磁盘
     *
     * @param req
     * @return
     */
    boolean deleteDisk(String req);

    /**
     * 挂载磁盘
     *
     * @param req
     * @return
     */
    boolean attachDisk(String req);

    /**
     * 卸载磁盘
     *
     * @param req
     * @return
     */
    boolean detachDisk(String req);

    /**
     * 扩容磁盘
     *
     * @param req
     * @return
     */
    boolean enlargeDisk(String req);

    /**
     * 监控信息
     *
     * @param req
     * @return
     */
    List<F2CPerfMetricMonitorData> getF2CPerfMetricMonitorData(String req);

    /**
     * 宿主机监控信息
     *
     * @param req
     * @return
     */
    List<F2CPerfMetricMonitorData> getF2CHostPerfMetricMonitorData(String req);

    /**
     * 云磁盘监控信息
     *
     * @param req
     * @return
     */
    List<F2CPerfMetricMonitorData> getF2CDiskPerfMetricMonitorData(String req);

    /**
     * 存储器监控信息
     *
     * @param req
     * @return
     */
    List<F2CPerfMetricMonitorData> getF2CDatastorePerfMetricMonitorData(String req);

    F2CVirtualMachine getSimpleServerByCreateRequest(String req);

    F2CVirtualMachine createVirtualMachine(String req);

    CheckCreateServerResult validateServerCreateRequest(String req);

    /**
     * 询价
     * @param req
     * @return
     */
    String calculateConfigPrice(String req);

}
