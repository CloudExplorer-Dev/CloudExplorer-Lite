package org.openstack4j.openstack.compute.internal.ext;

import org.openstack4j.api.compute.ext.MigrationService;
import org.openstack4j.model.compute.ext.Migration;
import org.openstack4j.model.compute.ext.MigrationsFilter;
import org.openstack4j.openstack.compute.domain.ext.ExtMigration.Migrations;
import org.openstack4j.openstack.compute.internal.BaseComputeServices;

import java.util.List;

/**
 * API which supports the "os-migrations" extension.
 *
 * @author Jeremy Unruh
 */
public class MigrationServiceImpl extends BaseComputeServices implements MigrationService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Migration> list() {
        return list(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Migration> list(MigrationsFilter filter) {
        Invocation<Migrations> inv = get(Migrations.class, uri("/os-migrations"));
        if (filter != null) {
            inv.params(filter.getConstraints());
        }
        return inv.execute().getList();
    }

}
