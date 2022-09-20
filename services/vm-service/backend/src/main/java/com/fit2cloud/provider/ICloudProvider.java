package com.fit2cloud.provider;


import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;

import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  10:34 AM
 * @Version 1.0
 * @注释:
 */
public interface ICloudProvider {
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
}
