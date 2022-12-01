package org.openstack4j.openstack.compute.functions;

import com.google.common.base.Function;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.openstack.compute.domain.NovaServerCreateWithHintsWrapper;

/**
 * Wraps a ServerCreate entity to another type if applicable.  For example if a server container scheduler hints
 * this function is responsible for wrapping it in a higher level class to support booting.
 *
 * @author Jeremy Unruh
 */
public class WrapServerIfApplicableFunction implements Function<ServerCreate, ModelEntity> {

    public static final WrapServerIfApplicableFunction INSTANCE = new WrapServerIfApplicableFunction();

    /**
     * Wraps the ServerCreate if applicable or returns {@code server} if the operation is a no-op
     */
    @Override
    public ModelEntity apply(ServerCreate server) {
        if (server.getSchedulerHints() != null)
            return NovaServerCreateWithHintsWrapper.wrap(server);

        return server;
    }

}
