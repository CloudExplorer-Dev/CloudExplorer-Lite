package com.fit2cloud.service.impl;

import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.service.IPermissionService;
import com.fit2cloud.service.OrganizationCommonService;
import com.fit2cloud.service.WorkspaceCommonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author : LiuDi
 * @date : 2023/1/13 13:46
 */
@Service
public class PermissionServiceImpl implements IPermissionService {
    @Resource
    private OrganizationCommonService organizationCommonService;
    @Resource
    private WorkspaceCommonService workspaceCommonService;

    @Override
    public List<String> getSourceIds() {
        List<String> sourceIds = new ArrayList<>();
        // 普通用户
        if (CurrentUserUtils.isUser() && StringUtils.isNotBlank(CurrentUserUtils.getWorkspaceId())) {
            sourceIds = Collections.singletonList(CurrentUserUtils.getWorkspaceId());
        }
        // 组织管理员
        if (CurrentUserUtils.isOrgAdmin()) {
            List<String> orgWorkspaceList = new ArrayList<>();
            orgWorkspaceList.add(CurrentUserUtils.getOrganizationId());
            orgWorkspaceList.addAll(organizationCommonService.getOrgIdsByParentId(CurrentUserUtils.getOrganizationId()));
            orgWorkspaceList.addAll(workspaceCommonService.getWorkspaceIdsByOrgIds(orgWorkspaceList));
            sourceIds = orgWorkspaceList;
        }
        return sourceIds;
    }
}
