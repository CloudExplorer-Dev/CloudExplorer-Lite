package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.controller.request.GrantRequest;
import com.fit2cloud.controller.request.disk.CreateVmCloudDiskRequest;
import com.fit2cloud.controller.request.disk.ListVmRequest;
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
     * 根据云账号ID获取云主机列表
     *
     * @param req
     * @return
     */
    List<VmCloudServerDTO> cloudServerList(ListVmRequest req);

    /**
     * 根据云ID获取磁盘
     *
     * @param id
     * @return
     */
    VmCloudDiskDTO cloudDisk(String id);

    /**
     * 根据云平台获取获取创建磁盘的表单数据
     *
     * @param platform
     * @return
     */
    FormObject getCreateDiskForm(String platform);

    /**
     * 创建磁盘
     *
     * @param request
     * @return
     */
    boolean createDisk(CreateVmCloudDiskRequest request);

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
     * 批量挂载磁盘
     *
     * @param ids
     * @param instanceUuid
     * @param deleteWithVm
     * @return
     */
    boolean batchAttach(String[] ids, String instanceUuid, Boolean deleteWithVm);

    /**
     * 卸载磁盘
     *
     * @param id
     * @return
     */
    boolean detach(String id);

    /**
     * 批量卸载磁盘
     *
     * @param ids
     * @return
     */
    boolean batchDetach(String[] ids);

    /**
     * 删除磁盘
     *
     * @param id
     * @return
     */
    boolean delete(String id);

    /**
     * 批量删除磁盘
     *
     * @param ids
     * @return
     */
    boolean batchDelete(String[] ids);

    /**
     * 批量授权磁盘
     *
     * @param grantDiskRequest
     * @return
     */
    boolean grant(GrantRequest grantDiskRequest);

    /**
     * 批量将磁盘放入回收站
     *
     * @param ids
     * @return
     */
    boolean batchRecycleDisks(String[] ids);

    /**
     * 将磁盘放入回收站
     *
     * @param id
     * @return
     */
    boolean recycleDisk(String id);


    long countDisk();
}
