package org.openstack4j.model.storage.block;

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
     * return the host where volume is on
     *
     * @return hostname
     */
    String getHostname();

    /**
     * Gets the id of this volume attachment
     *
     * @return the id
     */
    String getId();

    /**
     * the server's id in this volume attachment
     *
     * @return the  id of a server
     */
    String getServerId();

    /**
     * the volume's id in this volume attachment
     *
     * @return the id of a volume
     */
    String getVolumeId();

    /**
     * the attachment id for this volume
     *
     * @return the id of a attachment
     */
    String getAttachmentId();
}
