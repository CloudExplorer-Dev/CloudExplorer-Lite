package org.openstack4j.api.compute.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.ext.DNSEntry;
import org.openstack4j.model.compute.ext.DNSRecordType;

import java.util.List;

/**
 * A Service which handles DNS Entries for the Floating IP DNS Extension
 *
 * @author Jeremy Unruh
 */
@Deprecated
public interface FloatingIPDNSEntryService extends RestService {

    /**
     * Return a list of entries for the given domain and IP Address
     *
     * @param domain the FQ Domain name
     * @param ip     the IP Address
     * @return List of DNS Entries
     */
    List<? extends DNSEntry> listByIP(String domain, String ip);

    /**
     * Return a list of entries for the given domain and name
     *
     * @param domain the FQ Domain name
     * @param name   DNS entry name assigned under a domain
     * @return List of DNS Entries
     */
    List<? extends DNSEntry> listByName(String domain, String name);

    /**
     * Creates or Updates a DNS Entry
     *
     * @param domain the FQ Domain name
     * @param name   DNS entry name assigned under a domain
     * @param ip     the IP Address associated with the current entry
     * @param type   the DNS Record Type
     * @return the created or modified DNSEntry
     */
    DNSEntry create(String domain, String name, String ip, DNSRecordType type);

    /**
     * Modifies the IP Address for the specified domain and name
     *
     * @param domain the FQ Domain name
     * @param name   DNS entry name assigned under a domain
     * @param ip     the new IP Address
     * @return the modified DNSEntry
     */
    DNSEntry modifyIP(String domain, String name, String ip);

    /**
     * Deletes a specified DNS entry
     *
     * @param domain the FQ Domain name
     * @param name   DNS entry name assigned under a domain
     * @return the action response
     */
    ActionResponse delete(String domain, String name);
}
