package org.openstack4j.openstack.compute.internal;

import org.openstack4j.api.compute.HostAggregateService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.HostAggregate;
import org.openstack4j.openstack.compute.domain.*;
import org.openstack4j.openstack.compute.domain.NovaHostAggregate.NovaHostAggregates;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Host Aggregate Operation API implementation
 *
 * @author liujunpeng
 */
public class HostAggregateServiceImpl extends BaseComputeServices implements
        HostAggregateService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends HostAggregate> list() {
        Invocation<NovaHostAggregates> req = get(NovaHostAggregates.class, uri("/os-aggregates"));
        return req.execute().getList();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends HostAggregate> list(Map<String, String> filteringParams) {
        Invocation<NovaHostAggregates> req = get(NovaHostAggregates.class, uri("/os-aggregates"));
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                req = req.param(entry.getKey(), entry.getValue());
            }
        }
        return req.execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NovaHostAggregate get(String aggregateId) {
        checkNotNull(aggregateId);
        return get(NovaHostAggregate.class, uri("/os-aggregates/%s", aggregateId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String aggregateId) {
        checkNotNull(aggregateId);
        return ToActionResponseFunction.INSTANCE.apply(
                delete(Void.class, uri("/os-aggregates/%s", aggregateId)).executeWithResponse()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HostAggregate create(String name, String availabilityZone) {
        checkNotNull(name);
        checkNotNull(availabilityZone);
        // modify by chenyan 2017.02.13
        //return post(NovaHostAggregate.class, uri("/os-aggregates")).entity(NovaHostAggregate.create(name, availabilityZone)).execute();
        return post(NovaHostAggregate.class, uri("/os-aggregates")).entity(new NovaHostAggregateCreate(name, availabilityZone)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HostAggregate update(String hostAggregateId, String name,
                                @Nullable String availabilityZone) {
        checkNotNull(hostAggregateId);
        checkNotNull(name);
        return put(NovaHostAggregate.class, uri("/os-aggregates/%s", hostAggregateId)).entity(new NovaHostAggregateUpdate(name, availabilityZone)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HostAggregate setMetadata(String hostAggregateId, Map<String, String> metadata) {
        checkNotNull(hostAggregateId);
        checkNotNull(metadata);
        return post(NovaHostAggregate.class, uri("/os-aggregates/%s/action", hostAggregateId)).entity(new HostAggregateMetadata(metadata)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HostAggregate addHost(String hostAggregateId, String host) {
        checkNotNull(hostAggregateId);
        checkNotNull(host);
        return post(NovaHostAggregate.class, uri("/os-aggregates/%s/action", hostAggregateId)).entity(new AggregateAddHost(host)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HostAggregate removeHost(String hostAggregateId, String host) {
        checkNotNull(hostAggregateId);
        checkNotNull(host);

        return post(NovaHostAggregate.class, uri("/os-aggregates/%s/action", hostAggregateId)).entity(new AggregateRemoveHost(host)).execute();
    }


}
