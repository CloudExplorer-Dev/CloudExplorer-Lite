package com.fit2cloud.provider.impl.proxmox.entity.request;

import com.fit2cloud.common.provider.impl.proxmox.client.entity.InstanceId;
import com.fit2cloud.common.utils.JsonUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/17  18:12}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class AddDiskFormRequest extends AddDiskForm {

    private InstanceId instanceUuid;

    public void setInstanceUuid(String instanceUuid) {
        this.instanceUuid = JsonUtil.parseObject(instanceUuid, InstanceId.class);
    }
}
