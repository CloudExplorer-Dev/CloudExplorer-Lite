package org.openstack4j.api;

/**
 * To keep our dependencies simple in the current Openstack4J, we utilize ServiceLoader to load a provider who is responsible
 * for loading the implementation for any of the defined API interfaces.  This allows us to avoid pulling in extra 3rd party
 * dependencies such as Guice, Inject, etc.  It also allows us flexibility on the provider which may be overriden and choose to bind
 * modules and simple delegate out the {@link #get(Class)} calls
 *
 * @author Jeremy Unruh
 */
public interface APIProvider {

    /**
     * Called after the APIProvider is constructed.  This allows the provider to pre-initialize or bind any interface implementations if desired
     */
    void initialize();

    /**
     * Gets the implementation for the specified interface type
     *
     * @param <T> the Openstack4j API type
     * @param api the API interface
     * @return the implementation for T
     * @throws ApiNotFoundException if the API cannot be found
     */
    <T> T get(Class<T> api);
}
