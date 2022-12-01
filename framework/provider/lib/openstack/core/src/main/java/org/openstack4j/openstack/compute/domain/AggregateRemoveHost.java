package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;

/**
 * remove a host to an aggregate.
 *
 * @author liujunpeng
 */
@JsonRootName("remove_host")
public class AggregateRemoveHost implements ModelEntity {

    private static final long serialVersionUID = 1L;
    /**
     * host id
     */
    @JsonProperty("host")
    String host;

    /**
     *
     */
    public AggregateRemoveHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
