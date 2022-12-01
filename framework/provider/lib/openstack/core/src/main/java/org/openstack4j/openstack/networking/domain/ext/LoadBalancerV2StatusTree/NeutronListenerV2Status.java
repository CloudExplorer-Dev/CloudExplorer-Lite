package org.openstack4j.openstack.networking.domain.ext.LoadBalancerV2StatusTree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.status.LbPoolV2Status;
import org.openstack4j.model.network.ext.status.ListenerV2Status;

import java.util.List;

/**
 * An object to hold status of lbaas v2 listener
 *
 * @author emjburns
 */
@JsonRootName("listeners")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NeutronListenerV2Status extends Status implements ListenerV2Status {

    @JsonProperty("pools")
    List<LbPoolV2Status> lbPoolStatuses;
    @JsonProperty("name")
    private String name;

    @Override
    public List<LbPoolV2Status> getLbPoolV2Statuses() {
        return lbPoolStatuses;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("pools", lbPoolStatuses)
                .add("operatingStatus", operatingStatus)
                .add("provisioningStatus", provisioningStatus)
                .toString();
    }
}
