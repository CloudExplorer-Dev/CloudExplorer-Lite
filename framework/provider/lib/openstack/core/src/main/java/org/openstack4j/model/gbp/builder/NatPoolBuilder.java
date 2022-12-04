package org.openstack4j.model.gbp.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.gbp.IPVersionType;
import org.openstack4j.model.gbp.NatPool;

/**
 * A builder which produces a Nat Pool object
 *
 * @author vinod borole
 */
public interface NatPoolBuilder extends Builder<NatPoolBuilder, NatPool> {

    NatPoolBuilder name(String name);

    NatPoolBuilder description(String description);

    NatPoolBuilder ipVersion(IPVersionType ipVersion);

    NatPoolBuilder cidr(String cidr);

    NatPoolBuilder isShared(boolean shared);

    NatPoolBuilder externalSegmentId(String id);
}
