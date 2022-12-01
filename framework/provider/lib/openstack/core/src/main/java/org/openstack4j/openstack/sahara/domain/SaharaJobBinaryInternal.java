package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.sahara.JobBinaryInternal;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;

/**
 * For mapping JSON response to/from java objects
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */

@JsonRootName("job_binary_internal")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaharaJobBinaryInternal implements JobBinaryInternal {

    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    private String name;
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
    @JsonProperty("datasize")
    private int dataSize;

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
    public int getDataSize() {
        return dataSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name)
                .add("tenant_id", tenantId)
                .add("created_at", createdAt)
                .add("updated_at", updatedAt)
                .add("id", id)
                .add("datasize", dataSize)
                .toString();
    }

    public static class JobBinaryInternals extends ListResult<SaharaJobBinaryInternal> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("binaries")
        private List<SaharaJobBinaryInternal> jobBinaryInternal;

        public List<SaharaJobBinaryInternal> value() {
            return jobBinaryInternal;
        }

    }

}
