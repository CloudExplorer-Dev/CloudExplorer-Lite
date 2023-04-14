package com.fit2cloud.common.provider.impl.openstack.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.identity.v3.Service;

import java.util.List;
import java.util.stream.Collectors;

public class OpenStackBaseUtils {

    public static List<String> getRegionList(OSClient.OSClientV3 osClient) {
        List<String> regions = osClient.identity().regions().list().stream().map(org.openstack4j.model.identity.v3.Region::getId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(regions)) {
            for (Service service : osClient.getToken().getCatalog()) {
                if (CollectionUtils.isNotEmpty(service.getEndpoints()) && ServiceType.IMAGE.getType().equalsIgnoreCase(service.getType())) {
                    regions.add(service.getEndpoints().get(0).getRegion());
                }
            }
        }
        return regions;
    }
}
