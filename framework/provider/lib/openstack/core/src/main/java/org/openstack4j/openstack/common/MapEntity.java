package org.openstack4j.openstack.common;

import org.openstack4j.model.ModelEntity;

import java.util.HashMap;

/**
 * Simple name value pair based Entity
 *
 * @author Jeremy Unruh
 */
public class MapEntity extends HashMap<String, Object> implements ModelEntity {

    private static final long serialVersionUID = 1L;

    public static MapEntity create(String key, Object value) {
        return new MapEntity().add(key, value);
    }

    public MapEntity add(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
