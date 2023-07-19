package com.fit2cloud.common.provider.impl.proxmox.client.entity.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/14  19:17}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class NetworkInterface {
    private String name;
    @JsonProperty("ip-addresses")
    private List<IpAddresses> ipAddresses;

    @JsonProperty("hardware-address")
    private String hardwareAddress;

    @Getter
    @Setter
    public static class IpAddresses {
        @JsonProperty("ip-address")
        private String ipAddress;

        private int prefix;

        @JsonProperty("ip-address-type")
        private String ipAddressType;

    }
}
