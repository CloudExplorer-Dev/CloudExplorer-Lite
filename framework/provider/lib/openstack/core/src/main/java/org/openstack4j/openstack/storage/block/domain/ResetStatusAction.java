package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.storage.block.Volume;

@JsonRootName("os-reset_status")
public class ResetStatusAction implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonProperty("status")
    private final Volume.Status status;

    public ResetStatusAction(Volume.Status status) {
        this.status = status;
    }

    public static ResetStatusAction create(Volume.Status status) {
        return new ResetStatusAction(status);
    }

    public Volume.Status getStatus() {
        return status;
    }
}
