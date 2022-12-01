package org.openstack4j.openstack.compute.domain.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.RebootType;
import org.slf4j.LoggerFactory;

/**
 * Simple Actions Classes used for Server Action Invocation
 *
 * @author Jeremy Unruh
 */
@SuppressWarnings("serial")
public final class BasicActions {

    /**
     * Returns a ServerAction Entity instance for the specified Action. If the action is not a "basic"
     * type or does not exist then null is returned
     *
     * @param action the type of action to return an instance for
     * @return the ServerAction instance or null
     */
    public static ServerAction actionInstanceFor(Action action) {
        switch (action) {
            case PAUSE:
                return instanceFor(Pause.class);
            case UNPAUSE:
                return instanceFor(UnPause.class);
            case LOCK:
                return instanceFor(Lock.class);
            case UNLOCK:
                return instanceFor(UnLock.class);
            case START:
                return instanceFor(Start.class);
            case STOP:
                return instanceFor(Stop.class);
            case RESUME:
                return instanceFor(Resume.class);
            case RESCUE:
                return instanceFor(Rescue.class);
            case UNRESCUE:
                return instanceFor(UnRescue.class);
            case SHELVE:
                return instanceFor(Shelve.class);
            case SHELVE_OFFLOAD:
                return instanceFor(ShelveOffload.class);
            case UNSHELVE:
                return instanceFor(UnShelve.class);
            case SUSPEND:
                return instanceFor(Suspend.class);
            case FORCEDELETE:
                return instanceFor(ForceDelete.class);
            default:
                return null;
        }
    }

    public static ServerAction instanceFor(Class<? extends ServerAction> action) {
        ServerAction sa = null;
        try {
            sa = action.newInstance();
        } catch (Throwable t) {
            LoggerFactory.getLogger(ServerAction.class).error(t.getMessage(), t);
        }
        return sa;
    }

    @JsonRootName("pause")
    public static class Pause implements ServerAction {
    }

    @JsonRootName("unpause")
    public static class UnPause implements ServerAction {
    }

    @JsonRootName("lock")
    public static class Lock implements ServerAction {
    }

    @JsonRootName("unlock")
    public static class UnLock implements ServerAction {
    }

    @JsonRootName("os-start")
    public static class Start implements ServerAction {
    }

    @JsonRootName("os-stop")
    public static class Stop implements ServerAction {
    }

    @JsonRootName("resume")
    public static class Resume implements ServerAction {
    }

    @JsonRootName("rescue")
    public static class Rescue implements ServerAction {
    }

    @JsonRootName("unrescue")
    public static class UnRescue implements ServerAction {
    }

    @JsonRootName("shelve")
    public static class Shelve implements ServerAction {
    }

    @JsonRootName("shelveOffload")
    public static class ShelveOffload implements ServerAction {
    }

    @JsonRootName("unshelve")
    public static class UnShelve implements ServerAction {
    }

    @JsonRootName("suspend")
    public static class Suspend implements ServerAction {
    }

    @JsonRootName("confirmResize")
    public static class ConfirmResize implements ServerAction {
    }

    @JsonRootName("revertResize")
    public static class RevertResize implements ServerAction {
    }

    @JsonRootName("migrate")
    public static class Migrate implements ServerAction {
    }

    @JsonRootName("forceDelete")
    public static class ForceDelete implements ServerAction {
    }

    @JsonRootName("reboot")
    public static class Reboot implements ServerAction {

        @JsonProperty("type")
        public String type;

        public Reboot(RebootType type) {
            this.type = type.name();
        }
    }

    @JsonRootName("resize")
    public static class Resize implements ServerAction {

        @JsonProperty("flavorRef")
        public String flavorRef;

        public Resize(String flavorRef) {
            this.flavorRef = flavorRef;
        }
    }

    @JsonRootName("changePassword")
    public static class ChangePassword implements ServerAction {

        @JsonProperty("adminPass")
        public String adminPass;

        public ChangePassword(String adminPass) {
            this.adminPass = adminPass;
        }
    }
}
