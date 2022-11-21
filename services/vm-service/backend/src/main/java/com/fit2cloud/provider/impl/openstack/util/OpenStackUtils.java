package com.fit2cloud.provider.impl.openstack.util;

import com.fit2cloud.common.provider.impl.openstack.utils.OpenStackBaseUtils;
import com.fit2cloud.provider.constants.F2CDiskStatus;
import com.fit2cloud.provider.constants.F2CInstanceStatus;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import org.apache.commons.lang3.StringUtils;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Address;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.image.v2.Image;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeAttachment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenStackUtils extends OpenStackBaseUtils {


    public static F2CVirtualMachine toF2CVirtualMachine(OSClient.OSClientV3 osClient, Server instance, String region) {
        return toF2CVirtualMachine(osClient, instance, region, null);
    }

    public static F2CVirtualMachine toF2CVirtualMachine(OSClient.OSClientV3 osClient, Server instance, String region, Map<String, Image> imageMap) {

        F2CVirtualMachine vm = new F2CVirtualMachine()
                .setRegion(region)
                .setZone(instance.getAvailabilityZone())
                .setInstanceId(instance.getId())
                .setInstanceUUID(instance.getId())
                .setName(instance.getName())
                .setCpu(instance.getFlavor().getVcpus())
                .setMemory(instance.getFlavor().getRam() / 1024);
        String instanceType = vm.getCpu() + "vCPU " + vm.getMemory() + "GB";
        vm.setInstanceType(instanceType)
                .setInstanceTypeDescription(instanceType)
                .setImageId(instance.getImageId())
                .setCreated(instance.getCreated())
                .setCreateTime(instance.getCreated().getTime())
                .setHostId(instance.getHostId())
                .setHost(instance.getHost())
                .setProjectId(instance.getTenantId())
                .setInstanceStatus(getStatus(instance.getStatus()).name());
        List<String> ipArray = new ArrayList<>();
        for (List<? extends Address> addresses : instance.getAddresses().getAddresses().values()) {
            for (Address address : addresses) {
                ipArray.add(address.getAddr());
                if (StringUtils.equals(address.getType(), "fixed")) {
                    if (address.getVersion() == 4) {
                        vm.setLocalIP(address.getAddr());
                    } else {
                        vm.setLocalIPV6(address.getAddr());
                    }
                } else {
                    if (address.getVersion() == 4) {
                        vm.setRemoteIP(address.getAddr());
                    } else {
                        vm.setRemoteIPV6(address.getAddr());
                    }
                }
            }
        }
        vm.setIpArray(ipArray);

        boolean bootFromVolume = StringUtils.isBlank(vm.getImageId());

        //判断是否为boot from image，是的话需要保存flavor的disk大小
        if (!bootFromVolume) {
            vm.setDisk(instance.getFlavor().getDisk());
        }

        //对于boot from volume的需要单独查image id
        if (bootFromVolume) {
            for (String attachedVolumeId : instance.getOsExtendedVolumesAttached()) {
                Volume volume = osClient.blockStorage().volumes().get(attachedVolumeId);
                if (!volume.bootable()) {
                    continue;
                }
                vm.setImageId(volume.getImageRef());

                break;
            }
        }
        Image image = null;
        if (StringUtils.isNotBlank(vm.getImageId())) {
            //简化查询
            if (imageMap != null) {
                image = imageMap.get(vm.getImageId());
            } else {
                image = osClient.imagesV2().get(vm.getImageId());
            }
        }

        vm.setOsInfo(getOsInfo(image));

        return vm;
    }

    public static F2CInstanceStatus getStatus(Server.Status status) {
        return switch (status) {
            case ACTIVE -> F2CInstanceStatus.Running;
            case BUILD, REBUILD -> F2CInstanceStatus.Creating;
            case RESIZE, VERIFY_RESIZE, REVERT_RESIZE -> F2CInstanceStatus.Resize;
            case REBOOT, HARD_REBOOT -> F2CInstanceStatus.Rebooting;
            case DELETED -> F2CInstanceStatus.Deleted;
            case STOPPED, SHUTOFF -> F2CInstanceStatus.Stopped;
            default -> F2CInstanceStatus.Unknown;
        };
    }

    private static String getOsInfo(Image image) {
        if (image != null) {
            String os = image.getOsDistro();
            String osVersion = image.getOsVersion();

            if (StringUtils.isNotBlank(os)) {
                return os + (StringUtils.isNotBlank(osVersion) ? (" " + osVersion) : "");
            }
        }
        return null;
    }

    public static F2CImage toF2CImage(Image image, String region) {
        return new F2CImage()
                .setId(image.getId())
                .setRegion(region)
                .setRegionName(region)
                .setName(image.getName())
                .setOs(getOsInfo(image))
                .setDescription(image.getSchema())
                .setDiskSize(image.getMinDisk())//这里获取支持创建的最小的盘大小
                .setCreated(image.getCreatedAt().getTime());
    }

    public static F2CDisk toF2CDisk(Volume volume, String region) {
        String device = null, serverId = null;
        for (VolumeAttachment attachment : volume.getAttachments()) {
            device = attachment.getDevice();
            serverId = attachment.getServerId();
        }
        return new F2CDisk()
                .setRegion(region)
                .setZone(volume.getZone())
                .setDiskId(volume.getId())
                .setSize(volume.getSize())
                .setDiskName(StringUtils.defaultIfBlank(volume.getName(), volume.getId()))
                .setDescription(volume.getDescription())
                .setDiskType(volume.getVolumeType())
                .setBootable(volume.bootable())
                .setImageId(volume.getImageRef())
                .setDevice(device)
                .setInstanceUuid(serverId)
                .setStatus(toF2CDiskStatus(volume.getStatus()))
                .setCreateTime(volume.getCreated().getTime());
    }

    public static String toF2CDiskStatus(Volume.Status status) {
        return switch (status) {
            case IN_USE -> F2CDiskStatus.IN_USE;
            case AVAILABLE -> F2CDiskStatus.AVAILABLE;
            case DELETING -> F2CDiskStatus.DELETING;
            case ATTACHING -> F2CDiskStatus.ATTACHING;
            case DETACHING -> F2CDiskStatus.DETACHING;
            case CREATING -> F2CDiskStatus.CREATING;
            case ERROR, ERROR_DELETING, ERROR_RESTORING -> F2CDiskStatus.ERROR;
            default -> F2CDiskStatus.UNKNOWN;
        };
    }
}
