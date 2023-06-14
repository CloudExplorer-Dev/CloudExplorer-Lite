package com.fit2cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.dao.entity.VmCloudServerStatusTiming;

import java.time.LocalDateTime;

/**
 * <p>
 * 云主机运行关机状态计时 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IVmCloudServerStatusTimingService extends IService<VmCloudServerStatusTiming> {
    /**
     * 单个记录云主机开关机状态时长
     *
     * @param vm          虚拟机
     * @param operateTime 操作完成时间
     * @author jianneng
     */
    void singleInsertVmCloudServerStatusEvent(VmCloudServer vm, LocalDateTime operateTime);

    /**
     * 批量记录云主机开关机状态时长
     *
     * @param accountId   云账号ID
     * @param regionId    regionId
     * @param operateTime 操作完成时间
     * @author jianneng
     */
    void batchInsertVmCloudServerStatusEvent(String accountId, String regionId, LocalDateTime operateTime);
}
