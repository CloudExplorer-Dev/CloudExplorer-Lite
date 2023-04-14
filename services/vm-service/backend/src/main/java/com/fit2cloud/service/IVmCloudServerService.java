package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.controller.request.GrantRequest;
import com.fit2cloud.controller.request.vm.BatchOperateVmRequest;
import com.fit2cloud.controller.request.vm.ChangeServerConfigRequest;
import com.fit2cloud.controller.request.vm.CreateServerRequest;
import com.fit2cloud.controller.request.vm.PageVmCloudServerRequest;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.response.JobRecordResourceResponse;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IVmCloudServerService extends IService<VmCloudServer> {

    IPage<VmCloudServerDTO> pageVmCloudServer(PageVmCloudServerRequest request);

    List<VmCloudServer> listVmCloudServer(PageVmCloudServerRequest request);

    boolean powerOff(String vmId);

    boolean powerOn(String vmId);

    boolean shutdownInstance(String vmId);

    boolean rebootInstance(String vmId);

    boolean deleteInstance(String vmId);

    boolean deleteFailedRecord(String vmId);

    boolean recycleInstance(String vmId);

    boolean recoverInstance(String recycleBinId);

    boolean batchOperate(BatchOperateVmRequest request);

    List<JobRecordResourceResponse> findCloudServerOperateStatus(List<String> vmIds);

    VmCloudServerDTO getById(String vmId);

    List<VmCloudServerDTO> getByIds(List<String> vmIds);

    boolean createServer(CreateServerRequest request);

    boolean changeConfig(ChangeServerConfigRequest request);

    FormObject getConfigUpdateForm(String platform);

    String calculateConfigUpdatePrice(String platform, Map<String, Object> params);

    boolean grant(GrantRequest grantServerRequest);

    long countVmCloudServer();

    List<Map<String, Object>> countByStatus();
}
