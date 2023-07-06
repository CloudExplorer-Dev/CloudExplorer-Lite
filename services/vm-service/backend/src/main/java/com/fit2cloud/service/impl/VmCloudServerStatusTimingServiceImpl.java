package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.autoconfigure.ThreadPoolConfig;
import com.fit2cloud.base.entity.VmCloudServer;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.log.utils.LogUtil;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.dao.entity.VmCloudServerStatusTiming;
import com.fit2cloud.dao.mapper.VmCloudServerMapper;
import com.fit2cloud.dao.mapper.VmCloudServerStatusTimingMapper;
import com.fit2cloud.dto.VmCloudServerDTO;
import com.fit2cloud.service.IVmCloudServerStatusTimingService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.fit2cloud.vm.constants.F2CInstanceStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
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
    @Resource
    private VmCloudServerMapper vmCloudServerMapper;

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
            record(vm, operateTime, CollectionUtils.isNotEmpty(vmCloudServerStatusMeteringList) ? vmCloudServerStatusMeteringList.get(0) : null);
        } catch (Exception e) {
            LogUtil.error("记录云主机状态事件失败:" + vm.getInstanceName(), e);
            e.printStackTrace();
        }
    }

    /**
     * 批量插入vm云服务器状态事件
     * 同步云主机时调用该方法
     *
     * @param accountId   云账号ID
     * @param operateTime 操作完成时间
     * @author jianneng
     * @date 2023/05/30
     */
    @Override
    public void batchInsertVmCloudServerStatusEvent(String accountId, String regionId, LocalDateTime operateTime) {
        try {
            threadPoolConfig.workThreadPool().execute(() -> {
                LogUtil.debug("开始初始化云账号[" + accountId + "][" + regionId + "]云主机状态时间");
                LambdaQueryWrapper<VmCloudServer> vmCloudServerLambdaQueryWrapper = new LambdaQueryWrapper<>();
                vmCloudServerLambdaQueryWrapper.eq(VmCloudServer::getAccountId, accountId).eq(VmCloudServer::getRegion, regionId)
                        .in(VmCloudServer::getInstanceStatus, Arrays.asList(F2CInstanceStatus.Stopped.name(), F2CInstanceStatus.Running.name()));
                List<VmCloudServerDTO> vmList = vmCloudServerMapper.selectServerList(vmCloudServerLambdaQueryWrapper);
                LogUtil.debug("成功初始化[" + vmList.size() + "]台虚拟机的状态时间");
                if (CollectionUtils.isNotEmpty(vmList)) {
                    QueryWrapper<VmCloudServerStatusTiming> batchWrapper = new QueryWrapper<>();
                    batchWrapper.in(true, ColumnNameUtil.getColumnName(VmCloudServerStatusTiming::getCloudServerId, true), vmList.stream().map(VmCloudServer::getId).toList());
                    batchWrapper.groupBy(true, ColumnNameUtil.getColumnName(VmCloudServerStatusTiming::getCloudServerId, true));
                    batchWrapper.orderByDesc(true, ColumnNameUtil.getColumnName(VmCloudServerStatusTiming::getId, true));
                    List<VmCloudServerStatusTiming> vmCloudServerStatusMeteringList = vmCloudServerStatusTimingMapper.selectList(batchWrapper);
                    for (VmCloudServer vm : vmList) {
                        if (StringUtils.isEmpty(vm.getId())) {
                            throw new Fit2cloudException(404, "云主机[" + vm.getInstanceName() + "]ID为空");
                        }
                        // 获得虚拟机的记录
                        List<VmCloudServerStatusTiming> vmMeteringList = vmCloudServerStatusMeteringList.stream().filter(v -> StringUtils.equalsIgnoreCase(v.getCloudServerId(), vm.getId())).collect(Collectors.toList());
                        try {
                            record(vm, operateTime, CollectionUtils.isNotEmpty(vmMeteringList) ? vmMeteringList.get(0) : null);
                        } catch (Exception e) {
                            LogUtil.error("初始化云主机状态时间失败[" + vm.getInstanceName() + "]:" + e.getMessage());
                        }

                    }
                }
                LogUtil.debug("结束初始化云账号[" + accountId + "][" + regionId + "]云主机状态时间");
            });
        } catch (Exception e) {
            LogUtil.error("云账号[" + accountId + "]初始化云主机状态时间失败:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 记录
     *
     * @param vm                          虚拟机
     * @param operateTime                 操作时间
     * @param vmCloudServerStatusMetering vm云服务器状态计量
     * @author jianneng
     * @date 2023/05/30
     */
    private void record(VmCloudServer vm, LocalDateTime operateTime, VmCloudServerStatusTiming vmCloudServerStatusMetering) {

        boolean isRunning = StringUtils.equalsIgnoreCase(vm.getInstanceStatus(), F2CInstanceStatus.Running.name());
        boolean isStopped = StringUtils.equalsIgnoreCase(vm.getInstanceStatus(), F2CInstanceStatus.Stopped.name());

        if (vmCloudServerStatusMetering == null) {
            vmCloudServerStatusMetering = new VmCloudServerStatusTiming();
        }
        vmCloudServerStatusMetering.setCloudServerId(vm.getId());
        // 开机计算关机时长
        if (isRunning && Objects.nonNull(vmCloudServerStatusMetering.getId())) {
            LocalDateTime offTime = vmCloudServerStatusMetering.getOffTime();
            // 开机操作时，如果关机时间为空那么认为是，当前云主机在其他环境更改了状态，这里取之前记录的开机时间为关机时间
            if (Objects.isNull(offTime) && Objects.nonNull(vmCloudServerStatusMetering.getOnTime())) {
                offTime = vmCloudServerStatusMetering.getOnTime();
            }
            Duration stoppedTime = Duration.between(Objects.nonNull(offTime) ? offTime : operateTime, operateTime);
            vmCloudServerStatusMetering.setShutdownDuration(stoppedTime.getSeconds() + (Objects.isNull(vmCloudServerStatusMetering.getShutdownDuration()) ? 0 : vmCloudServerStatusMetering.getShutdownDuration()));
            vmCloudServerStatusMetering.setOnTime(operateTime);
            vmCloudServerStatusMetering.setOffTime(null);
            updateInstance(vmCloudServerStatusMetering);
        }
        // 关机计算开机时长
        if (isStopped && Objects.nonNull(vmCloudServerStatusMetering.getId())) {
            LocalDateTime onTime = vmCloudServerStatusMetering.getOnTime();
            // 关机操作时，如果开机时间为空那么认为是，当前云主机在其他环境更改了状态，这里取之前记录的关机时间为开机时间
            if (Objects.isNull(onTime) && Objects.nonNull(vmCloudServerStatusMetering.getOffTime())) {
                onTime = vmCloudServerStatusMetering.getOffTime();
            }
            Duration runningTime = Duration.between(Objects.nonNull(onTime) ? onTime : operateTime, operateTime);
            vmCloudServerStatusMetering.setRunningDuration(runningTime.getSeconds() + (Objects.isNull(vmCloudServerStatusMetering.getRunningDuration()) ? 0 : vmCloudServerStatusMetering.getRunningDuration()));
            vmCloudServerStatusMetering.setOffTime(operateTime);
            vmCloudServerStatusMetering.setOnTime(null);
            updateInstance(vmCloudServerStatusMetering);
        }
        // 开机未记录，插入新数据
        boolean insertRunning = isRunning && Objects.isNull(vmCloudServerStatusMetering.getId());
        // 关机未记录，插入新数据
        boolean insertStopped = isStopped && Objects.isNull(vmCloudServerStatusMetering.getId());
        if (insertRunning || insertStopped) {
            vmCloudServerStatusMetering.setOnTime(isRunning ? operateTime : null);
            vmCloudServerStatusMetering.setOffTime(isStopped ? operateTime : null);
            insertInstance(vmCloudServerStatusMetering);
        }
    }

    private void updateInstance(VmCloudServerStatusTiming vmCloudServerStatusMetering) {
        vmCloudServerStatusTimingMapper.updateById(vmCloudServerStatusMetering);
    }

    private void insertInstance(VmCloudServerStatusTiming vmCloudServerStatusMetering) {
        vmCloudServerStatusTimingMapper.insert(vmCloudServerStatusMetering);
    }
}
