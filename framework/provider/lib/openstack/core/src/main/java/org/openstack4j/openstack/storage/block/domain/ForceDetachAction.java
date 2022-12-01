package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;

/**
 * os force detach
 *
 * @author Wang Ting/王婷
 */
@JsonRootName("os-force_detach")
public class ForceDetachAction implements ModelEntity {

    private static final long serialVersionUID = 1L;

    /**
     * The interface ID
     */
    @JsonProperty("attachment_id ")
    private String attachmentId;

    /**
     * The connector object.
     */
    @JsonProperty
    private ForceDetachConnector connector;

    public ForceDetachAction(String attachmentId, ForceDetachConnector connector) {
        super();
        this.attachmentId = attachmentId;
        this.connector = connector;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public ForceDetachConnector getConnector() {
        return connector;
    }

    public void setConnector(ForceDetachConnector connector) {
        this.connector = connector;
    }


}
