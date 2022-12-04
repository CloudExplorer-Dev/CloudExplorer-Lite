package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.openstack4j.model.sahara.Instance;
import org.openstack4j.model.sahara.NodeGroup;
import org.openstack4j.model.sahara.ServiceConfig;
import org.openstack4j.model.sahara.builder.NodeGroupBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An OpenStack Sahara
 *
 * @author Ekasit Kijsipongse
 */
@JsonRootName("node_group")
public class SaharaNodeGroup implements NodeGroup {

    private static final long serialVersionUID = 1L;

    private String name;
    private Integer count;
    @JsonProperty("node_group_template_id")
    private String nodeGroupTemplateId;
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date createdAt;
    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date updatedAt;
    @JsonProperty("image_id")
    private String imageId;
    @JsonProperty("volumes_size")
    private Integer volumesSize;
    @JsonProperty("volumes_per_node")
    private Integer volumesPerNode;
    @JsonProperty("floating_ip_pool")
    private String floatingNetworkId;
    @JsonProperty("flavor_id")
    private String flavorId;
    @JsonProperty("volume_mount_prefix")
    private String volumeMountPrefix;
    @JsonProperty("auto_security_group")
    private Boolean autoSecurityGroup;

    @JsonProperty("security_groups")
    private List<String> securityGroups;

    @JsonProperty("node_processes")
    private List<String> nodeProcesses;

    @JsonProperty("node_configs")
    private Map<String, SaharaServiceConfig> serviceConfigs;


    @JsonProperty("instances")
    private List<SaharaInstance> instances; // only in cluster json response

    /**
     * @return the node group Builder
     */
    public static NodeGroupBuilder builder() {
        return new ConcreteNodeGroupBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeGroupBuilder toBuilder() {
        return new ConcreteNodeGroupBuilder(this);
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
    public Integer getCount() {
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeGroupTemplateId() {
        return nodeGroupTemplateId;
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
    public String getFloatingNetworkId() {
        return floatingNetworkId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getVolumesPerNode() {
        return volumesPerNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getVolumesSize() {
        return volumesSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVolumeMountPrefix() {
        return volumeMountPrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getImageId() {
        return imageId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFlavorId() {
        return flavorId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getSecurityGroups() {
        return securityGroups;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isAutoSecurityGroup() {
        return autoSecurityGroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getNodeProcesses() {
        return nodeProcesses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, ? extends ServiceConfig> getServiceConfigs() {
        return serviceConfigs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Instance> getInstances() {
        return instances;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name)
                .add("count", count)
                .add("created_at", createdAt)
                .add("updated_at", updatedAt)
                .add("image_id", imageId)
                .add("volumes_size", volumesSize)
                .add("volumes_per_node", volumesPerNode)
                .add("float_ip_pool", floatingNetworkId)
                .add("flavor_id", flavorId)
                .add("volume_mount_prefix", volumeMountPrefix)
                .add("node_group_template_id", imageId)
                .add("security_groups", securityGroups)
                .add("auto_security_group", autoSecurityGroup)
                .add("node_processes", nodeProcesses)
                .add("node_configs", serviceConfigs)
                .add("instances", instances)
                .toString();
    }

    public static class NodeGroups extends ListResult<SaharaNodeGroup> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("node_groups")
        private List<SaharaNodeGroup> nodeGroups;

        @Override
        protected List<SaharaNodeGroup> value() {
            return nodeGroups;
        }
    }

    public static class ConcreteNodeGroupBuilder implements NodeGroupBuilder {

        private SaharaNodeGroup m;

        ConcreteNodeGroupBuilder() {
            this(new SaharaNodeGroup());
        }

        ConcreteNodeGroupBuilder(SaharaNodeGroup m) {
            this.m = m;
        }

        @Override
        public NodeGroup build() {
            return m;
        }

        @Override
        public NodeGroupBuilder from(NodeGroup in) {
            m = (SaharaNodeGroup) in;
            return this;
        }

        @Override
        public NodeGroupBuilder name(String name) {
            m.name = name;
            return this;
        }

        @Override
        public NodeGroupBuilder nodeGroupTemplateId(String templateId) {
            m.nodeGroupTemplateId = templateId;
            return this;
        }

        @Override
        public NodeGroupBuilder count(int count) {
            m.count = count;
            return this;
        }

        @Override
        public NodeGroupBuilder floatingIpPool(String networkId) {
            m.floatingNetworkId = networkId;
            return this;
        }

        @Override
        public NodeGroupBuilder flavor(String flavorId) {
            m.flavorId = flavorId;
            return this;
        }

        @Override
        public NodeGroupBuilder setAutoSecurityGroup(boolean isAutoSecurityGroup) {
            m.autoSecurityGroup = isAutoSecurityGroup;
            return this;
        }

        @Override
        public NodeGroupBuilder addSecurityGroup(String id) {
            if (id != null && !id.isEmpty()) {
                if (m.securityGroups == null)
                    m.securityGroups = Lists.newArrayList();
                m.securityGroups.add(id);
            }
            return this;
        }

        @Override
        public NodeGroupBuilder addNodeProcess(String name) {
            if (name != null && !name.isEmpty()) {
                if (m.nodeProcesses == null)
                    m.nodeProcesses = Lists.newArrayList();
                m.nodeProcesses.add(name);
            }
            return this;
        }

        @Override
        public NodeGroupBuilder addServiceConfig(String name, ServiceConfig config) {
            if (name != null && !name.isEmpty()) {
                if (m.serviceConfigs == null)
                    m.serviceConfigs = new HashMap<String, SaharaServiceConfig>();
                m.serviceConfigs.put(name, (SaharaServiceConfig) config);
            }
            return this;
        }

    }
}
