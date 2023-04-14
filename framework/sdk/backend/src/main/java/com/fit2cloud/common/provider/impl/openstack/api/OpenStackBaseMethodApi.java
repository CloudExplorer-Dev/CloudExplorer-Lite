package com.fit2cloud.common.provider.impl.openstack.api;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.impl.openstack.entity.Zone;
import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.ext.AvailabilityZone;

import java.util.ArrayList;
import java.util.List;


public class OpenStackBaseMethodApi {

    public static List<Credential.Region> getRegions(OpenStackBaseRequest request) {
        try {
            return request.getOpenStackCredential().regions();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Zone> getZones(OpenStackBaseRequest request) {
        try {
            OSClient.OSClientV3 osClient = request.getOSClient();
            osClient.useRegion(request.getRegionId());
            List<Zone> list = new ArrayList<>();
            List<? extends AvailabilityZone> availabilityZones = osClient.compute().zones().list();
            for (AvailabilityZone availabilityZone : availabilityZones) {
                if (availabilityZone.getZoneState().getAvailable()) {
                    list.add(
                            new Zone()
                                    .setId(availabilityZone.getZoneName())
                                    .setName(availabilityZone.getZoneName())
                    );
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
