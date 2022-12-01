package org.openstack4j.openstack.manila.domain.actions;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.manila.ShareInstance;

/**
 * Actions classes used for share instance action invocation.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public final class ShareInstanceActions {
    public static ResetStateAction resetState(ShareInstance.Status status) {
        return new ResetStateAction(status);
    }

    public static ForceDeleteAction forceDelete() {
        return new ForceDeleteAction();
    }

    @JsonRootName("os-reset_status")
    public static class ResetStateAction implements ModelEntity {
        private ShareInstance.Status status;

        ResetStateAction(ShareInstance.Status status) {
            this.status = status;
        }

        public ShareInstance.Status getStatus() {
            return status;
        }
    }

    @JsonRootName("os-force_delete")
    public static class ForceDeleteAction implements ModelEntity {
    }
}
