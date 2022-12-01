package org.openstack4j.model.network;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.network.builder.TrunkSubportBuilder;

/**
 * A network subport used ONLY in trunks
 *
 * @author Kashyap Jha
 */
public interface TrunkSubport extends Resource, Buildable<TrunkSubportBuilder> {

    /**
     * @return the segmentation ID
     */
    int getSegmentationId();

    /**
     * @return the port ID
     */
    String getPortId();

    /**
     * @return the segmentation type
     */
    String getSegmentationType();
}
