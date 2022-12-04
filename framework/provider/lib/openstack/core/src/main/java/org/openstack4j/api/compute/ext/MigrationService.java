package org.openstack4j.api.compute.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.compute.ext.Migration;
import org.openstack4j.model.compute.ext.MigrationsFilter;

import java.util.List;

/**
 * API which supports the "os-migrations" extension.
 *
 * @author Jeremy Unruh
 */
public interface MigrationService extends RestService {

    /**
     * Administrators only. Fetch in-progress migrations for a region or a specified cell in a region.
     *
     * @return in-progress migrations or empty list
     */
    List<? extends Migration> list();

    /**
     * Administrators only. Fetch in-progress migrations for a region or a specified cell in a region with filtering options
     *
     * @param filter the filter constraints
     * @return in-progress migrations or empty list
     */
    List<? extends Migration> list(MigrationsFilter filter);
}
