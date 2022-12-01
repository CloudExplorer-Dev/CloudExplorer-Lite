package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.openstack4j.model.sahara.NodeGroupTemplate;
import org.openstack4j.model.sahara.ServiceConfig;
import org.openstack4j.model.sahara.builder.NodeGroupTemplateBuilder;
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
@JsonRootName("node_group_template")
public class SaharaNodeGroupTemplate implements NodeGroupTemplate {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    @JsonProperty("hadoop_version")
    private String hadoopVersion;
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date createdAt;
    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date updatedAt;
    @JsonProperty("plugin_name")
    private String pluginName;
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

    /**
     * @return the node group template Builder
     */
    public static NodeGroupTemplateBuilder builder() {
        return new ConcreteNodeGroupTemplateBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeGroupTemplateBuilder toBuilder() {
        return new ConcreteNodeGroupTemplateBuilder(this);
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
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHadoopVersion() {
        return hadoopVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPluginName() {
        return pluginName;
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
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("name", name)
                .add("description", description)
                .add("hadoop_version", hadoopVersion)
                .add("tenant_id", tenantId)
                .add("created_at", createdAt)
                .add("updated_at", updatedAt)
                .add("plugin_name", pluginName)
                .add("image_id", imageId)
                .add("volumes_size", volumesSize)
                .add("volumes_per_node", volumesPerNode)
                .add("float_ip_pool", floatingNetworkId)
                .add("flavor_id", flavorId)
                .add("volume_mount_prefix", volumeMountPrefix)
                .add("security_groups", securityGroups)
                .add("auto_security_group", autoSecurityGroup)
                .add("node_processes", nodeProcesses)
                .add("node_configs", serviceConfigs)
                .toString();
    }

    public static class NodeGroupTemplates extends ListResult<SaharaNodeGroupTemplate> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("node_group_templates")
        private List<SaharaNodeGroupTemplate> nodeGroupTemplates;

        @Override
        protected List<SaharaNodeGroupTemplate> value() {
            return nodeGroupTemplates;
        }
    }

    public static class ConcreteNodeGroupTemplateBuilder implements NodeGroupTemplateBuilder {

        private SaharaNodeGroupTemplate m;

        ConcreteNodeGroupTemplateBuilder() {
            this(new SaharaNodeGroupTemplate());
        }

        ConcreteNodeGroupTemplateBuilder(SaharaNodeGroupTemplate m) {
            this.m = m;
        }

        @Override
        public NodeGroupTemplate build() {
            return m;
        }

        @Override
        public NodeGroupTemplateBuilder from(NodeGroupTemplate in) {
            m = (SaharaNodeGroupTemplate) in;
            return this;
        }

        @Override
        public NodeGroupTemplateBuilder name(String name) {
            m.name = name;
            return this;
        }

        @Override
        public NodeGroupTemplateBuilder description(String description) {
            m.description = description;
            return this;
        }

        @Override
        public NodeGroupTemplateBuilder hadoopVersion(String hadoopVersion) {
            m.hadoopVersion = hadoopVersion;
            return this;
        }

        @Override
        public NodeGroupTemplateBuilder pluginName(String pluginName) {
            m.pluginName = pluginName;
            return this;
        }

        @Override
        public NodeGroupTemplateBuilder floatingIpPool(String networkId) {
            m.floatingNetworkId = networkId;
            return this;
        }

        @Override
        public NodeGroupTemplateBuilder flavor(String flavorId) {
            m.flavorId = flavorId;
            return this;
        }

        @Override
        public NodeGroupTemplateBuilder setAutoSecurityGroup(boolean isAutoSecurityGroup) {
            m.autoSecurityGroup = isAutoSecurityGroup;
            return this;
        }

        @Override
        public NodeGroupTemplateBuilder addSecurityGroup(String id) {
            if (id != null && !id.isEmpty()) {
                if (m.securityGroups == null)
                    m.securityGroups = Lists.newArrayList();
                m.securityGroups.add(id);
            }
            return this;
        }

        @Override
        public NodeGroupTemplateBuilder addNodeProcess(String name) {
            if (name != null && !name.isEmpty()) {
                if (m.nodeProcesses == null)
                    m.nodeProcesses = Lists.newArrayList();
                m.nodeProcesses.add(name);
            }
            return this;
        }

        @Override
        public NodeGroupTemplateBuilder addServiceConfig(String name, ServiceConfig config) {
            if (name != null && !name.isEmpty()) {
                if (m.serviceConfigs == null)
                    m.serviceConfigs = new HashMap<String, SaharaServiceConfig>();
                m.serviceConfigs.put(name, (SaharaServiceConfig) config);
            }
            return this;
        }

    }
}
