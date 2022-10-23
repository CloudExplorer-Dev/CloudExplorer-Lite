package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.controller.request.disk.PageVmCloudDiskRequest;
import com.fit2cloud.dto.VmCloudDiskDTO;
import com.fit2cloud.dto.VmCloudServerDTO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IVmCloudDiskService extends IService<VmCloudDisk> {

    IPage<VmCloudDiskDTO> pageVmCloudDisk(PageVmCloudDiskRequest request);

    /**
     * 根据云账号ID获取虚拟机列表
     *
     * @param accountId
     * @return
     */
    List<VmCloudServerDTO> cloudServerList(String accountId);

    /**
     * 根据云ID获取磁盘
     *
     * @param id
     * @return
     */
    VmCloudDiskDTO cloudDisk(String id);


    /**
     * 扩容磁盘
     *
     * @param id
     * @param newDiskSize
     * @return
     */
    boolean enlarge(String id, long newDiskSize);

    /**
     * 挂载磁盘
     *
     * @param id
     * @param instanceUuid
     * @return
     */
    boolean attach(String id, String instanceUuid, Boolean deleteWithVm);

    /**
     * 卸载磁盘
     *
     * @param id
     * @return
     */
    boolean detach(String id);

    /**
     * 删除磁盘
     *
     * @param id
     * @return
     */
    boolean delete(String id);
}
