package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;

@JsonRootName("os-set_bootable")
public class SetBootableAction implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonProperty("bootable")
    private final Boolean bootable;

    public SetBootableAction(Boolean bootable) {
        this.bootable = bootable;
    }

    public static SetBootableAction create(Boolean bootable) {
        return new SetBootableAction(bootable);
    }

    public Boolean getBootable() {
        return bootable;
    }
}
