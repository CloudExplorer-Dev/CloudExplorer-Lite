package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.controller.request.vm.PageVmCloudServerRequest;
import com.fit2cloud.dto.VmCloudServerDTO;

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

}
