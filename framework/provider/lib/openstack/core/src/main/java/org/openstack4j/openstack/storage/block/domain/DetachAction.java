package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;

/**
 * os-detach
 *
 * @author capitek-xuning（首信科技-徐宁）
 */
@JsonRootName("os-detach")
public class DetachAction implements ModelEntity {

    private static final long serialVersionUID = 1L;

    /**
     * The ID of the attachment.
     */
    @JsonProperty("attachment_id")
    private String attachmentId;

    /**
     * @param attachmentId The ID of the attachment.
     * @author capitek-xuning（首信科技-徐宁）
     */
    public DetachAction(String attachmentId) {
        super();
        this.attachmentId = attachmentId;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

}
