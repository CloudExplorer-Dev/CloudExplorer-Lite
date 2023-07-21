package com.fit2cloud.provider.impl.proxmox.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.proxmox.ProxmoxCloudProvider;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/11  18:36}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ProxmoxNetworkForm {
    @Form(inputType = InputType.BaseArrayContainer,
            type = "component",
            clazz = ProxmoxCloudProvider.class,
            defaultValue = "[{}]",
            defaultJsonValue = true,
            propsInfo = "{\"addLabel\":\"添加网卡\"}",
            method = "getNetworkAdapterForm")
    private List<NetworkAdapter> adapters;

    @Form(inputType = InputType.Text, label = "dns域", required = false, propsInfo = "{\"elFormItemStyle\":{\"width\":\"50%\",\"float\":\"left\"},\"style\":{\"width\":\"90%\"}}")
    private String dnsDomain;

    @Form(inputType = InputType.Text, label = "dns服务器", required = false, propsInfo = "{\"rules\":[{\"message\":\"DNS服务器格式不正确\",\"trigger\":\"blur\",\"pattern\":\"^((25[0-5]|2[0-4]\\\\d|((1\\\\d{2})|([1-9]?\\\\d)))\\\\.){3}(25[0-5]|2[0-4]\\\\d|((1\\\\d{2})|([1-9]?\\\\d)))$\",\"required\":false}],\"elFormItemStyle\":{\"width\":\"50%\",\"float\":\"left\",\"margin-bottom\":\"18px\"},\"style\":{\"width\":\"90%\"}}")
    private String dnsServers;


    @Getter
    @Setter
    public static class NetworkAdapter {
        @Form(inputType = InputType.SingleSelect, label = "网络",
                textField = "iface", valueField = "iface",
                clazz = ProxmoxCloudProvider.class,
                method = "getNetworkList",
                propsInfo = "{\"elFormItemStyle\":{\"margin-bottom\":\"18px\"},\"rules\":[{\"message\":\"网络不能为空\",\"trigger\":\"change\",\"required\":true}]}")
        private String network;

        @Form(inputType = InputType.SingleSelect, label = "网络模型",
                clazz = ProxmoxCloudProvider.class,
                textField = "key", valueField = "value",
                method = "getModelList",
                propsInfo = "{\"elFormItemStyle\":{\"margin-bottom\":\"18px\"},\"rules\":[{\"message\":\"网络模型\",\"trigger\":\"change\",\"required\":true}]}")
        private String model;

        @Form(label = "防火墙",
                inputType = InputType.SwitchBtn,
                defaultJsonValue = true,
                defaultValue = "true",
                propsInfo = "{\"elFormItemStyle\":{\"margin-bottom\":\"18px\"},\"rules\":[{\"message\":\"防火墙\",\"trigger\":\"change\",\"required\":true,\"type\":\"boolean\"}]}")
        private boolean firewall;

        @Form(inputType = InputType.Radio, label = "IP分配类型", defaultValue = "dhcp",
                textField = "key", valueField = "value", method = "getDhcpType",
                clazz = ProxmoxCloudProvider.class, attrs = "{\"style\":\"width:100%\"}",
                propsInfo = "{\"elFormItemStyle\":{\"margin-bottom\":\"18px\"}}"
        )
        private String ipv4dhcp;
        @Form(inputType = InputType.Text, label = "IPv4/CIDR", relationShows = {"ipv4dhcp"}, relationShowValues = "static"
                , propsInfo = "{\"elFormItemStyle\":{\"margin-bottom\":\"18px\"},\"rules\":[{\"message\":\"IPv4/CIDR不能为空\",\"trigger\":\"blur\",\"required\":true},{\"message\":\"IPv4/CIDR格式不正确\",\"trigger\":\"blur\",\"pattern\":\"^(((25[0-5]|2[0-4]\\\\d|((1\\\\d{2})|([1-9]?\\\\d)))\\\\.){3}(25[0-5]|2[0-4]\\\\d|((1\\\\d{2})|([1-9]?\\\\d))))\\\\/(3[0-6]|[1-2]\\\\d|\\\\d)$\"}]}")
        private String ipv4cidr;

        @Form(inputType = InputType.Text, label = "网关", relationShows = {"ipv4dhcp"}, relationShowValues = "static",
                propsInfo = "{\"elFormItemStyle\":{\"margin-bottom\":\"18px\"},\"rules\":[{\"message\":\"网关不能为空\",\"trigger\":\"blur\",\"required\":true},{\"message\":\"网关格式不正确\",\"trigger\":\"blur\",\"pattern\":\"^((25[0-5]|2[0-4]\\\\d|((1\\\\d{2})|([1-9]?\\\\d)))\\\\.){3}(25[0-5]|2[0-4]\\\\d|((1\\\\d{2})|([1-9]?\\\\d)))$\"}]}")
        private String ipv4gateway;


        public String toIpConfigString() {
            if (StringUtils.equals(ipv4dhcp, "dhcp")) {
                return URLEncoder.encode("ip=dhcp", StandardCharsets.UTF_8);
            } else {
                return URLEncoder.encode("ip=" + ipv4cidr + ",gw=" + ipv4gateway, StandardCharsets.UTF_8);
            }
        }

        public String toNetString() {
            //virtio=B2:09:24:DD:D1:24,bridge=vmbr0
            //virtio,bridge=vmbr0,firewall=1
            return URLEncoder.encode("virtio,bridge=" + network + (firewall ? ",firewall=1" : ""), StandardCharsets.UTF_8);
        }
    }
}

