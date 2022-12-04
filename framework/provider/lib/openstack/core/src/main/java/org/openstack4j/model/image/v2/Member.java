package org.openstack4j.model.image.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.openstack.image.v2.domain.GlanceMember;

import java.util.Date;

/**
 * A Glance V2 Member
 *
 * @author emjburns
 */
@JsonDeserialize(as = GlanceMember.class)
public interface Member extends ModelEntity {

    /**
     * An identifier for the image
     * Pattern: ^([0-9a-fA-F]){8}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){12}$
     *
     * @return imageId
     */
    String getImageId();

    /**
     * An identifier for the image member (tenantId or projectId)
     *
     * @return memberId
     */
    String getMemberId();

    /**
     * The date and time of image member creation
     *
     * @return createdAt
     */
    Date getCreatedAt();

    /**
     * The date and time of last modification of image member
     *
     * @return updatedAt
     */
    Date getUpdatedAt();

    /**
     * The status of the image member
     *
     * @return status
     */
    MemberStatus getStatus();

    /**
     * The json schema for the member object
     */
    String getSchema();

    public enum MemberStatus {
        /**
         * Indicates to owner that image has not been updated.
         */
        PENDING,
        /**
         * Accept member status on image to see details of image.
         */
        ACCEPTED,
        /**
         * Functionally the same as pending, but indicates awareness of the
         * member request and specifically denies it.
         */
        REJECTED,
        /**
         * ImageStatus is not one of the reccognized statuses
         */
        UNKNOWN;

        @JsonCreator
        public static MemberStatus forValue(String value) {
            if (value != null) {
                for (MemberStatus s : MemberStatus.values()) {
                    if (s.name().equalsIgnoreCase(value)) {
                        return s;
                    }
                }
            }
            return MemberStatus.UNKNOWN;
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }
}
