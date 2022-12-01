package org.openstack4j.api.senlin;

import org.openstack4j.model.senlin.ProfileType;

import java.util.List;

/**
 * This interface defines all methods for the manipulation of ProfileType
 *
 * @author lion
 */
public interface SenlinProfileTypeService {

    /**
     * Gets a list of currently existing {@link ProfileType}s.
     *
     * @return the list of {@link ProfileType}s
     */
    List<? extends ProfileType> list();

    /**
     * returns details of a {@link ProfileType}.
     *
     * @param profileTypeName Name of {@link ProfileType}
     * @return ProfileType
     */
    ProfileType get(String profileTypeName);
}
