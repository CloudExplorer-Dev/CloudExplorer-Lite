package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.controller.request.vm.BatchOperateVmRequest;
import com.fit2cloud.controller.request.vm.PageVmCloudServerRequest;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.response.JobRecordResourceResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
public interface IVmCloudServerService extends IService<VmCloudServer> {

    IPage<VmCloudServerDTO> pageVmCloudServer(PageVmCloudServerRequest request);

    boolean powerOff(String vmId);

    boolean powerOn(String vmId);

    boolean shutdownInstance(String vmId,Boolean powerOff);

    boolean rebootInstance(String vmId);

    boolean deleteInstance(String vmId);

    boolean batchOperate(BatchOperateVmRequest request);

    List<JobRecordResourceResponse> findCloudServerOperateStatus(List<String> vmIds);

    VmCloudServerDTO getById(String vmId);

}
