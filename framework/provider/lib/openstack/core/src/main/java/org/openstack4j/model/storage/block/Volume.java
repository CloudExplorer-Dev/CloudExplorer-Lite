package org.openstack4j.model.storage.block;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.CaseFormat;
import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.storage.block.builder.VolumeBuilder;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An OpenStack Volume
 *
 * @author Jeremy Unruh
 */
public interface Volume extends ModelEntity, Buildable<VolumeBuilder> {

    /**
     * @return the identifier for the volume
     */
    String getId();

    /**
     * @return the name of the volume
     */
    String getName();

    /**
     * @return the display name of the volume
     */
    @Deprecated
    String getDisplayName();

    /**
     * @return the description of the volume
     */
    String getDescription();

    /**
     * @return the display description of the volume
     */
    @Deprecated
    String getDisplayDescription();

    /**
     * @return the status of the volume
     */
    Status getStatus();

    /**
     * @return the size in GB of the volume
     */
    int getSize();

    /**
     * @return the zone of availability to use
     */
    String getZone();

    /**
     * @return the created date of the volume
     */
    Date getCreated();

    /**
     * @return the type of volume
     */
    String getVolumeType();

    /**
     * @return the snapshot identifier
     */
    String getSnapshotId();

    /**
     * @return the image reference identifier (if an image was associated) otherwise null
     */
    String getImageRef();

    /**
     * @return To enable this volume to attach
     */
    Boolean multiattach();

    /**
     * @return ID of source volume to clone from
     */
    String getSourceVolid();

    /**
     * @return extended meta data information. key value pair of String key, String value
     */
    Map<String, String> getMetaData();

    /**
     * @return volume attachment data information.
     */
    List<? extends VolumeAttachment> getAttachments();

    /**
     * @return the status of volume migrate status, default null
     */
    MigrationStatus getMigrateStatus();

    /**
     * @return the tenant id
     */
    String getTenantId();

    /**
     * @return whether the volume is bootable
     */
    boolean bootable();

    /**
     * @return whether this volume is encrypted.
     */
    boolean encrypted();

    /**
     * @return current back-end of the volume.
     */
    String host();

    /**
     * The current Volume Status
     */
    public enum Status {
        AVAILABLE, ATTACHING, BACKING_UP, CREATING, DELETING, DOWNLOADING, UPLOADING, ERROR, ERROR_DELETING, ERROR_RESTORING, IN_USE, RESTORING_BACKUP, DETACHING, UNRECOGNIZED;

        @JsonCreator
        public static Status fromValue(String status) {
            try {
                return valueOf(CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, checkNotNull(status, "status")));
            } catch (IllegalArgumentException e) {
                return UNRECOGNIZED;
            }
        }

        @JsonValue
        public String value() {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
        }

        @Override
        public String toString() {
            return value();
        }
    }

    public enum MigrationStatus {
        MIGRATING, ERROR, SUCCESS, COMPLETING, NONE, STARTING;

        @JsonCreator
        public static MigrationStatus fromValue(String migrationStatus) {
            if (migrationStatus != null) {
                try {
                    return valueOf(CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_UNDERSCORE, checkNotNull(migrationStatus, "migrationStatus")));
                } catch (IllegalArgumentException e) {
                    LoggerFactory.getLogger(MigrationStatus.class).error(e.getMessage(), e);
                }
            }
            return NONE;
        }

        @JsonValue
        public String value() {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
        }

        @Override
        public String toString() {
            return value();
        }
    }
}
