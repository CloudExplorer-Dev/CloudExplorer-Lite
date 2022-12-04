package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.network.ext.builder.FlowClassifierBuilder;

import java.util.Map;

/**
 * A Flow Classifier Entity.
 *
 * @author Dmitry Gerenrot.
 */
public interface FlowClassifier extends Resource, Buildable<FlowClassifierBuilder> {

    /**
     * @return description : Human readable description for the flow classifier
     */
    String getDescription();

    /**
     * @return protocol : Short name for the protocol (TCP, UDP, etc)
     */
    String getProtocol();

    /**
     * @return rangeMin : Minimum value for the source port, converted to String
     */
    Integer getSourcePortRangeMin();

    /**
     * @return rangeMax : Maximum value for the source port, converted to String
     */
    Integer getSourcePortRangeMax();

    /**
     * @return rangeMin : Minimum value for the destination port, converted to String
     */
    Integer getDestinationPortRangeMin();

    /**
     * @return rangeMax : Maximum value for the destination port, converted to String
     */
    Integer getDestinationPortRangeMax();

    /**
     * @return sourcePrefix : Prefix for the source ip addresses
     */
    String getSourceIpPrefix();

    /**
     * @return destinationPrefix : Prefix for the destination ip addresses
     */
    String getDestinationIpPrefix();

    /**
     * @return logicalSourcePort : Id of the port pair at the start of the port chain
     */
    String getLogicalSourcePort();

    /**
     * @return logicalDestinationPort : Id of the port pair at the end of the port chain
     */
    String getLogicalDestinationPort();

    /**
     * @return l7Parameters
     */
    Map<String, String> getL7Parameters();

    /**
     * @return ethertype
     */
    Ethertype getEthertype();
}
