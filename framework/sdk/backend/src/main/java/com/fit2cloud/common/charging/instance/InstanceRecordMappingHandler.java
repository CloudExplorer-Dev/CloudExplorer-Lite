package com.fit2cloud.common.charging.instance;

import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.entity.Workspace;
import com.fit2cloud.common.charging.entity.InstanceRecord;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/26  14:00}
 * {@code @Version 1.0}
 * {@code @注释: 实例转换器 获取全量的资源实例 -> 应该从原始数据表里面查询后映射过来 }
 */
public interface InstanceRecordMappingHandler {
    /**
     * 获取资源实例列表
     *
     * @return 资源实例列表
     */
    List<InstanceRecord> mapping();

    default void setAuthData(List<Workspace> workspaceList, List<Organization> organizationList, InstanceRecord instanceRecord, String sourceId) {
        workspaceList.stream().filter(workspace -> StringUtils.equals(workspace.getId(), sourceId))
                .findFirst().ifPresent(workspace -> {
                    instanceRecord.setWorkspaceId(workspace.getId());
                    instanceRecord.setWorkspaceName(workspace.getName());
                    instanceRecord.setOrganizationId(workspace.getOrganizationId());
                });
        organizationList
                .stream()
                .filter(organization ->
                        StringUtils.equals(organization.getId(), StringUtils.isEmpty(instanceRecord.getOrganizationId()) ? sourceId : instanceRecord.getOrganizationId()))
                .findFirst()
                .ifPresent(organization -> {
                    instanceRecord.setOrganizationId(organization.getId());
                    instanceRecord.setOrganizationName(organization.getName());
                });
    }
}
