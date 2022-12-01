package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;

@JsonRootName("os-update_readonly_flag")
public class UpdateReadOnlyFlagAction implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonProperty("readonly")
    private final boolean readonly;

    public UpdateReadOnlyFlagAction(boolean readonly) {
        this.readonly = readonly;
    }

    public static UpdateReadOnlyFlagAction create(boolean readonly) {
        return new UpdateReadOnlyFlagAction(readonly);
    }

    public boolean isReadonly() {
        return readonly;
    }

}
