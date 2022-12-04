package org.openstack4j.model.compute.ext;

import org.openstack4j.model.ModelEntity;

/**
 * A Floating IP DNS Extension - DNS Entry
 *
 * @author Jeremy Unruh
 */
@Deprecated
public interface DNSEntry extends ModelEntity {

    /**
     * The identifier for this entry
     *
     * @return the identifier for this entry
     */
    String getId();

    /**
     * The FQ Domain this entry is associated with
     *
     * @return the domain name
     */
    String getDomain();

    /**
     * The IP Address associated with this entry
     *
     * @return the IP Address
     */
    String getIpAddress();

    /**
     * The name of this entry
     *
     * @return the name of this entry
     */
    String getName();

    /**
     * The DNS Record Type for this entry
     *
     * @return the entry record type
     */
    DNSRecordType getType();

}
