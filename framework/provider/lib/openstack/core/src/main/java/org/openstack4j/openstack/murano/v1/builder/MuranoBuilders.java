package org.openstack4j.openstack.murano.v1.builder;

import org.openstack4j.model.murano.v1.builder.AppCatalogBuilders;
import org.openstack4j.model.murano.v1.builder.EnvironmentBuilder;
import org.openstack4j.openstack.murano.v1.domain.MuranoEnvironment;

/**
 * The Application Catalog Builders
 */
public class MuranoBuilders implements AppCatalogBuilders {
    @Override
    public EnvironmentBuilder environment() {
        return MuranoEnvironment.builder();
    }
}
