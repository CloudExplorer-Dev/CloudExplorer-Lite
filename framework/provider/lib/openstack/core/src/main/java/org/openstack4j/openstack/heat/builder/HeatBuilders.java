package org.openstack4j.openstack.heat.builder;

import org.openstack4j.model.heat.builder.*;
import org.openstack4j.openstack.heat.domain.*;

/**
 * The Orchestration V3 Builders
 */
public class HeatBuilders implements OrchestrationBuilders {

    private OrchestrationBuilders HeatBuilders() {
        return this;
    }

    @Override
    public TemplateBuilder template() {
        return HeatTemplate.build();
    }

    @Override
    public StackCreateBuilder stack() {
        return HeatStackCreate.build();
    }

    @Override
    public SoftwareConfigBuilder softwareConfig() {
        return new HeatSoftwareConfig.Builder();
    }

    @Override
    public StackUpdateBuilder stackUpdate() {
        return HeatStackUpdate.builder();
    }

    @Override
    public ResourceHealthBuilder resourceHealth() {
        return HeatResourceHealth.builder();
    }
}
