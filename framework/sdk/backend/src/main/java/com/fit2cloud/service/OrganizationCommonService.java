package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fit2cloud.base.entity.Organization;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/9/5 4:49 PM
 */
@Service
public class OrganizationCommonService {

    @Resource
    BaseMapper<Organization> organizationMapper;

    public List<String> getOrgIdsByParentId(String orgId) {
        List<String> results = new ArrayList<>();
        results.add(orgId);

        QueryWrapper<Organization> wrapper = Wrappers.query();
        wrapper.lambda().isNotNull(Organization::getPid);

        List<Organization> organizations = organizationMapper.selectList(wrapper);
        getOrgIds(organizations, orgId, results);
        return results;
    }

    private void getOrgIds(List<Organization> organizations, String pid, List<String> results) {
        List<String> collect = organizations.stream().filter(org -> StringUtils.equals(pid, org.getPid())).map(org -> {
            String newPid = org.getId();
            getOrgIds(organizations, newPid, results);
            return newPid;
        }).toList();
        results.addAll(collect);
    }
}
