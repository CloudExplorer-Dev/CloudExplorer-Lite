package com.fit2cloud.provider.impl.openstack.util;

import com.fit2cloud.common.provider.impl.openstack.utils.OpenStackBaseUtils;
import com.fit2cloud.provider.constants.F2CDiskStatus;
import com.fit2cloud.provider.constants.F2CInstanceStatus;
import com.fit2cloud.provider.entity.*;
import com.fit2cloud.provider.impl.openstack.entity.CheckStatusResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.compute.Address;
import org.openstack4j.model.compute.HostAggregate;
import org.openstack4j.model.compute.SecurityGroup;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.openstack4j.model.identity.v3.Role;
import org.openstack4j.model.identity.v3.Service;
import org.openstack4j.model.image.v2.Image;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeAttachment;
import org.openstack4j.openstack.storage.block.domain.VolumeBackendPool;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class OpenStackUtils extends OpenStackBaseUtils {

    private static final int MAX_COUNT = 300;
    private static final int SLEEP_TIME = 10000;

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
        String instanceType = vm.getCpu() + "vCPU " + vm.getMemory() + "GB" + (instance.getFlavor().getDisk() == 0 ? "" : (" " + instance.getFlavor().getDisk() + "GB"));
        vm.setInstanceType(instance.getFlavor().getName())
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
        if (CollectionUtils.isNotEmpty(instance.getSecurityGroups())) {
            // TODO 安全组ID?
            List<String> names = instance.getSecurityGroups().stream().map(SecurityGroup::getName).collect(Collectors.toList());
            vm.setSecurityGroupIds(names);
        }
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

    public static CheckStatusResult checkServerStatus(OSClient.OSClientV3 osClient, Server server, Server.Status expect) {
        int count = 0;
        while (true) {
            try {
                Thread.sleep(SLEEP_TIME);
                Server s = osClient.compute().servers().get(server.getId());
                if (s != null && s.getStatus() != null) {
                    if (expect.equals(s.getStatus())) {
                        return CheckStatusResult.success(s);
                    }
                    if (Server.Status.ERROR.equals(s.getStatus())) {
                        return CheckStatusResult.fail("The server status is ERROR，message:"
                                + (s.getFault() == null ? "" : s.getFault().getMessage()), s);
                    }
                }
                count++;
                if (count >= MAX_COUNT) {
                    return CheckStatusResult.fail("check server status timeout! [" + MAX_COUNT * SLEEP_TIME + "]");
                }
            } catch (Exception e) {
                return CheckStatusResult.fail("Server: " + server.getId() + ", error: " + e.getMessage());
            }
        }
    }

    public static CheckStatusResult checkDiskStatus(OSClient.OSClientV3 osClient, Volume volume, Volume.Status... expect) {
        int count = 0;
        while (true) {
            try {
                Thread.sleep(SLEEP_TIME);
                Volume v = osClient.blockStorage().volumes().get(volume.getId());
                if (v != null && v.getStatus() != null) {
                    if (Arrays.stream(expect).toList().contains(v.getStatus())) {
                        return CheckStatusResult.success(v);
                    }
                    if (Volume.Status.ERROR.equals(v.getStatus())) {
                        return CheckStatusResult.fail("The volume status is ERROR");
                    }
                }
                count++;
                if (count >= MAX_COUNT) {
                    return CheckStatusResult.fail("check volume status timeout! [" + MAX_COUNT * SLEEP_TIME + "]");
                }
            } catch (Exception e) {
                return CheckStatusResult.fail("Volume: " + volume.getId() + ", error: " + e.getMessage());
            }
        }
    }


    public static boolean isAdmin(OSClient.OSClientV3 osClient) {
        boolean isAdmin = false;
        List<? extends Role> roles = osClient.getToken().getRoles();
        for (Role role : roles) {
            if ("admin".equals(role.getName())) {
                isAdmin = true;
            }
        }
        return isAdmin;
    }

    public static F2CHost toF2CHost(List<? extends HostAggregate> hostAggregates, Hypervisor hypervisor, String region) {
        F2CHost f2cHost = new F2CHost();
        f2cHost.setCpuMHzTotal((long) hypervisor.getVirtualCPU() * (hypervisor.getCpuAllocationRatio() == 0 ? 1 : hypervisor.getCpuAllocationRatio()) * 1000);
        //已分配
        f2cHost.setCpuMHzAllocated(hypervisor.getVirtualCPU());
        //已使用
        f2cHost.setCpuMHzUsed(hypervisor.getVirtualUsedCPU() * 1000L);
        f2cHost.setCpuMHzPerOneCore(1000);
        f2cHost.setNumCpuCores(hypervisor.getVirtualCPU() * (hypervisor.getCpuAllocationRatio() == 0 ? 1 : hypervisor.getCpuAllocationRatio()));
        f2cHost.setHostId(hypervisor.getId());
        f2cHost.setHostName(hypervisor.getHypervisorHostname());
        f2cHost.setHostIp(hypervisor.getHostIP());
        f2cHost.setMemoryAllocated(hypervisor.getLocalMemory());
        f2cHost.setMemoryTotal((long) hypervisor.getLocalMemory() * (hypervisor.getRamAllocationRatio() == 0 ? 1 : hypervisor.getRamAllocationRatio()));
        f2cHost.setMemoryUsed(hypervisor.getLocalMemoryUsed());
        f2cHost.setStatus("poweredOn");
        f2cHost.setVmRunning(hypervisor.getRunningVM());
        f2cHost.setVmCpuCores(hypervisor.getVirtualUsedCPU());
        f2cHost.setVmStopped(0);
        f2cHost.setVmTotal(hypervisor.getRunningVM());
        f2cHost.setHypervisorType(hypervisor.getType());
        f2cHost.setHypervisorVersion(String.valueOf(hypervisor.getVersion()));
        f2cHost.setDataCenterId(region);
        f2cHost.setRegion(region);
        //新建的availableZone hostAggregates是不为空的，默认是nova
        if (CollectionUtils.isNotEmpty(hostAggregates)) {
            for (HostAggregate hostAggregate : hostAggregates) {
                if (hostAggregate.getHosts().contains(hypervisor.getHypervisorHostname())) {
                    f2cHost.setClusterId(hostAggregate.getAvailabilityZone());
                    break;
                } else {
                    f2cHost.setClusterId("nova");
                }
            }
        } else {
            f2cHost.setClusterId("nova");
        }
        return f2cHost;
    }

    public static boolean isSupport(OSClient.OSClientV3 osClient, ServiceType serviceType) {
        List<? extends Service> services = osClient.getToken().getCatalog();
        for (Service service : services) {
            if (!CollectionUtils.isEmpty(service.getEndpoints())
                    && serviceType.getType().equalsIgnoreCase(service.getType())) {
                return true;
            }
        }
        return false;
    }

    public static F2CDatastore toF2CDatastore(VolumeBackendPool backendPool, String region) {
        F2CDatastore f2cDs = new F2CDatastore();
        //默认
        f2cDs.setClusterId("nova");
        f2cDs.setDataCenterName(region);
        f2cDs.setCapacity(backendPool.getCapabilities().getTotalCapacityGb());
        f2cDs.setDataStoreId(backendPool.getName());
        f2cDs.setDataStoreName(backendPool.getCapabilities().getVolumeBackendName());
        f2cDs.setFreeSpace(backendPool.getCapabilities().getFreeCapacityGb());
        //已分配
        f2cDs.setAllocatedSpace(backendPool.getCapabilities().getAllocatedcapacitygb());
        f2cDs.setType("storage_pool");
        f2cDs.setLastUpdate(new Date().getTime() / 1000);
        return f2cDs;
    }

    public static String getCloudInitUserData(String password) {
        String loginUser = "root";

        ClassPathResource classPathResource = new ClassPathResource("linux_create_user.sh");
        String cloudInitBootstrapUserData = getContentFromStream(classPathResource);

        cloudInitBootstrapUserData = cloudInitBootstrapUserData
                .replaceAll("USER_NAME", Matcher.quoteReplacement(loginUser))
                .replaceAll("USER_PASSWORD", Matcher.quoteReplacement(password))
                .replaceAll("GROUP_NAME", Matcher.quoteReplacement(loginUser));

        return Base64.encodeBase64String(cloudInitBootstrapUserData.getBytes(StandardCharsets.UTF_8));
    }

    private static String getContentFromStream(ClassPathResource classPathResource) {
        Reader reader = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new InputStreamReader(classPathResource.getInputStream());
            br = new BufferedReader(reader);

            String line = br.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException ignored) {
            }
        }

        return sb.toString();
    }
}
