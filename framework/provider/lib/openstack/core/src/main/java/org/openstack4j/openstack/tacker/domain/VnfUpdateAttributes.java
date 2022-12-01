package org.openstack4j.openstack.tacker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;

/**
 * @author Vishvesh Deshmukh
 * @date Aug 11, 2016
 */
@JsonRootName("attributes")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VnfUpdateAttributes {

    private String config;

    public static VnfUpdateAttributes create() {
        return new VnfUpdateAttributes();
    }

    /**
     * User config file or data..
     *
     * @return VnfUpdateAttributes
     */
    public VnfUpdateAttributes config(String config) {
        this.config = config;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("config", config)
                .toString();
    }

    /**
     * @return the config
     */
    public String getConfig() {
        return config;
    }
}
