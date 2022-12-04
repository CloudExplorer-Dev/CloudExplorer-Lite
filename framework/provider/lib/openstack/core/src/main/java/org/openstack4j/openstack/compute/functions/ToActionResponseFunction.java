package org.openstack4j.openstack.compute.functions;

import com.google.common.base.Function;
import org.openstack4j.core.transport.HttpEntityHandler;
import org.openstack4j.core.transport.HttpResponse;
import org.openstack4j.core.transport.functions.ResponseToActionResponse;
import org.openstack4j.model.common.ActionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Function which consumes the input of an HttpResponse and returns the
 * corresponding ActionResponse
 *
 * @author Jeremy Unruh
 */
public class ToActionResponseFunction implements Function<HttpResponse, ActionResponse> {

    public static final ToActionResponseFunction INSTANCE = new ToActionResponseFunction();
    private static final Logger LOG = LoggerFactory.getLogger(ToActionResponseFunction.class);
    private static final String FAILED_MSG = "Cannot '%s' while instance in in state of %s";

    @Override
    public ActionResponse apply(HttpResponse response) {
        return apply(response, null);
    }

    public ActionResponse apply(HttpResponse response, String action) {
        try {
            if (response.getStatus() == 409 || response.getStatus() == 500) {
                ActionResponse ar = ResponseToActionResponse.INSTANCE.apply(response, true);
                if (ar != null) {
                    return ar;
                }

                LOG.error("{}, {}", response.getStatus(), response.getStatusMessage());
                if (action == null) {
                    return ActionResponse.actionFailed("Instance currently is in build state", 409);
                }
                return ActionResponse.actionFailed(String.format(FAILED_MSG, action, action), 409);
            }
            if (response.getStatus() >= 400 && response.getStatus() < 409) {
                return ResponseToActionResponse.INSTANCE.apply(response);
            }
            return ActionResponse.actionSuccess();
        } finally {
            HttpEntityHandler.closeQuietly(response);
        }
    }

}
