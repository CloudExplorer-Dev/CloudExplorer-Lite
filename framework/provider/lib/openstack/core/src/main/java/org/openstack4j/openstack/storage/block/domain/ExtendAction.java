package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;

@JsonRootName("os-extend")
public class ExtendAction implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonProperty("new_size")
    private final Integer newSize;

    public ExtendAction(Integer newSize) {
        this.newSize = newSize;
    }

    public static ExtendAction create(Integer newSize) {
        return new ExtendAction(newSize);
    }

    public Integer getNewSize() {
        return newSize;
    }
}
