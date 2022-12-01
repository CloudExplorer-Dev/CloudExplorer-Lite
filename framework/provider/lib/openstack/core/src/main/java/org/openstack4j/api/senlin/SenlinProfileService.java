package org.openstack4j.api.senlin;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.senlin.Profile;
import org.openstack4j.model.senlin.ProfileCreate;

import java.util.List;

/**
 * This interface defines all methods for the manipulation of Profile
 *
 * @author lion
 */
public interface SenlinProfileService {

    /**
     * Gets a list of currently existing {@link Profile}s.
     *
     * @return the list of {@link Profile}s
     */
    List<? extends Profile> list();

    /**
     * <code>POST /v1/profiles</code><br \>
     * <p>
     * Creates a new {@link Profile} out of a {@link ProfileCreate} object
     *
     * @param newProfile {@link ProfileCreate} object out of which stack is to be created
     * @return new {@link Profile} as returned from the server
     */
    Profile create(ProfileCreate newProfile);

    /**
     * returns details of a {@link Profile}.
     *
     * @param profileID Id of {@link Profile}
     * @return Profile
     */
    Profile get(String profileID);

    /**
     * <code>PATCH /v1/profiles/​{profile_id}</code><br \>
     * <p>
     * Update a {@link Profile} out of a {@link ProfileCreate} object
     *
     * @param profileID  Id of {@link Profile}
     * @param newProfile {@link ProfileCreate} object out of which stack is to be update
     * @return new {@link Profile} as returned from the server
     */
    Profile update(String profileID, ProfileCreate newProfile);

    /**
     * Deletes the specified {@link ActionResponse} from the server.
     *
     * @param profileID Id of {@link ActionResponse}
     * @return the action response
     */
    ActionResponse delete(String profileID);
}
