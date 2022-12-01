package org.openstack4j.model.compute.actions;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Options for Creating a Backup schedule
 *
 * @author Jeremy Unruh
 */
public final class BackupOptions extends BaseActionOptions {

    private BackupOptions(String name) {
        add(Option.NAME, name);
    }

    /**
     * Create a new Backup schedule with the given {@code backupName}
     *
     * @param backupName the name of the backup
     * @return BackupOptions
     */
    public static BackupOptions create(String backupName) {
        checkNotNull(backupName);
        return new BackupOptions(backupName);
    }

    /**
     * Indicates the backup will run daily keeping {@code rotation} copies
     *
     * @param rotation the number of backups to maintain
     * @return BackupOptions
     */
    public BackupOptions daily(int rotation) {
        add(Option.BACKUP_TYPE, "daily");
        add(Option.ROTATION, rotation);
        return this;
    }

    /**
     * Indicates the backup will run weekly keeping {@code rotation} copies
     *
     * @param rotation the number of backups to maintain
     * @return BackupOptions
     */
    public BackupOptions weekly(int rotation) {
        add(Option.BACKUP_TYPE, "weekly");
        add(Option.ROTATION, rotation);
        return this;
    }

    public String getName() {
        return get(Option.NAME);
    }

    public String getBackupType() {
        return get(Option.BACKUP_TYPE);
    }

    public Integer getRotation() {
        return get(Option.ROTATION);
    }

    private enum Option implements OptionEnum {
        NAME("name"),
        BACKUP_TYPE("backup_type"),
        ROTATION("rotation");
        private final String param;

        private Option(String param) {
            this.param = param;
        }

        public String getParam() {
            return param;
        }
    }
}
