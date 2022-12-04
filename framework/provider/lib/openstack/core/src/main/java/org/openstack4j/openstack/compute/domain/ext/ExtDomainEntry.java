package org.openstack4j.openstack.compute.domain.ext;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.compute.ext.DomainEntry;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * A Floating IP DNS Extension - Domain Entry
 *
 * @author Jeremy Unruh
 */
@JsonRootName("domain_entry")
@Deprecated
public class ExtDomainEntry implements DomainEntry {

    private static final long serialVersionUID = 1L;

    @JsonProperty("availability_zone")
    private String availabilityZone;
    @JsonProperty
    private String domain;
    @JsonProperty
    private String project;
    @JsonProperty
    private Scope scope;

    public ExtDomainEntry() {
    }

    public ExtDomainEntry(Scope scope, String availabilityZone, String project) {
        this.scope = scope;
        this.availabilityZone = availabilityZone;
        this.project = project;
    }

    @Override
    public String getAvailabilityZone() {
        return availabilityZone;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public String getProject() {
        return project;
    }

    @Override
    public Scope getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("availabilityZone", availabilityZone).add("domain", domain)
                .add("project", project).add("scope", scope)
                .toString();
    }

    @Deprecated
    public static class DomainEntries extends ListResult<ExtDomainEntry> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("domain_entries")
        private List<ExtDomainEntry> results;

        @Override
        protected List<ExtDomainEntry> value() {
            return results;
        }

    }

}
