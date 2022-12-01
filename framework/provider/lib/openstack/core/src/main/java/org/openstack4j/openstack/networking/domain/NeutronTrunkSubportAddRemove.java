package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class is used by the subport operations which don't use the json root
 * name "trunks"
 *
 * @author Kashyap Jha
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronTrunkSubportAddRemove extends AbstractNeutronTrunk {
    private static final long serialVersionUID = 1L;
}
