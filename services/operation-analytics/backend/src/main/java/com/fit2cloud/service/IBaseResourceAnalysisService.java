package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.request.datastore.PageDatastoreRequest;
import com.fit2cloud.controller.request.host.PageHostRequest;
import com.fit2cloud.dto.VmCloudDatastoreDTO;
import com.fit2cloud.dto.VmCloudHostDTO;

/**
 * @author jianneng
 * @date 2022/12/11 18:42
 **/
public interface IBaseResourceAnalysisService {

    public IPage<VmCloudHostDTO> pageHost(PageHostRequest request);

    public IPage<VmCloudDatastoreDTO> pageDatastore(PageDatastoreRequest request);

}
