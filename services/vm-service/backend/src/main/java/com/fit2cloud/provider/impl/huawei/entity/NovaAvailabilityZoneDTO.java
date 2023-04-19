package com.fit2cloud.provider.impl.huawei.entity;

import com.huaweicloud.sdk.ecs.v2.model.NovaAvailabilityZone;
import lombok.Data;

/**
 * @author jianneng
 * @date 2022/11/11 16:39
 **/
@Data
public class NovaAvailabilityZoneDTO extends NovaAvailabilityZone {

    private String displayName;

    public NovaAvailabilityZoneDTO() {
    }

    public NovaAvailabilityZoneDTO(NovaAvailabilityZone zone) {
        setHosts(zone.getHosts());
        setZoneName(zone.getZoneName());
        setZoneState(zone.getZoneState());
    }
}
