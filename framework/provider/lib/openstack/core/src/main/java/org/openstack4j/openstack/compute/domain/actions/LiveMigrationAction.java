package org.openstack4j.openstack.compute.domain.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.compute.actions.LiveMigrateOptions;

/**
 * Live Migration Action for a Server
 *
 * @author Jeremy Unruh
 */
@JsonRootName("os-migrateLive")
public class LiveMigrationAction implements ServerAction {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String host;

    @JsonProperty("block_migration")
    private boolean blockMigration;

    @JsonProperty("disk_over_commit")
    private boolean diskOverCommit;

    public static LiveMigrationAction create(LiveMigrateOptions options) {
        LiveMigrationAction action = new LiveMigrationAction();
        action.host = options.getHost();
        action.blockMigration = options.getBlockMigration();
        action.diskOverCommit = options.getDiskOverCommit();
        return action;
    }

    public String getHost() {
        return host;
    }

    @JsonIgnore
    public boolean isBlockMigration() {
        return blockMigration;
    }

    @JsonIgnore
    public boolean isDiskOverCommit() {
        return diskOverCommit;
    }

    @Override
    public String toString() {
        return "os-migrateLive";
    }

}
