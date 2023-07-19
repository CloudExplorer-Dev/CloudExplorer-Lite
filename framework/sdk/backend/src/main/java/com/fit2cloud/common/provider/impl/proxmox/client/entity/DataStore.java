package com.fit2cloud.common.provider.impl.proxmox.client.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fit2cloud.common.provider.impl.proxmox.client.serializer.DataStorageFormatSerialize;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/11  16:31}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class DataStore {
    private Long avail;
    private Long shared;
    private Long total;
    private BigDecimal used_fraction;
    private int active;
    private String storage;
    private Long used;
    private String type;
    private String content;
    private int enabled;
    /**
     * 支持的格式
     */
    @JsonDeserialize(using = DataStorageFormatSerialize.class)
    @JsonProperty("format")
    private List<String> formats;
}
