package com.fit2cloud.provider.impl.proxmox.entity.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fit2cloud.common.form.annotaion.Form;
import com.fit2cloud.common.form.annotaion.FormConfirmInfo;
import com.fit2cloud.common.form.annotaion.FormGroupInfo;
import com.fit2cloud.common.form.annotaion.FormStepInfo;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.provider.impl.proxmox.ProxmoxCloudProvider;
import com.fit2cloud.provider.impl.proxmox.entity.DiskConfig;
import com.fit2cloud.vm.ICreateServerRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/7  10:22}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@FormStepInfo(step = 1, name = "基础配置")
@FormStepInfo(step = 2, name = "网络配置")
@FormStepInfo(step = 3, name = "系统配置")
@FormConfirmInfo(group = 0, name = "云账号")
@FormConfirmInfo(group = 1, name = "基础配置")
@FormConfirmInfo(group = 2, name = "网络配置")
@FormConfirmInfo(group = 3, name = "系统配置")
@FormGroupInfo(group = 10, name = "付费方式")
@FormGroupInfo(group = 20, name = "区域")
@FormGroupInfo(group = 30, name = "操作系统")
@FormGroupInfo(group = 40, name = "实例规格")
@FormGroupInfo(group = 50, name = "磁盘配置")
@FormGroupInfo(group = 60, name = "存储器")
@FormGroupInfo(group = 70, name = "网络")
//@FormGroupInfo(group = 70, name = "公网IP", description = "可以公网访问的公网IP")
@FormGroupInfo(group = 80, name = "登录凭证")
@FormGroupInfo(group = 90, name = "主机命名")
public class ProxmoxCreateServerRequest extends ProxmoxBaseRequest implements ICreateServerRequest {
    /**
     * 唯一标识
     */
    private String id;
    /**
     * 索引
     */
    private int index;


    @Form(inputType = InputType.Radio,
            label = "付费方式",
            clazz = ProxmoxCloudProvider.class,
            method = "getChargeType",
            textField = "key",
            valueField = "value",

            defaultValue = "PostPaid",
            step = 1,
            group = 10,
            confirmGroup = 1
    )
    private String instanceChargeType;

    @Form(inputType = InputType.LineNumber,
            label = "购买数量",
            unit = "台",
            defaultValue = "1",
            defaultJsonValue = true,
            attrs = "{\"min\":1,\"max\":10,\"step\":1}",
            confirmGroup = 1
    )
    private int count;

    @Form(inputType = InputType.SingleSelect,
            label = "时长",
            clazz = ProxmoxCloudProvider.class,

            method = "getPeriodOption",
            textField = "periodDisplayName",
            valueField = "period",
            defaultValue = "1",
            defaultJsonValue = true,
            relationShows = "billingMode",
            relationShowValues = "prePaid",
            confirmGroup = 1
    )
    private String periodNum;

    @Form(inputType = InputType.TableRadio,
            label = "节点",

            clazz = ProxmoxCloudProvider.class,
            method = "getNodes",
            textField = "node",
            valueField = "node",
            propsInfo = "{\"activeTextEval\":\"`当前选择节点 ${row['node']}`\",\"style\":{\"width\":\"100%\",\"height\":\"300px\"},\"tableColumns\":[{\"property\":\"node\",\"label\":\"节点\"},{\"property\":\"cpu\",\"label\":\"CPU 使用率\",\"component\":\"el-progress\",\"attrs\":{\"percentage\":\"parseFloat(DecimalFormat.format(row['cpu']*100,2))\",\"color\":[{\"color\":\"rgba(52, 199, 36, 1)\",\"percentage\":60},{\"color\":\"rgba(255, 136, 0, 1)\",\"percentage\":80},{\"color\":\"rgba(245, 74, 69, 1)\",\"percentage\":100}]}},{\"property\":\"mem\",\"label\":\"内存使用率\",\"min-width\":\"120px\",\"component\":\"ProgressTableItem\",\"attrs\":{\"unit\":\"GB\",\"percentage\":\"parseFloat(DecimalFormat.format(row['mem']/row['maxmem']*100,2))\",\"total\":\"DecimalFormat.format(row['maxmem']/1024/1024/1024,2)\",\"available\":\"DecimalFormat.format((row['maxmem']-row['mem'])/1024/1024/1024,2)\",\"used\":\"DecimalFormat.format(row['mem']/row['maxmem']*100,2)\",\"title\":\"row['node']\",\"color\":[{\"color\":\"rgba(52, 199, 36, 1)\",\"percentage\":60},{\"color\":\"rgba(255, 136, 0, 1)\",\"percentage\":80},{\"color\":\"rgba(245, 74, 69, 1)\",\"percentage\":100}]}}]}",
            step = 1,
            group = 20,
            confirmGroup = 0
    )
    private String regionId;


