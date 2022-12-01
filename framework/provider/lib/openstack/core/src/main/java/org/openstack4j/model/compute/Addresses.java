package org.openstack4j.model.compute;

import org.openstack4j.model.ModelEntity;

import java.util.List;
import java.util.Map;

/**
 * A container which holds networks with 1 or more addresses
 *
 * @author Jeremy Unruh
 */
public interface Addresses extends ModelEntity {

    /**
     * Adds an address to the given network type
     *
     * @param key   the type of address classification
     * @param value of the address
     */
    void add(String key, Address value);

    /**
     * @return the a Map<String, List<Address>
     */
    Map<String, List<? extends Address>> getAddresses();

    /**
     * Gets the addresses associated with the given network type
     *
     * @param type the type of network
     * @return the List of Addresses
     */
    List<? extends Address> getAddresses(String type);

}
