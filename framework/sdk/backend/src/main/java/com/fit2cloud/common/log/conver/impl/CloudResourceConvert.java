package com.fit2cloud.common.log.conver.impl;

import com.fit2cloud.base.entity.*;
import com.fit2cloud.base.mapper.*;
import com.fit2cloud.common.log.cache.*;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.log.conver.ResourceConvert;
import com.fit2cloud.common.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 转换器
 * 缓存可能会导致无法获取变更后的名称
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/1  4:26 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class CloudResourceConvert implements ResourceConvert {
    @Override
    public String conver(String resourceId) {
        if(StringUtils.isEmpty(resourceId)){
            return "";
        }
        String[] resource = resourceId.split("@");
        if(resource.length>1){
            String id = resource[0];
            String name = "";
            ResourceTypeEnum resourceTypeEnum = ResourceTypeEnum.codeOf(resource[1]);
            switch (resourceTypeEnum){
                case CLOUD_DISK -> name = getVmCloudDiskName(id);
                case CLOUD_SERVER -> name = getVmCloudServerName(id);
                case CLOUD_ACCOUNT -> name = getCloudAccountName(id);
                case WORKSPACE -> name = getWorkspaceName(id);
                case ORGANIZATION -> name = getOrgName(id);
                case USER -> name = getUserName(id);
                case ROLE -> name = getRoleName(id);
            }
            return name;
        }
        return resourceId;
    }

    private String getVmCloudServerName(String resourceId){
        List<String> names = new ArrayList<>();
        List<String> ids = splitIds(resourceId);
        for(String id:ids){
            String cache = null;// BaseCloudServerCache.getCache(id);
            if (StringUtils.isEmpty(cache)) {
                BaseVmCloudServerMapper cloudServerMapper = SpringUtil.getBean(BaseVmCloudServerMapper.class);
                VmCloudServer cloudServer = cloudServerMapper.selectById(id);
                if (Objects.nonNull(cloudServer)) {
                    BaseCloudServerCache.updateCache();
                    names.add(cloudServer.getInstanceName());
                }
            } else {
                names.add(cache);
            }
        }
        return names.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private String getVmCloudDiskName(String resourceId){
        List<String> names = new ArrayList<>();
        List<String> ids = splitIds(resourceId);
        for(String id:ids){
            String cache = null;// BaseCloudDiskCache.getCache(id);
            if (StringUtils.isEmpty(cache)) {
                BaseVmCloudDiskMapper mapper = SpringUtil.getBean(BaseVmCloudDiskMapper.class);
                VmCloudDisk vo = mapper.selectById(id);
                if (Objects.nonNull(vo)) {
                    BaseCloudDiskCache.updateCache();
                    names.add(vo.getDiskName());
                }
            } else {
                names.add(cache);
            }
        }
        return names.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private String getCloudAccountName(String resourceId){
        List<String> names = new ArrayList<>();
        List<String> ids = splitIds(resourceId);
        for(String id:ids){
            String cache = null;// BaseCloudAccountCache.getCache(id);
            if (StringUtils.isEmpty(cache)) {
                BaseCloudAccountMapper mapper = SpringUtil.getBean(BaseCloudAccountMapper.class);
                CloudAccount vo = mapper.selectById(id);
                if (Objects.nonNull(vo)) {
                    BaseCloudAccountCache.updateCache();
                    names.add(vo.getName());
                }
            } else {
                names.add(cache);
            }
        }
        return names.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
    private String getOrgName(String resourceId){
        List<String> names = new ArrayList<>();
        List<String> ids = splitIds(resourceId);
        for(String id:ids){
            String cache = null;// BaseOrgCache.getCache(id);
            if (StringUtils.isEmpty(cache)) {
                BaseOrganizationMapper mapper = SpringUtil.getBean(BaseOrganizationMapper.class);
                Organization vo = mapper.selectById(id);
                if (Objects.nonNull(vo)) {
                    BaseOrgCache.updateCache();
                    names.add(vo.getName());
                }
            } else {
                names.add(cache);
            }
        }
        return names.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
    private String getWorkspaceName(String resourceId){
        List<String> names = new ArrayList<>();
        List<String> ids = splitIds(resourceId);
        for(String id:ids){
           String cache = null;// BaseWorkspaceCache.getCache(id);
           if (StringUtils.isEmpty(cache)) {
               BaseWorkspaceMapper mapper = SpringUtil.getBean(BaseWorkspaceMapper.class);
               Workspace vo = mapper.selectById(id);
               if (Objects.nonNull(vo)) {
                   BaseWorkspaceCache.updateCache();
                   names.add(vo.getName());
               }
           } else {
               names.add(cache);
           }
        }
        return names.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
    private String getRoleName(String resourceId){
        List<String> names = new ArrayList<>();
        List<String> ids = splitIds(resourceId);
        for(String id:ids){
            String cache = null;// BaseRoleCache.getCache(id);
            if (StringUtils.isEmpty(cache)) {
                BaseRoleMapper mapper = SpringUtil.getBean(BaseRoleMapper.class);
                Role vo = mapper.selectById(id);
                if (Objects.nonNull(vo)) {
                    BaseRoleCache.updateCache();
                    names.add(vo.getName());
                }
            } else {
                names.add(cache);
            }
        }
        return names.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
    private String getUserName(String resourceId){
        List<String> names = new ArrayList<>();
        List<String> ids = splitIds(resourceId);
        for(String id:ids){
            String cache = null;// BaseUserCache.getCache(id);
            if (StringUtils.isEmpty(cache)) {
                BaseUserMapper mapper = SpringUtil.getBean(BaseUserMapper.class);
                User vo = mapper.selectById(id);
                if (Objects.nonNull(vo)) {
                    BaseUserCache.updateCache();
                    names.add(vo.getName());
                }
            } else {
                names.add(cache);
            }
        }
        return names.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private List<String> splitIds(String id){
        return Arrays.asList(id.split(",")).stream().distinct().toList();
    }

}
