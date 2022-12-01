package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.compute.ServerPassword;

/**
 * A model class to return the password of the server
 *
 * @author vinod borole
 */
public class NovaPassword implements ServerPassword {

    private static final long serialVersionUID = 1L;
    @JsonProperty
    private String password;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return password;
    }


}
