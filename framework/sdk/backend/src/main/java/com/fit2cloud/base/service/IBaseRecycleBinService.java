package com.fit2cloud.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.RecycleBin;
import com.fit2cloud.common.constants.ResourceTypeConstants;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IBaseRecycleBinService extends IService<RecycleBin> {

    void insertRecycleRecord(String resourceId, ResourceTypeConstants resourceType);

    boolean updateRecycleRecordOnRecover(String id);

    boolean updateRecycleRecordOnDelete(String resourceId, ResourceTypeConstants resourceType);
}
