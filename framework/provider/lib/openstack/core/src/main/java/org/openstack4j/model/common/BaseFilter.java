package org.openstack4j.model.common;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Base Filter class for building Filter Request options
 *
 * @author Jeremy Unruh
 */
public class BaseFilter {

    private Map<String, Object> constraints = Maps.newHashMap();

    protected BaseFilter() {

    }

    protected void filter(String name, Object value) {
        constraints.put(name, value);
    }

    public Map<String, Object> getConstraints() {
        return constraints;
    }

}
