package com.fit2cloud.provider.impl.proxmox.entity.request;

import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.proxmox.ProxmoxCloudProvider;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/16  21:33}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class AddDiskForm extends ProxmoxBaseRequest {
    @Form(inputType = InputType.Radio, label = "总线/设备",
            defaultValue = "scsi",
            textField = "key", valueField = "value",
            method = "getDeviceList",
            
            clazz = ProxmoxCloudProvider.class, attrs = "{\"style\":\"width:100%\"}")
    private String device;


    @Form(inputType = InputType.TableRadio,
            label = "请选择存储器",
            
            clazz = ProxmoxCloudProvider.class,
            method = "getDataStoreList",
            textField = "storage",
            valueField = "storage",
            propsInfo = "{\"style\":{\"width\":\"100%\"},\"tableColumns\":[{\"property\":\"storage\",\"label\":\"名称\"},{\"property\":\"used_fraction\",\"label\":\"使用量\",\"min-width\":\"120px\",\"component\":\"ProgressTableItem\",\"attrs\":{\"unit\":\"GB\",\"percentage\":\"parseFloat(DecimalFormat.format(row['used_fraction']*100,2))\",\"total\":\"DecimalFormat.format(row['total']/1024/1024/1024,2)\",\"available\":\"DecimalFormat.format((row['total']-row['used'])/1024/1024/1024,2)\",\"used\":\"DecimalFormat.format(row['used']/1024/1024/1024,2)\",\"title\":\"row['storage']\",\"color\":[{\"color\":\"rgba(52, 199, 36, 1)\",\"percentage\":60},{\"color\":\"rgba(255, 136, 0, 1)\",\"percentage\":80},{\"color\":\"rgba(245, 74, 69, 1)\",\"percentage\":100}]}}]}"
    )
    private String datastore;

    @Form(inputType = InputType.Radio, label = "磁盘格式",
            defaultValue = "raw",
            textField = "key", valueField = "value",
            method = "getFormatList",
            relationTrigger = "datastore",
            
            clazz = ProxmoxCloudProvider.class, attrs = "{\"style\":\"width:100%\"}")
    public String format;

    @Form(inputType = InputType.Number,
            label = "磁盘大小",
            unit = "GB",
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":1024,\"step\":10}",
            confirmGroup = 1
    )
    private int size;

}
