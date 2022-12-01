package org.openstack4j.core.transport.functions;

import com.google.common.base.Function;
import org.openstack4j.core.transport.HttpResponse;
import org.openstack4j.model.common.ActionResponse;

import java.util.Map;

/**
 * Attempts to Parse a JSON Map created from an error response and map the message to an ActionResponse.
 *
 * @author Jeremy Unruh
 */
public class ParseActionResponseFromJsonMap implements Function<Map<String, Object>, ActionResponse> {

    private static final String KEY_MESSAGE = "message";
    private static final String NEUTRON_ERROR = "NeutronError";
    private static final String OCTAVIA_ERROR = "faultstring";
    private static final String COMPUTE_FAULT = "computeFault";
    private static final String TACKER_ERROR = "TackerError";
    private HttpResponse response;

    public ParseActionResponseFromJsonMap(HttpResponse response) {
        this.response = response;
    }

    /**
     * Parses the JSON Map for an Error message.  An OpenStack error response typically is a Map of Map containing a single key
     * which is "error", "badRequest", etc which contains a value of another Map containing the underlying message
     *
     * @param map the JSON Map
     * @return ActionResponse or null if the map could not be parsed
     */
    @SuppressWarnings("unchecked")
    @Override
    public ActionResponse apply(Map<String, Object> map) {
        if (map == null || map.isEmpty())
            return null;

        for (String key : map.keySet()) {
            if (map.get(key) == null) {
                continue;
            }

            if (Map.class.isAssignableFrom(map.get(key).getClass())) {
                Map<String, Object> inner = (Map<String, Object>) map.get(key);
                if (inner.containsKey(KEY_MESSAGE)) {
                    String msg = String.valueOf(inner.get(KEY_MESSAGE));
                    return ActionResponse.actionFailed(msg, response.getStatus());
                }
                if (inner.containsKey(NEUTRON_ERROR)) {
                    String msg = String.valueOf(inner.get(NEUTRON_ERROR));
                    return ActionResponse.actionFailed(msg, response.getStatus());
                }
                if (inner.containsKey(COMPUTE_FAULT)) {
                    /** For 'computeFault' Error Message Propagation.. */
                    String msg = String.valueOf(map.get(COMPUTE_FAULT));
                    return ActionResponse.actionFailed(msg, response.getStatus());
                }
                if (inner.containsKey(TACKER_ERROR)) {
                    /** For 'TackerError' Error Message Propagation.. */
                    String msg = String.valueOf(inner.get(TACKER_ERROR));
                    return ActionResponse.actionFailed(msg, response.getStatus());
                }
            }
        }

        // Try with Sahara fault response which is just a plain Map
        // { "error_name": "error name",
        //   "error_message": "error message",
        //   "error_code": XXX }
        if (map.containsKey("error_message")) {
            String msg = String.valueOf(map.get("error_message"));
            return ActionResponse.actionFailed(msg, response.getStatus());
        }

        // Neutron error handling when just a message is present
        if (map.containsKey(NEUTRON_ERROR)) {
            String msg = String.valueOf(map.get(NEUTRON_ERROR));
            return ActionResponse.actionFailed(msg, response.getStatus());
        }

        // Neutron error handling when just a message is present
        if (map.containsKey(OCTAVIA_ERROR)) {
            String msg = String.valueOf(map.get(OCTAVIA_ERROR));
            return ActionResponse.actionFailed(msg, response.getStatus());
        }

        return null;
    }

}
