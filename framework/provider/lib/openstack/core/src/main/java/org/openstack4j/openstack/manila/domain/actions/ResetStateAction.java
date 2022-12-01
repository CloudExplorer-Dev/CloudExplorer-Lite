package org.openstack4j.openstack.manila.domain.actions;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.manila.Share;

/**
 * Reset the state of a share.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("os-reset_status")
public class ResetStateAction implements ShareAction {
    private static final long serialVersionUID = 1L;

    private Share.Status status;

    ResetStateAction(Share.Status status) {
        this.status = status;
    }

    public Share.Status getStatus() {
        return status;
    }
}
