package org.openstack4j.model.dns.v2;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.dns.v2.builder.RecordsetBuilder;

import java.util.List;
import java.util.Map;

/**
 * Recordset model
 *
 * @see <a href="https://developer.openstack.org/api-ref/dns/">API reference</a>
 */
public interface Recordset extends ModelEntity, Buildable<RecordsetBuilder> {

    /**
     * @return id for the recordset
     */
    String getId();

    /**
     * @return id for the project that owns the resource
     */
    String getProjectId();

    /**
     * @return DNS Name for the recordset
     */
    String getName();

    /**
     * @return TTL (Time to Live) for the recordset.
     */
    String getTTL();

    /**
     * @return status of the resource
     */
    Status getStatus();

    /**
     * @return current action in progress on the resource
     */
    Action getAction();

    /**
     * @return id for the zone that contains this recordset
     */
    String getZoneId();

    /**
     * @return name of the zone that contains this recordset
     */
    String getZoneName();

    /**
     * @return description for this recordset
     */
    String getDescription();

    /**
     * @return RRTYPE of the recordset
     */
    String getType();

    /**
     * @return version of the resource
     */
    Integer getVersion();

    /**
     * @return date / time when resource was created
     */
    String getCreatedAt();

    /**
     * @return date / time when resource was last updated
     */
    String getUpdatedAt();

    /**
     * @return links to the resource, and other related resources.
     */
    Map<String, String> getLinks();

    /**
     * @return list of data for this recordset. Each item will be a separate record in Designate.
     */
    List<String> getRecords();

}
