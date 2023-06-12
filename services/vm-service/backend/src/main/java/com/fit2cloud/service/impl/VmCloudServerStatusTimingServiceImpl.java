package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.autoconfigure.ThreadPoolConfig;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.dao.entity.VmCloudServerStatusTiming;
import com.fit2cloud.dao.mapper.VmCloudServerStatusTimingMapper;
import com.fit2cloud.provider.constants.F2CInstanceStatus;
import com.fit2cloud.service.IVmCloudServerStatusTimingService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 云主机运行关机状态计时 服务实现类
 * </p>
 *
 * @author fit2cloud
 */
@Service
public class VmCloudServerStatusTimingServiceImpl extends ServiceImpl<VmCloudServerStatusTimingMapper, VmCloudServerStatusTiming> implements IVmCloudServerStatusTimingService {
    @Resource
    private ThreadPoolConfig threadPoolConfig;
    @Resource
    private VmCloudServerStatusTimingMapper vmCloudServerStatusTimingMapper;

    /**
     * 单个虚拟机状态事件（只管开关机）
     * 完成虚拟机创建或者开关机操作后，调用该方法
     * 先查询该虚拟机是否已经记录过状态数据
     * 已记录
     * 开机操作时：获取最新一条数据的关机时间，计算虚拟机的关机时长，清空ID与开机时间，新增一条开机操作记录数据。
     * 关机操作时：获取最新一条数据的开机时间，计算该虚拟机的开机时长，清空ID与开机时间，新增一条关机操作记录数据。
     * 未记录 直接插入一条新数据。
     *
     * @param vm          虚拟机
     * @param operateTime 操作完成时间
     * @author jianneng
     * @date 2023/05/30
     */
    @Override
    public void singleInsertVmCloudServerStatusEvent(VmCloudServer vm, LocalDateTime operateTime) {
        try {
            QueryWrapper<VmCloudServerStatusTiming> wrapper = new QueryWrapper<>();
            wrapper.eq(true, ColumnNameUtil.getColumnName(VmCloudServerStatusTiming::getCloudServerId, true), vm.getId());
            wrapper.orderByDesc(true, ColumnNameUtil.getColumnName(VmCloudServerStatusTiming::getId, true));
            List<VmCloudServerStatusTiming> vmCloudServerStatusMeteringList = vmCloudServerStatusTimingMapper.selectList(wrapper);
            record(vm, operateTime, wrapper, CollectionUtils.isNotEmpty(vmCloudServerStatusMeteringList) ? vmCloudServerStatusMeteringList.get(0) : null);
        } catch (Exception e) {
            LogUtil.error("记录云主机状态事件失败:" + vm.getInstanceName(), e);
            e.printStackTrace();
        }
    }

