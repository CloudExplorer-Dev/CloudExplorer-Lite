package org.openstack4j.model.compute;

/**
 * Host Resource Body contains Nova host resource
 *
 * @author Qin An
 */
public interface HostResourceBody {

    /**
     * @return the Host Resource contained in the unamed body
     */
    public HostResource getHostResource();

}
