package com.fit2cloud.autoconfigure;

import com.fit2cloud.dto.module.ModuleInfo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
@Component
public class ServerInfo {

    @Getter
    public static String module;

    @Getter
    public static ModuleInfo moduleInfo;

    @Resource
    private EurekaInstanceConfigBean eurekaInstanceConfigBean;

    @Value("${spring.application.name}")
    public void setModuleName(String name) {
        ServerInfo.module = name;
    }

    @PostConstruct
    public void setModuleInfo() {
        ServerInfo.moduleInfo = new ModuleInfo()
                .setName(eurekaInstanceConfigBean.getMetadataMap().get("name_zh-cn"))
                .setNameZhTw(eurekaInstanceConfigBean.getMetadataMap().get("name_zh-cn"))
                .setNameEn(eurekaInstanceConfigBean.getMetadataMap().get("name_en"))
                .setBasePath(eurekaInstanceConfigBean.getMetadataMap().get("basePath"))
                .setIcon(eurekaInstanceConfigBean.getMetadataMap().get("icon"))
                .setOrder(Integer.parseInt(eurekaInstanceConfigBean.getMetadataMap().get("order")));
    }


}
