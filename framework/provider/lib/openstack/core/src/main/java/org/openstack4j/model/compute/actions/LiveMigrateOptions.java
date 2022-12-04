package org.openstack4j.model.compute.actions;

/**
 * Options to live-migrate a server to a new host without rebooting
 *
 * @author Jeremy Unruh
 */
public class LiveMigrateOptions extends BaseActionOptions {

    private LiveMigrateOptions() {
        super();
        add(Option.HOST, null);
        add(Option.BLOCK_MIGRATION, false);
        add(Option.DISK_OVER_COMMIT, false);
    }

    public static LiveMigrateOptions create() {
        return new LiveMigrateOptions();
    }

    /**
     * Name of the new host (Optional)
     *
     * @param host the name of the new host
     * @return LiveMigrateOptions
     */
    public LiveMigrateOptions host(String host) {
        add(Option.HOST, host);
        return this;
    }

    /**
     * Block Migration
     *
     * @param blocked if true, migration is blocked
     * @return LiveMigrateOptions
     */
    public LiveMigrateOptions blockMigration(boolean blocked) {
        add(Option.BLOCK_MIGRATION, blocked);
        return this;
    }

    /**
     * Disk over commit
     *
     * @param enabled if enabled, disk over commit is allowed
     * @return LiveMigrateOptions
     */
    public LiveMigrateOptions diskOverCommit(boolean enabled) {
        add(Option.DISK_OVER_COMMIT, enabled);
        return this;
    }

    public String getHost() {
        return get(Option.HOST);
    }

    public boolean getBlockMigration() {
        return get(Option.BLOCK_MIGRATION);
    }

    public boolean getDiskOverCommit() {
        return get(Option.DISK_OVER_COMMIT);
    }

    private enum Option implements OptionEnum {
        HOST("host"),
        BLOCK_MIGRATION("block_migration"),
        DISK_OVER_COMMIT("disk_over_commit");
        private final String param;

        private Option(String param) {
            this.param = param;
        }

        public String getParam() {
            return param;
        }
    }
}
