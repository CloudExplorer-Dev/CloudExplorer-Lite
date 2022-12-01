package org.openstack4j.api.compute.ext;


import org.openstack4j.common.RestService;
import org.openstack4j.model.compute.ext.AvailabilityZone;

import java.util.List;

/**
 * API which supports the "os-availability-zone" extension.
 *
 * @author octopus zhang
 */
public interface ZoneService extends RestService {

    /**
     * List availability zone info .
     * <p>
     * NOTE: This is an extension and not all deployments support os-availability-zone
     *
     * @return the available zones in brief form
     */
    List<? extends AvailabilityZone> list();

    /**
     * List availability zone info .
     * <p>
     * NOTE: This is an extension and not all deployments support os-availability-zone
     *
     * @param detailed if true (admin only) details information will be populated
     * @return the available zones resolved to the specified {@code detailed} param
     */
    List<? extends AvailabilityZone> list(boolean detailed);
}
