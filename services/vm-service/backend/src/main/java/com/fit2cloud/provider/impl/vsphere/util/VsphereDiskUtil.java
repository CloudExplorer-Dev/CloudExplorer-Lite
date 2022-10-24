package com.fit2cloud.provider.impl.vsphere.util;

import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.impl.vsphere.entity.DiskOpsType;
import com.fit2cloud.provider.impl.vsphere.entity.VsphereDisk;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class VsphereDiskUtil {
    private static final long MB = 1024 * 1024;

    public static List<VirtualDisk> getVirtualMachineDisks(VirtualMachine virtualMachine) {
        VirtualDevice[] devices = virtualMachine.getConfig().getHardware().getDevice();
        List<VirtualDisk> disks = new ArrayList<VirtualDisk>();
        for (final VirtualDevice disk : devices) {
            if (disk instanceof VirtualDisk) {
                disks.add((VirtualDisk) disk);
            }
        }
        return disks;
    }

    public static VirtualDisk getVirtualMachineDisk(VirtualMachine virtualMachine, String diskId) {
        List<VirtualDisk> disks = getVirtualMachineDisks(virtualMachine);
        for (VirtualDisk disk : disks) {
            String virtualDiskId = getDiskId(virtualMachine, disk);
            if (virtualDiskId.equalsIgnoreCase(diskId)) {
                return disk;
            }
        }
        return null;
    }

    private static VirtualDiskFlatVer2BackingInfo getVirtualDiskFlatVer2BackingInfo(VsphereDisk disk) {
        VirtualDiskFlatVer2BackingInfo backingInfo = new VirtualDiskFlatVer2BackingInfo();
        backingInfo.setDiskMode(disk.getDiskMode() == null ? VirtualDiskMode.persistent.name() : disk.getDiskMode().toLowerCase());
        backingInfo.setThinProvisioned(disk.getDiskType() != null && disk.getDiskType().equalsIgnoreCase("thin"));
        backingInfo.setEagerlyScrub(disk.getDiskType() != null && disk.getDiskType().equalsIgnoreCase("THICK"));
        backingInfo.setFileName("");
        return backingInfo;
    }

    private static int findUnitNumber(List<VirtualDisk> disks, int controllerKey) {
        int unitNumber = -1;
        List<Integer> unitNumberList = new ArrayList<Integer>();
        for (final VirtualDisk disk : disks) {
            if (disk.getUnitNumber() != null && disk.getControllerKey().equals(controllerKey)) {
                unitNumberList.add(disk.getUnitNumber());
            }
        }
        for (int i = 0; i < 16; i++) {
            //7不可用，https://docs.vmware.com/cn/VMware-vSphere/6.5/rn/vsphere-esxi-vcenter-server-65-release-notes.html
            if (!unitNumberList.contains(i) && i != 7) {
                unitNumber = i;
                break;
            }
        }
        return unitNumber;
    }

    private static void setDeviceInfo(VirtualDisk virtualDisk, List<VirtualDisk> disks) throws Exception {
        int controllerKey = -1;
        int unitNumber = -1;
        for (final VirtualDisk disk : disks) {
            if ((unitNumber = findUnitNumber(disks, disk.getControllerKey())) != -1) {
                controllerKey = disk.getControllerKey();
                break;
            }
        }
        if (unitNumber == -1) {
            throw new Exception("can not find unitNumber");
        }

        VirtualDeviceConnectInfo virtualDeviceConnectInfo = new VirtualDeviceConnectInfo();
        virtualDeviceConnectInfo.setStartConnected(true);
        virtualDeviceConnectInfo.setConnected(true);
        virtualDeviceConnectInfo.setAllowGuestControl(false);
        virtualDisk.setKey(-1);
        virtualDisk.setConnectable(virtualDeviceConnectInfo);

        virtualDisk.setControllerKey(controllerKey);
        virtualDisk.setUnitNumber(unitNumber);
    }

    private static VirtualDisk findVirtualDisk(List<VirtualDisk> disks, VirtualMachine virtualMachine, String diskId) {
        for (VirtualDisk virtualDisk : disks) {
            if (diskId.equalsIgnoreCase(getDiskId(virtualMachine, virtualDisk))) {
                return virtualDisk;
            }
        }
        return null;
    }

    private static VirtualDeviceConfigSpec getVirtualMachineDiskConfigSpec(VirtualMachine virtualMachine, VsphereDisk disk, List<VirtualDisk> disks, VsphereDisk changedVsphereDisk) throws Exception {
        VirtualDisk virtualDisk = new VirtualDisk();
        if (!DiskOpsType.ADD.equals(disk.getDiskOpsType())) {
            if ((virtualDisk = findVirtualDisk(disks, virtualMachine, disk.getDiskId())) == null) {
                throw new Exception("Wrong parameter, no disk found to be edited:" + disk.getDiskId());
            }
        }
        virtualDisk.setCapacityInKB(disk.getSize());

        if (DiskOpsType.ADD.equals(disk.getDiskOpsType())) {
            VirtualDiskFlatVer2BackingInfo backingInfo = getVirtualDiskFlatVer2BackingInfo(disk);
            if (disk.getDatastore() != null) {
                backingInfo.setDatastore(disk.getDatastore().getMOR());
                //修复新增磁盘时，存储器设置无效问题：新增磁盘时，如果不设置fileName，则存储器选项不生效。
                backingInfo.setFileName("[" + changedVsphereDisk.getDatastoreName() + "]");
            }

            setDeviceInfo(virtualDisk, disks);
            virtualDisk.setBacking(backingInfo);
            disks.add(virtualDisk);
        }

        VirtualDeviceConfigSpec virtualDiskConfigSpec = new VirtualDeviceConfigSpec();
        virtualDiskConfigSpec.setFileOperation(DiskOpsType.ADD.equals(disk.getDiskOpsType()) ? VirtualDeviceConfigSpecFileOperation.create : null);
        virtualDiskConfigSpec.setOperation(DiskOpsType.ADD.equals(disk.getDiskOpsType()) ? VirtualDeviceConfigSpecOperation.add : VirtualDeviceConfigSpecOperation.edit);
        virtualDiskConfigSpec.setDevice(virtualDisk);
        return virtualDiskConfigSpec;
    }

    public static void createDiskForServer(VirtualMachine virtualMachine, List<VsphereDisk> disks) throws Exception {
        if (CollectionUtils.isEmpty(disks))
            return;
        List<VirtualDisk> virtualMachineDisks = VsphereDiskUtil.getVirtualMachineDisks(virtualMachine);
        VirtualMachineConfigSpec virtualMachineConfigSpec = new VirtualMachineConfigSpec();
        List<VirtualDeviceConfigSpec> virtualDeviceConfigSpecs = new ArrayList<VirtualDeviceConfigSpec>();

        for (VsphereDisk vsphereDisk : disks) {
            virtualDeviceConfigSpecs.add(getVirtualMachineDiskConfigSpec(virtualMachine, vsphereDisk, virtualMachineDisks, vsphereDisk));
        }

        virtualMachineConfigSpec.setDeviceChange(virtualDeviceConfigSpecs.toArray(new VirtualDeviceConfigSpec[0]));

        Task reconfigVMTask = virtualMachine.reconfigVM_Task(virtualMachineConfigSpec);
        String status = reconfigVMTask.waitForTask();
        if (!Task.SUCCESS.equals(status)) {
            throw new Exception(reconfigVMTask.getTaskInfo().getError().getLocalizedMessage());
        }
    }

    private static String getDiskId(VirtualMachine virtualMachine, VirtualDisk virtualDisk) {
        return virtualMachine.getMOR().getVal() + "-" + Integer.toString(virtualDisk.getKey());
    }

    public static List<VsphereDisk> getDiskChange(List<F2CDisk> disks) throws Exception {
        List<VsphereDisk> result = new ArrayList<VsphereDisk>();
        for (F2CDisk disk : disks) {
            if (disk.getDiskId() != null && disk.getNewSize() <= disk.getSize()) {
                continue;
            }
            VsphereDisk vsphereDisk = new VsphereDisk();
            vsphereDisk.setDatastoreName(disk.getDatastoreName());
            vsphereDisk.setSize(disk.getNewSize() * MB);
            if (disk.getDiskId() != null) {
                vsphereDisk.setDiskId(disk.getDiskId());
                vsphereDisk.setDiskOpsType(DiskOpsType.EXTEND);
            } else {
                vsphereDisk.setDiskOpsType(DiskOpsType.ADD);
                vsphereDisk.setDiskType(disk.getDiskType());
                vsphereDisk.setDiskMode(disk.getDiskMode());
            }
            result.add(vsphereDisk);
        }
        return result;
    }

    public static List<VsphereDisk> toVsphereDisk(List<F2CDisk> disks) throws Exception {
        List<VsphereDisk> result = new ArrayList<VsphereDisk>();
        for (F2CDisk disk : disks) {
            VsphereDisk vsphereDisk = new VsphereDisk();
            vsphereDisk.setDatastoreName(disk.getDatastoreName());
            vsphereDisk.setSize(disk.getSize() * MB);
            if (disk.getDiskId() != null) {
                vsphereDisk.setDiskId(disk.getDiskId());
                vsphereDisk.setDiskOpsType(DiskOpsType.EXTEND);
            } else {
                vsphereDisk.setDiskOpsType(DiskOpsType.ADD);
                vsphereDisk.setDiskType(disk.getDiskType());
                vsphereDisk.setDiskMode(disk.getDiskMode());
            }
            result.add(vsphereDisk);
        }
        return result;
    }
}
