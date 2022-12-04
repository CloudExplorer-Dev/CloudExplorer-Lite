package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.sahara.Instance;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;

/**
 * For mapping JSON response to java objects
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */

//@JsonRootName("instance")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaharaInstance implements Instance {

    private static final long serialVersionUID = 1L;

    @JsonProperty("instance_name")
    private String name;
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date createdAt;
    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date updatedAt;
    @JsonProperty("instance_id")
    private String instanceId;         // Nova instance ID
    @JsonProperty("management_ip")
    private String managementIp;
    private List<String> volumes;
    @JsonProperty("internal_ip")
    private String internalIp;
    @JsonProperty("id")               // Sahara instance ID
    private String id;

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
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getManagementIp() {
        return managementIp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getVolumes() {
        return volumes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInternalIp() {
        return internalIp;
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
                .add("instance_name", name)
                .add("created_at", createdAt)
                .add("updated_at", updatedAt)
                .add("instance_id", instanceId)
                .add("management_ip", managementIp)
                .add("volumes", volumes)
                .add("internal_ip", internalIp)
                .add("id", id)
                .toString();
    }

    public static class Instances extends ListResult<SaharaInstance> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("instances")
        private List<SaharaInstance> instances;

        public List<SaharaInstance> value() {
            return instances;
        }
    }
}
