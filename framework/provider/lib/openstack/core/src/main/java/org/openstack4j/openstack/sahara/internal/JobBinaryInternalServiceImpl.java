package org.openstack4j.openstack.sahara.internal;

import org.openstack4j.api.sahara.JobBinaryInternalService;
import org.openstack4j.core.transport.HttpEntityHandler;
import org.openstack4j.core.transport.HttpResponse;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.sahara.JobBinaryInternal;
import org.openstack4j.openstack.sahara.domain.SaharaJobBinaryInternal;
import org.openstack4j.openstack.sahara.domain.SaharaJobBinaryInternal.JobBinaryInternals;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Sahara Data Processing Operations
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public class JobBinaryInternalServiceImpl extends BaseSaharaServices implements JobBinaryInternalService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends JobBinaryInternal> list() {
        return get(JobBinaryInternals.class, uri("/job-binary-internals")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobBinaryInternal get(String jobBinaryInternalId) {
        checkNotNull(jobBinaryInternalId);
        return get(SaharaJobBinaryInternal.class, uri("/job-binary-internals/%s", jobBinaryInternalId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String jobBinaryInternalId) {
        checkNotNull(jobBinaryInternalId);
        return deleteWithResponse(uri("/job-binary-internals/%s", jobBinaryInternalId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobBinaryInternal create(Payload<File> payload) {
        checkNotNull(payload);
        return put(SaharaJobBinaryInternal.class, uri("/job-binary-internals/%s", payload.getRaw().getName()))
                .entity(payload)
                .execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Payload<InputStream> getData(String jobBinaryInternalId) {
        HttpResponse response = get(Void.class, uri("/job-binary-internals/%s/data", jobBinaryInternalId)).executeWithResponse();
        try {
            if (response.getStatus() < 400)
                return Payloads.create(response.getInputStream());
            return null;
        } finally {
            HttpEntityHandler.closeQuietly(response);
        }
    }

}
