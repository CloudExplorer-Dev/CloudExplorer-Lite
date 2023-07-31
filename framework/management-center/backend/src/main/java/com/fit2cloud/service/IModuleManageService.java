package com.fit2cloud.service;

import com.fit2cloud.dao.entity.ExtraModules;
import org.springframework.web.multipart.MultipartFile;

public interface IModuleManageService {

    ExtraModules list();

    ExtraModules list(String version);

    void install(String name, String version);

    void uninstall(String module);

    void start(String module);

    void stop(String module);

    void upload(MultipartFile file);

    void startGateway();

    void startExtras();

}
