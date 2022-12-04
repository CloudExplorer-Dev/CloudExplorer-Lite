package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.openstack4j.api.Apis;
import org.openstack4j.model.common.Link;
import org.openstack4j.model.common.functions.IdEntityToString;
import org.openstack4j.model.compute.*;
import org.openstack4j.openstack.common.GenericLink;
import org.openstack4j.openstack.common.IdResourceEntity;
import org.openstack4j.openstack.common.ListResult;

import java.util.*;

@JsonRootName("server")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NovaServer implements Server {

    public static final long serialVersionUID = 1L;
    public String id;
    public String name;
    public NovaAddresses addresses;
    public List<GenericLink> links;
    public Object image;
    public NovaFlavor flavor;
    public String accessIPv4;
    public String accessIPv6;
    @JsonProperty("config_drive")
    public String configDrive;
    public Status status;
    public Integer progress;
    public NovaFault fault;
    @JsonProperty("tenant_id")
    public String tenantId;
    @JsonProperty("user_id")
    public String userId;
    @JsonProperty("key_name")
    public String keyName;
    public String hostId;
    public Date updated;
    public Date created;
    public Map<String, String> metadata;

    @JsonProperty("security_groups")
    private List<NovaSecurityGroup> securityGroups;

    @JsonProperty("OS-EXT-STS:task_state")
    private String taskState;
    @JsonProperty("OS-EXT-STS:power_state")
    private String powerState;
    @JsonProperty("OS-EXT-STS:vm_state")
    private String vmState;
    @JsonProperty("OS-EXT-SRV-ATTR:host")
    private String host;
    @JsonProperty("OS-EXT-SRV-ATTR:instance_name")
    private String instanceName;
    @JsonProperty("OS-EXT-SRV-ATTR:hypervisor_hostname")
    private String hypervisorHostname;
    @JsonProperty("OS-DCF:diskConfig")
    private DiskConfig diskConfig;
    @JsonProperty("OS-EXT-AZ:availability_zone")
    private String availabilityZone;
    @JsonProperty("OS-SRV-USG:launched_at")
    private Date launchedAt;
    @JsonProperty("OS-SRV-USG:terminated_at")
    private Date terminatedAt;
    @JsonProperty("os-extended-volumes:volumes_attached")
    private List<IdResourceEntity> osExtendedVolumesAttached;
    private String uuid;
    private String adminPass;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Addresses getAddresses() {
        return addresses;
    }

    @Override
    public List<? extends Link> getLinks() {
        return links;
    }

    @JsonIgnore
    @Override
    public String getImageId() {
		/*Image image = getImage();
		return (image != null) ? image.getId() : null;*/
        if (image instanceof Map) {
            Map<String, String> map = (Map<String, String>) image;
            return map.get("id");
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Image getImage() {
        if (image instanceof LinkedHashMap) {
            LinkedHashMap map = (LinkedHashMap) image;
            String imageId = (String) map.get("id");
            if (imageId == null || imageId.isEmpty()) {
                return null;
            }
            NovaImage novaImage = (NovaImage) Apis.getComputeServices().images().get(imageId);
            return novaImage;
        }
        return null;
    }

    @JsonIgnore
    @Override
    public String getFlavorId() {
        return (flavor != null) ? flavor.getId() : null;
    }

    @Override
    public Flavor getFlavor() {
        if (flavor != null && flavor.getName() == null)
            flavor = (NovaFlavor) Apis.getComputeServices().flavors().get(flavor.getId());
        return flavor;
    }

    @Override
    public String getAccessIPv4() {
        return accessIPv4;
    }

    @Override
    public String getAccessIPv6() {
        return accessIPv6;
    }

    @Override
    public String getConfigDrive() {
        return configDrive;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public int getProgress() {
        return (progress != null) ? progress : 0;
    }

    @Override
    public Fault getFault() {
        return fault;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getKeyName() {
        return keyName;
    }

    @Override
    public String getHostId() {
        return hostId;
    }

    @Override
    public Date getUpdated() {
        return updated;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Override
    public List<? extends NovaSecurityGroup> getSecurityGroups() {
        return securityGroups;
    }

    @Override
    public String getTaskState() {
        return taskState;
    }

    @Override
    public String getPowerState() {
        return powerState;
    }

    @Override
    public String getVmState() {
        return vmState;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    @Override
    public String getHypervisorHostname() {
        return hypervisorHostname;
    }

    @Override
    public DiskConfig getDiskConfig() {
        return diskConfig;
    }

    @Override
    public String getAvailabilityZone() {
        return availabilityZone;
    }

    @Override
    public Date getLaunchedAt() {
        return launchedAt;
    }

    @Override
    public Date getTerminatedAt() {
        return terminatedAt;
    }

    @SuppressWarnings("unchecked")
    @JsonIgnore
    @Override
    public List<String> getOsExtendedVolumesAttached() {
        return (List<String>) ((osExtendedVolumesAttached == null)
                ? Collections.emptyList()
                : Lists.transform(osExtendedVolumesAttached, IdEntityToString.INSTANCE));
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public String getAdminPass() {
        return adminPass;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name).add("image", image).add("flavor", flavor)
                .add("status", status).add("diskconfig", diskConfig).add("userId", userId)
                .add("admin-pass", adminPass).add("created", created).add("updated", updated)
                .add("launched at", launchedAt).add("tenantId", tenantId).add("hostId", hostId)
                .add("addresses", addresses).add("hypervisor host", hypervisorHostname)
                .add("uuid", uuid).add("powerstate", powerState).add("fault", fault).add("instanceName", instanceName)
                .add("vmState", vmState).add("metadata", metadata)
                .toString();
    }

    public static class Servers extends ListResult<NovaServer> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("servers")
        private List<NovaServer> servers;

        @Override
        public List<NovaServer> value() {
            return servers;
        }
    }
}
