package org.openstack4j.openstack.manila.domain.actions;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Actions classes used for share type action invocation.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public final class ShareTypeActions {
    public static AddShareTypeAccessAction addShareTypeAccess(String project) {
        return new AddShareTypeAccessAction(project);
    }

    public static RemoveShareTypeAccessAction removeShareTypeAccess(String project) {
        return new RemoveShareTypeAccessAction(project);
    }

    private static class ShareTypeAccessAction implements ShareTypeAction {
        private String project;

        private ShareTypeAccessAction(String project) {
            this.project = project;
        }

        private String getProject() {
            return project;
        }
    }

    @JsonRootName("addProjectAccess")
    public static class AddShareTypeAccessAction extends ShareTypeAccessAction {
        AddShareTypeAccessAction(String project) {
            super(project);
        }
    }

    @JsonRootName("removeProjectAccess")
    public static class RemoveShareTypeAccessAction extends ShareTypeAccessAction {
        RemoveShareTypeAccessAction(String project) {
            super(project);
        }
    }
}