    /**
     * 批量插入vm云服务器状态事件
     * 同步云主机时调用该方法
     *
     * @param vms         虚拟机
     * @param operateTime 操作完成时间
     * @author jianneng
     * @date 2023/05/30
     */
    @Override
    public void batchInsertVmCloudServerStatusEvent(List<VmCloudServer> vms, LocalDateTime operateTime) {
        try {
            threadPoolConfig.workThreadPool().execute(() -> {
                if (CollectionUtils.isNotEmpty(vms)) {
                    QueryWrapper<VmCloudServerStatusTiming> batchWrapper = new QueryWrapper<>();
                    batchWrapper.in(true, ColumnNameUtil.getColumnName(VmCloudServerStatusTiming::getCloudServerId, true), vms.stream().map(VmCloudServer::getId).toList());
                    batchWrapper.groupBy(true, ColumnNameUtil.getColumnName(VmCloudServerStatusTiming::getCloudServerId, true));
                    batchWrapper.orderByDesc(true, ColumnNameUtil.getColumnName(VmCloudServerStatusTiming::getId, true));
                    List<VmCloudServerStatusTiming> vmCloudServerStatusMeteringList = vmCloudServerStatusTimingMapper.selectList(batchWrapper);
                    for (VmCloudServer vm : vms) {
                        if(StringUtils.isEmpty(vm.getId())){
                            throw new Fit2cloudException(404,"云主机["+vm.getInstanceName()+"]ID为空");
                        }
                        // 获得虚拟机的记录
                        List<VmCloudServerStatusTiming> vmMeteringList = vmCloudServerStatusMeteringList.stream().filter(v -> StringUtils.equalsIgnoreCase(v.getCloudServerId(), vm.getId())).collect(Collectors.toList());
                        QueryWrapper<VmCloudServerStatusTiming> wrapper = new QueryWrapper<>();
                        try {
                            record(vm, operateTime, wrapper, CollectionUtils.isNotEmpty(vmMeteringList) ? vmMeteringList.get(0) : null);
                        } catch (Exception e) {
                            LogUtil.error("初始化云主机状态时间失败[" + vm.getInstanceName() + "]:" + e.getMessage());
                        }

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("初始化云主机状态时间失败:" + e.getMessage());
        }
    }

    /**
     * 记录
     *
     * @param vm                          虚拟机
     * @param operateTime                 操作时间
     * @param wrapper                     包装器
     * @param vmCloudServerStatusMetering vm云服务器状态计量
     * @author jianneng
     * @date 2023/05/30
     */
    private void record(VmCloudServer vm, LocalDateTime operateTime, QueryWrapper<VmCloudServerStatusTiming> wrapper, VmCloudServerStatusTiming vmCloudServerStatusMetering) {

        boolean isRunning = StringUtils.equalsIgnoreCase(vm.getInstanceStatus(), F2CInstanceStatus.Running.name());
        boolean isStopped = StringUtils.equalsIgnoreCase(vm.getInstanceStatus(), F2CInstanceStatus.Stopped.name());

        if (vmCloudServerStatusMetering == null) {
            vmCloudServerStatusMetering = new VmCloudServerStatusTiming();
        }
        vmCloudServerStatusMetering.setCloudServerId(vm.getId());
        // 开机计算关机时长
        if (isRunning && Objects.nonNull(vmCloudServerStatusMetering.getId()) && Objects.nonNull(vmCloudServerStatusMetering.getOffTime())) {
            Duration stoppedTime = Duration.between(vmCloudServerStatusMetering.getOffTime(), operateTime);
            vmCloudServerStatusMetering.setShutdownDuration(stoppedTime.getSeconds() + (Objects.isNull(vmCloudServerStatusMetering.getShutdownDuration()) ? 0 : vmCloudServerStatusMetering.getShutdownDuration()));
            vmCloudServerStatusMetering.setOnTime(operateTime);
            vmCloudServerStatusMetering.setOffTime(null);
            updateInstance(vmCloudServerStatusMetering, wrapper);
        }
        // 关机计算开机时长
        if (isStopped && Objects.nonNull(vmCloudServerStatusMetering.getId()) && Objects.nonNull(vmCloudServerStatusMetering.getOnTime())) {
            Duration runningTime = Duration.between(vmCloudServerStatusMetering.getOnTime(), operateTime);
            vmCloudServerStatusMetering.setRunningDuration(runningTime.getSeconds() + (Objects.isNull(vmCloudServerStatusMetering.getRunningDuration()) ? 0 : vmCloudServerStatusMetering.getRunningDuration()));
            vmCloudServerStatusMetering.setOffTime(operateTime);
            vmCloudServerStatusMetering.setOnTime(null);
            updateInstance(vmCloudServerStatusMetering, wrapper);
        }
        // 开机未记录，插入新数据
        boolean insertRunning = isRunning && Objects.isNull(vmCloudServerStatusMetering.getId()) && Objects.isNull(vmCloudServerStatusMetering.getOnTime());
        // 关机未记录，插入新数据
        boolean insertStopped = isStopped && Objects.isNull(vmCloudServerStatusMetering.getId()) && Objects.isNull(vmCloudServerStatusMetering.getOffTime());
        if (insertRunning || insertStopped) {
            vmCloudServerStatusMetering.setOnTime(isRunning ? operateTime : null);
            vmCloudServerStatusMetering.setOffTime(isStopped ? operateTime : null);
            insertInstance(vmCloudServerStatusMetering);
        }
    }

    private void updateInstance(VmCloudServerStatusTiming vmCloudServerStatusMetering, QueryWrapper<VmCloudServerStatusTiming> wrapper) {
        vmCloudServerStatusTimingMapper.update(vmCloudServerStatusMetering, wrapper);
    }

    private void insertInstance(VmCloudServerStatusTiming vmCloudServerStatusMetering) {
        vmCloudServerStatusTimingMapper.insert(vmCloudServerStatusMetering);
    }
}
