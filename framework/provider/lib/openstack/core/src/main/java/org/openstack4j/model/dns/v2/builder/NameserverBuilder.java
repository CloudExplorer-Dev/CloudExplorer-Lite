package org.openstack4j.model.dns.v2.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.dns.v2.Nameserver;

/**
 * A Builder which creates a designate v2 nameserver
 */
public interface NameserverBuilder extends Builder<NameserverBuilder, Nameserver> {

    /**
     * @see Nameserver#getHostname()
     */
    NameserverBuilder hostname(String hostname);

    /**
     * @see Nameserver#getPriority()
     */
    NameserverBuilder priority(Integer priority);

}
