package com.fit2cloud.config;

import com.fit2cloud.service.IModuleManageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@DependsOn({"flyway", "flywayInitializer"})
public class ModuleManageConfig {


    @Resource
    private IModuleManageService moduleManageService;

    @Value("${ce.debug:false}")
    public void setModule(boolean debug) {
        if (debug) {
            return;
        }
        try {

            moduleManageService.startGateway();
            moduleManageService.startExtras();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
