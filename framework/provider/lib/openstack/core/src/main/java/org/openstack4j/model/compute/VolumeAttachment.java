package org.openstack4j.model.compute;

import org.openstack4j.model.ModelEntity;

/**
 * Provides volume attachment result
 *
 * @author Octopus Zhang
 */
public interface VolumeAttachment extends ModelEntity {

    /**
     * the device name in the server,like /dev/vdd
     *
     * @return device name
     */
    String getDevice();

    /**
     * Gets the id.
     *
     * @return the id
     */
    String getId();

    /**
     * the server's id int this attachment
     *
     * @return the id
     */
    String getServerId();

    /**
     * the volume's id int this attachment
     *
     * @return the id
     */
    String getVolumeId();
}
