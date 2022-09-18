package com.fit2cloud.service;

import com.fit2cloud.autoconfigure.ServerInfo;
import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.dto.module.Module;
import com.fit2cloud.dto.job.JobModuleInfo;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommonService {

    @Resource
    private DiscoveryClient discoveryClient;
    @Resource
    private RedissonClient redissonClient;

    public List<Module> getModules() {
        List<Module> modules = new ArrayList<>();
        Set<String> ids = new HashSet<>(discoveryClient.getServices());
        //为了刚启动能拿到自己
        ids.add(ServerInfo.module);

        for (String id : ids) {
            if (StringUtils.equalsIgnoreCase("gateway", id)) {
                continue; //排除网关
            }
            Module module;
            if (StringUtils.equals(id, ServerInfo.module)) {
                module = new Module()
                        .setId(id)
                        .setName(ServerInfo.moduleInfo.getName()) //todo 国际化
                        .setBasePath(ServerInfo.moduleInfo.getBasePath())
                        .setIcon(ServerInfo.moduleInfo.getIcon())
                        .setOrder(ServerInfo.moduleInfo.getOrder());
            } else {
                ServiceInstance instance = discoveryClient.getInstances(id).get(0);
                module = new Module()
                        .setId(id)
                        .setName(instance.getMetadata().get("name_zh-cn")) //todo 国际化
                        .setBasePath(instance.getMetadata().get("basePath"))
                        .setIcon(instance.getMetadata().get("icon"))
                        .setOrder(Integer.parseInt(instance.getMetadata().get("order")));
            }
            modules.add(module);
        }

        return modules.stream().sorted(Comparator.comparing(Module::getOrder)).collect(Collectors.toList());
    }

    public List<JobModuleInfo> getModuleJobs() {
        Set<String> ids = new HashSet<>(discoveryClient.getServices());
        ids.add(ServerInfo.module);
        return ids.stream().map(this::getModuleJobByModuleId).filter(Objects::nonNull).toList();
    }


    /**
     * @param moduleId 模块id
     * @return 模块定时任务信息
     */
    public JobModuleInfo getModuleJobByModuleId(String moduleId) {
        if (StringUtils.equalsIgnoreCase("gateway", moduleId)) {
            return null;
        }
        if (StringUtils.equals(moduleId, ServerInfo.module)) {
            return JobSettingConfig.getModuleJobInfo();
        } else {
            return null;
        }
    }
}
