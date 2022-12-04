package org.openstack4j.openstack.sahara.internal;

import org.openstack4j.api.sahara.JobService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.sahara.Job;
import org.openstack4j.model.sahara.JobConfigHint;
import org.openstack4j.openstack.sahara.domain.SaharaJob;
import org.openstack4j.openstack.sahara.domain.SaharaJob.Jobs;
import org.openstack4j.openstack.sahara.domain.SaharaJobConfigHint;
import org.openstack4j.openstack.sahara.domain.SaharaJobUnwrapped;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Sahara Data Processing Operations
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public class JobServiceImpl extends BaseSaharaServices implements JobService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Job> list() {
        return get(Jobs.class, uri("/jobs")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job get(String JobId) {
        checkNotNull(JobId);
        return get(SaharaJob.class, uri("/jobs/%s", JobId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job create(Job job) {
        checkNotNull(job);
        SaharaJobUnwrapped unwrapped = new SaharaJobUnwrapped(job);
        return post(SaharaJob.class, uri("/jobs"))
                .entity(unwrapped)  // setup request
                .execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String JobId) {
        checkNotNull(JobId);
        return deleteWithResponse(uri("/jobs/%s", JobId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobConfigHint getConfigHint(String type) {
        checkNotNull(type);
        return get(SaharaJobConfigHint.class, uri("/jobs/config-hints/%s", type)).execute();
    }

}
