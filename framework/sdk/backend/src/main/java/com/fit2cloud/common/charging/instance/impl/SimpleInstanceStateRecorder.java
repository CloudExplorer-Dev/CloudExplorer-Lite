package com.fit2cloud.common.charging.instance.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fit2cloud.base.entity.ResourceInstance;
import com.fit2cloud.base.entity.ResourceInstanceState;
import com.fit2cloud.base.service.IBaseResourceInstanceService;
import com.fit2cloud.base.service.IBaseResourceInstanceStateService;
import com.fit2cloud.common.charging.constants.BillingInstanceStateConstants;
import com.fit2cloud.common.charging.entity.InstanceRecord;
import com.fit2cloud.common.charging.entity.InstanceState;
import com.fit2cloud.common.charging.instance.InstanceRecordMappingHandler;
import com.fit2cloud.common.charging.instance.InstanceStateRecorder;
import com.fit2cloud.common.charging.setting.BillSetting;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.SpringUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/26  20:23}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class SimpleInstanceStateRecorder implements InstanceStateRecorder {
    private final BillSetting setting;

    public SimpleInstanceStateRecorder(BillSetting billSetting) {
        this.setting = billSetting;
    }

    public static SimpleInstanceStateRecorder of(BillSetting billSetting) {
        return new SimpleInstanceStateRecorder(billSetting);
    }

    @Override
    public void run() {
        InstanceRecordMappingHandler resourceInstanceHandler = setting.getResourceInstanceHandler();
        // 获取需要记录状态的资源实例
        List<InstanceRecord> instanceRecords = resourceInstanceHandler.mapping();
        LocalDateTime currentDateTime = LocalDateTime.now();
        recordState(instanceRecords, currentDateTime);
        saveInstance(instanceRecords, currentDateTime);
    }

    @SneakyThrows
    private void recordState(List<InstanceRecord> instanceRecords, LocalDateTime currentDateTime) {
        String month = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        IBaseResourceInstanceStateService resourceInstanceStateService = SpringUtil.getBean(IBaseResourceInstanceStateService.class);
        List<ResourceInstanceState> resourceInstanceStates = resourceInstanceStateService.list(
                new LambdaQueryWrapper<ResourceInstanceState>()
                        .eq(ResourceInstanceState::getMonth, month)
        );
        List<ResourceInstanceState> newResourceInstanceState = instanceRecords
                .parallelStream()
                .map(instanceRecord -> toResourceInstanceState(instanceRecord, resourceInstanceStates, month, currentDateTime))
                .toList();
        resourceInstanceStateService.saveOrUpdateBatch(newResourceInstanceState);
    }

    /**
     * 转换为资源实例状态对象
     *
     * @param instanceRecord         实例详情记录
     * @param resourceInstanceStates 实例状态
     * @param month                  月份
     * @param currentDateTime        当前时间
     * @return 实例状态
     */
    private ResourceInstanceState toResourceInstanceState(InstanceRecord instanceRecord,
                                                          List<ResourceInstanceState> resourceInstanceStates,
                                                          String month,
                                                          LocalDateTime currentDateTime) {

        return resourceInstanceStates.stream()
                .filter(state -> state.getCloudAccountId().equals(instanceRecord.getCloudAccountId())
                        && state.getMonth().equals(month)
                        && state.getResourceId().equals(instanceRecord.getResourceId()))
                .findFirst()
                .map(resourceInstanceState -> {
                    String instanceState = resourceInstanceState.getInstanceState();
                    InstanceState instanceState1 = new InstanceState(instanceState);
                    instanceState1.updateHistory(InstanceState.Time.of(currentDateTime.getDayOfMonth(), currentDateTime.getHour(), currentDateTime.getMinute()), instanceRecord.getState());
                    resourceInstanceState.setInstanceState(instanceState1.toString());
                    resourceInstanceState.setResourceType(setting.getResourceInstanceType());
                    return resourceInstanceState;
                }).orElseGet(() -> {
                    ResourceInstanceState resourceInstanceState = new ResourceInstanceState();
                    InstanceState instanceState = new InstanceState();
                    instanceState.update(null, InstanceState.Time.of(currentDateTime.getDayOfMonth(), currentDateTime.getHour(), currentDateTime.getMinute() - 1), BillingInstanceStateConstants.NOT_CREATED.getState());
                    instanceState.update(InstanceState.Time.of(currentDateTime.getDayOfMonth(), currentDateTime.getHour(), currentDateTime.getMinute() - 1), InstanceState.Time.of(currentDateTime.getDayOfMonth(), currentDateTime.getHour(), currentDateTime.getMinute()), instanceRecord.getState());
                    resourceInstanceState.setInstanceState(instanceState.toString());
                    resourceInstanceState.setMonth(month);
                    resourceInstanceState.setResourceId(instanceRecord.getResourceId());
                    resourceInstanceState.setCloudAccountId(instanceRecord.getCloudAccountId());
                    resourceInstanceState.setResourceType(setting.getResourceInstanceType());
                    resourceInstanceState.setId(getResourceInstanceStateId(resourceInstanceState));
                    return resourceInstanceState;
                });

    }

    private void saveInstance(List<InstanceRecord> instanceRecordList, LocalDateTime currentDateTime) {
        List<ResourceInstance> resourceInstances = instanceRecordList.stream().map(instanceRecord -> {
            ResourceInstance resourceInstance = new ResourceInstance();
            BeanUtils.copyProperties(instanceRecord, resourceInstance);
            resourceInstance.setCreateTime(currentDateTime);
            resourceInstance.setUpdateTime(currentDateTime);
            return resourceInstance;
        }).toList();
        IBaseResourceInstanceService resourceInstanceService = SpringUtil.getBean(IBaseResourceInstanceService.class);

        List<ResourceInstance> resourceInstanceList = resourceInstanceService.listLastResourceInstance(
                new LambdaQueryWrapper<ResourceInstance>()
                        .eq(ResourceInstance::getResourceType, setting.getResourceInstanceType())
        );

        List<ResourceInstance> diffResourceInstance = resourceInstances
                .stream()
                .filter(resourceInstance -> resourceInstanceList.stream()
                        .noneMatch(cloudResourceInstance -> diffResourceInstance(cloudResourceInstance, resourceInstance))
                )
                .toList();

        resourceInstanceService.saveBatch(diffResourceInstance);
    }


    public String getResourceInstanceStateId(ResourceInstanceState resourceInstanceState) {
        return resourceInstanceState.getResourceId() + '-' +
                resourceInstanceState.getCloudAccountId() + '-' +
                resourceInstanceState.getMonth();
    }


    private String getDiffId(ResourceInstance resourceInstance) {
        return resourceInstance.getResourceId() +
                resourceInstance.getCloudAccountId() +
                CommonUtil.strSort(JsonUtil.toJSONString(resourceInstance.getMeta())) +
                (StringUtils.isEmpty(resourceInstance.getWorkspaceId()) ? "" : resourceInstance.getWorkspaceId()) +
                (StringUtils.isEmpty(resourceInstance.getOrganizationId()) ? "" : resourceInstance.getOrganizationId());
    }

    private boolean diffResourceInstance(ResourceInstance cloudResourceInstance, ResourceInstance localResourceInstance) {
        return StringUtils.equals(getDiffId(cloudResourceInstance), getDiffId(localResourceInstance));
    }

}