    //模板
    @Form(inputType = InputType.SingleSelect,
            label = "模板",

            clazz = ProxmoxCloudProvider.class,
            method = "listVmCloudImage",
            textField = "imageName",
            valueField = "imageId",
            relationTrigger = "regionId",
            propsInfo = "{\"style\":{\"width\":\"80%\"}}",
            attrs = "{\"placeholder\":\"请选择一个模板\"}",
            group = 30,
            step = 1,
            confirmGroup = 1
    )
    private String template;

    //cpu核数
    @Form(inputType = InputType.Number,
            label = "CPU Sockets",
            leftLabel = true,
            unit = "个",
            group = 40,
            step = 1,
            defaultValue = "1",
            defaultJsonValue = true,
            propsInfo = "{\"labelStyle\":{\"width\":\"100px\"}}",
            attrs = "{\"min\":1,\"max\":128,\"step\":1}",
            confirmGroup = 1
    )
    private int cpuSlot;


    //内存GB
    @Form(inputType = InputType.Number,
            label = "CPU Cores",
            leftLabel = true,
            unit = "核",
            group = 40,
            step = 1,
            defaultValue = "1",
            defaultJsonValue = true,
            propsInfo = "{\"labelStyle\":{\"width\":\"100px\"}}",
            attrs = "{\"min\":1,\"max\":512,\"step\":1}",
            confirmGroup = 1
    )
    private int cpu;

    //内存GB
    @Form(inputType = InputType.Number,
            label = "内存",
            leftLabel = true,
            unit = "GB",
            group = 40,
            step = 1,
            defaultValue = "1",
            defaultJsonValue = true,
            propsInfo = "{\"labelStyle\":{\"width\":\"100px\"}}",
            attrs = "{\"min\":1,\"max\":512,\"step\":1}",
            confirmGroup = 1
    )
    private int mem;

    @Form(inputType = InputType.DiskConfig,
            step = 1,
            group = 50,
            defaultValue = "[]",
            defaultJsonValue = true,
            relationTrigger = "template",
            confirmGroup = 1,
            confirmSpecial = true,
            propsInfo = "{\"mappingEval\":\"row&&row['diskInfos']?JSON.parse(row['diskInfos'])['disks'].map((item,index)=>({size:item.size,boot:item.boot,unit:item.unit,template:true,minValue:item.size,readonly:true,key:item.key})).sort((pre,next)=>next.boot-pre.boot):[]\",\"defaultDisk\":{\"size\":20,\"boot\":false,\"unit\":\"GB\",\"template\":false,\"minValue\":1}}"
    )
    private List<DiskConfig> disks;

    @Form(inputType = InputType.TableRadio,
            label = "存储器",
            clazz = ProxmoxCloudProvider.class,
            method = "getDataStoreList",
            relationTrigger = "regionId",
            textField = "storage",
            valueField = "storage",
            propsInfo = "{\"activeTextEval\":\"`当前选择存储器 ${row['storage']}`\",\"style\":{\"width\":\"100%\",\"height\":\"300px\"},\"tableColumns\":[{\"property\":\"storage\",\"label\":\"名称\"},{\"property\":\"used_fraction\",\"label\":\"使用量\",\"min-width\":\"120px\",\"component\":\"ProgressTableItem\",\"attrs\":{\"unit\":\"GB\",\"percentage\":\"parseFloat(DecimalFormat.format(row['used_fraction']*100,2))\",\"total\":\"DecimalFormat.format(row['total']/1024/1024/1024,2)\",\"available\":\"DecimalFormat.format((row['total']-row['used'])/1024/1024/1024,2)\",\"used\":\"DecimalFormat.format(row['used']/1024/1024/1024,2)\",\"title\":\"row['storage']\",\"color\":[{\"color\":\"rgba(52, 199, 36, 1)\",\"percentage\":60},{\"color\":\"rgba(255, 136, 0, 1)\",\"percentage\":80},{\"color\":\"rgba(245, 74, 69, 1)\",\"percentage\":100}]}}]}",
            step = 1,
            group = 60,
            confirmGroup = 0
    )
    private String datastore;

    @Form(inputType = InputType.BaseCountTabContainer, label = "网络",

            type = "component",
            clazz = ProxmoxCloudProvider.class,
            defaultValue = "[{}]",
            defaultJsonValue = true,
            method = "getNetworkForm",
            step = 2,
            group = 70,
            confirmGroup = 2,
            confirmSpecial = true,
            confirmPosition = Form.Position.TOP)
    private List<ProxmoxNetworkForm> networkList;

