package com.fit2cloud.provider.impl.openstack.api;

import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import com.fit2cloud.provider.entity.F2CDisk;
import com.fit2cloud.provider.entity.F2CImage;
import com.fit2cloud.provider.entity.F2CVirtualMachine;
import com.fit2cloud.provider.impl.openstack.util.OpenStackUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.identity.v3.Region;
import org.openstack4j.model.identity.v3.Service;
import org.openstack4j.model.image.v2.Image;
import org.openstack4j.model.storage.block.Volume;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
}
