package org.openstack4j.model.common.builder;

import org.openstack4j.model.common.BasicResource;

/**
 * Abstract Basic Resource Builder
 *
 * @param <M> the model type
 * @param <T> the builder type
 */
public abstract class BasicResourceBuilder<M extends BasicResource, T extends BasicResourceBuilder<M, T>> {

    /**
     * Sets the name on the resource
     *
     * @param name the name
     * @return the builder
     */
    public T name(String name) {
        reference().setName(name);
        return self();
    }

    /**
     * Sets the Id.
     *
     * @param id the identifier
     * @return the builder
     */
    public T id(String id) {
        reference().setId(id);
        return self();
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    protected abstract M reference();

}
