package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.sahara.JobBinary;
import org.openstack4j.model.sahara.JobBinaryCredentials;
import org.openstack4j.model.sahara.builder.JobBinaryBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;

/**
 * For mapping JSON response to/from java objects
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */

@JsonRootName("job_binary")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaharaJobBinary implements JobBinary {

    private static final long serialVersionUID = 1L;

    @JsonProperty("description")
    private String description;
    @JsonProperty("url")
    private String url;
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date createdAt;
    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date updatedAt;
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("extra")
    private SaharaJobBinaryCredentials credentials;

    /**
     * @return the job binary Builder
     */
    public static JobBinaryBuilder builder() {
        return new ConcreteJobBinaryBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getURL() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTenantId() {
        return tenantId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobBinaryCredentials getCredentials() {
        return credentials;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("description", description)
                .add("url", url)
                .add("tenant_id", tenantId)
                .add("created_at", createdAt)
                .add("updated_at", updatedAt)
                .add("id", id)
                .add("name", name)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobBinaryBuilder toBuilder() {
        return new ConcreteJobBinaryBuilder(this);
    }

    public static class JobBinaries extends ListResult<SaharaJobBinary> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("binaries")
        private List<SaharaJobBinary> jobbinaries;

        public List<SaharaJobBinary> value() {
            return jobbinaries;
        }
    }

    public static class ConcreteJobBinaryBuilder implements JobBinaryBuilder {

        SaharaJobBinary m;

        ConcreteJobBinaryBuilder() {
            this(new SaharaJobBinary());
        }

        ConcreteJobBinaryBuilder(SaharaJobBinary m) {
            this.m = m;
        }

        @Override
        public JobBinary build() {
            return m;
        }

        @Override
        public JobBinaryBuilder from(JobBinary in) {
            m = (SaharaJobBinary) in;
            return this;
        }

        @Override
        public JobBinaryBuilder description(String description) {
            m.description = description;
            return this;
        }

        @Override
        public JobBinaryBuilder url(String url) {
            m.url = url;
            return this;
        }

        @Override
        public JobBinaryBuilder name(String name) {
            m.name = name;
            return this;
        }

        @Override
        public JobBinaryBuilder credentials(String user, String password) {
            m.credentials = new SaharaJobBinaryCredentials(user, password);
            return this;
        }

    }
}
