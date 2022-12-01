package org.openstack4j.model.dns.v2.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.dns.v2.Action;
import org.openstack4j.model.dns.v2.Status;
import org.openstack4j.model.dns.v2.Zone;
import org.openstack4j.model.dns.v2.ZoneType;

import java.util.List;
import java.util.Map;

/**
 * A Builder which creates a designate v2 Zone
 */
public interface ZoneBuilder extends Builder<ZoneBuilder, Zone> {

    /**
     * @see Zone#getId()
     */
    ZoneBuilder id(String id);

    /**
     * @see Zone#getPoolId()
     */
    ZoneBuilder poolId(String poolId);

    /**
     * @see Zone#getProjectId()
     */
    ZoneBuilder projectId(String projectId);

    /**
     * @see Zone#getName()
     */
    ZoneBuilder name(String name);

    /**
     * @see Zone#getEmail() ()
     */
    ZoneBuilder email(String email);

    /**
     * @see Zone#getTTL() ()
     */
    ZoneBuilder ttl(Integer ttl);

    /**
     * @see Zone#getSerial()
     */
    ZoneBuilder serial(String serial);

    /**
     * @see Zone#getStatus()
     */
    ZoneBuilder status(Status status);

    /**
     * @see Zone#getAction()
     */
    ZoneBuilder action(Action action);

    /**
     * @see Zone#getDescription() ()
     */
    ZoneBuilder description(String description);

    /**
     * @see Zone#getMasters()
     */
    ZoneBuilder masters(List<String> masters);

    /**
     * @see Zone#getType()
     */
    ZoneBuilder type(ZoneType type);

    /**
     * @see Zone#getTransferedAt()
     */
    ZoneBuilder transferredAt(String transferredAt);

    /**
     * @see Zone#getVersion()
     */
    ZoneBuilder version(Integer version);

    /**
     * @see Zone#getCreatedAt()
     */
    ZoneBuilder createdAt(String createdAt);

    /**
     * @see Zone#getUpdatedAt()
     */
    ZoneBuilder updatedAt(String updatedAt);

    /**
     * @see Zone#getLinks()
     */
    ZoneBuilder links(Map<String, String> links);

}
