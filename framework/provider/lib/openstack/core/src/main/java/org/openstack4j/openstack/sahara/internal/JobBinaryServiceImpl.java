package org.openstack4j.openstack.sahara.internal;

import org.openstack4j.api.sahara.JobBinaryService;
import org.openstack4j.core.transport.HttpEntityHandler;
import org.openstack4j.core.transport.HttpResponse;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.sahara.JobBinary;
import org.openstack4j.openstack.sahara.domain.SaharaJobBinary;
import org.openstack4j.openstack.sahara.domain.SaharaJobBinary.JobBinaries;
import org.openstack4j.openstack.sahara.domain.SaharaJobBinaryUnwrapped;

import java.io.InputStream;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Sahara Data Processing Operations
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */

public class JobBinaryServiceImpl extends BaseSaharaServices implements JobBinaryService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends JobBinary> list() {
        return get(JobBinaries.class, uri("/job-binaries")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobBinary get(String JobBinaryId) {
        checkNotNull(JobBinaryId);
        return get(SaharaJobBinary.class, uri("/job-binaries/%s", JobBinaryId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobBinary create(JobBinary jobBinary) {
        checkNotNull(jobBinary);
        SaharaJobBinaryUnwrapped unwrapped = new SaharaJobBinaryUnwrapped(jobBinary);
        return post(SaharaJobBinary.class, uri("/job-binaries"))
                .entity(unwrapped)  // setup request
                .execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String JobBinaryId) {
        checkNotNull(JobBinaryId);
        return deleteWithResponse(uri("/job-binaries/%s", JobBinaryId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Payload<InputStream> getData(String JobBinaryId) {
        HttpResponse response = get(Void.class, uri("/job-binaries/%s/data", JobBinaryId)).executeWithResponse();
        try {
            if (response.getStatus() < 400)
                return Payloads.create(response.getInputStream());
            return null;
        } finally {
            HttpEntityHandler.closeQuietly(response);
        }
    }

}
