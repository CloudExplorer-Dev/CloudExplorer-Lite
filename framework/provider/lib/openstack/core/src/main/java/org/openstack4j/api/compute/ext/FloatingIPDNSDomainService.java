package org.openstack4j.api.compute.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.ext.DomainEntry;

import java.util.List;

/**
 * Service that handles domain names for the floating IP DNS Extension
 *
 * @author Jeremy Unruh
 */
@Deprecated
public interface FloatingIPDNSDomainService extends RestService {

    /**
     * Return the list of available DNS domains
     *
     * @return list of domain entries
     */
    List<? extends DomainEntry> list();

    /**
     * Delete the specified {@code domainName}
     *
     * @param domainName the name of the domain
     * @return the action response
     */
    ActionResponse delete(String domainName);

    /**
     * Creates or Updates a PUBLIC domain name record
     *
     * @param domainName the FQ Domain name
     * @param project    the project name
     * @return the created/updated domain entry
     */
    DomainEntry createPublic(String domainName, String project);

    /**
     * Creates or Updates a PRIVATE domain name record
     *
     * @param domainName       the FQ Domain name
     * @param availabilityZone the availability zone
     * @return he created/updated domain entry
     */
    DomainEntry createPrivate(String domainName, String availabilityZone);

}
