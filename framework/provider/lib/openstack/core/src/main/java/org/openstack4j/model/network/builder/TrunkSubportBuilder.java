package org.openstack4j.model.network.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.TrunkSubport;

/**
 * A builder which creates a subport
 *
 * @author Kashyap Jha
 */
public interface TrunkSubportBuilder extends Builder<TrunkSubportBuilder, TrunkSubport> {

    /**
     * Set the segmentation ID
     */
    TrunkSubportBuilder segmentationId(int segmentationId);

    /**
     * Set the port ID
     */
    TrunkSubportBuilder portId(String portId);

    /**
     * Set the segmentation type
     */
    TrunkSubportBuilder segmentationType(String segmentationType);
}
