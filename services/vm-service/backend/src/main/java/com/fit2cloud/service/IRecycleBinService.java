package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.RecycleBin;
import com.fit2cloud.controller.request.recycle_bin.BatchRecycleRequest;
import com.fit2cloud.controller.request.recycle_bin.PageRecycleBinRequest;
import com.fit2cloud.dto.RecycleBinDTO;

/**
 * @author : LiuDi
 * @date : 2023/1/12 14:38
 */
public interface IRecycleBinService extends IService<RecycleBin> {

    IPage<RecycleBinDTO> pageRecycleBin(PageRecycleBinRequest request);

    boolean batchDeleteResource(BatchRecycleRequest request);

    boolean deleteResource(String recycleId);

    boolean batchRecoverResource(BatchRecycleRequest request);

    boolean recoverResource(String recycleId);

    boolean getRecycleEnableStatus();

}
