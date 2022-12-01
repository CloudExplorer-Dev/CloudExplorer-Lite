package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.storage.block.VolumeAttachment;

/**
 * return a description for this volume attachment job
 *
 * @author Octopus Zhang
 */
@JsonRootName("volumeAttachment")
public class CinderVolumeAttachment implements VolumeAttachment {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String device;

    @JsonProperty
    private String host_name;

    @JsonProperty
    private String id;

    @JsonProperty
    private String server_id;

    @JsonProperty
    private String attachment_id;

    @JsonProperty
    private String volume_id;

    @Override
    public String getDevice() {
        return device;
    }

    @Override
    public String getHostname() {
        return host_name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getServerId() {
        return server_id;
    }

    @Override
    public String getVolumeId() {
        return volume_id;
    }

    @Override
    public String getAttachmentId() {
        return attachment_id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("device", device).add("id", id).add("serverId", server_id)
                .add("volumeId", volume_id).add("hostname", host_name)
                .add("attachmentId", attachment_id).toString();
    }
}
