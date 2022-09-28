package com.fit2cloud.common.provider.impl.vsphere.entity.credential;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.annotaion.From;
import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.platform.credential.Credential;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/9/16 2:22 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VsphereCredential implements Credential {

    private static Logger logger = LoggerFactory.getLogger(VsphereCredential.class);

    /**
     * 用户名
     */
    @From(inputType = InputType.text, label = "用户名")
    @JsonProperty("vUserName")
    private String vUserName;
    /**
     * 密码
     */
    @From(inputType = InputType.password, label = "密码")
    @JsonProperty("vPassword")
    private String vPassword;
    /**
     * vCenter服务器IP
     */
    @From(inputType = InputType.text, label = "vCenter服务器IP")
    @JsonProperty("vCenterIp")
    private String vCenterIp;
    /**
     * 是否启用内容库镜像
     */
    @From(inputType = InputType.switchBtn, label = "是否启用内容", defaultValue = "false", required = false)
    private boolean useContentLibrary = false;

    @Override
    public boolean verification() {
        ServiceInstance serviceInstance = null;
        try {
            serviceInstance = initServiceInstance();
            return true;
        } catch (Exception e) {
            logger.error("Cloud Account Verification failed!" + e.getMessage(), e);
            throw new Fit2cloudException(100001, "云账号校验失败!");
        } finally {
            closeConnection(serviceInstance);
        }
    }

    @Override
    public List<Region> regions() {
        ServiceInstance serviceInstance = null;
        try {
            serviceInstance = initServiceInstance();
            ManagedEntity rootEntity = serviceInstance.getRootFolder();
            ManagedEntity[] mes = new InventoryNavigator(rootEntity).searchManagedEntities(Datacenter.class.getSimpleName());
            List<Datacenter> datacenters = new ArrayList<>();
            if (mes != null) {
                for (ManagedEntity m : mes) {
                    datacenters.add((Datacenter) m);
                }
            }
            return datacenters.stream().map(this::toRegion).toList();
        } catch (Exception e) {
            logger.error("Failed to Get Regions!" + e.getMessage(), e);
            throw new Fit2cloudException(100001, "云账号校验失败!");
        } finally {
            closeConnection(serviceInstance);
        }
    }

    private Region toRegion(Datacenter datacenter) {
        Region region = new Region();
        region.setRegionId(datacenter.getName());
        region.setName(datacenter.getName());
        return region;
    }

    /**
     * 初始化 vsphere 连接实例
     *
     * @return
     */
    private ServiceInstance initServiceInstance() {
        try {
            return new ServiceInstance(new URL("https://" + vCenterIp + "/sdk"), vUserName, vPassword, true, 60000, 12000);
        } catch (RemoteException e) {
            if (e.toString().contains("InvalidLogin")) {
                throw new RuntimeException("Account verification failed!" + e.getMessage(), e);
            }
            throw new RuntimeException("Failed to connect to Vsphere server!" + e.getMessage(), e);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Vsphere server address error!", e);
        }
    }

    /**
     * 关闭 vsphere 连接
     */
    private void closeConnection(ServiceInstance serviceInstance) {
        if (serviceInstance != null) {
            serviceInstance.getServerConnection().logout();
        }
    }
}
