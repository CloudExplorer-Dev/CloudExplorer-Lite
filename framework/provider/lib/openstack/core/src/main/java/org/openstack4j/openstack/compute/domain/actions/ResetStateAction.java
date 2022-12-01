package org.openstack4j.openstack.compute.domain.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.compute.Server.Status;

/**
 * Resets the state of a server to a specified state.
 *
 * @author Jeremy Unruh
 */
@JsonRootName("os-resetState")
public class ResetStateAction implements ServerAction {

    private static final long serialVersionUID = 1L;

    @JsonProperty("state")
    private final Status state;

    public ResetStateAction(Status state) {
        this.state = state;
    }

    public static ResetStateAction create(Status state) {
        return new ResetStateAction(state);
    }

    public Status getState() {
        return state;
    }
}

