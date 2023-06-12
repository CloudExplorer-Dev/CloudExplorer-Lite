package com.fit2cloud.base.service.impl;

import com.fit2cloud.base.entity.VmCloudServerStatusTiming;
import com.fit2cloud.base.mapper.BaseVmCloudServerStatusTimingMapper;
import com.fit2cloud.base.service.IBaseVmCloudServerStatusTimingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.dto.operation.VmCloudServerStatusTimingDTO;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 云主机运行关机状态计时 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
@Service
public class BaseVmCloudServerStatusTimingServiceImpl extends ServiceImpl<BaseVmCloudServerStatusTimingMapper, VmCloudServerStatusTimingDTO> implements IBaseVmCloudServerStatusTimingService {

}
