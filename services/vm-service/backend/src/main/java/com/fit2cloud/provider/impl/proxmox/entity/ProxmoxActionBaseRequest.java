package com.fit2cloud.provider.impl.proxmox.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fit2cloud.common.provider.impl.proxmox.client.entity.InstanceId;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.impl.proxmox.entity.request.ProxmoxBaseRequest;
import lombok.Getter;


@Getter
public class ProxmoxActionBaseRequest extends ProxmoxBaseRequest {

    @JsonProperty("uuid")
    private InstanceId uuid;

    public void setInstanceUuid(String uuid) {
        this.uuid = JsonUtil.parseObject(uuid, InstanceId.class);
    }

    public void setUuid(String uuid) {
        this.uuid = JsonUtil.parseObject(uuid, InstanceId.class);
    }
}
