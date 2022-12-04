package org.openstack4j.api.dns.v2;

import org.openstack4j.common.RestService;

/**
 * DNS/Designate Service Operations API
 */
public interface DNSService extends RestService {

    /**
     * Zone Service API
     *
     * @return the zone service
     */
    ZoneService zones();

    /**
     * Recordset Service API
     *
     * @return the recordsets service
     */
    RecordsetService recordsets();

}
