package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.openstack4j.model.sahara.Job;
import org.openstack4j.model.sahara.JobBinary;
import org.openstack4j.model.sahara.builder.JobBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;

/**
 * For mapping JSON response to/from java objects
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */

@JsonRootName("job")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaharaJob implements Job {

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
    @JsonProperty("type")
    private String type;
    @JsonProperty("mains")
    private List<SaharaJobBinary> mains;
    @JsonProperty("libs")
    private List<SaharaJobBinary> libs;

    private List<String> mainBinaryIds;
    private List<String> libBinaryIds;

    /**
     * @return the job Builder
     */
    public static JobBuilder builder() {
        return new ConcreteJobBuilder();
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
    public String getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends JobBinary> getFullMains() {
        return mains;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends JobBinary> getFullLibs() {
        return libs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getMains() {
        return mainBinaryIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getLibs() {
        return libBinaryIds;
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
                .add("mains", mains)
                .add("libs", libs)
                .add("type", type)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobBuilder toBuilder() {
        return new ConcreteJobBuilder(this);
    }

    public static class Jobs extends ListResult<SaharaJob> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("jobs")
        private List<SaharaJob> jobs;

        @Override
        protected List<SaharaJob> value() {
            return jobs;
        }
    }

    public static class ConcreteJobBuilder implements JobBuilder {

        SaharaJob m;

        ConcreteJobBuilder() {
            this(new SaharaJob());
        }

        ConcreteJobBuilder(SaharaJob m) {
            this.m = m;
        }

        @Override
        public Job build() {
            return m;
        }

        @Override
        public JobBuilder from(Job in) {
            m = (SaharaJob) in;
            return this;
        }

        @Override
        public JobBuilder description(String description) {
            m.description = description;
            return this;
        }

        @Override
        public JobBuilder type(String type) {
            m.type = type;
            return this;
        }

        @Override
        public JobBuilder name(String name) {
            m.name = name;
            return this;
        }

        @Override
        @JsonProperty("mains")
        public JobBuilder setMain(String jobBinaryId) {
            if (m.mainBinaryIds == null)
                m.mainBinaryIds = Lists.newArrayList();
            m.mainBinaryIds.add(jobBinaryId);
            return this;
        }

        @Override
        @JsonProperty("libs")
        public JobBuilder addLibs(String jobBinaryId) {
            if (m.libBinaryIds == null)
                m.libBinaryIds = Lists.newArrayList();
            m.libBinaryIds.add(jobBinaryId);
            return this;
        }

    }

}
