package org.openstack4j.model.storage.object.options;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Options used for the deletion of Objects
 */
public final class ObjectDeleteOptions {

    public static final ObjectDeleteOptions NONE = new ObjectDeleteOptions();

    private Map<String, List<Object>> queryParams = Maps.newHashMap();

    private ObjectDeleteOptions() {
    }

    public static ObjectDeleteOptions create() {
        return new ObjectDeleteOptions();
    }

    public ObjectDeleteOptions queryParam(String key, Object value) {
        if (value == null)
            return this;

        if (queryParams.containsKey(key)) {
            List<Object> list = queryParams.get(key);
            list.add(value);
        } else {
            List<Object> list = new ArrayList<Object>();
            list.add(value);
            queryParams.put(key, list);
        }
        return this;
    }

    public Map<String, List<Object>> getQueryParams() {
        return queryParams;
    }
}
