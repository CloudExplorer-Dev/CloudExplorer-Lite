package com.fit2cloud.vm;


import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CPerfMetricMonitorData;
import com.fit2cloud.vm.entity.*;
import com.fit2cloud.vm.entity.result.CheckCreateServerResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * { @Author:张少虎 }
 * { @Date: 2022/9/20  10:34 AM }
 * { @Version 1.0 }
 * { @注释: }
 */
public interface ICloudProvider extends IBaseCloudProvider {

    /**
     * 获取创建虚拟机的请求对象
     *
     * @return 创建虚拟机请求对象
     */
    Class<? extends ICreateServerRequest> getCreateServerRequestClass();

    /**
     * 获取创建云主机的表单数据
     *
     * @return 创建虚拟机表单
     */
    FormObject getCreateServerForm();

    /**
     * 根据供应商获取对应云平台处理器
     *
     * @param platform 供应商
     * @return 处理器
     */
    static Class<? extends ICloudProvider> of(String platform) {
        return null;
    }

    /**
     * 续费后到期时间
     *
     * @param req 请求参数
     * @return 续费后到期时间
     */
    String renewInstanceExpiresTime(String req);

    /**
     * 续费价钱
     *
     * @param req 请求参数
     * @return 续费价钱
     */
    BigDecimal renewInstancePrice(String req);

    /**
     * 获取 续费表单
     *
     * @return 续费表单
     */
    FormObject getRenewInstanceForm();

    /**
     * 续费
     *
     * @param req 续费请求对象
     * @return 是否续费成功
     */
    F2CVirtualMachine renewInstance(String req);

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
     * @param req 请求对象
     * @return 执行状态
     */
    boolean powerOff(String req);

    /**
     * 云主机打开电源
     *
     * @param req 请求对象
     * @return 执行状态
     */
    boolean powerOn(String req);

    /**
     * 云主机关机
     *
     * @param req 请求对象
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
     * @param getZonesRequest 请求参数
     * @return 磁盘类型列表
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
     * @param req 挂载磁盘请求参数
     * @return 挂载的磁盘信息
     */
    F2CDisk attachDisk(String req);

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
     * 创建云主机询价
     *
     * @param req
     * @return
     */
    String calculateConfigPrice(String req);

    /**
     * 配置变更
     *
     * @return
     */
    F2CVirtualMachine changeVmConfig(String req);

    FormObject getConfigUpdateForm();

    /**
     * 云主机配置变更询价
     *
     * @return
     */
    String calculateConfigUpdatePrice(String req);

    /**
     * 获取云主机关联的云磁盘
     *
     * @param req
     * @return
     */
    List<F2CDisk> getVmF2CDisks(String req);

    boolean supportResetPassword();

    boolean resetPassword(String req);
}
