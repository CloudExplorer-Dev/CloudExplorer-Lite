package com.fit2cloud.provider.impl.vsphere.util;

import com.fit2cloud.common.constants.Language;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.provider.impl.vsphere.utils.VsphereClient;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Author: LiuDi
 * Date: 2022/9/22 11:06 AM
 */
public class VsphereVmClient extends VsphereClient {

    public VsphereVmClient(VsphereCredential credential, String regionId, Language lang) {
        super(credential.getVCenterIp(), credential.getVUserName(), credential.getVPassword(), regionId, Optional.ofNullable(lang).orElse(Language.zh_CN));
    }

    /**
     * 获取虚拟机
     *
     * @return 虚拟机列表
     */
    public List<VirtualMachine> listVirtualMachines() {
        List<VirtualMachine> list = listResources(VirtualMachine.class);
        List<VirtualMachine> vmList = new ArrayList<>();
        for (VirtualMachine vm : list) {
            VirtualMachineConfigInfo cfg = vm.getConfig();
            if (cfg == null || !cfg.isTemplate()) {
                vmList.add(vm);
            }
        }
        return vmList;
    }

    /**
     * 获取镜像
     *
     * @return 镜像列表
     */
    public List<VirtualMachine> listTemplates() {
        List<VirtualMachine> list = listResources(VirtualMachine.class);
        List<VirtualMachine> templateList = new ArrayList<>();
        for (VirtualMachine vm : list) {
            VirtualMachineConfigInfo cfg = vm.getConfig();
            if (cfg != null && cfg.isTemplate()) {
                templateList.add(vm);
            }
        }
        return templateList;
    }

    /**
     * 获取虚拟机创建时间
     *
     * @param mor
     * @param runtime
     * @return
     */
    public Date getVmCreateDate(ManagedObjectReference mor, VirtualMachineRuntimeInfo runtime) {
        try {
            // reference: http://asvignesh.in/how-to-get-a-virtual-machine-created-time/
            final EventFilterSpec eventFilterSpec = new EventFilterSpec();
            eventFilterSpec.setEventTypeId(new String[]{"VmCreatedEvent", "VmBeingDeployedEvent", "VmRegisteredEvent", "VmClonedEvent"});

            EventFilterSpecByEntity entity = new EventFilterSpecByEntity();
            entity.setEntity(mor);
            EventFilterSpecRecursionOption recOption = EventFilterSpecRecursionOption.self;
            entity.setRecursion(recOption);

            eventFilterSpec.setEntity(entity);
            final Event[] events = getSi().getEventManager().queryEvents(eventFilterSpec);
            if (events != null) {
                for (Event event : events) {
                    Calendar calendar = event.createdTime;
                    if (calendar == null) {
                        continue;
                    } else {
                        return calendar.getTime();
                    }
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        try {
            if (runtime != null) {
                Calendar bootTime = runtime.getBootTime();
                if (bootTime != null) {
                    return bootTime.getTime();
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        return null;
    }

    public VirtualMachine getTemplateFromAll(String name) {
        return getResourceFromAll(VirtualMachine.class, name);
    }

    /**
     * 获取虚拟机具有的磁盘
     *
     * @param vm 虚拟机实例
     * @return 虚拟机的磁盘列表
     */
    public List<VirtualDisk> getVirtualDisks(VirtualMachine vm) {
        List<VirtualDisk> disks = new ArrayList<>();
        VirtualMachineConfigInfo config = vm.getConfig();
        if (config != null) {
            VirtualHardware hardware = config.getHardware();
            VirtualDevice[] devices = hardware.getDevice();
            if (devices != null && devices.length > 0) {
                for (VirtualDevice device : devices) {
                    if (device instanceof VirtualDisk) {
                        disks.add((VirtualDisk) device);
                    }
                }
            }
        }
        return disks;
    }

    /**
     * 关闭电源
     * @param uuid
     * @return
     */
    public boolean powerOff(String uuid){
        VirtualMachine virtualMachine = getVirtualMachineByUuId(uuid);
        //电源已是关闭状态
        if(StringUtils.equalsIgnoreCase(VirtualMachinePowerState.poweredOff.name(),virtualMachine.getRuntime().getPowerState().name())){
            throw new RuntimeException("The current state of the virtual machine is power off!");
        }
        try {
            Task task = virtualMachine.powerOffVM_Task();
            String result = task.waitForTask();
            if(!StringUtils.equalsIgnoreCase(TaskInfoState.success.name(),result)){
                throw new RuntimeException("TaskInfo - "+task.getTaskInfo());
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 开机
     * @param uuid
     * @return
     */
    public boolean powerOn(String uuid){
        VirtualMachine virtualMachine = getVirtualMachineByUuId(uuid);
        if(StringUtils.equalsIgnoreCase(VirtualMachinePowerState.poweredOn.name(),virtualMachine.getRuntime().getPowerState().name())){
            throw new RuntimeException("The current state of the virtual machine is power on!");
        }
        try {
            HostSystem hostSystem = getHost(virtualMachine);
            Task task = virtualMachine.powerOnVM_Task(hostSystem);
            String result = task.waitForTask();
            if(!StringUtils.equalsIgnoreCase(TaskInfoState.success.name(),result)){
                throw new RuntimeException("TaskInfo - "+task.getTaskInfo());
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 关机
     * @param uuid
     * @return
     */
    public boolean shutdownInstance(String uuid){
        VirtualMachine virtualMachine = getVirtualMachineByUuId(uuid);
        if(StringUtils.equalsIgnoreCase(VirtualMachinePowerState.poweredOff.name(),virtualMachine.getRuntime().getPowerState().name())){
            throw new RuntimeException("The current state of the virtual machine is shutdown!");
        }
        try {
            virtualMachine.shutdownGuest();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 重启
     * @param uuid
     * @return
     */
    public boolean reboot(String uuid){
        VirtualMachine virtualMachine = getVirtualMachineByUuId(uuid);
        if(StringUtils.equalsIgnoreCase(VirtualMachinePowerState.poweredOff.name(),virtualMachine.getRuntime().getPowerState().name())){
            throw new RuntimeException("The current state of the virtual machine is shutdown!");
        }
        try {
            virtualMachine.rebootGuest();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 删除
     * @param uuid
     * @return
     */
    public boolean deleteInstance(String uuid){
        VirtualMachine virtualMachine = getVirtualMachineByUuId(uuid);
        if(StringUtils.equalsIgnoreCase(VirtualMachinePowerState.poweredOff.name(),virtualMachine.getRuntime().getPowerState().name())){
            throw new RuntimeException("The current state of the virtual machine is running!");
        }
        try {
            Task task = virtualMachine.destroy_Task();
            String result = task.waitForTask();
            if(!StringUtils.equalsIgnoreCase(TaskInfoState.success.name(),result)){
                throw new RuntimeException("TaskInfo - "+task.getTaskInfo());
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
