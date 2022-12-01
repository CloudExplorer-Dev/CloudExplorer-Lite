package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.Ethertype;
import org.openstack4j.model.network.ext.FlowClassifier;

import java.util.Map;

/**
 * A builder to create a flow classifier
 *
 * @author Dmitry Gerenrot
 */
public interface FlowClassifierBuilder extends Builder<FlowClassifierBuilder, FlowClassifier> {

    /**
     * @param id : Flow Classifier identifer
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder id(String id);

    /**
     * @param name : Human readable name for the flow classifier
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder name(String name);

    /**
     * @param projectId : Project (tenant) identifier
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder projectId(String projectId);

    /**
     * @param description : Human readable description for the flow classifier
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder description(String description);

    /**
     * @param protocol : Short name for the protocol (TCP, UDP, etc)
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder protocol(String protocol);

    /**
     * @param sourcePortRangeMin : Minimum value for the source port, converted to String
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder sourcePortRangeMin(Integer sourcePortRangeMin);

    /**
     * @param sourcePortRangeMax : Maximum value for the source port, converted to String
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder sourcePortRangeMax(Integer sourcePortRangeMax);

    /**
     * @param destinationPortRangeMin : Minimum value for the destination port, converted to String
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder destinationPortRangeMin(Integer destinationPortRangeMin);

    /**
     * @param destinationPortRangeMax : Maximum value for the destination port, converted to String
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder destinationPortRangeMax(Integer destinationPortRangeMax);

    /**
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder sourceIpPrefix(String sourceIpPrefix);

    /**
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder destinationIpPrefix(String destinationIpPrefix);

    /**
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder logicalSourcePort(String logicalSourcePort);

    /**
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder logicalDestinationPort(String logicalDestinationPort);

    /**
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder l7Parameters(Map<String, String> l7Parameters);

    /**
     * @return FlowClassifierBuilder
     */
    FlowClassifierBuilder ethertype(Ethertype ethertype);
}
