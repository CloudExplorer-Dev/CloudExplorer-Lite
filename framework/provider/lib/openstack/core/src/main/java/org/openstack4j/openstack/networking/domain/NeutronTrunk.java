package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * This class is used to add the json root name "trunk"
 *
 * @author Kashyap Jha
 */
@JsonRootName("trunk")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronTrunk extends AbstractNeutronTrunk {
    private static final long serialVersionUID = 1L;
}
