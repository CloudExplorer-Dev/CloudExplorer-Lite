package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * This class is used to add the json root name "encryption"
 *
 * @author Jyothi Saroja
 */
@JsonRootName("encryption")
public class CinderVolumeTypeEncryption extends AbstractCinderVolumeTypeEncryption {

    private static final long serialVersionUID = 1L;

}
