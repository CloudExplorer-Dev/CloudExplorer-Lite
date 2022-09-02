package com.fit2cloud.service;

import com.fit2cloud.dto.module.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommonService {

    @Resource
    private DiscoveryClient discoveryClient;

    public List<Module> getModules() {
        List<Module> modules = new ArrayList<>();
        List<String> ids = discoveryClient.getServices();
        for (String id : ids) {
            if (StringUtils.equalsIgnoreCase("gateway", id)) {
                continue; //排除网关
            }
            ServiceInstance instance = discoveryClient.getInstances(id).get(0);
            Module module = new Module()
                    .setId(id)
                    .setName(instance.getMetadata().get("name_zh-cn")) //todo 国际化
                    .setBasePath(instance.getMetadata().get("basePath"))
                    .setIcon(instance.getMetadata().get("icon"))
                    .setOrder(Integer.parseInt(instance.getMetadata().get("order")));
            modules.add(module);
        }
        return modules;
    }

}
