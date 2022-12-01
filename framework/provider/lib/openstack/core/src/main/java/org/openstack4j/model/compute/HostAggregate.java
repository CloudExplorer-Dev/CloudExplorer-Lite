package org.openstack4j.model.compute;

import org.openstack4j.model.ModelEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Represents the HostAggregates
 *
 * @author liujunpeng
 */
public interface HostAggregate extends ModelEntity {

    /**
     * Availability zone of aggregate
     *
     * @return Availability zone
     */
    String getAvailabilityZone();

    /**
     * @return created time
     */
    Date getCreate();

    /**
     * @return true:deleted;false:no
     */
    boolean isDeleted();

    /**
     * @return deleted time
     */
    Date getDeletedAt();

    /**
     * Host ID to add to an aggregate, which is a collection of multiple groups
     * of hosts that share common resources like storage and network.
     *
     * @return hosts list
     */
    List<String> getHosts();

    /**
     * The ID associated with an aggregate
     *
     * @return id
     */
    String getId();

    /**
     * Metadata value
     *
     * @return Map
     */
    Map<String, String> getMetadata();

    /**
     * @return name
     */
    String getName();

    /**
     * @return last updated time
     */
    Date getUpdatedAt();

}
