package org.openstack4j.openstack.common;


import org.openstack4j.model.ModelEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple name value pair based Entity
 *
 * @author Jeremy Unruh
 */
public class ListEntity<T> extends ArrayList<T> implements ModelEntity {

    private static final long serialVersionUID = 1L;

    /*
    public static ListEntity create(String key, Object value) {
        return new ListEntity().add(key, value);
    }
    */

    public ListEntity() {
        super();
    }

    public ListEntity(List<T> list) {
        super(list);
    }


    public boolean add(T value) {
        return super.add(value);
        //super.put(value);
        //return this;
    }

}
