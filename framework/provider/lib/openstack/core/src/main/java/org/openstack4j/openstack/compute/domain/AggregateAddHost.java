package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;

/**
 * Adds a host to an aggregate.
 *
 * @author liujunpeng
 */
@JsonRootName("add_host")
public class AggregateAddHost implements ModelEntity {


    private static final long serialVersionUID = 1L;
    /**
     * host id
     */
    @JsonProperty("host")
    String host;

    /**
     * @return AggregateAddHost
     */
    public AggregateAddHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
