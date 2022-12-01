package org.openstack4j.openstack.networking.domain.ext.LoadBalancerV2StatusTree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.status.ListenerV2Status;
import org.openstack4j.model.network.ext.status.LoadBalancerV2Status;

import java.util.List;

/**
 * An object to hold status of lbaas v2 loadbalancer
 *
 * @author emjburns
 */
@JsonRootName("loadbalancer")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NeutronLoadBalancerV2Status extends Status implements LoadBalancerV2Status {
    @JsonProperty("name")
    private String name;

    @JsonProperty("listeners")
    private List<ListenerV2Status> listenerStatuses;

    @Override
    public List<ListenerV2Status> getListenerStatuses() {
        return listenerStatuses;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("listenerStatuses", listenerStatuses)
                .add("operatingStatus", operatingStatus)
                .add("provisioningStatus", provisioningStatus)
                .toString();
    }

}
