package org.openstack4j.model.trove;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.trove.builder.InstanceCreateBuilder;

public interface InstanceCreate extends ModelEntity, Buildable<InstanceCreateBuilder> {

    void setFlavor(String flavorRef);

    void setName(String name);

    void setDatastore(Datastore datastore);

    void setVolumetype(String volumeType);

    void setvolumeSize(int size);

    void setAvailabilityZone(String availabilityZone);
}
