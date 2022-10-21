package com.fit2cloud.provider;

import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;

import java.util.Arrays;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  10:34 AM
 * @Version 1.0
 * @注释:
 */
public interface ICloudProvider {

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
     * 获取云平台虚拟机
     *
     * @param req 请求参数
     * @return 云平台虚拟机
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
     * 虚拟机关闭电源
     * @param req
     * @return
     */
    boolean powerOff(String req);

    /**
     * 虚拟机打开电源
     * @param req
     * @return
     */
    boolean powerOn(String req);

    /**
     * 虚拟机关机
     * @param req
     */
    boolean shutdownInstance(String req);

    /**
     * 虚拟机重启
     * @param req
     * @return
     */
    boolean rebootInstance(String req);

    /**
     * 删除虚拟机
     * @param req
     * @return
     */
    boolean deleteInstance(String req);

    /**
     * 虚拟机关机
     * @param req
     */
    boolean hardShutdownInstance(String req);

    /**
     * 虚拟机重启
     * @param req
     * @return
     */
    boolean hardRebootInstance(String req);

    /**
     * 创建磁盘
     * @param req
     * @return
     */
    List<F2CDisk>  createDisks(String req);

    /**
     * 删除磁盘
     * @param req
     * @return
     */
    boolean deleteDisk(String req);

    /**
     * 挂载磁盘
     * @param req
     * @return
     */
    boolean attachDisk(String req);

    /**
     * 卸载磁盘
     * @param req
     * @return
     */
    boolean detachDisk(String req);

    /**
     * 扩容磁盘
     * @param req
     * @return
     */
    boolean enlargeDisk(String req);

}
