package com.fit2cloud.service;

import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.dao.entity.VmCloudServerStatusTiming;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

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
     * @param vm 虚拟机
     * @param operateTime 操作完成时间
     * @author jianneng
     */
    void singleInsertVmCloudServerStatusEvent(VmCloudServer vm, LocalDateTime operateTime);

    /**
     * 批量记录云主机开关机状态时长
     *
     * @param vms 虚拟机列表
     * @param operateTime 操作完成时间
     * @author jianneng
     */
    void batchInsertVmCloudServerStatusEvent(List<VmCloudServer> vms, LocalDateTime operateTime);
}
