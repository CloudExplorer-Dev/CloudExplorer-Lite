package org.openstack4j.model.dns.v2;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.dns.v2.builder.NameserverBuilder;

/**
 * Nameserver model
 *
 * @see <a href="https://developer.openstack.org/api-ref/dns/">API reference</a>
 */
public interface Nameserver extends ModelEntity, Buildable<NameserverBuilder> {

    /**
     * @return the hostname of the nameserver that the zone should be delegated to
     */
    String getHostname();

    /**
     * @return the priority of the nameserver
     */
    Integer getPriority();

}
