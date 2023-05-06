package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fit2cloud.base.entity.Workspace;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: LiuDi
 * Date: 2022/9/5 5:29 PM
 */
@Service
@Lazy
public class WorkspaceCommonService {

    @Resource
    BaseMapper<Workspace> workspaceBaseMapper;

    public List<String> getWorkspaceIdsByOrgIds(List<String> orgIds) {
        if (CollectionUtils.isEmpty(orgIds)) {
            return new ArrayList<>();
        }

        QueryWrapper<Workspace> wrapper = Wrappers.query();
        wrapper.lambda().in(Workspace::getOrganizationId, orgIds);

        return workspaceBaseMapper.selectList(wrapper).stream().map(Workspace::getId).collect(Collectors.toList());
    }
}
