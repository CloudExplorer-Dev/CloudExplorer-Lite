package org.openstack4j.openstack.compute.internal.ext;

import org.openstack4j.api.compute.ext.FloatingIPDNSEntryService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.ext.DNSEntry;
import org.openstack4j.model.compute.ext.DNSRecordType;
import org.openstack4j.openstack.compute.domain.ext.ExtDNSEntry;
import org.openstack4j.openstack.compute.domain.ext.ExtDNSEntry.DNSEntries;
import org.openstack4j.openstack.compute.internal.BaseComputeServices;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A Service which handles DNS Entries for the Floating IP DNS Extension
 *
 * @author Jeremy Unruh
 */
@Deprecated
public class FloatingIPDNSEntryServiceImpl extends BaseComputeServices implements FloatingIPDNSEntryService {

    @Override
    public List<? extends DNSEntry> listByIP(String domain, String ip) {
        checkNotNull(ip, "ip");
        return listByName(domain, ip);
    }

    @Override
    public List<? extends DNSEntry> listByName(String domain, String name) {
        checkNotNull(domain, "domain");
        checkNotNull(name, "name");

        return get(DNSEntries.class, uri("/os-floating-ip-dns/%s/entries/%s", domain, name)).execute().getList();
    }

    @Override
    public DNSEntry create(String domain, String name, String ip, DNSRecordType type) {
        checkNotNull(domain, "domain");
        checkNotNull(name, "name");
        checkNotNull(ip, "ip");
        checkNotNull(type, "type");

        return put(ExtDNSEntry.class, uri("/os-floating-ip-dns/%s/entries/%s", domain, name))
                .entity(new ExtDNSEntry(ip, type))
                .execute();
    }

    @Override
    public DNSEntry modifyIP(String domain, String name, String ip) {
        checkNotNull(domain, "domain");
        checkNotNull(name, "name");
        checkNotNull(ip, "ip");

        return put(ExtDNSEntry.class, uri("/os-floating-ip-dns/%s/entries/%s", domain, name))
                .entity(new ExtDNSEntry(ip, DNSRecordType.A))
                .execute();
    }

    @Override
    public ActionResponse delete(String domain, String name) {
        checkNotNull(domain, "domain");
        checkNotNull(name, "name");

        return delete(ActionResponse.class, uri("/os-floating-ip-dns/%s/entries/%s", domain, name)).execute();
    }

}
