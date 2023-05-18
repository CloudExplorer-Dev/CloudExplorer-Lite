package com.fit2cloud.provider.impl.huawei.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 操作信息
 * 带的镜像为随机
 *
 * @author jianneng
 * @date 2022/11/21 15:29
 **/
@Data
@Accessors(chain = true)
public class OsConfig {
    private String os;
    private String osVersion;
    private String imageId;
    private String imageName;
    private Long imageDiskMinSize;
}