    @Form(inputType = InputType.BaseObjectContainer,

            type = "component",
            clazz = ProxmoxCloudProvider.class,
            defaultValue = "{}",
            defaultJsonValue = true,
            method = "getAuthObjForm",
            step = 3,
            group = 80,
            confirmGroup = 3,
            confirmSpecial = true,
            confirmPosition = Form.Position.TOP,
            propsInfo = "{\"style\":\"width:100%\"}")
    private AuthObj authObj;

    @Form(inputType = InputType.BaseCountContainer,

            type = "component",
            clazz = ProxmoxCloudProvider.class,
            defaultValue = "[{}]",
            defaultJsonValue = true,
            method = "getHostObjForm",
            attrs = "{\"label-width\":0}",
            propsInfo = "{\"formHostTitleStyle\":{\"margin-bottom\":\"8px\"},\"formStyle\":{\"width\":\"100%\",\"background\":\"#f7f9fc\",\"border-radius\":\"4px\",\"display\":\"flex\",\"align-items\":\"center\",\"height\":\"46px\",\"padding-left\":\"12px\",\"padding-right\":\"12px\",\"margin-bottom\":\"6px\",\"justify-content\":\"center\"}}",
            step = 3,
            group = 90,
            confirmGroup = 3,
            confirmSpecial = true,
            confirmPosition = Form.Position.TOP)
    private List<HostObj> hostObj;

    @Form(inputType = InputType.LabelText,
            label = "配置费用",
            clazz = ProxmoxCloudProvider.class,
            method = "calculateConfigPrice",
            relationTrigger = {"count", "periodNum", "cpuSlot", "cpu", "mem", "disks"},
            attrs = "{\"style\":\"color: red; font-size: large\"}",
            confirmGroup = 1,
            footerLocation = 1,
            confirmSpecial = true,
            required = false
    )
    private String configPrice;

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String getRegionId() {
        return regionId;
    }


    @Override
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Getter
    @Setter
    public static class AuthObj {
        @Form(inputType = InputType.Radio, label = "登录方式", defaultValue = "custom",
                textField = "key", valueField = "value", method = "getAuthTypeList",
                clazz = ProxmoxCloudProvider.class, attrs = "{\"style\":\"width:100%\"}")
        private String type;

        @Form(inputType = InputType.Text, label = "登录名", relationShows = {"type"}, relationShowValues = "custom",
                propsInfo = "{\"rules\":[{\"message\":\"用户名不能为空\",\"trigger\":\"blur\",\"required\":true},{\"message\":\"用户名只能为大小写字母\",\"trigger\":\"blur\",\"pattern\":\"^[a-zA-z]+$\"}]}")
        private String userName;

        @Form(inputType = InputType.Password, label = "登录密码", relationShows = {"type"}, relationShowValues = "custom",
                propsInfo = "{\"rules\":[{\"message\":\"密码不能为空\",\"trigger\":\"blur\",\"required\":true},{\"message\":\"密码格式必须符合: 字符个数大于等于8，包含英文大小写、数字及特殊字符 ~!@#$%^&*()_+=?.\",\"trigger\":\"blur\",\"pattern\":\"^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z~!@#$%^&*()_+=?]+$)(?![a-z0-9]+$)(?![a-z~!@#$%^&*()_+=?]+$)(?![0-9~!@#$%^&*()_+=?]+$)(?![a-zA-z0-9]+$)(?![a-z0-9~!@#$%^&*()_+=?]+$)(?![A-Z0-9~!@#$%^&*()_+=?]+$)[a-zA-Z0-9~!@#$%^&*()_+=?]{8,30}$\"}]}")
        private String password;

    }

    @Getter
    @Setter
    public static class HostObj {

        @Form(inputType = InputType.LabelTextInput, attrs = "{\"label\":\"云主机名称\"}", propsInfo = "{\"rules\":[{\"message\":\"主机名称不能为空\",\"trigger\":\"blur\",\"required\":true},{\"message\":\"主机名称格式不正确\",\"trigger\":\"blur\",\"pattern\":\"^(?:(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9\\\\\\\\-]*[a-zA-Z0-9])?)\\\\\\\\.)*(?:[A-Za-z0-9](?:[A-Za-z0-9\\\\\\\\-]*[A-Za-z0-9])?))$\"}],\"elFormItemStyle\":{\"width\":\"50%\",\"height\":\"48px\"},\"style\":{\"width\":\"90%\"}}")
        private String name;

        @Form(inputType = InputType.LabelTextInput, required = false, attrs = "{\"label\":\"云主机备注\"}", propsInfo = "{\"elFormItemStyle\":{\"width\":\"50%\",\"height\":\"48px\"},\"style\":{\"width\":\"90%\"}}")
        private String notes;
    }

}
