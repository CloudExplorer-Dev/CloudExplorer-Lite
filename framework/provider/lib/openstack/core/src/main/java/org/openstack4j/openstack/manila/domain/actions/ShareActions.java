package org.openstack4j.openstack.manila.domain.actions;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.manila.Share;
import org.openstack4j.model.manila.actions.AccessOptions;

/**
 * Simple Actions classes used for share action invocation.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public final class ShareActions {
    public static GrantAccessAction grantAccess(AccessOptions options) {
        return new GrantAccessAction(options.getAccessLevel(), options.getAccessType(), options.getAccessTo());
    }

    public static RevokeAccessAction revokeAccess(String accessId) {
        return new RevokeAccessAction(accessId);
    }

    public static ListAccessRulesAction listAccessRules() {
        return new ListAccessRulesAction();
    }

    public static ResetStateAction resetState(Share.Status status) {
        return new ResetStateAction(status);
    }

    public static ForceDeleteAction forceDelete() {
        return new ForceDeleteAction();
    }

    public static SizeAction.Extend extend(int newSize) {
        return new SizeAction.Extend(newSize);
    }

    public static SizeAction.Shrink shrink(int newSize) {
        return new SizeAction.Shrink(newSize);
    }

    @JsonRootName("os-access_list")
    public static class ListAccessRulesAction implements ShareAction {
    }

    @JsonRootName("os-force_delete")
    public static class ForceDeleteAction implements ShareAction {
    }
}
