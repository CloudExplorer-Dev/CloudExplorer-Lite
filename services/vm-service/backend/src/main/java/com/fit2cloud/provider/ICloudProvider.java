package com.fit2cloud.provider;


import com.fit2cloud.common.form.vo.FormObject;
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
     * 获取创建云主机的表单数据
     *
     * @return
     */
    FormObject getCreateServerForm();

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
     *
     * @param req
     * @return
     */
    boolean powerOff(String req);

    /**
     * 虚拟机打开电源
     *
     * @param req
     * @return
     */
    boolean powerOn(String req);

    /**
     * 虚拟机关机
     *
     * @param req
     */
    boolean shutdownInstance(String req);

    /**
     * 虚拟机重启
     *
     * @param req
     * @return
     */
    boolean rebootInstance(String req);

    /**
     * 删除虚拟机
     *
     * @param req
     * @return
     */
    boolean deleteInstance(String req);

}
