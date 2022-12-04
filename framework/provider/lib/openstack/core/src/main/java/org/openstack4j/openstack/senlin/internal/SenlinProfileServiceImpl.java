package org.openstack4j.openstack.senlin.internal;

import org.openstack4j.api.senlin.SenlinProfileService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.senlin.Profile;
import org.openstack4j.model.senlin.ProfileCreate;
import org.openstack4j.openstack.senlin.domain.SenlinProfile;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class contains getters for all implementation of the available profile services
 *
 * @author lion
 */
public class SenlinProfileServiceImpl extends BaseSenlinServices implements SenlinProfileService {

    @Override
    public List<? extends Profile> list() {
        return get(SenlinProfile.Profile.class, uri("/profiles")).execute().getList();
    }

    @Override
    public Profile create(ProfileCreate newProfile) {
        checkNotNull(newProfile);
        return post(SenlinProfile.class, uri("/profiles")).entity(newProfile).execute();
    }

    @Override
    public Profile get(String policyID) {
        checkNotNull(policyID);
        return get(SenlinProfile.class, uri("/profiles/%s", policyID)).execute();
    }

    @Override
    public Profile update(String policyID, ProfileCreate newPolicy) {
        checkNotNull(policyID);
        checkNotNull(newPolicy);
        return patch(SenlinProfile.class, uri("/profiles/%s", policyID)).entity(newPolicy).execute();
    }

    @Override
    public ActionResponse delete(String policyID) {
        checkNotNull(policyID);
        return deleteWithResponse(uri("/profiles/%s", policyID)).execute();
    }
}
