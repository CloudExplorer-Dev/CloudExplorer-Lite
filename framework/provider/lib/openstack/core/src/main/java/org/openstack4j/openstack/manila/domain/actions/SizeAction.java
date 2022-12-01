package org.openstack4j.openstack.manila.domain.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Action changing the size of shares.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public abstract class SizeAction implements ShareAction {
    private static final long serialVersionUID = 1L;

    @JsonProperty("new_size")
    private Integer newSize;

    protected SizeAction(Integer newSize) {
        this.newSize = newSize;
    }

    public Integer getNewSize() {
        return newSize;
    }

    @JsonRootName("os-extend")
    public static class Extend extends SizeAction {
        Extend(Integer newSize) {
            super(newSize);
        }
    }

    @JsonRootName("os-shrink")
    public static class Shrink extends SizeAction {
        Shrink(Integer newSize) {
            super(newSize);
        }
    }
}
