package org.openstack4j.openstack.gbp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.gbp.NetworkServiceParamType;
import org.openstack4j.model.gbp.NetworkServiceParamValue;
import org.openstack4j.model.gbp.NetworkServiceParams;

/**
 * Created by sumit gandhi on 7/4/2016.
 */

public class GbpNetworkServiceParams implements NetworkServiceParams {

    @JsonProperty("type")
    private NetworkServiceParamType paramType;

    private String name;

    @JsonProperty("value")
    private NetworkServiceParamValue paramValue;


    public NetworkServiceParamType getParamType() {
        return paramType;
    }

    public void setParamType(NetworkServiceParamType paramType) {
        this.paramType = paramType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public NetworkServiceParamValue getParamValue() {
        return paramValue;
    }

    public void setParamValue(NetworkServiceParamValue paramValue) {
        this.paramValue = paramValue;
    }

}
