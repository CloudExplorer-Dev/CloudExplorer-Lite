package com.fit2cloud.provider.impl.aliyun.entity;

import lombok.Data;

import java.util.List;

@Data
public class AliyunPriceModuleConfig {

    private String instanceTypeConfig;

    private String systemDiskConfig;

    private List<String> dataDiskConfigList;

    private String publicIpConfig;
}
