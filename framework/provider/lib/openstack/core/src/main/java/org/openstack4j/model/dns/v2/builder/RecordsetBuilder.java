package org.openstack4j.model.dns.v2.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.dns.v2.Action;
import org.openstack4j.model.dns.v2.Recordset;
import org.openstack4j.model.dns.v2.Status;

import java.util.List;
import java.util.Map;

/**
 * A Builder which creates a designate v2 Recordset
 */
public interface RecordsetBuilder extends Builder<RecordsetBuilder, Recordset> {

    /**
     * @see Recordset#getId()
     */
    RecordsetBuilder id(String id);

    /**
     * @see Recordset#getProjectId()
     */
    RecordsetBuilder projectId(String projectId);

    /**
     * @see Recordset#getName()
     */
    RecordsetBuilder name(String name);

    /**
     * @see Recordset#getTTL() ()
     */
    RecordsetBuilder ttl(String ttl);

    /**
     * @see Recordset#getStatus()
     */
    RecordsetBuilder status(Status status);

    /**
     * @see Recordset#getAction()
     */
    RecordsetBuilder action(Action action);

    /**
     * @see Recordset#getZoneId()
     */
    RecordsetBuilder zoneId(String zoneId);

    /**
     * @see Recordset#getZoneName()
     */
    RecordsetBuilder zoneName(String zoneName);

    /**
     * @see Recordset#getDescription()
     */
    RecordsetBuilder description(String description);

    /**
     * @see Recordset#getType()
     */
    RecordsetBuilder type(String type);

    /**
     * @see Recordset#getVersion()
     */
    RecordsetBuilder version(Integer version);

    /**
     * @see Recordset#getCreatedAt()
     */
    RecordsetBuilder createdAt(String createdAt);

    /**
     * @see Recordset#getUpdatedAt()
     */
    RecordsetBuilder updatedAt(String updatedAt);

    /**
     * @see Recordset#getLinks()
     */
    RecordsetBuilder links(Map<String, String> links);

    /**
     * @see Recordset#getRecords()
     */
    RecordsetBuilder records(List<String> records);

}
