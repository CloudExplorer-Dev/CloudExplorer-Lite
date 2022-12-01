package org.openstack4j.model.compute.actions;

/**
 * Options to evacuates a server from a failed host to a new host
 */
public class EvacuateOptions extends BaseActionOptions {

    private EvacuateOptions() {
        super();
        add(Option.HOST, null);
        add(Option.ADMIN_PASS, null);
        add(Option.ON_SHARED_STORAGE, false);
    }

    public static EvacuateOptions create() {
        return new EvacuateOptions();
    }

    /**
     * Name of the the host (Optional)
     *
     * @param name or ID of the host to which the server is evacuated
     * @return EvacuateOptions
     */
    public EvacuateOptions host(String host) {
        add(Option.HOST, host);
        return this;
    }

    /**
     * An administrative password (Optional)
     *
     * @param adminPass to access the evacuated or rebuilt instance
     * @return EvacuateOptions
     */
    public EvacuateOptions adminPass(String adminPass) {
        add(Option.ADMIN_PASS, adminPass);
        return this;
    }

    /**
     * Server on shared storage
     *
     * @param isShare if isShare, server on shared storage
     * @return EvacuateOptions
     */
    public EvacuateOptions onSharedStorage(boolean isShare) {
        add(Option.ON_SHARED_STORAGE, isShare);
        return this;
    }

    public String getHost() {
        return get(Option.HOST);
    }

    public String getAdminPass() {
        return get(Option.ADMIN_PASS);
    }

    public boolean isOnSharedStorage() {
        return get(Option.ON_SHARED_STORAGE);
    }

    private enum Option implements OptionEnum {
        HOST("host"),
        ADMIN_PASS("adminPass"),
        ON_SHARED_STORAGE("onSharedStorage");
        private final String param;

        private Option(String param) {
            this.param = param;
        }

        public String getParam() {
            return param;
        }
    }
}
