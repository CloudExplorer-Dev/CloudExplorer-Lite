package org.openstack4j.openstack.common;

import com.google.common.base.MoreObjects;
import org.openstack4j.model.common.BasicResource;

/**
 * Basic Id/Name based Entity Model implementation
 *
 * @author Jeremy Unruh
 */
public class BasicResourceEntity extends IdResourceEntity implements BasicResource {

    private static final long serialVersionUID = 1L;

    private String name;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass()).omitNullValues()
                .add("id", getId()).add("name", name)
                .toString();
    }
}
