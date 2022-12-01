package org.openstack4j.model.octavia.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.octavia.ListenerProtocol;
import org.openstack4j.model.octavia.ListenerV2;

import java.util.List;

public interface ListenerV2Builder extends Buildable.Builder<ListenerV2Builder, ListenerV2> {
    /**
     * @param projectId the ID of the project/tenant that owns the resource
     * @return ListenerV2Builder
     */
    ListenerV2Builder projectId(String projectId);

    /**
     * @param protocol The protocol of the VIP address. A valid value is TCP, HTTP,
     *                 or HTTPS.
     * @return ListenerV2Builder
     */
    ListenerV2Builder protocol(ListenerProtocol protocol);

    /**
     * The port in which the frontend will be listening. Must be an integer in the range of 1-65535
     *
     * @return ListenerV2Builder
     */
    ListenerV2Builder protocolPort(Integer protocolPort);

    /**
     * The load balancer this listener will be provisioned on. A tenant can only create listeners on
     * load balancers authorized by policy (e.g. her own load balancers).
     *
     * @return ListenerV2Builder
     */
    ListenerV2Builder loadBalancerId(String loadbalancerId);

    /**
     * Optional
     *
     * @param adminStateUp The administrative state of the VIP. A valid value is true
     *                     (UP) or false (DOWN). Default is true
     * @return ListenerV2Builder
     */
    ListenerV2Builder adminStateUp(boolean adminStateUp);

    /**
     * Optional
     *
     * @param name Pool name. Does not have to be unique.
     * @return ListenerV2Builder
     */
    ListenerV2Builder name(String name);

    /**
     * Optional
     *
     * @param description Description for the pool.
     * @return ListenerV2Builder
     */
    ListenerV2Builder description(String description);

    /**
     * Optional
     * <p>
     * The default value for this attribute will be -1, indicating an infinite limit.
     *
     * @return ListenerV2Builder
     */
    ListenerV2Builder connectionLimit(Integer connectionLimit);

    /**
     * Optional
     * <p>
     * Barbican container with tls policy
     *
     * @return ListenerV2Builder
     */
    ListenerV2Builder defaultTlsContainerRef(String tlsContainerRef);

    /**
     * Optional
     * <p>
     * Barbican container(s) with sni certificates
     *
     * @return ListenerV2Builder
     */
    ListenerV2Builder sniContainerRefs(List<String> sniContainerRefs);
}
