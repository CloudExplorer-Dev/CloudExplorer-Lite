package com.fit2cloud.provider.impl.openstack.api;

import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.openstack.entity.CheckStatusResult;
import com.fit2cloud.provider.impl.openstack.entity.VolumeType;
import com.fit2cloud.provider.impl.openstack.entity.request.*;
import com.fit2cloud.provider.impl.openstack.util.OpenStackUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.RebootType;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.image.v2.Image;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.SecurityGroup;
import org.openstack4j.model.network.State;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.builder.VolumeBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class OpenStackCloudApi {


    public static List<F2CVirtualMachine> listVirtualMachine(OpenStackBaseRequest request) {
        List<F2CVirtualMachine> list = new ArrayList<>();
        try {
            OSClient.OSClientV3 osClient = request.getOSClient();

            List<String> regions = OpenStackUtils.getRegionList(osClient);

            for (String region : regions) {
                osClient.useRegion(region);
                List<? extends Server> instances = osClient.compute().servers().list(true);
                Map<String, Image> imageMap = osClient.imagesV2().list().stream().collect(Collectors.toMap(Image::getId, image -> image));
                for (Server instance : instances) {
                    list.add(OpenStackUtils.toF2CVirtualMachine(osClient, instance, region, imageMap));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return list;
    }

    public static List<F2CImage> listImage(OpenStackBaseRequest request) {
        List<F2CImage> list = new ArrayList<>();

        try {
            OSClient.OSClientV3 osClient = request.getOSClient();

            List<String> regions = OpenStackUtils.getRegionList(osClient);
            for (String region : regions) {
                osClient.useRegion(region);
                for (Image image : osClient.imagesV2().list()) {
                    list.add(OpenStackUtils.toF2CImage(image, region));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return list;
    }

    public static List<F2CDisk> listDisk(OpenStackBaseRequest request) {
        List<F2CDisk> list = new ArrayList<>();

        try {
            OSClient.OSClientV3 osClient = request.getOSClient();

            List<String> regions = OpenStackUtils.getRegionList(osClient);
            for (String region : regions) {
                osClient.useRegion(region);
                for (Volume volume : osClient.blockStorage().volumes().list()) {
                    list.add(OpenStackUtils.toF2CDisk(volume, region));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return list;

    }

    public static boolean powerOff(OpenStackInstanceActionRequest request) {
        try {
            OSClient.OSClientV3 osClient = request.getOSClient();
            osClient.useRegion(request.getRegionId());

            Server server = osClient.compute().servers().get(request.getUuid());
            if (server == null) {
                throw new RuntimeException("server not exist");
            }

            ActionResponse response = osClient.compute().servers().action(request.getUuid(), Action.STOP);
            if (!response.isSuccess()) {
                throw new RuntimeException(response.getFault());
            }
            CheckStatusResult result = OpenStackUtils.checkServerStatus(osClient, server, Server.Status.SHUTOFF);
            if (result.isSuccess()) {
                return true;
            } else {
                throw new RuntimeException(result.getFault());
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static boolean powerOn(OpenStackInstanceActionRequest request) {
        try {
            OSClient.OSClientV3 osClient = request.getOSClient();
            osClient.useRegion(request.getRegionId());

            Server server = osClient.compute().servers().get(request.getUuid());
            if (server == null) {
                throw new RuntimeException("server not exist");
            }

            ActionResponse response = osClient.compute().servers().action(request.getUuid(), Action.START);
            if (!response.isSuccess()) {
                throw new RuntimeException(response.getFault());
            }
            CheckStatusResult result = OpenStackUtils.checkServerStatus(osClient, server, Server.Status.ACTIVE);
            if (result.isSuccess()) {
                return true;
            } else {
                throw new RuntimeException(result.getFault());
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static boolean rebootInstance(OpenStackInstanceActionRequest request) {
        try {
            OSClient.OSClientV3 osClient = request.getOSClient();
            osClient.useRegion(request.getRegionId());

            Server server = osClient.compute().servers().get(request.getUuid());
            if (server == null) {
                throw new RuntimeException("server not exist");
            }

            ActionResponse response;
            if (!request.getForce()) {
                response = osClient.compute().servers().reboot(request.getUuid(), RebootType.SOFT);
            } else {
                response = osClient.compute().servers().reboot(request.getUuid(), RebootType.HARD);
            }
            if (!response.isSuccess()) {
                throw new RuntimeException(response.getFault());
            }
            CheckStatusResult result = OpenStackUtils.checkServerStatus(osClient, server, Server.Status.ACTIVE);
            if (result.isSuccess()) {
                return true;
            } else {
                throw new RuntimeException(result.getFault());
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static boolean deleteInstance(OpenStackInstanceActionRequest request) {
        try {
            OSClient.OSClientV3 osClient = request.getOSClient();
            osClient.useRegion(request.getRegionId());

            Server server = osClient.compute().servers().get(request.getUuid());
            if (server == null) {
                return true;
            }
            ActionResponse response;
            if (!request.getForce()) {
                response = osClient.compute().servers().delete(request.getUuid());
            } else {
                //force DElETE
                response = osClient.compute().servers().action(request.getUuid(), Action.FORCEDELETE);
            }
            if (response.isSuccess()) {
                return true;
            } else {
                throw new RuntimeException(response.getFault());
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static boolean attachDisk(OpenStackDiskActionRequest request) {
        try {
            OSClient.OSClientV3 osClient = request.getOSClient();
            osClient.useRegion(request.getRegionId());

            Volume volume = osClient.blockStorage().volumes().get(request.getDiskId());
            if (volume == null) {
                throw new RuntimeException("volume not exist");
            }
            Server server = osClient.compute().servers().get(request.getInstanceUuid());
            if (server == null) {
                throw new RuntimeException("server not exist");
            }

            CheckStatusResult result = doAttachVolume(osClient, volume, server.getId(), request.getDevice());
            if (result.isSuccess()) {
                return true;
            } else {
                throw new RuntimeException(result.getFault());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static CheckStatusResult doAttachVolume(OSClient.OSClientV3 osClient, Volume volume, String serverId, String device) {
        osClient.compute().servers().attachVolume(serverId, volume.getId(), device);
        return OpenStackUtils.checkDiskStatus(osClient, volume, Volume.Status.IN_USE);
    }

    public static boolean detachDisk(OpenStackDiskActionRequest request) {
        try {
            OSClient.OSClientV3 osClient = request.getOSClient();
            osClient.useRegion(request.getRegionId());

            Volume volume = osClient.blockStorage().volumes().get(request.getDiskId());
            if (volume == null) {
                throw new RuntimeException("volume not exist");
            }
            CheckStatusResult result = doDetachVolume(osClient, volume);
            if (result.isSuccess()) {
                return true;
            } else {
                throw new RuntimeException(result.getFault());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static CheckStatusResult doDetachVolume(OSClient.OSClientV3 osClient, Volume volume) {
        ActionResponse response = osClient.compute().servers().detachVolume(volume.getAttachments().get(0).getServerId(), volume.getAttachments().get(0).getId());
        if (!response.isSuccess()) {
            throw new RuntimeException(response.getFault());
        }
        return OpenStackUtils.checkDiskStatus(osClient, volume, Volume.Status.AVAILABLE);
    }


    public static boolean deleteDisk(OpenStackDiskActionRequest request) {
        try {
            OSClient.OSClientV3 osClient = request.getOSClient();
            osClient.useRegion(request.getRegionId());

            Volume volume = osClient.blockStorage().volumes().get(request.getDiskId());
            if (volume == null) {
                return true;
            }

            ActionResponse response;
            if (!request.isForce()) {
                response = osClient.blockStorage().volumes().delete(request.getDiskId());
            } else {
                //force
                response = osClient.blockStorage().volumes().forceDelete(request.getDiskId());
            }
            if (response.isSuccess()) {
                return true;
            } else {
                throw new RuntimeException(response.getFault());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static boolean enlargeDisk(OpenStackDiskEnlargeRequest request) {
        String serverId = null;
        boolean needAttach = false;
        OSClient.OSClientV3 osClient = null;
        Volume volume = null;
        String device = null;
        try {
            osClient = request.getOSClient();
            osClient.useRegion(request.getRegionId());

            volume = osClient.blockStorage().volumes().get(request.getDiskId());
            if (volume == null) {
                throw new RuntimeException("volume not exist");
            }
            CheckStatusResult result;
            //只有api > 3.42 时，in_use的盘可以直接扩，这里只能先卸载再挂载了
            if (volume.getStatus().equals(Volume.Status.IN_USE)) {
                serverId = volume.getAttachments().get(0).getServerId();
                device = volume.getAttachments().get(0).getDevice();
                //卸载
                result = doDetachVolume(osClient, volume);
                if (result.isSuccess()) {
                    needAttach = true;
                } else {
                    throw new RuntimeException(result.getFault());
                }
            }

            //扩容
            ActionResponse response = osClient.blockStorage().volumes().extend(request.getDiskId(), request.getNewDiskSize());
            if (!response.isSuccess()) {
                throw new RuntimeException(response.getFault());
            }
            result = OpenStackUtils.checkDiskStatus(osClient, volume, Volume.Status.AVAILABLE);
            if (!result.isSuccess()) {
                throw new RuntimeException(result.getFault());
            }

            if (needAttach) {
                needAttach = false; //防止catch重复挂载
                //挂载
                result = doAttachVolume(osClient, volume, serverId, device);
                if (!result.isSuccess()) {
                    throw new RuntimeException(result.getFault());
                }
            }
            return true;

        } catch (Exception e) {
            if (needAttach) {
                try {
                    doAttachVolume(osClient, volume, serverId, device);
                } catch (Exception e1) {
                    log.error(e1.getMessage(), e1);
                }
            }
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static F2CDisk createDisk(OpenStackDiskCreateRequest request) {
        try {
            OSClient.OSClientV3 osClient = request.getOSClient();
            osClient.useRegion(request.getRegionId());

            VolumeBuilder builder = Builders.volume()
                    .name(request.getDiskName())
                    .description(request.getDescription())
                    .size(request.getSize());
            //.zone(request.getZone());
            if (StringUtils.isNotBlank(request.getDiskType())) {
                builder.volumeType(request.getDiskType());
            }
            Volume volume = osClient.blockStorage().volumes().create(builder.build());
            CheckStatusResult result = OpenStackUtils.checkDiskStatus(osClient, volume, Volume.Status.AVAILABLE);
            if (!result.isSuccess()) {
                throw new RuntimeException(result.getFault());
            }

            //创建出来后挂载
            if (StringUtils.isNotBlank(request.getInstanceUuid())) {
                Server server = osClient.compute().servers().get(request.getInstanceUuid());
                if (server == null) {
                    throw new RuntimeException("server not exist");
                }
                osClient.compute().servers().attachVolume(request.getInstanceUuid(), volume.getId(), null);
                result = OpenStackUtils.checkDiskStatus(osClient, volume, Volume.Status.IN_USE);
                if (!result.isSuccess()) {
                    throw new RuntimeException(result.getFault());
                }
            }

            return OpenStackUtils.toF2CDisk((Volume) result.getObject(), request.getRegionId());

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    public static List<VolumeType> listVolumeType(OpenStackBaseRequest request) {
        List<VolumeType> list = new ArrayList<>();
        try {
            OSClient.OSClientV3 osClient = request.getOSClient();
            osClient.useRegion(request.getRegionId());

            for (org.openstack4j.model.storage.block.VolumeType type : osClient.blockStorage().volumes().listVolumeTypes()) {
                list.add(new VolumeType().setId(type.getId()).setName(type.getName()));
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return list;
    }

    public static List<Flavor> getFlavors(OpenStackServerCreateRequest request) {
        List<Flavor> list = new ArrayList<>();
        try {
            OSClient.OSClientV3 osClient = request.getOSClient();
            osClient.useRegion(request.getRegionId());

            Image image = osClient.imagesV2().get(request.getImageId());

            for (Flavor flavor : osClient.compute().flavors().list()) {
                if (flavor.isDisabled()) {
                    continue;
                }
                //排除内存小于1G的
                if (flavor.getRam() < 1024) {
                    continue;
                }
                //根据镜像过滤
                if (image != null) {
                    if (image.getMinDisk() > flavor.getDisk()) {
                        continue;
                    }
                    if (image.getMinRam() > flavor.getRam()) {
                        continue;
                    }
                }
                list.add(flavor);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return list.stream().sorted(Comparator.comparingInt(Flavor::getVcpus).thenComparingInt(Flavor::getRam).thenComparingInt(Flavor::getDisk)).collect(Collectors.toList());
    }

    public static List<SecurityGroup> getSecurityGroups(OpenStackServerCreateRequest request) {
        List<SecurityGroup> list = new ArrayList<>();
        try {
            OSClient.OSClientV3 osClient = request.getOSClient();
            osClient.useRegion(request.getRegionId());

            Map<String, String> filteringParams = new HashMap<>();
            //管理员账权限号可以拿到所有安全组？暂时无法区分是否为共享的，所以只拿这个project下的
            filteringParams.put("project_id", request.getOpenStackCredential().getProject());

            for (SecurityGroup securityGroup : osClient.networking().securitygroup().list(filteringParams)) {
                //排除其他project下非共享的
                list.add(securityGroup);
            }


        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return list;
    }

    public static List<Network> getNetworks(OpenStackServerCreateRequest request) {
        List<Network> list = new ArrayList<>();
        try {
            OSClient.OSClientV3 osClient = request.getOSClient();
            osClient.useRegion(request.getRegionId());

            Map<String, String> filteringParams = new HashMap<>();
            //filteringParams.put("project_id", request.getOpenStackCredential().getProject());
            //filteringParams.put("shared", "true");

            for (Network network : osClient.networking().network().list(filteringParams)) {
                if (State.ACTIVE.equals(network.getStatus())) {
                    list.add(network);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return list;
    }
}
