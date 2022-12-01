package org.openstack4j.core.transport.propagation;

import org.openstack4j.api.exceptions.ResponseException;
import org.openstack4j.core.transport.HttpResponse;
import org.openstack4j.core.transport.PropagateResponse;
import org.openstack4j.core.transport.functions.ResponseToActionResponse;
import org.openstack4j.model.common.ActionResponse;

/**
 * Propagates an exception based on the specified Status code
 *
 * @author Jeremy Unruh
 */
public class PropagateOnStatus implements PropagateResponse {

    private final int statusCode;

    private PropagateOnStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public static PropagateOnStatus on(int statusCode) {
        return new PropagateOnStatus(statusCode);
    }

    @Override
    public void propagate(HttpResponse response) {
        if (response.getStatus() == statusCode) {
            ActionResponse ar = ResponseToActionResponse.INSTANCE.apply(response);
            throw ResponseException.mapException(response, ar.getFault());
        }
    }

}
