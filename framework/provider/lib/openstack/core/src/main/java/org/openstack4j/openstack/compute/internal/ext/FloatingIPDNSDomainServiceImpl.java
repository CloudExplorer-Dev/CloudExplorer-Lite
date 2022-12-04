package org.openstack4j.openstack.compute.internal.ext;

import org.openstack4j.api.compute.ext.FloatingIPDNSDomainService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.ext.DomainEntry;
import org.openstack4j.model.compute.ext.DomainEntry.Scope;
import org.openstack4j.openstack.compute.domain.ext.ExtDomainEntry;
import org.openstack4j.openstack.compute.domain.ext.ExtDomainEntry.DomainEntries;
import org.openstack4j.openstack.compute.internal.BaseComputeServices;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Service that handles domain names for the floating IP DNS Extension
 *
 * @author Jeremy Unruh
 */
@Deprecated
public class FloatingIPDNSDomainServiceImpl extends BaseComputeServices implements FloatingIPDNSDomainService {

    @Override
    public List<? extends DomainEntry> list() {
        return get(DomainEntries.class, uri("/os-floating-ip-dns")).execute().getList();
    }

    @Override
    public ActionResponse delete(String domainName) {
        checkNotNull(domainName);
        return delete(ActionResponse.class, uri("/os-floating-ip-dns/%s", domainName)).execute();
    }

    @Override
    public DomainEntry createPublic(String domainName, String project) {
        checkNotNull(domainName);
        return put(ExtDomainEntry.class, uri("/os-floating-ip-dns/%s", domainName))
                .entity(new ExtDomainEntry(Scope.PUBLIC, null, project))
                .execute();
    }

    @Override
    public DomainEntry createPrivate(String domainName, String availabilityZone) {
        checkNotNull(domainName);
        return put(ExtDomainEntry.class, uri("/os-floating-ip-dns/%s", domainName))
                .entity(new ExtDomainEntry(Scope.PRIVATE, availabilityZone, null))
                .execute();
    }

}
