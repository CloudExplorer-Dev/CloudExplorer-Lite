package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.VmCloudImage;
import com.fit2cloud.controller.request.images.PageVmCloudImageRequest;
import com.fit2cloud.controller.request.images.VmCloudImageRequest;
import com.fit2cloud.controller.request.vm.PageVmCloudServerRequest;
import com.fit2cloud.dto.VmCloudDiskDTO;
import com.fit2cloud.dto.VmCloudImageDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
public interface IVmCloudImageService extends IService<VmCloudImage> {
    IPage<VmCloudImageDTO> pageVmCloudImage(PageVmCloudImageRequest request);

    List<VmCloudImage> listVmCloudImage(String request);
}
