package org.openstack4j.core.transport.functions;

import com.google.common.base.Function;
import org.openstack4j.core.transport.HttpResponse;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.openstack.internal.Parser;

import java.util.Map;

/**
 * Takes an HttpResponse as input and returns an ActionResponse as an output
 *
 * @author Jeremy Unruh
 */
public class ResponseToActionResponse implements Function<HttpResponse, ActionResponse> {

    public static final ResponseToActionResponse INSTANCE = new ResponseToActionResponse();

    @Override
    public ActionResponse apply(HttpResponse response) {
        return apply(response, false);
    }

    public ActionResponse apply(HttpResponse response, boolean returnNullIfNotMapped) {
        if (Parser.isContentTypeText(response.getContentType())) {
            return ActionResponse.actionFailed(response.getStatusMessage(), response.getStatus());
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> map = response.readEntity(Map.class);
        ActionResponse ar = new ParseActionResponseFromJsonMap(response).apply(map);
        if (ar != null)
            return ar;

        if (returnNullIfNotMapped)
            return null;

        return ActionResponse.actionFailed(String.format("Status: %d, Reason: %s", response.getStatus(), response.getStatusMessage()), response.getStatus());
    }
}
