package org.openstack4j.model.manila;

import org.openstack4j.model.ModelEntity;

import java.util.List;

/**
 * Limits are the resource limitations that are allowed for each tenant (project).
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface Limits extends ModelEntity {
    /**
     * @return the rate limits
     */
    List<? extends RateLimit> getRate();

    /**
     * @return the absolute limits
     */
    AbsoluteLimit getAbsolute();
}
