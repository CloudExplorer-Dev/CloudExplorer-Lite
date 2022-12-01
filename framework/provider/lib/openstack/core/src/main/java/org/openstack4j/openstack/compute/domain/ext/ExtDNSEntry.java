package org.openstack4j.openstack.compute.domain.ext;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.compute.ext.DNSEntry;
import org.openstack4j.model.compute.ext.DNSRecordType;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * A Floating IP DNS Extension - DNS Entry
 *
 * @author Jeremy Unruh
 */
@JsonRootName("dns_entry")
@Deprecated
public class ExtDNSEntry implements DNSEntry {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String id;
    @JsonProperty
    private String domain;
    @JsonProperty("ip")
    private String ipAddress;
    @JsonProperty
    private String name;
    @JsonProperty
    private DNSRecordType type;

    /* Only used during create / modify operations */
    @JsonProperty("dns_type")
    private DNSRecordType createType;

    public ExtDNSEntry() {
    }

    public ExtDNSEntry(String ipAddress, DNSRecordType type) {
        this.ipAddress = ipAddress;
        this.createType = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DNSRecordType getType() {
        return type;
    }

    @Override
    public String toString() {
        return toStringHelper(this).omitNullValues()
                .add("id", id).add("domain", domain).add("ip", ipAddress)
                .add("name", name).add("type", type)
                .toString();
    }

    @Deprecated
    public static class DNSEntries extends ListResult<ExtDNSEntry> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("dns_entries")
        private List<ExtDNSEntry> result;

        @Override
        protected List<ExtDNSEntry> value() {
            return result;
        }

    }

}
