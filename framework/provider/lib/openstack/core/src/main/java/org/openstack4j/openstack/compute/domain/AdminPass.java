package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.compute.ServerPassword;

/**
 * Administrative password
 *
 * @author taemin
 */
public class AdminPass implements ServerPassword {

    private static final long serialVersionUID = 1L;
    @JsonProperty
    private String adminPass;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return adminPass;
    }


}
