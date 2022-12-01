package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.ext.builder.PortForwardingBuilder;

/**
 * A floating IP port forwarding.
 *
 * @author zjluo
 */
public interface PortForwarding extends ModelEntity, Buildable<PortForwardingBuilder> {
    /**
     * @return the id of the floating IP port forwarding.
     */
    String getId();

    /**
     * @return the IP protocol used in the floating IP port forwarding.
     */
    String getProtocol();


    /**
     * @return the fixed IPv4 address of the Neutron port associated to the floating IP port forwarding.
     */
    String getInternalIpAddress();

    /**
     * @return the TCP/UDP/other protocol port number of the Neutron port fixed IP address associated to the floating ip port forwarding.
     */
    int getInternalPort();

    /**
     * @return the ID of the Neutron port associated to the floating IP port forwarding.
     */
    String getInternalPortId();

    /**
     * @return the TCP/UDP/other protocol port number of the port forwarding’s floating IP address.
     */
    int getExternalPort();

    /**
     * @return a text describing the rule, which helps users to manage/find easily theirs rules.
     */
    String getDescription();

}
