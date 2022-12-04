package org.openstack4j.api.dns.v2;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.dns.v2.Recordset;

import java.util.List;


/**
 * Designate V2 Recordset Service
 */
public interface RecordsetService extends RestService {

    /**
     * create a new recordset
     *
     * @param zoneId    the identifier of the zone
     * @param recordSet the Recordset
     * @return the newly created Recordset
     */
    Recordset create(String zoneId, Recordset recordSet);

    /**
     * create a new recordset
     *
     * @param zoneId  the identifier for the zone
     * @param name    the DNS name for the recordset
     * @param type    the RRTYPE of the recordset
     * @param records a list of data for this recordset. Each item will be a separate record in Designate These items should conform to the DNS spec for the record type - e.g. A records must be IPv4 addresses, CNAME records must be a hostname.
     * @return the newly created Recordset
     */
    Recordset create(String zoneId, String name, String type, List<String> records);

    /**
     * gets detailed information about a specified recordset in a zone by id
     *
     * @param zoneId      the uui of the zone
     * @param recordsetId the uuid of the recordset
     * @return the recordset
     */
    Recordset get(String zoneId, String recordsetId);

    /**
     * updates an existing recordset
     *
     * @param zoneId    the identifier of the zone
     * @param recordset the recordset set to update
     * @return the updated recordset
     */
    Recordset update(String zoneId, Recordset recordset);

    /**
     * delete a recordset within a zone
     *
     * @param zoneId      the uuid of the zone
     * @param recordsetId the uuid of the recordset
     * @return the action response
     */
    ActionResponse delete(String zoneId, String recordsetId);

    /**
     * list all recordsets owned by project
     *
     * @return list of recordsets
     */
    List<? extends Recordset> list();

    /**
     * list recordsets in a zone
     *
     * @param zoneId the identifier of the zone
     * @return list of recordsets in a zone
     */
    List<? extends Recordset> list(String zoneId);

}
