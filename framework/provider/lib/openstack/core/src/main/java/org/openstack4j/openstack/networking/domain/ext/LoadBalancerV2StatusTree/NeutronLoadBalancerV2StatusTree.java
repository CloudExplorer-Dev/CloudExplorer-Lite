package org.openstack4j.openstack.networking.domain.ext.LoadBalancerV2StatusTree;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.LoadBalancerV2StatusTree;
import org.openstack4j.model.network.ext.status.LoadBalancerV2Status;

/**
 * The status tree of a lbaas v2 loadbalancer
 *
 * @author emjburns
 */
@JsonRootName("statuses")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronLoadBalancerV2StatusTree implements LoadBalancerV2StatusTree {

    @JsonProperty("loadbalancer")
    private NeutronLoadBalancerV2Status loadBalancerStatus;

    @Override
    public LoadBalancerV2Status getLoadBalancerV2Status() {
        return loadBalancerStatus;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("loadbalancer", loadBalancerStatus)
                .toString();
    }
}
