package org.openstack4j.openstack.storage.block.domain;

import org.openstack4j.model.ModelEntity;

/**
 * Force Detach Connector
 *
 * @author Wang Ting/王婷
 */
public class ForceDetachConnector implements ModelEntity {

    private static final long serialVersionUID = 1L;

    private String initiator;

    public ForceDetachConnector(String initiator) {
        super();
        this.initiator = initiator;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

}
