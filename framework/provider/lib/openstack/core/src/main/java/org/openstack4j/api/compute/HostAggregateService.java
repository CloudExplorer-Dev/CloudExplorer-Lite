package org.openstack4j.api.compute;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.HostAggregate;

import java.util.List;
import java.util.Map;

/**
 * Host aggregate Operations API
 *
 * @author liujunpeng
 */
public interface HostAggregateService extends RestService {

    /**
     * List all aggregates (detailed) that the current tenant has access to
     *
     * @return list of all aggregates
     */
    List<? extends HostAggregate> list();

    /**
     * Returns list of Aggregates filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends HostAggregate> list(Map<String, String> filteringParams);


    /**
     * Get the specified HostAggregate by ID
     *
     * @param hostAggregateId the aggregate identifier
     * @return the Aggregate or null if not found
     */
    HostAggregate get(String aggregateId);

    /**
     * Delete of the aggregate
     *
     * @param aggregateId the aggregate identifier
     * @return the action response
     */
    ActionResponse delete(String aggregateId);

    /**
     * Create a hostAggregate
     *
     * @return HostAggregate
     */
    HostAggregate create(String name, String availabilityZone);

    /**
     * Updates the name, and optionally the availability zone, for a specified aggregate.
     *
     * @param hostAggregateId the aggregate identifier
     * @return HostAggregate
     */
    HostAggregate update(String hostAggregateId, String name, String availabilityZone);

    /**
     * Sets metadata for an aggregate.
     *
     * @param hostAggregateId the aggregate identifier
     * @return HostAggregate
     */
    HostAggregate setMetadata(String hostAggregateId, Map<String, String> metadata);

    /**
     * Add host to aggregate
     *
     * @param hostAggregateId The ID associated with an aggregate.
     * @param host            Host ID to add to an aggregate, which is a collection of multiple groups of hosts that share common resources like storage and network.
     * @return HostAggregate
     */
    HostAggregate addHost(String hostAggregateId, String host);

    /**
     * remove host from aggregate
     *
     * @param hostAggregateId The ID associated with an aggregate.
     * @param host            Host ID to add to an aggregate, which is a collection of multiple groups of hosts that share common resources like storage and network.
     * @return HostAggregate
     */
    HostAggregate removeHost(String hostAggregateId, String host);
}
