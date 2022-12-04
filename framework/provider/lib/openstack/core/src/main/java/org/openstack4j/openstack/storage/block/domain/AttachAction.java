package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;

/**
 * os attach
 *
 * @author Wang Ting/王婷
 */
@JsonRootName("os-attach")
public class AttachAction implements ModelEntity {

    private static final long serialVersionUID = 1L;

    /**
     * The UUID of the attaching instance.
     */
    @JsonProperty("instance_uuid")
    private String instanceId;

    /**
     * The attaching mount point.
     */
    @JsonProperty
    private String mountpoint;

    /**
     * The name of the attaching host.
     */
    @JsonProperty("host_name")
    private String hostName;

    /**
     * <br/>Description:
     * <p>Author:Wang Ting/王婷</p>
     */

    public AttachAction(String instanceId, String mountpoint, String hostName) {
        super();
        this.instanceId = instanceId;
        this.mountpoint = mountpoint;
        this.hostName = hostName;
    }

    public String getMountpoint() {
        return mountpoint;
    }

    public void setMountpoint(String mountpoint) {
        this.mountpoint = mountpoint;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getInstanceId() {
        return instanceId;
    }
}
