package org.openstack4j.model.trove.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.trove.Datastore;
import org.openstack4j.model.trove.InstanceCreate;

public interface InstanceCreateBuilder extends Buildable.Builder<InstanceCreateBuilder, InstanceCreate> {

    InstanceCreateBuilder availabilityZone(String availabilityZone);

    InstanceCreateBuilder volumeType(String volumeType);

    InstanceCreateBuilder volumeSize(int size);

    InstanceCreateBuilder flavor(String flavorRef);

    InstanceCreateBuilder name(String name);

    InstanceCreateBuilder datastore(Datastore datastore);

}
